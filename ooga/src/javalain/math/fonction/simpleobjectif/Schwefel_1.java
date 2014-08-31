package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*--------------------*/
/* Fonction Schwefel1 */
/*--------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-65.536,65.536] avec n = 10 (par défaut)
 *  - Continue, non separable, unimodale
 *  - Minimum global : f(x) = 0 avec xi = 0 pour i [1,n]
 *
 * Fonction :
 *  f(x) = sum[i=1,n](sum[j=1,i]xj)^2
 * 
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Schwefel_1 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Schwefel_1 () { this(10); }

	public Schwefel_1 (int d)
		{
		this.nomFct = "Schwefel_1";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -65.536;
			this.upperLimit[i] =  65.536;
			}
		}


    /*----------*/
	/* Methodes */
	/*----------*/

	/**
	 * Retourne les résultats des fonctions objectifs.
	 */
	public double[] compute (double[] x)
		{
		/*----- Incrémente de nombre d'évaluations de la fonction -----*/
		this.nbEvaluation++;

		double[] fct = new double[this.nbFctObjectif];

		fct[0] = 0.0;

		double somme;
		for (int i=0; i<this.nbVariable ; i++)
			{
			somme = 0.0;
			for (int j=0; j<=i; j++)
				somme += x[j];

			fct[0] += somme*somme;
			}

		return fct;
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

} /*----- Fin de la classe Schwefel1 -----*/
