package javalain.ea.operator;

import javalain.ea.Solution;
import javalain.math.fonction.Fct;
import javalain.math.PseudoRandomNumbers;


/*-----------*/
/* Crossover */
/*-----------*/
/**
 * Classe contenant des opérateurs de croisement.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	13 août 2010
*/
public final class Crossover
{
	/*----------------------------*/
	/* Opérations évolutionnaires */
	/*----------------------------*/

	/**
	 * SBX : Simulated Binary Crossover.
	 *
	 * Article(s) de référence :
	 * Kalyannoy Deb and Ram Bhushan Agrawal
	 * "Simulated binary crossover for continuous search space"
	 * Complex System 9, p. 115-148, 1995.
	 */
	private static final double EPS = 1.0e-14;				// Recherche... Pourquoi cette valeur de paramètre ? Issu de JMetal ?

	public static Solution[] SBX (double Pc,
								  Solution p1,
								  Solution p2,
								  Fct fct,
								  double Nc) // Index de distribution de croisement (valeur dans NSGA II : 20.0)
		{
		/*----- Les enfants -----*/
		Solution e1 = new Solution(p1.getVariable()); // A voir - Utilisation de new Solution(double[]) ?
		Solution e2 = new Solution(p2.getVariable());

		if (PseudoRandomNumbers.random() <= Pc)
			{
			double x1, x2;
			double inf, sup;
			double rnd;
			double beta, alpha;
			double betaq;
			double c1, c2;

			for (int i=0; i<p1.getNbVariable(); i++)
				{
				x1 = p1.getVariable(i);
				x2 = p2.getVariable(i);

				if (PseudoRandomNumbers.random() <= 0.5)
					{
					if (Math.abs(x1-x2) > Crossover.EPS)
						{
						/*----- On ordonne les 2 valeurs (x1 < x2) -----*/
						if (x1 > x2)
							{
							double min = x2;
							x2 = x1;
							x1 = min;
							}

						inf = fct.getLowerLimit(i);
						sup = fct.getUpperLimit(i);

						/*----- c1 -----*/
						rnd = PseudoRandomNumbers.random();
						beta = 1.0 + (2.0*(x1-inf)/(x2-x1));
						alpha = 2.0 - Math.pow(beta,-(Nc+1.0));

						if (rnd <= (1.0/alpha))
							betaq = Math.pow(rnd*alpha,(1.0/(Nc+1.0)));
						else
							betaq = Math.pow(1.0/(2.0 - rnd*alpha), 1.0/(Nc+1.0));

						c1 = 0.5*((x1+x2)-betaq*(x2-x1));

						/*----- c2 -----*/
						beta = 1.0 + (2.0*(sup-x2)/(x2-x1));
						alpha = 2.0 - Math.pow(beta,-(Nc+1.0));

						if (rnd <= (1.0/alpha))
							betaq = Math.pow(rnd*alpha,1.0/(Nc+1.0));
						else
							betaq = Math.pow(1.0/(2.0 - rnd*alpha),1.0/(Nc+1.0));

						c2 = 0.5*((x1+x2)+betaq*(x2-x1));

						if (c1 < inf) c1=inf; else
						if (c1 > sup) c1=sup;

						if (c2 < inf) c2=inf; else
						if (c2 > sup) c2=sup;

						if (PseudoRandomNumbers.random() <= 0.5)
							{
							e1.setVariable(i,c2);
							e2.setVariable(i,c1);
							}
						else
							{
							e1.setVariable(i,c1);
							e2.setVariable(i,c2);
							}
						}
					else
						{
						e1.setVariable(i, x1);
						e2.setVariable(i, x2);
						}
					}
				else
					{
					e1.setVariable(i, x2);
					e2.setVariable(i, x1);
					}
				}
			}

		Solution[] enfants = new Solution[2];
		enfants[0] = e1;
		enfants[1] = e2;

		return enfants;
		}

} /*----- Fin de la classe Crossover -----*/
