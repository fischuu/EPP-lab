package javalain.algorithmegenetique.gene;

import javalain.algorithmegenetique.DomaineEntier;
import javalain.algorithmegenetique.Gene;
import javalain.math.PseudoRandomNumbers;


/*------------------*/
/* IntervalleEntier */
/*------------------*/
/**
 * La classe <code>IntervalleEntier</code> définit un gène représentant
 * un intervalle définit par des nombres entiers.
 * Vous devez impérativement créer une instance de classe
 * <code>DomaineEntier</code> afin de définir le domaine de valeur de
 * votre <code>IntervalleEntier</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public final class IntervalleEntier extends Gene
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Domaine de définition de l'<code>IntervalleEntier</code>.
	 */
	private DomaineEntier domaine;


	/**
	 * Bornes de l'<code>IntervalleEntier</code>.
	 */
	private int borne_inf;
	private int borne_sup;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée et initialise aléatoirement l'<code>IntervalleEntier</code>.
	 */
	public IntervalleEntier (DomaineEntier i)
		{
		int taille	= i.getTaille();
		int min		= i.getMin();

		this.domaine	= i;
		this.borne_inf	= (int)(PseudoRandomNumbers.random()*taille) + min;
		this.borne_sup	= (int)(PseudoRandomNumbers.random()*taille) + min;

		if	(this.borne_inf > this.borne_sup)
			{
			min				= this.borne_inf;
			this.borne_inf	= this.borne_sup;
			this.borne_sup	= min;
			}
		}


	private IntervalleEntier (DomaineEntier i,
							  int inf,
							  int sup)
		{
		this.domaine	= i;
		this.borne_inf 	= inf;
		this.borne_sup	= sup;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la borne inférieure de l'<code>IntervalleEntier</code>.
	 */
	public int getBorneInf () { return this.borne_inf; }


	/**
	 * Retourne la borne supérieure de l'<code>IntervalleEntier</code>.
	 */
	public int getBorneSup () { return this.borne_sup; }


	/**
	 * Retourne le domaine de définition de l'<code>IntervalleEntier</code>.
	 *
	 * @return	un <code>DomaineEntier</code>
	 */
	public DomaineEntier getDomaineDefinition () { return this.domaine; }


	/*---------------------------------*/
	/* Définition des méthodes de Gene */
	/*---------------------------------*/

	/**
	 * Mélange de deux <code>IntervalleEntier</code>s.
	 *
	 * @param	g	<code>IntervalleEntier</code> mélangé avec this
	 *
	 * @return	<code>IntervalleEntier</code> obtenu après le mélange
	 */
	public Gene melanger (Gene g)
		{
		switch((int)(PseudoRandomNumbers.random()*3))
			{
			case 0	: return new IntervalleEntier(this.domaine,this.borne_inf,this.borne_sup);
			case 1	:
					{
					IntervalleEntier ie = (IntervalleEntier)g;
					return new IntervalleEntier(this.domaine,ie.borne_inf,ie.borne_sup);
					}
			default :
					{
					double d;
					int inf, sup, aux;
					IntervalleEntier ie = (IntervalleEntier)g;

					d	= PseudoRandomNumbers.random()*1.5 - 0.25;
					inf	= (int)(d*this.borne_inf + (1.0-d)*ie.borne_inf);
					sup	= (int)(d*this.borne_sup + (1.0-d)*ie.borne_sup);

					if	(inf > sup) { aux = inf; inf = sup; sup = aux; }

					aux = this.domaine.getMin();
					if	(inf < aux)
						{
						if	(sup < aux) return new IntervalleEntier(this.domaine); // Echec du mélange, intervalle hors domaine

						inf = aux;
						}

					aux = this.domaine.getMax();
					if	(sup > aux)
						{
						if	(inf > aux) return new IntervalleEntier(this.domaine); // Echec du mélange, intervalle hors domaine

						sup = aux;
						}

					return new IntervalleEntier(this.domaine,inf,sup);
					}
			}
		}


	/**
	 * Mutation d'un <code>IntervalleEntier</code>.
	 */
	public void mutation ()
		{
		switch ((int)(PseudoRandomNumbers.random()*8))
			{
			case 0	:  this.borne_inf += (int)(PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_sup-this.borne_inf+1)); break;
			case 1	:  this.borne_inf += (int)(PseudoRandomNumbers.random()*(this.borne_sup-this.borne_inf+1)); break;

			case 2	:  this.borne_inf -= (int)(PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_inf-this.domaine.getMin()+1)); break;
			case 3	:  this.borne_inf -= (int)(PseudoRandomNumbers.random()*(Math.min(this.borne_sup-this.borne_inf,this.borne_inf-this.domaine.getMin())+1)); break;

			case 4	:  this.borne_sup += (int)(PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.domaine.getMax()-this.borne_sup+1)); break;
			case 5	:  this.borne_sup += (int)(PseudoRandomNumbers.random()*(Math.min(this.borne_sup-this.borne_inf,this.domaine.getMax()-this.borne_sup)+1)); break;

			case 6	:  this.borne_sup -= (int)(PseudoRandomNumbers.random()*Math.min(this.domaine.getRayon(),this.borne_sup-this.borne_inf+1)); break;
			case 7	:  this.borne_sup -= (int)(PseudoRandomNumbers.random()*(this.borne_sup-this.borne_inf+1)); break;
			default :
			}
		}


	/**
	 * Crée une copie de l'<code>IntervalleEntier</code> ("deep copy").
	 */
	public Gene copier () { return new IntervalleEntier(this.domaine,this.borne_inf,this.borne_sup); }


	/**
	 * Crée un nouvel <code>IntervalleEntier</code>.
	 */
	public Gene creer () { return new IntervalleEntier(this.domaine); }


	/**
	 * Crée un nouvel <code>IntervalleEntier</code> en fonction
	 * d'un autre <code>Gene</code>.
	 * <p>
	 * Le nouvel <code>IntervalleEntier</code> inclura la valeur
	 * du <code>Gene</code> passé en paramètre.
	 *
	 * @param	g	<code>Entier</code> ou <code>IntervalleEntier</code>
	 *				utilisé pour initialiser le nouvel
	 *				<code>IntervalleEntier</code>
	 *
	 * @exception	IllegalArgumentException si <code>g</code> n'est pas
	 *				une instance de la classe <code>Entier</code> ou
	 *				<code>IntervalleEntier</code>, ou  si la valeur de
	 *				<code>g</code> n'est pas inclus dans le domaine
	 *				de definition de this
	 */
	@Override
	public Gene creer (Gene g)
		{
		if	(g instanceof Entier)
			{
			int e		= ((Entier)g).get();
			int dmin	= this.domaine.getMin();
			int dmax	= this.domaine.getMax();

			if	(dmin <= e && e <= dmax)
				{
				int diff_inf = e - dmin + 1;
				int diff_sup = dmax - e + 1;

				return new IntervalleEntier(this.domaine,e-(int)(PseudoRandomNumbers.random()*diff_inf),e+(int)(PseudoRandomNumbers.random()*diff_sup));
				}
			else
				throw new IllegalArgumentException("IntervalleEntier, creer (Gene g) : l'Entier passe en parametre n'est pas inclus dans le domaine de definition de this.");
			}

		if	(g instanceof IntervalleEntier)
			{
			IntervalleEntier ie = (IntervalleEntier)g;
			int binf = ie.borne_inf;
			int bsup = ie.borne_sup;
			int dmin = this.domaine.getMin();
			int dmax = this.domaine.getMax();

			if	(dmin <= binf && bsup <= dmax)
				{
				int diff_inf = binf - dmin + 1;
				int diff_sup = dmax - bsup + 1;

				return new IntervalleEntier(this.domaine,dmin+(int)(PseudoRandomNumbers.random()*diff_inf),dmax-(int)(PseudoRandomNumbers.random()*diff_sup));
				}
			else
				throw new IllegalArgumentException("IntervalleEntier, creer (Gene g) : l'IntervalleEntier passe en parametre n'est pas inclus dans le domaine de definition de this.");
			}

		throw new IllegalArgumentException("IntervalleEntier, creer (Gene g) : g doit etre une instance de Entier ou IntervalleEntier.");
		}


	/**
	 * Compare la valeur de deux <code>IntervalleEntier</code>s.
	 *
	 * @param	g	<code>IntervalleEntier</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof IntervalleEntier)
			{
			IntervalleEntier ie = (IntervalleEntier)g;

			if	(this.borne_inf == ie.borne_inf && this.borne_sup == ie.borne_sup)
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Compare si this est plus général que <code>g</code>.
	 *
	 * @param	g	<code>Entier</code> ou <code>IntervalleEntier</code> comparé
	 *
	 * @return	<code>true</code> si la valeur de this inclus la valeur
	 *			de <code>g</code>
	 */
	@Override
	public boolean estPlusGeneralQue (Gene g)
		{
		if	(g instanceof Entier)
			{
			int e = ((Entier)g).get();

			if	(this.borne_inf <= e && e <= this.borne_sup)
				return true;
			else
				return false;
			}

		if	(g instanceof IntervalleEntier)
			{
			IntervalleEntier ie = (IntervalleEntier)g;

			if	(this.borne_inf <= ie.borne_inf && ie.borne_sup <= this.borne_sup)
				return true;
			else
				return false;
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>IntervalleEntier</code>.
	 */
	public StringBuilder afficher () { return new StringBuilder("[").append(this.borne_inf).append(",").append(this.borne_sup).append("]"); }

} /*----- Fin de la classe IntervalleEntier -----*/
