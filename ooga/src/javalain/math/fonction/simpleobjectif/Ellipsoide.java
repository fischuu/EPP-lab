package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*---------------------*/
/* Fonction Ellipsoide */
/*---------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-5.12,5.12] avec n = 10 (par défaut)
 *  - Continue, separable, unimodale
 *  - Minimum global : f(x) = 0 avec xi = 0 pour i [1,n]
 *
 * Fonction :
 *  f(x) = sum[i=1,n]i*(xi)^2
 * 
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Ellipsoide extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Ellipsoide () { this(10); }

	public Ellipsoide (int d)
		{
		this.nomFct = "Ellipsoïde";

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
	public double[] compute (double[] x)
		{
		/*----- Incrémente de nombre d'évaluations de la fonction -----*/
		this.nbEvaluation++;

		double[] fct = new double[this.nbFctObjectif];

		fct[0] = 0.0;

		for	(int i=0; i<this.nbVariable ; i++)
			fct[0] += (i+1)*x[i]*x[i];

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

} /*----- Fin de la classe Ellipsoide -----*/
