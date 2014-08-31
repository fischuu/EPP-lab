package ihm.graphique;

import ihm.Analyse;
import javalain.math.Calcul;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JPanel;
import util.GuiUtils;


/*--------------------*/
/* Classe Histogramme */
/*--------------------*/
/**
 * La classe <code>Histogramme</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	5 juin 2010
 */
public class Histogramme extends JPanel
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Dimension de la fenêtre.
	 */
	private final int echelle_fenetre = 100;
	private final int marge = 20;


	/*------------*/
	/* Références */
	/*------------*/

	/**
	 * Fenêtre parente.
	 */
	private Analyse frAnalyse;


	/*---------*/
	/* Données */
	/*---------*/
	
	/**
	 * Tableau des données projetées à visualiser
	 */
	private double[]	donnees_x;
	private double		ecart_type;
	private double		moyenne;


	/**
	 * Taille de la zone d'affichage.
	 */
	private int largeur;
	private int hauteur;

	/*----- Position de l'axe des abscisses -----*/
	private int centre_y;

	/*----- Paramètres de l'axe des abscisses -----*/
	private int		decal_x;
	private double	unite_x;	// Valeur du l'unité sur l'axe des x
	private int		axe_x_min;	// Valeur entière minimale affichée sur l'axe des x
	private int		lg_x;		// Nombre d'unité sur l'axe des x

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

	/*----- Données de test -----*/
	private String fichier_donnees = "";

	/*----- Informations sur les données -----*/
	private String nomIndice;
	private String nomMethode;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Histogramme (int l, int h, boolean atypique, boolean selection_souris, Analyse analyse)
		{
		/*----- Taille de la zone d'affichage -----*/
		this.largeur = l*this.echelle_fenetre + 2*this.marge;
		this.hauteur = h*this.echelle_fenetre + 2*this.marge;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));

		/*----- Position de l'axe des abscisses -----*/
		this.centre_y = this.hauteur - 82;

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
	 * Mise à jour des données de la projection à afficher.
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

		/*----- Recherche des valeurs projetées minimale et maximale -----*/
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		for (int i=0; i<this.donnees_x.length; i++)
			{
			if (this.donnees_x[i] < min) min = this.donnees_x[i]; else
			if (this.donnees_x[i] > max) max = this.donnees_x[i];
			}

		/*----- Espace de coordonnées en x -----*/
		this.axe_x_min	= (int)min + (min < 0.0 ? -1 : 0);
		int axe_x_max	= (int)max + (max > 0.0 ? +1 : 0);
		this.lg_x		= axe_x_max - this.axe_x_min;

		this.unite_x = (this.largeur-2*this.marge)/(float)this.lg_x;
		this.decal_x = (int)((this.largeur-2*this.marge - this.unite_x*this.lg_x)/2 + this.marge);

		/*----- Raz des indicateurs de sélection -----*/
		this.selection = false;
		this.en_selection = false;
		}


	/**
	 * Précise le nom du fichier de données.
	 */
	public void setFichierDonnees (String f)
		{
		this.fichier_donnees = f;
		}


	/**
	 * Mise à jour de la valeur pour marquer les atypiques.
	 */
	public void setValeurAtypiques (int i) { this.valAtypique = i; }


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
		this.x_debut = (int)((x0-this.axe_x_min)*this.unite_x + decal_x);
		this.x_fin = (int)((x1-this.axe_x_min)*this.unite_x + decal_x);
		}


	/**
	 * Retourne le tableau des données projetées.
	 */
	public double[] getDonnees () { return this.donnees_x; }


	/**
	 * Mise à jour des informations.
	 */
	public void setInfos (String nomIndice, String nomMethode)
		{
		this.nomIndice = nomIndice;
		this.nomMethode = nomMethode;
		}


	/**
	 * Dessine la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		/*----- Si aucune donnée alors on termine -----*/
		if (this.donnees_x == null) return;

		int i, tps;

		/*----- On détermine les 3 plus "grands" atypiques -----*/
