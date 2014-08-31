package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*-------------------*/
/* Fonction Surface1 */
/*-------------------*/
/**
 * Fonction z = f(x,y).
 *  - x,y [-5.12,5.12]
 *  - Continue, separable, unimodale et des optima locaux plus ou moins nombreux en fonction de la constante 'cte'
 *  - Minimum global : f(x) = 0 avec x = 0 et y = 0
 *
 * Fonction :
 *  f(x,y) = |x| + |y| - cos(x*cte) - cos(y*cte)
 *
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Surface_1 extends Fct
{
	/*---------*/
	/* Données */
	/*---------*/

	private final int cte;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Surface_1 (int cte)
		{
		this.nomFct = "Surface_1 (" + cte + ")";
		this.cte = cte;

		/*----- Initialisation -----*/
		this.nbVariable		= 2;
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
	public double[] compute (double[] x)
		{
		/*----- Incrémente de nombre d'évaluations de la fonction -----*/
		this.nbEvaluation++;

		double[] fct = new double[this.nbFctObjectif];

		fct[0] = Math.abs(x[0]) + Math.abs(x[1]) - Math.cos(x[0]*this.cte) - Math.cos(x[1]*this.cte);

		return fct;
		}


	/**
	 * Retourne le front de Pareto.
	 */
	public double[][] getFront ()
		{
		if (this.front == null)
			{
			this.front = new double[1][2];
			this.front[0][0] = 0.0;
			this.front[0][1] = 0.0;
			}

		return this.front;
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); }

} /*----- Fin de la classe Surface1 -----*/
