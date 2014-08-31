package javalain.algorithmegenetique.ihm;

import javalain.algorithmegenetique.ParametreAG;
import javalain.algorithmegenetique.ihm.composant.JCheckBoxEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javalain.algorithmegenetique.ihm.composant.JRadioButtonEA;
import javalain.algorithmegenetique.ihm.composant.JTextFieldEA;
import javalain.algorithmegenetique.ihm.composant.TitledBorderEA;
import javalain.algorithmegenetique.ihm.controle.ListeNbPartTournoi;
import javalain.algorithmegenetique.ihm.controle.SliderCroisement;
import javalain.algorithmegenetique.ihm.controle.SliderMutation;
import javalain.algorithmegenetique.ihm.controle.TexteAlpha;
import javalain.algorithmegenetique.ihm.controle.TexteFrontiere;
import javalain.algorithmegenetique.ihm.controle.TexteNbMeilleur;
import javalain.algorithmegenetique.ihm.controle.TexteNbOverlap;
import javalain.algorithmegenetique.ihm.controle.TexteTdom;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import util.GuiUtils;


/*----------------------*/
/* PanneauModeEvolution */
/*----------------------*/
/**
 * La classe <code>PanneauModeEvolution</code> est une interface graphique de
 * contrôle des paramètres d'un algorithme évolutionnaire.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	29 avril 2009
 */
public class PanneauModeEvolution extends JPanel implements ActionListener
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Eléments d'affichage.
	 */
	private JPanel				jpAlgorithme;
	private TitledBorderEA		tbAlgorithme;

	private JLabelEAPres		lblSelection;
	private JRadioButtonEA		rbRang;
	private JRadioButtonEA		rbRoulette;
	private JRadioButtonEA		rbResteStochastique;
	private JRadioButtonEA		rbTournoi;
