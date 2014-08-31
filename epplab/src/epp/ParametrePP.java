package epp;

import epp_pso.ParametrePSO;
import epp_pso.tribes_new.ParametreTribes_new;
import java.io.File;
import javalain.algorithmegenetique.ParametreAG;


/*-------------*/
/* ParametrePP */
/*-------------*/
/**
 * Cette classe regroupe les paramètres de l'application.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	11 décembre 2013
 */
public class ParametrePP
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Méthode.
	 */
	public final static String GA			= "Genetic Algorithm";
	public final static String PSO			= "Particule Swarm Optimization";
	public final static String TRIBES		= "Tribes";
	public final static String MULTISTART	= "MultiStart";


	/**
	 * Indices.
	 */
	public final static String FRIEDMAN			= "Friedman";
	public final static String FRIEDMAN_TUKEY	= "Friedman Tukey";
	public final static String KURTOSIS_MIN		= "Kurtosis Min";
	public final static String KURTOSIS_MAX		= "Kurtosis Max";
	public final static String INDICE_4			= "Indice 4";
	public final static String DISCRIMINANT		= "Discriminant";
	public final static String STAHEL_DONOHO	= "Stahel Donoho";


	/**
	 * Transformation des données.
	 */
	public final static String SPHERIQUE		= "Spherique";
	public final static String CENTREE_REDUITE	= "Centree_reduite";


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Répertoires de travail.
	 */
	public String rep_travail;
	public String rep_donnees = "";
	public String rep_sortie = "";
	public String rep_graph;

	/*----- Nom du fichier de données -----*/
	public String ficherDeDonnees = "RData";

	/*----- Méthode -----*/
	public String methode = ParametrePP.GA;

	/*----- Indice -----*/
	public String nomIndice = "";

	/*----- Dimension de projection -----*/
	public int dimensionProjection = 1;

	/*----- Nombre de simulations à effectuer -----*/
	public int nbSimulation = 1;

	/*----- Nombre maximum d'itérations -----*/
	private int nbIterations = 0;

	/*----- Nombre d'individus -----*/
	private int nbIndividus = 0;

	/*----- Données sphériques : 'true' sinon les données sont centrées réduites -----*/
	public boolean sphere = false;

	/*----- Paramètres AG -----*/
	public ParametreAG pag = new ParametreAG("Projection pursuit");

	/*----- Paramètres PSO -----*/
	public ParametrePSO ppso = new ParametrePSO("Projection pursuit");

	/*----- Paramètres PSO -----*/
	public ParametreTribes_new ptribe = new ParametreTribes_new("Projection pursuit");

	/*----- Convergence parameters -----*/
	public int step_iter = -1;		// Default -1 : No measure of convergence
	public double eps = 1.0E-7;

	public int[] tabNumIterReached;	// Save the numbers of iteration reached
	public boolean[] tabConvSimu;	// Save if the simulation converged


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise les paramètres d'évolution du PSO.
	 */
	public ParametrePP () { }


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Lecture / écriture du nombre d'itérations et d'individus.
	 */
	public int getNbIterations () { return this.nbIterations; }

	public int getNbIndividus () { return this.nbIndividus; }

	public void setNbIterations (int nbIterations) { this.nbIterations = nbIterations; }

	public void setNbIndividus (int nbIndividus) { this.nbIndividus = nbIndividus; }


	/**
	 * Création, si nécessaire, des répertoires de l'application en fonction du
	 * répertoire de travail et mise à jour des chemins.
	 */
	public void updatePath (String rep_travail)
		{
		this.rep_travail = rep_travail;

		File f = new File(this.rep_travail);
		if (!f.exists()) f.mkdirs();

		this.setRepDonnees(this.rep_travail + File.separatorChar + "donnees");
		this.setRepSortie(this.rep_travail + File.separatorChar + "sortie");
		}

	public void setRepDonnees (String rep_donnees)
		{
		this.rep_donnees = rep_donnees;

		File f = new File(this.rep_donnees);
		if (!f.exists()) f.mkdirs();
		}

	public void setRepSortie (String rep_sortie)
		{
		this.rep_sortie = rep_sortie;
		this.rep_graph = this.rep_sortie + File.separatorChar + "graphique";

		File f = new File(this.rep_sortie);
		if (!f.exists()) f.mkdirs();
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Affiche l'ensemble des paramètres.
	 */
	@Override
	public String toString ()
		{
		StringBuilder sb;

		//sb = new StringBuilder();
		//sb.append("-----------------------\n");
		//sb.append("Nom du fichier             : "); sb.append(this.ficherDeDonnees); sb.append("\n");
		//sb.append("Méthode                    : "); sb.append(this.methode); sb.append("\n");
		//sb.append("Nom indice                 : "); sb.append(this.nomIndice); sb.append("\n");
		//sb.append("Dimension de projection    : "); sb.append(this.dimensionProjection); sb.append("\n");
		//sb.append("Nb simulations             : "); sb.append(this.nbSimulation); sb.append("\n");
		//sb.append("Nb itérations              : "); sb.append(this.nbIterations); sb.append("\n");
		//sb.append("Nb individus               : "); sb.append(this.nbIndividus); sb.append("\n");
		//sb.append("Transformation des données : "); sb.append(this.sphere ? "Spherique" : "Centree_reduite"); sb.append("\n");
		//sb.append("-----------------------\n");

                sb = new StringBuilder();
                sb.append("");
		return sb.toString();
		}

} /*----- Fin de la classe ParametrePP -----*/
