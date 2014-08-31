package ihm.controle;


import java.awt.Dimension;
import java.io.File;
import javalain.algorithmegenetique.ihm.composant.JButtonEA;
import javalain.algorithmegenetique.ihm.composant.JLabelEAPar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import epp.EPPLab;


/*------------------------------*/
/* Classe JTextNomFichierResult */
/*------------------------------*/
/**
 * La classe <code>JTextNomFichierResult</code> ...
 *
 * @author	Alain Berro
 * @version	5 juin 2010
 */
public final class JTextNomFichierResult extends JTextField implements CaretListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private JLabelEAPar message;
	private JButtonEA btOk;

	private String repertoire;

	private File f;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	 public JTextNomFichierResult (String repertoire, String nom_fich, JLabelEAPar msg, JButtonEA bt_ok)
		{
		super(nom_fich);

		this.setPreferredSize(new Dimension(350, 20));
		this.addCaretListener(this);

		this.message = msg;
		this.btOk = bt_ok;
		this.repertoire = repertoire;

		/*----- On positionne le curseur en fin du texte -----*/
		this.getCaret().setDot(nom_fich.length());
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	public void caretUpdate (CaretEvent ce) { this.invokeLater(); }

	private void invokeLater()
		{
		SwingUtilities.invokeLater(new Runnable()
			{
			public void run()
				{
				if ("".equals(getText()))
					{
					message.setText("Vous devez entrer un nom de fichier."); // A faire - Traduire.
					btOk.setEnabled(false);
					}
				else
					{
					btOk.setEnabled(true);

					/*----- On vérifie si le fichier existe déjà -----*/
					f = new File(repertoire + File.separatorChar + getText());

					if (f.exists())
						{
						message.setText("Un fichier de même nom existe déjà, il sera écrasé."); // A faire - Traduire.
						btOk.setText("Ecraser"); // A faire - Traduire.
						}
					else
						{
						message.setText("Le fichier suivant sera créé."); // A faire - Traduire.
						btOk.setText(EPPLab.msg.getString("bt.enregistrer"));
						}
					}
				}
			});
		}

} /*----- Fin de la classe JTextNomFichierResult -----*/
