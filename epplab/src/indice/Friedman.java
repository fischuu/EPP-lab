package indice;

import javalain.math.Calcul;
import math.Matrice;
import umontreal.iro.lecuyer.probdist.NormalDist;


/*----------*/
/* Friedman */
/*----------*/
/**
 * Classe Friedman.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class Friedman extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public Friedman (Matrice matrice)
		{
		super(matrice);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la valeur de l'indice unidimensionnel.
	 */
	@Override
	public double calcul (double[] a)
		{
		this.nbEvaluation++;

		double[] Xi;

		double somme_i ;
		double somme_j = 0.0;

		/*----- Paramètre -----*/
		int m = 3;

		for(int j=0; j<m ; j++)
			{
			somme_i = 0;
			for (int i=0; i<this.N; i++)
				{
				Xi = this.matrice.ligne(i);

				somme_i += Friedman.polynome(j,(2*NormalDist.cdf01(Calcul.produitScalaireVecteur(a, Xi))-1));
				}

			somme_j += (2*j+1)/2.0 * Calcul.pow2(somme_i/this.N);
			}

		return  somme_j;
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Retourne la valeur du polynome.
	 */
	private static double polynome (int j, double r)
		{
		double L = 0;

		if (j==0)	L = 1.0;
		if (j==1)	L = r;
		if (j==2)	L = 1.0/2*(3*r*r-1);
		if (j>2)	L = 1.0/j*((2*j-1) * r * Friedman.polynome(j-1,r) - (j-1) * Friedman.polynome(j-2,r));

		return L;
		}

} /*----- Fin de la classe Friedman -----*/
