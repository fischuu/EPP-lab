package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*--------------------*/
/* Fonction Schwefel2 */
/*--------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-500,500] avec n = 10 (par défaut)
 *  - Continue, unimodale, separable et des optima locaux, trompeuse
 *  - Minimum global : f(x) = 0 avec xi = 420.9687 pour i [1,n]
 *
 * Fonction :
 *  f(x) = 418.9829*n + sum[i=1,n]-xi*sin(sqrt(abs(xi)))
 *
 * @author	Alain Berro
 * @version	3 août 2010
 */
public final class Schwefel_2 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Schwefel_2 () { this(10); }

	public Schwefel_2 (int d)
		{
		this.nomFct = "Schwefel_2";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -500.0;
			this.upperLimit[i] =  500.0;
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

		fct[0] = 418.9829*this.nbVariable;

		for	(int i=0; i<this.nbVariable ; i++)
			fct[0] += -x[i]*Math.sin(Math.sqrt(Math.abs(x[i])));

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
				this.front[0][i] = 420.9687;
			}

		return this.front;
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); }

} /*----- Fin de la classe Schwefel2 -----*/
