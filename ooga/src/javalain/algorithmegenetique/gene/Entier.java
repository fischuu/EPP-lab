package javalain.algorithmegenetique.gene;

import javalain.algorithmegenetique.DomaineEntier;
import javalain.algorithmegenetique.Gene;
import javalain.math.PseudoRandomNumbers;


/*--------*/
/* Entier */
/*--------*/
/**
 * La classe <code>Entier</code> définit un gène de type entier.
 * Vous devez impérativement créer une instance de classe
 * <code>DomaineEntier</code> afin de définir le domaine de valeurs de
 * votre <code>Entier</code> et son rayon local de mutation.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public final class Entier extends Gene
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Domaine de valeurs.
	 */
	private DomaineEntier interv;


	/**
	 * Valeur de l'<code>Entier</code>.
	 */
	private int valeur;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initialise aléatoirement un <code>Entier</code>.
	 *
	 * @param	i	domaine de valeurs dans lequel sera choisi la valeur du gène
	 */
	public Entier (DomaineEntier i)
		{
		this.interv = i;
		this.valeur = (int)(PseudoRandomNumbers.random()*this.interv.getTaille()) + this.interv.getMin();
		}


	private Entier (DomaineEntier i,
					int d)
		{
		this.interv = i;
		this.valeur = d;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie la valeur de l'<code>Entier</code>.
	 */
	public void set (int i)
		{
		if	(i >= this.interv.getMin() && i <= this.interv.getMax())
			this.valeur = i;
		else
			throw(new IllegalArgumentException("Entier, set (int i) : le nombre n'appartient pas à l'intervalle de definition."));
		}

	/**
	 * Retourne la valeur d'un <code>Entier</code>.
	 */
	public int get() { return this.valeur; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>Entier</code>s.
	 *
	 * @param	g	<code>Entier</code> mélangé avec this
	 *
	 * @return	<code>Entier</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: return new Entier(this.interv,this.valeur);
			case 1	: return new Entier(this.interv,((Entier)g).valeur);
			default	:
					{
					double x = PseudoRandomNumbers.random()*1.5 - 0.25;
					int y = (int)(x*this.valeur + (1-x)*((Entier)g).valeur);
					if	(y > this.interv.getMax()) y = this.interv.getMax();
					if	(y < this.interv.getMin()) y = this.interv.getMin();

					return new Entier (this.interv,y);
					}
			}
		}


	/**
	 * Mutation d'un <code>Entier</code>.
	 * <p>
	 * Un gène <code>Entier</code> peut muter de trois manières différentes :
	 * <ul><li>addition d'une valeur aléatoire comprise
	 *         dans [0, rayon local de mutation[ ,
	 *     <li>soustraction d'une valeur aléatoire comprise
	 *         dans [0, rayon local de mutation[ ou
	 *     <li>remplacement par une valeur choisie aléatoirement
	 *         dans son domaine de valeurs.
	 * </ul>
	 * Chaque mutation a la même probabilité de survenir.
	 */
	public void mutation()
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: this.valeur += (int)(PseudoRandomNumbers.random()*Math.min(this.interv.getRayon(),this.interv.getMax()-this.valeur+1)); break;
			case 1	: this.valeur -= (int)(PseudoRandomNumbers.random()*Math.min(this.interv.getRayon(),this.valeur-this.interv.getMin()+1)); break;
			default	: this.valeur  = (int)(PseudoRandomNumbers.random()*this.interv.getTaille()) + this.interv.getMin();
			}
		}


	/**
	 * Crée une copie d'un <code>Entier</code> ("deep copy").
	 */
	public Gene copier () { return new Entier(this.interv,this.valeur); }


	/**
	 * Crée un nouvel <code>Entier</code>.
	 */
	public Gene creer () { return new Entier(this.interv); }


	/**
	 * Compare la valeur de deux <code>Entier</code>s ou
	 * d'un <code>Entier</code> et d'un <code>ReelDouble</code>.
	 *
	 * @param	g	<code>Entier</code> ou <code>ReelDouble</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof Entier)
			{
			if	(this.valeur == ((Entier)g).valeur)
				return true;
			else
				return false;
			}

		if	(g instanceof ReelDouble)
			{
			if	(this.valeur == ((ReelDouble)g).get())
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>Entier</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder().append(this.valeur); }

} /*----- Fin de la classe Entier -----*/