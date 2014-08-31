package javalain.algorithmegenetique.gene;

import java.text.DecimalFormat;
import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.Gene;
import util.GuiUtils;
import javalain.math.PseudoRandomNumbers;


/*----------------*/
/* IntervalleReel */
/*----------------*/
/**
 * La classe <code>IntervalleReel</code> définit un gène représentant
 * un intervalle définit par des nombres réels.
 * Vous devez impérativement créer une instance de classe
 * <code>DomaineReel</code> afin de définir le domaine de valeur de
 * votre <code>IntervalleReel</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public final class IntervalleReel extends Gene
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Format d'affichage des bornes de l'<code>IntervalleReel</code>.
	 */
	private static DecimalFormat df = GuiUtils.DECIMAL_3;


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Domaine de définition de l'<code>IntervalleReel</code>.
	 */
	private DomaineReel domaine;


	/**
	 * Bornes de l'<code>IntervalleReel</code>.
	 */
	private double borne_inf;
	private double borne_sup;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initialise aléatoirement l'<code>IntervalleReel</code>.
	 */
	public IntervalleReel (DomaineReel i)
		{
		double taille	= i.getTaille();
		double min		= i.getMin();

		this.domaine	= i;
		this.borne_inf	= PseudoRandomNumbers.random()*taille + min;
		this.borne_sup	= PseudoRandomNumbers.random()*taille + min;

		if	(this.borne_inf > this.borne_sup)
			{
			min			= this.borne_inf;
			this.borne_inf	= this.borne_sup;
			this.borne_sup	= min;
			}
		}


	private IntervalleReel (DomaineReel	i,
							double		inf,
							double		sup)
		{
		this.domaine	= i;
		this.borne_inf 	= inf;
		this.borne_sup	= sup;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la borne inférieure de l'<code>IntervalleReel</code>.
	 */
	public double getBorneInf () { return this.borne_inf; }


	/**
	 * Retourne la borne supérieure de l'<code>IntervalleReel</code>.
	 */
	public double getBorneSup () { return this.borne_sup; }


	/**
	 * Retourne le domaine de définition de l'<code>IntervalleReel</code>.
	 *
	 * @return	un <code>DomaineReel</code>
	 */
	public DomaineReel getDomaineDefinition () { return this.domaine; }


	/**
	 * Modifie le format de représentation de l'<code>IntervalleReel</code>
	 * en chaîne de caractères.
	 * <p>
	 * Par défaut le format est "0.000".
	 */
	public void setDecimalFormat (DecimalFormat df) { IntervalleReel.df = df; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>IntervalleReel</code>s.
	 *
	 * @param	g	<code>IntervalleReel</code> mélangé avec this
	 *
	 * @return	<code>IntervalleReel</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: return new IntervalleReel(this.domaine,this.borne_inf,this.borne_sup);
			case 1	:
					{
					IntervalleReel ir = (IntervalleReel)g;
					return new IntervalleReel(this.domaine,ir.borne_inf,ir.borne_sup);
					}
			default	:
					{
					double aux, inf, sup;
					IntervalleReel ir = (IntervalleReel)g;

					aux	= PseudoRandomNumbers.random()*1.5 - 0.25;
					inf	= aux*this.borne_inf + (1.0-aux)*ir.borne_inf;
					sup	= aux*this.borne_sup + (1.0-aux)*ir.borne_sup;

					if	(inf > sup) { aux = inf; inf = sup; sup = aux; }

					aux = this.domaine.getMin();
					if	(inf < aux)
						{
						if	(sup < aux) return new IntervalleReel(this.domaine); // Echec du mélange, intervalle hors domaine

						inf = aux;
						}

					aux = this.domaine.getMax();
					if	(sup > aux)
						{
						if	(inf > aux) return new IntervalleReel(this.domaine); // Echec du mélange, intervalle hors domaine

						sup = aux;
						}

					return new IntervalleReel(this.domaine,inf,sup);
					}
			}
		}


	/**
	 * Mutation d'un <code>IntervalleReel</code>.
	 */
	public void mutation ()
		{
		switch ((int)(PseudoRandomNumbers.random()*8))
			{
			case 0	: this.borne_inf += PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_sup-this.borne_inf); break;
			case 1	: this.borne_inf += PseudoRandomNumbers.random()*(this.borne_sup-this.borne_inf); break;

			case 2	: this.borne_inf -= PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_inf-this.domaine.getMin()); break;
			case 3	: this.borne_inf -= PseudoRandomNumbers.random()*Math.min(this.borne_sup-this.borne_inf,this.borne_inf-this.domaine.getMin()); break;

			case 4	: this.borne_sup += PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.domaine.getMax()-this.borne_sup); break;
			case 5	: this.borne_sup += PseudoRandomNumbers.random()*Math.min(this.borne_sup-this.borne_inf,this.domaine.getMax()-this.borne_sup); break;

			case 6	: this.borne_sup -= PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_sup-this.borne_inf); break;
			case 7	: this.borne_sup -= PseudoRandomNumbers.random()*(this.borne_sup-this.borne_inf); break;
			default	:
			}
		}


	/**
	 * Crée une copie de l'<code>IntervalleReel</code> ("deep copy").
	 */
	public Gene copier () { return new IntervalleReel(this.domaine,this.borne_inf,this.borne_sup); }


	/**
	 * Crée un nouvel <code>IntervalleReel</code>.
	 */
	public Gene creer () { return new IntervalleReel(this.domaine); }


	/**
	 * Crée un nouvel <code>IntervalleReel</code> en fonction
	 * d'un autre <code>Gene</code>.
	 * <p>
	 * Le nouvel <code>IntervalleReel</code> inclura la valeur
	 * du <code>Gene</code> passé en paramètre.
	 *
	 * @param	g	<code>ReelDouble</code> ou <code>IntervalleReel</code>
	 *				utilisé pour initialiser le nouvel
	 *				<code>IntervalleReel</code>
	 *
	 * @exception	IllegalArgumentException si <code>g</code> n'est pas
	 *				une instance de la classe <code>ReelDouble</code> ou
	 *				<code>IntervalleReel</code>, ou  si la valeur de
	 *				<code>g</code> n'est pas inclus dans le domaine
	 *				de definition de this
	 */
	@Override
	public Gene creer (Gene g)
		{
		if	(g instanceof ReelDouble)
			{
			double rd	= ((ReelDouble)g).get();
			double dmin	= this.domaine.getMin();
			double dmax	= this.domaine.getMax();

			if	(dmin <= rd && rd <= dmax)
				{
				double diff_inf = rd - dmin;
				double diff_sup = dmax - rd;

				return new IntervalleReel(this.domaine,rd-PseudoRandomNumbers.random()*diff_inf,rd+PseudoRandomNumbers.random()*diff_sup);
				}
			else
				throw new IllegalArgumentException("IntervalleReel, creer (Gene g) : le ReelDouble passe en parametre n'est pas inclus dans le domaine de definition de this.");
			}

		if	(g instanceof IntervalleReel)
			{
			IntervalleReel ir = (IntervalleReel)g;
			double binf = ir.borne_inf;
			double bsup = ir.borne_sup;
			double dmin = this.domaine.getMin();
			double dmax = this.domaine.getMax();

			if	(dmin <= binf && bsup <= dmax)
				{
				double diff_inf = binf - dmin;
				double diff_sup = dmax - bsup;

				return new IntervalleReel(this.domaine,dmin+PseudoRandomNumbers.random()*diff_inf,dmax-PseudoRandomNumbers.random()*diff_sup);
				}
			else
				throw new IllegalArgumentException("IntervalleReel, creer (Gene g) : l'IntervalleReel passe en parametre n'est pas inclus dans le domaine de definition de this.");
			}

		throw new IllegalArgumentException("IntervalleReel, creer (Gene g) : g doit etre une instance de ReelDouble ou IntervalleReel.");
		}


	/**
	 * Compare la valeur de deux <code>IntervalleReel</code>s.
	 *
	 * @param	g	<code>IntervalleReel</code> comparé
	 *
	 * @return	<code>true</code> si les deux <code>IntervalleReel</code>s sont
	 *			égaux, <code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof IntervalleReel)
			{
			IntervalleReel ir = (IntervalleReel)g;

			if	(this.borne_inf == ir.borne_inf && this.borne_sup == ir.borne_sup)
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Compare si this est plus général que <code>g</code>.
	 *
	 * @param	g	<code>ReelDouble</code> ou <code>IntervalleReel</code>
	 *				comparé
	 *
	 * @return	<code>true</code> si la valeur de this inclus la valeur
	 *			de <code>g</code>
	 */
	@Override
	public boolean estPlusGeneralQue (Gene g)
		{
		if	(g instanceof ReelDouble)
			{
			double rd = ((ReelDouble)g).get();

			if	(this.borne_inf <= rd && rd <= this.borne_sup)
				return true;
			else
				return false;
			}

		if	(g instanceof IntervalleReel)
			{
			IntervalleReel ir = (IntervalleReel)g;

			if	(this.borne_inf <= ir.borne_inf && ir.borne_sup <= this.borne_sup)
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>IntervalleReel</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder("[").append(df.format(this.borne_inf)).append(",").append(df.format(this.borne_sup)).append("]"); }

}  /*----- Fin de la classe IntervalleReel -----*/
