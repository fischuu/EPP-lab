package ihm;

import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JComboBoxEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javalain.algorithmegenetique.ihm.composant.JRadioButtonEA;
import javalain.algorithmegenetique.ihm.composant.JTextFieldEA;
import javalain.algorithmegenetique.ihm.composant.TitledBorderEA;
import util.FileUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import epp.EvolutionPP;
import epp.EPPLab;
import epp.ParametrePP;
import util.GuiUtils;
import util.MsgUtils;


/*------------*/
/* Classe Ihm */
/*------------*/
/**
 * La classe <code>Ihm</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert, Ingrid Griffit
 * @version	5 juin 2010
 */
public class Ihm extends JFrame implements ItemListener
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Fenêtres liées.
	 */
	protected Recherche frRecherche;

	protected Analyse frAnalyse;


	/**
	 * Composants de l'interface.
	 */
	private TitledBorderEA		tbRecherche;

	private JLabelEAPres		lblMode;
	private JRadioButtonEA		rbExpert;
	private JRadioButtonEA		rbDebutant;
	private JRadioButtonEA		rbExploration;

	private JLabelEAPres		lblStructure;
	private ButtonGroup			groupStructure;
	private JRadioButtonEA		rbGroupes;
	private JRadioButtonEA		rbAtypiques;

	private JLabelEAPres		lblDonnees;
	private JComboBoxEA			cbListeDonnees;
	protected Vector<String>	l_fichiers_donnees;
	private JButtonEA			btParcourir;

	private TitledBorderEA		tbAnalyse;

	private JLabelEAPar			lblRepertoire;
	private JTextFieldEA		txtChemin;
	private JButtonEA			btParcourirRep;
	private JRadioButtonEA 		rbAnalyse;
	private JComboBoxEA			cbListeSortie;
	protected Vector<String>	l_fichiers_sortie;

	private JButtonEA 			btOk;
	private JComboBoxEA			cbLangue;


	/**
	 * Evolution.
	 */
	private EvolutionPP evolution;

	private ParametrePP parametre;


	/*--------------*/
	/* Constructeur */
	/*--------------*/
	
	@SuppressWarnings("LeakingThisInConstructor")
	public Ihm (int x, int y, EvolutionPP e, ParametrePP p)
		{
		/*----- Titre de la fenêtre -----*/
		this.setTitle(EPPLab.nom);

		/*----- Localisation de la fenêtre -----*/
		this.setLocation(x,y);
		this.setResizable(false);

		/*----- Listener sur la fenêtre -----*/
		this.addWindowListener(new WindowListener()
			{
			public void windowOpened (WindowEvent e) {}
			public void windowClosing (WindowEvent e)
				{
				/*----- Enregistrement des préférences de l'utilisateur -----*/
				EPPLab.savePrefs(parametre, Ihm.this.getX(), Ihm.this.getY());
				System.exit(0);
				}
			public void windowClosed (WindowEvent e) {}
			public void windowIconified (WindowEvent e) {}
			public void windowDeiconified (WindowEvent e) {}
			public void windowDeactivated (WindowEvent e) {}
			public void windowActivated (WindowEvent e) { cbListeDonnees.updateUI(); cbListeSortie.updateUI(); }
			});

		/*----- Lien avec l'évolution et les paramètres -----*/
		this.evolution = e;
		this.parametre = p;

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur,BoxLayout.Y_AXIS));

		/*----- Variables locales -----*/
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		ButtonGroup	groupMode = new ButtonGroup();

		int ligne  = 0;

		/**
		 * Panneau 'Recherche'.
		 */
		JPanel jpRecherche = new JPanel();
		jpRecherche.setLayout(gb);
		this.tbRecherche = new TitledBorderEA(EPPLab.msg.getString("tb.recherche"));
		jpRecherche.setBorder(this.tbRecherche);

		/*----- Zone d'espacement -----*/
		Component espace = Box.createHorizontalStrut(150);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(espace,gbc);
		jpRecherche.add(espace);

		/*----- Mode -----*/
		this.lblMode = new JLabelEAPres(EPPLab.msg.getString("label.mode"));
		gb.setConstraints(this.lblMode,gbc);
		jpRecherche.add(this.lblMode);
		ligne++;

		/*----- Mode 'Expert' -----*/
		this.rbExpert = new JRadioButtonEA(EPPLab.msg.getString("rb.expert"));
		this.rbExpert.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbExpert,gbc);
		jpRecherche.add(this.rbExpert);
		groupMode.add(this.rbExpert);
		ligne++;

		/*----- Mode 'Semi-automatique' -----*/
		this.rbDebutant = new JRadioButtonEA(EPPLab.msg.getString("rb.debutant"));
		this.rbDebutant.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbDebutant,gbc);
		jpRecherche.add(this.rbDebutant);
		groupMode.add(this.rbDebutant);
		ligne++;

		/*----- Mode 'Exploration' -----*/
		this.rbExploration = new JRadioButtonEA(EPPLab.msg.getString("rb.comparaison"));
		this.rbExploration.setEnabled(false);
		this.rbExploration.addItemListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbExploration,gbc);
		jpRecherche.add(this.rbExploration);
		groupMode.add(this.rbExploration);

		ligne = 0;

		/*----- Zone d'espacement -----*/
		espace = Box.createHorizontalStrut(150);
		gbc.gridx = 1; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(espace,gbc);
		jpRecherche.add(espace);

		/*----- Structure -----*/
		this.lblStructure = new JLabelEAPres(EPPLab.msg.getString("label.structure"));
		this.lblStructure.setEnabled(false);
		gb.setConstraints(this.lblStructure,gbc);
		jpRecherche.add(this.lblStructure);
		ligne++;

		this.groupStructure = new ButtonGroup();

		/*----- Groupes -----*/
		this.rbGroupes = new JRadioButtonEA(EPPLab.msg.getString("rb.groupes"));
		this.rbGroupes.setEnabled(false);
		this.rbGroupes.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbGroupes,gbc);
		jpRecherche.add(this.rbGroupes);
		this.groupStructure.add(this.rbGroupes);
		ligne++;

		/*----- Atypiques -----*/
		this.rbAtypiques = new JRadioButtonEA(EPPLab.msg.getString("rb.atypiques"));
		this.rbAtypiques.setEnabled(false);
		this.rbAtypiques.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbAtypiques,gbc);
		jpRecherche.add(this.rbAtypiques);
		this.groupStructure.add(this.rbAtypiques);

		ligne = 0;

		/*----- Zone d'espacement -----*/
		espace = Box.createHorizontalStrut(150);
		gbc.gridx = 2; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(espace,gbc);
		jpRecherche.add(espace);

		/*----- Fichier de données -----*/
		this.lblDonnees = new JLabelEAPres(EPPLab.msg.getString("label.donnees"));
		this.lblDonnees.setEnabled(false);
		gb.setConstraints(this.lblDonnees,gbc);
		jpRecherche.add(this.lblDonnees);
		ligne++;

		/*----- Liste des fichiers de données -----*/
		this.l_fichiers_donnees = FileUtils.listeFichiers(new File(this.parametre.rep_donnees));
		this.l_fichiers_donnees.add(0,EPPLab.msg.getString("cb.listeDonnees"));

		this.cbListeDonnees = new JComboBoxEA(this.l_fichiers_donnees);
		this.cbListeDonnees.setMaximumSize(new Dimension(210,23));
		this.cbListeDonnees.setPreferredSize(new Dimension(210,23));
		this.cbListeDonnees.setSelectedIndex(0);
		this.cbListeDonnees.setEnabled(false);
		this.cbListeDonnees.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				if (cbListeDonnees.getItemCount() != 0)
					{
					if (cbListeDonnees.getSelectedItem().equals(EPPLab.msg.getString("cb.listeDonnees")))
						{
						btOk.setEnabled(false);

						/*----- Paramètre de l'évolution -----*/
						parametre.ficherDeDonnees = null;
						}
					else
						{
						btOk.setEnabled(true);
						getRootPane().setDefaultButton(btOk);

						/*----- Paramètre de l'évolution -----*/
						parametre.ficherDeDonnees = (String)cbListeDonnees.getSelectedItem();
						}
					}
				else
					btOk.setEnabled(false);
				}
			});
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.cbListeDonnees,gbc);
		jpRecherche.add(this.cbListeDonnees);
		ligne++;

		/*----- Bouton d'ajout de fichiers de données -----*/
		this.btParcourir = new JButtonEA(EPPLab.msg.getString("bt.parcourir"));
		this.btParcourir.setEnabled(false);
		this.btParcourir.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				parcourirFichier();
				}
			});
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy = ligne; gbc.gridheight = 2; gbc.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gb.setConstraints(this.btParcourir,gbc);
		jpRecherche.add(this.btParcourir);

		/*----- Ajoute du panneau 'recherche' à la fenêtre -----*/
		conteneur.add(jpRecherche);

		/**
		 * Panneau 'Analyse'.
		 */
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();

		JPanel jpAnalyse = new JPanel();
		jpAnalyse.setLayout(gb);
		this.tbAnalyse = new TitledBorderEA(EPPLab.msg.getString("tb.analyse"));
		jpAnalyse.setBorder(this.tbAnalyse);

		/*----- Mode 'Analyse' -----*/
		this.rbAnalyse = new JRadioButtonEA(EPPLab.msg.getString("rb.analyse"));
		this.rbAnalyse.addItemListener(this);
		gbc.gridx = 0; gbc.gridy = 0; gbc.insets = GuiUtils.INSETS_0500; gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gb.setConstraints(this.rbAnalyse,gbc);
		jpAnalyse.add(this.rbAnalyse);
		groupMode.add(this.rbAnalyse);

		/*----- Liste des fichiers de sortie -----*/
		this.l_fichiers_sortie = FileUtils.listeFichiers(new File(this.parametre.rep_sortie));
		this.l_fichiers_sortie.add(0, EPPLab.msg.getString("cb.listeSortie"));

		this.cbListeSortie = new JComboBoxEA(this.l_fichiers_sortie);
		this.cbListeSortie.setMaximumSize(new Dimension(350,23));
		this.cbListeSortie.setPreferredSize(new Dimension(350,23));
		this.cbListeSortie.setSelectedIndex(0);
		this.cbListeSortie.setEnabled(false);
		this.cbListeSortie.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				if (cbListeSortie.getItemCount() != 0)
					{
					if (cbListeSortie.getSelectedItem().equals(EPPLab.msg.getString("cb.listeSortie")))
						btOk.setEnabled(false);
					else
						{
						btOk.setEnabled(true);
						getRootPane().setDefaultButton(btOk);
						}
					}
				else
					btOk.setEnabled(false);
				}
			});

		gbc.gridx = 1; gbc.gridy = 0; gbc.insets = GuiUtils.INSETS_0500; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
		gb.setConstraints(this.cbListeSortie,gbc);
		jpAnalyse.add(this.cbListeSortie);

		/*----- Ajoute du panneau 'Analyse' à la fenêtre -----*/
		conteneur.add(jpAnalyse);

		/**
		 * Panneau du répertoire de travail.
		 */
		JPanel jpDirWork = new JPanel();
		jpDirWork.setLayout(new BorderLayout(4, 4));
		jpDirWork.setBorder(BorderFactory.createEmptyBorder(2,4,0,2));

		/*----- 'Repertoire' -----*/
		this.lblRepertoire = new JLabelEAPar(EPPLab.msg.getString("label.repertoireResult"));
		jpDirWork.add(this.lblRepertoire, BorderLayout.WEST);

		/*----- Chemin du 'Repertoire' de sortie -----*/
		this.txtChemin = new JTextFieldEA(10);
		this.txtChemin.setText(this.parametre.rep_travail);
		jpDirWork.add(this.txtChemin, BorderLayout.CENTER);

		/*----- Parcourir 'Repertoire' -----*/
		this.btParcourirRep = new JButtonEA(EPPLab.msg.getString("bt.modifier"));
		this.btParcourirRep.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				parcourirRepertoire();
				}
			});
		jpDirWork.add(this.btParcourirRep, BorderLayout.EAST);

		/*----- Ajoute du panneau du répertoire de travail -----*/
		conteneur.add(jpDirWork);

		/**
		 * Panneau de commande.
		 */
		JPanel jpOk = new JPanel();
		jpOk.setLayout(new BorderLayout());
		jpOk.setBorder(BorderFactory.createEmptyBorder(4,2,2,2));

		/*----- Changement de langue -----*/
		String[] langue = {"fr", "en", "es"};
		this.cbLangue = new JComboBoxEA(langue);
		this.cbLangue.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				String cde = (String)cbLangue.getSelectedItem();

				Locale locale = new Locale("en", "US");
				if (cde.equals("fr")) locale = new Locale("fr", "FR");
				if (cde.equals("es")) locale = new Locale("es", "ES");

				EPPLab.msg = ResourceBundle.getBundle("ihm/MessagesBundle", locale);

				/*----- Modification des libellés de la fenêtre -----*/
				setTitle(EPPLab.msg.getString("frame.interface"));

				btParcourir.setText(EPPLab.msg.getString("bt.parcourir"));
				btParcourirRep.setText(EPPLab.msg.getString("bt.modifier"));
				btOk.setText(EPPLab.msg.getString("bt.ok"));

				lblDonnees.setText(EPPLab.msg.getString("label.donnees"));
				lblMode.setText(EPPLab.msg.getString("label.mode"));
				lblRepertoire.setText(EPPLab.msg.getString("label.repertoireResult"));
				lblStructure.setText(EPPLab.msg.getString("label.structure"));

				rbAnalyse.setText(EPPLab.msg.getString("rb.analyse"));
				rbAtypiques.setText(EPPLab.msg.getString("rb.atypiques"));
				rbExploration.setText(EPPLab.msg.getString("rb.comparaison"));
				rbDebutant.setText(EPPLab.msg.getString("rb.debutant"));
				rbExpert.setText(EPPLab.msg.getString("rb.expert"));
				rbGroupes.setText(EPPLab.msg.getString("rb.groupes"));

				tbAnalyse.setTitle(EPPLab.msg.getString("tb.analyse"));
				tbRecherche.setTitle(EPPLab.msg.getString("tb.recherche"));

				/*----- Réajuster la fenêtre -----*/
				repaint();
				pack();
				}
			});
		jpOk.add(this.cbLangue, BorderLayout.WEST);

		/*----- Bouton 'Valider' -----*/
		this.btOk= new JButtonEA(EPPLab.msg.getString("bt.ok"));
		this.btOk.setEnabled(false);
		this.btOk.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				cbLangue.setEnabled(false);

				if (rbGroupes.isSelected() || rbAtypiques.isSelected())
					{
					if (frRecherche == null)
						{
						frRecherche = new Recherche(Ihm.this.getX(),Ihm.this.getY(),evolution,parametre,Ihm.this);
						evolution.setFrRecherche(frRecherche);
						}

					frRecherche.initialise();
					frRecherche.setVisible(true);
					}
				else
					{
					if (frAnalyse == null)
						frAnalyse = new Analyse(Ihm.this.getX(),Ihm.this.getY(),(String)cbListeSortie.getSelectedItem(),Ihm.this);

					frAnalyse.setVisible(true);
					frAnalyse.initialise((String)cbListeSortie.getSelectedItem());
					}

				setVisible(false);
				}
			});
		jpOk.add(this.btOk, BorderLayout.EAST);

  		/*----- Ajoute du panneau de commande à la fenêtre -----*/
		conteneur.add(jpOk);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();

		/*----- Rend la fenêtre visible -----*/
		this.setVisible(true);

		/*----- Initialisation de l'interface -----*/
		this.rbExpert.setSelected(true);
		this.rbGroupes.setSelected(true);
