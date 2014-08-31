package ihm.controle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPres;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import epp.EPPLab;
import util.GuiUtils;


/*------------------------------*/
/* Classe JTextNomFichierResult */
/*------------------------------*/
/**
 * Boîte de dialogue...
 *
 * @author	Alain Berro
 * @version	5 juin 2010
 */
public class JDialogFichier extends JDialog implements ActionListener
{
	/*------------*/
	/* Constantes */
	/*------------*/

	private final static String ACTION_OUI		= "oui";
	private final static String ACTION_ANNULER	= "annuler";


	/*-----------*/
	/* Variables */
	/*-----------*/

	private String nomFich;

	private JTextNomFichierResult jtNomFichier;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	public JDialogFichier (JFrame frame, String repertoire, String nom_fich, String message)
		{
		super(frame, EPPLab.msg.getString("showOptionDialog.enregistrerSous"), true);

		/*----- Panneau -----*/
		JPanel panneau = new JPanel(new BorderLayout(12,10));
		panneau.setBorder(GuiUtils.BORDER_10);

		/*----- Icône -----*/
		Box box0 = Box.createVerticalBox();
		box0.add(new JLabel(UIManager.getIcon(GuiUtils.ICON_QUESTION)));
		box0.add(Box.createVerticalGlue());

		panneau.add(box0, BorderLayout.WEST);

		/*----- Liste des boutons -----*/
		JPanel bouton_pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButtonEA boutonOK = new JButtonEA(EPPLab.msg.getString("oui"));
		boutonOK.addActionListener(this);
		boutonOK.setActionCommand(ACTION_OUI);
		bouton_pane.add(boutonOK);

		JButtonEA boutonAnnuler = new JButtonEA(EPPLab.msg.getString("bt.annuler"));
		boutonAnnuler.addActionListener(this);
		boutonAnnuler.setActionCommand(ACTION_ANNULER);
		bouton_pane.add(boutonAnnuler);

		panneau.add(bouton_pane, BorderLayout.SOUTH);

		/*----- Message -----*/
		Box box1 = Box.createVerticalBox();

		JLabelEAPres question = new JLabelEAPres(message);
		question.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabelEAPar msg = new JLabelEAPar(" ");
		msg.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.jtNomFichier = new JTextNomFichierResult(repertoire, nom_fich, msg, boutonOK);
		this.jtNomFichier.setAlignmentX(Component.LEFT_ALIGNMENT);

		box1.add(question);
		box1.add(Box.createVerticalStrut(3));
		box1.add(msg);
		box1.add(Box.createVerticalStrut(3));
		box1.add(this.jtNomFichier);

		panneau.add(box1, BorderLayout.CENTER);

		this.setContentPane(panneau);

		/*----- Position -----*/
		Rectangle r = frame.getBounds();
		this.setLocation(r.x + r.width / 4, r.y + r.height / 4);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Demande l'affichage de la boîte de dialogue.
	 * 
	 * Retourne le nom du fichier ou "" si l'utilisateur a annulé.
	 */
	public String afficher ()
		{
		this.setVisible(true);
		return this.nomFich;
		}


	/*------------------------------------*/
	/* Méthodes de gestion des évènements */
	/*------------------------------------*/

	public void actionPerformed (ActionEvent e)
		{
		String cmd = e.getActionCommand();
		if (ACTION_OUI.equals(cmd))
			this.nomFich = this.jtNomFichier.getText();
		else
			if (ACTION_ANNULER.equals(cmd))
				this.nomFich = "";

		this.setVisible(false);
		this.dispose();
		}

} /*----- Fin de la classe JdialogFichier -----*/
