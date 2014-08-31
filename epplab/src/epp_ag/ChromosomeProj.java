package epp_ag;

import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.chromosome.ChromoFixeReel;
import javalain.math.Calcul;


/*-----------------------*/
/* Classe ChromosomeProj */
/*-----------------------*/
/**
 * La classe ChromosomeProj ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	21 mars 2010
 */
public class ChromosomeProj extends ChromoFixeReel
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public ChromosomeProj (int dim,
						   int mode_crois)
		{
		super(dim, mode_crois, new DomaineReel(-1.0, 1.0));
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le vecteur projection normalisé contenu dans le chromosome.
	 */
	public double[] getVecteurProjectionNormalise ()
		{
		/*----- Normalise la projection courante -----*/
		return Calcul.normaliseVecteur(this.toArray());
		}

} /*----- Fin de la classe ChromosomeProj -----*/
