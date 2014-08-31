package javalain.ea.operator.comparator;

import java.util.Comparator;
import javalain.ea.SolutionEA;


/*------------------------*/
/* DominanceMinComparator */
/*------------------------*/
/**
 * Cette classe compare deux solutions pour un problème multiobjectif
 * de minimisation.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class DominanceMinComparator implements Comparator<SolutionEA>
{
	/*----------------------*/
	/* Méthodes  redéfinies */
	/*----------------------*/

	/**
	 * Compare deux solutions pour un problème multiobjectif de minimisation.
	 * 
	 * Retourne :
	 *	 1 si s1 domine s2
	 *	 0 si les deux sont non-dominées
	 *	-1 si s2 domine s1
	 */
	public int compare (SolutionEA s1, SolutionEA s2)
		{
		if (s1 == null)
			return (s2 == null) ? 0 : -1;
		else
			if (s2 == null) return 1;

		double[] solution1 = s1.getObjectif();
		double[] solution2 = s2.getObjectif();

		int domine1 = 0;
		int domine2 = 0;
		for (int i=0; i<solution1.length; i++)
			{
			if (solution1[i] < solution2[i])
				domine1 = 1;
			else
				if (solution1[i] > solution2[i])
					domine2 = 1;
			}

		if (domine1 == domine2)
			return 0;
		else
			{
			if (domine1 == 1)
				return 1;
			else
				return -1;
			}
		}

} /*----- Fin de la classe DominanceMinComparator -----*/
