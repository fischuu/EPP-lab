package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*-----------------*/
/* Fonction Ackley */
/*-----------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-32.768,32.768] avec n = 10 (par défaut)
 *  - Continue, non separable, symétrique, unimodale et
 *    de nombreux optima locaux régulièrement distribués
 *  - Minimum global : f(x) = 0 avec xi = 0 pour i [1,n]
 *
 * Fonction :
 *  f(x) = 20 + e - 20*exp(-0.2*sqrt((1/n)*sum[i=1:n]xi^2)) - exp((1/n)*sum[i=1:n]cos(2*Pi*xi))
 * 
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Ackley extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Ackley () { this(10); }

	public Ackley (int d)
		{
		this.nomFct = "Ackley";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -32.768;
			this.upperLimit[i] =  32.768;
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

		double somme1 = 0.0;
		double somme2 = 0.0;
		for	(int i=0; i<this.nbVariable ; i++)
			{
			somme1 += x[i]*x[i];
			somme2 += Math.cos(cte*x[i]);
			}

		fct[0] = 20.0 + Math.E - 20.0*Math.exp(-0.2*Math.sqrt(somme1/this.nbVariable)) - Math.exp(somme2/this.nbVariable);

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

} /*----- Fin de la classe Ackley -----*/
