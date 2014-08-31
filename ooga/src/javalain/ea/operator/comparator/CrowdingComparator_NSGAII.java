package javalain.ea.operator.comparator;

import java.util.Comparator;
import javalain.ea.SolutionEA;


/*---------------------------*/
/* CrowdingComparator_NSGAII */
/*---------------------------*/
/**
 * Cette classe compare deux solutions par rapport à leur rang de domination et
 * leur distance de crowding.
 *
 * Référence :												// Recherche... A vérifier.
 * Deb K., Pratap A., Agarwal S. and Meyarivan, T.
 * "A fast and elitist multiobjective genetic algorithm: NSGA-II"
 * IEEE Trans. on Evol. Computation 6, 182-197, 2002.
 *
 * @author	Alain Berro
 * @version	13 août 2010
 */
public class CrowdingComparator_NSGAII implements Comparator<SolutionEA>
{
	/*----------------------*/
	/* Méthodes  redéfinies */
	/*----------------------*/

	/**
	 * Compare deux solutions par rapport à leur rang de domination et
	 * leur distance de crowding.
	 *
	 * Retourne :
	 *	 1 si s1 meilleur que s2
	 *	 0 si les deux sont égales
	 *	-1 si s2 est meilleur que s2
	 */
	public int compare (SolutionEA s1, SolutionEA s2)
		{
		if (s1 == null)
			return (s2 == null) ? 0 : 1;
		else
			if (s2 == null) return -1;

		/*----- Comparaison des rangs -----*/
		int rang1 = s1.getRank();
		int rang2 = s2.getRank();

		if (rang1 < rang2) return 1;
		if (rang2 < rang1) return -1;

		/*----- Comparaison des distance de crowding -----*/
		double d1 = s1.getCrowdingDistance();
		double d2 = s2.getCrowdingDistance();

		if (d1 < d2) return 1;
		if (d1 > d2) return -1;

		return 0;
		}

} /*----- Fin de la classe CrowdingComparator_NSGAII -----*/
