package indice;

import javalain.math.Calcul;
import math.Matrice;


/*-------------*/
/* KurtosisMax */
/*-------------*/
/**
 * Classe KurtosisMax.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class KurtosisMax extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public KurtosisMax (Matrice matrice)
		{
		super(matrice);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la valeur de l'indice unidimensionnel.
	 */
	public double calcul (double[] a)
		{
		this.nbEvaluation++;

		double[] Xi;
		double somme_i = 0.0;

		for (int i=0; i<this.N; i++)
			{
			/*----- Xi -----*/
			Xi = this.matrice.ligne(i);

			somme_i += Calcul.pow4(Calcul.produitScalaireVecteur(a, Xi));
			}

		return somme_i;
		}

} /*----- Fin de la classe KurtosisMax -----*/
