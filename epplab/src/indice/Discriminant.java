package indice;

import javalain.math.Calcul;
import math.Matrice;


/*--------------*/
/* Discriminant */
/*--------------*/
/**
 * Classe Discriminant (I3).
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class Discriminant extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public Discriminant (Matrice matrice)
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
		double[] Xj;

		double somme_j;
		double somme_j_denominateur;

		double somme_i = 0.0;
		double somme_i_denominateur = 0.0;

		double beta = 2.0;
		double d, e;

		for(int i=0; i<this.N-1 ; i++)
			{
			/*----- Xi -----*/
			Xi = this.matrice.ligne(i);

			somme_j = 0.0;
			somme_j_denominateur = 0.0;
			for (int j=i+1; j<this.N; j++)
				{
				/*----- Xj ----- */
				Xj = this.matrice.ligne(j);

				d = Calcul.pow2(Calcul.produitScalaireVecteur(a, Calcul.differenceVecteur(Xi, Xj)));
				e = Discriminant.fct_K(d, beta);

				somme_j += e * d;
				somme_j_denominateur += e;
				}

			somme_i += somme_j;
			somme_i_denominateur += somme_j_denominateur;
			}

		return -somme_i/somme_i_denominateur;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Fonction K.
	 */
	private static double fct_K (double d, double beta)
		{
		return Math.exp(-(beta/2) * d);
		}

} /*----- Fin de la classe Discriminant -----*/
