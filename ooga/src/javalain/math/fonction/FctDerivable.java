package javalain.math.fonction;


/*----------*/
/* Fonction */
/*----------*/
/**
 * Fonction.
 *
 * @author	Alain Berro
 * @version	28 mai 2010
 */
public abstract class FctDerivable extends Fct
{
	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Retourne les résultats des fonctions dérivées.
	 */
	public abstract double[] computeDerivative (double[] x);

} /*----- Fin de la classe FctDerivable -----*/
