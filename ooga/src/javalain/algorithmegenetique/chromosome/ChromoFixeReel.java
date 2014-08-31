package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.Gene;
import javalain.algorithmegenetique.gene.ReelDouble;


/*----------------*/
/* ChromoFixeReel */
/*----------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes <code>ReelDouble</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeReel extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeReel</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeReel</code>
	 */
	public ChromoFixeReel (int taille,
						   int mode_crois)
		{
		super(taille,mode_crois);
		}


	/**
	 * Construit et initialise un <code>ChromoFixeReel</code> tel que tous les
	 * gènes <code>ReelDouble</code> ont le même domaine de définition.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeReel</code>
	 * @param	dr			domaine de définition des <code>ReelDouble</code>s
	 *						du <code>ChromoFixeReel</code>
	 */
	public ChromoFixeReel (int			taille,
						   int			mode_crois,
						   DomaineReel	dr)
		{
		super(new ReelDouble(dr),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>ReelDouble</code> de locus (position) i.
	 */
	public ReelDouble getReelDouble (int i) { return (ReelDouble)this.getGene(i); }


	/**
	 * Retourne la valeur (allèle) du <code>ReelDouble</code> en position i.
	 */
	public double getAllele (int i) { return ((ReelDouble)this.getGene(i)).get(); }


	/**
	 * Retourne le chromosome sous forme d'un tableau de réels.
	 */
	public double[] toArray ()
		{
		int taille = this.size();
		double[] v = new double[taille];

		for (int i=0; i<taille; i++)
			v[i] = ((ReelDouble)this.getGene(i)).get();

		return v;
		}


	/**
     * Ajoute un gène <code>ReelDouble</code> au <code>ChromoFixeReel</code>.
	 *
	 * @exception	IllegalArgumentException si le gène n'est pas
	 *				un <code>ReelDouble</code>
	 */
	@Override
	public void addGene (Gene ge)
		{
		if (ge instanceof ReelDouble)
			super.addGene(ge);
		else
			throw new IllegalArgumentException("ChromoFixeReel, addGene (Gene ge) : le gene doit etre un ReelDouble.");
		}

} /*----- Fin de la classe ChromoFixeReel -----*/
