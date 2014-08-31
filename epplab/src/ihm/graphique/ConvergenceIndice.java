package ihm.graphique;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import util.GuiUtils;


/*--------------------------*/
/* Classe ConvergenceIndice */
/*--------------------------*/
/**
 * La classe <code>ConvergenceIndice</code> dessine la courbe
 * de convergence de I.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	22 mars 2010
 */
public class ConvergenceIndice extends JPanel
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Tableau de stockage des données à afficher.
	 */
	private float tabNb[];
	private int indiceTab;


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
	private int marge = 2;
	private int taille_point = 2;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public ConvergenceIndice (int l, int h)
		{
		this.largeur = l;
		this.hauteur = h;

		this.tabNb = new float[500];
		this.indiceTab = -1;

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
		this.indiceTab = -1;

		/*----- Retrace la courbe -----*/
		this.repaint();
		}


	/**
	 * Ajoute une donnée à afficher.
	 */
	public void addNombre (float f)
		{
		if	(this.indiceTab == -1)
			{
			this.min = f;
			this.max = f;
			}

		int taille = this.tabNb.length;

		if	(this.indiceTab == (taille-1))
			{
			int n_taille	= (taille*120)/100 + 1;
			float[] tab		= this.tabNb;
			this.tabNb		= new float[n_taille];

			System.arraycopy(tab,0,this.tabNb,0,taille);
			}

		if	(f > this.max) this.max = f;
		if	(f < this.min) this.min = f;

		this.tabNb[++this.indiceTab] = f;

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
//		g.setColor(GuiUtils.VERT_LEGER);
		g.setColor(GuiUtils.GRIS_238);
		g.fillRect(0,0,this.largeur,this.hauteur);

		/*----- Echelles -----*/
		float echelleX = (this.largeur-2*this.marge-this.taille_point)/(float)this.indiceTab;
		float echelleY = (this.hauteur-2*this.marge-this.taille_point)/(this.max-this.min);

		/*----- Affichage des points -----*/
		g.setColor(Color.black);
		for	(int i=0; i<=this.indiceTab; i++)
			g.fillRect((int)(i*echelleX) + this.marge,
					   this.hauteur-this.marge-this.taille_point - (int)((this.tabNb[i]-this.min)*echelleY),
					   this.taille_point,
					   this.taille_point);

		/*----- Affiche le maximum -----*/
		if (this.indiceTab != -1)
			{
			g.setFont(GuiUtils.FONT_11);
			g.drawString(GuiUtils.SCIENTIFIQUE.format(this.max), this.largeur-65, this.hauteur-5);
			}
		}

} /*----- Fin de la classe ConvergenceIndice -----*/
