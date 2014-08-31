package indice;

import javalain.math.Calcul;
import math.Matrice;


/*---------------*/
/* FriedmanTukey */
/*---------------*/
/**
 * Classe FriedmanTukey.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public final class FriedmanTukey extends Indice
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/*----- H -----*/
	private final double H;

	/*----- Constante -----*/
	private final double CTE;

	private final double c0 = 35.0/32.0;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public FriedmanTukey (Matrice matrice)
		{
		super(matrice);

		/*----- Valeur de H -----*/
		this.H = 3.12*Math.pow(this.N, (-1.0/6.0));

		/*----- Valeur constante -----*/
		this.CTE = N*N*H*H;
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

		double somme_i = 0.0;
		double somme_j;
		for	(int i=0; i<this.N ; i++)
			{
			/*----- Xi -----*/
			Xi = this.matrice.ligne(i);

			somme_j = 0.0;
			for (int j=0; j<i-1; j++)
				{
				/*----- Xj -----*/
				Xj = this.matrice.ligne(j);

				somme_j += fct_K(Calcul.produitScalaireVecteur(a, Calcul.differenceVecteur(Xi, Xj)) / this.H);
				}

			somme_i += 0.5 + 2*somme_j;
			}

		return somme_i/this.CTE;
		}


	/**
	 * Fonction K.
	 */
	private double fct_K (double d)
		{
		if	(Math.abs(d) < this.H)
			return c0 * Calcul.pow3(1-d*d);
		else
			return 0.0;
		}

} /*----- Fin de la classe FriedmanTukey -----*/
