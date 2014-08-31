package ihm.graphique;

import ihm.controle.JPanelEA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import util.GuiUtils;


/*---------------------*/
/* Classe CourbeIndice */
/*---------------------*/
/**
 * La classe <code>CourbeIndice</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	24 mai 2010
 */
public class CourbeIndice extends JPanelEA
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Données pour l'affichage.
	 */
	private float donnees[];

	/*----- Indice du dernier élément du tableau -----*/
	private int indiceFinDonnees;

	/*----- Informations sur les données -----*/
	private String nomDonnees;
	private String nomIndice;
	private String nomMethode;


	/**
	 * Tous les indices.
	 */
	/*----- Indice (numéro de lancement) minimum et maximum à partir duquel la courbe sera affichée -----*/
	private int indiceMin;
	private int indiceMax;

	/*----- Indice du lancement sélectionné -----*/
	private int indiceRunSelect;

	/*----- Indice du lancement de référence -----*/
	private int indiceRef;

	/*----- Etat pour savoir si indiceMin ou indiceMax a changé afin de recalculer les valeurs min et max -----*/
	private boolean change;


	/**
	 * Minimum et maximum de la courbe.
	 */
	private float min;
	private float max;


	/**
	 * Taille de la zone d'affichage.
	 */
	private int largeur;
	private int hauteur;

	private int marge_haut		= 20;
	private int marge_gauche	= 25;
	private int marge_bas		= 17;
	private int marge_droite	= 25;

	private int taille_point 	= 3;
	private int decal_point		= (this.taille_point-1)/2;

	/*----- Position de l'axe des abscisses -----*/
	private int centre_y;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public CourbeIndice (int l, int h)
		{
		this.largeur = l;
		this.hauteur = h;

		/*----- Position de l'axe des abscisses -----*/
		this.centre_y = this.hauteur-1-this.marge_bas;

		this.donnees = new float[500];
		this.indiceFinDonnees = -1;
		this.indiceMin = 0;
		this.indiceMax = -1;
		this.indiceRunSelect = 0;
		this.indiceRef = 0;

		this.change = false;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les espacements à gauche et à droite.
	 */
	public int getMargeGauche () { return this.marge_gauche; }
	public int getMargeDroite () { return this.marge_droite; }


	/**
	 * Remise à zéro de la courbe.
	 */
	public void raz ()
		{
		this.indiceFinDonnees = -1;
		this.indiceMin = 0;
		this.indiceMax = -1;

		this.change = true;
		}


	/**
	 * Modifie les indices minimun, maximum du tableau des valeurs à afficher.
	 */
	public void setIndiceMinimum (int i) { this.indiceMin = i; this.change = true; }
	public void setIndiceMaximum (int i) { this.indiceMax = i; this.change = true; }


	/**
	 * Modifie les indices de lancement.
	 */
	public void setIndiceRunSelect (int i) { this.indiceRunSelect = i; }
	public void setIndiceReference (int i) { this.indiceRef = i; }


	/**
	 * Mise à jour des informations.
	 */
	public void setInfos (String nomDonnees, String nomIndice, String nomMethode)
		{
		this.nomDonnees = nomDonnees;
		this.nomIndice = nomIndice;
		this.nomMethode = nomMethode;
		}


	/**
	 * Ajoute une donnée à afficher.
	 */
	public void addNombre (float f)
		{
		if	(this.indiceFinDonnees == -1)
			{
			this.min = f;
			this.max = f;
			}

		int taille = this.donnees.length;

		/*----- Agrandissement de la taille du tableau de données -----*/
		if	(this.indiceFinDonnees == (taille-1))
			{
			int n_taille	= (taille*120)/100 + 1;
			float[] tab		= this.donnees;
			this.donnees	= new float[n_taille];

			System.arraycopy(tab,0,this.donnees,0,taille);
			}

		if	(f > this.max) this.max = f; else
		if	(f < this.min) this.min = f;

		this.donnees[++this.indiceFinDonnees] = f;

		/*----- Indice max pour l'affichage -----*/
		this.indiceMax = this.indiceFinDonnees;
		}


	/**
	 * Dessine la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		int nb;


		/**
		 * Couleur de fond.
		 */
//		g.setColor(GuiUtils.VERT_LEGER);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.largeur,this.hauteur);


		/**
		 * Axes.
		 */
		/*----- Calcul des échelles -----*/
		float echelleX = (this.largeur-this.marge_gauche-this.marge_droite-this.taille_point)/(float)(this.indiceMax-this.indiceMin+1);

		if (this.change)
			{
			this.min = this.donnees[this.indiceMax];
			this.max = this.donnees[this.indiceMax];

			float f;
			for	(int i=this.indiceMin; i<this.indiceMax; i++)
				{
				f = this.donnees[i];
				if	(f > this.max) this.max = f; else
				if	(f < this.min) this.min = f;
				}

			this.change = false;
			}

		float echelleY = (this.hauteur-this.marge_bas-this.marge_haut-this.taille_point)/(this.max-this.min);

		/*----- Affichage de l'axe X -----*/
		g.setColor(Color.GRAY);
		g.setColor(Color.BLACK);
		g.drawLine(this.marge_gauche, this.centre_y, this.largeur-1-this.marge_droite, this.centre_y);

		/*----- Affiche l'échelle des X -----*/
		int pas = 1;
		nb = this.indiceMax-this.indiceMin+1;
		if (nb >= 10)
			{
			if (nb >= 20)
				pas = nb/10;
			else
				pas = 2;
			}

		for	(int i=this.indiceMin; i<=this.indiceMax; i++)
			{
			if ((i+1)%pas == 0)
				{
				g.drawLine((int)((i+1-this.indiceMin)*echelleX) + this.marge_gauche, this.centre_y, (int)((i+1-this.indiceMin)*echelleX) + this.marge_gauche, this.centre_y + 3);
				g.drawString("" + (i+1), (int)((i+1-this.indiceMin)*echelleX) + this.marge_gauche, this.centre_y + 15);
				}
			}

		/*----- Affichage de l'axe Y -----*/
		g.drawLine(this.marge_gauche, this.marge_haut, this.marge_gauche, this.centre_y);
		g.drawLine(this.marge_gauche-3, this.marge_haut, this.marge_gauche, this.marge_haut);
		g.drawLine(this.marge_gauche-3, this.centre_y, this.marge_gauche, this.centre_y);

		/*----- Affiche la valeur maximale de I -----*/
		g.drawString(GuiUtils.DECIMAL_2.format(this.max), this.marge_gauche + 4, this.marge_haut - 3);

		/*----- Affiche la valeur minimale de I -----*/
		g.drawString(GuiUtils.DECIMAL_2.format(this.min), this.marge_gauche + 4, this.centre_y - 3);


		/**
		 * Lancement sélectionné.
		 */
		g.setColor(Color.BLUE);
		g.setFont(GuiUtils.FONT_PARAMETRE);
		g.drawLine((int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche, this.marge_haut,(int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche , this.centre_y);
		g.drawString("" + (this.indiceRunSelect+1), (int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche + 4, this.centre_y + 10);
		g.drawString("" + GuiUtils.DECIMAL_2.format(this.donnees[this.indiceRunSelect]), (int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche + 4, this.hauteur-this.marge_bas-this.taille_point - (int)((this.donnees[this.indiceRunSelect]-this.min)*echelleY) - 3);


		/**
		 * On affiche tous les points en noir.
		 */
		g.setColor(Color.BLACK);
		for	(int i=this.indiceMin; i<=this.indiceMax; i++)
			{
			g.fillRect((int)((i+1-this.indiceMin)*echelleX) + this.marge_gauche - this.decal_point,
					   this.hauteur-this.marge_bas-this.taille_point - (int)((this.donnees[i]-this.min)*echelleY) - this.decal_point,
					   this.taille_point,
					   this.taille_point);
			}


		/**
		 * Lancement de référence (en rouge).
		 */
		if (this.indiceMin <= this.indiceRef && this.indiceRef <= this.indiceMax)
			{
			g.setColor(Color.RED);
//			g.setColor(Color.BLACK);
			g.fillOval((int)((this.indiceRef+1-this.indiceMin)*echelleX) + this.marge_gauche - 4,
					   this.hauteur-this.marge_bas-this.taille_point - (int)((this.donnees[this.indiceRef]-this.min)*echelleY) - 4,
					   8,
					   8);
			}


		/**
		 * On affiche le point sélectionné en jaune.
		 */
//		g.setColor(Color.YELLOW);
//		g.fillRect((int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche - this.decal_point,
//				   this.hauteur-this.marge_bas-this.taille_point - (int)((this.donnees[this.indiceRunSelect]-this.min)*echelleY) - this.decal_point,
//				   this.taille_point,
//				   this.taille_point);


		/*----- Affichage des informations -----*/
//		g.setFont(GuiUtils.TAHOMA_18_BOLD);
//		g.setColor(Color.LIGHT_GRAY);
//		if (this.nomDonnees != null)
//			{
//			String s = this.nomDonnees.substring(0,this.nomDonnees.lastIndexOf('.'));
//			if (s.startsWith("fiab")) s = "reliability";
//			if (s.startsWith("lubischev")) s = "lubischew";
//
//			g.drawString(s, this.largeur-this.marge_droite-100, this.marge_haut+5);
//			}
//		if (this.nomIndice != null) g.drawString(this.nomIndice, this.largeur-this.marge_droite-100, this.marge_haut+25);
//		if (this.nomMethode != null)
//			{
//			String s = "";
//			if (this.nomMethode.equals(ParametrePP.GA)) s = "GA";
//			if (this.nomMethode.equals(ParametrePP.PSO)) s = "PSO";
//
//			g.drawString(s, this.largeur-this.marge_droite-100, this.marge_haut+45);
//			}
		}

} /*----- Fin de la classe CourbeIndice -----*/
