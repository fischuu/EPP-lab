package ihm;

import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JTextFieldEA;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javalain.algorithmegenetique.ihm.composant.JCheckBoxEA;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import epp.EvolutionPP;
import epp.EPPLab;
import epp.ParametrePP;
import util.FileUtils;
import util.GuiUtils;
import util.MsgUtils;


/*-------------------*/
/* Classe Repertoire */
/*-------------------*/
/**
 * La classe <code>Repertoire</code> ...
 *
 * @author	Alain Berro, Ingrid Griffit
 * @version	6 juin 2010
 */
public class Repertoire extends JFrame
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Fenêtre précédente.
	 */
	private Bienvenue frBienvenue;


	/**
	 * Composants de l'interface.
	 */
	private JTextFieldEA	txtDirWork;
	private JButtonEA 		btContinue;
	private JCheckBoxEA		cbCopieExemple;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Repertoire (int x, int y, Bienvenue f, final EvolutionPP evolution, final ParametrePP parametre)
		{
		/*----- Paramétre de la fenêtre -----*/
		this.setTitle(EPPLab.nom);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(x,y);

		/*----- Lien vers la fenêtre principale -----*/
		this.frBienvenue = f;

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur,BoxLayout.PAGE_AXIS));

		/*----- Panneau -----*/
//		JPanel jp0 = new JPanel();
//		jp0.setLayout(new BoxLayout(jp0,BoxLayout.PAGE_AXIS));
//		jp0.setBorder(new TitledBorderEA("Répertoire de travail"));
//		jp0.setBorder(GuiUtils.BOUTON_BORDER);

		/*----- Texte -----*/
		JEditorPane editeur = new JEditorPane();
		editeur.setEditable(false);
		editeur.setPreferredSize(new Dimension(400,150));
//		editeur.setBackground(GuiUtils.GRIS_238);

		JScrollPane barre_verticale = new JScrollPane(editeur);
		barre_verticale.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		barre_verticale.setMinimumSize(new Dimension(10, 10));

		URL message = this.getClass().getResource("/ihm/html/repertoire.html");

		try {
			editeur.setPage(message);
			}
		catch (IOException e)
			{
			editeur.setText("Le fichier 'repertoire.html' est introuvable.");
			}

//		jp0.add(barre_verticale);
		conteneur.add(barre_verticale);

		/*----- Panneau pour choisir le répertoire de travail -----*/
		JPanel jpDirWork = new JPanel();
		jpDirWork.setLayout(new BorderLayout(4,4));
		jpDirWork.setBorder(GuiUtils.BORDER_2);

		/*----- Chemin du 'Repertoire' de sortie -----*/
		this.txtDirWork = new JTextFieldEA("Choisir un répertoire..."); // A faire - Traduire

		jpDirWork.add(this.txtDirWork, BorderLayout.CENTER);

		/*----- Parcourir 'Repertoire' -----*/
		JButtonEA btParcourirRep = new JButtonEA(EPPLab.msg.getString("bt.parcourir"));
		btParcourirRep.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				/*----- Créer un explorateur -----*/
				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnVal = fc.showDialog(Repertoire.this, EPPLab.msg.getString("bt.ajouter"));

				if (returnVal == JFileChooser.APPROVE_OPTION)
					{
					File src = fc.getSelectedFile();

					/*----- On regarde si le répertoire est vide -----*/
					int choix = 0;
					if (src.listFiles().length != 0)
						{
						choix = JOptionPane.showConfirmDialog(Repertoire.this,
															  "Le répertoire choisi n'est pas vide.\nEtes-vous sûr de vouloir continuer ?", // A faire - Traduire
															  "Avertissement",
															  JOptionPane.YES_NO_OPTION,
															  JOptionPane.WARNING_MESSAGE);
						}

					if (choix == 0)
						{
						/*----- Mise à jour de l'affichage -----*/
						txtDirWork.setText(src.getAbsolutePath());

						/*----- On active le bouton 'Continuer' -----*/
						btContinue.setEnabled(true);
						}
					}
				}
			});

		jpDirWork.add(btParcourirRep, BorderLayout.EAST);

//		jp0.add(jpDirWork);
		conteneur.add(jpDirWork);

		/*----- Exemple de fichiers -----*/
		JPanel jp1 = new JPanel(GuiUtils.FL_LEFT_00);
		this.cbCopieExemple = new JCheckBoxEA("Fichiers exemples"); // A faire - traduire
		jp1.add(this.cbCopieExemple);

//		jp0.add(jp1);
		conteneur.add(jp1);

//		conteneur.add(jp0);

		/*----- Panneau de commandes -----*/
		JPanel jpCde = new JPanel();
		jpCde.setLayout(new BorderLayout());
		jpCde.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

		/*----- Bouton 'Retour' -----*/
		JButtonEA btRetour = new JButtonEA("Retour");
		btRetour.setEnabled(true);
		btRetour.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);
				frBienvenue.setVisible(true);
				}
			});
		jpCde.add(btRetour, BorderLayout.WEST);

		/*----- Bouton 'Continuer' -----*/
		this.btContinue= new JButtonEA(EPPLab.msg.getString("bt.continuer"));
		this.btContinue.setEnabled(false);
		this.btContinue.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);

				/*----- Mise à jour des répertoires de l'application -----*/
				parametre.updatePath(txtDirWork.getText());

				/*----- Copie des fichiers de données -----*/
				if (cbCopieExemple.isSelected())
					{
					try {
						FileUtils.copierFichier(this.getClass().getResourceAsStream("/ihm/data/don1.txt"), parametre.rep_donnees + File.separatorChar + "don1.txt");
						FileUtils.copierFichier(this.getClass().getResourceAsStream("/ihm/data/don3.txt"), parametre.rep_donnees + File.separatorChar + "don3.txt");
						FileUtils.copierFichier(this.getClass().getResourceAsStream("/ihm/data/lubischew.txt"), parametre.rep_donnees + File.separatorChar + "lubischew.txt");
						FileUtils.copierFichier(this.getClass().getResourceAsStream("/ihm/data/olive.txt"), parametre.rep_donnees + File.separatorChar + "olive.txt");
						}
					catch (IOException ex)
						{
						MsgUtils.erreur(Repertoire.this,
										"Problème lors de la copie des exemples dans le répertoire\n'" + parametre.rep_donnees + "'",
										"Copie des exemples");
						}
					}

				/*----- Affichage la fenêtre suivante -----*/
				new Ihm(getX(),getY(),evolution,parametre);
				}
			});
		jpCde.add(this.btContinue, BorderLayout.EAST);

		conteneur.add(jpCde);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();

		/*----- Rend la fenêtre visible -----*/
		this.setVisible(true);
		}

} /*----- Fin de la classe Repertoire -----*/
