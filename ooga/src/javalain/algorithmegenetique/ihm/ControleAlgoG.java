package javalain.algorithmegenetique.ihm;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Locale;
import javalain.algorithmegenetique.EvolutionGA;
import javalain.algorithmegenetique.ParametreAG;
import javalain.algorithmegenetique.ihm.composant.JCheckBoxMenuItemEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javalain.algorithmegenetique.ihm.composant.JMenuEA;
import javalain.algorithmegenetique.ihm.composant.JMenuItemEA;
import javalain.algorithmegenetique.ihm.composant.JRadioButtonMenuItemEA;
import javalain.algorithmegenetique.ihm.composant.JTextFieldEA;
import javalain.algorithmegenetique.ihm.composant.TitledBorderEA;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import util.GuiUtils;


/*---------------*/
/* ControleAlgoG */
/*---------------*/
/**
 * La classe <code>ControleAlgoG</code> est une interface graphique de contrôle
 * de l'évolution et des paramètres d'un algorithme évolutionnaire.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 mai 2009
 */
public final class ControleAlgoG extends JFrame implements ActionListener, Runnable, WindowListener
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Bulles de présentation.
	 */
	private static final String MI_AIDE			= "Api";
	private static final String MI_TUTORIAL		= "Tutorial";

	private static final String BULLE_DEMARRE	= "Démarre une évolution";
	private static final String BULLE_TERMINE	= "Termine l'évolution en cours";
	private static final String BULLE_REPRISE	= "Reprend l'évolution";
	private static final String BULLE_PAUSE		= "Suspend l'évolution en cours";


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Données sur la fenêtre.
	 */
	private Container conteneur;


	/**
	 * Eléments du menu.
	 */
	private JMenuItemEA miDemarre;
	private JMenuItemEA miPause;

	private JMenuItemEA miEdition;

	private JRadioButtonMenuItemEA	rbmiGGA;
	private JRadioButtonMenuItemEA	rbmiSSGA;
	private JRadioButtonMenuItemEA	rbmiES;

	private JRadioButtonMenuItemEA	rbmiMOGA;
	private JRadioButtonMenuItemEA	rbmiNPGA;
	private JRadioButtonMenuItemEA	rbmiNSGA;
	private JRadioButtonMenuItemEA	rbmiVEGA;


	/**
	 * Panneau de contrôle.
	 */
	private PanneauModeEvolution modeEvolution;


	/**
	 * Zone d'affichage des courbes.
	 */
	private JPanel				jpCourbe;
	private JCheckBoxMenuItemEA	cbmiCourbe;
	private Courbe				courbe;


	/**
	 * Eléments pour l'affichage des statistiques (temps, notes, ...).
	 */
	private JTextFieldEA	txtNbInd	= null;	// Nombre d'individus
	private JTextFieldEA	txtNbGen	= null;	// Nombre de générations
	private JTextFieldEA	txtTpsMoy	= null;	// Temps moyen d'une génération
	private JTextFieldEA	txtTpsSel	= null;
	private JTextFieldEA	txtTpsCrois	= null;
	private JTextFieldEA	txtTpsMut	= null;
	private JTextFieldEA	txtTpsFit	= null;
	private JTextFieldEA	txtTpsAutre	= null;
	private JTextFieldEA	txtNoteMax	= null;
	private JTextFieldEA	txtNoteMoy	= null;


	/**
	 * Paramètres pour gèrer le temps de rafraîchissement des statistiques.
	 */
	private long		frequence;
	private boolean		freqPause;


	/**
	 * Données statistiques.
	 */
	private int			nbInd		= 0;
	private int			nbGen		= 0;
	private long		tpsSel		= 0;
	private long		tpsCrois	= 0;
	private long		tpsMut		= 0;
	private long		tpsFit		= 0;
	private long		tpsTotal	= 0;

	private double		noteMax;
	private double		noteMoy;


	/**
	 * <code>Evolution</code>s attachée.
	 */
	private EvolutionGA evolutionGA;


	/**
	 * Liste des classes à synchroniser avec l'IHM.
	 */
	@SuppressWarnings("CollectionWithoutInitialCapacity")
	private ArrayList<SynchroCtrlAG> listeSynchro = new ArrayList<SynchroCtrlAG>();


	/**
	 * Etat du fil d'exécution.
	 */
	private boolean	vivant;
	private boolean marche;
	private boolean	stop;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise la fenêtre de contrôle.
	 */
	public ControleAlgoG () { this(0,0); }

	@SuppressWarnings({"LeakingThisInConstructor", "CallToThreadStartDuringObjectConstruction"})
	public ControleAlgoG (int x,
						  int y)
		{
		/*----- Titre de la fenêtre -----*/
		super("Contrôle de l'évolution et des paramètres d'un algorithme génétique");

		/*----- Locale -----*/
		Locale.setDefault(Locale.FRANCE);

		/*----- Localisation de la fenêtre -----*/
		this.setLocation(x,y);
		this.setResizable(false);

		/*----- Création des menus -----*/
		JMenuBar				menu_bar = new JMenuBar();
		JMenuEA					menu, smenu;
		JMenuItemEA				menu_item;
		JRadioButtonMenuItemEA	rb_item;
		ButtonGroup				group;

		/*----- Menu de contrôle de l'évolution -----*/
		menu = new JMenuEA("Evolution");
		menu_bar.add(menu);

			this.miDemarre = new JMenuItemEA("Demarre");
			this.miDemarre.addActionListener(this);
			this.miDemarre.setEnabled(false);
			menu.add(this.miDemarre);

			this.miPause = new JMenuItemEA("Pause");
			this.miPause.addActionListener(this);
			this.miPause.setEnabled(false);
			menu.add(this.miPause);

			menu.addSeparator();

			menu_item = new JMenuItemEA("Quitter");
			menu_item.addActionListener(this);
			menu.add(menu_item);

		/*----- Menu choisir le type d'algorithme -----*/
		menu = new JMenuEA("Algorithme");
		menu_bar.add(menu);

			group = new ButtonGroup();

			menu_item = new JMenuItemEA("Méthode MonoObjectif");
			menu_item.setEnabled(true);
//			menu.add(menu_item);

			this.rbmiGGA = new JRadioButtonMenuItemEA(ParametreAG.GGA);
			this.rbmiGGA.setToolTipText("or Simple GA");
			this.rbmiGGA.setEnabled(false);
			this.rbmiGGA.addActionListener(this);
			group.add(this.rbmiGGA);
			menu.add(this.rbmiGGA);

			this.rbmiSSGA = new JRadioButtonMenuItemEA(ParametreAG.SSGA);
			this.rbmiSSGA.setEnabled(false);
			this.rbmiSSGA.addActionListener(this);
			group.add(this.rbmiSSGA);
			menu.add(this.rbmiSSGA);

			this.rbmiES = new JRadioButtonMenuItemEA(ParametreAG.ES);
			this.rbmiES.setEnabled(false);
			this.rbmiES.addActionListener(this);
			group.add(this.rbmiES);
//			menu.add(this.rbmiES);

			menu.addSeparator();

			menu_item = new JMenuItemEA("Méthode MultiObjectif");
			menu_item.setEnabled(true);
			menu.add(menu_item);

			this.rbmiMOGA = new JRadioButtonMenuItemEA(ParametreAG.MOGA);
			this.rbmiMOGA.setEnabled(false);
			this.rbmiMOGA.addActionListener(this);
			group.add(this.rbmiMOGA);
//			menu.add(this.rbmiMOGA);

			this.rbmiNPGA = new JRadioButtonMenuItemEA(ParametreAG.NPGA);
			this.rbmiNPGA.setEnabled(false);
			this.rbmiNPGA.addActionListener(this);
			group.add(this.rbmiNPGA);
//			menu.add(this.rbmiNPGA);

			this.rbmiNSGA = new JRadioButtonMenuItemEA(ParametreAG.NSGA_2);
			this.rbmiNSGA.setEnabled(true);
			this.rbmiNSGA.addActionListener(this);
			group.add(this.rbmiNSGA);
			menu.add(this.rbmiNSGA);

			this.rbmiVEGA = new JRadioButtonMenuItemEA(ParametreAG.VEGA);
			this.rbmiVEGA.setEnabled(false);
			this.rbmiVEGA.addActionListener(this);
			group.add(this.rbmiVEGA);
//			menu.add(this.rbmiVEGA);

		/*----- Menu pour l'affichage des paramètres et des courbes -----*/
		menu = new JMenuEA("Affichage");
		menu_bar.add(menu);

			this.miEdition = new JMenuItemEA("Edition");
			this.miEdition.addActionListener(this);
			this.miEdition.setEnabled(false);
			menu.add(this.miEdition);
			menu.addSeparator();

			smenu = new JMenuEA("Fréquence");
			menu.add(smenu);

				group = new ButtonGroup();

				rb_item = new JRadioButtonMenuItemEA("Haute");
				rb_item.setToolTipText("250 ms");
				rb_item.addActionListener(this);
				group.add(rb_item);
				smenu.add(rb_item);

				rb_item = new JRadioButtonMenuItemEA("Normale");
				rb_item.setToolTipText("500 ms");
				rb_item.addActionListener(this);
				group.add(rb_item);
				smenu.add(rb_item);

				rb_item.setSelected(true);
				this.frequence = 500;
				this.freqPause = false;

				rb_item = new JRadioButtonMenuItemEA("Basse");
				rb_item.setToolTipText("1000 ms");
				rb_item.addActionListener(this);
				group.add(rb_item);
				smenu.add(rb_item);

				rb_item = new JRadioButtonMenuItemEA("En pause");
				rb_item.addActionListener(this);
				group.add(rb_item);
				smenu.add(rb_item);

		menu.addSeparator();

		/*----- Courbe -----*/
		this.cbmiCourbe = new JCheckBoxMenuItemEA("Courbe");
		this.cbmiCourbe.setEnabled(false);
		this.cbmiCourbe.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				if	(cbmiCourbe.isSelected())
					conteneur.add(jpCourbe);
				else
					conteneur.remove(jpCourbe);
				pack();
				}
			});
		menu.add(this.cbmiCourbe);

		/*----- Menu d'aide -----*/
		menu = new JMenuEA("?");
