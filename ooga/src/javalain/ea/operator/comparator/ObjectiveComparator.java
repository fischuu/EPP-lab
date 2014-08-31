package javalain.ea.operator.comparator;

import java.util.Comparator;
import javalain.ea.SolutionEA;


/*---------------------*/
/* ObjectiveComparator */
/*---------------------*/
/**
 * Cette classe compare deux solutions par rapport à un objectif donné.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class ObjectiveComparator implements Comparator<SolutionEA>
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Numéro de l'objectif sur lequel s'effectue la comparaison des solutions -----*/
	private int nObj;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public ObjectiveComparator (int nObj) { this.nObj = nObj; }


	/*----------------------*/
	/* Méthodes  redéfinies */
	/*----------------------*/

	/**
	 * Compare deux solutions par rapport à l'objectif 'nObj'.
	 * 
	 * Retourne :
	 *	 1 si la valeur de nObj de s1 est supérieur à celle de nObj de s2
	 *	 0 si les deux sont égaux
	 *	-1 si la valeur de nObj de s1 est inférieur à celle de nObj de s2
	 */
	public int compare (SolutionEA s1, SolutionEA s2)
		{
		if (s1 == null)
			return (s2 == null) ? 0 : -1;
		else
			if (s2 == null) return 1;

		double d1 = s1.getObjectif()[this.nObj];
		double d2 = s2.getObjectif()[this.nObj];

		if (d1 > d2)
			return 1;
		else
			{
			if (d1 < d2)
				return -1;
			else
				return 0;
			}
		}

} /*----- Fin de la classe ObjectiveComparator -----*/
