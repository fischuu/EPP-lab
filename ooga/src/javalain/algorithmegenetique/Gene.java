package javalain.algorithmegenetique;

import java.io.Serializable;


/*------*/
/* Gene */
/*------*/
/**
 * La classe <code>Gene</code> présente les méthodes à implémenter pour créer
 * un nouveau gène.
 * <p>
 * <b>Conseil:</b>	Pour une utilisation plus facile, munissez toutes les
 *					classes dérivées de <code>Gene</code> d'une fonction
 *					<code>get()</code> qui retourne la valeur du gène et
 *					d'une fonction <code>set</code> pour mettre à jour
 *					votre <code>Gene</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	26 décembre 2003
 */
public abstract class Gene implements Serializable
{
	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Mélange de deux <code>Gene</code>s.
	 * <p>
	 * Cette fonction retourne un nouveau <code>Gene</code> créé à partir de
	 * deux <code>Gene</code>s parents. Cela permet d'augmenter la diversité
	 * génétique.
	 *
	 * @param	g	<code>Gene</code> mélangé avec this
	 *
	 * @return	<code>Gene</code> obtenu après le mélange
	 */
	abstract public Gene melanger (Gene g);


	/**
	 * Mutation d'un <code>Gene</code>.
	 */
	abstract public void mutation ();


	/**
	 * Crée une copie du <code>Gene</code> ("deep copy").
	 *
	 * @return	une copie parfaite de this
	 */
	abstract public Gene copier ();


	/**
	 * Crée un nouveau <code>Gene</code>.
	 * <p>
	 * Le <code>Gene</code> créé aura la même structure que this
	 * mais sa valeur sera initialisée aléatoirement.
	 *
	 * @return	le nouveau <code>Gene</code>
	 */
	abstract public Gene creer ();


	/**
	 * Compare la valeur de deux <code>Gene</code>s.
	 *
	 * @param	g	<code>Gene</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	abstract public boolean estEgalA (Gene g);


	/**
	 * Retourne la représentation sous forme d'une chaîne de caractères
	 * d'un <code>Gene</code>.
	 */
	abstract public StringBuilder afficher ();


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Crée un nouveau <code>Gene</code> en fonction d'un autre
	 * <code>Gene</code>.
	 * <p>
	 * Le <code>Gene</code> créé sera du même type que this mais
	 * sa valeur sera initialisée en fonction de <code>g</code>.
	 * Par défaut cette fonction retourne une copie de <code>g</code>.
	 * <p>
	 * Il peut être utile de redéfinir cette fonction pour réaliser
	 * un comportement de création spécifique.
	 *
	 * @param	g 	<code>Gene</code> utilisé pour initialiser la valeur
	 *				du nouveau <code>Gene</code>
	 *
	 * @exception	IllegalArgumentException si <code>g</code> n'est pas une
	 *				instance de la classe de this
	 */
	public Gene creer (Gene g)
		{
		if	(this.getClass().isInstance(g)) return g.copier();

		throw new IllegalArgumentException(this.getClass().getName() + ", creer (Gene g) : g n'est pas une instance de la classe " + this.getClass().getName() + ".");
		}


	/**
	 * Compare si this est plus général que <code>g</code>.
	 * <p>
	 * Cette méthode retourne <code>true</code> si this est plus général ou
	 * égal à <code>g</code>. Par défaut, son comportement est identique
	 * à la méthode <code>estEgalA()</code>.
	 * <p>
	 * Il est utile de redéfinir cette fonction pour constater par exemple que
	 * <ul>
	 *	<li><code>**01</code> est un schéma de <code>0*01, 1001,</code> etc.
	 *	<li>l'intervalle [2, 17] inclus le nombre 13
	 * </ul>
	 *
	 * @param	g	<code>Gene</code> comparé
	 */
	public boolean estPlusGeneralQue (Gene g) { return this.estEgalA(g); }


	/**
	 * Redéfinition de la méthode <code>toString()</code> de
	 * la classe <code>Object</code>.
	 * <p>
	 * <b>Remarque:</b> Cette méthode ne peut pas être outrepassée
	 *					par les classes dérivées de <code>Gene</code>.
	 */
	@Override
	final public String toString () { return this.afficher().toString(); }

} /*----- Fin de la classe Gene -----*/
