package ihm.controle;

import ihm.Recherche;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import util.GuiUtils;


/*--------------------------*/
/* Classe JTextNbIterations */
/*--------------------------*/
/**
 * La classe <code>JTextNbIterations</code> ...
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	28 mars 2010
 */
public final class JTextNbIterations extends JTextField implements CaretListener, FocusListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private Recherche ctrl;

	private int valeur;

	private boolean erreur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	 public JTextNbIterations (Recherche c)
		{
		super(3);
		this.addCaretListener(this);
		this.addFocusListener(this);

		this.ctrl = c;
		this.erreur = false;
		this.valeur = Integer.MAX_VALUE;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	public void setValeur (int i)
		{
		if	(this.valeur != i)
			{
			this.valeur = i;
			this.setText(Integer.toString(i));
			}
		}


	/*-------------------*/
	/* Méthodes définies */
	/*-------------------*/

	public void caretUpdate (CaretEvent ce)
		{
		try	{
			int i = Integer.parseInt(getText());

			if	(i <= 0)
				throw new NumberFormatException();
			else
				{
				this.setBackground(Color.WHITE);
				this.valeur = i;
				this.ctrl.getParametre().setNbIterations(i);
				this.erreur = false;
				}
			}
		catch (NumberFormatException nfe)
			{
			this.setBackground(GuiUtils.BLEU_ERREUR);
			this.erreur = true;
			}
		}


	public void focusLost (FocusEvent e)
		{
		if (this.erreur)
			{
			JOptionPane.showMessageDialog(ctrl,
										  "Vous devez entrer un nombre entier positif non nul.", // A faire - Traduire.
										  "Message",
										  JOptionPane.WARNING_MESSAGE);

			this.erreur = false;
			this.requestFocus();
			}
		}

	public void focusGained (FocusEvent e) { }

} /*----- Fin de la classe JTextNbIterations -----*/