//	private String[]			listeNbPartTournoi = {"2","3","4","5","6","7"};
//	private JComboBoxEA			cbNbPartTournoi = new JComboBoxEA(this.listeNbPartTournoi);
//	private int					itemListe;

	private ListeNbPartTournoi	cbNbPartTournoi1;

	private JPanel				jpTournoi;
	private JPanel				jpTournoiSimple;
	private JPanel				jpTournoiPareto;

	private TexteTdom			textTdom;

	private JLabelEAPres		lblDiversite;
	private JCheckBoxEA			cbScaling;
	private JCheckBoxEA			cbSharing;
	private JCheckBoxEA			cbCrowding;
	private JCheckBoxEA			cbCroisRestreint;

	private JLabelEAPres		lblElitisme;
	private JCheckBoxEA			cbElitisme;

	private JLabelEAPres		lblRemplacement;
	private JPanel				jpRemplacement;
	private JPanel				jpWithoutOverlap;
	private JPanel				jpWithOverlap;
	private JRadioButtonEA		rbOverRandom;
	private JRadioButtonEA		rbOverWorst;
	private JRadioButtonEA		rbOverTournoi;
	private JRadioButtonEA		rbOverOld;
	private JRadioButtonEA		rbOverCustom;
	private JLabelEAPar			lblWhitoutOver;

	private JLabelEAPres		lblTauxMut;
	private JLabelEAPres		lblTauxCrois;
	private SliderMutation		sliderMut;
	private SliderCroisement	sliderCrois;
	private JTextFieldEA		txtMut;
	private JTextFieldEA		txtCrois;

	private JLabelEAPres		lblParamVoisin;
	private JLabelEAPar			lblAlpha;
	private JLabelEAPar			lblFrontiere;

	private TexteAlpha			textAlpha;
	private TexteFrontiere		textFrontiere;

	private JLabelEAPres		lblOverlap;
	private JLabelEAPar			lblNbOverlap;
	private TexteNbOverlap		textNbOverlap;

	private JLabelEAPres		lblElitisme2;
	private JLabelEAPar			lblNbMeilleurs;
	private TexteNbMeilleur		textNbMeilleurs;
	private JLabelEAPar			lblNbMeilleurs2;


	/**
	 * Panneau de contrôle général.
	 */
	private ControleAlgoG ctrlAG;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	@SuppressWarnings("LeakingThisInConstructor")
	public PanneauModeEvolution (ControleAlgoG c)
		{
		this.ctrlAG = c;

		/*----- Gestionnaire de disposition -----*/
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

		/*----- Variables locales -----*/
		JRadioButtonEA		rb;
		TitledBorderEA		tb;
		GridBagLayout		gb;
		GridBagConstraints	gbc;
		ButtonGroup			group;
		int					ligne;

		/*----- Panneau (algorithme) -----*/
		this.jpAlgorithme = new JPanel();
		this.tbAlgorithme = new TitledBorderEA("Algorithme");
		this.jpAlgorithme.setBorder(this.tbAlgorithme);

		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.jpAlgorithme.setLayout(gb);

		ligne = 0;

		/*----- Sélection -----*/
		this.lblSelection = new JLabelEAPres("Sélection");
		this.lblSelection.setEnabled(false);
		gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.lblSelection,gbc);
		this.jpAlgorithme.add(this.lblSelection);
		ligne++;

		group = new ButtonGroup();

		/*----- Rang -----*/
		this.rbRang = new JRadioButtonEA("Rang");
		this.rbRang.setEnabled(false);
		this.rbRang.addActionListener(this);
		group.add(this.rbRang);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.rbRang,gbc);
		this.jpAlgorithme.add(this.rbRang);
		ligne++;

		/*----- Roulette pipée -----*/
		this.rbRoulette = new JRadioButtonEA("Roulette pipée");
		this.rbRoulette.setEnabled(false);
		this.rbRoulette.addActionListener(this);
		group.add(this.rbRoulette);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbRoulette,gbc);
		this.jpAlgorithme.add(this.rbRoulette);
		ligne++;

		/*----- Reste stochastique -----*/
		this.rbResteStochastique = new JRadioButtonEA("Reste stochastique");
		this.rbResteStochastique.setEnabled(false);
		this.rbResteStochastique.addActionListener(this);
		group.add(this.rbResteStochastique);
		gbc.gridy = ligne;
		gb.setConstraints(this.rbResteStochastique,gbc);
		this.jpAlgorithme.add(this.rbResteStochastique);
		ligne++;

		/*----- Tournoi -----*/
		this.jpTournoi = new JPanel(GuiUtils.FL_LEFT_00);
		gbc.gridy = ligne;
		gb.setConstraints(this.jpTournoi,gbc);

		/*----- Tournoi simple -----*/
		this.jpTournoiSimple = new JPanel(GuiUtils.FL_LEFT_00);
		this.jpTournoiSimple.setVisible(true);

		this.rbTournoi = new JRadioButtonEA("Tournoi");
		this.rbTournoi.setEnabled(false);
		this.rbTournoi.addActionListener(this);
		group.add(this.rbTournoi);

		this.rbTournoi.addItemListener(new ItemListener()
			{
			//----- Changement de valeur : true/false.
			public void itemStateChanged (ItemEvent e)
				{
				if	(rbTournoi.isSelected() && rbTournoi.isEnabled())
//					cbNbPartTournoi.setEnabled(true);
					cbNbPartTournoi1.setEnabled(true);
				else
//					cbNbPartTournoi.setEnabled(false);
					cbNbPartTournoi1.setEnabled(false);
				}
			});

		this.jpTournoiSimple.add(this.rbTournoi);

//		this.itemListe = 0;
//		this.cbNbPartTournoi.setSelectedIndex(this.itemListe);
//		this.cbNbPartTournoi.setToolTipText("Nombre de participants");
//		this.cbNbPartTournoi.setEditable(true);
//		this.cbNbPartTournoi.setEnabled(false);
//		this.cbNbPartTournoi.addActionListener(this);
//		this.cbNbPartTournoi.setPreferredSize(new Dimension(45,21));

//		this.jpTournoiSimple.add(this.cbNbPartTournoi);

		this.cbNbPartTournoi1 = new ListeNbPartTournoi(this.ctrlAG);
		this.cbNbPartTournoi1.setEnabled(false);

		this.jpTournoiSimple.add(this.cbNbPartTournoi1);

		// A voir - Pourquoi ne pas mettre un spinner ?

