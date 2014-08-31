package ihm;

import epp.OrdonnerPP;
import com.visutools.nav.bislider.BiSlider;
import ihm.controle.ButtonTabComponent;
import ihm.controle.JDialogFichier;
import ihm.graphique.CourbeAtypique;
import ihm.graphique.CourbeIndice;
import ihm.graphique.EstimateurNoyau;
import ihm.graphique.Histogramme;
import indice.Discriminant;
import indice.Friedman;
import indice.FriedmanTukey;
import indice.Indice;
import indice.Indice4;
import indice.KurtosisMax;
import indice.KurtosisMin;
import indice.StahelDonoho;
import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JComboBoxEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javalain.algorithmegenetique.ihm.composant.JRadioButtonEA;
import javalain.algorithmegenetique.ihm.composant.TitledBorderEA;
import util.FileUtils;
import util.StringUtils;
import javalain.math.Calcul;
import math.Matrice;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import epp.EPPLab;
import java.awt.Color;
import util.GuiUtils;
import util.MsgUtils;


/*----------------*/
/* Classe Analyse */
/*----------------*/
/**
 * La classe <code>Analyse</code> est la fenêtre d'analyse des résultats.
 *
 * @author	Alain Berro, Emilie Chabbert, Ingrid Griffit
 * @version	15 juillet 2010
 */
