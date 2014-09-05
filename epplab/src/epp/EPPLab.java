package epp;

import ihm.Bienvenue;
import ihm.Ihm;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import util.FileUtils;
import util.MsgUtils;

import javalain.math.PseudoRandomNumbers;


/*---------------*/
/* Classe EPPLab */
/*---------------*/
/**
 * La classe <code>EPPLab</code> ...
 *
 * @author	Alain Berro
 * @version	0.1
 */
public class EPPLab
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Nom du logiciel.
	 */
	public static final String nom = "EPP-Lab";

	private static final String name = "epplab";


	/**
	 * Ensemble des libellés et des messages.
	 */
	public static ResourceBundle msg;
	static { msg = ResourceBundle.getBundle("ihm/MessagesBundle"); }


	/**
	 * Position de la fenêtre.
	 */
	private static int		pos_x;
	private static int		pos_y;

        // Put here the same thing what will be written into the file
        public static String result;
        public static void setResult (String putThis){
              result = putThis;
        }
        public math.Matrice dataMatrix;
        public static int dataRows;
        public static int dataCols;
        public static double[] dataValues;
        public static boolean runsInR = false;

	/*------*/
	/* Main */
	/*------*/

	@SuppressWarnings("SleepWhileHoldingLock")
	public static void main (String[] args) throws Exception
		{
		/*----- Décoration -----*/
		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		/*----- Paramètres de l'application -----*/
		final ParametrePP p = new ParametrePP();

		/*----- Création d'une évolution -----*/
		final EvolutionPP e = new EvolutionPP(p);
//		final EvolutionPP e = new EvolutionPPnew(p);


		/**
		 * Lancement sans IHM.
		 */
		if (args.length != 0 && args[0].equals("noIhm"))
			{
			/*----- Paramétres communs -----*/
			p.setRepDonnees("/home/medafi/temp");
			p.setRepSortie ("/home/medafi/temp/sortie");
			p.nbSimulation	= 10;
			p.sphere        = true;

			String copie = p.rep_donnees + File.separatorChar + "copie";
			File fc = new File(copie);
			if (!fc.exists()) fc.mkdir();

			/**
			 * Boucle infinie.
			 */
			do	{
				/*----- Lecture des fichiers dans le répertoire de données -----*/
				File f = new File(p.rep_donnees);
				@SuppressWarnings("UseOfObsoleteCollectionType")
				Vector<String> v = FileUtils.listeFichiers(f);

				for (int i=0; i<v.size(); i++)
					{
					/*----- Lecture d'un fichier -----*/
					p.ficherDeDonnees	= v.get(i);

					/**
					 * KURTOSIS_MAX
					 */
//					p.nomIndice = ParametrePP.KURTOSIS_MAX;
//
//					/*----- GA -----*/
//					p.methode = ParametrePP.GA; p.setNbIndividus(50); p.setNbIterations(20);
//					System.out.println(p);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- PSO -----*/
//					p.methode = ParametrePP.PSO; p.setNbIndividus(20); p.setNbIterations(50);
//					System.out.println(p);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- TRIBE -----*/
//					p.methode = ParametrePP.TRIBES; p.setNbIndividus(20); p.setNbIterations(100);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);

					/**
					 * FRIEDMAN
					 */
					p.nomIndice = ParametrePP.FRIEDMAN;

					/*----- GA -----*/
                                        p.methode = ParametrePP.GA; p.setNbIndividus(50); p.setNbIterations(20);
					e.setEtat(EvolutionPP.DEMARRE);
					EvolutionPP.attendre(e);

					/*----- PSO -----*/
					p.methode = ParametrePP.PSO; p.setNbIndividus(20); p.setNbIterations(50);
					e.setEtat(EvolutionPP.DEMARRE);
					EvolutionPP.attendre(e);

					/*----- TRIBE -----*/
					p.methode = ParametrePP.TRIBES; p.setNbIndividus(20); p.setNbIterations(100);
					e.setEtat(EvolutionPP.DEMARRE);
					EvolutionPP.attendre(e);

					/**
					 * DISCRIMINANT.
					 */
//					p.nomIndice = ParametrePP.DISCRIMINANT;
//
//					/*----- GA -----*/
//					p.methode = ParametrePP.GA; p.setNbIndividus(50); p.setNbIterations(20);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- PSO -----*/
//					p.methode = ParametrePP.PSO; p.setNbIndividus(20); p.setNbIterations(50);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- TRIBE -----*/
//					p.methode = ParametrePP.TRIBES; p.setNbIndividus(20); p.setNbIterations(100);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);

					/**
					 * FRIEDMAN_TUKEY
					 */
//					p.nomIndice = ParametrePP.FRIEDMAN_TUKEY;
//
//					/*----- GA -----*/
//					p.methode = ParametrePP.GA; p.setNbIndividus(50); p.setNbIterations(20);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- PSO -----*/
//					p.methode = ParametrePP.PSO; p.setNbIndividus(20); p.setNbIterations(50);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);
//
//					/*----- TRIBE -----*/
//					p.methode = ParametrePP.TRIBES; p.setNbIndividus(20); p.setNbIterations(100);
//					e.setEtat(EvolutionPP.DEMARRE);
//					EvolutionPP.attendre(e);

					/*----- Déplacement du fichier -----*/
					File src = new File(p.rep_donnees + File.separatorChar + p.ficherDeDonnees);
					System.out.println(src);
					File dest = new File(copie + File.separatorChar + p.ficherDeDonnees); // A faire : Si le déplacement échoue on recommence à traiter le fichier.
					System.out.println(dest);

					src.renameTo(dest);
					}

				/*----- On attend 5 secondes -----*/
				Thread.sleep(10);
				} while(true);
			}


		/**
		 * Lancement avec IHM.
		 */
		else
			{
			/*----- Lancement de l'IHM -----*/
			javax.swing.SwingUtilities.invokeLater(new Runnable()
				{
				@SuppressWarnings("ResultOfObjectAllocationIgnored")
				public void run()
					{
					if (!EPPLab.loadPrefs(p))
						{
						/*----- Premier lancement de l'application ou problème au chargement des préférences utilisateur -----*/
						Bienvenue bienvenue = new Bienvenue(e,p);

						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						Dimension size = bienvenue.getSize();

						bienvenue.setLocation((screenSize.width - size.width)/3, (screenSize.height - size.height)/3);
						bienvenue.pack();
						bienvenue.setVisible(true);
						}
					else
						{
						/*----- Lancement de l'application -----*/
						new Ihm(EPPLab.pos_x, EPPLab.pos_y, e, p);
						}
					}
				});
			}
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Charge les préférences de l'utilisateur pour la fenêtre générale.
	 */
	public static boolean loadPrefs (ParametrePP parametre)
		{
		Properties p = new Properties();
		try	{
			/*----- Ouverture du fichier en lecture -----*/
			FileInputStream fis = new FileInputStream(System.getProperty("user.home") + File.separatorChar + "." + EPPLab.name + File.separatorChar + EPPLab.name + ".ini");

			/*----- Charge les préférences -----*/
			p.loadFromXML(fis);
			fis.close();

			/*----- Paramètres -----*/
			EPPLab.pos_x = Integer.parseInt(p.getProperty("position_fenetre_x", "0"));
			EPPLab.pos_y = Integer.parseInt(p.getProperty("position_fenetre_y", "0"));

			/*----- Répertoire de travail -----*/
			String dir_work = p.getProperty("repertoire_de_travail");
			File f = new File(dir_work);

			if (!f.exists())
				throw new FileNotFoundException();
			else
				/*----- Mise à jour des répertoires de l'application -----*/
				parametre.updatePath(p.getProperty("repertoire_de_travail"));
			}
		catch (Exception e)
			{
			/*----- Fichier de paramètres non trouvé -----*/
			/*----- Répertoire de travail non trouvé (effacé) -----*/
			return false;
			}

		return true;
		}


	/**
	 * Enregistrement des préférences de l'utilisateur pour la fenêtre générale.
	 */
	public static void savePrefs (ParametrePP parametre, int pos_x, int pos_y)
		{
		Properties p = new Properties();
		try	{
			/*----- Enregistrement des préférences -----*/
			p.setProperty("version", "0");
			p.setProperty("position_fenetre_x", "" + pos_x);
			p.setProperty("position_fenetre_y", "" + pos_y);
			p.setProperty("repertoire_de_travail", parametre.rep_travail);

			/*----- Vérifie si le répertoire existe -----*/
			File f = new File(System.getProperty("user.home") + File.separatorChar + "." + EPPLab.name);
			if (!f.exists()) f.mkdir();

			/*----- Ouverture du fichier en écriture -----*/
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + File.separatorChar + "." + EPPLab.name + File.separatorChar + EPPLab.name + ".ini");

			/*----- Ecriture des préférences -----*/
			p.storeToXML(fos, "Préférences de l'utilisateur pour " + EPPLab.nom);

			fos.close();
			}
		catch (IOException e)
			{
			MsgUtils.erreurAndExit(null,
								   "Erreur lors de l'enregistrement des préférences de l'utilisateur.", // A faire - Traduire
								   "Erreur");
			}
		}

        public static String test (double chooseIndex) throws Exception
        {
            String inPut = ("Hallo " + chooseIndex);

            return inPut;
        }


        public static String eppLabRInterface (double chooseIndex, double nSimulation, String alg, double iter, double tPara,  double dRows, double dCols, double[] data, double seed, double step_iter, double eps) throws Exception
        {
                
                long seedCasted = (long) seed;
                PseudoRandomNumbers.createGenerator(seedCasted);
                           
                int iterations =(int) iter;
                int individuals =(int) tPara;
                
                int stepIter =(int) step_iter;
                
                runsInR = true;

                dataRows = (int) dRows;
                dataCols = (int) dCols;

                dataValues = new double [dataRows*dataCols];
                System.arraycopy(data, 0, dataValues, 0, dataRows*dataCols);

		/*----- Paramètres de l'application -----*/
		final ParametrePP p = new ParametrePP();

		/*----- Création d'une évolution -----*/
		final EvolutionPP e = new EvolutionPP(p);
//		final EvolutionPP e = new EvolutionPPnew(p);


			/*----- Paramétres communs -----*/
	//		p.setRepDonnees(Donnees);
	//		p.setRepSortie (Sortie);
			p.nbSimulation	= (int) nSimulation;
			p.sphere        = false;
                        // Convergence parameters
                        p.step_iter = stepIter; 
                        p.eps = eps; 

                    
                        
	//		String copie = p.rep_donnees + File.separatorChar + "copie";
	//		File fc = new File(copie);
	//		if (!fc.exists()) fc.mkdir();

			/**
			 * Boucle infinie.
			 */
			//do	{
				/*----- Lecture des fichiers dans le répertoire de données -----*/
	//			File f = new File(p.rep_donnees);
	//			@SuppressWarnings("UseOfObsoleteCollectionType")
	//			Vector<String> v = FileUtils.listeFichiers(f);



	//			for (int i=0; i<v.size(); i++)
	//				{
					/*----- Lecture d'un fichier -----*/
	//				p.ficherDeDonnees	= v.get(i);
                                        p.ficherDeDonnees = "olive.txt";
                                        if(chooseIndex==1){
					/**
					 * KURTOSIS_MAX
					 */
                                            p.nomIndice = ParametrePP.KURTOSIS_MAX;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                System.out.println(p);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                System.out.println(p);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        } else if(chooseIndex==2){
                                         /**
                                         * FRIEDMAN
                                         */

                                            p.nomIndice = ParametrePP.FRIEDMAN;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        } else if(chooseIndex==3){
					/**
					 * DISCRIMINANT.
					 */
                                            p.nomIndice = ParametrePP.DISCRIMINANT;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        } else if(chooseIndex==4){
					/**
					 * FRIEDMAN_TUKEY
					 */
                                            p.nomIndice = ParametrePP.FRIEDMAN_TUKEY;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        }   else if(chooseIndex==5){
					/**
					 * KURTOSIS_MIN
					 */
                                            p.nomIndice = ParametrePP.KURTOSIS_MIN;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        } else if(chooseIndex==6){
					/**
					 * INDICE_4
					 */
                                            p.nomIndice = ParametrePP.INDICE_4;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                        }else if(chooseIndex==7){
					/**
					 * STAHEL_DONOHO
					 */
                                            p.nomIndice = ParametrePP.STAHEL_DONOHO;

                                            if(alg.equals("GA")){
                                            /*----- GA -----*/
                                                p.methode = ParametrePP.GA; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("PSO")){
                                            /*----- PSO -----*/
                                                p.methode = ParametrePP.PSO; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }

                                            if(alg.equals("Tribe")){
                                            /*----- TRIBE -----*/
                                                p.methode = ParametrePP.TRIBES; p.setNbIndividus(individuals); p.setNbIterations(iterations);
                                                e.setEtat(EvolutionPP.DEMARRE);
                                                EvolutionPP.attendre(e);
                                            }
                                        }

					/*----- Déplacement du fichier -----*/
		//			File src = new File(p.rep_donnees + File.separatorChar + p.ficherDeDonnees);
		//			System.out.println(src);
		//			File dest = new File(copie + File.separatorChar + p.ficherDeDonnees); // A faire : Si le déplacement échoue on recommence à traiter le fichier.
		//			System.out.println(dest);

		//			src.renameTo(dest);
		//			}

				/*----- On attend 5 secondes -----*/
			//	Thread.sleep(10);
			//	} while(true);

                        EPPLab.setResult(epp.reportToR.getResult());

                        // Destroy the RNG
                        PseudoRandomNumbers.destroyGenerator();
                        
                        return result;
        }
} /*----- Fin de la classe EPPLab -----*/
