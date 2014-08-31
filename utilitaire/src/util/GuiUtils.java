package util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/*----------*/
/* GuiUtils */
/*----------*/
/**
 * Classe d'utilitaires pour l'IHM.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 juin 2010
 */
public final class GuiUtils
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Polices de présentation : Cambria, Courier, Garamond, Georgia,
	 * Palatino Linotype, Tahoma, Times New Roman, Verdana.
	 */
	public static final Font FONT_PARAMETRE		= new Font("Tahoma",Font.PLAIN,11);
	public static final Font FONT_PRESENTATION	= new Font("Tahoma",Font.BOLD,11);
	public static final Font FONT_MENU			= FONT_PARAMETRE;

	public static final Font FONT_11			= new Font("", Font.PLAIN, 11);
	public static final Font ARIAL_11			= new Font("Arial", Font.PLAIN, 11);
	public static final Font ARIAL_14			= new Font("Arial", Font.PLAIN, 14);
	public static final Font SERIF_14_BOLD		= new Font("Serif", Font.BOLD, 14);
	public static final Font SANSSERIF_12		= new Font("SansSerif", Font.PLAIN, 12);
	public static final Font SANSSERIF_12_BOLD	= new Font("SansSerif", Font.BOLD, 12);
	public static final Font TAHOMA_11			= new Font("Tahoma", Font.PLAIN, 11);
	public static final Font TAHOMA_11_BOLD		= new Font("Tahoma", Font.BOLD, 11);
	public static final Font TAHOMA_18_BOLD		= new Font("Tahoma", Font.BOLD, 18);


	/**
	 * Formats de présentation.
	 */
	private static final DecimalFormatSymbols POINT = new DecimalFormatSymbols();

	public static final DecimalFormat DECIMAL		= new DecimalFormat("0.0#", POINT);
	public static final DecimalFormat DECIMAL_2		= new DecimalFormat("0.00", POINT);
	public static final DecimalFormat DECIMAL_3		= new DecimalFormat("0.000", POINT);
	public static final DecimalFormat DECIMAL_6		= new DecimalFormat("0.000000", POINT);
	public static final DecimalFormat DECIMAL_9		= new DecimalFormat("0.000000000", POINT);
	public static final DecimalFormat DECIMAL_12	= new DecimalFormat("0.000000000000", POINT);
	public static final DecimalFormat POURCENTAGE	= new DecimalFormat("00.00", POINT);
	public static final DecimalFormat SCIENTIFIQUE	= new DecimalFormat("0.0000E0", POINT);


	/**
	 * Couleurs.
	 */
	public static final Color GRIS_238		= new Color(238,238,238);
	public static final Color GRIS_153		= new Color(153,153,153);
	public static final Color BLEU_ERREUR	= new Color(204,204,255);
	public static final Color VERT_LEGER	= new Color(153,204,153);

	public final static Color blanc			= Color.white; // A faire - Inverser dtaic et final et mettre en MAJUSCULE
	public final static Color noir			= Color.black;
	public final static Color rouge			= Color.red;
	public final static Color vert			= Color.green;
	public final static Color bleu			= Color.blue;
	public final static Color jaune			= Color.yellow;
	public final static Color cyan			= Color.cyan;
	public final static Color magenta		= Color.magenta;

	public final static Color grisLeger		= Color.lightGray;
	public final static Color gris			= Color.gray;
	public final static Color grisFonce		= Color.darkGray;

	public final static Color rose			= Color.pink;
	public final static Color orange		= Color.orange;

	public final static Color jauneLeger	= new Color(255, 255, 128);
	public final static Color rougeFonce	= new Color(160, 0, 0);
	public final static Color vertEau		= new Color(195, 255, 255);


	/**
	 * Marges.
	 */
	public static final Insets INSETS_0000		= new Insets(0,0,0,0);
	public static final Insets INSETS_0500		= new Insets(0,5,0,0);
	public static final Insets INSETS_0A00		= new Insets(0,10,0,0);
	public static final Insets INSETS_5000		= new Insets(5,0,0,0);
	public static final Insets INSETS_5500		= new Insets(5,5,0,0);
	public static final Insets INSETS_5A00		= new Insets(5,10,0,0);


	/**
	 * Gestionnaire de disposition.
	 */
	public static final FlowLayout FL_LEFT_00	= new FlowLayout(FlowLayout.LEFT,0,0);


	/**
	 * Bordures.
	 */
	public static final Border NO_BORDER		= BorderFactory.createEmptyBorder(0, 0, 0, 0);
	public static final Border BORDER_5			= BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static final Border BORDER_2			= BorderFactory.createEmptyBorder(2, 2, 2, 2);
	public static final Border BORDER_10		= BorderFactory.createEmptyBorder(10, 10, 10, 10);
	public static final Border LINE_DARK		= BorderFactory.createLineBorder(Color.darkGray);

	public static final Border BOUTON_BORDER	= BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


	/**
	 * Formats de présentation.
	 */
	/**
	 * Ressources.
	 */
	/*----- Icône de l'OptionPane : Icon icon = UIManager.getIcon("OptionPane.questionIcon"); -----*/
	public static final String ICON_INFORMATION	= "OptionPane.informationIcon";
	public static final String ICON_ERREUR		= "OptionPane.errorIcon";
	public static final String ICON_ATTENTION	= "OptionPane.warningIcon";
	public static final String ICON_QUESTION	= "OptionPane.questionIcon";


	/*------*/
	/* Main */
	/*------*/

	public static void main(String[] args) throws Exception
		{
		/**
		 * Affichage de toutes les ressources.
		 */
		Enumeration e = UIManager.getDefaults().keys();
		while(e.hasMoreElements())
			System.out.println(e.nextElement());

		System.out.println("----------\nNombre de clés : " + UIManager.getDefaults().size() + "\n----------");


		/**
		 * Lecture des Look and Feel.
		 */
		LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();

		for (int i=0; i<laf.length; i++)
			System.out.println(laf[i].getName());

		System.out.println("----------");
		}

} /*----- Fin de la classe GuiUtils -----*/
