package javalain.math;

import java.util.ArrayList;
import java.util.Collections;


/*--------------*/
/* IndiceValeur */
/*--------------*/
/**
 * La classe <code>IndiceValeur</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	22 juin 2010
 */

public class IndiceValeur implements Comparable<IndiceValeur>
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Indice -----*/
	private int indice;

	/*----- Valeur -----*/
	private double valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public IndiceValeur (int i, double d)
		{
		this.indice = i;
		this.valeur = d;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les propriétés de l'objet.
	 */
	public double getValeur() { return this.valeur; }
	public int getIndice() { return this.indice; }


	/**
	 * Ordonne les valeurs des données dans l'ordre croissant.
	 */
	public int compareTo(IndiceValeur e)
		{
		if (this.valeur > e.valeur)
			return 1;
		else
			if (this.valeur < e.valeur)
				return -1;
			else
				return 0;
		}


	/*------*/
	/* Main */
	/*------*/

	public static void main (String[] s)
		{
		/*----- Initilisation de la liste -----*/
		ArrayList<IndiceValeur> liste = new ArrayList<IndiceValeur>();

		/*----- Pour le test -----*/
		liste.add(new IndiceValeur(123, Double.POSITIVE_INFINITY));
		liste.add(new IndiceValeur(456, 3.5));
		liste.add(new IndiceValeur(789, Double.POSITIVE_INFINITY));

		for (int i=0; i<5; i++)
			liste.add(new IndiceValeur(i, PseudoRandomNumbers.random()*10));

		/*----- Affichage de la liste avant le tri -----*/
		System.out.println("----- Non trié -----");
		for (int i=0; i<liste.size(); i++)
			System.out.print("(" + liste.get(i).getIndice() + " : " + liste.get(i).getValeur() + ")");
		System.out.println();

		/*----- Tri -----*/
		Collections.sort(liste);

		/*----- Affichage de la liste avant le tri -----*/
		System.out.println("----- Trié -----");
		for (int i=0; i<liste.size(); i++)
			System.out.print("(" + liste.get(i).getIndice() + " : " + liste.get(i).getValeur() + ")");
		System.out.println();
		}

} /*----- Fin de la classe IndiceValeur -----*/
