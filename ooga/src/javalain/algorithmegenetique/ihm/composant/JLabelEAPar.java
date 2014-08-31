package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JLabel;
import util.GuiUtils;


/*-------------*/
/* JLabelEAPar */
/*-------------*/
/**
 * La classe <code>JLabelEAPar</code> est une étiquette de présentation.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JLabelEAPar extends JLabel
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JLabelEAPar (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		}

} /*----- Fin de la classe JLabelEAPres -----*/
