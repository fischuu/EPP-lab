package javalain.math.fonction.multiobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.DominanceMinComparator;
import javalain.math.Calcul;
import javalain.math.fonction.Fct;


/*---------------*/
/* Fonction ZDT6 */
/*---------------*/
/**
 * Fonction multiobjectif ZDT6.
 *  - x1...xn [0,1] avec n = 10 (par défaut)
 *  - Distibution sur le front de Pareto non uniforme et densité des points
 *    basse près du front et haute loin du front
 *  - Pas d'optimum local
 *
 * Minimiser :
 *  f1(x) = 1 - exp(-4*x1).pow6(sin(6*PI*x1))
 *  f2(x) = g(x).h(f1(x),g(x))
 * 
 *  g(x)    = 1 + 9/pow(sum[i=2,n](xi)/(n-1),0.25)
 *  h(f1,g) = 1 - pow(f1/g,2)
 *
 * @author	Alain Berro
 * @version	20 juillet 2010
 */
public final class ZDT6 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public ZDT6 () { this(10); }

	public ZDT6 (int nbVariable)
		{
		this.nomFct = "ZDT6";

		this.nbVariable		= nbVariable;
		this.nbFctObjectif	= 2;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = 0.0;
			this.upperLimit[i] = 1.0;
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
		fct[0] = 1.0 - Math.exp(-4.0*x[0])*Calcul.pow6(Math.sin(cte*x[0]));
		fct[1] = g * f(fct[0],g);

		return fct;
		}


	/**
	 * Retourne le front de Pareto.
	 */
	public double[][] getFront ()
		{
		if (this.front == null)
			this.readParetoFront("ZDT6.pf");

		return this.front;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	private final static double cte = 6.0*Math.PI;

	private static double g (double[] x)
		{
		double somme = 0.0;
		for (int i=1; i<x.length; i++) somme += x[i];

		return 1.0 + 9.0*Math.pow(somme/(x.length-1), 0.25);
		}


	private static double f (double f1, double g)
		{
		return 1.0 - Calcul.pow2(f1/g);
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new DominanceMinComparator(); }

} /*----- Fin de la classe ZDT6 -----*/
