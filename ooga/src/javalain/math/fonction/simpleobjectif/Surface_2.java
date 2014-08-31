package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*-------------------*/
/* Fonction Surface2 */
/*-------------------*/
/**
 * Fonction z = f(x,y).
 *  - x,y [-10,10]
 *  - Continue, non separable, multimodale et des optima locaux (761 minima dont 18 minima globaux)
 *
 * Fonction :
 *  f(x,y) = sum[i=1,5]i*cos((i+1)*x+1) * sum[i=1,5]i*cos((i+1)*y+1)
 *
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Surface_2 extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Surface_2 ()
		{
		this.nomFct = "Surface_2";

		/*----- Initialisation -----*/
		this.nbVariable		= 2;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -10.0;
			this.upperLimit[i] =  10.0;
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
		double dx = 0.0, dy = 0.0;
		for	(int i=1 ; i<=5 ; i++)
			{
			dx += i*Math.cos((i+1)*x[0]+i);
			dy += i*Math.cos((i+1)*x[1]+i);
			}

		fct[0] = dx*dy;

		return fct;
		}


	/**
	 * Retourne le front de Pareto.
	 */
	public double[][] getFront () { return null; }


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); }

} /*----- Fin de la classe Surface2 -----*/
