package javalain.algorithmegenetique.chromosome;

import javalain.algorithmegenetique.ChromosomeFixe;
import javalain.algorithmegenetique.DomaineEntier;
import javalain.algorithmegenetique.gene.Entier;


/*------------------*/
/* ChromoFixeEntier */
/*------------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes <code>Entier</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromoFixeEntier extends ChromosomeFixe
{
	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromoFixeEntier</code>.
	 *
	 * @param	taille		taille du chromosome
	 * @param	mode_crois	mode de croisement du <code>ChromoFixeEntier</code>
	 * @param	de			domaine de définition des <code>Entier</code>s
	 *						du <code>ChromoFixeEntier</code>
	 */
	public ChromoFixeEntier (int	        taille,
							 int			mode_crois,
							 DomaineEntier	de)
		{
		super(new Entier(de),taille,mode_crois);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le gène <code>Entier</code> de locus (position) <code>i</code>.
	 */
	public Entier getEntier (int i) { return (Entier)this.getGene(i); }


	/**
	 * Retourne la valeur (allèle) du <code>Entier</code> en position
	 * <code>i</code>.
	 */
	public int getAllele (int i) { return ((Entier)this.getGene(i)).get(); }

} /*----- Fin de la classe ChromoFixeEntier -----*/
