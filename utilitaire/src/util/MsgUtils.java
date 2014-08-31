package util;

import java.awt.Window;
import javax.swing.JOptionPane;


/*----------*/
/* MsgUtils */
/*----------*/
/**
 * Classe d'utilitaires pour l'affichage de messages.
 *
 * @author	Alain Berro
 * @version	21 mai 2010
 */
public final class MsgUtils
{
	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Message d'erreur.
	 */
	public static void erreur (Window w, String message, String titre)
		{
		JOptionPane.showMessageDialog(w,
									  message,
									  titre,
									  JOptionPane.ERROR_MESSAGE);

		System.err.println(titre + " : " + message);
		}

	public static void erreurAndExit (Window w, String message, String titre)
		{
		MsgUtils.erreur(w, message, titre);
		System.exit(0);
		}


	/**
	 * Message d'attention.
	 */
	public static void attention (Window w, String titre, String message)
		{
		JOptionPane.showMessageDialog(w,
									  message,
									  titre,
									  JOptionPane.WARNING_MESSAGE);
		}


	/**
	 * Message d'attention.
	 */
	public static void info (Window w, String titre, String message)
		{
		JOptionPane.showMessageDialog(w,
									  message,
									  titre,
									  JOptionPane.INFORMATION_MESSAGE);
		}

} /*----- Fin de la classe MsgUtils -----*/
