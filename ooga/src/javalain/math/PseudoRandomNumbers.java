package javalain.math;

import java.util.Random;


/*---------------------*/
/* PseudoRandomNumbers */
/*---------------------*/
/**
 * La classe <code>PseudoRandomNumbers</code> donne la possibilité
 * à l'utilisateur de générer des nombres aléatoires à partir d'une graine fixée.
 *
 * @author	Alain Berro
 * @version	5 janvier 2012
 */
public final class PseudoRandomNumbers
{
	/*-----------------*/
	/* Donnée statique */
	/*-----------------*/

	/*----- Générateur de nombres aléatoires -----*/
	private static Random generator = null;


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	public static void createGenerator (long seed)
		{
		if (generator == null){
			generator = new Random(seed); 
                      }
		}

        public static void destroyGenerator ()
		{
		if (generator != null){
			generator = null;
                      }
		}
        
	public static double random ()
		{
		if (generator == null)
			generator = new Random();

		return generator.nextDouble();
		}

} /*----- Fin de la classe PseudoRandomNumbers -----*/
