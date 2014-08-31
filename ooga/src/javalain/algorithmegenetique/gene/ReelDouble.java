package javalain.algorithmegenetique.gene;

import java.text.DecimalFormat;
import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.Gene;
import javalain.ea.operator.Mutator;
import util.GuiUtils;
import javalain.math.PseudoRandomNumbers;


/*------------*/
/* ReelDouble */
/*------------*/
/**
 * La classe <code>ReelDouble</code> définit un gène réel.
 * Vous devez impérativement créer une instance de classe
 * <code>DomaineReel</code> afin de définir le domaine de valeurs de
 * votre <code>ReelDouble</code> et son rayon local de mutation.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public final class ReelDouble extends Gene
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Format d'affichage du <code>ReelDouble</code>.
	 */
	private static DecimalFormat DF = GuiUtils.DECIMAL_3;


	/**
	 * Types de mutation possible du <code>ReelDouble</code>.
	 */
	public static enum type_mutation { MUTATION_PERSO, MUTATION_POLYNOMIALE };


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Domaine de valeurs du <code>ReelDouble</code>.
	 */
	private DomaineReel interv;


	/**
	 * Valeur du <code>ReelDouble</code>.
	 */
	private double valeur;


	/**
	 * Type de mutation du <code>ReelDouble</code>.
	 */
	private type_mutation mutation;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initialise aléatoirement un <code>ReelDouble</code>.
	 */
	public ReelDouble (DomaineReel i)
		{
		this.interv		= i;
		this.valeur		= PseudoRandomNumbers.random()*this.interv.getTaille() + this.interv.getMin();
		this.mutation	= type_mutation.MUTATION_PERSO;
		}


	private ReelDouble (DomaineReel	i,
						double		d)
		{
		this.interv		= i;
		this.valeur		= d;
		this.mutation	= type_mutation.MUTATION_PERSO;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie la valeur du <code>ReelDouble</code>.
	 *
	 * @param	d	nouvelle valeur
	 *
	 * @exception	IllegalArgumentException si la nouvelle valeur n'appartient
	 *				à l'intervalle de définition
	 */
	public void set (double d)
		{
		if	(this.interv.getMin() <= d && d <= this.interv.getMax())
			this.valeur = d;
		else
			throw(new IllegalArgumentException("ReelDouble, set (double d) : le nombre n'appartient pas à l'intervalle de definition."));
		}


	/**
	 * Retourne la valeur du <code>ReelDouble</code>.
	 */
	public double get () { return this.valeur; }


	/**
	 * Retourne le domaine de valeurs du <code>ReelDouble</code>.
	 */
	public DomaineReel getIntervalle () { return this.interv; }


	/**
	 * Modifie le format de représentation du <code>ReelDouble</code>
	 * en chaîne de caractères.
	 * <p>
	 * Par défaut le format est "0.000".
	 */
	public static void setDecimalFormat (DecimalFormat df) { ReelDouble.DF = df; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>ReelDouble</code>s.
	 *
	 * @param	g	<code>ReelDouble</code> mélangé avec this
	 *
	 * @return	<code>ReelDouble</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: return new ReelDouble(this.interv,this.valeur);
			case 1	: return new ReelDouble(((ReelDouble)g).interv,((ReelDouble)g).valeur);
			default	:
					{
					double x = PseudoRandomNumbers.random()*1.5 - 0.25;
					double y = x*this.valeur + (1-x)*((ReelDouble)g).valeur;
					if	(y > this.interv.getMax()) y = this.interv.getMax();
					if	(y < this.interv.getMin()) y = this.interv.getMin();

					return new ReelDouble(this.interv,y);
					}
			}
		}


	/**
	 * Mutation d'un <code>ReelDouble</code>.
	 * <p>
	 * Un <code>ReelDouble</code> peut muter de trois manières différentes :
	 * <ul>
	 *	<li>addition d'une valeur aléatoire comprise
	 *		dans [0, rayon local de mutation[ ,
	 *	<li>soustraction d'une valeur aléatoire comprise
	 *		dans [0, rayon local de mutation[ ou
	 *	<li>remplacement par une valeur choisie aléatoirement
	 *		dans son domaine de valeurs.
	 * </ul>
	 * Chaque mutation a la même probabilité de survenir.
	 */
	public void mutation ()
		{
		if (this.mutation == ReelDouble.type_mutation.MUTATION_PERSO)
			{
			/*----- Mutation perso -----*/
			switch((int)(PseudoRandomNumbers.random()*3))
				{
				case 0	: this.valeur += PseudoRandomNumbers.random()*Math.min(this.interv.getRayon(),this.interv.getMax()-this.valeur); break;
				case 1	: this.valeur -= PseudoRandomNumbers.random()*Math.min(this.interv.getRayon(),this.valeur-this.interv.getMin()); break;
				default	: this.valeur  = PseudoRandomNumbers.random()*this.interv.getTaille() + this.interv.getMin();
				}
			}
		else
			/*----- Mutation polynomiale -----*/
			this.valeur = Mutator.mutationPolynomiale(1.0/20.0,this.valeur,this.interv.getMin(),this.interv.getMax(), 20.0);
		}


	/**
	 * Crée une copie d'un <code>ReelDouble</code> ("deep copy").
	 */
	public Gene copier () { return new ReelDouble(this.interv,this.valeur); }


	/**
	 * Crée un nouveau <code>ReelDouble</code>.
	 */
	public Gene creer () { return new ReelDouble(this.interv); }


	/**
	 * Compare la valeur de deux <code>ReelDouble</code>s ou
	 * d'un <code>ReelDouble</code> et d'un <code>Entier</code>.
	 *
	 * @param	g	<code>ReelDouble</code> ou <code>Entier</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof ReelDouble)
			{
			if (this.valeur == ((ReelDouble)g).valeur)
				return true;
			else
				return false;
			}

		if	(g instanceof Entier)
			{
			if	(this.valeur == ((Entier)g).get())
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>ReelDouble</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder().append(ReelDouble.DF.format(this.valeur)); }

} /*----- Fin de la classe ReelDouble -----*/
