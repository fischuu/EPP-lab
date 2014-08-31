package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JMenuItem;
import util.GuiUtils;


/*-------------*/
/* JMenuItemEA */
/*-------------*/
/**
 * La classe <code>JMenuItemEA</code> est un item de menu.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JMenuItemEA extends JMenuItem
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JMenuItemEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_MENU);
		}

} /*----- Fin de la classe JMenuItemEA -----*/
