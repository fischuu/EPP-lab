package javalain.algorithmegenetique;

import java.io.Serializable;


/*---------------*/
/* DomaineEntier */
/*---------------*/
/**
 * Domaine de valeurs pour un nombre entier.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public class DomaineEntier implements Serializable
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
     * Borne inférieure du <code>DomaineEntier</code>.
	 */
	private int min;


	/**
     * Borne supérieure du <code>DomaineEntier</code>.
	 */
	private int max;


	/**
     * Taille du <code>DomaineEntier</code>.
	 */
	private int taille;


	/**
     * Rayon local de mutation.
	 * <p>
	 * Le rayon local de mutation influence la mutation d'un gène
	 * <code>Entier</code>.
	 */
	private int rayon;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
     * Construit un <code>DomaineEntier</code>.
	 *
	 * @param	min	borne inférieure
	 * @param	max	borne supérieure
	 *
	 * @exception	RuntimeException si <code>min</code> est > à
	 *				<code>max</code>
	 */
	public DomaineEntier (int min,
						  int max)
		{
		if	(min > max) throw new RuntimeException("DomaineEntier, DomaineEntier (int min, int max) : min > max.");
		else
			{
			this.min = min;
			this.max = max;
			this.taille = max-min+1;

			if  (this.taille == 1)
				this.rayon = 0;
			else
				if	(this.taille < 21)
					this.rayon = this.taille/10 + 1;
				else
					this.rayon = (int)Math.log(this.taille);
			}
		}


	/**
     * Construit un <code>DomaineEntier</code> avec un rayon local de mutation.
	 *
	 * @param	min		borne inférieure
	 * @param	max		borne supérieure
	 * @param	rayon	rayon local de mutation
	 *
	 * @exception	RuntimeException si <code>min</code> est > à
	 *				<code>max</code>
	 */
	public DomaineEntier (int min,
						  int max,
						  int rayon)
		{
		if	(min > max) throw new RuntimeException("DomaineEntier, DomaineEntier (int min, int max, int rayon) : min > max.");
		else
			{
			this.min = min;
			this.max = max;
			this.taille = max-min+1;
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
	public void setMin (int nb)
		{
		if	(nb > this.max) throw new RuntimeException("DomaineEntier, setMin (int nb) : nb est > a la borne superieure du domaine.");
		else
			{
			this.min = nb;
			this.taille = this.max-nb+1;
			}
		}


	/**
     * Modifie la valeur de la borne supérieure.
	 *
	 * @exception	RuntimeException si <code>nb</code> est < à la
	 *				borne inférieure
	 */
	public void setMax (int nb)
		{
		if	(nb < this.min) throw new RuntimeException("DomaineEntier, setMax (int nb) : nb est < a la borne inferieure du domaine.");
		else
			{
			this.max = nb;
			this.taille = nb-this.min+1;
			}
		}


	/**
     * Modifie la valeur du rayon local de mutation.
	 */
	public void setRayon (int nb) { this.rayon = nb; }


	/**
     * Retourne la valeur de la borne inférieure.
	 */
	public int getMin () { return this.min; }


	/**
     * Retourne la valeur de la borne supérieure.
	 */
	public int getMax () { return this.max; }


	/**
     * Retourne la valeur du rayon local de mutation.
	 */
	public int getRayon () { return this.rayon; }


	/**
     * Retourne la taille du <code>DomaineEntier</code>.
	 */
	public int getTaille () { return this.taille; }

} /*----- Fin de la classe DomaineEntier -----*/