public class Analyse extends JFrame
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Fenêtre principale.
	 */
	private Ihm frIhm;


	/**
	 * Composants de l'interface.
	 */

	/*----- Panneau 'Ouest' contenant les panneaux 'Projections', 'Sigma', 'Boutons' et 'Informations' -----*/
	private JPanel				jpOuest;

	private JRadioButtonEA		rbHistogramme;
	private JRadioButtonEA		rbEstimateur;

	private Histogramme 		histogramme;
	private EstimateurNoyau		estimateurNoyau;

	private JLabelEAPres		lblAtypiques;
	private JLabelEAPar			lblSigma;
	private JSlider				sliderAtypiques;

	private JButtonEA			btSaveSelection;
	private JButtonEA			btSaveAtypique;
	private JButtonEA			btSaveGraphique;

	/*----- Panneau 'Projection' -----*/
	private JPanel				jpProjection;

	private JLabelEAPres		lblFichier;
	private JLabelEAPres		lblNomMethode;
	private JLabelEAPres		lblNomIndice;
	private JLabelEAPres		lblValeurI;
	private JLabelEAPres		lblNbObservations;
	private	JLabelEAPar			lblIndividus;
	private JLabelEAPres		lblNbIndividus;
	private JLabelEAPres		lblNbIterations;
	private JLabelEAPres		lblNbSimulations;

	private JComboBoxEA			cbListeSortie;
	private JButtonEA			btAnalyse;

	/*----- Panneau 'Est' contenant les onglets -----*/
	private JPanel				jpEst;
	private JTabbedPane			ongletCourbe;
	private JPanel				jpCourbeIndice;
	private JPanel				jpCourbeCosinus;
	private JPanel				jpCourbeAtypique;
	private GridBagLayout		gb2;
	private GridBagConstraints	gbc2;
	private GridBagLayout		gb1;
	private GridBagConstraints	gbc1;
	private GridBagLayout		gb3;
	private GridBagConstraints	gbc3;

	private CourbeIndice		courbeIndice;
	private CourbeIndice		courbeCosinus;
	private CourbeAtypique		courbeAtypiques;

	private JSlider				sliderNumLancement;
	private JSlider				sliderNumObservation;
	private BiSlider			biSlider;

	/*----- Panneau des fenêtres des projections référentes -----*/
	private JTabbedPane			tabPaneHistogramme;
	private JTabbedPane			tabPaneEstimateur;


	/**
	 * Données et méthodes.
 	 */

	/*----- Fichier de résultats à analyser -----*/
	private String nomFicherResult;

	/*----- Indice -----*/
	private String nomIndice;
	private Indice indice;

	/*----- Fichier de données (lubischev, olive...) et le tableau de données -----*/
	private String fichierDonnees;

	/*----- Matrice originelle -----*/
	private Matrice matrice;

	private int nbObservations;
	private int dimObservation;

	/*----- Type de transformation de données -----*/
	private String transformDonnees;

	/*----- Methode -----*/
	private String methode;

	/*----- Nombre de simulations -----*/
	private int nbSimulations;

	/*----- Nombre d'iterations -----*/
	private int nbIterations;

	/*----- Nombre d'individus -----*/
	private int nbIndividus;

	/*----- Tableau des données projetées à visualiser et ecart-type de ces données -----*/
	private double[] donnees_x;
	private double ecart_type;
	private double moyenne;


	/**
	 * Variables.
	 */

	/*-----  Liste pour récupérer les I et les vecteurs associés dans le fichier résultat des simulations -----*/
	private ArrayList<OrdonnerPP> liste = new ArrayList<OrdonnerPP>(100);

	/*----- Numéro du lancement de référence pour calculer le cosinus -----*/
	private int numLancementRef;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Analyse (int x, int y, String fich, Ihm f)
		{
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
				EPPLab.savePrefs(frIhm.getParametre(), Analyse.this.getX(), Analyse.this.getY());
				System.exit(0);
				}
			public void windowClosed (WindowEvent e) {}
			public void windowIconified (WindowEvent e) {}
			public void windowDeiconified (WindowEvent e) {}
			public void windowDeactivated (WindowEvent e) {}
			public void windowActivated (WindowEvent e) { cbListeSortie.updateUI(); }
			});

		/*----- Lien vers la fenêtre principale -----*/
		this.frIhm = f;

		/*----- Fichier de résultats à analyser -----*/
		this.nomFicherResult = fich;


		/**
		 * Graphiques et courbes.
		 */
		/*----- Histogramme  -----*/
		this.histogramme = new Histogramme(6,3,true,true,this);

		/*----- Estimateur à noyau -----*/
		this.estimateurNoyau = new EstimateurNoyau(6,3,true,true,this);

		/*----- Courbe de variation de l'indice -----*/
		this.courbeCosinus = new CourbeIndice(400,200);

		/*----- Courbe de variation de l'indice -----*/
		this.courbeIndice = new CourbeIndice(400,200);

		/*----- Courbe de variation de l'indice -----*/
		this.courbeAtypiques = new CourbeAtypique(400,200);


		/**
		 * Interface.
		 */

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur, BoxLayout.X_AXIS));

		/*----- Variables locales -----*/
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();


		/**
		 * Panneau 'Ouest'.
		 */
		this.jpOuest = new JPanel();
		this.jpOuest.setLayout(new BoxLayout(this.jpOuest, BoxLayout.Y_AXIS));


		/**
		 * Panneau de commandes.
		 */
		JPanel jpCde = new JPanel();
		jpCde.setLayout(gb);

		/*----- Zone d'espacement -----*/
		Component espace = Box.createHorizontalStrut(150);
		gbc.gridx = 0; gbc.gridy = 0; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(espace,gbc);
		jpCde.add(espace);

		/*----- Choix du graphique pour la visualisation -----*/
		JLabelEAPres lblGraph = new JLabelEAPres(EPPLab.msg.getString("label.graphique"));
		gb.setConstraints(lblGraph,gbc);
		jpCde.add(lblGraph);

		ButtonGroup groupGraph = new ButtonGroup();

		/*----- Histogramme -----*/
		this.rbHistogramme = new JRadioButtonEA(EPPLab.msg.getString("label.histogramme"));
		this.rbHistogramme.setSelected(false);
		this.rbHistogramme.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				if (rbHistogramme.isSelected())
					{
					/*----- Modifie le graphique de la projection -----*/
					jpProjection.remove(estimateurNoyau);
					jpProjection.add(histogramme, 0);

					/*----- Mise à jour de la sélection éventuelle -----*/
					histogramme.setSelection(estimateurNoyau.isSelection(), estimateurNoyau.getBorneInfSelect(), estimateurNoyau.getBorneSupSelect());

					/*
					 * Synchronise les deux panneaux d'onglets.
					 * Supprime des onglets 'histogramme' les onglets 'estimateur' supprimés.
					 */
					if (tabPaneEstimateur.getTabCount() != tabPaneHistogramme.getTabCount())
						{
						for (int i=0; i<tabPaneEstimateur.getTabCount(); i++)
							{
							while (!tabPaneEstimateur.getTitleAt(i).equals(tabPaneHistogramme.getTitleAt(i)))
								tabPaneHistogramme.remove(i);
							}

						while (tabPaneEstimateur.getTabCount() < tabPaneHistogramme.getTabCount())
							tabPaneHistogramme.remove(tabPaneEstimateur.getTabCount());
						}

					/*----- Supprime les onglets 'histogramme' et ajoute les onglets 'estimateur' -----*/
					jpEst.remove(tabPaneEstimateur);
					jpEst.add(tabPaneHistogramme);

					/*----- Sélectionne le même onglet -----*/
					tabPaneHistogramme.setSelectedIndex(tabPaneEstimateur.getSelectedIndex());

					repaint();
					pack();
					}
				}
			});
		gbc.gridy = 1; gbc.insets = GuiUtils.INSETS_5A00; gbc.gridwidth = 2;
		gb.setConstraints(this.rbHistogramme,gbc);
		jpCde.add(this.rbHistogramme);

		groupGraph.add(this.rbHistogramme);

		/*----- Estimateur à noyau -----*/
		this.rbEstimateur = new JRadioButtonEA(EPPLab.msg.getString("label.estimateur"));
		this.rbEstimateur.setSelected(true);
		this.rbEstimateur.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				if (rbEstimateur.isSelected())
					{
					/*----- Modifie le graphique de la projection -----*/
					jpProjection.remove(histogramme);
					jpProjection.add(estimateurNoyau, 0);

					/*----- Mise à jour de la sélection éventuelle -----*/
					estimateurNoyau.setSelection(histogramme.isSelection(), histogramme.getBorneInfSelect(), histogramme.getBorneSupSelect());

					/*
					 * Synchronise les deux panneaux d'onglets.
					 * Supprime des onglets 'estimateur' les onglets 'histogramme' supprimés.
					 */
					if (tabPaneHistogramme.getTabCount() != tabPaneEstimateur.getTabCount())
						{
						for (int i=0; i<tabPaneHistogramme.getTabCount(); i++)
							{
							while (!tabPaneHistogramme.getTitleAt(i).equals(tabPaneEstimateur.getTitleAt(i)))
								tabPaneEstimateur.remove(i);
							}

						while (tabPaneHistogramme.getTabCount() < tabPaneEstimateur.getTabCount())
							tabPaneEstimateur.remove(tabPaneHistogramme.getTabCount());
						}

					/*----- Supprime les onglet 'histogramme' et ajoute les onglets 'estimateur' -----*/
					jpEst.remove(tabPaneHistogramme);
					jpEst.add(tabPaneEstimateur);

					/*----- Sélectionne le même onglet -----*/
					tabPaneEstimateur.setSelectedIndex(tabPaneHistogramme.getSelectedIndex());

					repaint();
					pack();
					}
				}
			});
		gbc.gridy = 2; gbc.insets = GuiUtils.INSETS_0A00;
		gb.setConstraints(this.rbEstimateur,gbc);
		jpCde.add(this.rbEstimateur);

		groupGraph.add(this.rbEstimateur);

		/*----- Slider atypique -----*/
		this.sliderAtypiques=new JSlider(JSlider.HORIZONTAL,1,10,3);
		this.sliderAtypiques.setMajorTickSpacing(1);
		this.sliderAtypiques.setPaintLabels(true);
		this.sliderAtypiques.setFont(GuiUtils.FONT_PARAMETRE);
		this.sliderAtypiques.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				if (!((JSlider)e.getSource()).getValueIsAdjusting())
					{
					long start = System.currentTimeMillis();

					/*----- Valeur sur le slider -----*/
					int i = sliderAtypiques.getValue();

					lblAtypiques.setText(EPPLab.msg.getString("label.sigma") + " " + i + "σ");

					/*----- Dessine les atypiques sur le graphique -----*/
					histogramme.setValeurAtypiques(i);
					histogramme.repaint();

					estimateurNoyau.setValeurAtypiques(i);
					estimateurNoyau.repaint();

					courbeAtypiques.setValeurAtypiques(i);
					courbeAtypiques.repaint();

					long stop = System.currentTimeMillis();
					System.out.println("Temps : " + (stop-start));
					}
				else
					System.out.println("sliderAtypiques en cours de modification");
				}
			});

		/*----- Atypiques -----*/
		this.lblAtypiques = new JLabelEAPres(EPPLab.msg.getString("label.sigma") + " " + sliderAtypiques.getValue() + "σ");
		gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblAtypiques,gbc);
		jpCde.add(this.lblAtypiques);

		this.lblSigma = new JLabelEAPar("σ = ");
		gbc.gridy = 1; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.lblSigma,gbc);
		jpCde.add(this.lblSigma);

		gbc.gridy = 2; gbc.gridheight = 2; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.sliderAtypiques,gbc);
		jpCde.add(this.sliderAtypiques);

		/*----- Panneau des boutons -----*/
		JPanel jpBoutons = new JPanel(new GridLayout(4,1,0,2));

		/*----- Bouton 'Sauvegarde sélection souris' -----*/
		this.btSaveSelection = new JButtonEA(EPPLab.msg.getString("bt.sauvegardeSelection"));
		this.btSaveSelection.setEnabled(false);
		this.btSaveSelection.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				int cpt = 0;
				double x0 = 0;
				double x1 = 0;
				StringBuilder sb = new StringBuilder();

				/*----- Intervalle sélectionné -----*/
				if (rbHistogramme.isSelected())
					{
					x0 = Calcul.min(histogramme.getBorneInfSelect(),histogramme.getBorneSupSelect());
					x1 = Calcul.max(histogramme.getBorneInfSelect(),histogramme.getBorneSupSelect());
					}
				else
				if (rbEstimateur.isSelected())
					{
					x0 = Calcul.min(estimateurNoyau.getBorneInfSelect(),estimateurNoyau.getBorneSupSelect());
					x1 = Calcul.max(estimateurNoyau.getBorneInfSelect(),estimateurNoyau.getBorneSupSelect());
					}

				/*----- Crée la chaîne de caractères à sauvegarder -----*/
				for (int i=0; i<donnees_x.length; i++)
					if (donnees_x[i] >= x0 && donnees_x[i] <= x1)
						{
						for (int j=0; j<dimObservation-1; j++)
							sb.append(matrice.element(i,j)).append(" ");

						sb.append(matrice.element(i,dimObservation-1)).append("\n");
						cpt++;
						}

				/*----- Ajout des dimensions des données -----*/
				sb = new StringBuilder().append(cpt).append(" ").append(dimObservation).append("\n\n").append(sb);

				/*----- On enregistre s'il y a eu des éléments sélectionnés -----*/
				if (cpt != 0)
					{
					/*----- Enregistrement du fichier -----*/
					String nom_fich = fichierDonnees.substring(0, fichierDonnees.length()-4) + "[" + GuiUtils.DECIMAL_2.format(x0).replace(",", ".") + "," + GuiUtils.DECIMAL_2.format(x1).replace(",", ".") + "].txt";

					JDialogFichier dlg = new JDialogFichier(Analyse.this, frIhm.getParametre().rep_donnees, nom_fich, "Nom du fichier 'sélection'"); // A faire - Traduire
					nom_fich = dlg.afficher();

					/*----- Enregistrement du fichier -----*/
					if (!"".equals(nom_fich))
						{
						/*----- On enregistre -----*/
						FileUtils.saveStringToFich(frIhm.getParametre().rep_donnees, nom_fich, sb);

						/*----- On ajoute le fichier à la liste des fichiers de données -----*/
						frIhm.l_fichiers_donnees.add(nom_fich);
						}
					}
				}
			});
		jpBoutons.add(this.btSaveSelection);

		/*----- Bouton 'Sauvegarde les atypiques' -----*/
		this.btSaveAtypique = new JButtonEA(EPPLab.msg.getString("bt.sauvegardeAtypiques"));
		this.btSaveAtypique.setEnabled(true);
		this.btSaveAtypique.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				StringBuilder sb = new StringBuilder();

				/*----- Compte le nombre d'atypiques et crée la chaîne de caractères -----*/
				int cpt = 0;
				double d = ecart_type * sliderAtypiques.getValue();
				System.out.println("==");
				for (int i=0; i<donnees_x.length; i++)
					if (donnees_x[i] > (moyenne + d) ||
						donnees_x[i] < (moyenne - d))
						{
						System.out.println("i : " + i);
						for (int j=0; j<dimObservation-1; j++)
							sb.append(matrice.element(i,j)).append(" ");

						sb.append(matrice.element(i,dimObservation-1)).append("\n");

						cpt++;
						}

				/*----- Ajout des dimensions des données -----*/
				sb = new StringBuilder().append(cpt).append(" ").append(dimObservation).append("\n\n").append(sb);

				/*----- On enregistre s'il y a eu des atypiques -----*/
				if (cpt != 0)
					{
					/*----- Enregistrement du fichier 'atypique' -----*/
					String nom_fich = fichierDonnees.substring(0, fichierDonnees.length()-4) + "_outliers-" + sliderAtypiques.getValue() + "s.txt";

					JDialogFichier dlg = new JDialogFichier(Analyse.this, frIhm.getParametre().rep_donnees, nom_fich, "Nom du fichier 'atypique'"); // A faire - Traduire
					nom_fich = dlg.afficher();

					/*----- Enregistrement du fichier -----*/
					if (!"".equals(nom_fich))
						{
						/*----- On enregistre -----*/
						FileUtils.saveStringToFich(frIhm.getParametre().rep_donnees, nom_fich, sb);

						/*----- On ajoute le fichier à la liste des fichiers de données -----*/
						frIhm.l_fichiers_donnees.add(nom_fich);
						}
					}
				}
			});
		jpBoutons.add(this.btSaveAtypique);

		/*----- Bouton de sauvegarde du graphique -----*/
		this.btSaveGraphique =new JButtonEA(EPPLab.msg.getString("bt.sauvegardeGraphique"));
		this.btSaveGraphique.setEnabled(true);
		this.btSaveGraphique.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				String s = "";
				if (rbHistogramme.isSelected())	s = "_histogramme_"; else
				if (rbEstimateur.isSelected())	s = "_estimateur_";

				/*----- Crée un nom de fichier existant -----*/
				String nom_fich = FileUtils.creeNomFichierJpg(frIhm.getParametre().rep_graph, fichierDonnees.substring(0, fichierDonnees.length()-4) + s);

				/*----- Enregistrement du fichier image -----*/
				JDialogFichier dlg = new JDialogFichier(Analyse.this, frIhm.getParametre().rep_graph, nom_fich, "Nom du fichier 'image'"); // A faire - Traduire
				nom_fich = dlg.afficher();

				/*----- Enregistrement du fichier -----*/
				try {
					if (!"".equals(nom_fich))
						{
						if (rbHistogramme.isSelected())
							FileUtils.saveJPanelToFile(histogramme, new File(frIhm.getParametre().rep_graph + File.separatorChar + nom_fich));
						else
						if (rbEstimateur.isSelected())
							FileUtils.saveJPanelToFile(estimateurNoyau, new File(frIhm.getParametre().rep_graph + File.separatorChar + nom_fich));
						}
					}
				catch (IOException ioe)
					{
					MsgUtils.erreur(Analyse.this,
									EPPLab.msg.getString("showOptionDialog.erreurCopie") + nom_fich,
									EPPLab.msg.getString("showOptionDialog.erreur"));
					}
				}
			});
		jpBoutons.add(this.btSaveGraphique);

		/*----- Slider 'Numéro de l'observation' -----*/
		this.sliderNumObservation = new JSlider(JSlider.HORIZONTAL);
		this.sliderNumObservation.setOpaque(true);
		this.sliderNumObservation.setPaintTrack(true);
		this.sliderNumObservation.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				if (!((JSlider)e.getSource()).getValueIsAdjusting())
					{
					long start = System.currentTimeMillis();

					/*----- Modifie l'indice de l'observation sélectionnée sur la courbe 'Atypiques' -----*/
					courbeAtypiques.setIndiceObsSelect(sliderNumObservation.getValue()-1);

					if (ongletCourbe.getSelectedIndex() == 2) courbeAtypiques.repaint();

					long stop = System.currentTimeMillis();
					System.out.println("Temps : " + (stop-start));
					}
				else
					System.out.println("sliderNumObservation en cours de modification");
				}
			});
		jpBoutons.add(this.sliderNumObservation);

		gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 3; gbc.insets = GuiUtils.INSETS_5000; gbc.anchor = GridBagConstraints.FIRST_LINE_END; gbc.weightx = 1.0;
		gb.setConstraints(jpBoutons,gbc);
		jpCde.add(jpBoutons);

		/*----- Bouton 'Aide' -----*/
		JButtonEA btAide = new JButtonEA(new ImageIcon(this.getClass().getResource("icone/help.gif")));
		btAide.setToolTipText(EPPLab.msg.getString("menu.aide"));
		btAide.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				System.out.println(EPPLab.msg.getString("menu.aide"));
				}
			});

		gbc.gridx = 0; gbc.gridy = 3; gbc.gridheight = 1; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.FIRST_LINE_START; gbc.weightx = 0.0; gbc.weighty = 1.0;
		gb.setConstraints(btAide,gbc);
		jpCde.add(btAide);

		JPanel jpNav = new JPanel();
		jpNav.setLayout(new BorderLayout(5,0));

		/*----- Bouton 'Retour' -----*/
		JButtonEA btRetour = new JButtonEA(EPPLab.msg.getString("menu.retour"));
		btRetour.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);

				if (frIhm.frRecherche != null)
					frIhm.frRecherche.setVisible(true);
				else
					frIhm.setVisible(true);
				}
			});
		jpNav.add(btRetour, BorderLayout.WEST);

		/*----- Liste des fichiers de sortie -----*/
		this.cbListeSortie = new JComboBoxEA(this.frIhm.l_fichiers_sortie);
		this.cbListeSortie.setMaximumSize(new Dimension(350,23));
		this.cbListeSortie.setPreferredSize(new Dimension(350,23));
		this.cbListeSortie.setSelectedIndex(0);
		this.cbListeSortie.setEnabled(true);
		this.cbListeSortie.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				if (cbListeSortie.getSelectedItem().equals(EPPLab.msg.getString("cb.listeSortie")) ||
					cbListeSortie.getSelectedItem().equals(nomFicherResult))
					btAnalyse.setEnabled(false);
				else
					btAnalyse.setEnabled(true);
				}
			});
		jpNav.add(this.cbListeSortie, BorderLayout.CENTER);

		/*----- Bouton 'Analyse' -----*/
		this.btAnalyse = new JButtonEA(EPPLab.msg.getString("bt.analyse"));
		this.btAnalyse.setEnabled(false);
		this.btAnalyse.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				initialise((String)cbListeSortie.getSelectedItem());
				}
			});
		jpNav.add(this.btAnalyse, BorderLayout.EAST);

		gbc.gridx = 2; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5000; gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gb.setConstraints(jpNav,gbc);
		jpCde.add(jpNav);

		this.jpOuest.add(jpCde);
		this.jpOuest.add(Box.createVerticalStrut(19));


		/**
		 * Panneau d'informations.
		 */
		JPanel jpInfo = new JPanel();
		jpInfo.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.info")));

		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jpInfo.setLayout(gb);

		/*----- Fichier résultat -----*/
		JLabelEAPar jl = new JLabelEAPar(EPPLab.msg.getString("label.Fdonnees"));
		gbc.gridx = 0; gbc.gridy = 0; gbc.insets = GuiUtils.INSETS_5A00; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblFichier = new JLabelEAPres(this.fichierDonnees);
		gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new Insets(5,20,0,0);
		gb.setConstraints(this.lblFichier,gbc);
		jpInfo.add(this.lblFichier);

		/*----- Nombre d'observations -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.nbObs"));
		gbc.gridx = 2; gbc.gridy = 0;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblNbObservations = new JLabelEAPres("n=" + this.nbObservations + " p=" + this.dimObservation);
		gbc.gridx = 3; gbc.gridy = 0;
		gb.setConstraints(this.lblNbObservations,gbc);
		jpInfo.add(this.lblNbObservations);

		/*----- Méthode -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.methode"));
		gbc.gridx = 0; gbc.gridy = 1; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblNomMethode = new JLabelEAPres(this.methode);
		gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(5,20,0,0);
		gb.setConstraints(this.lblNomMethode,gbc);
		jpInfo.add(this.lblNomMethode);

		/*----- Nombre d'individus -----*/
		this.lblIndividus = new JLabelEAPar("");
		gbc.gridx = 2; gbc.gridy = 1;
		gb.setConstraints(this.lblIndividus,gbc);
		jpInfo.add(this.lblIndividus);

		this.lblNbIndividus = new JLabelEAPres("" + this.nbIndividus);
		gbc.gridx = 3; gbc.gridy = 1;
		gb.setConstraints(this.lblNbIndividus,gbc);
		jpInfo.add(this.lblNbIndividus);

		/*----- Indice -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.indice"));
		gbc.gridx = 0; gbc.gridy = 2; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblNomIndice = new JLabelEAPres(this.nomIndice);
		gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(5,20,0,0);
		gb.setConstraints(this.lblNomIndice,gbc);
		jpInfo.add(this.lblNomIndice);

		/*----- Nombre d'itérations -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.iteration"));
		gbc.gridx = 2; gbc.gridy = 2;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblNbIterations = new JLabelEAPres("" + this.nbIterations);
		gbc.gridx = 3; gbc.gridy = 2;
		gb.setConstraints(this.lblNbIterations,gbc);
		jpInfo.add(this.lblNbIterations);

		/*----- Valeur de l'indice -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.valeurIndice"));
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblValeurI = new JLabelEAPres("");
		gbc.gridx = 1; gbc.gridy = 3; gbc.weighty = 1.0; gbc.insets = new Insets(5,20,10,0);
		gb.setConstraints(this.lblValeurI,gbc);
		jpInfo.add(this.lblValeurI);

		/*----- Nombre de simulations -----*/
		jl = new JLabelEAPar(EPPLab.msg.getString("label.simulation"));
		gbc.gridx = 2; gbc.gridy = 3;
		gb.setConstraints(jl,gbc);
		jpInfo.add(jl);

		this.lblNbSimulations = new JLabelEAPres("" + this.nbSimulations);
		gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 1.0;
		gb.setConstraints(this.lblNbSimulations,gbc);
		jpInfo.add(this.lblNbSimulations);

		this.jpOuest.add(jpInfo);


		/**
		 * Panneau de visualisation de la projection.
		 */
		this.jpProjection = new JPanel(GuiUtils.FL_LEFT_00);
		this.jpProjection.add(this.estimateurNoyau);

		/*----- Bouton 'Ajout onglet' -----*/
		JButtonEA btAjoutOnglet = new JButtonEA(new ImageIcon(this.getClass().getResource("icone/forward.gif")));
		btAjoutOnglet.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				/*----- Numéro du lancement à ajouter -----*/
				int num = sliderNumLancement.getValue();

				/*----- On regarde si ce lancement n'a pas déjà été sélectionné -----*/
				boolean ajoute = true;

				if (num == 1)
					ajoute = false;
				else
					{
					if (rbHistogramme.isSelected())
						{
						for (int i=1; i<tabPaneHistogramme.getTabCount(); i++)
							{
							String select = tabPaneHistogramme.getTitleAt(i);
							select = select.replace("n° ", "");

							if (num == Integer.parseInt(select))
								ajoute = false;
							}
						}
					else
						{
						for (int i=1; i<tabPaneEstimateur.getTabCount(); i++)
							{
							String select = tabPaneEstimateur.getTitleAt(i);
							select = select.replace("n° ", "");

							if (num == Integer.parseInt(select))
								ajoute = false;
							}
						}
					}

				if (ajoute)
					{
					/*----- Onglets 'histogramme' -----*/
					Histogramme histo = new Histogramme(4,2,false,false,Analyse.this);
					histo.setFichierDonnees(fichierDonnees);
					histo.setProjection(donnees_x);

					tabPaneHistogramme.add("n° " + num, histo);
					tabPaneHistogramme.setTabComponentAt(tabPaneHistogramme.getTabCount()-1, new ButtonTabComponent(tabPaneHistogramme));

					/*----- Onglets 'estimateur' -----*/
					EstimateurNoyau estim = new EstimateurNoyau(4,2,false,false,null);
					estim.setProjection(donnees_x);

					tabPaneEstimateur.add("n° " + num, estim);
					tabPaneEstimateur.setTabComponentAt(tabPaneEstimateur.getTabCount()-1, new ButtonTabComponent(tabPaneEstimateur));

					pack();
					}
				}
			});

		/*----- Bouton '..........' -----*/ // A faire - Compléter l'évènement sur le bouton
		JButtonEA btRef = new JButtonEA(new ImageIcon(this.getClass().getResource("icone/back.gif")));
		btRef.setEnabled(false);
		btRef.setVisible(false);
		btRef.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				System.out.println("Référence");
				}
			});

		JPanel jp0 = new JPanel();
		jp0.setLayout(new BoxLayout(jp0, BoxLayout.Y_AXIS));

		jp0.add(btAjoutOnglet);
		jp0.add(Box.createVerticalStrut(3));
		jp0.add(btRef);

		this.jpProjection.add(jp0);

		this.jpOuest.add(this.jpProjection);

		conteneur.add(jpOuest, BorderLayout.WEST);
		conteneur.add(Box.createHorizontalStrut(5));


		/**
		 * Panneau 'Est'.
		 */
		this.jpEst = new JPanel();
		this.jpEst.setLayout(new BoxLayout(this.jpEst,BoxLayout.Y_AXIS));

		/*----- Slider 'Numéro de lancement' -----*/
		final Insets marge_slider = new Insets(0, this.courbeIndice.getMargeGauche()-7, 0, this.courbeIndice.getMargeDroite()-7);

		this.sliderNumLancement = new JSlider(JSlider.HORIZONTAL);
		this.sliderNumLancement.setOpaque(false);
		this.sliderNumLancement.setPaintTrack(true);
		this.sliderNumLancement.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				if (!((JSlider)e.getSource()).getValueIsAdjusting())
					{
					long start = System.currentTimeMillis();

					int i = sliderNumLancement.getValue()-1;

					/*----- Modifie l'indice du lancement sélectionné sur les courbes 'Indice', 'Cosinus' et 'Atypiques' -----*/
					courbeIndice.setIndiceRunSelect(i);
					courbeCosinus.setIndiceRunSelect(i);
					courbeAtypiques.setIndiceRunSelect(i);

					if (ongletCourbe.getSelectedIndex() == 0) courbeIndice.repaint();
					if (ongletCourbe.getSelectedIndex() == 1) courbeCosinus.repaint();
					if (ongletCourbe.getSelectedIndex() == 2) courbeAtypiques.repaint();

					/*----- Modifie la valeur du I -----*/
					lblValeurI.setText(GuiUtils.DECIMAL_9.format(liste.get(i).getI()));

					/*----- Modifie le tracé de l'histogramme ou de l'estimateur à noyau -----*/
					for (int j=0; j<indice.getMatrice().nbLigne(); j++)
						donnees_x[j] = Calcul.produitScalaireVecteur(indice.getMatrice().ligne(j), liste.get(i).getA());

					ecart_type = Calcul.ecartType(donnees_x);

					histogramme.setProjection(donnees_x);
					estimateurNoyau.setProjection(donnees_x);

					if (rbHistogramme.isSelected()) histogramme.repaint();
					if (rbEstimateur.isSelected()) estimateurNoyau.repaint();

					long stop = System.currentTimeMillis();
					System.out.println("Temps : " + (stop-start));
					}
				else
					System.out.println("sliderNumLancement en cours de modification");
				}
			});

		/*----- Panneau 'Atypiques' -----*/
		this.gb3 = new GridBagLayout();
		this.gbc3 = new GridBagConstraints();

		this.jpCourbeAtypique = new JPanel(this.gb3);
