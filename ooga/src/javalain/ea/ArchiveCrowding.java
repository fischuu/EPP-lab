package javalain.ea;

import java.util.Collections;
import javalain.ea.operator.comparator.CrowdingDistanceComparator;
import javalain.math.fonction.Fct;
import javalain.math.PseudoRandomNumbers;


/*-----------------*/
/* ArchiveCrowding */
/*-----------------*/
/**
 * Une <code>ArchiveCrowding</code> est une archive dont la technique
 * de réduction est le crowding.
 *
 * @author	Alain Berro
 * @version	13 aout 2010
 */
public class ArchiveCrowding extends Archive
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Comparateur des solutions -----*/
	private static CrowdingDistanceComparator crowdingDistance = new CrowdingDistanceComparator();


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public ArchiveCrowding (int size,
							Fct fct)
		{
		super(size, fct);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Réduction de la taille de l'archive.
	 */
	public void reduce ()
		{
		/*----- Calcul de la distance de crowding -----*/
		this.crowdingDistance();

		if (this.liste.size() > this.tailleMaxArchive)
			{
			/*----- Réduction de l'archive -----*/
			Collections.sort(this.liste,ArchiveCrowding.crowdingDistance);

			while (this.liste.size() > this.tailleMaxArchive)
				this.liste.remove(this.tailleMaxArchive);
			}
		}


	/**
	 * Sélectionne une solution de l'archive par un tournoi binaire.
	 * Celle avec la plus grande distance crowding est choisie car
	 * elle est située dans une zone plus désertique.
	 */
	public SolutionEA binaryTournament ()
		{
		int p1 = (int)(PseudoRandomNumbers.random()*this.liste.size());
		int p2 = (int)(PseudoRandomNumbers.random()*this.liste.size());

		double dc1 = this.liste.get(p1).getCrowdingDistance();
		double dc2 = this.liste.get(p2).getCrowdingDistance();

		return (dc1 < dc2) ? this.liste.get(p2) : this.liste.get(p1);
		}

} /*----- Fin de la classe ArchiveCrowding -----*/
