package ihm.graphique;

import ihm.Analyse;
import javalain.math.Calcul;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import util.GuiUtils;


/*------------------------*/
/* Classe EstimateurNoyau */
/*------------------------*/
/**
 * La classe <code>EstimateurNoyau</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	24 mai 2010
 */
public class EstimateurNoyau extends JPanel
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Dimension de la fenêtre.
	 */
	private final int echelle_fenetre = 100;
	private final int marge = 20;


	/*---------*/
	/* Données */
	/*---------*/

	 /**
	  * Fenêtre parente.
	  */
	private Analyse frAnalyse;


	/**
	 * Tableau des données projetées à visualiser
	 */
	private double[]	donnees_x;
	private double		ecart_type;
	private double		moyenne;


	/**
	 * Taille de la zone de la courbe.
	 */
	private int largeur;
	private int hauteur;

	/*----- Position de l'axe des abscisses -----*/
	private int centre_y;

	/*----- Espace des coordonnées -----*/
	private double	unite_x;
	private double	x_min;
	private double	x_max;
	private double	y_min;
	private double	y_max;

	/*----- Prise en compte des atypiques et valeur pour les marquer -----*/
	private boolean	atypique;
	private int		valAtypique;

	/*----- Coordonnées de la sélection souris -----*/
	private boolean		selection = false;
	private boolean		en_selection = false;
	private int			x_debut;
	private int			x_fin;

	/*----- Intervalle sélectionné [x0, x1] -----*/
	public double x0;
	public double x1;

	/*----- Données projetées redéfinies sur 500 valeurs en abscisses -----*/
	private final static int NB = 500;
	private double[] donnees_y = new double[EstimateurNoyau.NB];


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public EstimateurNoyau (int l, int h, boolean atypique, boolean selection_souris, Analyse analyse)
		{
		/*----- Taille de la zone d'affichage -----*/
		this.largeur = l*this.echelle_fenetre + 2*this.marge;
		this.hauteur = h*this.echelle_fenetre + 2*this.marge;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));

		/*----- Position de l'axe des abscisses -----*/
		this.centre_y = this.hauteur-1-this.marge;

		/*----- Prise en compte des atypiques -----*/
		this.atypique = atypique;

		/*----- Fenêtre parente -----*/
		this.frAnalyse = analyse;

		/**
		 * Sélection souris autorisée ?
		 */
		if (selection_souris)
			{
			ComportementSouris souris = new ComportementSouris();

			this.addMouseListener(souris);
			this.addMouseMotionListener(souris);
			}
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les bornes de l'intervalle sélectionné.
	 */
	public double getBorneInfSelect () { return this.x0; }
	public double getBorneSupSelect () { return this.x1; }
	public boolean isSelection () { return this.selection; }

	public void setSelection (boolean b, double x0, double x1)
		{
		this.selection = b;
		this.x0 = x0;
		this.x1 = x1;
		this.x_debut = (int)((x0-this.x_min)*this.unite_x + marge);
		this.x_fin = (int)((x1-this.x_min)*this.unite_x + marge);
		}


	/**
	 * Retourne le tableau des données projetées.
	 */
	public double[] getDonnees () { return this.donnees_x; }


	/**
	 * Mise à jour de la projection à représenter.
	 */
	public void setProjection (double[] a)
		{
		/*----- Mise à jour du tableau des données projetées -----*/
		if (this.donnees_x == null || this.donnees_x.length != a.length)
			this.donnees_x = new double[a.length];

		System.arraycopy(a, 0, this.donnees_x,0, a.length);

		/*----- Statistiques -----*/
		this.ecart_type = Calcul.ecartType(this.donnees_x);
		this.moyenne = Calcul.moyenne(this.donnees_x);

		/**
		 * Recherche des valeurs projetées minimale et maximale pour calculer
		 * l'étendue des données projetées et la fenêtre de l'estimateur.
		 */
		this.x_min = Double.POSITIVE_INFINITY;
		this.x_max = Double.NEGATIVE_INFINITY;

		for (int i=0; i<this.donnees_x.length; i++)
			{
			if (this.donnees_x[i] < this.x_min) this.x_min = this.donnees_x[i]; else
			if (this.donnees_x[i] > this.x_max) this.x_max = this.donnees_x[i];
			}

		this.unite_x = (this.largeur-2*this.marge)/(float)(this.x_max-this.x_min);

		/*----- Etendue des données projetées -----*/
		double etendue = this.x_max - this.x_min;

		/*----- Fenêtre de l'estimateur à noyau -----*/
		double fenetre = EstimateurNoyau.Fenetre(etendue);

		/**
		 * Calcul des points de l'estimateur à noyau et recherche
		 * des valeurs minimale et maximale.
		 */
		double somme;
		this.y_min = Double.POSITIVE_INFINITY;
		this.y_max = Double.NEGATIVE_INFINITY;

		for (int i=0; i<EstimateurNoyau.NB; i++)
			{
			somme = 0.0;
			this.donnees_y[i] = this.x_min + i*etendue/(EstimateurNoyau.NB-1); // A voir - Pourquoi le -1 (Anne) ?

			for (int j=0; j<this.donnees_x.length; j++)
				somme = somme + EstimateurNoyau.Noyau((this.donnees_y[i] - this.donnees_x[j]) / fenetre);

			this.donnees_y[i] = somme / (this.donnees_x.length*fenetre);

			if (this.donnees_y[i] < this.y_min) this.y_min = this.donnees_y[i]; else
			if (this.donnees_y[i] > this.y_max) this.y_max = this.donnees_y[i];
			}

		/*----- Raz des indicateurs de sélection -----*/
		this.selection = false;
		this.en_selection = false;
		}


	/**
	 * Mise à jour de la valeur pour marquer les atypiques.
	 */
	public void setValeurAtypiques (int i) { this.valAtypique = i; }


	/**
	 * Dessine la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		/*----- Effacement de la zone d'affichage -----*/
		g.setColor(GuiUtils.GRIS_238);
