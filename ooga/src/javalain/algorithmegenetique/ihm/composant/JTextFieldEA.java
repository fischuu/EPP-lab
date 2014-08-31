package javalain.algorithmegenetique.ihm.composant;

import javax.swing.JTextField;
import util.GuiUtils;


/*---------------------*/
/* Classe JTextFieldEA */
/*---------------------*/
/**
 * La classe <code>JTextFieldEA</code> est une zone de texte non éditable.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class JTextFieldEA extends JTextField
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JTextFieldEA (int taille)
		{
		super(taille);

//		this.setBackground(GuiUtils.grisLeger);
		this.setFont(GuiUtils.FONT_PARAMETRE);
		this.setEditable(false);
		}

	public JTextFieldEA (String texte)
		{
		super(texte);

//		this.setBackground(GuiUtils.grisLeger);
		this.setFont(GuiUtils.FONT_PARAMETRE);
		this.setEditable(false);
		}

} /*----- Fin de la classe JTextFieldEA -----*/
