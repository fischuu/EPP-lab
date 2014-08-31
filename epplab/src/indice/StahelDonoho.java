package indice;

import javalain.math.Calcul;
import math.Matrice;


/*--------------*/
/* StahelDonoho */
/*--------------*/
/**
 * Classe StahelDonoho.
 *
 * @author	Alain Berro
 * @version	26 mai 2010
 */
public final class StahelDonoho extends Indice
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public StahelDonoho (Matrice matrice)
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

		/*----- Calcul des données projetées -----*/
		double[] proj = new double[this.N];

		for (int i=0; i<this.N; i++)
			proj[i] = Calcul.produitScalaireVecteur(a, this.matrice.ligne(i));

		/*----- Calcul des médianes -----*/
		double med = Calcul.med(proj);
		double mad = Calcul.mad(proj);

		/*----- Recherche de la valeur supérieure -----*/
		double max = Math.abs(proj[0]-med)/mad;
		for (int i=1; i<this.N; i++)
			max = Calcul.max(Math.abs(proj[i]-med)/mad, max);

		return max;
		}

} /*----- Fin de la classe StahelDonoho -----*/
