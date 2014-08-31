package javalain.algorithmegenetique.gene;

import javalain.algorithmegenetique.Gene;
import javalain.math.PseudoRandomNumbers;


/*-----*/
/* Bit */
/*-----*/
/**
 * La classe <code>Bit</code> définit un type à 2 états '0' ou '1'.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public final class Bit extends Gene
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Valeur du <code>Bit</code>.
	 */
	private char valeur;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initialise aléatoirement un <code>Bit</code>.
	 */
	public Bit ()
		{
		if	(PseudoRandomNumbers.random() < 0.5)
			this.valeur = '0';
		else
			this.valeur = '1';
		}


	private Bit (char c) { this.valeur = c; }


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie la valeur du <code>Bit</code>.
	 *
	 * @param	c	nouvelle valeur
	 */
	public void set (char c)
		{
		if	(c == '0' || c == '1')
			this.valeur = c;
		else
			throw(new IllegalArgumentException("Bit, set (char c) : caractere non valide ('0' ou '1')."));
		}


	/**
	 * Retourne la valeur du <code>Bit</code>.
	 *
	 * @return	le caractère '0' ou '1'
	 */
	public char get () { return this.valeur; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>Bit</code>s.
	 *
	 * @param	g	<code>Bit</code> mélangé avec this
	 *
	 * @return	<code>Bit</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		if	(this.valeur == ((Bit)g).valeur)
			return new Bit(this.valeur);
		else
			return new Bit();
		}


	/**
	 * Mutation d'un <code>Bit</code>.
	 */
	public void mutation ()
		{
		if	(PseudoRandomNumbers.random() < 0.5)
			this.valeur = '0';
		else
			this.valeur = '1';
		}


	/**
	 * Crée une copie de ce <code>Bit</code> ("deep copy").
	 */
	public Gene copier () { return new Bit(this.valeur); }


	/**
	 * Crée un nouveau <code>Bit</code>.
	 */
	public Gene creer () { return new Bit(); }


	/**
	 * Compare la valeur de deux <code>Bit</code>s ou
	 * d'un <code>Bit</code> avec un <code>Trit</code>.
	 *
	 * @param	g	<code>Bit</code> ou <code>Trit</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof Bit)
			{
			if	(this.valeur == ((Bit)g).valeur)
				return true;
			else
				return false;
			}

		if	(g instanceof Trit)
			{
			if	(this.valeur == ((Trit)g).get())
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme de chaîne de caractères
	 * d'un <code>Bit</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder().append(this.valeur); }

} /*----- Fin de la classe Bit -----*/