//		this.jpCourbeAtypique.setBackground(GuiUtils.VERT_LEGER);
		this.jpCourbeAtypique.setBackground(GuiUtils.blanc);

		this.gbc3.gridx = 0; this.gbc3.gridy = 0; this.gbc3.anchor = GridBagConstraints.FIRST_LINE_START;
		this.gb3.setConstraints(this.courbeAtypiques,this.gbc3);
		this.jpCourbeAtypique.add(this.courbeAtypiques);

		this.gbc3.gridx = 0; this.gbc3.gridy = 1; gbc3.insets = marge_slider; this.gbc3.fill = GridBagConstraints.HORIZONTAL;
		this.gb3.setConstraints(this.sliderNumLancement,this.gbc3);
		this.jpCourbeAtypique.add(this.sliderNumLancement);

		JLabelEAPres lbl_axe2 = new JLabelEAPres(EPPLab.msg.getString("label.axeLancements"));
		lbl_axe2.setHorizontalAlignment(JLabelEAPres.CENTER);
		this.gbc3.gridx = 0; this.gbc3.gridy = 2; this.gbc3.insets = new Insets(0,0,5,0);
		this.gb3.setConstraints(lbl_axe2,this.gbc3);
		this.jpCourbeAtypique.add(lbl_axe2);

		/*----- Panneau 'Cosinus' -----*/
		this.gb2 = new GridBagLayout();
		this.gbc2 = new GridBagConstraints();

		this.jpCourbeCosinus = new JPanel(this.gb2);
