package ihm;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import epp.EPPLab;


/*------------------*/
/* Classe FiltreTXT */
/*------------------*/
/**
 * La classe <code>FiltreTXT</code> permet de ne filtrer que les fichiers 'txt'
 * lors de la lecture de répertoire.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	5 juin 2010
 */
public final class FiltreTXT extends FileFilter
{
	/**
	 * Accepte tous les répertoires et les fichiers 'txt'.
	 */
	public boolean accept (File f)
		{
		if (f.isDirectory())
			return true;

		String name = f.toString();
		return name.toLowerCase().endsWith(".txt");
		}


	/**
	 * Libellé du filtre.
	 */
	public String getDescription () { return EPPLab.msg.getString("filtre.txt"); }

} /*----- Fin de la classe FiltreTXT -----*/