//		int ind_min = 0;
//		int ind_max = 0;
//		int ind_min_2 = 0;
//		int ind_max_2 = 0;
//		int ind_min_3 = 0;
//		int ind_max_3 = 0;
//		double min_1 = Double.POSITIVE_INFINITY;
//		double max_1 = Double.NEGATIVE_INFINITY;
//		double min_2 = Double.POSITIVE_INFINITY;
//		double max_2 = Double.NEGATIVE_INFINITY;
//		double min_3 = Double.POSITIVE_INFINITY;
//		double max_3 = Double.NEGATIVE_INFINITY;
//
//		for (i=0; i<donnees_x.length; i++)
//			{
//			if (donnees_x[i] < min_1)
//				{
//				ind_min_3 = ind_min_2;
//				min_3 = min_2;
//				ind_min_2 = ind_min;
//				min_2 = min_1;
//				ind_min = i;
//				min_1 = donnees_x[i];
//				}
//			else
//				if (donnees_x[i] < min_2)
//					{
//					ind_min_3 = ind_min_2;
//					min_3 = min_2;
//					ind_min_2 = i;
//					min_2 = donnees_x[i];
//					}
//				else
//					if (donnees_x[i] < min_3)
//						{
//						ind_min_3 = i;
//						min_3 = donnees_x[i];
//						}
//
//			if (donnees_x[i] > max_1)
//				{
//				ind_max_3 = ind_max_2;
//				max_3 = max_2;
//				ind_max_2 = ind_max;
//				max_2 = max_1;
//				ind_max = i;
//				max_1 = donnees_x[i];
//				}
//			else
//				if (donnees_x[i] > max_2)
//					{
//					ind_max_3 = ind_max_2;
//					max_3 = max_2;
//					ind_max_2 = i;
//					max_2 = donnees_x[i];
//					}
//				else
//					if (donnees_x[i] > max_3)
//						{
//						ind_max_3 = i;
//						max_3 = donnees_x[i];
//						}
//			}

		/*----- Effacement de la zone d'affichage -----*/
		g.setColor(GuiUtils.GRIS_238);
//		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.largeur,this.hauteur);

		/*----- Affichage de l'axe x -----*/
		int pas_x = 1;
		if (this.lg_x >= 10)
			{
			if (this.lg_x >= 20)
				pas_x = this.lg_x/10;
			else
				pas_x = 2;
			}

		g.setFont(GuiUtils.FONT_11);
		g.setColor(Color.GRAY);
//		g.setColor(Color.BLACK);
		g.drawLine(this.decal_x, this.centre_y, this.decal_x + (int)(this.unite_x*this.lg_x), this.centre_y);
		for (i=0; i<=this.lg_x; i++)
			{
			if (i%pas_x == 0)
				{
				tps = this.decal_x + (int)(i*this.unite_x);
				g.drawString("" + (i+this.axe_x_min), tps, this.centre_y+15);
				g.drawLine(tps, this.centre_y-3, tps, this.centre_y+3);
				}
			}

		/*----- Affichage de l'histogramme -----*/
		int nb_hist = 100;

		if (this.fichier_donnees.startsWith("gorille")) nb_hist = 30; else
		if (this.fichier_donnees.startsWith("lub")) nb_hist = 40; else
		if (this.fichier_donnees.startsWith("don")) nb_hist = 50; else
		if (this.fichier_donnees.equals("olive.txt")) nb_hist = 80; else
		if (this.fichier_donnees.equals("oliveA.txt")) nb_hist = 80; else
		if (this.fichier_donnees.equals("olive3.txt")) nb_hist = 40; else
		if (this.fichier_donnees.startsWith("fiab")) nb_hist = 200;

		/*----- Tableau du nombre d'observations par barre de l'histogramme  ----*/
		int[] histogramme = new int[nb_hist];

		/*----- Tableau pour marquer les barres contenant des atypiques -----*/
		boolean[] isAtypique = new boolean[nb_hist];

		int k;
		for (i=0; i<this.donnees_x.length; i++)
			{
			k = (int)((this.donnees_x[i]-this.axe_x_min)*nb_hist/this.lg_x);

			/*----- On compte le nombre de données par barre -----*/
			try {
			histogramme[k]++; // A suivre - Il y a régulièrement un exception lévée ! dernière fois en utilisant les données fiab Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 213
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.println("axe_x_min = " + axe_x_min);
				System.out.println("lg_x = " + lg_x);
				System.out.println("i = " + i);
				System.out.println("donnees_x[i] = " + donnees_x[i]);
				System.out.println("nb_hist = " + nb_hist);
				System.out.println("calcul = " + ((this.donnees_x[i]-this.axe_x_min)*nb_hist/this.lg_x));
			}

			if (this.atypique)
				/*----- On marque les barres contenant des atypiques -----*/
				isAtypique[k] = this.atypique &&
								this.valAtypique != 0 &&
								(this.donnees_x[i] < (moyenne - this.ecart_type*this.valAtypique) ||
								 this.donnees_x[i] > (moyenne + this.ecart_type*this.valAtypique));
			}

		/*----- Recherche de la barre la plus haute -----*/
		double max = 0;
		for (i=0; i<nb_hist; i++)
			if (histogramme[i] > max) max = histogramme[i];

		/*----- Histogramme -----*/
		boolean affiche = false;
		for (i=0; i<nb_hist; i++)
			if (histogramme[i] != 0)
				{
				/*----- Couleurs : histogramme en gris, atypiques en orange ----*/
				g.setColor((isAtypique[i] ? Color.ORANGE : Color.GRAY));
//				g.setColor((isAtypique[i] ? Color.GRAY : Color.GRAY));

				/*----- Dessin des barres de l'histogramme -----*/
				tps = Math.max(2,(int)(histogramme[i] * (this.centre_y-this.marge)/max));

				g.fillRect (this.decal_x + (int)(i*this.unite_x*this.lg_x/nb_hist),
							this.centre_y - tps,
							(int)(this.unite_x*this.lg_x/nb_hist) + 1,
							tps + 1);

				if (!affiche && histogramme[i] == max)
					{
					affiche = true;
					g.setColor(Color.LIGHT_GRAY);
					g.drawString("" + (int)max, this.decal_x + (int)(i*this.unite_x*this.lg_x/nb_hist), this.centre_y-tps-5);
//					g.setColor(Color.GRAY);
					}
				}



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




		/*----- Affichage des informations -----*/
