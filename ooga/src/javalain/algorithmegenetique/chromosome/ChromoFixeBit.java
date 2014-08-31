package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.gene.Bit;


/*---------------*/
/* ChromoFixeBit */
/*---------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes <code>Bit</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeBit extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeBit</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeBit</code>
	 */
	public ChromoFixeBit (int taille,
						  int mode_crois)
		{
		super(new Bit(),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>Bit</code> de locus (position) <code>i</code>.
	 */
	public Bit getBit (int i) { return (Bit)this.getGene(i); }


	/**
	 * Retourne la valeur (allèle) du <code>Bit</code> en position
	 * <code>i</code>.
	 *
	 * @return	caractère '0' ou '1'
	 */
	public char getAllele (int i) { return ((Bit)this.getGene(i)).get(); }

} /*----- Fin de la classe ChromoFixeBit -----*/