//		JSpinner js = new JSpinner(new SpinnerNumberModel(3,1,10,2));
//		js.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		js.setBorder(BorderFactory.createLineBorder(new Color(122,138,153)));
//		System.out.println(js.getBorder().getClass().getName());
//		js.setPreferredSize(new Dimension(45,21));
//		this.jpTournoiSimple.add(js);

		this.jpTournoi.add(this.jpTournoiSimple);

		/*----- Tournoi Pareto -----*/
		this.jpTournoiPareto = new JPanel(GuiUtils.FL_LEFT_00);
		this.jpTournoiPareto.setVisible(false);

		rb = new JRadioButtonEA("<html>T<sub>dom</sub></html>");
		rb.setToolTipText("Tournoi de dominance");
		rb.setSelected(true);
		rb.addActionListener(new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				((JRadioButtonEA)e.getSource()).setSelected(true);
				}
			});
		rb.setMargin(new Insets(0,2,0,2));

		this.jpTournoiPareto.add(rb);

		this.textTdom = new TexteTdom(this.ctrlAG);

		this.jpTournoiPareto.add(this.textTdom);

		this.jpTournoi.add(this.jpTournoiPareto);

		this.jpAlgorithme.add(this.jpTournoi);
		ligne++;

		/*----- Maintien de la diversité -----*/
		this.lblDiversite = new JLabelEAPres("Diversité");
		this.lblDiversite.setEnabled(false);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblDiversite,gbc);
		this.jpAlgorithme.add(this.lblDiversite);
		ligne++;

		/*----- Scaling -----*/
		this.cbScaling = new JCheckBoxEA("Scaling");
		this.cbScaling.setToolTipText("Mise à l'échelle des notes");
		this.cbScaling.setEnabled(false);
		this.cbScaling.addActionListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.cbScaling,gbc);
		this.jpAlgorithme.add(this.cbScaling);
		ligne++;

		/*----- Sharing -----*/
		this.cbSharing = new JCheckBoxEA("Sharing");
		this.cbSharing.setToolTipText("Heuristique de partage");
		this.cbSharing.setEnabled(false);
		this.cbSharing.addActionListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.cbSharing,gbc);

		this.cbSharing.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				//----- Changement de valeur : true/false.
				if	(cbSharing.isSelected())
					{
					lblParamVoisin.setEnabled(true);
					lblAlpha.setEnabled(true);
					textAlpha.setEnabled(true);
					lblFrontiere.setEnabled(true);
					textFrontiere.setEnabled(true);
					}
				else
					{
					lblParamVoisin.setEnabled(false);
					lblAlpha.setEnabled(false);
					textAlpha.setEnabled(false);
					lblFrontiere.setEnabled(false);
					textFrontiere.setEnabled(false);
					}
				}
			});

		this.jpAlgorithme.add(this.cbSharing);
		ligne++;

		/*----- Crowding -----*/
		this.cbCrowding = new JCheckBoxEA("Crowding");
		this.cbCrowding.setToolTipText("Heuristique de remplacement");
		this.cbCrowding.setEnabled(false);
		this.cbCrowding.addActionListener(this);
		gbc.gridy = ligne;
		gb.setConstraints(this.cbCrowding,gbc);
		this.jpAlgorithme.add(this.cbCrowding);
		ligne++;

		/*----- Croisement restreint -----*/
		this.cbCroisRestreint = new JCheckBoxEA("Croisement restreint");
		this.cbCroisRestreint.setEnabled(false);
		this.cbCroisRestreint.addActionListener(this);
		gbc.gridy = ligne;
