package javalain.ea.operator;

import javalain.math.PseudoRandomNumbers;

/*---------*/
/* Mutator */
/*---------*/
/**
 * Classe contenant des opérateurs de mutation.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	13 août 2010
*/
public final class Mutator
{
	/*----------------------------*/
	/* Opérations évolutionnaires */
	/*----------------------------*/

	/**
	 * Mutation polynomiale.
	 *
	 * Article(s) de référence (voir l'algorithme n°2) :
	 * Mohammad Hamdan
	 * "On the disruption-level of polynomial mutation for evolutionary
	 * multi-objective optimisation algorithms"
	 * Computing and Informatics, vol. 28, 2009.
	 */
	public static double mutationPolynomiale (double Pm,
											  double x,
											  double x_lower,
											  double x_upper,
											  double Nm) // Index de distribution pour la mutation (valeur dans NSGA II : 20.0)
		{
		if (PseudoRandomNumbers.random() <= Pm)
			{
			double d = x_upper - x_lower;

			double delta1, delta2, deltaq;

			delta1 = (x - x_lower)/d;
			delta2 = (x_upper - x)/d;

			double r = PseudoRandomNumbers.random();
			if (r <= 0.5)
				deltaq = Math.pow(2.0*r+(1.0-2.0*r)*Math.pow(1.0-delta1,Nm+1.0),1.0/(Nm+1.0)) - 1.0;
			else
				deltaq = 1.0 - Math.pow(2.0*(1.0-r)+2.0*(r-0.5)*Math.pow(1.0-delta2,Nm+1.0),1.0/(Nm+1.0));

			x = x + deltaq*d;
			}

		if (x < x_lower) return x_lower;
		if (x > x_upper) return x_upper;

		return x;
		}

} /*----- Fin de la classe Mutator -----*/