//		this.jpCourbeCosinus.setBackground(GuiUtils.VERT_LEGER);
		this.jpCourbeCosinus.setBackground(GuiUtils.blanc);

		this.gbc2.gridx = 0; this.gbc2.gridy = 0; this.gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
		this.gb2.setConstraints(this.courbeCosinus,this.gbc2);
		this.jpCourbeCosinus.add(this.courbeCosinus);

		this.gbc2.gridx = 0; this.gbc2.gridy = 1; this.gbc2.insets = marge_slider; this.gbc2.fill = GridBagConstraints.HORIZONTAL;
		this.gb2.setConstraints(this.sliderNumLancement,this.gbc2);
		this.jpCourbeCosinus.add(this.sliderNumLancement);

		JLabelEAPres lbl_axe = new JLabelEAPres(EPPLab.msg.getString("label.axeLancements"));
		lbl_axe.setHorizontalAlignment(JLabelEAPres.CENTER);
		this.gbc2.gridx = 0; this.gbc2.gridy = 2; this.gbc2.insets = new Insets(0,0,5,0);
		this.gb2.setConstraints(lbl_axe,this.gbc2);
		this.jpCourbeCosinus.add(lbl_axe);

		/*----- Panneau 'Indice' -----*/
		this.gb1 = new GridBagLayout();
		this.gbc1 = new GridBagConstraints();

		this.jpCourbeIndice = new JPanel(this.gb1);
