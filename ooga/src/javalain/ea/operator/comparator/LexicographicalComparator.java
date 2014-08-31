package javalain.ea.operator.comparator;

import java.util.Comparator;


/*---------------------------*/
/* LexicographicalComparator */
/*---------------------------*/
/**
 * Cette classe compare deux double[].
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class LexicographicalComparator implements Comparator<double[]>
{
	/*----------------------*/
	/* Méthodes  redéfinies */
	/*----------------------*/

	/**
	 * Compare deux double[].
	 *
	 * Retourne :
	 *	 1 si d1 est supérieur à d2
	 *	 0 si les deux sont égaux
	 *	-1 si d1 est inférieur à d2
	 */
	public int compare (double[] d1, double[] d2)
		{
		/*----- Recherche la première valeur différente -----*/
		int indice = 0;
		while (indice < d1.length &&
			   indice < d2.length &&
			   d1[indice] == d2[indice]) indice++;

		if (indice == d1.length || indice == d2.length )
			return 0;
		else
		if (d1[indice] < d2[indice])
			return -1;
		else
		if (d1[indice] > d2[indice])
			return 1;

		return 0;
		}

} /*----- Fin de la classe LexicographicalComparator -----*/