//		g.setFont(GuiUtils.TAHOMA_18_BOLD);
//		g.setColor(Color.LIGHT_GRAY);
//		if (this.fichier_donnees != null)
//			{
//			String s = this.fichier_donnees.substring(0,this.fichier_donnees.lastIndexOf('.'));
//			if (s.startsWith("fiab")) s = "reliability";
//			if (s.startsWith("lubischev")) s = "lubischew";
//
//			g.drawString(s, this.largeur-this.marge-100, this.marge+5);
//			}
//		if (this.nomIndice != null) g.drawString(this.nomIndice, this.largeur-this.marge-100, this.marge+25);
//		if (this.nomMethode != null)
//			{
//			String s = "";
//			if (this.nomMethode.equals(ParametrePP.GA)) s = "GA";
//			if (this.nomMethode.equals(ParametrePP.PSO)) s = "PSO";
//
//			g.drawString(s, this.largeur-this.marge-100, this.marge+45);
//			}


		/**
		 * Affichage pour les données de Lubischev.
		 *
		 * 74 observations et 6 grandeurs morphologiques.
		 * 3 groupes : (1 à 21), (22 à 43) et (44 à 74).
		 */
		if (fichier_donnees.startsWith("lub"))
			{
			g.setColor(Color.blue);
			for (i=0; i<21; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=21; i<43; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}

			g.setColor(Color.green);
			for (i=43; i<74; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+44, tps, this.centre_y+54);
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}
			}
		else

		/**
		 * Don 3 et 4.
		 *
		 * 200 observations et 2 grandeurs.
		 * 2 groupes : (1 à 190) et (191 à 200).
		 */
		if ((fichier_donnees.startsWith("don3")) || (fichier_donnees.startsWith("don4")))
			{
			g.setColor(Color.blue);
			for (i=0; i<190; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=190; i<200; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
			}
		else

		/**
		 * Don 1 et 2.
		 *
		 * 200 observations et 2 grandeurs.
		 * 2 groupes : (1 à 100) et (101 à 200).
		 */
		if ((fichier_donnees.startsWith("don1")) || (fichier_donnees.startsWith("don2")))
			{
			g.setColor(Color.blue);
			for (i=0; i<100; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=100; i<200; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
			}
		else

		/**
		 * Fiab.
		 */
		if (fichier_donnees.startsWith("fiab"))
			{
			g.setColor(Color.blue);
			for (i=0; i<donnees_x.length; i++)
				{
//				g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+20, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+30);
				g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+20, 3, 11);

//				if (i == ind_min   || i == ind_max)   g.drawString("" + (i+1), (int)((donnees_x[i]-axe_x_min)*unite_x)+decal_x, centre_y+41);
//				if (i == ind_max_2 || i == ind_min_2) g.drawString("" + (i+1), (int)((donnees_x[i]-axe_x_min)*unite_x)+decal_x, centre_y+51);
//				if (i == ind_max_3 || i == ind_min_3) g.drawString("" + (i+1), (int)((donnees_x[i]-axe_x_min)*unite_x)+decal_x, centre_y+61);
				}
			}
		else

		/**
		 * Tous les fichiers commençant par 'olive'.
		 *
		 * Partie A (oliveA) :
		 *	. groupe (0 à 24)
		 *	. groupe (25 à 80)
		 *	. groupe (81 à 286) --> (olive3)
		 *	. groupe (287 à 322)
		 * 
		 * Partie B :
		 *	. groupe (323 à 387)
		 *	. groupe (388 à 420)
		 *	. groupe (421 à 470)
		 *	. groupe (471 à 520)
		 *	. groupe (521 à 572)
		 */
		if (frAnalyse != null && fichier_donnees.startsWith("olive"))
			{
			/*----- Fichier original 'olive.txt' -----*/
			String filePath1 = frAnalyse.getIhm().getParametre().rep_donnees + File.separatorChar + "olive.txt";

			/*----- Fichier courant -----*/
			String filePath2 = frAnalyse.getIhm().getParametre().rep_donnees + File.separatorChar + fichier_donnees;

			/*----- Ouverture des fichiers et des buffers -----*/
			FileReader fichier1;
			FileReader fichier2;
			BufferedReader buffer1 = null;
			BufferedReader buffer2 = null;
			try	{
				fichier1 = new FileReader(filePath1);
				fichier2 = new FileReader(filePath2);

				buffer1 = new BufferedReader(fichier1);
				buffer2 = new BufferedReader(fichier2);

				buffer2.readLine();
				buffer2.readLine();
				}
			catch (Exception e) {}

			int num = 0;
			String ch;
			for (i=0; i<donnees_x.length; i++)
				{
				/*----- Trouver le numéro initial de l'indice -----*/
				try	{
					/*----- Lecture de le fichier courant -----*/
					ch = buffer2.readLine();
					ch = ch.replace(".0", "");

					/*----- On recherche la même ligne dans le fichier original -----*/
					while (!buffer1.readLine().equals(ch)) { num++; }

					/*----- Couleur en fonction du groupe -----*/
					if (num-2 >= 521)
						{
						g.setColor(Color.cyan);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+68, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+78);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+68, 1, 11);
						}
					else
					if (num-2 >= 471)
						{
						g.setColor(Color.magenta);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+56, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+66);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+56, 1, 11);
						}
					else
					if (num-2 >= 421)
						{
						g.setColor(Color.pink);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+44, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+54);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+44, 1, 11);
						}
					else
					if (num-2 >= 388)
						{
						g.setColor(Color.yellow);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+32, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+42);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+32, 1, 11);
						}
					else
					if (num-2 >= 323)
						{
						g.setColor(Color.orange);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+20, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+30);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+20, 1, 11);
						}
					else
					if (num-2 >= 287)
						{
						g.setColor(Color.red);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+56, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+66);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+56, 1, 11);
						}
					else
					if (num-2 >= 81)
						{
						g.setColor(Color.green);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+44, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+54);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+44, 1, 11);
						}
					else
					if (num-2 >= 25)
						{
						g.setColor(Color.black);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+32, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+42);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+32, 1, 11);
						}
					else
					if (num-2 >= 0)
						{
						g.setColor(Color.blue);
//						g.drawLine((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+20, (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+30);
						g.fillRect((int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+20, 1, 11);
						}

					num++;
					}
				catch (Exception e)
					{
					System.out.println("Erreur dans la recherche dans le fichier olive.txt original.");
					System.exit(0);
					}
				}
			}
		else

		/**
		 * Gorille.
		 *
		 * 59 observations et 12 dimensions.
		 * 2 groupes : (0 à 28) les mâles et (29 à 58) les femelles.
		 */
		if (fichier_donnees.startsWith("gorille"))
			{
			g.setColor(Color.blue);
			for (i=0; i<29; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=29; i<59; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
			}
		else

		/**
		 * Ruspini.
		 *
		 * 75 observations et 2 dimensions.
		 * 4 groupes : (1 à 20), (21 à 43), (44 à 60) et (61 à 75).
		 */
		if (fichier_donnees.startsWith("ruspini"))
			{
			g.setColor(Color.blue);
			for (i=0; i<20; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=20; i<43; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
			g.setColor(Color.yellow);
			for (i=43; i<60; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}

			g.setColor(Color.red);
			for (i=60; i<75; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
				g.fillRect(tps-1, this.centre_y+56, 3, 11);
				}
			}
		else

		/**
		 * donsimu83 : 200 observations et 8 variables
		 * 3 groupes : (1 à 100), (101 à 150) et (151 à 200)
		 */
		if (fichier_donnees.startsWith("donsimu83"))
			{
			g.setColor(Color.blue);
			for (i=0; i<100; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.black);
			for (i=100; i<150; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}

			g.setColor(Color.green);
			for (i=150; i<200; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+44, tps, this.centre_y+54);
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}
			}
		else

		/**
		 * Normale2 : 1000 observations et 8 variables
		 * 2 groupes : (1 à 500) et (500 à 1000)
		 */
		if (fichier_donnees.startsWith("normale2"))
			{
			g.setColor(Color.blue);
			for (i=0; i<500; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.red);
			for (i=500; i<1000; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
			}
        else

		/**
		 * Normale4 : 1000 observations et 8 variables
		 * 4 groupes : (1 à 250), (251 à 500), (501 à 750) et (751 à 1000)
		 */
		if (fichier_donnees.startsWith("normale4"))
			{
			g.setColor(Color.blue);
			for (i=0; i<250; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.pink);
			for (i=250; i<500; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
            g.setColor(Color.red);
			for (i=500; i<750; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}

			g.setColor(Color.orange);
			for (i=750; i<1000; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+56, 3, 11);
				}
			}
		else

		/**
		 * Normale10 : 1000 observations et 8 variables
		 * 10 groupes : 100 obseravations pour chacun
		 */
		if (fichier_donnees.startsWith("normale10"))
			{
			g.setColor(Color.blue);
			for (i=0; i<100; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}

			g.setColor(Color.pink);
			for (i=100; i<200; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}
            g.setColor(Color.red);
			for (i=200; i<300; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}

			g.setColor(Color.orange);
			for (i=300; i<400; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+56, 3, 11);
				}
            g.setColor(Color.green);
			for (i=400; i<500; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+68, 3, 11);
				}

			g.setColor(Color.yellow);
			for (i=500; i<600; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+20, 3, 11);
				}
            g.setColor(Color.black);
			for (i=600; i<700; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+32, 3, 11);
				}

			g.setColor(Color.magenta);
			for (i=700; i<800; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+44, 3, 11);
				}
            g.setColor(Color.cyan);
			for (i=800; i<900; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+20, tps, this.centre_y+30);
				g.fillRect(tps-1, this.centre_y+56, 3, 11);
				}

			g.setColor(Color.gray);
			for (i=900; i<1000; i++)
				{
				tps = (int)((this.donnees_x[i]-this.axe_x_min)*this.unite_x) + this.decal_x;
//				g.drawLine(tps, this.centre_y+32, tps, this.centre_y+42);
				g.fillRect(tps-1, this.centre_y+68, 3, 11);
				}
			}
		else

		if (fichier_donnees.equals("QN_BadPart300.txt"))
			{
			g.setColor(Color.blue);
//			g.drawLine((int)((this.donnees_x[299]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+20, (int)((this.donnees_x[299]-this.axe_x_min)*this.unite_x) + this.decal_x, this.centre_y+30);
			g.fillRect((int)((this.donnees_x[299]-this.axe_x_min)*this.unite_x) + this.decal_x-1, this.centre_y+20, 2, 11);
			}
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

				x0 = (x_debut-decal_x)/unite_x + axe_x_min;
				x1 = (x_fin-decal_x)/unite_x + axe_x_min;

				repaint();
				}
			}
		}

} /*----- Fin de la classe Histogramme -----*/
