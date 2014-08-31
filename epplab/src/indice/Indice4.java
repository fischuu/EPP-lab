package indice;

import javalain.math.Calcul;
import math.Matrice;


/*---------*/
/* Indice4 */
/*---------*/
/**
 * Classe Indice4.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class Indice4 extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public Indice4 (Matrice matrice)
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
		double somme_i = 0.0 ;
		double d;

		for(int i=0; i<this.N ; i++)
			{
			Xi = this.matrice.ligne(i);
			d = Calcul.pow2(Calcul.produitScalaireVecteur(a, Xi));
			somme_i += Indice4.fct_K(d) * d;
			}

		return -somme_i;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Retourne la valeur de la fonction K.
	 */
	private static double fct_K (double x)
		{
		double beta = 0.01; // ou 0.1 ancienne version

		return Math.exp(-0.5*beta*x);
		}

} /*----- Fin de la classe Indice4 -----*/
