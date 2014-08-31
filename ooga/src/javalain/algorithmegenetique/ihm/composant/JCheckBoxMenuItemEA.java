package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JCheckBoxMenuItem;
import util.GuiUtils;


/*---------------------*/
/* JCheckBoxMenuItemEA */
/*---------------------*/
/**
 * La classe <code>JCheckBoxMenuItemEA</code> est une boite à cochée de menu.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	6 mai 2009
 */
public final class JCheckBoxMenuItemEA extends JCheckBoxMenuItem
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JCheckBoxMenuItemEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_MENU);
		}

} /*----- Fin de la classe JCheckBoxMenuItemEA -----*/