//		this.rbAtypiques.setSelected(true);
//		this.cbListeDonnees.setSelectedItem("QN_BadPart300.txt");
		this.cbListeDonnees.setSelectedItem("olive.txt");
//		this.cbListeDonnees.setSelectedItem("lubischev.txt");
//		this.cbListeDonnees.setSelectedItem("fiab_880.txt");
//		this.cbListeDonnees.setSelectedItem("ruspini.txt");

//		this.rbAnalyse.setSelected(true);
//		this.cbListeSortie.setSelectedItem("simulation_100_F_GA_1_lubischev.txt");
//		this.cbListeSortie.setSelectedItem("simulation_5_F_GA_1_don3.txt");
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les paramètres de l'apllication.
	 */
	public ParametrePP getParametre () { return this.parametre; }


	/**
	 * Vérifie si l'application est en mode 'Expert'.
	 */
	public boolean isModeExpert () { return this.rbExpert.isSelected(); }

	/**
	 * Retourne le chemin du repertoire de sortie
	 */
	public String getRepertoire () { return this.txtChemin.getText(); }


	/**
	 * Vérifie si l'application est en mode 'Débutant'.
	 */
	public boolean isModeDebutant () { return this.rbDebutant.isSelected(); }


	/**
	 * Vérifie si l'application est en mode de recherche de groupes.
	 */
	public boolean isRechercheGroupe () { return this.rbGroupes.isSelected(); }


	/**
	 * Recherche un fichier en parcourant l'arborescence.
	 */
	private void parcourirFichier ()
		{
		/*----- Créer un explorateur -----*/
		JFileChooser fc = new JFileChooser();

		/*----- Ajoute un filtre pour ne visualiser que les fichiers 'txt' -----*/
		fc.addChoosableFileFilter(new FiltreTXT());
		
		int returnVal = fc.showDialog(this, EPPLab.msg.getString("bt.ajouter"));
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			File src = fc.getSelectedFile();
			File dest = new File(this.parametre.rep_donnees + File.separatorChar + src.getName());

			if (dest.exists())
				{
				Object[] options = { EPPLab.msg.getString("rb.oui"), EPPLab.msg.getString("bt.annuler")};
				int choix = JOptionPane.showOptionDialog(this,
														 EPPLab.msg.getString("showOptionDialog.ajoutDonnee") + "\n" + EPPLab.msg.getString("showOptionDialog.remplacer"),
														 EPPLab.msg.getString("showOptionDialog.attention"),
														 JOptionPane.OK_OPTION,
														 JOptionPane.QUESTION_MESSAGE,
														 null,
														 options,
														 options[0]);
				if (choix == 0)
					{
					/*----- On remplace -----*/
					try	{
						FileUtils.copierFichier(src, dest);
						}
					catch (IOException e)
						{
						MsgUtils.erreur(Ihm.this,
										EPPLab.msg.getString("showOptionDialog.erreurCopie") + src.getName(),
										EPPLab.msg.getString("showOptionDialog.erreur"));
						}

					this.cbListeDonnees.setSelectedItem(src.getName());
					}
				}
			else
				{
				try	{
					FileUtils.copierFichier(src, dest);
					}
				catch (IOException e)
					{
					MsgUtils.erreur(Ihm.this,
									EPPLab.msg.getString("showOptionDialog.erreurCopie") + src.getName(),
									EPPLab.msg.getString("showOptionDialog.erreur"));
					}

				this.cbListeDonnees.addItem(src.getName());
				this.cbListeDonnees.setSelectedItem(src.getName());
				}
			}
		}


	/**
	 * Recherche un répertoire en parcourant l'arborescence.
	 */
	private void parcourirRepertoire ()
		{
		/*----- Créer un explorateur -----*/
		JFileChooser fc = new JFileChooser(this.parametre.rep_travail);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fc.showDialog(this, EPPLab.msg.getString("bt.ajouter"));

		if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			File src = fc.getSelectedFile();

			/*----- Mise à jour des répertoires de l'application -----*/
			this.parametre.updatePath(src.getAbsolutePath());

			/*----- Mise à jour des listes de fichiers -----*/
			FileUtils.listeFichiers(new File(this.parametre.rep_donnees), this.l_fichiers_donnees);
			this.l_fichiers_donnees.add(0,EPPLab.msg.getString("cb.listeDonnees"));

			FileUtils.listeFichiers(new File(this.parametre.rep_sortie), this.l_fichiers_sortie);
			this.l_fichiers_sortie.add(0, EPPLab.msg.getString("cb.listeSortie"));

			/*----- Mise des menus déroulants -----*/
			this.cbListeDonnees.updateUI(); this.cbListeDonnees.setSelectedIndex(0);
			this.cbListeSortie.updateUI(); this.cbListeSortie.setSelectedIndex(0);

			this.txtChemin.setText(this.parametre.rep_travail);
			}
		}


	/*-------------------*/
	/* Méthodes définies */
	/*-------------------*/

	/**
	 * Définition de la méthode de ItemListener.
	 */
	public void itemStateChanged (ItemEvent e)
		{
		Object source = e.getItemSelectable();

		if ((source == this.rbDebutant || source == this.rbExpert || source == this.rbExploration) &&
			(this.rbDebutant.isSelected() || this.rbExpert.isSelected() || this.rbExploration.isSelected()))
			{
			this.rbGroupes.setEnabled(true);
			this.rbAtypiques.setEnabled(true);
			this.lblStructure.setEnabled(true);

			this.cbListeSortie.setSelectedItem(EPPLab.msg.getString("cb.listeSortie"));
			this.cbListeSortie.setEnabled(false);

			if ((this.rbGroupes.isSelected() || this.rbAtypiques.isSelected()) &&
				!cbListeDonnees.getSelectedItem().equals(EPPLab.msg.getString("cb.listeDonnees")))
					this.btOk.setEnabled(true);
			}
		else
		if (source == this.rbAnalyse && this.rbAnalyse.isSelected())
			{
			this.lblStructure.setEnabled(false);
			this.rbGroupes.setEnabled(false);
			this.rbAtypiques.setEnabled(false);
			this.groupStructure.clearSelection();
			this.lblDonnees.setEnabled(false);
			this.cbListeDonnees.setSelectedItem(EPPLab.msg.getString("cb.listeDonnees"));
			this.cbListeDonnees.setEnabled(false);
			this.btParcourir.setEnabled(false);

			this.cbListeSortie.setEnabled(true);

			this.btOk.setEnabled(false);
			}
		else
		if ((source == this.rbGroupes || source == this.rbAtypiques) &&
			(this.rbGroupes.isSelected() || this.rbAtypiques.isSelected()))
			{
			this.lblDonnees.setEnabled(true);
			this.cbListeDonnees.setEnabled(true);
			this.btParcourir.setEnabled(true);
			}
		}

} /*----- Fin de la classe Ihm -----*/
