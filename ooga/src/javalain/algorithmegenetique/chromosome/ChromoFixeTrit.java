package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.gene.Trit;


/*----------------*/
/* ChromoFixeTrit */
/*----------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes <code>Trit</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeTrit extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeTrit</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeReel</code>
	 */
	public ChromoFixeTrit (int taille,
						   int mode_crois)
		{
		super(new Trit(),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>Trit</code> de locus (position) <code>i</code>.
	 */
	public Trit getTrit (int i) { return (Trit)this.getGene(i); }


	/**
	 * Retourne la valeur (allèle) du <code>Trit</code> en position
	 * <code>i</code>.
	 *
	 * @return	caractère '0', '1' ou '*'
	 */
	public char getAllele (int i) { return ((Trit)this.getGene(i)).get(); }


	/**
	 * Teste si this est un schéma de <code>cfb</code>.
	 *
	 * @param	cfb <code>ChromoFixeBit</code>
	 *
	 * @return	<code>true</code> si this est un schéma de cfb
	 */
	public boolean estSchemaDe (ChromoFixeBit cfb) { return this.estPlusGeneralQue(cfb); }

} /*----- Fin de la classe ChromoFixeTrit -----*/
