package ihm.graphique;

import epp.OrdonnerPP;
import ihm.controle.JPanelEA;
import indice.Indice;
import java.awt.event.ActionEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javalain.algorithmegenetique.ihm.composant.JCheckBoxMenuItemEA;
import javalain.math.Calcul;
import util.GuiUtils;


/*-----------------------*/
/* Classe CourbeAtypique */
/*-----------------------*/
/**
 * La classe <code>CourbeAtypique</code> ...
 *
 * @author	Alain Berro, Ingrid Griffit
 * @version	25 mai 2010
 */
public class CourbeAtypique extends JPanelEA
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Liste des résultats des simulations (lancements) -----*/
	private ArrayList<OrdonnerPP> liste;

	/*----- Indice de projection -----*/
	private Indice I;

	/*----- Détermine si une observation est atypique (>nbEC*ecart-type ou <-nbEC*ecart-type) -----*/
	private int nbEC = 0;

	/*----- Tableau pour marquer les données atypiques [Numéro de lancement][Numéro d'observation] -----*/
	private boolean isAtypiques[][];


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

	/*----- Indice de l'observation sélectionnée -----*/
	private int indiceObsSelect;


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

	/*----- Affecte le style d'affichage -----*/
	private final JCheckBoxMenuItemEA avecCumul;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public CourbeAtypique (int l, int h)
		{
		this.largeur = l;
		this.hauteur = h;

		/*----- Position de l'axe des abscisses -----*/
		this.centre_y = this.hauteur-1-this.marge_bas;

		this.indiceMin = 0;
		this.indiceMax = -1;
		this.indiceRunSelect = 0;
		this.indiceObsSelect = 0;
		this.indiceRef = 0;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));

		/*----- Menu contextuel -----*/
		this.avecCumul = new JCheckBoxMenuItemEA("Cumul"); // A faire - Traduire
		this.avecCumul.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e) { repaint(); }
			});
		this.getMenuContextuel().addSeparator();
		this.getMenuContextuel().add(this.avecCumul);
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
		this.indiceMin = 0;
		this.indiceMax = -1;
		}


	/**
	 * Modifie les indices minimun, maximum du tableau des valeurs à afficher.
	 */
	public void setIndiceMinimum (int i) { /*this.indiceMin = i; */ }
	public void setIndiceMaximum (int i) { /*this.indiceMax = i; */ }


	/**
	 * Modifie les indices de lancement.
	 */
	public void setIndiceRunSelect (int i) { this.indiceRunSelect = i; }
	public void setIndiceReference (int i) { this.indiceRef = i; }


	/**
	 * Modifie l'indice de l'observation sélectionnée.
	 */
	public void setIndiceObsSelect (int i) { this.indiceObsSelect = i; }


	/**
	 * Mise à jour de l'indice de projection  et de la liste des résultats des
	 * simulations afin d'effectuer les calculs de projection etensuite la recherche d'atypiques.
	 */
	public void setIndProjAndListeSimu (Indice i, ArrayList<OrdonnerPP> l)
		{
		this.I = i;
		this.liste = l;

		this.indiceMax = l.size()-1;

		/*----- Initialisation du tableau pour marquer les atypiques en fonction du nombre d'écart-type -----*/
		this.isAtypiques = new boolean[l.size()][i.getMatrice().nbLigne()];
		}


	/**
	 * Mise à jour de la valeur pour marquer les atypiques.
	 */
	public void setValeurAtypiques (int ec)
		{
		this.nbEC = ec;

		if (this.I != null && this.liste != null && this.nbEC != 0)
			{
			double ecartType;
			double moyenne;
			double[] donnees_x = new double[this.I.getMatrice().nbLigne()];

			int nbObs = this.I.getMatrice().nbLigne();
			int nbLancement = this.liste.size();

			for (int i=0; i<nbLancement; i++)
				{
				/*----- Projection des observations sur le résultat de la simulation 'i' -----*/
				for (int j=0; j<nbObs; j++)
					donnees_x[j] = Calcul.produitScalaireVecteur(this.I.getMatrice().ligne(j), this.liste.get(i).getA());

				/*----- Calcul de l'écart-type et de la moyenne -----*/
				ecartType = Calcul.ecartType(donnees_x);
				moyenne = Calcul.moyenne(donnees_x);

				/*----- Teste si la valeur projetée est atypique -----*/
				for (int j=0; j<nbObs; j++)
					this.isAtypiques[i][j] = (donnees_x[j] < (moyenne - ecartType * this.nbEC)) || (donnees_x[j] > (moyenne + ecartType * this.nbEC));
				}
			}
		}


	/**
	 * Dessine la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		int nb;

		/*----- Variables locales -----*/
		int nb_lancement = this.liste.size();
		int nb_obs = this.I.getMatrice().nbLigne();


		/**
		 * Couleur de fond.
		 */
