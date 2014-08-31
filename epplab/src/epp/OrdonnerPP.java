package epp;


/*-------------------*/
/* Classe OrdonnerPP */
/*-------------------*/
/**
 * La classe <code>OrdonnerPP</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	22 juin 2010
 */

public class OrdonnerPP implements Comparable<OrdonnerPP>
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Numéro de la simulation -----*/
	private int numSimulation;

	/*----- Meilleure valeur d'indice obtenue lors de la simulation -----*/
	private double I;

	/*----- Vecteur de projection -----*/
	private double[] A;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public OrdonnerPP (int numero, double i, double[] a)
		{
		this.numSimulation = numero;
		this.I = i;
		this.A = a;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * 
	 */
	public double[] getA() { return this.A; }
	public double getI() { return this.I; }
	public int getNumSimulation() { return this.numSimulation; }


	/**
	 * Ordonne les valeurs des données dans l'ordre décroissant.
	 */
	public int compareTo(OrdonnerPP e)
		{
		if (this.I > e.I)
			return -1;
		else
			if (this.I < e.I)
				return 1;
			else
				return 0;
		}

} /*----- Fin de la classe OrdonnerPP -----*/
