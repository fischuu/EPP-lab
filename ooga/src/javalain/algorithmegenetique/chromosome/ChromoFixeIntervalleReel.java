package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.gene.IntervalleReel;


/*--------------------------*/
/* ChromoFixeIntervalleReel */
/*--------------------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes
 * <code>IntervalleReel</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeIntervalleReel extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeIntervalleReel</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeIntervalleReel</code>
	 * @param	dr			domaine de définition des <code>IntervalleReel</code>s
	 *						du <code>ChromoFixeIntervalleReel</code>
	 */
	public ChromoFixeIntervalleReel (int			taille,
									 int			mode_crois,
									 DomaineReel	dr)
		{
		super(new IntervalleReel(dr),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>IntervalleReel</code> de locus (position) i.
	 */
	public IntervalleReel getIntervalle (int i) { return (IntervalleReel)this.getGene(i); }


	/**
	 * Retourne la valeur de la borne inférieure de
	 * l'<code>IntervalleReel</code> en position <code>i</code>.
	 */
	public double getBorneInf (int i) { return ((IntervalleReel)this.getGene(i)).getBorneInf(); }


	/**
	 * Retourne la valeur de la borne supérieure de
	 * l'<code>IntervalleReel</code> en position <code>i</code>.
	 */
	public double getBorneSup (int i) { return ((IntervalleReel)this.getGene(i)).getBorneSup(); }

}  /*----- Fin de la classe ChromoFixeIntervalleReel -----*/