//		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.largeur,this.hauteur);

		/*----- Espace de coordonnées en 'x' -----*/
		int axe_x_min	= (int)this.x_min + (this.x_min < 0.0 ? 0 : 1);
		int axe_x_max	= (int)this.x_max + (this.x_max > 0.0 ? 0 : -1);
		int lg_x		= axe_x_max - axe_x_min + 1;

		int decal_x = (int)((Math.abs(this.x_min-axe_x_min))*this.unite_x) + this.marge;

		int pas_x = 1;
		if (lg_x >= 10)
			{
			if (lg_x >= 20)
				pas_x = lg_x/10;
			else
				pas_x = 2;
			}

		/*----- Affichage de l'axe 'x' -----*/
		g.setFont(GuiUtils.FONT_11);
		g.setColor(Color.GRAY);
//		g.setColor(Color.BLACK);
		g.drawLine(this.marge, this.centre_y, this.largeur-1-this.marge, this.centre_y);

		for (int i=0; i<lg_x; i++)
			{
			if (i%pas_x == 0)
				{
				g.drawLine(decal_x+(int)(i*unite_x), this.centre_y-3, decal_x+(int)(i*unite_x), this.centre_y+3);
				g.drawString("" + (i+axe_x_min), decal_x+(int)(i*unite_x), this.centre_y+15);
				}
			}

		/*----- Bornes des atypiques -----*/
		double d0 = this.moyenne + this.ecart_type*this.valAtypique;
		double d1 = this.moyenne - this.ecart_type*this.valAtypique;

		/*----- Tracé de la courbe -----*/
		float echelleX = (this.largeur-2*this.marge)/(float)EstimateurNoyau.NB;
		float echelleY = (this.hauteur-2*this.marge)/(float)(this.y_max-this.y_min);
		for (int i=0; i<EstimateurNoyau.NB-1; i++)
			{
			if (this.atypique &&
				(i*echelleX > (d0-this.x_min)*this.unite_x || (i+1)*echelleX < (d1-this.x_min)*this.unite_x))
				g.setColor(Color.ORANGE);
			else
				g.setColor(Color.BLACK);

			g.drawLine((int)(i*echelleX) + this.marge,
					   this.centre_y - (int)((this.donnees_y[i] - this.y_min)*echelleY),
					   (int)((i+1)*echelleX) + this.marge,
					   this.centre_y - (int)((this.donnees_y[i+1] - this.y_min)*echelleY));
			}

		/*----- Affichage des valeurs min et max sur l'axe des 'x' -----*/
		g.setFont(GuiUtils.FONT_11);
		g.setColor(Color.GRAY);
//		g.setColor(Color.BLACK);
		g.drawString(GuiUtils.DECIMAL_3.format(this.x_min), this.marge, this.hauteur-this.marge-5);
		g.drawString(GuiUtils.DECIMAL_3.format(this.x_max), this.largeur-1-this.marge-25, this.centre_y-5);

		/**
		 * Dessine la selection souris.
		 */
		if (this.en_selection || this.selection)
			{
			g.setColor(Color.BLACK);

			g.drawLine(x_debut, this.centre_y-8, x_debut, this.centre_y+8);
			g.drawLine(x_fin, this.centre_y-8, x_fin, this.centre_y+8);
			g.drawRect(this.x_debut-3, this.centre_y-3, 6, 6);
			g.drawRect(this.x_fin-3, this.centre_y-3, 6, 6);

			g.drawString(GuiUtils.DECIMAL_2.format(x0), x_debut-3, this.centre_y-10);
			g.drawString(GuiUtils.DECIMAL_2.format(x1), x_fin-3, this.centre_y-10);
			}
	}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Calcul de la fenêtre de l'estimateur.
	 */
	private static double Fenetre (double etendue) { return etendue * 0.1; }


	/**
	 * Calcul du Noyau de l'estimateur.
	 */
	private static double Noyau (double d)
		{
		double triweight = 35.0/32.0;

		if (d >= -1.0 && d <= 1.0)
			triweight = triweight * (1.0-Calcul.pow2(d)) * Calcul.pow2(1.0-Calcul.pow2(d));
		else
			triweight = 0.0;

		return triweight;
		}


	/*----------------*/
	/* Classe interne */
	/*----------------*/

	final class ComportementSouris extends MouseAdapter
		{
		@Override
		public void mousePressed (MouseEvent evt)
			{
			selection = false;
			en_selection = true;

			x_debut = evt.getX();
			x_fin = x_debut;
			}

		@Override
		public void mouseReleased (MouseEvent evt)
			{
			en_selection = false;

			if (x_debut == x_fin)
				{
				selection = false;

				x_debut = 0;
				x_fin = 0;

				repaint();

				frAnalyse.setEnabledSaveSelection(false);
				}
			else
				frAnalyse.setEnabledSaveSelection(true);
			}

		@Override
		public void mouseDragged (MouseEvent evt)
			{
			if (en_selection)
				{
				selection = true;

				x_fin = evt.getX();

				x0 = (x_debut-marge)/unite_x + x_min;
				x1 = (x_fin-marge)/unite_x + x_min;

				repaint();
				}
			}
		}

} /*----- Fin de la classe EstimateurNoyau -----*/
