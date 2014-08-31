package epp_ag;

import indice.Indice;
import javalain.algorithmegenetique.Genome;
import javalain.algorithmegenetique.chromosome.ChromoFixeReel;
import javalain.math.Calcul;


/*-------------------*/
/* Classe GenomeProj */
/*-------------------*/
/**
 * La classe GenomeProj décrit la composition du génome de chaque individu.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	20 mars 2010
 */
public class GenomeProj extends Genome
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Indice
	 */
	private static Indice INDICE;


	/**
	 * Meilleure projection a* obtenue lors de la technique
	 * bidimensionnelle : I(a) puis I(a*,b).
	 */
	private double[] A = null;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise le génome.
	 */
	public GenomeProj (Indice i)
		{ 
		super(1);
		/*----- Indice -----*/
		INDICE = i;

		/*----- Codage du génome -----*/
		this.addChromosome(new ChromosomeProj(GenomeProj.INDICE.getNombreParametres(), ChromoFixeReel.MELANGE));
		this.A = null;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Calcul de l'indice de Friedmann.
	 */
	public void fitness()
		{       
		/*----- Vecteur projection de l'individu -----*/
		double[] a = ((ChromosomeProj)this.getChromosome(0)).getVecteurProjectionNormalise();
		this.setNote((float) GenomeProj.INDICE.calcul(a));
		}
	
} /*----- Fin de la classe GenomeProj -----*/