//		g.setColor(GuiUtils.VERT_LEGER);
		g.setColor(GuiUtils.blanc);
		g.fillRect(0,0,this.largeur,this.hauteur);


		/**
		 * Axes.
		 */
		/*----- Calcul des échelles -----*/
		float echelleX = (this.largeur-this.marge_gauche-this.marge_droite-this.taille_point)/(float)(this.indiceMax-this.indiceMin+1);
		float echelleY = (this.hauteur-this.marge_bas-this.marge_haut-this.taille_point)/(float)(nb_obs);

		/*----- Affichage de l'axe X -----*/
		g.setColor(Color.GRAY);
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

		/*----- Affiche l'échelle des Y -----*/
		pas = 1;
		nb = nb_obs;
		if (nb >= 10)
			{
			if (nb >= 20)
				pas = nb/5;
			else
				pas = 2;
			}

		for	(int i=1; i<=nb_obs ; i++)
			{
			if (i%pas == 0)
				{
				g.drawLine(this.marge_gauche-3, (int)((nb_obs-i)*echelleY)+this.marge_haut, this.marge_gauche, (int)((nb_obs-i)*echelleY)+this.marge_haut);
				g.drawString("" + i, this.marge_gauche-25, (int)((nb_obs-i)*echelleY)+this.marge_haut);
				}
			}


		/**
		 * Lancement et observation sélectionnés.
		 */
		/*----- Affichage du lancement sélectionné -----*/
		g.setColor(Color.BLUE);
		g.setFont(GuiUtils.FONT_PARAMETRE);
		g.drawLine((int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche, this.marge_haut, (int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche, this.centre_y);
		g.drawString("" + (this.indiceRunSelect+1), (int)((this.indiceRunSelect+1-this.indiceMin)*echelleX) + this.marge_gauche + 4, this.centre_y + 10);

		/*----- Affichage de l'observation sélectionnée -----*/
		g.drawLine(this.marge_gauche, (int)((nb_obs-this.indiceObsSelect-1)*echelleY) + this.marge_haut, this.largeur-1-this.marge_droite, (int)((nb_obs-this.indiceObsSelect-1)*echelleY) + this.marge_haut);
		g.drawString("" + (this.indiceObsSelect+1), 0, (int)((nb_obs-this.indiceObsSelect)*echelleY) + this.marge_haut);


		/**
		 * On affiche tous les points en noir.
		 */
		int[] nbAtypParRun = new int[nb_lancement];
		int[] nbAtypParObs = new int [nb_obs];

		g.setColor(Color.BLACK);
		for (int i=0; i<nb_lancement; i++)
			{
			for (int j=0; j<nb_obs; j++)
				{
				if (this.isAtypiques[i][j])
					{
					nbAtypParRun[i]++;
					nbAtypParObs[j]++;

					if (this.avecCumul.isSelected() && i<=this.indiceRunSelect)
						{
						g.fillRect((int)((nbAtypParObs[j])*echelleX) + this.marge_gauche - this.decal_point,
								   (int)((nb_obs-j-1)*echelleY) + this.marge_haut - this.decal_point,
								   this.taille_point,
								   this.taille_point);
						}
					else
						{
						g.fillRect((int)((i+1)*echelleX) + this.marge_gauche - this.decal_point,
								   (int)((nb_obs-j-1)*echelleY) + this.marge_haut - this.decal_point,
								   this.taille_point,
								   this.taille_point);
						}
					}
				}
			}


		/**
		 * Lancement de référence (en rouge).
		 */
		if (this.indiceMin <= this.indiceRef && this.indiceRef <= this.indiceMax)
			{
			g.setColor(Color.RED);
			g.fillOval((int)((this.indiceRef+1-this.indiceMin)*echelleX) + this.marge_gauche - 4,
					   this.marge_haut - 4,
					   8,
					   8);
			}


		/**
		 * On affiche les points sélectionnés en jaune.
		 */
		g.setColor(Color.YELLOW);

		/*----- Observation sélectionnée-----*/
		int cpt = 0;
		for (int i=0; i<nb_lancement; i++)
			{
			if (this.isAtypiques[i][this.indiceObsSelect])
				{
				cpt++;
				if (this.avecCumul.isSelected() && i<=this.indiceRunSelect)
					{
					g.fillRect((int)(cpt*echelleX) + this.marge_gauche - this.decal_point,
							   (int)((nb_obs-this.indiceObsSelect-1)*echelleY) + this.marge_haut - this.decal_point,
							   this.taille_point,
							   this.taille_point);
					}
				else
					{
					g.fillRect((int)((i+1)*echelleX) + this.marge_gauche - this.decal_point,
							   (int)((nb_obs-this.indiceObsSelect-1)*echelleY) + this.marge_haut - this.decal_point,
							   this.taille_point,
							   this.taille_point);
					}
				}
			}


		/*----- Lancement sélectionnée-----*/
		for (int i=0; i<nb_obs; i++)
			{
			if (this.isAtypiques[this.indiceRunSelect][i])
				{
				if (this.avecCumul.isSelected())
					{
					/*----- Compte du nombre d'atypique de cette observation jusqu'au lancement sélectionné -----*/
					cpt = 0;
					for (int j=0; j<=this.indiceRunSelect; j++)
						if (this.isAtypiques[j][i]) cpt++;

					g.fillRect((int)(cpt*echelleX) + this.marge_gauche - this.decal_point,
							   (int)((nb_obs-i-1)*echelleY) + this.marge_haut - this.decal_point,
							   this.taille_point,
							   this.taille_point);
					}
				else
					{
					g.fillRect((int)((this.indiceRunSelect+1)*echelleX) + this.marge_gauche - this.decal_point,
							   (int)((nb_obs-i-1)*echelleY) + this.marge_haut - this.decal_point,
							   this.taille_point,
							   this.taille_point);
					}
				}
			}


		/**
		 * Affichage des nombres d'atypiques.
		 */
		/*----- Affichage du nombre d'atypiques pour le lancement selectionné -----*/
		g.setColor(Color.BLUE);
		g.drawString("" + nbAtypParRun[this.indiceRunSelect], (int)((this.indiceRunSelect+1)*echelleX) + this.marge_gauche + 4, this.marge_haut - 3);

		/*----- Affichage du nombre d'atypiques pour l'observation selectionnée -----*/
		g.drawString("" + nbAtypParObs[this.indiceObsSelect], this.largeur-this.marge_droite + 3, (int)((nb_obs-this.indiceObsSelect-1)*echelleY) + this.marge_haut);
		}

} /*----- Fin de la classe CourbeAtypique -----*/
