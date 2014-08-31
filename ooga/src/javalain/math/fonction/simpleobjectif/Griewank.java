package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*-------------------*/
/* Fonction Griewank */
/*-------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-600,600] avec n = 10 (par défaut)
 *  - Continue, non separable, symétrique, unimodale et
 *    de nombreux optima locaux régulièrement distribués
 *  - Minimum global : f(x) = 0 avec xi = 0 pour i [1,n]
 *
 * Fonction :
 *  f(x) = 1 + 1/4000*sum[i=1,n](xi)^2 - prod[i=1,n]cos(xi/sqrt(i))
 * 
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Griewank extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Griewank () { this(10); }

	public Griewank (int d)
		{
		this.nomFct = "Griewank";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -600.0;
			this.upperLimit[i] =  600.0;
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

		double somme	= 0.0;
		double produit	= 1.0;

		for	(int i=0; i<this.nbVariable ; i++)
			{
			somme += x[i]*x[i];
			produit *= Math.cos(x[i]/Math.sqrt(i+1));
			}

		fct[0] = 1.0 + somme/4000.0 - produit;

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
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); } // A suivre - Objectif, devrait minimiser.

} /*----- Fin de la classe Griewank -----*/
