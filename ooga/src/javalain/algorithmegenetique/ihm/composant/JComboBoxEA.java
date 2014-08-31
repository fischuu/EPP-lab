package javalain.algorithmegenetique.ihm.composant;

import java.util.Vector;
import javax.swing.JComboBox;
import util.GuiUtils;


/*--------------------*/
/* Classe JComboBoxEA */
/*--------------------*/
/**
 * La classe <code>JComboBoxEA</code> est un menu déroulant.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	27 mai 2008
 */
public class JComboBoxEA extends JComboBox
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JComboBoxEA (String[] s)
		{
		super(s);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		this.setLightWeightPopupEnabled(false);
		}


	public JComboBoxEA (Vector<String> v)
		{
		super(v);

		this.setFont(GuiUtils.FONT_PARAMETRE);
		this.setLightWeightPopupEnabled(false);
		}

} /*----- Fin de la classe JComboBoxEA -----*/
