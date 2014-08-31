package javalain.algorithmegenetique;

import java.io.Serializable;


/*------------*/
/* Chromosome */
/*------------*/
/**
 * La classe <code>Chromosome</code> présente les méthodes à implémenter
 * pour réaliser un nouveau type de chromosome.
 * <p>
 * La structure de données permettant le stockage des <code>Gene</code>s du
 * <code>Chromosome</code> n'est pas implémentée dans cette classe car le
 * fonctionnement du moteur de l'algorithme génétique est indépendant de la
 * manière dont les <code>Chromosome</code>s seront codés.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public abstract class Chromosome implements Cloneable, Serializable
{
	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
     * Croisement de deux <code>Chromosome</code>s.
	 *
	 * @param	parent	<code>Chromosome</code> qui va être croisé avec this
	 *
	 * @return	un tableau des enfants obtenus par croisement
	 */
	abstract public Chromosome[] croisement (Chromosome	parent);


	/**
     * Mutation du <code>Chromosome</code>.
	 */
	abstract public boolean mutation (double Pm);


	/**
     * Crée une copie du <code>Chromosome</code> ("deep copy").
	 *
	 * @return	une copie parfaite de this
	 */
	abstract public Chromosome copier ();


	/**
     * Crée un nouveau <code>Chromosome</code>.
	 * <p>
	 * Le nouveau <code>Chromosome</code> aura la même structure que
	 * son créateur mais toutes ses données seront initialisées aléatoirement.
	 *
	 * @return	le nouveau <code>Chromosome</code>
	 */
	abstract public Chromosome creer ();


	/**
     * Compare la valeur de deux <code>Chromosome</code>s.
	 *
	 * @param	c	<code>Chromosome</code> comparé
	 *
	 * @return	<code>true</code> si les deux <code>Chromosome</code>s
	 *			sont égaux, <code>false</code> sinon
	 */
	abstract public boolean estEgalA (Chromosome c);


	/**
     * Retourne la représentation sous forme de chaîne de caractères
	 *	d'un <code>Chromosome</code>.
	 */
	abstract public StringBuilder afficher ();


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
     * Compare si this est plus général que <code>c</code>.
	 * Par défaut, son comportement est identique à la méthode
	 * <code>estEgalA()</code>.
	 *
	 * @param	c	<code>Chromosome</code> comparé
	 *
	 * @return	<code>true</code> si this est plus général ou
	 *			égal à <code>c</code>
	 */
	public boolean estPlusGeneralQue (Chromosome c) { return this.estEgalA(c); }


	/**
     * Redéfinition de la méthode <code>toString()</code> de
	 * la classe <code>Object</code>.
	 * <p>
	 * Cette méthode ne peut pas être outrepassée par les classes dérivées
	 * de <code>Chromosome</code>.
	 */
	@Override
	final public String toString () { return this.afficher().toString(); }

} /*----- Fin de la classe Chromosome -----*/