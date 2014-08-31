package indice;

import javalain.math.Calcul;
import math.Matrice;


/*-------------*/
/* KurtosisMin */
/*-------------*/
/**
 * Classe KurtosisMin.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class KurtosisMin extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public KurtosisMin (Matrice matrice)
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

		return -somme_i;
		}


	/**
	 * Retourne la valeur du gradient au point 'a'.
	 */
	public double[] gradient (double[] a)
		{
		double[] Xi;
		double[] somme_i = new double[a.length];

		for (int i=0; i<this.N; i++)
			{
			/*----- Xi -----*/
			Xi = this.matrice.ligne(i);

			somme_i = Calcul.sommeVecteur(somme_i, Calcul.multReelVecteur(Calcul.pow3(Calcul.produitScalaireVecteur(a, Xi)), Xi));
			}

		return Calcul.multReelVecteur(4.0, somme_i);
		}

} /*----- Fin de la classe KurtosisMin -----*/
