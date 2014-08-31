package javalain.math.fonction.simpleobjectif;

import java.util.Comparator;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.Calcul;
import javalain.math.fonction.Fct;


/*---------------------------------------*/
/* Fonction Rosenbrock (Banana function) */
/*---------------------------------------*/
/**
 * Fonction z = f(x).
 *  - x1...xn [-2.048,2.048] avec n = 10 (par défaut)
 *  - Continue, unimodale, non separable (strongly epistasis)
 *  - Minimum global : f(x) = 0 avec xi = 1 pour i [1,n]
 *
 * Fonction :
 *  f(x) = sum[i=1,n-1](1-xi)^2 + 100*(x_(i+1)-xi^2)^2
 *
 * En 2 dimensions, cette fonction crée une petite vallée avec autour
 * une petite colline et de grandes ailes de chaque côté.
 *
 * @author	Alain Berro
 * @version	4 août 2010
 */
public final class Rosenbrock extends Fct
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Rosenbrock () { this(10); }

	public Rosenbrock (int d)
		{
		this.nomFct = "Rosenbrock (Banana function)";

		/*----- Initialisation -----*/
		this.nbVariable		= d;
		this.nbFctObjectif	= 1;

		this.lowerLimit = new double[this.nbVariable];
		this.upperLimit = new double[this.nbVariable];

		for (int i=0; i<this.nbVariable; i++)
			{
			this.lowerLimit[i] = -2.048;
			this.upperLimit[i] =  2.048;
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

		for	(int i=0; i<this.nbVariable-1 ; i++)
			fct[0] += Calcul.pow2(1.0-x[i]) + 100.0*Calcul.pow2(x[i+1]-x[i]*x[i]);

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
				this.front[0][i] = 1.0;
			}

		return this.front;
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	@Override
	public Comparator<SolutionEA> comparator() { return new ObjectiveComparator(0); }

} /*----- Fin de la classe Rosenbrock -----*/