//		menu_bar.add(menu);

			menu_item = new JMenuItemEA(ControleAlgoG.MI_AIDE);
			menu_item.addActionListener(this);
//			menu.add(menu_item);

			menu_item = new JMenuItemEA(ControleAlgoG.MI_TUTORIAL);
			menu_item.addActionListener(this);
//			menu.add(menu_item);

		/*----- Attachement de la barre de menu -----*/
		this.setJMenuBar(menu_bar);

		/*----- Conteneur de la fenêtre -----*/
		this.conteneur = this.getContentPane();
		this.conteneur.setLayout(new BoxLayout(conteneur,BoxLayout.Y_AXIS));

		/*----- Panneau des paramètres d'évolution -----*/
		this.modeEvolution	= new PanneauModeEvolution(this);

		/*----- Panneau du haut (paramétres + statistiques) -----*/
		JPanel jp_haut = new JPanel();
		jp_haut.setLayout(new BoxLayout(jp_haut,BoxLayout.X_AXIS));
		jp_haut.add(this.modeEvolution);

		/*----- Variables locales -----*/
		TitledBorderEA		tb;
		JLabel				lb;
		GridBagLayout		gb;
		GridBagConstraints	gbc;
		int					ligne;

		/*----- Panneau (statistiques) -----*/
		JPanel jp_stat = new JPanel();
		tb = new TitledBorderEA("Statistiques");
		jp_stat.setBorder(tb);

		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jp_stat.setLayout(gb);

		ligne = 0;

		/*----- Nombre d'individus -----*/
		lb = new JLabelEAPar("Nb d'individus");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtNbInd = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.txtNbInd,gbc);
		jp_stat.add(this.txtNbInd);
		ligne++;

		/*----- Génération -----*/
		lb = new JLabelEAPar("Génération n°");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtNbGen = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.txtNbGen,gbc);
		jp_stat.add(this.txtNbGen);
		ligne++;

		/*----- Temps moyen -----*/
		lb = new JLabelEAPar("Temps moyen");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsMoy = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.txtTpsMoy,gbc);
		jp_stat.add(this.txtTpsMoy);

		lb = new JLabelEAPar("ms");
		gbc.gridx = 2;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);
		ligne++;

		/*----- Note maximale -----*/
		lb = new JLabelEAPar("Note maximale");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtNoteMax = new JTextFieldEA(8);
		gbc.gridx = 1; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.txtNoteMax,gbc);
		jp_stat.add(this.txtNoteMax);
		ligne++;

		/*----- Note moyenne -----*/
		lb = new JLabelEAPar("Note moyenne");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtNoteMoy = new JTextFieldEA(8);
		gbc.gridx = 1; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(this.txtNoteMoy,gbc);
		jp_stat.add(this.txtNoteMoy);
		ligne++;

		/*----- Répartition CPU en % -----*/
		lb = new JLabelEAPres("Répartition CPU (%)");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);
		ligne++;

		/*----- Temps de sélection -----*/
		lb = new JLabelEAPar("Sélection");
		gbc.gridy = ligne; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST; gbc.insets = GuiUtils.INSETS_5500;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsSel = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtTpsSel,gbc);
		jp_stat.add(this.txtTpsSel);
		ligne++;

		/*----- Temps de croisement -----*/
		lb = new JLabelEAPar("Croisement");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsCrois = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtTpsCrois,gbc);
		jp_stat.add(this.txtTpsCrois);
		ligne++;

		/*----- Temps de mutation -----*/
		lb = new JLabelEAPar("Mutation");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsMut = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtTpsMut,gbc);
		jp_stat.add(this.txtTpsMut);
		ligne++;

		/*----- Temps de fitness -----*/
		lb = new JLabelEAPar("Notation");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsFit = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtTpsFit,gbc);
		jp_stat.add(this.txtTpsFit);
		ligne++;

		/*----- Temps sup -----*/
		lb = new JLabelEAPar("Autre");
		gbc.gridx = 0; gbc.gridy = ligne; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(lb,gbc);
		jp_stat.add(lb);

		this.txtTpsAutre = new JTextFieldEA(5);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtTpsAutre,gbc);
		jp_stat.add(this.txtTpsAutre);

		jp_haut.add(jp_stat);

		this.conteneur.add(jp_haut);

		/*----- Elément d'interface pour afficher une courbe -----*/
		tb = new TitledBorderEA("Courbe");
		tb.setTitlePosition(TitledBorder.ABOVE_TOP);
		this.jpCourbe = new JPanel();
		this.jpCourbe.setBorder(tb);
		this.jpCourbe.setLayout(new BoxLayout(this.jpCourbe,BoxLayout.Y_AXIS));

		this.courbe = new Courbe((int)jp_haut.getPreferredSize().getWidth()-11, 100);
		this.jpCourbe.add(this.courbe);

		/*----- Attachement du WindowListener -----*/
		this.addWindowListener(this);

		/*----- Pour ajuster la fenêtre à son contenu -----*/
		this.pack();

		/*----- Rend la fenetre visible -----*/
		this.setVisible(true);

		/*----- Tâche -----*/
		Thread th = new Thread(this);
		this.vivant = true;
		this.marche = false;
		this.stop = true;

		/*----- Démarrage du thread -----*/
		th.start();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Attache l'<code>Evolution</code> au panneau de contrôle.
	 */
	public void setEvolution (EvolutionGA e)
		{
		this.evolutionGA = e;

		/*----- Activation des éléments d'interface -----*/
		this.rbmiGGA.setEnabled(true);
		this.rbmiSSGA.setEnabled(true);
//		this.rbmiES.setEnabled(true);

		if	(this.evolutionGA.getPopulation().getParent().getNbObjectif() > 1)
			{
			this.rbmiMOGA.setEnabled(true);
			this.rbmiNPGA.setEnabled(true);
//			this.rbmiNSGA.setEnabled(true);
			this.rbmiVEGA.setEnabled(true);
			}

		this.miEdition.setEnabled(true);

		/*----- Mise à jour des paramètres -----*/
		this.updateEtatEvolution();
		this.updateParametreAG();
		}


	/**
	 * Retourne l'évolution.
	 */
	public EvolutionGA getEvolution () { return this.evolutionGA; }


	/**
	 * Mise à jour de l'affichage des paramètres de l'algorithme génétique.
	 * <p>
	 * Lorsque les paramètres de l'algorithme génétique sont mis à jour
	 * directement, c'est-à-dire sans utiliser l'interface, alors l'appel à
	 * cette méthode permet de répercuter la mise à jour sur l'interface.
	 */
	public void updateParametreAG ()
		{
		/*----- Type de l'algorithme -----*/
		String algo =  this.evolutionGA.getPopulation().getParametreAG().getAlgorithme();

		if	(algo.equals(ParametreAG.GGA))		{ this.rbmiGGA.setSelected(true);  this.cbmiCourbe.setEnabled(true); }
		if	(algo.equals(ParametreAG.SSGA))		{ this.rbmiSSGA.setSelected(true); this.cbmiCourbe.setEnabled(true); }
		if	(algo.equals(ParametreAG.ES))		{ this.rbmiES.setSelected(true);   this.cbmiCourbe.setEnabled(true); }

		if	(algo.equals(ParametreAG.MOGA))		{ this.rbmiMOGA.setSelected(true); this.cbmiCourbe.setSelected(false); this.cbmiCourbe.setEnabled(false); }
		if	(algo.equals(ParametreAG.NPGA))		{ this.rbmiNPGA.setSelected(true); this.cbmiCourbe.setSelected(false); this.cbmiCourbe.setEnabled(false); }
		if	(algo.equals(ParametreAG.NSGA_2))	{ this.rbmiNSGA.setSelected(true); this.cbmiCourbe.setSelected(false); this.cbmiCourbe.setEnabled(false); }
		if	(algo.equals(ParametreAG.VEGA))		{ this.rbmiVEGA.setSelected(true); this.cbmiCourbe.setSelected(false); this.cbmiCourbe.setEnabled(false); }

		this.modeEvolution.setAlgorithme(algo);
		}


	/**
	 * Mise à jour de l'affichage de l'état de l'évolution.
	 * <p>
	 * Lorsque l'état de l'évolution est mis à jour directement, c'est-à-dire
	 * sans utiliser l'interface, alors l'appel à cette méthode permet
	 * de répercuter la mise à jour sur l'interface.
	 */
	public void updateEtatEvolution ()
		{
		switch(this.evolutionGA.getEtat())
			{
			case EvolutionGA.EXECUTABLE :
				{
				this.miDemarre.setText("Demarre");
				this.miDemarre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
				this.miDemarre.setEnabled(true);
				this.miDemarre.setToolTipText(ControleAlgoG.BULLE_DEMARRE);
				this.miPause.setText("Pause");
				this.miPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
				this.miPause.setEnabled(false);
				this.miPause.setToolTipText(ControleAlgoG.BULLE_PAUSE);
				} break;

			case EvolutionGA.MARCHE :
				{
				this.miDemarre.setText("Termine");
				this.miDemarre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
				this.miDemarre.setEnabled(true);
				this.miDemarre.setToolTipText(ControleAlgoG.BULLE_TERMINE);
				this.miPause.setText("Pause");
				this.miPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
				this.miPause.setEnabled(true);
				this.miPause.setToolTipText(ControleAlgoG.BULLE_PAUSE);
				} break;

			case EvolutionGA.PAUSE :
				{
				this.miPause.setText("Reprise");
				this.miPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
				this.miPause.setEnabled(true);
				this.miPause.setToolTipText(ControleAlgoG.BULLE_REPRISE);
				} break;

			default :
			}
		}


	/**
	 * Modifie les statistiques de l'occupation CPU.
	 */
	public void setStatCpu (long s,
							long c,
							long m,
							long f,
							long t)
		{
		this.tpsSel		= s;
		this.tpsCrois	= c;
		this.tpsMut		= m;
		this.tpsFit		= f;
		this.tpsTotal	= t;
		}


	/**
	 * Modifie le nombre d'individus.
	 */
	public void setNbIndividu (int nb) { this.nbInd = nb; }


	/**
	 * Modifie le nombre de génération.
	 */
	public void setNbGeneration (int nb) { this.nbGen = nb; }


	/**
	 * Modifie la note maximale.
	 */
	public void setNoteMax (double d)
		{
		this.noteMax = d;
		this.courbe.addNombre((float)d);
		}


	/**
	 * Modifie la note moyenne.
	 */
	public void setNoteMoyenne (double d) { this.noteMoy = d; }


	/*--------------------------------------*/
	/* Définition de la méthode de Runnable */
	/*--------------------------------------*/

	/**
	 * Méthode d'exécution.
	 */
	@SuppressWarnings("SleepWhileHoldingLock")
	public void run ()
		{
		double d;

		while (this.vivant)
			{
			try {
				Thread.sleep(this.frequence);
				} catch (InterruptedException e) {}

			if	(this.marche && !this.freqPause && this.nbGen != 0)
				{
				this.txtNbInd.setText(Integer.toString(this.nbInd));

				this.txtNbGen.setText(Integer.toString(this.nbGen));

				this.txtTpsMoy.setText(Long.toString(this.tpsTotal/this.nbGen));

				d = 100.0/this.tpsTotal;
				this.txtTpsSel.setText(GuiUtils.POURCENTAGE.format(this.tpsSel*d));
				this.txtTpsCrois.setText(GuiUtils.POURCENTAGE.format(this.tpsCrois*d));
				this.txtTpsMut.setText(GuiUtils.POURCENTAGE.format(this.tpsMut*d));
				this.txtTpsFit.setText(GuiUtils.POURCENTAGE.format(this.tpsFit*d));
				this.txtTpsAutre.setText(GuiUtils.POURCENTAGE.format((this.tpsTotal-this.tpsSel-this.tpsCrois-this.tpsMut-this.tpsFit)*d));

//				this.txtTpsSel.setText(POURCENTAGE.format(this.tpsSel*100.0/this.tpsTotal));
//				this.txtTpsCrois.setText(POURCENTAGE.format(this.tpsCrois*100.0/this.tpsTotal));
//				this.txtTpsMut.setText(POURCENTAGE.format(this.tpsMut*100.0/this.tpsTotal));
//				this.txtTpsFit.setText(POURCENTAGE.format(this.tpsFit*100.0/this.tpsTotal));
//				this.txtTpsAutre.setText(POURCENTAGE.format((this.tpsTotal-this.tpsSel-this.tpsCrois-this.tpsMut-this.tpsFit)*100.0/this.tpsTotal));

				this.txtNoteMax.setText(GuiUtils.SCIENTIFIQUE.format(noteMax));
				this.txtNoteMoy.setText(GuiUtils.SCIENTIFIQUE.format(noteMoy));

				this.courbe.repaint();

				/*----- Synchronisation -----*/
				for	(int i=0 ; i<this.listeSynchro.size() ; i++) this.listeSynchro.get(i).synchroniseCtrlAG();

				if	(this.stop) this.marche = false;
				}
			}
		}


	/**
	 * Demarre le processus de mise à jour.
	 */
	public void demarre ()
		{
		this.stop = false;
		this.marche = true;
		this.courbe.raz();
		}


	/**
	 * Termine la mise à jour.
	 */
	public void termine () { this.stop = true; }


	/**
	 * Stoppe définitivement le processus de mise à jour.
	 */
	public void stop ()
		{
		this.vivant = false;

		this.miDemarre.setText("Demarre");
		this.miDemarre.setEnabled(false);
		this.miDemarre.setToolTipText(null);
		this.miPause.setText("Pause");
		this.miPause.setEnabled(false);
		this.miPause.setToolTipText(null);
		}


	/**
	 * Ajoute une classe à synchroniser avec l'IHM.
	 */
	public void addSynchroCtrlAG (SynchroCtrlAG scag) { this.listeSynchro.add(scag); }


	/*-------------------------------------------*/
	/* Définition des méthodes de ActionListener */
	/*-------------------------------------------*/

	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void actionPerformed (ActionEvent e)
		{
		String cde = e.getActionCommand();

		if	(cde == "Demarre")	this.evolutionGA.setEtat(EvolutionGA.DEMARRE);
		if	(cde == "Termine")	this.evolutionGA.setEtat(EvolutionGA.TERMINE);
		if	(cde == "Reprise")	this.evolutionGA.setEtat(EvolutionGA.MARCHE);
		if	(cde == "Pause")	this.evolutionGA.setEtat(EvolutionGA.PAUSE);

		if	(cde.equals(ParametreAG.GGA) || cde.equals(ParametreAG.SSGA) || cde.equals(ParametreAG.ES))
			{
			this.cbmiCourbe.setEnabled(true);
			this.getEvolution().getPopulation().getParametreAG().setAlgorithme(e.getActionCommand());
			this.modeEvolution.setAlgorithme(cde);
			}

		if	(cde.equals(ParametreAG.MOGA) || cde.equals(ParametreAG.NPGA) ||cde.equals(ParametreAG.NSGA_2) || cde.equals(ParametreAG.VEGA))
			{
			this.cbmiCourbe.setSelected(false);
			this.cbmiCourbe.setEnabled(false);
			this.getEvolution().getPopulation().getParametreAG().setAlgorithme(e.getActionCommand());
			this.modeEvolution.setAlgorithme(cde);
			}

		if	(cde == "Haute")	{ this.freqPause = false ; this.frequence = 250; }
		if	(cde == "Normale")	{ this.freqPause = false ; this.frequence = 500; }
		if	(cde == "Basse")	{ this.freqPause = false ; this.frequence = 1000; }
		if	(cde == "En pause")	{ this.freqPause = true  ; this.frequence = 250; }

		if	(cde == "Quitter")					System.exit(0);
		if	(cde == "Edition")					System.out.print(this.evolutionGA.getPopulation().getParametreAG());
		if	(cde == ControleAlgoG.MI_TUTORIAL)	JOptionPane.showMessageDialog(this,"A faire !","Tutorial",JOptionPane.INFORMATION_MESSAGE);
		if	(cde == ControleAlgoG.MI_AIDE)		try { new Aide(); } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Aide introuvable !","Erreur",JOptionPane.ERROR_MESSAGE); }

//		System.out.print(this.evolution.getPopulation().getParametreAG());
		}


	/*-------------------------------------------*/
	/* Définition des méthodes de WindowListener */
	/*-------------------------------------------*/

	public void windowActivated(WindowEvent e)		{ }
	public void windowClosed(WindowEvent e)			{ }
	public void windowClosing(WindowEvent e)		{ System.exit(0);}
	public void windowDeactivated(WindowEvent e)	{ }
	public void windowDeiconified(WindowEvent e)	{ }
	public void windowIconified(WindowEvent e)		{ }
	public void windowOpened(WindowEvent e)			{ }

} /*----- Fin de la classe ControleAlgoG -----*/
