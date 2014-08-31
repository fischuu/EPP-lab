package javalain.math.fonction.multiobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.DominanceMinComparator;
import javalain.math.Calcul;
import javalain.math.fonction.Fct;


/*---------------*/
/* Fonction ZDT4 */
/*---------------*/
/**
 * Fonction multiobjectif ZDT4.
 *  - x1 [0,1] x2...xn [-5,5] avec n = 10 (par défaut)
 *  - Front de Pareto convexe et très multimodal
 *  - De très nombreuses frontières locales : pow(21,9)
 *
 * Minimiser :
 *  f1(x) = x1
 *  f2(x) = g(x).h(f1(x),g(x))
 * 
 *  g(x)    = 1 + 10*(n-1) + sum[i=2,n]pow2(xi)-10.cos(4*PI*xi)
 *  h(f1,g) = 1 - sqrt(f1/g)
 *
 * @author	Alain Berro
 * @version	27 juillet 2010
 */
public final class ZDT4 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public ZDT4 () { this(10); }

	public ZDT4 (int nbVariable)
		{
		this.nomFct = "ZDT4";

		this.nbVariable		= nbVariable;
		this.nbFctObjectif	= 2;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		this.lowerLimit[0] = 0.0;
		this.upperLimit[0] = 1.0;
		for (int i=1; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -5.0;
			this.upperLimit[i] =  5.0;
			}
		}


    /*----------*/
	/* Methodes */
	/*----------*/

	/**
	 * Retourne le tableau des résultats des fonctions objectifs.
	 */
	public double[] compute (double[] x)
		{
		double[] fct = new double[this.nbFctObjectif];

		double g = g(x);
		fct[0] = x[0];
		fct[1] = g * f(fct[0],g);

		return fct;
		}


	/**
	 * Retourne le front de Pareto.
	 */
	public double[][] getFront ()
		{
		if (this.front == null)
			this.readParetoFront("ZDT4.pf");

		return this.front;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	private final static double cte = 4.0*Math.PI;

	public static double g (double[] x)
		{
		double somme = 0.0;
		for (int i=1; i<x.length; i++)
			somme += Calcul.pow2(x[i]) - 10.0*Math.cos(cte*x[i]);

		return 1.0 + 10.0*(x.length-1) + somme;
		}


	public static double f (double f1, double g)
		{
		return 1.0 - Math.sqrt(f1/g);
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new DominanceMinComparator(); }

} /*----- Fin de la classe ZDT4 -----*/
