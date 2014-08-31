package javalain.algorithmegenetique.gene;

import javalain.algorithmegenetique.Gene;
import javalain.math.PseudoRandomNumbers;


/*------*/
/* Trit */
/*------*/
/**
 * La classe <code>Trit</code> définit un type à 3 états représentés pour
 * l'utilisateur par les caractères '0','1' ou '*'.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public final class Trit extends Gene
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Valeur du <code>Trit</code>.
	 */
	private char valeur;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initiale aléatoirement un <code>Trit</code>.
	 */
	public Trit ()
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: this.valeur = '0'; break;
			case 1	: this.valeur = '1'; break;
			default	: this.valeur = '*';
			}
		}


	private Trit (char c) { this.valeur = c; }


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie la valeur du <code>Trit</code>.
	 *
	 * @param	c	nouvelle valeur
	 */
	public void set (char c)
		{
		if	(c == '0' || c == '1' || c == '*')
			this.valeur = c;
		else
			throw(new IllegalArgumentException("Trit, set (char c) : caractere non valide ('0', '1' ou '*')."));
		}


	/**
	 * Retourne la valeur du <code>Trit</code>.
	 */
	public char get () { return this.valeur; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>Trit</code>s.
	 *
	 * @param	g	<code>Trit</code> mélangé avec this
	 *
	 * @return	<code>Trit</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		if	(this.valeur == ((Trit)g).valeur)
			return new Trit(this.valeur);
		else
			return new Trit();
		}


	/**
	 * Mutation du <code>Trit</code>.
	 */
	public void mutation ()
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: this.valeur = '0'; break;
			case 1	: this.valeur = '1'; break;
			default	: this.valeur = '*';
			}
		}


	/**
	 * Crée une copie du <code>Trit</code> ("deep copy").
	 */
	public Gene copier () { return new Trit(this.valeur); }


	/**
	 * Crée un nouveau <code>Trit</code>.
	 */
	public Gene creer () { return new Trit(); }


	/**
	 * Crée un nouveau <code>Trit</code> en fonction d'un autre
	 * <code>Gene</code>.
	 * <p>
	 * La valeur du nouveau <code>Trit</code> sera égale ou généralisera
	 * la valeur du <code>Gene</code> passé en paramètre.
	 * <ul>
	 *	<li><code>0</code> deviendra <code>0</code> ou <code>*</code>
	 *	<li><code>1</code> deviendra <code>1</code> ou <code>*</code>
	 *	<li><code>*</code> deviendra <code>*</code>
	 * </ul>
	 * Ce comportement est notamment utilisé dans les systèmes de classeurs.
	 *
	 * @param	g	<code>Bit</code> ou <code>Trit</code> utilisé pour
	 *				initialiser la valeur du nouveau <code>Trit</code>
	 *
	 * @exception	IllegalArgumentException si <code>g</code> n'est pas une
	 *				instance de la classe <code>Bit</code> ou <code>Trit</code>
	 */
	@Override
	public Gene creer (Gene g)
		{
		if	(g instanceof Bit)
			{
			if	(PseudoRandomNumbers.random() > 0.5)
				return new Trit('*');
			else
				return new Trit(((Bit)g).get());
			}

		if	(g instanceof Trit)
			{
			if	(PseudoRandomNumbers.random() > 0.5)
				return new Trit('*');
			else
				return g.copier();
			}

		throw new IllegalArgumentException("Trit, creer (Gene g) : g doit etre une instance de Bit ou Trit.");
		}


	/**
	 * Compare la valeur de deux <code>Trit</code>s ou
	 * d'un <code>Trit</code> et d'un <code>Bit</code>.
	 *
	 * @param	g	<code>Trit</code> ou <code>Bit</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof Trit)
			{
			if (this.valeur == ((Trit)g).valeur)
				return true;
			else
				return false;
			}

		if	(g instanceof Bit)
			{
			if (this.valeur == ((Bit)g).get())
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Compare si this est plus général que <code>g</code>.
	 *
	 * @param	g	<code>Bit</code> ou <code>Trit</code> comparé
	 *
	 * @return	<code>true</code> si la valeur de this généralise la valeur
	 *			de <code>g</code>
	 */
	@Override
	public boolean estPlusGeneralQue (Gene g)
		{
		if	(g instanceof Bit)
			{
			if	(this.valeur == '*' || this.valeur == ((Bit)g).get())
				return true;
			else
				return false;
			}

		if	(g instanceof Trit)
			{
			if	(this.valeur == '*' || this.valeur == ((Trit)g).valeur)
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>Trit</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder().append(this.valeur); }

} /*----- Fin de la classe Trit -----*/
