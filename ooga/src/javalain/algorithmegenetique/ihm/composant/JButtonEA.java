package javalain.algorithmegenetique.ihm.composant;


import javax.swing.Icon;
import javax.swing.JButton;
import util.GuiUtils;


/*------------------*/
/* Classe JButtonEA */
/*------------------*/
/**
 * La classe <code>JButtonEA</code> est un bouton.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	18 mai 2009
 */
public final class JButtonEA extends JButton
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JButtonEA (String s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		}

	public JButtonEA (Icon i)
		{
		super(i);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		this.setBorder(GuiUtils.BOUTON_BORDER);
		}

} /*----- Fin de la classe JButtonEA -----*/