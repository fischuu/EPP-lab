package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JLabel;
import util.GuiUtils;


/*--------------*/
/* JLabelEAPres */
/*--------------*/
/**
 * La classe <code>JLabelEAPres</code> est une étiquette de présentation.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JLabelEAPres extends JLabel
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JLabelEAPres (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PRESENTATION);
		}

} /*----- Fin de la classe JLabelEAPres -----*/
