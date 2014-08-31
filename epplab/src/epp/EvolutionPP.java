package epp;

import epp_ag.ChromosomeProj;
import epp_ag.GenomeProj;
import epp_pso.Essaim;
import epp_pso.tribe.EssaimTribe;
import ihm.Recherche;
import indice.Discriminant;
import indice.Friedman;
import indice.FriedmanTukey;
import indice.Indice;
import indice.Indice4;
import indice.KurtosisMax;
import indice.KurtosisMin;
import indice.StahelDonoho;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import javalain.algorithmegenetique.Population;
import javalain.ea.Evolution;
import javalain.math.Calcul;
import javalain.math.PseudoRandomNumbers;
import math.Matrice;
import util.FileUtils;
import util.GuiUtils;
import util.MsgUtils;
import util.StringUtils;


/*--------------------*/
/* Classe EvolutionPP */
/*--------------------*/
/**
 * La classe <code>EvolutionPP</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert, Ingrid Griffit
 * @version	2 novembre 2013
 */
public class EvolutionPP extends Evolution
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Paramètres de l'évolution -----*/
	private final ParametrePP parametre;

	/*----- Indice -----*/
	private Indice indice;

	/*----- Projection théorique -----*/
	private double[] projection_theorique;

	/*----- Buffer de sortie -----*/
	private StringBuilder sb;

	/*----- Compteur des itérations -----*/
	private int cpt_iteration;

	/*----- Convergence -----*/
	private double[] tabConvI;	// Save the index value for each iteration

	/*----- Compteur des simulations -----*/
	private int cpt_simulation;

	/*----- Nombre de jeux de données -----*/
	private int nb_de_jeu_de_donnees;

	/*----- Numéro du jeu de données -----*/
	private int cpt_jeu_de_donnees;

	/*----- Variables lors d'une simulation -----*/
	private double I;						// Enregistre le meilleure I de la simulation en cours
	private double[] A;						// Enregistre la meilleure projection de la simulation en cours

	/*----- Statistiques sur les simulations -----*/
	private double[] tabI;					// Enregistre le meilleur I de chaque simulation
	private double meilleurI;				// Enregistre le meilleur I de toutes les simulations
	private double meilleurCosinus;			// Enregistre le meilleur cosinus de toutes les simulations
	private double[] meilleureProjectionA;	// Enregistre la projection permettant d'obtenir le meilleur I de toutes les simulations

	private final ArrayList<OrdonnerPP> liste;	// Liste pour récupérer les I et les vecteurs associés dans le fichier résultat des simulations

	private long tempsEcoule;
	private long tempsEstime;

	/*----- Statistiques sur les jeux de données -----*/
	private double[] tabImax;		// Enregistre le meilleur I de chaque jeu de données
	private double[] tabCosImax;	// Enregistre le cosinus de la meilleure projection obtenue de chaque jeu de données
	private double[] tabCmax;		// Enregistre le meilleur cosinus de chaque jeu de données

	/*----- Population de l'AG, essaim de PSO et TRIBES-----*/
	private Population	pop;
	private Essaim		swarm;
	private	EssaimTribe	tribe;

	/*----- Algorithme MultiStart -----*/
	private double[] X_multistart;

	/**
	 * Lien avec l'interface.
	 */
	private Recherche frRecherche;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée et initialise l'évolution.
	 */
	public EvolutionPP (ParametrePP p)
		{
		this.parametre = p;

		this.liste = new ArrayList<OrdonnerPP>();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Attache une IHM à l'évolution.
	 */
	public void setFrRecherche (Recherche nr) { this.frRecherche = nr; }


	/**
	 * Calcul des données projetées pour faire l'affichage graphique.
	 */
	private void affProjection (double[] A)
		{
		double[] donnees = new double[this.indice.getMatrice().nbLigne()];

		for (int j=0; j<this.indice.getMatrice().nbLigne(); j++)
			donnees[j] = Calcul.produitScalaireVecteur(this.indice.getMatrice().ligne(j), A);

		this.frRecherche.setGraphique(donnees);
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Méthode qui s'exécute lors du lancement de la simulation.
	 */
	public void demarre ()
		{
		/**
		 * Initialisation des paramètres lors du tout premier lancement.
		 */
		if (this.cpt_iteration == 0 && this.cpt_simulation == 0)
			{
			/*----- Convergence parameters -----*/
			this.tabConvI = new double[this.parametre.getNbIterations()];

			this.parametre.tabConvSimu = new boolean[this.parametre.nbSimulation];

			this.parametre.tabNumIterReached = new int[this.parametre.nbSimulation];
			Arrays.fill(this.parametre.tabNumIterReached, -1);

			/*----- Raz de l'affichage graphique -----*/
			if (this.frRecherche != null) this.frRecherche.raz();

			/*----- Chemin du fichier de données -----*/
			String path = this.parametre.rep_donnees + File.separatorChar + this.parametre.ficherDeDonnees;

                        Matrice X;

                        if(EPPLab.runsInR==true){
                           // X = new Matrice(path, this.cpt_jeu_de_donnees);
                            X = new Matrice(EPPLab.dataRows,EPPLab.dataCols);
                            double tempValue;
                            int datPosition = 0;
                            for (int r=0; r<EPPLab.dataRows; r++){
                                for (int c=0; c<EPPLab.dataCols; c++){
                                    tempValue = EPPLab.dataValues[datPosition];
                                    X.setElement(r, c, tempValue);
                                    datPosition += 1;
                                }
                            }


                        } else {
			/*----- Matrice d'observations -----*/
			    X = new Matrice(path, this.cpt_jeu_de_donnees);

                        }


			/*----- Matrice de travail 'matrice' -----*/
			long start = System.currentTimeMillis();
			//System.out.print("Data transformation ...");

			Matrice matrice;
			if (this.parametre.sphere)
				{
				//System.out.print("calling function X.spherique ... ");

				/*----- Les données sont rendues sphériques -----*/
				if (this.parametre.ficherDeDonnees.equals("fiab_880.txt"))
					matrice = new Matrice(this.parametre.rep_donnees + File.separatorChar + "fiab_880_sphere.txt");
				else
					matrice = X.spherique();
				}
			else
				{
				//System.out.print("calling function X.centreReduite ...");
				/*----- Les données sont centrées et réduites -----*/
				matrice = X.centreReduite();
				}


			long stop = System.currentTimeMillis();
			//System.out.println(" ... finished (in " + StringUtils.convertHMSms(stop-start) + ")");

			matrice.setNom(this.parametre.ficherDeDonnees);

			/*----- Instanciation de l'indice -----*/
			if (this.parametre.nomIndice.equals(ParametrePP.KURTOSIS_MAX))			this.indice = new KurtosisMax(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.KURTOSIS_MIN))		this.indice = new KurtosisMin(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.FRIEDMAN))			this.indice = new Friedman(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.FRIEDMAN_TUKEY))	this.indice = new FriedmanTukey(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.DISCRIMINANT))		this.indice = new Discriminant(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.INDICE_4))			this.indice = new Indice4(matrice);
			else if (this.parametre.nomIndice.equals(ParametrePP.STAHEL_DONOHO))	this.indice = new StahelDonoho(matrice);

			/*----- Lors du premier jeu de données -----*/
			if (this.cpt_jeu_de_donnees == 0)
				{
				/**
				 * Lecture du fichier de données afin de connaitre :
				 *  - le nombre de jeux de données (matrices d'observations).
				 *  - la meilleure projection théorique (si indiquée).
				 */
                                if(EPPLab.runsInR==false)
                                  {
        				FileReader fichier;
        				BufferedReader buffer;
        				try	{
        					/*----- Fichier -----*/
        					fichier = new FileReader(path);

        					/*----- Buffer -----*/
        					buffer = new BufferedReader(fichier);

        					StringTokenizer st;
        					/*----- Lecture de la première ligne -----*/
        					st = new StringTokenizer(buffer.readLine());

        					/*----- Lecture du nombre de lignes du fichier -----*/
        					st.nextToken();

        					/*----- Lecture du nombre de colonnes (dimension) -----*/
        					int dim = Integer.parseInt(st.nextToken());

        					/*----- Lecture éventuelle du nombre de matrices contenues dans le fichier (jeu de test) -----*/
        					if (st.hasMoreTokens())
        						this.nb_de_jeu_de_donnees = Integer.parseInt(st.nextToken());
        					else
        						this.nb_de_jeu_de_donnees = 1;

					/*----- Lecture de la deuxième ligne (projection théorique) -----*/
        					st = new StringTokenizer(buffer.readLine());

        					if (st.countTokens() != 0)
        						{
        						/*----- La projection théorique est dans le fichier -----*/
        						if (st.countTokens() == dim)
        							{
        							this.projection_theorique = new double[dim];

        							int cpt = 0;
        							while (st.hasMoreTokens()) this.projection_theorique[cpt++] = Double.parseDouble(st.nextToken());
        							}
        						else
        							{
        							MsgUtils.erreurAndExit(null,
        												   "Lecture du fichier '" + path + "'.\nLa dimension de la projection théorique n'est pas égale à la dimension de l'espace.",
        												   "Programme principal");
        							}
        						}
					else
						/*----- Pas de projection théorique -----*/
						this.projection_theorique = null;
        					}
        				catch (FileNotFoundException fnfe)
        					{
        					MsgUtils.erreurAndExit(null,
        										   "Le fichier '" + path + "' est introuvable.",
        										   "Programme principal");
        					}
        				catch (IOException ioe)
        					{
        					MsgUtils.erreurAndExit(null,
        										   "Problème lors de la lecture du fichier '" + path + "'.",
        										   "Programme principal");
        					}
                                  }
				/*----- Initialisation des statistiques sur les jeux de données -----*/
				this.tabImax	= new double[this.nb_de_jeu_de_donnees];
				this.tabCosImax	= new double[this.nb_de_jeu_de_donnees];
				this.tabCmax	= new double[this.nb_de_jeu_de_donnees];

				/*----- Sortie fichier (Entête) -----*/
				sb = new StringBuilder();
				sb.append("<Date_heure>               ").append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date())).append('\n');
				sb.append("<Jeu_de_donnees>           ").append(this.parametre.ficherDeDonnees).append('\n');
				sb.append("<Dimension_matrice>        ").append(X.nbLigne()).append(" ").append(X.nbColonne()).append('\n');
				sb.append("<Transformation_donnees>   ").append(this.parametre.sphere ? ParametrePP.SPHERIQUE : ParametrePP.CENTREE_REDUITE).append('\n');
				sb.append("<Indice>                   ").append(this.indice.getClass().getSimpleName()).append('\n');
				sb.append("<Dimension_de_projection>  ").append(this.parametre.dimensionProjection).append('\n');
				sb.append("<Technique_bimensionnelle> ").append((this.parametre.dimensionProjection == 2 ? "?" : "?")).append('\n');
				sb.append("<Projection_theorique>     ").append((this.projection_theorique != null) ? EvolutionPP.afficheVecteur(this.projection_theorique) : "?").append('\n');
				sb.append("<Valeur_de_I>              ").append((this.projection_theorique != null) ? GuiUtils.DECIMAL_9.format(this.indice.calcul(this.projection_theorique)) : "?").append('\n');
				sb.append("<Methode>                  ").append(this.parametre.methode).append('\n');
				sb.append("<Nombre_d_iterations>      ").append(this.parametre.getNbIterations()).append('\n');
				sb.append("<Nombre_d_individus>       ").append(this.parametre.methode.equals(ParametrePP.TRIBES) ? "?" : this.parametre.getNbIndividus()).append('\n');
				if (this.parametre.methode.equals(ParametrePP.GA))		sb.append(this.parametre.pag); else
				if (this.parametre.methode.equals(ParametrePP.PSO))		sb.append(this.parametre.ppso); else
				if (this.parametre.methode.equals(ParametrePP.TRIBES))	sb.append(this.parametre.ptribe);
				sb.append("\n");
				}

			/*----- Initialisation des statistiques sur les simulations -----*/
			this.tabI = new double[this.parametre.nbSimulation];

			this.meilleurI				= Double.NEGATIVE_INFINITY;
			this.meilleurCosinus		= -1.0;
			this.meilleureProjectionA	= null;

			this.liste.clear();

			/*----- Sortie écran -----*/
			//System.out.println("Starting the calculations...");

			/*----- Sortie fichier -----*/
			sb.append("<Jeu_de_donnees_").append(this.cpt_jeu_de_donnees).append(">\n");
			/**
			 * On positionne une balise à la place du nombre initial de
			 * lancements qui sera remplacée en fin des simulations sur
			 * le jeu de données.
			 * Cela afin de prendre en compte un arrêt volontaire de
			 * l'utilisateur.
			 */
			sb.append("<Nombre_de_simulations>    ").append("<nbSimulations>").append("\n");

			/*----- Temps -----*/
			this.tempsEcoule = 0;
			this.tempsEstime = 0;
			}


		/**
		 * A chaque lancement de simulation.
		 */
		/*----- Affichage graphique -----*/
		if (this.frRecherche != null) this.frRecherche.getCourbeConvergence().raz();

		/*----- Sortie écran -----*/
		System.out.print("Simulation " + cpt_simulation) ;

            	/*----- Sortie fichier -----*/
		sb.append("<Simulation_").append(cpt_simulation).append(">\n");

		/*----- Initialisation des méthodes -----*/
		if (this.parametre.methode.equals(ParametrePP.GA))
			{
			this.pop = new Population(new GenomeProj(this.indice), this.parametre.getNbIndividus(), this.parametre.pag);
                        this.pop.initialise();
			}
		else
		if (this.parametre.methode.equals(ParametrePP.PSO))
			{
			this.swarm = new Essaim(this.parametre.ppso, this.indice, this.parametre.getNbIndividus());
			}
		else
		if (this.parametre.methode.equals(ParametrePP.TRIBES))
			{
			/*----- Essaim -----*/
			this.tribe = new EssaimTribe(this.indice);

			/*----- Début de l'évolution -----*/
			while (this.cpt_iteration < 2) // A faire - Séparer ces boucles du compteur cpt_iteration
				{
				/*----- Génération de nouvelles tribus : 2ième et 3ième tribus -----*/
				this.tribe.creationTribu(this.indice);

				/*----- Première itération-----*/
//				this.tribe.calculMeilleurePosition();
//				this.tribe.calculNouvelleProjection(false, 0);
				this.tribe.updatePbest();
				this.tribe.updatePosition(false, 0);

				this.cpt_iteration++;
				}

			/*----- Première adaptation -----*/
			if (!this.tribe.premiereAdaptation())
				this.tribe.creationTribu(this.indice);
			}
		else
		if (this.parametre.methode.equals(ParametrePP.MULTISTART))
			{
			/*----- Initialisation du vecteur de départ -----*/
			this.X_multistart = new double[this.indice.getNombreParametres()];

			for (int i=0; i<this.indice.getNombreParametres(); i++)
				this.X_multistart[i] = PseudoRandomNumbers.random()*2 - 1;

			this.X_multistart = Calcul.normaliseVecteur(this.X_multistart);
			}
		}


	/**
	 * Méthode qui permet de passer à l'étape suivante du processus
	 * d'<code>Evolution</code>. Elle s'exécute si la méthode
	 * <code>conditionEvolution()</code> a renvoyé <code>true</code>.
	 */
	public void itere ()
		{
		/*----- Sortie écran -----*/
//		System.out.print(this.cpt_iteration + " ");

		/*----- Itération -----*/
		if (this.parametre.methode.equals(ParametrePP.GA))
			{
			/*----- GA -----*/
			this.pop.generation();

			/*----- Meilleur I et meilleure projection de l'itération -----*/
			this.I = this.pop.getIndividu(this.pop.getPositionMax()).getNote();
			this.A = ((ChromosomeProj)this.pop.getIndividu(this.pop.getPositionMax()).getChromosome(0)).getVecteurProjectionNormalise();
			}
		else
		if (this.parametre.methode.equals(ParametrePP.PSO))
			{
			/*----- PSO -----*/
			this.swarm.calculMeilleurePosition();
			this.swarm.calculNouvellePosition();

			/*----- Meilleur I et meilleure projection de l'itération -----*/
			this.I = this.swarm.getMeilleureParticule().meilleurI;
			this.A = this.swarm.getMeilleureParticule().meilleureProjection;
			}
		else
		if (this.parametre.methode.equals(ParametrePP.TRIBES))
			{
			/*----- TRIBES -----*/
//			this.tribe.calculMeilleurePosition();
//			this.tribe.calculNouvelleProjection(false, 0);
			this.tribe.updatePbest();
			this.tribe.updatePosition(false, 0);

			/*----- Vérifier si adaptation-----*/
			if (this.cpt_iteration == this.tribe.nbLiens()/2)
				this.tribe.adaptation(this.indice);

			/*----- Meilleur I et meilleure projection de l'itération -----*/
			this.I = this.tribe.getBestParticle().meilleurI;
			this.A = this.tribe.getBestParticle().meilleureProjection;
			}
		else
		if (this.parametre.methode.equals(ParametrePP.MULTISTART))
			{
			/*----- Descente du gradient -----*/
			double[] gradient = ((KurtosisMin)this.indice).gradient(this.X_multistart);
			gradient = Calcul.normaliseVecteur(gradient);

			this.X_multistart = Calcul.differenceVecteur(this.X_multistart, Calcul.multReelVecteur(0.1, gradient));
			this.X_multistart = Calcul.normaliseVecteur(this.X_multistart);

			/*----- Meilleur I et meilleure projection de l'itération -----*/
			this.I = this.indice.calcul(this.X_multistart);
			this.A = this.X_multistart;
			}

		/*----- Sortie fichier (Pour un nombre de simulations <= 10 alors on conserve l'historique d'évolution de I) -----*/
		if (this.parametre.nbSimulation <= 10) sb.append(GuiUtils.DECIMAL_9.format(this.I)).append("\n");

		/**
		 * Mise à jour de l'affichage graphique à chaque itération.
		 */
		if (this.frRecherche != null)
			{
			/*----- Affichage des nombres de simulations et d'itérations -----*/
			this.frRecherche.setSimuIterEnCours(this.cpt_simulation, this.cpt_iteration);

			/*----- Si première simulation alors on calcule le temps estimé en fonction de la durée moyenne des itérations -----*/
			if (this.cpt_simulation == 0)
				this.tempsEstime = this.getElapsedTime()*this.parametre.getNbIterations()*this.parametre.nbSimulation/(this.cpt_iteration+1);

			/*----- Affichage du temps (si 1 seconde dépassée) -----*/
			if (this.tempsEcoule + this.getElapsedTime() > 1000)
				this.frRecherche.setAffTemps(this.tempsEcoule + this.getElapsedTime(), this.tempsEstime);

			/*----- Ajout du meilleur I à la courbe de convergence -----*/
			this.frRecherche.getCourbeConvergence().addNombre((float)this.I);

			/*----- Affichage des données projetées en fonction de A -----*/
			if (this.frRecherche.isVisuIteration())
				this.affProjection(this.A);
			}
		}


	/**
	 * Méthode qui s'exécute lorsque l'<code>Evolution</code> se termine.
	 */
	public void termine ()
		{
		/**
		 * Si l'état précédent est différent de l'état 'exécutable' alors
		 * on effectue les instructions de terminaison d'une simulation (lancement).
		 */
		if (this.getEtatPrecedent() != Evolution.EXECUTABLE)
			{
			/*----- Temps écoulé -----*/
			this.tempsEcoule += this.getElapsedTime();

			/*----- Calcul du temps estimé en fonction de la durée moyenne des simulations -----*/
			this.tempsEstime = this.parametre.nbSimulation*this.tempsEcoule/(this.cpt_simulation+1);

			/*----- Affichage graphique -----*/
			if (this.frRecherche != null && this.frRecherche.isVisuLancement())
				this.affProjection(this.A);

			/*----- Statistiques : enregistrement du meilleur I et de la meilleure projection de la simulation -----*/
			this.tabI[this.cpt_simulation] = this.I;

			if (this.I > this.meilleurI)
				{
				this.meilleurI = this.I;
				this.meilleureProjectionA = this.A;
				}

			this.liste.add(new OrdonnerPP(this.cpt_simulation, this.I, this.A));

			/*----- Sortie fichier -----*/
			if (this.parametre.nbSimulation == 1) sb.append("\n");
			sb.append("<I> ").append(GuiUtils.DECIMAL_9.format(this.I)).append("\n");
			sb.append("<A> ").append(EvolutionPP.afficheVecteur(this.A)).append("\n");


			/*----- Si la projection théorique est connue -----*/
			if (this.projection_theorique != null)
				{
				double cos = Math.abs(Calcul.cosinus(this.projection_theorique, this.A));

				if (cos > this.meilleurCosinus)
					this.meilleurCosinus = cos;

				sb.append("<COS> ").append(cos).append("\n");
				}

			sb.append("</Simulation_").append(cpt_simulation).append(">\n\n");

			/*----- Sortie écran -----*/
			System.out.print("... finished (I " + GuiUtils.DECIMAL_9.format(this.I) + " in " + GuiUtils.DECIMAL_3.format(this.getElapsedTime()/1000.0) + "s)");

//			System.out.print(" Nb éval : " + this.indice.getNbEvaluation());
//			if (this.parametre.methode == ParametrePP.TRIBES) System.out.print(" Nb de tribu : " + this.tribe.getNbTribus());

			System.out.println("");
			}


		/**
		 * L'utilisateur n'a pas stoppé l'exécution et
		 * toutes les simulations n'ont pas encore été effectuées...
		 * ... donc on continue les simulations.
		 */
		if (!this.isStopUser() && (this.cpt_simulation+1) < this.parametre.nbSimulation)
			{
			this.cpt_simulation++;

			if (this.frRecherche != null && this.frRecherche.isStepByStep())
				{
				this.setEtat(Evolution.EXECUTABLE);
				this.frRecherche.endStepByStep();
				}
			else
				this.setEtat(Evolution.DEMARRE);
			}

		/**
		 * L'utilisateur a stoppé ou toutes les simulations ont été effectuées.
		 */
		else
			{
			/**
			 * Statistiques sur les simulations.
	 		 */

			/*----- Sortie écran -----*/
			//System.out.println("----------");

			/*----- Affichage graphique -----*/
			if (this.frRecherche != null && this.frRecherche.isVisuFin())
				this.affProjection(this.meilleureProjectionA);

			/*----- Sortie fichier des statistiques sur les simulations -----*/
			double somme = this.tabI[0];
			for (int i=1; i<=this.cpt_simulation; i++) somme += this.tabI[i];

			sb.append("<Valeur_de_I_moyenne>      ").append(GuiUtils.DECIMAL_9.format((somme/(this.cpt_simulation+1)))).append("\n");
			sb.append("<Valeur_de_I_maximale>     ").append(GuiUtils.DECIMAL_9.format(this.meilleurI)).append("\n");

			sb.append("<Projection_A>             ").append(EvolutionPP.afficheVecteur(this.meilleureProjectionA)).append("\n");

			/*----- Si la projection théorique est connue -----*/
			if (this.projection_theorique != null)
				sb.append("<Valeur_du_COS_max>        ").append(this.meilleurCosinus).append("\n");

			sb.append("\n");
			sb.append("<Temps_total>              ").append(StringUtils.convertHMSms(this.tempsEcoule)).append("\n");
			sb.append("<Temps_moyen_simulation>   ").append(StringUtils.convertHMSms(this.tempsEcoule/(this.cpt_simulation+1))).append("\n\n");

			sb.append("<NbEVal> ").append(this.indice.getNbEvaluation()).append("\n");


			/**
			 * Mesures de qualité.
			 */
			if (true && this.liste.size() > 1)
				{
				/*----- Calcul des observations atypiques -----*/
				int nbEC = 3;
				boolean[][] isAtypiques = new boolean[this.liste.size()][this.indice.getMatrice().nbLigne()];

				int nbObs = this.indice.getMatrice().nbLigne();
				int nbLancement = this.liste.size();

				if (this.indice != null && this.liste != null)
					{
					double ecartType;
					double moyenne;
					double[] donnees_x = new double[this.indice.getMatrice().nbLigne()];

					for (int i=0; i<nbLancement; i++)
						{
						/*----- Projection des observations sur le résultat de la simulation 'i' -----*/
						for (int j=0; j<nbObs; j++)
						donnees_x[j] = Calcul.produitScalaireVecteur(this.indice.getMatrice().ligne(j), this.liste.get(i).getA());

						/*----- Calcul de l'écart-type et de la moyenne -----*/
						ecartType = Calcul.ecartType(donnees_x);
						moyenne = Calcul.moyenne(donnees_x);

						/*----- Teste si la valeur projetée est atypique -----*/
						for (int j=0; j<nbObs; j++)
							isAtypiques[i][j] = (donnees_x[j] < (moyenne - ecartType * nbEC)) || (donnees_x[j] > (moyenne + ecartType * nbEC));
						}
					}

				int ind_atypique = nbObs*95/100;
				int demiNbLancement = nbLancement/2;

				/*----- M1 -----*/
				int M1 = 0;
				double P1 = 0;
				for (int i=0; i<nbLancement; i++)
					for (int j=ind_atypique; j<nbObs; j++)
						if (isAtypiques[i][j]) M1++;

				P1 = 100 * M1/(nbLancement*(nbObs-ind_atypique)); // Valeur entière

				/*----- M3 -----*/
				int M3 = 0;
				double P3 = 0;
				for (int i=0; i<nbLancement; i++)
					for (int j=0; j<ind_atypique; j++)
						if (isAtypiques[i][j]) M3++;

				P3 = 100 * M3/(nbLancement*ind_atypique); // Valeur entière

				/*----- Tri -----*/
				Collections.sort(liste);

				/*----- M2 -----*/
				int M2 = 0;
				double P2 = 0;
				for (int i=0; i<demiNbLancement; i++)
					for (int j=ind_atypique; j<nbObs; j++)
						if (isAtypiques[i][j]) M2++;

				P2 = 100 * M2/(demiNbLancement*(nbObs-ind_atypique)); // Valeur entière

				/*----- M4 -----*/
				int M4 = 0;
				double P4 = 0;
				for (int i=0; i<demiNbLancement; i++)
					for (int j=0; j<ind_atypique; j++)
						if (isAtypiques[i][j]) M4++;

				P4 = 100 * M4/(demiNbLancement*ind_atypique); // Valeur entière

				//System.out.println("M1 = " + M1 + " (" + P1 + " %)");
				//System.out.println("M2 = " + M2 + " (" + P2 + " %)");
				//System.out.println("M3 = " + M3 + " (" + P3 + " %)");
				//System.out.println("M4 = " + M4 + " (" + P4 + " %)");

				sb.append("<M1> ").append(M1).append(" ").append(P1).append("\n");
				sb.append("<M2> ").append(M2).append(" ").append(P2).append("\n");
				sb.append("<M3> ").append(M3).append(" ").append(P3).append("\n");
				sb.append("<M4> ").append(M4).append(" ").append(P4).append("\n\n");
				}

			/*----- Fin du fichier -----*/
			sb.append("<Les_I>\n");
			for (int i=0; i<=this.cpt_simulation; i++)
				sb.append(GuiUtils.DECIMAL_9.format(this.tabI[i])).append("\n");
			sb.append("</Les_I>\n");
			sb.append("</Jeu_de_donnees_").append(this.cpt_jeu_de_donnees).append(">\n\n");

			/*----- Enregistrement du meilleur I pour le jeu de données -----*/
			if(EPPLab.runsInR==false) this.tabImax[this.cpt_jeu_de_donnees] = this.meilleurI;

			/*----- Si la projection théorique est connue -----*/
			if (this.projection_theorique != null)
				{
				/*----- Cosinus de la meilleure projection (meilleur I) -----*/
				this.tabCosImax[this.cpt_jeu_de_donnees] = Math.abs(Calcul.cosinus(this.projection_theorique, this.meilleureProjectionA));

				/*----- Meilleur cosinus -----*/
				this.tabCmax[this.cpt_jeu_de_donnees] = this.meilleurCosinus;
				}


			/**
			 * L'utilisateur n'a pas stoppé l'exécution et
			 * tous les jeux de données ne sont été pas traités.
			 */
			if (!this.isStopUser() && (this.cpt_jeu_de_donnees+1) < this.nb_de_jeu_de_donnees)
				{
				cpt_jeu_de_donnees++;

				/*----- Relance la simulation -----*/
				this.setEtat(DEMARRE);
				}
			else
				{
				/**
				 * Statistiques sur les jeux de données.
		 		 */
				sb.append("<Stats_jeux_de_donnees>\n");
				sb.append("<Meilleurs_I>\n");
                                if(EPPLab.runsInR==false)
                                {
                                    for (int i=0; i<=this.cpt_jeu_de_donnees; i++)
                                            sb.append(GuiUtils.DECIMAL_9.format(this.tabImax[i])).append("\n");
                                }
				sb.append("</Meilleurs_I>\n");

				/*----- Si la projection théorique est connue -----*/
				if (this.projection_theorique != null)
					{
					sb.append("<Cosinus_I_max>\n");
					for (int i=0; i<=this.cpt_jeu_de_donnees; i++)
						sb.append(GuiUtils.DECIMAL_9.format(this.tabCosImax[i])).append("\n");
					sb.append("</Cosinus_I_max>\n");

					sb.append("<Meilleurs_Cos>\n");
					for (int i=0; i<=this.cpt_jeu_de_donnees; i++)
						sb.append(GuiUtils.DECIMAL_9.format(this.tabCmax[i])).append("\n");
					sb.append("</Meilleurs_Cos>\n");
					}

				sb.append("</Stats_jeux_de_donnees>\n");

                               // Add also the numer of iterations to the output:
                		sb.append("<Number_of_iterations> ");
		         	for (int i=0; i<=this.cpt_simulation; i++)
                                       sb.append(this.parametre.tabNumIterReached[i]).append(" ");
				sb.append("</Number_of_iterations>\n");

                               // Add also the numer of iterations to the output:
                		sb.append("<Has_converged> ");
		         	for (int i=0; i<=this.cpt_simulation; i++)
                                       sb.append(this.parametre.tabConvSimu[i]).append(" ");
				sb.append("</Has_converged>\n");

                                
                                
				/**
				 * Remplacement
				 */
				sb = StringUtils.replaceAll(sb, "<nbSimulations>", "" + (this.cpt_simulation+1));


				/**
				 * Enregistrement des résultats dans un fichier temporaire. // A faire - Il faudra changer le sens du nom du fichier
				 */															// don1_fried_ga_100_
				String nomFichierSortie = "simu_" + (this.cpt_simulation+1) + "_";

				if (this.parametre.nomIndice.equals(ParametrePP.FRIEDMAN))				nomFichierSortie += "F_";
				else if (this.parametre.nomIndice.equals(ParametrePP.FRIEDMAN_TUKEY))	nomFichierSortie += "FT_";
				else if (this.parametre.nomIndice.equals(ParametrePP.KURTOSIS_MAX))		nomFichierSortie += "KurtoMax_";
				else if (this.parametre.nomIndice.equals(ParametrePP.KURTOSIS_MIN))		nomFichierSortie += "KurtoMin_";
				else if (this.parametre.nomIndice.equals(ParametrePP.DISCRIMINANT))		nomFichierSortie += "I3_";
				else if (this.parametre.nomIndice.equals(ParametrePP.INDICE_4))			nomFichierSortie += "I4_";
				else if (this.parametre.nomIndice.equals(ParametrePP.STAHEL_DONOHO))	nomFichierSortie += "SD_";

				if (this.parametre.methode.equals(ParametrePP.GA))				nomFichierSortie += "GA_";
				else if	(this.parametre.methode.equals(ParametrePP.PSO))		nomFichierSortie += "PSO_";
				else if	(this.parametre.methode.equals(ParametrePP.TRIBES))		nomFichierSortie += "TRIBE_";
				else if (this.parametre.methode.equals(ParametrePP.MULTISTART))	nomFichierSortie += "MULTISTART_";

				nomFichierSortie += this.parametre.dimensionProjection + "_";
				nomFichierSortie += this.parametre.ficherDeDonnees;

				/*----- Enregistrement -----*/

				// Modified by Alain !!!
				// FileUtils.saveStringToFich(this.parametre.rep_sortie, nomFichierSortie + ".temp", sb);
				reportToR.setResult(sb.toString());

				/**
				 * Informe l'IHM que l'évolution est terminée.
				 */
				if (this.frRecherche != null)
					this.frRecherche.termine(nomFichierSortie);

				else
					/**
					 * Si aucune IHM attachée.
					 */
					{
					/*----- On renomme le fichier de sortie -----*/
                                        File f_temp = new File(this.parametre.rep_sortie + File.separatorChar + nomFichierSortie + ".temp");

					nomFichierSortie = FileUtils.creeNomFichier(this.parametre.rep_sortie, nomFichierSortie);
					f_temp.renameTo(new File(this.parametre.rep_sortie + File.separatorChar + nomFichierSortie));

					//System.out.println("Calculation done...");
					//System.out.println("----------");

					/*----- On termine l'exécution -----*/
//					System.exit(0);
					}

				/*----- Remise à zéro du compteur de jeu de données -----*/
				this.cpt_jeu_de_donnees = 0;
				}

			/*----- Remise à zéro du compteur de simulation -----*/
			this.cpt_simulation = 0;
			}

		/*----- Remise à zéro du compteur des itérations -----*/
		this.cpt_iteration = 0;
		}


	/**
	 * Condition d'arrêt de l'<code>Evolution</code>.
	 */
	public boolean conditionEvolution ()
		{
		/**
		 * Convergence.
		 */
		boolean converge = false;

		if (this.parametre.step_iter > -1 && this.cpt_iteration > 0)
			{
			/*----- Save the value of I -----*/
			this.tabConvI[this.cpt_iteration-1] = this.I;

			if (this.cpt_iteration > this.parametre.step_iter)
				converge = this.parametre.eps > Math.abs((this.tabConvI[this.cpt_iteration-1]-this.tabConvI[this.cpt_iteration-1-this.parametre.step_iter])/this.tabConvI[this.cpt_iteration-1-this.parametre.step_iter]);
			}


		/**
		 * Stop ?
		 */
		if (this.cpt_iteration < this.parametre.getNbIterations() && !converge)
			{
			this.cpt_iteration++;

			return true;
			}
		else
			{
			/*----- Convergence parameters -----*/
			this.parametre.tabNumIterReached[this.cpt_simulation] = this.cpt_iteration;

			this.parametre.tabConvSimu[this.cpt_simulation] = converge;

			return false;
			}
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Retourne la représentation 'caractères' d'un vecteur.
	 */
	public static StringBuilder afficheVecteur(double[] a)
		{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i<a.length-1; i++)
			sb.append(GuiUtils.DECIMAL_9.format(a[i])).append(" ");

		sb.append(GuiUtils.DECIMAL_9.format(a[a.length-1]));

		return sb;
		}

} /*----- Fin de la classe EvolutionPP -----*/