//		this.jpCourbeIndice.setBackground(GuiUtils.VERT_LEGER);
		this.jpCourbeIndice.setBackground(Color.WHITE);

		this.gbc1.gridx = 0; this.gbc1.gridy = 0; this.gbc1.anchor = GridBagConstraints.FIRST_LINE_START;
		this.gb1.setConstraints(this.courbeIndice,this.gbc1);
		this.jpCourbeIndice.add(this.courbeIndice);

		this.gbc1.gridx = 0; this.gbc1.gridy = 1; gbc1.insets = marge_slider; this.gbc1.fill = GridBagConstraints.HORIZONTAL;
		this.gb1.setConstraints(this.sliderNumLancement,this.gbc1);
		this.jpCourbeIndice.add(this.sliderNumLancement);

		JLabelEAPres lbl_axe1 = new JLabelEAPres(EPPLab.msg.getString("label.axeLancements"));
		lbl_axe1.setHorizontalAlignment(JLabelEAPres.CENTER);
		this.gbc1.gridx = 0; this.gbc1.gridy = 2; this.gbc1.insets = new Insets(0,0,5,0);
		this.gb1.setConstraints(lbl_axe1,this.gbc1);
		this.jpCourbeIndice.add(lbl_axe1);

		/*----- Création des onglets -----*/
		this.ongletCourbe = new JTabbedPane();
		this.ongletCourbe.addTab(EPPLab.msg.getString("tab.indice"), this.jpCourbeIndice);
		this.ongletCourbe.addTab(EPPLab.msg.getString("tab.cosinus"), this.jpCourbeCosinus);
		this.ongletCourbe.addTab(EPPLab.msg.getString("tab.atypiques"), this.jpCourbeAtypique);
		this.ongletCourbe.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				/*----- Panneau 'Indice' -----*/
				if (((JTabbedPane)e.getSource()).getSelectedIndex() == 0)
					{
					gbc1.gridx = 0; gbc1.gridy = 1; gbc1.insets = marge_slider; gbc1.fill = GridBagConstraints.HORIZONTAL;
					gb1.setConstraints(sliderNumLancement,gbc1);
					jpCourbeIndice.add(sliderNumLancement);
					}
				else
				/*----- Panneau 'Cosinus' -----*/
				if (((JTabbedPane)e.getSource()).getSelectedIndex() == 1)
					{
					gbc2.gridx = 0; gbc2.gridy = 1; gbc2.insets = marge_slider; gbc2.fill = GridBagConstraints.HORIZONTAL;
					gb2.setConstraints(sliderNumLancement,gbc2);
					jpCourbeCosinus.add(sliderNumLancement);
					}
				else
				/*----- Panneau 'Atypiques' -----*/
					{
					gbc3.gridx = 0; gbc3.gridy = 1; gbc3.insets = marge_slider; gbc3.fill = GridBagConstraints.HORIZONTAL;
					gb3.setConstraints(sliderNumLancement,gbc3);
					jpCourbeAtypique.add(sliderNumLancement);
					}
				}
			});

		this.jpEst.add(this.ongletCourbe);

		/*----- Zoom -----*/
		JPanel jpZoom = new JPanel(new BorderLayout(0,0));
		jpZoom.setBorder(new TitledBorderEA(EPPLab.msg.getString("slider.zoom")));

		this.biSlider = new BiSlider(BiSlider.RGB);
