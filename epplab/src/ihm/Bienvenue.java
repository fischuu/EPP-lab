package ihm;

import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JComboBoxEA;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import epp.EvolutionPP;
import epp.EPPLab;
import epp.ParametrePP;


/*------------------*/
/* Classe Bienvenue */
/*------------------*/
/**
 * La classe <code>Bienvenue</code> ...
 *
 * @author	Alain Berro, Ingrid Griffit
 * @version	6 juin 2010
 */
public class Bienvenue extends JFrame
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Fenêtres liées.
	 */
	private Repertoire frRepertoire;


	/**
	 * Composants de l'interface.
	 */
	private JButtonEA btOk;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Bienvenue (final EvolutionPP evolution, final ParametrePP parametre)
		{
		/*----- Paramétre de la fenêtre -----*/
		this.setTitle("Bienvenue sur " + EPPLab.nom);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur,BoxLayout.Y_AXIS));

		/*----- Editeur -----*/
		JEditorPane editeur = new JEditorPane();
		editeur.setEditable(false);
		editeur.setPreferredSize(new Dimension(400,250));

		JScrollPane barre_verticale = new JScrollPane(editeur);
		barre_verticale.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		barre_verticale.setMinimumSize(new Dimension(10, 10));

		URL message = this.getClass().getResource("/ihm/html/bienvenue.html");

		try {
			editeur.setPage(message);
			}
		catch (IOException e)
			{
			editeur.setText("Le fichier 'bienvenue.html' est introuvable.");
			}

		conteneur.add(barre_verticale);

		/**
		 * Panneau de commandes.
		 */
		JPanel jpCde = new JPanel();
		jpCde.setLayout(new BorderLayout());
		jpCde.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

		/*----- Changement de langue -----*/
		String[] langue = {"fr", "en", "es"};
		final JComboBoxEA cbLangue = new JComboBoxEA(langue);
		cbLangue.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				String cde = (String)cbLangue.getSelectedItem();

				Locale locale = new Locale("en", "US");
				if (cde.equals("fr")) locale = new Locale("fr", "FR");
				if (cde.equals("es")) locale = new Locale("es", "ES");

				EPPLab.msg = ResourceBundle.getBundle("ihm/MessagesBundle",locale);

				btOk.setText(EPPLab.msg.getString("bt.continuer"));

				/*----- Réajuster la fenêtre -----*/
				repaint();
				pack();
				}
			});
		jpCde.add(cbLangue, BorderLayout.WEST);

		/*----- Bouton 'Valider' -----*/
		this.btOk= new JButtonEA(EPPLab.msg.getString("bt.continuer"));
		this.btOk.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);
				if (frRepertoire == null)
					frRepertoire = new Repertoire(getX(),getY(),Bienvenue.this,evolution,parametre);

				frRepertoire.setVisible(true);
				}
			});

		jpCde.add(this.btOk, BorderLayout.EAST);

  		/*----- Ajoute du panneau de commande à la fenêtre -----*/
		conteneur.add(jpCde);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();
		}

} /*----- Fin de la classe Bienvenue -----*/
