package javalain.ea.operator.comparator;

import java.util.Comparator;
import javalain.ea.SolutionEA;


/*----------------------------*/
/* CrowdingDistanceComparator */
/*----------------------------*/
/**
 * Cette classe compare deux solutions par rapport à leur distance de crowding.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class CrowdingDistanceComparator implements Comparator<SolutionEA>
{
	/*----------------------*/
	/* Méthodes  redéfinies */
	/*----------------------*/

	/**
	 * Compare deux solutions par rapport à leur distance de crowding.
	 * 
	 * Retourne :
	 *	 1 si s1 a une distance de crowding plus petite que s2
	 *	 0 si les deux sont égales
	 *	-1 si s1 a une distance de crowding plus grande que s2
	 */
	public int compare (SolutionEA s1, SolutionEA s2)
		{
		if (s1 == null)
			return (s2 == null) ? 0 : 1;
		else
			if (s2 == null) return -1;

		double d1 = s1.getCrowdingDistance();
		double d2 = s2.getCrowdingDistance();

		if (d1 < d2) return 1;
		if (d1 > d2) return -1;

		return 0;
		}

} /*----- Fin de la classe CrowdingDistanceComparator -----*/