//		this.biSlider.setMinimumColor(GuiUtils.VERT_LEGER);
//		this.biSlider.setMaximumColor(GuiUtils.VERT_LEGER);
		this.biSlider.setMinimumColor(GuiUtils.GRIS_238);
		this.biSlider.setMaximumColor(GuiUtils.GRIS_238);
		this.biSlider.setArcSize(5);
		this.biSlider.setPreferredSize(new Dimension(100,30));
		this.biSlider.setMinimumSize(new Dimension(100,30));
		this.biSlider.setFont(GuiUtils.FONT_PARAMETRE);
		this.biSlider.addMouseListener(new MouseAdapter()
			{
			@Override
			public void mouseReleased(MouseEvent MouseEvent_Arg)
				{
				/*----- Mise à jour des courbes -----*/
				courbeIndice.setIndiceMinimum(biSlider.getMinimumColoredValue()-1);
				courbeIndice.setIndiceMaximum(biSlider.getMaximumColoredValue()-1);

				courbeCosinus.setIndiceMinimum(biSlider.getMinimumColoredValue()-1);
				courbeCosinus.setIndiceMaximum(biSlider.getMaximumColoredValue()-1);
 
				courbeAtypiques.setIndiceMinimum(biSlider.getMinimumColoredValue()-1);
				courbeAtypiques.setIndiceMaximum(biSlider.getMaximumColoredValue()-1);

				/*----- Mise à jour du slider Lancement -----*/
				sliderNumLancement.setMinimum(biSlider.getMinimumColoredValue());
				sliderNumLancement.setMaximum(biSlider.getMaximumColoredValue());

					System.out.println(Analyse.this);
				}
			});

		jpZoom.add(this.biSlider, BorderLayout.CENTER);
		this.jpEst.add(jpZoom);

		this.jpEst.add(Box.createVerticalStrut(26)); // 1128x642

		/*----- Panneaux des fenêtres des projections référentes -----*/
		this.tabPaneHistogramme = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		this.tabPaneHistogramme.add(EPPLab.msg.getString("tab.optimum"), null);
		this.tabPaneHistogramme.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				/*----- Numéro du lancement de référence sélectionné -----*/
				String select = tabPaneHistogramme.getTitleAt(tabPaneHistogramme.getSelectedIndex());

				if (select.equals(EPPLab.msg.getString("tab.optimum")))
					numLancementRef = 0;
				else
					{
					select = select.replace("n° ", "");
					numLancementRef = Integer.parseInt(select)-1;
					}

				/*----- Mise à jour des courbes -----*/
				courbeCosinus.setIndiceReference(numLancementRef);
				courbeIndice.setIndiceReference(numLancementRef);
                courbeAtypiques.setIndiceReference(numLancementRef);

				double[] proj = liste.get(numLancementRef).getA();
				courbeCosinus.raz();
				for (int i=0; i<liste.size(); i++)
					courbeCosinus.addNombre((float)Calcul.cosinus(proj, liste.get(i).getA()));

				courbeCosinus.repaint();
				courbeIndice.repaint();
				courbeAtypiques.repaint();
				}
			});

		this.tabPaneEstimateur = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		this.tabPaneEstimateur.add(EPPLab.msg.getString("tab.optimum"), null);
		this.tabPaneEstimateur.addChangeListener(new ChangeListener()
			{
			public void stateChanged (ChangeEvent e)
				{
				/*----- Numéro du lancement de référence sélectionné -----*/
				String select = tabPaneEstimateur.getTitleAt(tabPaneEstimateur.getSelectedIndex());

				if (select.equals(EPPLab.msg.getString("tab.optimum")))
					numLancementRef = 0;
				else
					{
					select = select.replace("n° ", "");
					numLancementRef = Integer.parseInt(select)-1;
					}

				/*----- Mise à jour des courbes -----*/
				courbeCosinus.setIndiceReference(numLancementRef);
				courbeIndice.setIndiceReference(numLancementRef);
				courbeAtypiques.setIndiceReference(numLancementRef);

				double[] proj = liste.get(numLancementRef).getA();
				courbeCosinus.raz();
				for (int i=0; i<liste.size(); i++)
					courbeCosinus.addNombre((float)Calcul.cosinus(proj, liste.get(i).getA()));

				courbeCosinus.repaint();
				courbeIndice.repaint();
				courbeAtypiques.repaint();
				}
			});

		this.jpEst.add(this.tabPaneEstimateur);

		conteneur.add(this.jpEst, BorderLayout.EAST);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Active/désactive les boutons.
	 */
	public void setEnabledSaveSelection (boolean b) { this.btSaveSelection.setEnabled(b); }


	/**
	 * Retourne la référence vers la fenêtre principale.
	 */
	public Ihm getIhm () { return this.frIhm; }


	/**
	 * Initialise.
	 */
	@SuppressWarnings({"unchecked"})
	public void initialise (String fich)
		{
		/*----- Titre de la fenêtre -----*/
		this.setTitle(EPPLab.msg.getString("tb.analyse") + " '" + fich + "'");

		/*----- Fichier de résultats à analyser -----*/
		this.nomFicherResult = fich;

		/*----- Chemin -----*/
		String filePath = frIhm.getParametre().rep_sortie + File.separatorChar + this.nomFicherResult;

		/*----- Compatibilité ? -----*/
		this.majCompatible(frIhm.getParametre().rep_sortie, this.nomFicherResult);

		/*----- Lecture du fichier -----*/
		String ch;
		StringTokenizer st;
		double[][] A = null;
		float[] tab_I = null;

		try	{
			/*----- Fichier -----*/
			FileReader fichier = new FileReader(filePath);

			/*----- Buffer -----*/
			BufferedReader buffer = new BufferedReader(fichier);

			/*----- Lecture de la date -----*/
			buffer.readLine();

			/*----- Lecture du jeu de test et création de la matrice originelle -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			this.fichierDonnees = st.nextToken();

			this.matrice = new Matrice(frIhm.getParametre().rep_donnees + File.separatorChar + this.fichierDonnees);

			/*----- Lecture de la dimension de la matrice -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			this.nbObservations = Integer.parseInt(st.nextToken());	// this.matrice.nbLigne()
			this.dimObservation = Integer.parseInt(st.nextToken());	// this.matrice.nbColonne();

			/*----- Lecture de la transformation des données -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			this.transformDonnees = st.nextToken();

			Matrice mat = null;
			if (this.transformDonnees.equals("Spherique"))
				{
				/*----- Les données sont rendues sphériques -----*/
				if (this.fichierDonnees.equals("fiab_880.txt"))
					mat = new Matrice(frIhm.getParametre().rep_donnees + File.separatorChar + "fiab_880_sphere.txt");
				else
				if (this.fichierDonnees.equals("geno_transpose.txt"))
					mat = new Matrice(frIhm.getParametre().rep_donnees + File.separatorChar + "geno_transpose_sph.txt");
				else
					mat = this.matrice.spherique();
				}
			else
			if (this.transformDonnees.equals("Centree_reduite"))
				{
				/*----- Les données sont centrées et réduites  -----*/
				mat = this.matrice.centreReduite();
				}

			/*----- Lecture du nom de l'indice et instanciation de l'indice -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			this.nomIndice = st.nextToken();

			if (this.nomIndice.equals("KurtosisMax"))			this.indice = new KurtosisMax(mat);
			else if (this.nomIndice.equals("KurtosisMin"))		this.indice = new KurtosisMin(mat);
			else if (this.nomIndice.equals("Friedman"))			this.indice = new Friedman(mat);
			else if (this.nomIndice.equals("FriedmanTukey"))	this.indice = new FriedmanTukey(mat);
			else if (this.nomIndice.equals("Discriminant"))		this.indice = new Discriminant(mat);
			else if (this.nomIndice.equals("Indice4"))			this.indice = new Indice4(mat);
			else if (this.nomIndice.equals("StahelDonoho"))		this.indice = new StahelDonoho(mat);

			/*----- Lecture du nom de la méthode -----*/
			buffer.readLine();
			buffer.readLine();
			buffer.readLine();
			buffer.readLine();
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();

			this.methode = "";
			while (st.hasMoreElements())
				this.methode += st.nextToken() + " ";
			this.methode = this.methode.substring(0, this.methode.length()-1);

			/*----- Lecture de nombre d'itérations -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			this.nbIterations = Integer.parseInt(st.nextToken());

			/*----- Lecture de nombre d'individus -----*/
			ch = buffer.readLine();
			st = new StringTokenizer(ch);
			st.nextToken();
			ch = st.nextToken();
			if (!ch.equals("?")) this.nbIndividus = Integer.parseInt(ch);

			/*----- Lecture des A -----*/
			do	{
				ch = buffer.readLine();
				} while (!ch.startsWith("<Nombre_de_simulations>"));

			st = new StringTokenizer(ch);
			st.nextToken();

			this.nbSimulations = Integer.parseInt(st.nextToken());

			A = new double[this.nbSimulations][this.dimObservation];
			for (int i=0; i<this.nbSimulations; i++)
				{
				do	{
					ch = buffer.readLine();
					} while (!ch.startsWith("<A>"));

				ch = ch.replace(",", ".");

				st = new StringTokenizer(ch);
				st.nextToken();

				for (int j=0; j<this.dimObservation; j++)
					A[i][j] = Double.parseDouble(st.nextToken());

				/*----- Faire en sorte que les vecteurs commencent par un nombre positif -----*/
				if (A[i][0] < 0.0)
					for (int j=0; j<this.dimObservation; j++)
						A[i][j] = -A[i][j];
				}

			/*----- Lecture des I -----*/
			do	{
				ch = buffer.readLine();
				} while (!ch.equals("<Les_I>"));

			StringBuilder sb = new StringBuilder();
			ch = buffer.readLine();
			while (!ch.equals("</Les_I>"))
				{
				/*----- Concatène le texte -----*/
				sb.append(ch).append(" ");
				ch = buffer.readLine();
				}
			sb.deleteCharAt(sb.length()-1);

			/*----- Remplace les ',' par '.' afin de pouvoir le mettre sous format décimal -----*/
			StringUtils.replaceAll(sb, ",", ".");

			/*----- Découpe le texte selon les espaces -----*/
			String[] tab_string = sb.toString().split(" ");

			/*----- Tableau des I () -----*/
			tab_I = new float[tab_string.length]; // tab_string.length = this.nbSimulations

			for(int i=0; i<this.nbSimulations; i++)
				tab_I[i] = Float.parseFloat(tab_string[i]);

			/*----- Fermeture des flux -----*/
			buffer.close();
			fichier.close();
			}
		catch (Exception e)
			{
			MsgUtils.erreurAndExit(Analyse.this,
								   "Problème lors de la lecture du fichier '" + filePath + "'. " + e.getMessage(), // A faire - Traduire
								   "Visualisation");
			}


		/**
		 * Ordonne les valeurs des I obtenues lors des simulations.
		 */
		this.liste.clear();
		for (int i=0; i<tab_I.length; i++)
			this.liste.add(new OrdonnerPP(i, tab_I[i], A[i]));

		Collections.sort(this.liste);


		/**
		 * Données à projeter.
		 */
		this.donnees_x = new double[this.nbObservations];

		for (int i=0; i<this.indice.getMatrice().nbLigne(); i++)
			this.donnees_x[i] = Calcul.produitScalaireVecteur(this.indice.getMatrice().ligne(i),this.liste.get(0).getA());

		this.ecart_type = Calcul.ecartType(this.donnees_x);
		this.moyenne = Calcul.moyenne(this.donnees_x);


		/**
		 * Graphiques de visualisation de la projection.
		 */

		/*----- Histogramme  -----*/
		this.histogramme.setFichierDonnees(this.fichierDonnees);
		this.histogramme.setProjection(this.donnees_x);
		this.histogramme.setValeurAtypiques(this.sliderAtypiques.getValue());
		this.histogramme.setInfos(nomIndice, methode);

		/*----- Estimateur à noyau -----*/
		this.estimateurNoyau.setProjection(this.donnees_x);
		this.estimateurNoyau.setValeurAtypiques(this.sliderAtypiques.getValue());

		if (rbHistogramme.isSelected()) histogramme.repaint();
		if (rbEstimateur.isSelected()) estimateurNoyau.repaint();


		/**
		 * Mise à jour des données des courbes 'Indice', 'Cosinus' et 'Atypiques'.
		 */
		/*----- 'Indice' -----*/
		this.courbeIndice.raz();
		this.courbeIndice.setInfos(this.fichierDonnees, this.nomIndice, this.methode);
		for (int i=0; i<this.liste.size(); i++)
			this.courbeIndice.addNombre((float)this.liste.get(i).getI());

		this.courbeIndice.repaint();

		/*----- 'Cosinus' -----*/
		this.numLancementRef = 0;
		double[] proj = this.liste.get(this.numLancementRef).getA();
		this.courbeCosinus.raz();
		this.courbeCosinus.setInfos(this.fichierDonnees, this.nomIndice, this.methode);
		for (int i=0; i<this.liste.size(); i++)
			this.courbeCosinus.addNombre((float)Calcul.cosinus(proj, this.liste.get(i).getA()));

		this.courbeCosinus.repaint();

		/*----- 'Atypiques' -----*/
		this.courbeAtypiques.raz();
		this.courbeAtypiques.setIndProjAndListeSimu(this.indice, this.liste);
		this.courbeAtypiques.setValeurAtypiques(this.sliderAtypiques.getValue());

		this.courbeAtypiques.repaint();


		/**
		 * Initialise le zoom.
		 */
		this.biSlider.setMinimumValue(1);
		this.biSlider.setMaximumValue(this.liste.size());
		this.biSlider.setMinimumColoredValue(1);
		this.biSlider.setMaximumColoredValue(this.liste.size());
		this.biSlider.setSegmentSize(-1);


		/**
		 * Initialise les sliders.
		 */
		this.sliderNumLancement.setValue(1);
		this.sliderNumLancement.setMinimum(1);
		this.sliderNumLancement.setMaximum(this.liste.size());

		this.lblSigma.setText("m = " + GuiUtils.DECIMAL_3.format(this.moyenne) + "   σ = " + GuiUtils.DECIMAL_3.format(this.ecart_type));

		this.sliderNumObservation.setValue(1);
		this.sliderNumObservation.setMinimum(1);
		this.sliderNumObservation.setMaximum(this.indice.getMatrice().nbLigne());


		/**
		 * Panneaux des fenêtres des projections référentes.
		 */
		this.tabPaneHistogramme.setSelectedIndex(0);
		this.tabPaneEstimateur.setSelectedIndex(0);
		while (this.tabPaneHistogramme.getTabCount() != 1)
			{
			this.tabPaneHistogramme.remove(1);
			this.tabPaneEstimateur.remove(1);
			}

		Histogramme histo = new Histogramme(4,2,false,false,this);
		histo.setFichierDonnees(this.fichierDonnees);
		histo.setProjection(this.donnees_x);

		this.tabPaneHistogramme.setComponentAt(0,histo);

		EstimateurNoyau estim = new EstimateurNoyau(4,2,false,false,null);
		estim.setProjection(this.donnees_x);

		this.tabPaneEstimateur.setComponentAt(0,estim);


		/**
		 * Informations.
		 */
		this.lblFichier.setText(this.fichierDonnees);
		this.lblNomIndice.setText(this.nomIndice);
		this.lblNomMethode.setText(this.methode);
		this.lblValeurI.setText(GuiUtils.DECIMAL_9.format(this.liste.get(0).getI()));

		if (this.methode.equals("Particule Swarm Optimization"))
			this.lblIndividus.setText(EPPLab.msg.getString("label.particule"));
		else
			this.lblIndividus.setText(EPPLab.msg.getString("label.individu"));

		this.lblNbObservations.setText("n=" + this.nbObservations + " p=" + this.dimObservation);
		this.lblNbIndividus.setText("" + this.nbIndividus);
		this.lblNbIterations.setText("" + this.nbIterations);
		this.lblNbSimulations.setText("" + this.nbSimulations);

		this.cbListeSortie.setSelectedItem(fich);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Assure la compatibilité de traitement des fichiers antérieurs.
	 */
	public void majCompatible (String path, String file)
		{
		String ch;
		StringBuilder sb = new StringBuilder();

		try {
			/*----- Fichier -----*/
			FileReader fichier = new FileReader(path + File.separatorChar + file);

			/*----- Buffer -----*/
			BufferedReader buffer = new BufferedReader(fichier);

			/**
			 * Passage de la version 1.1 à 1.2 (le 25 mai 2010)
			 *
			 * Jusqu'alors les données étaient systématiquement rendues
			 * sphériques avant la recherche de projection, or à partir de la
			 * version 1.2, l'utilisateur peut, soit rendre les données
			 * sphériques soit centrées réduites. Pour l'analyse des résultats
			 * trouvés, il est nécessaire de se remettre dans le contexte de la
			 * recherche donc de rendre les données sphériques ou centrées
			 * réduites.
			 * Problème : les fichiers de résultats ne contiennent pas cette
			 * information sur la transformation des données donc pour les
			 * anciens fichiers il est nécessaire de rajouter avant l'analyse
			 * l'information de la transformation sphérique.
			 */

			/*----- Recherche du la propriété "<Dimension_matrice>" -----*/
			do	{
				ch = buffer.readLine();
				sb.append(ch).append('\n');
				} while (!ch.startsWith("<Dimension_matrice>"));

			/*----- On vérifie si la ligne suivante contient la transformation sur les données -----*/
			ch = buffer.readLine();

			if (!ch.startsWith("<Transformation_donnees>"))
				{
				sb.append("<Transformation_donnees>   Spherique").append('\n');
				sb.append(ch).append('\n');

				/*----- On termine de lire le fichier avant d'enregistrer le fichier -----*/
				while ((ch = buffer.readLine()) != null)
					sb.append(ch).append('\n');

				buffer.close();
				fichier.close();

				/*----- Enregistrement du fichier mise à jour -----*/
				FileUtils.saveStringToFich(path, file, sb);
				}
			else
				{
				buffer.close();
				fichier.close();
				}
			}
		catch (Exception e)
			{
			MsgUtils.erreurAndExit(this,
								   "Analyse.majCompatibilite()",
								   EPPLab.msg.getString("showOptionDialog.erreur"));
			}
		}

} /*----- Fin de la classe Analyse -----*/
