package javalain.algorithmegenetique.ihm;


/*---------------*/
/* SynchroCtrlAG */
/*---------------*/
/**
 * L'interface <code>SynchroCtrlAG</code> permet de synchroniser la mise à jour
 * d'informations (donnée ou graphique) d'une classe avec celle de
 * <code>ControleAlgoG</code>.
 * <p>
 * Pour mettre en place la synchronisation il faut implémenter cette interface
 * et ajouter la classe à <code>ControleAlgoG</code> par la méthode
 * <code>addSynchroCtrlAG(SynchroCtrlAG)</code>. Ensuite à chaque mise à
 * jour des données de <code>ControleAlgoG</code> la méthode
 * <code>synchroniseCtrlAG()</code> est appelée pour toutes les classes à
 * synchroniser.
 *
 * @see	ControleAlgoG
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	6 janvier 2004
 */
public interface SynchroCtrlAG
{
	/*----------*/
	/* Méthodes */
	/*----------*/

	public void synchroniseCtrlAG ();

} /*----- Fin de l'interface SynchroCtrlAG -----*/
