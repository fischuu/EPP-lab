package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*--------------------*/
/* Fonction Rastrigin */
/*--------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-5.12,5.12] avec n = 10 (par défaut)
 *  - Continue, separable, symétrique, unimodale et
 *    des optima locaux régulièrement distribués
 *  - Minimum global : f(x) = 0 avec xi = 0 pour i [1,n]
 *
 * Fonction :
 *  f(x) = 10*n + sum[i=1,n]xi^2 - 10*cos(2*PI*xi)
 *
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Rastrigin extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Rastrigin () { this(10); }

	public Rastrigin (int d)
		{
		this.nomFct = "Rastrigin";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -5.12;
			this.upperLimit[i] =  5.12;
			}
		}


    /*----------*/
	/* Methodes */
	/*----------*/

	/**
	 * Retourne les résultats des fonctions objectifs.
	 */
	private final static double cte = 2.0*Math.PI;

	public double[] compute (double[] x)
		{
		/*----- Incrémente de nombre d'évaluations de la fonction -----*/
		this.nbEvaluation++;

		double[] fct = new double[this.nbFctObjectif];

		fct[0] = 10.0*this.nbVariable;

		for	(int i=0; i<this.nbVariable ; i++)
			fct[0] += x[i]*x[i] - 10.0*Math.cos(cte*x[i]);

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

} /*----- Fin de la classe Rastrigin -----*/
