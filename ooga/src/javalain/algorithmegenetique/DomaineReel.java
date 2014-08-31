package javalain.algorithmegenetique;

import java.io.Serializable;


/*-------------*/
/* DomaineReel */
/*-------------*/
/**
 * Domaine de valeurs pour un nombre réel.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public class DomaineReel implements Serializable
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
     * Borne inférieure du <code>DomaineReel</code>.
	 */
	private double min;


	/**
     * Borne supérieure du <code>DomaineReel</code>.
	 */
	private double max;


	/**
     * Taille du <code>DomaineReel</code>.
	 */
	private double taille;


	/**
     * Rayon local de mutation.
	 * <p>
	 * Le rayon local de mutation influence la mutation d'un gène
	 * <code>ReelDouble</code>.
	 */
	private double rayon;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
     * Construit un <code>DomaineReel</code>.
	 *
	 * @param	min	borne inférieure
	 * @param	max	borne supérieure
	 *
	 * @exception	RuntimeException si <code>min</code> est > à
	 *				<code>max</code>
	 */
	public DomaineReel (double min,
						double max)
		{
		if	(min > max) throw new RuntimeException("DomaineReel, DomaineReel (double min, double max) : min > max.");
		else
			{
			this.min = min;
			this.max = max;
			this.taille = max-min;

			if  (this.taille < 1000.0)
				this.rayon = this.taille/150.0;
			else
				this.rayon = Math.log(this.taille);
			}
		}


	/**
     * Construit un <code>DomaineReel</code> avec un rayon local de mutation.
	 *
	 * @param	min		borne inférieure
	 * @param	max		borne supérieure
	 * @param	rayon	rayon local de mutation
	 *
	 * @exception	RuntimeException si <code>min</code> est > à
	 *				<code>max</code>
	 */
	public DomaineReel (double min,
						double max,
						double rayon)
		{
		if	(min > max) throw new RuntimeException("DomaineReel, DomaineReel (double min, double max, double rayon) : min > max.");
		else
			{
			this.min = min;
			this.max = max;
			this.taille = max-min;
			this.rayon = rayon;
			}
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
     * Modifie la valeur de la borne inférieure.
	 *
	 * @exception	RuntimeException si <code>nb</code> est > à la
	 *				borne supérieure
	 */
	public void setMin (double nb)
		{
		if	(nb > this.max) throw new RuntimeException("DomaineReel, setMin (double nb) : nb est > a la borne superieure du domaine.");
		else
			{
			this.min = nb;
			this.taille = this.max-nb;
			}
		}


	/**
     * Modifie la valeur de la borne supérieure.
	 *
	 * @exception	RuntimeException si <code>nb</code> est < à
	 *				la borne inférieure
	 */
	public void setMax (double nb)
		{
		if	(nb < this.min) throw new RuntimeException("DomaineReel, setMax (double nb) : nb est < a la borne inferieure du domaine.");
		else
			{
			this.max = nb;
			this.taille = nb-this.min;
			}
		}


	/**
     * Modifie la valeur du rayon local de mutation.
	 */
	public void setRayon (double nb) { this.rayon = nb; }


	/**
     * Retourne la valeur de la borne inférieure.
	 */
	public double getMin () { return this.min; }


	/**
     * Retourne la valeur de la borne supérieure.
	 */
	public double getMax () { return this.max; }


	/**
     * Retourne la valeur du rayon local de mutation.
	 */
	public double getRayon () { return this.rayon; }


	/**
     * Retourne la taille du <code>DomaineReel</code>.
	 */
	public double getTaille () { return this.taille; }

} /*----- Fin de la classe DomaineReel -----*/
