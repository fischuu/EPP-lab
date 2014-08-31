package javalain.algorithmegenetique.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;


/*---------------*/
/* Classe Courbe */
/*---------------*/
/**
 * La classe <code>Courbe</code> dessine une courbe graphique à partir d'une
 * liste de nombres.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 mai 2009
 */
public class Courbe extends JPanel
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Tableau de stockage des données à afficher.
	 */
	private float tabNb[];
	private int indice;


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


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Courbe (int l, int h)
		{
		this.largeur = l;
		this.hauteur = h;

		this.tabNb = new float[500];
		this.indice = -1;

		this.setMinimumSize(new Dimension(this.largeur,this.hauteur));
		this.setPreferredSize(new Dimension(this.largeur,this.hauteur));
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Remise à zéro de la courbe.
	 */
	public void raz ()
		{
		this.indice = -1;

		/*----- Retrace la courbe -----*/
		this.repaint();
		}


	/**
	 * Ajoute une donnée à afficher.
	 */
	public void addNombre (float f)
		{
		if	(this.indice == -1)
			{
			this.min = f;
			this.max = f;
			}

		int taille = this.tabNb.length-1;

		if	(this.indice == taille)
			{
			int n_taille	= (taille*120)/100+1;
			float[] tab		= this.tabNb;
			this.tabNb		= new float[n_taille];

			System.arraycopy(tab,0,this.tabNb,0,taille);
			}

		if	(f > this.max) this.max = f;
		if	(f < this.min) this.min = f;

		this.tabNb[++this.indice] = f;

		/*----- Retrace la courbe -----*/
		this.repaint();
		}


	/**
	 * Effectue le tracé de la courbe.
	 */
	@Override
	public void paint (Graphics g)
		{
		/*----- Couleur de fond -----*/
		g.setColor(Color.lightGray);
		g.fillRect(0,0,this.largeur,this.hauteur);

		/*----- Affichage des points -----*/
		float echelleX;
		if	(this.indice < this.largeur-4) // Marge de 2 pixels.
			echelleX = 1.0f;
		else
			echelleX = (float)(this.largeur-6)/this.indice;

		float echelleY;
		echelleY = (this.hauteur-6)/(this.max-this.min);

		g.setColor(Color.black);
		for	(int i=0; i<=this.indice; i++)
			g.fillRect((int)(i*echelleX) + 2,
					   (this.hauteur-6) - (int)((this.tabNb[i]-this.min)*echelleY) + 2,
					   2,
					   2);
		}

} /*----- Fin de la classe Courbe -----*/
