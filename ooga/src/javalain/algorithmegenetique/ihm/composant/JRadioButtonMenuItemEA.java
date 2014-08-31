package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JRadioButtonMenuItem;
import util.GuiUtils;


/*------------------------*/
/* JRadioButtonMenuItemEA */
/*------------------------*/
/**
 * La classe <code>JRadioButtonMenuItemEA</code> est un radio bouton de menu.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JRadioButtonMenuItemEA extends JRadioButtonMenuItem
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JRadioButtonMenuItemEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_MENU);
		}

} /*----- Fin de la classe JRadioButtonMenuItemEA -----*/
