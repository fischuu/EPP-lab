package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.Calcul;
import javalain.math.fonction.Fct;


/*---------------------*/
/* Fonction Himmelblau */
/*---------------------*/
/**
 * Fonction z = f(x,y).
 *  - x,y [-4,4]
 *  - Continue
 *  - 4 minima locaux
 *		. f(3.0,2.0) = 0
 *		. f(-2.805118,3.131312) = 0
 *		. f(-3.779310,-3.283186= ) = 0
 *		. f(3.584428,-1.848126) = 0
 * 
 *  - 1 maximum local
 *		. f(-0.270844,-0.923038) = 181.616
 *
 * Fonction :
 *  f(x,y) = (x^2+y-11)^2 + (-x+y^2-7)^2
 *
 * @author	Alain Berro
 * @version	22 février 2011
 */
public final class Himmelblau extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Himmelblau ()
		{
		this.nomFct = "Himmelblau";

		/*----- Initialisation -----*/
		this.nbVariable		= 2;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -6.0;
			this.upperLimit[i] =  6.0;
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

		fct[0] = Calcul.pow2(x[0]*x[0]+x[1]-11.0) + Calcul.pow2(x[0]+x[1]*x[1]-7.0);

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

} /*----- Fin de la classe Himmelblau -----*/
