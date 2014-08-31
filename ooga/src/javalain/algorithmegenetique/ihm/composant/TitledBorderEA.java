package javalain.algorithmegenetique.ihm.composant;

import javax.swing.border.TitledBorder;
import util.GuiUtils;


/*----------------*/
/* TitledBorderEA */
/*----------------*/
/**
 * La classe <code>TitledBorderEA</code> est un encadrement avec un titre.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	22 décembre 2003
 */
public final class TitledBorderEA extends TitledBorder
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public TitledBorderEA (String s)
		{
		super(s);

		this.setTitleFont(GuiUtils.FONT_MENU);
		}

} /*----- Fin de la classe TitledBorderEA -----*/
