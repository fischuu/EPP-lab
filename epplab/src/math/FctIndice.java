package math;

import indice.Discriminant;
import indice.Friedman;
import indice.FriedmanTukey;
import indice.Indice;
import indice.KurtosisMin;
import java.io.File;
import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.Calcul;
import javalain.math.fonction.Fct;


/*-----------------*/
/* Fonction Indice */
/*-----------------*/
/**
 * Fonction 'indice' I(x, y).
 *
 * @author	Alain Berro
 * @version	1 octobre 2010
 */
public final class FctIndice extends Fct
{
	/*---------*/
	/* Données */
	/*---------*/

	private final double[] fct;

	private Indice indice;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public FctIndice (String nom_fichier, String nom_indice)
		{
		this.nomFct = nom_fichier + " - " + nom_indice;

		/*----- Initialisation -----*/
		this.nbVariable		= 2;
		this.nbFctObjectif	= 1;

		this.fct = new double[this.nbFctObjectif];

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		this.lowerLimit[0] = -1.0;
		this.upperLimit[0] =  1.0;

		this.lowerLimit[1] = -1.0;
		this.upperLimit[1] =  1.0;

		/*----- Matrice d'observations -----*/
		Matrice X = new Matrice("donnees" + File.separatorChar + nom_fichier + ".txt", 0);

		/*----- Indice -----*/
		if (nom_indice.equals("Discriminant"))	this.indice = new Discriminant(X.spherique()); else
		if (nom_indice.equals("FriedmanTukey"))	this.indice = new FriedmanTukey(X.spherique()); else
		if (nom_indice.equals("Friedman"))		this.indice = new Friedman(X.spherique()); else
		if (nom_indice.equals("Kurtosis"))		this.indice = new KurtosisMin(X.spherique());
		}


    /*----------*/
	/* Methodes */
	/*----------*/

	/**
	 * Retourne les résultats des fonctions objectifs.
	 */
	public double[] compute (double[] x)
		{
		this.fct[0] = this.indice.calcul(Calcul.normaliseVecteur(x));

		return this.fct;
		}


	/**
	 * Retourne le front de Pareto.
	 */
	public double[][] getFront ()
		{
		if (this.front == null)
			{
			this.front = new double[1][this.nbVariable];
			for (int i=0; i<this.nbVariable; i++)
				this.front[0][i] = 0.0;
			}

		return this.front;
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); }

} /*----- Fin de la classe Sphere -----*/
