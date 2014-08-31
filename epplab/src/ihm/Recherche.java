package ihm;

import ihm.controle.JDialogFichier;
import ihm.controle.JTextNbIndividus;
import ihm.controle.JTextNbIterations;
import ihm.controle.JTextNbSimulations;
import ihm.graphique.ConvergenceIndice;
import ihm.graphique.EstimateurNoyau;
import ihm.graphique.Histogramme;
import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JComboBoxEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javalain.algorithmegenetique.ihm.composant.JRadioButtonEA;
import javalain.algorithmegenetique.ihm.composant.TitledBorderEA;
import epp.EvolutionPP;
import epp.ParametrePP;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import epp.EPPLab;
import util.GuiUtils;
import util.StringUtils;


/*------------------*/
/* Classe Recherche */
/*------------------*/
/**
 * La classe <code>Recherche</code> est la fenêtre permettant de choisir
 * les paramètres afin d'effectuer une nouvelle recherche de groupes homogènes
 * ou d'atypiques dans un ensemble d'observations.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	5 juin 2010
 */
public class Recherche extends JFrame implements ItemListener
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
	private JLabelEAPres		lblSearchGroupe;
	private JRadioButtonEA		rbGKurtosisMin;
	private JRadioButtonEA		rbGFriedman;
	private JRadioButtonEA		rbGDiscriminant;

	private JLabelEAPres		lblSearchAtypique;
	private JRadioButtonEA		rbKurtosisMax;
	private JRadioButtonEA		rbFriedman;
	private JRadioButtonEA		rbFriedmanTukey;
	private JRadioButtonEA		rbDiscriminant;
	private JRadioButtonEA		rbIndice4;
	private JRadioButtonEA		rbStahelDonoho;

	private JLabelEAPres		lblTypeM;
	private JRadioButtonEA		rbGA;
	private JRadioButtonEA		rbPSO;
	private JRadioButtonEA 		rbTribes;
	private JRadioButtonEA		rbMultiStart;

	private JLabelEAPres		lblParametres;
	private JLabelEAPar			lblIndividus;
	private JLabelEAPar			lblIterations;
	private JTextNbIndividus	txtNbIndividus;
	private JTextNbIterations	txtNbIterations;

	private JLabelEAPres		lblDonnees;
	private JRadioButtonEA		rbDNon;
	private JRadioButtonEA		rbDOui;

	private JButtonEA			btLecture;
	private JButtonEA			btSuivant;
	private JButtonEA			btStop;

	private JRadioButtonEA		rbVisuFin;
	private JRadioButtonEA		rbVisuLancement;
	private JRadioButtonEA		rbVisuIteration;

	private JRadioButtonEA		rbHistogramme;
	private JRadioButtonEA		rbEstimateur;

	private JTextNbSimulations	txtNbLancements;
	private JLabelEAPar			lblEnCours;
	private JLabelEAPar			lblTemps;

	private JPanel				jpGraph;
	private Histogramme			histogramme;
	private EstimateurNoyau		estimateurNoyau;
	private ConvergenceIndice	courbe;

	private final ImageIcon		play = new ImageIcon(this.getClass().getResource("icone/play.gif"));
	private final ImageIcon		pause = new ImageIcon(this.getClass().getResource("icone/pause.gif"));

	private JButtonEA			btRetour;
	private JButtonEA			btAnalyse;
	private JComboBoxEA			cbListeSortie;

	private boolean				stepByStep = false;


	/**
	 * Evolution.
	 */
	private EvolutionPP evolution;

	private ParametrePP parametre;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée et initialise la fenêtre.
	 */
	@SuppressWarnings("LeakingThisInConstructor")
	public Recherche (int x, int y, EvolutionPP e, ParametrePP p, Ihm f)
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
				EPPLab.savePrefs(parametre, Recherche.this.getX(), Recherche.this.getY());
				System.exit(0);
				}
			public void windowClosed (WindowEvent e) {}
			public void windowIconified (WindowEvent e) {}
			public void windowDeiconified (WindowEvent e) {}
			public void windowDeactivated (WindowEvent e) {}
			public void windowActivated (WindowEvent e) { cbListeSortie.updateUI(); }
			});

		/*----- Lien avec l'évolution -----*/
		this.evolution = e;
		this.evolution.setFrRecherche(this);

		this.parametre = p;

		/*----- Lien vers la fenêtre principale -----*/
		this.frIhm = f;

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur,BoxLayout.Y_AXIS));
		
		/*----- Variables locales -----*/
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		int	ligne;

		/**
		 * Panneau 'Nord'.
		 */
		JPanel jpNord = new JPanel();
		jpNord.setLayout(new BoxLayout(jpNord,BoxLayout.X_AXIS));

		/**
		 * Panneau 'Indices'.
		 */
		JPanel jpIndice = new JPanel();
		jpIndice.setLayout(gb);
		jpIndice.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.indice")));

		ligne = 0;

		/*----- Zone d'espacement -----*/
		Component espace = Box.createHorizontalStrut(125);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(espace,gbc);
		jpIndice.add(espace);

		/*----- Recherche de groupes -----*/
		this.lblSearchGroupe = new JLabelEAPres(EPPLab.msg.getString("label.searchGroupe"));
		gb.setConstraints(this.lblSearchGroupe,gbc);
		jpIndice.add(this.lblSearchGroupe);
		ligne++;

		ButtonGroup groupSearch = new ButtonGroup();

		/*----- Indice 'KurtosisMin' -----*/
		this.rbGKurtosisMin = new JRadioButtonEA(ParametrePP.KURTOSIS_MIN);
		this.rbGKurtosisMin.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbGKurtosisMin,gbc);
		jpIndice.add(this.rbGKurtosisMin);
		groupSearch.add(this.rbGKurtosisMin);
		ligne++;

		/*----- Indice 'Friedman' -----*/
		this.rbGFriedman = new JRadioButtonEA(ParametrePP.FRIEDMAN);
		this.rbGFriedman.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbGFriedman,gbc);
		jpIndice.add(this.rbGFriedman);
		groupSearch.add(this.rbGFriedman);
		ligne++;

		/*----- Indice 'Discriminant' -----*/
		this.rbGDiscriminant = new JRadioButtonEA(ParametrePP.DISCRIMINANT);
		this.rbGDiscriminant.addItemListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbGDiscriminant,gbc);
		jpIndice.add(this.rbGDiscriminant);
		groupSearch.add(this.rbGDiscriminant);

		ligne = 0;

		/*----- Recherche d'atypiques -----*/
		this.lblSearchAtypique = new JLabelEAPres(EPPLab.msg.getString("label.searchAtypique"));
		gbc.gridx = 1; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.lblSearchAtypique,gbc);
		jpIndice.add(this.lblSearchAtypique);
		ligne++;

		/*----- Indice 'KurtosisMax' -----*/
		this.rbKurtosisMax = new JRadioButtonEA(ParametrePP.KURTOSIS_MAX);
		this.rbKurtosisMax.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(this.rbKurtosisMax,gbc);
		jpIndice.add(this.rbKurtosisMax);
		groupSearch.add(this.rbKurtosisMax);
		ligne++;

		/*----- Indice 'Friedman' -----*/
		this.rbFriedman = new JRadioButtonEA(ParametrePP.FRIEDMAN);
		this.rbFriedman.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0A00;
		gb.setConstraints(this.rbFriedman,gbc);
		jpIndice.add(this.rbFriedman);
		groupSearch.add(this.rbFriedman);
		ligne++;

		/*----- Indice 'Friedman Tukey' -----*/
		this.rbFriedmanTukey = new JRadioButtonEA(ParametrePP.FRIEDMAN_TUKEY);
		this.rbFriedmanTukey.addItemListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbFriedmanTukey,gbc);
		jpIndice.add(this.rbFriedmanTukey);
		groupSearch.add(this.rbFriedmanTukey);
		ligne++;

		/*----- Indice 'Discriminant' -----*/
		this.rbDiscriminant = new JRadioButtonEA(ParametrePP.DISCRIMINANT);
		this.rbDiscriminant.addItemListener(this);
		gbc.gridy = ligne; gbc.weightx = 1; gbc.weighty = 1;
		gb.setConstraints(this.rbDiscriminant,gbc);
		jpIndice.add(this.rbDiscriminant);
		groupSearch.add(this.rbDiscriminant);
		ligne++;

		/*----- Indice 'Indice' -----*/
		this.rbIndice4 = new JRadioButtonEA(ParametrePP.INDICE_4);
		this.rbIndice4.setVisible(false);
		this.rbIndice4.addItemListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbIndice4,gbc);
		jpIndice.add(this.rbIndice4);
		groupSearch.add(this.rbIndice4);
		ligne++;

		/*----- Indice 'Stahel Donoho' -----*/
		this.rbStahelDonoho = new JRadioButtonEA(ParametrePP.STAHEL_DONOHO);
		this.rbStahelDonoho.setVisible(false);
		this.rbStahelDonoho.addItemListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbStahelDonoho,gbc);
		jpIndice.add(this.rbStahelDonoho);
		groupSearch.add(this.rbStahelDonoho);

		jpNord.add(jpIndice);

		/**
		 * Panneau 'Méthodes'.
		 */
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();

		JPanel jpMethode = new JPanel();
		jpMethode.setLayout(gb);
		jpMethode.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.methode")));

		ligne = 0;

		/*----- Type de méthode -----*/
		this.lblTypeM = new JLabelEAPres(EPPLab.msg.getString("label.typeM"));
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(this.lblTypeM,gbc);
		jpMethode.add(this.lblTypeM);
		ligne++;

		ButtonGroup groupMethode = new ButtonGroup();

		/*----- Méthode 'GA' -----*/
		this.rbGA = new JRadioButtonEA(EPPLab.msg.getString("rb.GA"));
		this.rbGA.addItemListener(this);
		this.rbGA.setToolTipText(EPPLab.msg.getString("nom.GA"));
		gbc.gridy = ligne;  gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbGA,gbc);
		jpMethode.add(this.rbGA);
		groupMethode.add(this.rbGA);

		/*----- Méthode 'TRIBES' -----*/
		this.rbTribes = new JRadioButtonEA(EPPLab.msg.getString("rb.TRIBES"));
		this.rbTribes.addItemListener(this);
		this.rbTribes.setToolTipText(EPPLab.msg.getString("nom.TRIBES"));
		gbc.gridx=1; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbTribes,gbc);
		jpMethode.add(this.rbTribes);
		groupMethode.add(this.rbTribes);
		ligne++;

		/*----- Méthode 'PSO' -----*/
		this.rbPSO = new JRadioButtonEA(EPPLab.msg.getString("rb.PSO"));
		this.rbPSO.addItemListener(this);
		this.rbPSO.setToolTipText(EPPLab.msg.getString("nom.PSO"));
		gbc.gridx=0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbPSO,gbc);
		jpMethode.add(this.rbPSO);
		groupMethode.add(this.rbPSO);

		/*----- Méthode 'MultiStart' -----*/
		this.rbMultiStart = new JRadioButtonEA(EPPLab.msg.getString("rb.MULTISTART"));
		this.rbMultiStart.setVisible(true);
		this.rbMultiStart.addItemListener(this);
		this.rbMultiStart.setToolTipText(EPPLab.msg.getString("nom.MULTISTART"));
		gbc.gridx=1; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbMultiStart,gbc);
