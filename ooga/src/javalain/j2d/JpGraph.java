package javalain.j2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import util.GuiUtils;


/*---------*/
/* JpGraph */
/*---------*/
/**
 * La classe <code>JpGraph</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	24 juillet 2010
 */
public class JpGraph extends JPanel
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Taille de la zone d'affichage.
	 */
	private int largeur;
	private int hauteur;

	private int marge_haut		= 25;
	private int marge_gauche	= 50;
	private int marge_bas		= 50;
	private int marge_droite	= 25;

	private int taille_point 	= 2;
	private int decal_point		= (this.taille_point-1)/2;

	/*----- Position de l'axe des abscisses -----*/
	private int centre_y;


	/**
	 * Coorddonnées (x,y) à afficher.
	 */
	private double coord[];

	private double x_min;
	private double x_max;
	private double y_min;
	private double y_max;

	/*----- Indice du dernier élément du tableau -----*/
	private int indiceFinCoord;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JpGraph (int l, int h)
		{
		this.largeur = l;
		this.hauteur = h;

		/*----- Position de l'axe des abscisses -----*/
		this.centre_y = this.hauteur-1-this.marge_bas;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));

		this.coord = new double[100];
		this.indiceFinCoord = -1;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les espacements à gauche et à droite.
	 */
	public int getMargeGauche () { return this.marge_gauche; }
	public int getMargeDroite () { return this.marge_droite; }
	public int getMargeHaut () { return this.marge_haut; }
	public int getMargeBas () { return this.marge_bas; }


	/**
	 * Remise à zéro de la courbe.
	 */
	public void raz ()
		{
		this.indiceFinCoord = -1;
		}


	/**
	 * Ajoute une coordonnées à afficher.
	 */
	public void addNombre (double x, double y)
		{
		if	(this.indiceFinCoord == -1)
			{
			this.x_min = x;
			this.x_max = x;
			this.y_min = y;
			this.y_max = y;
			}

		int taille = this.coord.length;

		/*----- Agrandissement de la taille des tableaux de données -----*/
		if	(this.indiceFinCoord >= (taille-2))		// -2 car on ajoute 2 éléments à chaque fois
			{
			int n_taille	= (taille*120)/100 + 1;
			double[] tab	= this.coord;
			this.coord		= new double[n_taille];

			System.arraycopy(tab,0,this.coord,0,taille);
			}

		if	(x > this.x_max) this.x_max = x; else
		if	(x < this.x_min) this.x_min = x;

		if	(y > this.y_max) this.y_max = y; else
		if	(y < this.y_min) this.y_min = y;

		this.coord[++this.indiceFinCoord] = x;
		this.coord[++this.indiceFinCoord] = y;
		}


	/**
	 * Dessine la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		/**
		 * Couleur de fond.
		 */
		g.setColor(GuiUtils.VERT_LEGER);
		g.fillRect(0,0,this.largeur,this.hauteur);


		/**
		 * Axes.
		 */
		/*----- Calcul des échelles -----*/
		double echelleX = (this.largeur-this.marge_gauche-this.marge_droite-this.taille_point)/(this.x_max - this.x_min);
		double echelleY = (this.hauteur-this.marge_bas-this.marge_haut-this.taille_point)/(this.y_max - this.y_min);

		/*----- Affichage de l'axe X -----*/
		g.setColor(Color.GRAY);
		g.drawLine(this.marge_gauche, this.centre_y, this.largeur-1-this.marge_droite, this.centre_y);
		g.drawString("" + GuiUtils.DECIMAL_3.format(this.x_min), this.marge_gauche, this.centre_y + 15);
		g.drawString("" + GuiUtils.DECIMAL_3.format(this.x_max), this.largeur-1-this.marge_droite - 35, this.centre_y + 15);

		/*----- Affichage de l'axe Y -----*/
		g.drawLine(this.marge_gauche, this.marge_haut, this.marge_gauche, this.centre_y);
		g.drawString("" + GuiUtils.DECIMAL_3.format(this.y_min), this.marge_gauche - 35, this.centre_y - 5);
		g.drawString("" + GuiUtils.DECIMAL_3.format(this.y_max), this.marge_gauche - 35, this.marge_haut);


		/**
		 * On affiche tous les points en noir.
		 */
		g.setColor(Color.BLACK);
		for (int i=0; i<(this.indiceFinCoord+1)/2; i++)
			{
			g.fillRect((int)((this.coord[i*2]-this.x_min)*echelleX) + this.marge_gauche - this.decal_point,
					   this.centre_y - (int)((this.coord[i*2+1]-this.y_min)*echelleY) - this.decal_point,
					   this.taille_point,
					   this.taille_point);
			}
		}

} /*----- Fin de la classe JpGraph -----*/
