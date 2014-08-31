package javalain.math.fonction.multiobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.DominanceMinComparator;
import javalain.math.fonction.Fct;


/*---------------*/
/* Fonction ZDT1 */
/*---------------*/
/**
 * Fonction multiobjectif ZDT1.
 *  - x1...xn [0,1] avec n = 30 (par défaut)
 *  - Front de Pareto convexe
 *  - Pas d'optimum local
 *
 * Minimiser :
 *  f1(x) = x1
 *  f2(x) = g(x).h(f1(x),g(x))
 *
 *  g(x)    = 1 + 9/(n-1).sum[i=2,n](xi)
 *  h(f1,g) = 1 - sqrt(f1/g)
 *
 * @author	Alain Berro
 * @version	27 juillet 2010
 */
public final class ZDT1 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public ZDT1 () { this(30); }

	public ZDT1 (int nbVariable)
		{
		this.nomFct = "ZDT1";

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
			this.readParetoFront("ZDT1.pf");

		return this.front;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	private static double g (double[] x)
		{
		double somme = 0.0;
		for (int i=1; i<x.length; i++) somme += x[i];

		return 1.0 + somme*9.0/(x.length-1);
		}


	private static double f (double f1, double g)
		{
		return 1.0 - Math.sqrt(f1/g);
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new DominanceMinComparator(); }

} /*----- Fin de la classe ZDT1 -----*/
