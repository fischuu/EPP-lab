package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JMenu;
import util.GuiUtils;


/*---------*/
/* JMenuEA */
/*---------*/
/**
 * La classe <code>JMenuEA</code> est un élément de menu.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JMenuEA extends JMenu
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JMenuEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_MENU);
		}

} /*----- Fin de la classe JMenuEA -----*/