//		jpMethode.add(this.rbMultiStart);
		groupMethode.add(this.rbMultiStart);
		ligne++;

		/*----- Paramètres -----*/
		this.lblParametres = new JLabelEAPres(EPPLab.msg.getString("label.parametres"));
		gbc.gridx=0; gbc.gridy = ligne; gbc.gridwidth = 2; gbc.insets = new Insets(4,0,0,0);
		gb.setConstraints(this.lblParametres,gbc);
		jpMethode.add(this.lblParametres);
		ligne++;

		/*----- Nombre d'individus -----*/
		this.lblIndividus = new JLabelEAPar(EPPLab.msg.getString("label.individu"));
		gbc.gridy = ligne; gbc.insets = new Insets(4,5,0,0); gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gb.setConstraints(this.lblIndividus,gbc);
		jpMethode.add(this.lblIndividus);

		if( this.rbPSO.isSelected())	this.lblIndividus.setText(EPPLab.msg.getString("label.particule"));
			
		this.txtNbIndividus = new JTextNbIndividus(this);
		this.txtNbIndividus.setText("" + p.getNbIndividus());
		gbc.gridx = 2;
		gb.setConstraints(this.txtNbIndividus,gbc);
		jpMethode.add(this.txtNbIndividus);
		ligne++;

		/*-----  Nombre d'itérations -----*/
		this.lblIterations = new JLabelEAPar(EPPLab.msg.getString("label.iteration"));
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = new Insets(2,5,0,0);
		gb.setConstraints(this.lblIterations,gbc);
		jpMethode.add(this.lblIterations);

		this.txtNbIterations = new JTextNbIterations(this);
		this.txtNbIterations.setText("" + p.getNbIterations());
		gbc.gridx = 2; gbc.weightx = 1.0; gbc.weighty = 1.0;
		gb.setConstraints(this.txtNbIterations,gbc);
		jpMethode.add(this.txtNbIterations);

		jpNord.add(jpMethode);

		/**
		 * Panneau 'Donnees'.
		 */
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();

		JPanel jpDonnees = new JPanel();
		jpDonnees.setLayout(gb);
		jpDonnees.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.donnees")));

		ligne = 0;

		 /*----- Données sphériques -----*/
		this.lblDonnees = new JLabelEAPres(EPPLab.msg.getString("label.donneesSpheriques"));
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(this.lblDonnees,gbc);
		jpDonnees.add(this.lblDonnees);
		ligne++;

		ButtonGroup groupDonnees = new ButtonGroup();

		/*----- Données sphériques 'Oui' -----*/
		this.rbDOui = new JRadioButtonEA(EPPLab.msg.getString("rb.oui"));
		this.rbDOui.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.rbDOui,gbc);
		jpDonnees.add(this.rbDOui);
		groupDonnees.add(this.rbDOui);
		ligne++;

		/*----- Données sphériques 'Non' -----*/
		this.rbDNon = new JRadioButtonEA(EPPLab.msg.getString("rb.non"));
		this.rbDNon.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500; gbc.weightx = 1; gbc.weighty = 1;
		gb.setConstraints(this.rbDNon,gbc);
		jpDonnees.add(this.rbDNon);
		groupDonnees.add(this.rbDNon);

		jpNord.add(jpDonnees);

		/*----- Ajout du panneau à la fenêtre -----*/
		conteneur.add(jpNord);

		/**
		 * Panneau 'Sud'.
		 */
		JPanel jpSud = new JPanel();
		jpSud.setLayout(new BoxLayout(jpSud,BoxLayout.X_AXIS));

		/**
		 * Panneau 'Visualisation'.
		 */
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();

		JPanel jpVisualisation = new JPanel();
		jpVisualisation.setLayout(gb);
		jpVisualisation.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.visualisation")));

		ligne = 0;

		/*----- Navigation -----*/
		JLabelEAPres lblNavigation = new JLabelEAPres(EPPLab.msg.getString("label.navigation"));
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(lblNavigation,gbc);
		jpVisualisation.add(lblNavigation);
		ligne++;

		/**
		 * Panneau 'Navigation'.
		 */
		JPanel jpNavigation = new JPanel();
		jpNavigation.setLayout(GuiUtils.FL_LEFT_00);
		gbc.gridy = ligne; gbc.gridheight = 3; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(jpNavigation,gbc);

		/*----- Lecture -----*/
		this.btLecture = new JButtonEA(this.play);
		this.btLecture.setToolTipText(EPPLab.msg.getString("bt.lecture"));
		jpNavigation.add(this.btLecture);

		/*----- Suivant -----*/
		this.btSuivant= new JButtonEA(new ImageIcon(this.getClass().getResource("icone/suivant.gif")));
		this.btSuivant.setToolTipText(EPPLab.msg.getString("bt.suivant"));
		jpNavigation.add(this.btSuivant);

		/*----- Stop -----*/
		this.btStop= new JButtonEA(new ImageIcon(this.getClass().getResource("icone/stop.gif")));
		this.btStop.setEnabled(false);
		this.btStop.setToolTipText(EPPLab.msg.getString("bt.stop"));
		jpNavigation.add(this.btStop);

		jpVisualisation.add(jpNavigation);

		ligne = 0;

		/*----- Visualiser -----*/
		JLabelEAPres lblVisualisation = new JLabelEAPres(EPPLab.msg.getString("label.visualisation"));
		gbc.gridx = 1; gbc.gridy = ligne; gbc.gridheight = 1; gbc.insets = GuiUtils.INSETS_0500; gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gb.setConstraints(lblVisualisation,gbc);
		jpVisualisation.add(lblVisualisation);
		ligne++;

		ButtonGroup groupVisualisation = new ButtonGroup();

		/*----- Visualiser 'à la fin' -----*/
		this.rbVisuFin = new JRadioButtonEA(EPPLab.msg.getString("rb.visuFin"));
		this.rbVisuFin.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(this.rbVisuFin,gbc);
		jpVisualisation.add(this.rbVisuFin);
		groupVisualisation.add(this.rbVisuFin);
		ligne++;

		/*----- Visualiser 'à chaque lancement' -----*/
		this.rbVisuLancement = new JRadioButtonEA(EPPLab.msg.getString("rb.visuLancement"));
		this.rbVisuLancement.addItemListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0A00;
		gb.setConstraints(this.rbVisuLancement,gbc);
		jpVisualisation.add(this.rbVisuLancement);
		groupVisualisation.add(this.rbVisuLancement);
		ligne++;

		/*----- Visualiser 'à chaque itération'  -----*/
		this.rbVisuIteration = new JRadioButtonEA(EPPLab.msg.getString("rb.visuIteration"));
		this.rbVisuIteration.addItemListener(this);
		gbc.gridy = ligne; gbc.weighty = 1.0;
		gb.setConstraints(this.rbVisuIteration,gbc);
		jpVisualisation.add(this.rbVisuIteration);
		groupVisualisation.add(this.rbVisuIteration);

		gbc.weighty = 0.0;
		ligne = 0;

		/*----- Graphiques -----*/
		JLabelEAPres lblGraphiques = new JLabelEAPres(EPPLab.msg.getString("label.graphique"));
		gbc.gridx = 2; gbc.gridy = ligne; gbc.gridheight = 1; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(lblGraphiques,gbc);
		jpVisualisation.add(lblGraphiques);
		ligne++;

		ButtonGroup groupGraph = new ButtonGroup();

		/*----- Histogramme -----*/
		this.rbHistogramme = new JRadioButtonEA(EPPLab.msg.getString("label.histogramme"));
		this.rbHistogramme.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				if (rbHistogramme.isSelected() && estimateurNoyau != null)
					{
					/*----- Efface la fenêtre de l'affichage précédent -----*/
					jpGraph.remove(estimateurNoyau);

					/*----- Crée et ajoute la fenêtre d'affichage de l'histogramme -----*/
					if (histogramme == null)
						{
						histogramme = new Histogramme(6,3,false,false,null);
						histogramme.setFichierDonnees(parametre.ficherDeDonnees);
						}

					/*----- Mise à jour de l'histogramme -----*/
					histogramme.setProjection(estimateurNoyau.getDonnees());

					/*----- Ajoute l'histogramme à la fenêtre -----*/
					jpGraph.add(histogramme, 1);
					jpGraph.repaint();
					pack();
					}
				}
			});
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(this.rbHistogramme,gbc);
		jpVisualisation.add(this.rbHistogramme);
		groupGraph.add(this.rbHistogramme);
		ligne++;

		/*----- Estimateur -----*/
		this.rbEstimateur = new JRadioButtonEA(EPPLab.msg.getString("label.estimateur"));
		this.rbEstimateur.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				if (rbEstimateur.isSelected() && histogramme != null)
					{
					/*----- Efface la fenêtre de l'affichage précédent -----*/
					jpGraph.remove(histogramme);

					/*----- Crée et ajoute la fenêtre d'affichage de l'estimateur à noyau -----*/
					if (estimateurNoyau == null)
						estimateurNoyau = new EstimateurNoyau(6,3,false,false,null);

					/*----- Mise à jour de l'estimateur à noyau -----*/
					estimateurNoyau.setProjection(histogramme.getDonnees());

					/*----- Ajoute l'estimateur à noyau à la fenêtre -----*/
					jpGraph.add(estimateurNoyau, 1);
					jpGraph.repaint();
					pack();
					}
				}
			});
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0A00;
		gb.setConstraints(this.rbEstimateur,gbc);
		jpVisualisation.add(this.rbEstimateur);
		groupGraph.add(this.rbEstimateur);

		ligne = 0;

		/*----- Zone d'espacement -----*/
		espace = Box.createHorizontalStrut(175);
		gbc.gridx = 3; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(espace,gbc);
		jpVisualisation.add(espace);

		/*----- Nombre de lancements ----*/
		JLabelEAPres lblNbLancements = new JLabelEAPres(EPPLab.msg.getString("label.nbLancements"));
		gbc.weightx = 1.0;
		gb.setConstraints(lblNbLancements,gbc);
		jpVisualisation.add(lblNbLancements);
		ligne++;

		gbc.weightx = 0.0;

		this.txtNbLancements = new JTextNbSimulations(this);
		this.txtNbLancements.setText("" + p.nbSimulation);
		gbc.gridx = 3; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(this.txtNbLancements,gbc);
		jpVisualisation.add(this.txtNbLancements);
		ligne++;

		/*----- En cours... -----*/
		this.lblEnCours = new JLabelEAPar("");
		this.lblEnCours.setToolTipText(EPPLab.msg.getString("tiptext.enCours"));
		gbc.gridx = 3; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0A00;
		gb.setConstraints(this.lblEnCours,gbc);
		jpVisualisation.add(this.lblEnCours);
		ligne++;

		/*----- Temps de simulation -----*/
		this.lblTemps = new JLabelEAPar("");
		this.lblTemps.setToolTipText(EPPLab.msg.getString("tiptext.temps"));
		gbc.gridx = 3; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5A00;
		gb.setConstraints(this.lblTemps,gbc);
		jpVisualisation.add(this.lblTemps);

		jpSud.add(jpVisualisation);

		/**
		 * Panneau 'Courbe'.
		 */
		JPanel jpCourbe = new JPanel();
		jpCourbe.setLayout(GuiUtils.FL_LEFT_00);
		jpCourbe.setBorder(new TitledBorderEA(EPPLab.msg.getString("tb.courbe")));

		this.courbe = new ConvergenceIndice(200, 100);
		jpCourbe.add(this.courbe);

		jpSud.add(jpCourbe);

		/*----- Ajout du panneau à la fenêtre -----*/
		conteneur.add(jpSud);

		/**
		 * Action sur les boutons.
		 */
		/*----- Lecture -----*/
		MouseAdapter read = new MouseAdapter()
			{
			@Override
			public void mouseClicked(MouseEvent e)
				{
				/*----- Positionne les contrôles -----*/
				execute();

				btSuivant.setEnabled(false);
				btStop.setEnabled(true);

				if (btLecture.getIcon().equals(play))
					{
					btLecture.setIcon(pause);
					btLecture.setToolTipText(EPPLab.msg.getString("bt.pause"));

					/**
					 * Lancement de la simulation.
					 */
					if (evolution.getEtat() == EvolutionPP.PAUSE)
						evolution.setEtat(EvolutionPP.CONTINUE);
					else
						evolution.setEtat(EvolutionPP.DEMARRE);
					}
				else
					{
					btLecture.setIcon(play);
					btLecture.setToolTipText(EPPLab.msg.getString("bt.lecture"));

					/**
					 * Lancement de la simulation.
					 */
					evolution.setEtat(EvolutionPP.PAUSE);
					}
				}
			};
		this.btLecture.addMouseListener(read);

		/*----- Suivant -----*/
		MouseAdapter next = new MouseAdapter()
			{
			@Override
			public void mouseClicked(MouseEvent e)
				{
				/*----- Positionne les contrôles -----*/
				execute();

				stepByStep = true;

				btLecture.setEnabled(false);
				btSuivant.setEnabled(false);
				btStop.setEnabled(true);

				/**
				 * Lancement de la simulation.
				 */
				evolution.setEtat(EvolutionPP.DEMARRE);
				}
			};
		this.btSuivant.addMouseListener(next);

		/*----- Stop -----*/
		MouseAdapter arret = new MouseAdapter()
			{
			@Override
			public void mouseClicked(MouseEvent e)
				{
				/**
				 * Arrête l'évolution.
				 */
				evolution.setEtat(EvolutionPP.TERMINE);
				}
			};
		 this.btStop.addMouseListener(arret);

		/**
		 * Panneau de commande.
		 */
		JPanel jpCde = new JPanel();
		jpCde.setLayout(new BorderLayout());
		jpCde.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

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
		jpCde.add(btAide, BorderLayout.WEST);

		/*----- Boutons -----*/
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout(5,0));
		jp.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

		/*----- Bouton 'Retour' -----*/
		this.btRetour = new JButtonEA(EPPLab.msg.getString("menu.retour"));
		this.btRetour.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);
				frIhm.setVisible(true);
				}
			});
		jp.add(this.btRetour, BorderLayout.WEST);

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
				if (cbListeSortie.getSelectedItem().equals(EPPLab.msg.getString("cb.listeSortie")))
					btAnalyse.setEnabled(false);
				else
					btAnalyse.setEnabled(true);
				}
			});
		jp.add(this.cbListeSortie, BorderLayout.CENTER);

		/*----- Bouton 'Analyse' -----*/
		this.btAnalyse = new JButtonEA(EPPLab.msg.getString("bt.analyse"));
		this.btAnalyse.setEnabled(false);
		this.btAnalyse.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				setVisible(false);

				if (frIhm.frAnalyse == null)
					frIhm.frAnalyse = new Analyse(Recherche.this.getX(),Recherche.this.getY(),(String) cbListeSortie.getSelectedItem(),frIhm);

				frIhm.frAnalyse.setVisible(true);
				frIhm.frAnalyse.initialise((String)cbListeSortie.getSelectedItem());
				}
			});
		jp.add(this.btAnalyse, BorderLayout.EAST);

		jpCde.add(jp, BorderLayout.EAST);

  		/*----- Ajoute du panneau de commande à la fenêtre -----*/
		conteneur.add(jpCde);

		/**
		 * Panneau du graphique.
		 */
		this.jpGraph = new JPanel();
		this.jpGraph.setLayout(new BoxLayout(this.jpGraph, BoxLayout.X_AXIS));
		this.jpGraph.setBackground(GuiUtils.GRIS_238);
		this.jpGraph.setVisible(false);
		this.jpGraph.add(Box.createHorizontalGlue());

		/*----- Bouton par défaut -----*/
		this.getRootPane().setDefaultButton(this.btLecture);

		/*----- Ajoute du panneau de commande à la fenêtre -----*/
		conteneur.add(this.jpGraph);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();

		/*----- Rend la fenetre visible -----*/
		this.setVisible(true);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les paramètres de recherche.
	 */
	public ParametrePP getParametre () { return this.parametre; }


	/**
	 * Retourne la courbe de convergence.
	 */
	public ConvergenceIndice getCourbeConvergence () { return this.courbe; }


	/**
	 * Vérifie si l'affichage doit être effectué à chaque itération,
	 * à chaque lancement ou à la fin.
	 */
	public boolean isVisuIteration () { return this.rbVisuIteration.isSelected(); }
	public boolean isVisuLancement () { return this.rbVisuLancement.isSelected(); }
	public boolean isVisuFin () { return this.rbVisuFin.isSelected(); }


	/**
	 * Signale à l'interface la fin d'une simulation
	 * lors du mode lancement par lancement.
	 */
	public void endStepByStep ()
		{
		this.stepByStep = false;

		javax.swing.SwingUtilities.invokeLater (new Runnable()
			{
			public void run()
				{
				btLecture.setEnabled(true);
				btSuivant.setEnabled(true);
				}
			});
		}

	public boolean isStepByStep () { return this.stepByStep; }

	
	/**
	 * Initialise par défaut de l'interface.
	 */
	public void initialise ()
		{
		/*----- Titre de la fenêtre -----*/
		this.setTitle(EPPLab.msg.getString("frame.nouvelleRecherche") + " '" + parametre.ficherDeDonnees + "'");

		/*----- Mode 'Expert' -----*/
		if (this.frIhm.isModeExpert())
			{
			this.lblSearchGroupe.setEnabled(true);
			this.rbGKurtosisMin.setEnabled(true);
			this.rbGFriedman.setEnabled(true);
			this.rbGDiscriminant.setEnabled(true);

			this.lblSearchAtypique.setEnabled(true);
			this.rbKurtosisMax.setEnabled(true);
			this.rbFriedman.setEnabled(true);
			this.rbFriedmanTukey.setEnabled(true);
			this.rbDiscriminant.setEnabled(true);
			this.rbIndice4.setEnabled(true);
			this.rbStahelDonoho.setEnabled(true);

			this.lblTypeM.setEnabled(true);
			this.rbGA.setEnabled(true);
			this.rbPSO.setEnabled(true);
			this.rbTribes.setEnabled(true);
			this.rbMultiStart.setEnabled(true);

			this.lblParametres.setEnabled(true);
			this.lblIndividus.setEnabled(!this.rbTribes.isSelected());
			this.txtNbIndividus.setEnabled(!this.rbTribes.isSelected());
			this.lblIterations.setEnabled(true);
			this.txtNbIterations.setEnabled(true);

			this.lblDonnees.setEnabled(true);
			this.rbDOui.setEnabled(true);
			this.rbDNon.setEnabled(true);
			}
		else
		/*----- Mode 'Débutant" -----*/
		if (this.frIhm.isModeDebutant())
			{
			if (this.frIhm.isRechercheGroupe())
				{
				/*----- Recherche de groupes -----*/
				this.lblSearchGroupe.setEnabled(true);
				this.rbGKurtosisMin.setEnabled(true);
				this.rbGFriedman.setEnabled(true);
				this.rbGDiscriminant.setEnabled(true);

				this.lblSearchAtypique.setEnabled(false);
				this.rbKurtosisMax.setEnabled(false);
				this.rbFriedman.setEnabled(false);
				this.rbFriedmanTukey.setEnabled(false);
				this.rbDiscriminant.setEnabled(false);
				this.rbIndice4.setEnabled(false);
				this.rbStahelDonoho.setEnabled(false);
				}
			else
				{
				/*----- Recherche d'atypiques -----*/
				this.lblSearchGroupe.setEnabled(false);
				this.rbGKurtosisMin.setEnabled(false);
				this.rbGFriedman.setEnabled(false);
				this.rbGDiscriminant.setEnabled(false);

				this.lblSearchAtypique.setEnabled(true);
				this.rbKurtosisMax.setEnabled(true);
				this.rbFriedman.setEnabled(true);
				this.rbFriedmanTukey.setEnabled(true);
				this.rbDiscriminant.setEnabled(true);
				this.rbIndice4.setEnabled(true);
				this.rbStahelDonoho.setEnabled(true);
				}

			this.lblTypeM.setEnabled(false);
			this.rbGA.setEnabled(false);
			this.rbPSO.setEnabled(false);
			this.rbTribes.setEnabled(false);
			this.rbMultiStart.setEnabled(false);

			this.lblParametres.setEnabled(false);
			this.lblIndividus.setEnabled(false);
			this.txtNbIndividus.setEnabled(false);
			this.lblIterations.setEnabled(false);
			this.txtNbIterations.setEnabled(false);

			this.lblDonnees.setEnabled(false);
			this.rbDOui.setEnabled(true);
			this.rbDNon.setEnabled(true);
			}

		/**
		 * Initialisation des paramètres de l'application.
		 */

		/*----- Indice -----*/
		if (this.frIhm.isRechercheGroupe())
			this.rbGKurtosisMin.setSelected(true);
		else
			this.rbKurtosisMax.setSelected(true);

		/*----- Méthode -----*/
		if (this.parametre.methode == ParametrePP.GA)			this.rbGA.setSelected(true);
		else if (this.parametre.methode == ParametrePP.PSO)		this.rbPSO.setSelected(true);
		else if (this.parametre.methode == ParametrePP.TRIBES)	this.rbTribes.setSelected(true);

		/*----- Données sphériques -----*/
		if (this.parametre.sphere)
			this.rbDOui.setSelected(true);
		else
			this.rbDNon.setSelected(true);

		/*----- Graphique -----*/
		this.rbVisuLancement.setSelected(true);
//		this.rbVisuIteration.setSelected(true);
		this.rbEstimateur.setSelected(true);

		/*----- Courbe -----*/
		if (this.histogramme != null)
			{
			this.jpGraph.remove(this.histogramme);
			this.histogramme = null;
			}
		
		if (this.estimateurNoyau != null)
			{
			this.jpGraph.remove(this.estimateurNoyau);
			this.estimateurNoyau = null;
			}

		/*----- Temps -----*/
		this.lblEnCours.setVisible(false);
		this.lblTemps.setVisible(false);

		/*----- Liste des fichiers de sortie -----*/
		this.cbListeSortie.setSelectedItem(EPPLab.msg.getString("cb.listeSortie"));

		this.pack();

		/*----- Initialisation de l'interface -----*/
		this.rbDOui.setSelected(true);
		this.rbTribes.setSelected(true);
		this.txtNbLancements.setValeur(200);
//		this.txtNbIterations.setValeur(100);
		}


	/**
	 * Positionne les contrôles lors du lancement d'une exécution.
	 */
	private void execute ()
		{
		this.lblSearchGroupe.setEnabled(false);
		this.rbGKurtosisMin.setEnabled(false);
		this.rbGFriedman.setEnabled(false);
		this.rbGDiscriminant.setEnabled(false);

		this.lblSearchAtypique.setEnabled(false);
		this.rbKurtosisMax.setEnabled(false);
		this.rbFriedman.setEnabled(false);
		this.rbFriedmanTukey.setEnabled(false);
		this.rbDiscriminant.setEnabled(false);
		this.rbIndice4.setEnabled(false);
		this.rbStahelDonoho.setEnabled(false);

		this.lblTypeM.setEnabled(false);
		this.rbGA.setEnabled(false);
		this.rbPSO.setEnabled(false);
		this.rbTribes.setEnabled(false);
		this.rbMultiStart.setEnabled(false);

		this.lblParametres.setEnabled(false);
		this.lblIndividus.setEnabled(false);
		this.txtNbIndividus.setEnabled(false);
		this.lblIterations.setEnabled(false);
		this.txtNbIterations.setEnabled(false);

		this.lblDonnees.setEnabled(false);
		this.rbDOui.setEnabled(false);
		this.rbDNon.setEnabled(false);

		this.txtNbLancements.setEnabled(false);
		this.lblEnCours.setVisible(true);
		this.lblEnCours.setEnabled(true);
		this.lblTemps.setVisible(true);
		this.lblTemps.setEnabled(true);

		this.btRetour.setEnabled(false);
		this.cbListeSortie.setEnabled(false);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();
		}


	/**
	 * Positionne les contrôles lors de la fin d'une exécution.
	 */
	public void termine (final String new_fich)
		{
		javax.swing.SwingUtilities.invokeLater (new Runnable()
			{
			public void run()
				{
				/*----- Si mode 'Expert' -----*/
				if (frIhm.isModeExpert())
					{
					lblSearchGroupe.setEnabled(true);
					rbGKurtosisMin.setEnabled(true);
					rbGFriedman.setEnabled(true);
					rbGDiscriminant.setEnabled(true);

					lblSearchAtypique.setEnabled(true);
					rbKurtosisMax.setEnabled(true);
					rbFriedman.setEnabled(true);
					rbFriedmanTukey.setEnabled(true);
					rbDiscriminant.setEnabled(true);
					rbIndice4.setEnabled(true);
					rbStahelDonoho.setEnabled(true);

					lblTypeM.setEnabled(true);
					rbGA.setEnabled(true);
					rbPSO.setEnabled(true);
					rbTribes.setEnabled(true);
					rbMultiStart.setEnabled(true);

					lblParametres.setEnabled(true);
					lblIndividus.setEnabled(!rbTribes.isSelected());
					txtNbIndividus.setEnabled(!rbTribes.isSelected());
					lblIterations.setEnabled(true);
					txtNbIterations.setEnabled(true);

					lblDonnees.setEnabled(true);
					rbDOui.setEnabled(true);
					rbDNon.setEnabled(true);
					}
				else
				/*----- Mode 'Débutant" -----*/
				if (frIhm.isModeDebutant())
					{
					if (frIhm.isRechercheGroupe())
						{
						/*----- Recherche de groupes -----*/
						lblSearchGroupe.setEnabled(true);
						rbGKurtosisMin.setEnabled(true);
						rbGFriedman.setEnabled(true);
						rbGDiscriminant.setEnabled(true);

						lblSearchAtypique.setEnabled(false);
						rbKurtosisMax.setEnabled(false);
						rbFriedman.setEnabled(false);
						rbFriedmanTukey.setEnabled(false);
						rbDiscriminant.setEnabled(false);
						rbIndice4.setEnabled(false);
						rbStahelDonoho.setEnabled(false);
						}
					else
						{
						/*----- Recherche d'atypiques -----*/
						lblSearchGroupe.setEnabled(false);
						rbGKurtosisMin.setEnabled(false);
						rbGFriedman.setEnabled(false);
						rbGDiscriminant.setEnabled(false);

						lblSearchAtypique.setEnabled(true);
						rbKurtosisMax.setEnabled(true);
						rbFriedman.setEnabled(true);
						rbFriedmanTukey.setEnabled(true);
						rbDiscriminant.setEnabled(true);
						rbIndice4.setEnabled(true);
						rbStahelDonoho.setEnabled(true);
						}

					lblTypeM.setEnabled(false);
					rbGA.setEnabled(false);
					rbPSO.setEnabled(false);
					rbTribes.setEnabled(false);
					rbMultiStart.setEnabled(false);

					lblParametres.setEnabled(false);
					lblIndividus.setEnabled(false);
					txtNbIndividus.setEnabled(false);
					lblIterations.setEnabled(false);
					txtNbIterations.setEnabled(false);

					lblDonnees.setEnabled(false);
					rbDOui.setEnabled(true);
					rbDNon.setEnabled(true);
					}

				btLecture.setIcon(play);
				btLecture.setToolTipText(EPPLab.msg.getString("bt.lecture"));
				btLecture.setEnabled(true);
				btSuivant.setEnabled(true);
				btStop.setEnabled(false);

				txtNbLancements.setEnabled(true);
				lblEnCours.setEnabled(false);
				lblTemps.setEnabled(false);

				btRetour.setEnabled(true);
				cbListeSortie.setEnabled(true);

				/*----- Demande à l'utilisateur s'il souhaite sauvegarder le fichier résultat -----*/
				JDialogFichier dlg = new JDialogFichier(Recherche.this, parametre.rep_sortie, new_fich, EPPLab.msg.getString("showInputDialog.sauverFichier"));
				String retour = dlg.afficher();

				File f_temp = new File(parametre.rep_sortie + File.separatorChar + new_fich + ".temp");

				/*----- L'utilisateur désire garder le fichier -----*/
				if (!"".equals(retour))
					{
					File f_dest = new File(parametre.rep_sortie + File.separatorChar + retour);

					/*----- Si le fichier existe, cela signifie que l'utilisateur veut l'écraser -----*/
					boolean ecrasement = f_dest.exists();

					if (ecrasement)
						f_dest.delete(); // A faire - Suppression du fichier, il faut s'assurer de la suppression

					/*----- On crée le nouveau fichier à parir du fichier temporaire -----*/
					boolean copie = f_temp.renameTo(f_dest);

					while (!copie)
						{
						try	{
							Thread.sleep(1000);
							System.out.println("attendre"); // A faire - Qqchose pour passer l'attente car cela freeze l'IHM
							copie = f_temp.renameTo(f_dest);
							} catch (InterruptedException ex) {}
						}

					/*----- Mise à jour de la liste des fichiers si le fichier est nouveau -----*/
					if (!ecrasement)
						frIhm.l_fichiers_sortie.add(retour);

					/*----- Sélection du fichier enregistré -----*/
					cbListeSortie.setSelectedItem(retour);
					}
				else
					{
					/*----- Suppression du fichier temporaire -----*/
					f_temp.delete(); // A faire - Suppression du fichier, il faut s'assurer de la suppression
					}

				/*----- Pour ajuster la fenêtre à son contenu -----*/
				pack();

				/*----- Etat -----*/
				stepByStep = false;
				}
			});
		}


	/**
	 * Remise à zéro des affichages :
	 * - Numéro d'itération
	 * - Numéro de simulation
	 * - Temps écoulé
	 * - Temps estimé
	 * - Graphique des données projetées
	 */
	public void raz ()
		{
		this.setSimuIterEnCours(0, 0);
		this.setAffTemps(0, 0);
		this.jpGraph.setVisible(false);
		this.pack();
		}


	/**
	 * Mise à jour de l'affichage du numéro de l'itération en cours et
	 * de la simulation en cours.
	 */
	public void setSimuIterEnCours (final int nb_simu, final int nb_iter)
		{
		javax.swing.SwingUtilities.invokeLater (new Runnable()
			{
			public void run()
				{
				lblEnCours.setText(EPPLab.msg.getString("label.enCours") + "... " + nb_simu + " - " + nb_iter);
				}
			});
		}


	/**
	 * Mise à jour de l'affichage du temps passé et du temps estimé avant
	 * la fin des simulations.
	 */
	public void setAffTemps (final long tps_ecoule, final long tps_estime)
		{
		javax.swing.SwingUtilities.invokeLater (new Runnable()
			{
			public void run()
				{
				lblTemps.setText(EPPLab.msg.getString("label.temps") + "... " + StringUtils.convertHMS(tps_ecoule) +
								 " (" + StringUtils.convertHMS(tps_estime) + ")");
				}
			});
		}


	/**
	 * Mise à jour de l'affichage des données projetées
	 * sous la forme du graphique selectionné.
	 */
	public void setGraphique (final double[] d)
		{
		javax.swing.SwingUtilities.invokeLater (new Runnable()
			{
			public void run()
				{
				/**
				 * Estimateur à noyau.
				 */
				if (rbEstimateur.isSelected())
					{
					/*----- Crée et ajoute la fenêtre d'affichage de l'estimateur à noyau -----*/
					if (estimateurNoyau == null)
						{
						estimateurNoyau = new EstimateurNoyau(6,3,false,false,null);

						/*----- Mise à jour de l'estimateur à noyau -----*/
						estimateurNoyau.setProjection(d);
						estimateurNoyau.repaint();

						/*----- Ajoute l'estimateur à noyau à la fenêtre -----*/
						jpGraph.add(estimateurNoyau, 1);
						pack();
						}
					else
						{
						/*----- Mise à jour de l'estimateur à noyau -----*/
						estimateurNoyau.setProjection(d);
						estimateurNoyau.repaint();
						}
					}

				/**
				 * Histogramme.
				 */
				else
				if (rbHistogramme.isSelected())
					{
					/*----- Crée et ajoute la fenêtre d'affichage de l'histogramme -----*/
					if (histogramme == null)
						{
						histogramme = new Histogramme(6,3,false,false,null);
						histogramme.setFichierDonnees(parametre.ficherDeDonnees);

						/*----- Mise à jour de l'histogramme -----*/
						histogramme.setProjection(d);
						histogramme.repaint();

						/*----- Ajoute l'histogramme à la fenêtre -----*/
						jpGraph.add(histogramme, 1);
						pack();
						}
					else
						{
						/*----- Mise à jour de l'histogramme -----*/
						histogramme.setProjection(d);
						histogramme.repaint();
						}
					}

				/**
				 * Si le graphique est caché, on le rend visible.
				 */
				if (!jpGraph.isVisible())
					{
					jpGraph.setVisible(true);
					pack();
					}
				
				jpGraph.repaint();
				}
			});
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

		if (source == this.rbGKurtosisMin && this.rbGKurtosisMin.isSelected())			this.parametre.nomIndice = this.rbGKurtosisMin.getText();
		else if (source == this.rbGFriedman && this.rbGFriedman.isSelected())			this.parametre.nomIndice = this.rbGFriedman.getText();
		else if (source == this.rbGDiscriminant && this.rbGDiscriminant.isSelected())	this.parametre.nomIndice = this.rbGDiscriminant.getText();
		else if (source == this.rbKurtosisMax && this.rbKurtosisMax.isSelected())		this.parametre.nomIndice = this.rbKurtosisMax.getText();
		else if (source == this.rbFriedman && this.rbFriedman.isSelected())				this.parametre.nomIndice = this.rbFriedman.getText();
		else if (source == this.rbDiscriminant && this.rbDiscriminant.isSelected())		this.parametre.nomIndice = this.rbDiscriminant.getText();
		else if (source == this.rbFriedmanTukey && this.rbFriedmanTukey.isSelected())	this.parametre.nomIndice = this.rbFriedmanTukey.getText();
		else if (source == this.rbIndice4 && this.rbIndice4.isSelected())				this.parametre.nomIndice = this.rbIndice4.getText();
		else if (source == this.rbStahelDonoho && this.rbStahelDonoho.isSelected())		this.parametre.nomIndice = this.rbStahelDonoho.getText();
		else if (source == this.rbGA && this.rbGA.isSelected())
			{
			this.parametre.methode = ParametrePP.GA;
			this.lblIndividus.setText(EPPLab.msg.getString("label.individu"));
			this.lblIndividus.setEnabled(true);
			this.txtNbIndividus.setEnabled(true);

			if (this.parametre.ficherDeDonnees.startsWith("lubischev"))	{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(20); } else
			if (this.parametre.ficherDeDonnees.startsWith("don"))		{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(20); } else
			if (this.parametre.ficherDeDonnees.startsWith("fiab"))		{ this.txtNbIndividus.setValeur(200);	this.txtNbIterations.setValeur(50); } else
			if (this.parametre.ficherDeDonnees.startsWith("olive3"))	{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(20); } else
			if (this.parametre.ficherDeDonnees.startsWith("olive"))		{ this.txtNbIndividus.setValeur(100);	this.txtNbIterations.setValeur(50); } else
			/*----- Par défaut -----*/									{ this.txtNbIndividus.setValeur(100);	this.txtNbIterations.setValeur(50); }
			}
		else if (source == this.rbPSO && this.rbPSO.isSelected())	
			{
			this.parametre.methode = ParametrePP.PSO;
			this.lblIndividus.setText(EPPLab.msg.getString("label.particule"));
			this.lblIndividus.setEnabled(true);
			this.txtNbIndividus.setEnabled(true);

			if (this.parametre.ficherDeDonnees.startsWith("lubischev"))	{ this.txtNbIndividus.setValeur(20);	this.txtNbIterations.setValeur(50); } else
			if (this.parametre.ficherDeDonnees.startsWith("don"))		{ this.txtNbIndividus.setValeur(20);	this.txtNbIterations.setValeur(50); } else
			if (this.parametre.ficherDeDonnees.startsWith("fiab"))		{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(200); } else
			if (this.parametre.ficherDeDonnees.startsWith("olive3"))	{ this.txtNbIndividus.setValeur(20);	this.txtNbIterations.setValeur(50); } else
			if (this.parametre.ficherDeDonnees.startsWith("olive"))		{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(100); } else
			/*----- Par défaut -----*/									{ this.txtNbIndividus.setValeur(50);	this.txtNbIterations.setValeur(100); }
			}
		else if (source == this.rbTribes && this.rbTribes.isSelected())
			{
			this.parametre.methode = ParametrePP.TRIBES;
			this.lblIndividus.setText(EPPLab.msg.getString("label.particule"));
			this.lblIndividus.setEnabled(false);
			this.txtNbIndividus.setEnabled(false);

			this.txtNbIndividus.setValeur(1);
			this.txtNbIterations.setValeur(100);
			}
		else if (source == this.rbMultiStart && this.rbMultiStart.isSelected())
			{
			this.parametre.methode = ParametrePP.MULTISTART;
			this.lblIndividus.setEnabled(false);
			this.txtNbIndividus.setEnabled(false);

			this.txtNbIndividus.setValeur(1);
			this.txtNbIterations.setValeur(400);
			}
		else if (source == this.rbDOui && this.rbDOui.isSelected())		this.parametre.sphere = true;
		else if (source == this.rbDNon && this.rbDNon.isSelected())		this.parametre.sphere = false;

//		System.out.println(this.parametre);
		}

} /*----- Fin de la classe Recherche -----*/
