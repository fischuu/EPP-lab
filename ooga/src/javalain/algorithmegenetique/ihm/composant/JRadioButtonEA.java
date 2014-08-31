package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JRadioButton;
import util.GuiUtils;


/*----------------*/
/* JRadioButtonEA */
/*----------------*/
/**
 * La classe <code>JRadioButtonEA</code> est un radio bouton.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JRadioButtonEA extends JRadioButton
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JRadioButtonEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		}

} /*----- Fin de la classe JRadioButtonEA -----*/
