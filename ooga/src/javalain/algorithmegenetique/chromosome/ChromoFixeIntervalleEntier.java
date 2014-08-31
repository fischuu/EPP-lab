package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.DomaineEntier;
import javalain.algorithmegenetique.gene.IntervalleEntier;


/*----------------------------*/
/* ChromoFixeIntervalleEntier */
/*----------------------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes
 * <code>IntervalleEntier</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeIntervalleEntier extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeIntervalleEntier</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeIntervalleEntier</code>
	 * @param	de			domaine de définition des <code>IntervalleEntier</code>s
	 *						du <code>ChromoFixeIntervalleEntier</code>
	 */
	public ChromoFixeIntervalleEntier (int				taille,
									   int				mode_crois,
									   DomaineEntier	de)
		{
		super(new IntervalleEntier(de),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>IntervalleEntier</code> de locus (position) i.
	 */
	public IntervalleEntier getIntervalle (int i) { return (IntervalleEntier)this.getGene(i); }


	/**
	 * Retourne la valeur de la borne inférieure de
	 * l'<code>IntervalleEntier</code> en position <code>i</code>.
	 */
	public int getBorneInf (int i) { return ((IntervalleEntier)this.getGene(i)).getBorneInf(); }


	/**
	 * Retourne la valeur de la borne supérieure de
	 * l'<code>IntervalleEntier</code> en position <code>i</code>.
	 */
	public int getBorneSup (int i) { return ((IntervalleEntier)this.getGene(i)).getBorneSup(); }

} /*----- Fin de la classe ChromoFixeIntervalleEntier -----*/
