package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JCheckBox;
import util.GuiUtils;


/*-------------*/
/* JCheckBoxEA */
/*-------------*/
/**
 * La classe <code>JCheckBoxEA</code> est une boite à cochée.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	6 mai 2009
 */
public final class JCheckBoxEA extends JCheckBox
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JCheckBoxEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		}

} /*----- Fin de la classe JCheckBoxEA -----*/
