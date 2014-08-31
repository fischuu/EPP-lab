package indice;

import math.Matrice;

import javax.swing.JOptionPane;


/*--------*/
/* Indice */
/*--------*/
/**
 * Classe Indice.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public abstract class Indice
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Nombre d'évaluation de l'indice -----*/
	protected int nbEvaluation;

	/*----- Matrice des observations -----*/
	protected Matrice matrice;

	/*----- N - Nombre d'observations -----*/
	protected final int N;

	/*----- P - Nombre de paramètres -----*/
	protected final int P;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	protected Indice (Matrice matrice)
		{
		/*----- Nombre d'évaluations -----*/
		this.nbEvaluation = 0;

		/*----- Initialisation de la matrice des observations -----*/
		this.matrice = matrice;

		/*----- Dimension de la matrice -----*/
		this.N = matrice.nbLigne();
		this.P = matrice.nbColonne();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Mise à jour de la matrice.
	 */
	public void setMatrice (Matrice m)
		{
		/*----- Vérification de la taille de la matrice -----*/
		if (this.matrice.nbLigne() != m.nbLigne() ||
			this.matrice.nbColonne() != m.nbColonne())
			{
			JOptionPane.showMessageDialog(null,
										  "Taille incorrecte",
										  "Modification de la matrice de l'indice",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		this.matrice = m;
		}


	/**
	 * Retourne la matrice associée à l'indice.
	 */
	public Matrice getMatrice () { return this.matrice; }


	/**
	 * Retourne le nombre de paramètres.
	 */
	public int getNombreParametres () { return this.P; }


	/**
	 * Retourne le nombre d'évaluations de l'indice.
	 */
	public int getNbEvaluation () { return this.nbEvaluation; }


	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Retourne la valeur de l'indice unidimensionnel.
	 */
	public abstract double calcul (double[] a);
       
} /*----- Fin de la classe Indice -----*/