//		gbc.weighty = 1.0; gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gb.setConstraints(this.cbCroisRestreint,gbc);
		this.jpAlgorithme.add(this.cbCroisRestreint);
		ligne++;

		/*----- Zone d'espacement -----*/
		Component cc = Box.createVerticalStrut(54);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(cc,gbc);
		this.jpAlgorithme.add(cc);

		ligne = 0;

		/*----- Elitisme -----*/
		this.lblElitisme = new JLabelEAPres("Elitisme");
		this.lblElitisme.setEnabled(false);
		gbc.gridx = 1; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(this.lblElitisme,gbc);
		ligne++;
		this.jpAlgorithme.add(this.lblElitisme);

		/*----- Copier le meilleur -----*/
		this.cbElitisme = new JCheckBoxEA("Copie des meilleurs");
		this.cbElitisme.setEnabled(false);
		this.cbElitisme.addActionListener(this);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500;
		gb.setConstraints(this.cbElitisme,gbc);

		this.cbElitisme.addItemListener(new ItemListener()
			{
			public void itemStateChanged (ItemEvent e)
				{
				//----- Changement de valeur : true/false.
				if	(cbElitisme.isSelected())
					{
//					lblElitisme2.setEnabled(true);
//					lblNbMeilleurs.setEnabled(true);
//					textNbMeilleurs.setEnabled(true);
//					lblNbMeilleurs2.setEnabled(true);
					}
				else
					{
					lblElitisme2.setEnabled(false);
					lblNbMeilleurs.setEnabled(false);
					textNbMeilleurs.setEnabled(false);
					lblNbMeilleurs2.setEnabled(false);
					}
				}
			});

		this.jpAlgorithme.add(this.cbElitisme);
		ligne++;

		/*----- Remplacement -----*/
		this.lblRemplacement = new JLabelEAPres("Remplacement");
		this.lblRemplacement.setEnabled(false);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblRemplacement,gbc);
		this.jpAlgorithme.add(this.lblRemplacement);
		ligne++;

		this.jpRemplacement = new JPanel(GuiUtils.FL_LEFT_00);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0500; gbc.gridheight = GridBagConstraints.REMAINDER; gbc.fill = GridBagConstraints.VERTICAL;
		gb.setConstraints(this.jpRemplacement,gbc);
		this.jpAlgorithme.add(this.jpRemplacement);

		/*----- Panneau de non chevauchement -----*/
		this.jpWithoutOverlap = new JPanel(new FlowLayout(FlowLayout.LEFT,4,0));
		this.jpWithoutOverlap.setVisible(false);

		/*----- Etiquette de non chevauchement -----*/
		this.lblWhitoutOver = new JLabelEAPar("<html>Les enfants générés<br>remplacent les parents<br>pour former la<br>génération suivante.</html>");
		this.jpWithoutOverlap.add(this.lblWhitoutOver);

		this.jpRemplacement.add(this.jpWithoutOverlap);
		this.jpRemplacement.setPreferredSize(this.jpWithoutOverlap.getPreferredSize());

		/*----- Panneau de chevauchement -----*/
		this.jpWithOverlap = new JPanel(new GridLayout(5,1,0,0));
		this.jpWithOverlap.setVisible(false);
		this.jpWithOverlap.addComponentListener(new ComponentListener()
			{
			public void componentResized (ComponentEvent e) { }

			public void componentMoved (ComponentEvent e) { }

			public void componentShown (ComponentEvent e)
				{
				lblOverlap.setEnabled(true);
				lblNbOverlap.setEnabled(true);
				textNbOverlap.setEnabled(true);
				}

			public void componentHidden (ComponentEvent e)
				{
				lblOverlap.setEnabled(false);
				lblNbOverlap.setEnabled(false);
				textNbOverlap.setEnabled(false);
				}
			});

		group = new ButtonGroup();

		/*----- Remplacement aléatoire -----*/
		this.rbOverRandom = new JRadioButtonEA("Aléatoire");
		this.rbOverRandom.addActionListener(this);
		group.add(this.rbOverRandom);
		this.jpWithOverlap.add(this.rbOverRandom);

		/*----- Les plus mauvais -----*/
		this.rbOverWorst = new JRadioButtonEA("Les plus mauvais");
		this.rbOverWorst.addActionListener(this);
		group.add(this.rbOverWorst);
		this.jpWithOverlap.add(this.rbOverWorst);

		/*----- Remplacement par tournoi inversé -----*/
		this.rbOverTournoi = new JRadioButtonEA("Tournoi inversé");
		this.rbOverTournoi.addActionListener(this);
		group.add(this.rbOverTournoi);
		this.jpWithOverlap.add(this.rbOverTournoi);

		/*----- Les plus vieux -----*/
		this.rbOverOld = new JRadioButtonEA("Les plus vieux");
		this.rbOverOld.setEnabled(false);
		this.rbOverOld.addActionListener(this);
		group.add(this.rbOverOld);
		this.jpWithOverlap.add(this.rbOverOld);

		/*----- Personnel -----*/
		this.rbOverCustom = new JRadioButtonEA("Utilisateur");
		this.rbOverCustom.setEnabled(false);
		this.rbOverCustom.addActionListener(this);
		this.jpWithOverlap.add(this.rbOverOld);
		group.add(this.rbOverCustom);
		this.jpWithOverlap.add(this.rbOverCustom);

		this.jpRemplacement.add(this.jpWithOverlap);

		this.add(this.jpAlgorithme);

		/*----- Panneau (paramètres) -----*/
		JPanel jpParam = new JPanel();
		tb = new TitledBorderEA("Paramètres");
		jpParam.setBorder(tb);

		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jpParam.setLayout(gb);

		ligne = 0;

		/*----- Taux de mutation -----*/
		this.lblTauxMut = new JLabelEAPres("Taux de mutation");
		this.lblTauxMut.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_0000; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.lblTauxMut,gbc);
		jpParam.add(this.lblTauxMut);
		ligne++;

		this.txtMut = new JTextFieldEA(3);
		this.txtMut.setEnabled(false);
		this.sliderMut = new SliderMutation(this.ctrlAG,this.txtMut);
		gbc.gridy = ligne; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.sliderMut,gbc);
		jpParam.add(this.sliderMut);

		gbc.gridx = 2; gbc.gridy = ligne; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtMut,gbc);
		jpParam.add(this.txtMut);
		ligne++;

		/*----- Taux de croisement -----*/
		this.lblTauxCrois = new JLabelEAPres("Taux de croisement");
		this.lblTauxCrois.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblTauxCrois,gbc);
		jpParam.add(this.lblTauxCrois);
		ligne++;

		this.txtCrois = new JTextFieldEA(3);
		this.txtCrois.setEnabled(false);
		this.sliderCrois = new SliderCroisement(this.ctrlAG,this.txtCrois);
		gbc.gridy = ligne; gbc.gridwidth = 2; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.sliderCrois,gbc);
		jpParam.add(this.sliderCrois);

		gbc.gridx = 2; gbc.gridy = ligne; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.txtCrois,gbc);
		jpParam.add(this.txtCrois);
		ligne++;

		/*----- Paramètres de voisinage -----*/
		this.lblParamVoisin = new JLabelEAPres("Paramètres de voisinage");
		this.lblParamVoisin.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblParamVoisin,gbc);
		jpParam.add(this.lblParamVoisin);
		ligne++;

		/*----- Alpha -----*/
		this.lblAlpha = new JLabelEAPar("Alpha");
		this.lblAlpha.setEnabled(false);
		gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.lblAlpha,gbc);
		jpParam.add(this.lblAlpha);

		this.textAlpha = new TexteAlpha(this.ctrlAG);
		this.textAlpha.setEnabled(false);
		gbc.gridx = 1; gbc.gridy = ligne; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.textAlpha,gbc);
		jpParam.add(this.textAlpha);
		ligne++;

		/*----- Frontière -----*/
		this.lblFrontiere = new JLabelEAPar("<html>Sigma<sub>share</sub></html>");
		this.lblFrontiere.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.lblFrontiere,gbc);

		this.lblFrontiere.addPropertyChangeListener(new PropertyChangeListener()
			{
			public void propertyChange (PropertyChangeEvent evt)
				{
				if	(lblFrontiere.isEnabled())
					lblFrontiere.setForeground(Color.black);
				else
					lblFrontiere.setForeground(GuiUtils.GRIS_153);
				}
			});

		jpParam.add(this.lblFrontiere);

		this.textFrontiere = new TexteFrontiere(this.ctrlAG);
		this.textFrontiere.setEnabled(false);
		gbc.gridx = 1; gbc.gridy = ligne; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.textFrontiere,gbc);
		jpParam.add(this.textFrontiere);
		ligne++;

		/*----- Paramètre de chavauchement -----*/
		this.lblOverlap = new JLabelEAPres("Remplacement");
		this.lblOverlap.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblOverlap,gbc);
		jpParam.add(this.lblOverlap);
		ligne++;

		this.lblNbOverlap = new JLabelEAPar("Taille de p''");
		this.lblNbOverlap.setEnabled(false);
		gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.lblNbOverlap,gbc);
		jpParam.add(this.lblNbOverlap);

		this.textNbOverlap = new TexteNbOverlap(this.ctrlAG);
		this.textNbOverlap.setEnabled(false);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.textNbOverlap,gbc);
		jpParam.add(this.textNbOverlap);
		ligne++;

		/*----- Paramètre d'élitisme -----*/
		this.lblElitisme2 = new JLabelEAPres("Elitisme");
		this.lblElitisme2.setEnabled(false);
		gbc.gridx = 0; gbc.gridy = ligne; gbc.gridwidth = 3; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblElitisme2,gbc);
		jpParam.add(this.lblElitisme2);
		ligne++;

		this.lblNbMeilleurs = new JLabelEAPar("Copie les");
		this.lblNbMeilleurs.setEnabled(false);
		gbc.gridy = ligne; gbc.gridwidth = 1; gbc.insets = GuiUtils.INSETS_5500; gbc.anchor = GridBagConstraints.EAST;
		gb.setConstraints(this.lblNbMeilleurs,gbc);
		jpParam.add(this.lblNbMeilleurs);

		this.textNbMeilleurs = new TexteNbMeilleur(this.ctrlAG);
		this.textNbMeilleurs.setEnabled(false);
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
		gb.setConstraints(this.textNbMeilleurs,gbc);
		jpParam.add(this.textNbMeilleurs);

		this.lblNbMeilleurs2 = new JLabelEAPar("meilleurs");
		this.lblNbMeilleurs2.setEnabled(false);
		gbc.gridx = 2; gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_5000;
		gb.setConstraints(this.lblNbMeilleurs2,gbc);
		jpParam.add(this.lblNbMeilleurs2);
		ligne++;

		/*----- Zone d'espacement -----*/
		cc = Box.createVerticalStrut(25);
		gbc.gridy = ligne; gbc.insets = GuiUtils.INSETS_0000;
		gb.setConstraints(cc,gbc);
		jpParam.add(cc);

		this.add(jpParam);
		} /*----- Fin du constructeur -----*/


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie l'affichage de l'IHM en fonction du type d'algorithme
	 * évolutionnaire utilisé.
	 */
	public void setAlgorithme (String s)
		{
		if	(s != this.tbAlgorithme.getTitle())
			{
			this.tbAlgorithme.setTitle(s);
			this.jpAlgorithme.repaint();

			/*----- GGA -----*/
			if	(s.equals(ParametreAG.GGA))
				{
				this.lblSelection.setEnabled(true);
				this.rbRang.setEnabled(true);
				this.rbRoulette.setEnabled(true);
				this.rbResteStochastique.setEnabled(true);
				this.rbTournoi.setEnabled(true);

				this.jpTournoiPareto.setVisible(false);
				this.jpTournoiSimple.setVisible(true);

				this.lblDiversite.setEnabled(true);
				this.cbScaling.setEnabled(true);
				this.cbSharing.setEnabled(true);
	//			this.cbCrowding.setEnabled(true);
				this.cbCroisRestreint.setEnabled(true);

				this.lblElitisme.setEnabled(true);
				this.cbElitisme.setEnabled(true);

				this.lblRemplacement.setEnabled(true);
				this.jpWithOverlap.setVisible(false);
				this.jpWithoutOverlap.setVisible(true);

				this.lblTauxMut.setEnabled(true);
				this.sliderMut.setEnabled(true);
				this.txtMut.setEnabled(true);

				this.lblTauxCrois.setEnabled(true);
				this.sliderCrois.setEnabled(true);
				this.txtCrois.setEnabled(true);
				}
			else
			/*----- SSGA -----*/
			if	(s.equals(ParametreAG.SSGA))
				{
				this.lblSelection.setEnabled(true);
				this.rbRang.setEnabled(true);
				this.rbRoulette.setEnabled(true);
				this.rbResteStochastique.setEnabled(true);
				this.rbTournoi.setEnabled(true);

				this.jpTournoiPareto.setVisible(false);
				this.jpTournoiSimple.setVisible(true);

				this.lblDiversite.setEnabled(true);
				this.cbScaling.setEnabled(false);
				this.cbSharing.setEnabled(true);
	//			this.cbCrowding.setEnabled(true);
				this.cbCroisRestreint.setEnabled(true);

				this.lblElitisme.setEnabled(false);
				this.cbElitisme.setEnabled(false);

				this.lblRemplacement.setEnabled(true);
				this.jpWithoutOverlap.setVisible(false);
				this.jpWithOverlap.setVisible(true);

				/*----- Sélection de l'heuristique -----*/
				switch (this.ctrlAG.getEvolution().getPopulation().getParametreAG().getRemplacement())
					{
					case ParametreAG.ALEATOIRE					: this.rbOverRandom.setSelected(true);	break;
					case ParametreAG.TOURNOI_INVERSE			: this.rbOverTournoi.setSelected(true);	break;
					case ParametreAG.LES_PLUS_VIEUX				: this.rbOverOld.setSelected(true);		break;
					case ParametreAG.PERSO						: this.rbOverCustom.setSelected(true);	break;
					default /*----- LES_PLUS_MAUVAIS -----*/	: this.rbOverWorst.setSelected(true);
					}

				this.lblTauxMut.setEnabled(true);
				this.sliderMut.setEnabled(true);
				this.txtMut.setEnabled(true);

				this.lblTauxCrois.setEnabled(false);
				this.sliderCrois.setEnabled(false);
				this.txtCrois.setEnabled(false);
				}
			else
			/*----- ES -----*/
			if	(s.equals(ParametreAG.ES))
				{
				}
			else
			/*----- MOGA -----*/
			if	(s.equals(ParametreAG.MOGA))
				{
				this.lblSelection.setEnabled(true);
				this.rbRang.setEnabled(true);
				this.rbRoulette.setEnabled(false);
				this.rbResteStochastique.setEnabled(false);
				this.rbTournoi.setEnabled(false);

				this.jpTournoiPareto.setVisible(false);
				this.jpTournoiSimple.setVisible(true);

				this.lblDiversite.setEnabled(true);
				this.cbScaling.setEnabled(true);
				this.cbSharing.setEnabled(true);
	//			this.cbCrowding.setEnabled(false);
				this.cbCroisRestreint.setEnabled(true);

				this.lblElitisme.setEnabled(false);
				this.cbElitisme.setEnabled(false);

				this.lblRemplacement.setEnabled(true);
				this.jpWithOverlap.setVisible(false);
				this.jpWithoutOverlap.setVisible(true);

				this.lblTauxMut.setEnabled(true);
				this.sliderMut.setEnabled(true);
				this.txtMut.setEnabled(true);

				this.lblTauxCrois.setEnabled(true);
				this.sliderCrois.setEnabled(true);
				this.txtCrois.setEnabled(true);
				}
			else
			/*----- NPGA -----*/
			if	(s.equals(ParametreAG.NPGA))
				{
				this.lblSelection.setEnabled(false);
				this.rbRang.setEnabled(false);
				this.rbRoulette.setEnabled(false);
				this.rbResteStochastique.setEnabled(false);
				this.rbTournoi.setEnabled(false);

				this.jpTournoiSimple.setVisible(false);
				this.jpTournoiPareto.setVisible(true);

				this.lblDiversite.setEnabled(true);
				this.cbScaling.setEnabled(true);
				this.cbSharing.setEnabled(false);
	//			this.cbCrowding.setEnabled(true);
				this.cbCroisRestreint.setEnabled(true);

				this.lblElitisme.setEnabled(false);
				this.cbElitisme.setEnabled(false);

				this.lblRemplacement.setEnabled(true);
				this.jpWithOverlap.setVisible(false);
				this.jpWithoutOverlap.setVisible(true);

				this.lblTauxMut.setEnabled(true);
				this.sliderMut.setEnabled(true);
				this.txtMut.setEnabled(true);

				this.lblTauxCrois.setEnabled(true);
				this.sliderCrois.setEnabled(true);
				this.txtCrois.setEnabled(true);
				}
			else
			/*----- NSGA_2 -----*/
			if	(s.equals(ParametreAG.NSGA_2))
				{
				}
			else
			/*----- VEGA -----*/
				{
				this.lblSelection.setEnabled(true);
				this.rbRang.setEnabled(true);
				this.rbRoulette.setEnabled(true);
				this.rbResteStochastique.setEnabled(true);
				this.rbTournoi.setEnabled(true);

				this.jpTournoiPareto.setVisible(false);
				this.jpTournoiSimple.setVisible(true);

				this.lblDiversite.setEnabled(true);
				this.cbScaling.setEnabled(false);
				this.cbSharing.setEnabled(false);
	//			this.cbCrowding.setEnabled(false);
				this.cbCroisRestreint.setEnabled(true);

				this.lblElitisme.setEnabled(false);
				this.cbElitisme.setEnabled(false);

				this.lblRemplacement.setEnabled(true);
				this.jpWithOverlap.setVisible(false);
				this.jpWithoutOverlap.setVisible(true);

				this.lblTauxMut.setEnabled(true);
				this.sliderMut.setEnabled(true);
				this.txtMut.setEnabled(true);

				this.lblTauxCrois.setEnabled(true);
				this.sliderCrois.setEnabled(true);
				this.txtCrois.setEnabled(true);
				}
			}

		ParametreAG p = this.ctrlAG.getEvolution().getPopulation().getParametreAG();

		this.setValeurs (p.getTauxMutation(),
						 p.getTauxCroisement(),
						 p.getSelection(),
						 p.getNbPartTournoi(),
						 p.getScaling(),
						 p.getSharing(),
						 p.getAlpha(),
						 p.getSigmaShare(),
						 p.getCrowding(),
						 p.getCroisementRestreint(),
						 p.getElitisme(),
						 p.getNbMeilleurs(),
						 p.getNbOverlap(),
						 p.getTdom());
		}


	/**
	 * Permet de mettre à jour l'IHM.
	 */
	public void setValeurs (double	taux_mut,
							double	taux_crois,
							int		selection,
							int		nb_part,
							boolean	scaling,
							boolean sharing,
							double	alpha,
							double	sigma,
							boolean crowding,
							boolean	crois_restreint,
							boolean	elitisme,
							int		nb_meilleurs,
							int		nb_over,
							int		t_dom)
		{
		this.sliderMut.setValeur(taux_mut);
		this.sliderCrois.setValeur(taux_crois);

		switch (selection)
			{
			case ParametreAG.RANG				: this.rbRang.setSelected(true);				break;
			case ParametreAG.ROULETTE_PIPEE		: this.rbRoulette.setSelected(true);			break;
			case ParametreAG.RESTE_STOCHASTIQUE : this.rbResteStochastique.setSelected(true);	break;
			default /*----- TOURNOI -----*/		: this.rbTournoi.setSelected(true);
			}

//		if	(this.itemListe != nb_part)
//			{
//			this.itemListe = nb_part;
//			this.cbNbPartTournoi.setSelectedItem(Integer.toString(nb_part));
//			}

		this.cbNbPartTournoi1.setValeur(nb_part);

		this.cbScaling.setSelected(scaling);

		this.cbSharing.setSelected(sharing);
		this.textAlpha.setValeur(alpha);
		this.textFrontiere.setValeur(sigma);

		this.cbCrowding.setSelected(crowding);
		this.cbCroisRestreint.setSelected(crois_restreint);

		this.cbElitisme.setSelected(elitisme);
		this.textNbMeilleurs.setValeur(nb_meilleurs);

		this.textNbOverlap.setValeur(nb_over);

		this.textTdom.setValeur(t_dom);
		}


	/**
	 * Définition de la méthode de ActionListener.
	 */
	public void actionPerformed (ActionEvent e)
		{
		String cde = e.getActionCommand();

		if	(cde == "Rang")					this.ctrlAG.getEvolution().getPopulation().getParametreAG().setSelection(ParametreAG.RANG);
		if	(cde == "Roulette pipée")		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setSelection(ParametreAG.ROULETTE_PIPEE);
		if	(cde == "Reste stochastique")	this.ctrlAG.getEvolution().getPopulation().getParametreAG().setSelection(ParametreAG.RESTE_STOCHASTIQUE);
		if	(cde == "Tournoi")				this.ctrlAG.getEvolution().getPopulation().getParametreAG().setSelection(ParametreAG.TOURNOI);

//		if	(cde == this.cbNbPartTournoi.getActionCommand())	this.ctrlAG.getEvolution().getPopulation().getParametreAG().setNbPartTournoi(Integer.valueOf((String)this.cbNbPartTournoi.getSelectedItem()).intValue());

		if	(cde == "Scaling")				this.ctrlAG.getEvolution().getPopulation().getParametreAG().setScaling(this.cbScaling.isSelected());
		if	(cde == "Sharing")				this.ctrlAG.getEvolution().getPopulation().getParametreAG().setSharing(this.cbSharing.isSelected());
		if	(cde == "Crowding")				this.ctrlAG.getEvolution().getPopulation().getParametreAG().setCrowding(this.cbCrowding.isSelected());
		if	(cde == "Croisement restreint")	this.ctrlAG.getEvolution().getPopulation().getParametreAG().setCroisementRestreint(this.cbCroisRestreint.isSelected());

		if	(cde == "Copie des meilleurs")	this.ctrlAG.getEvolution().getPopulation().getParametreAG().setElitisme(this.cbElitisme.isSelected());

		if	(cde == "Aléatoire")			this.ctrlAG.getEvolution().getPopulation().getParametreAG().setRemplacement(ParametreAG.ALEATOIRE);
		if	(cde == "Les plus mauvais")		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setRemplacement(ParametreAG.LES_PLUS_MAUVAIS);
		if	(cde == "Tournoi inversé")		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setRemplacement(ParametreAG.TOURNOI_INVERSE);
		if	(cde == "Les plus vieux")		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setRemplacement(ParametreAG.LES_PLUS_VIEUX);
		if	(cde == "Utilisateur")			this.ctrlAG.getEvolution().getPopulation().getParametreAG().setRemplacement(ParametreAG.PERSO);

//		System.out.print(this.ctrlAG.getEvolution().getPopulation().getParametreAG());
		}

} /*----- Fin de la classe PanneauModeEvolution -----*/
