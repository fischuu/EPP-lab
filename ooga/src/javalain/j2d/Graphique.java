package javalain.j2d;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;


/*-----------*/
/* Graphique */
/*-----------*/
/**
 * La classe <code>Graphique</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	24 juillet 2010
 */
public class Graphique extends JFrame
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	  * Données relatives à la fenêtre.
	  */
	private int largeur;
	private int hauteur;
	private int posx;
	private int posy;

	/**
	 * Espace graphique.
	 */
	private JpGraph jpGraph;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Graphique (int l,
					  int h,
					  int px,
					  int py,
					  String titre)
		{
		/*----- Instanciation de la fenêtre graphique -----*/
		this.setTitle(titre);
		this.largeur = l;
		this.hauteur = h;
		this.setSize(this.largeur,this.hauteur);
		this.posx = px;
		this.posy = py;
		this.setLocation(this.posx,this.posy);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*----- Contenu de la fenêtre -----*/
		Container conteneur = getContentPane();
		conteneur.setLayout(new BorderLayout());

		this.jpGraph = new JpGraph(l,h);
		conteneur.add(this.jpGraph, BorderLayout.CENTER);

		/*----- Rend la fenêtre visible -----*/
		this.pack();
		this.setVisible(true);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Ajoute une coordonnées à afficher.
	 */
	public void addNombre (double x, double y) { this.jpGraph.addNombre(x,y); }


	/**
	 * Remise à zéro de la courbe.
	 */
	public void raz () { this.jpGraph.raz(); }

} /*----- Fin de la classe Graphique -----*/
