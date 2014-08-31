package javalain.algorithmegenetique.ihm.controle;

import javalain.algorithmegenetique.ihm.ControleAlgoG;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import util.GuiUtils;


/*-----------*/
/* TexteTdom */
/*-----------*/
/**
 * La classe <code>TexteTdom</code> gère le nombre de participants au tournoi
 * dans la méthode <code>NPGA</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 mai 2009
 */
public final class TexteTdom extends JTextField implements CaretListener
{
	/*--------*/
	/* Donnée */
	/*--------*/

	private ControleAlgoG ctrlAG;

	private boolean erreur;

	private int valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	 public TexteTdom (ControleAlgoG c)
		{
		super(3);
		this.addCaretListener(this);

		this.ctrlAG = c;
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


	public void caretUpdate (CaretEvent ce) { this.invokeLater(); }


	private void invokeLater()
		{
		SwingUtilities.invokeLater(new Runnable()
			{
			public void run()
				{
				try	{
					int i = Integer.parseInt(getText());

					if	(i <= 0)
						throw new NumberFormatException();
					else
						{
						valeur = i;
						ctrlAG.getEvolution().getPopulation().getParametreAG().setTdom(i);
						setBackground(Color.white);
						erreur = false;
						}
					}
				catch (NumberFormatException nfe)
					{
					if	(!erreur)
						{
						JOptionPane.showMessageDialog(ctrlAG,
													  "<html>T<sub>dom</sub><br/>------<br/>Vous devez entrer un nombre entier positif non nul</html>",
													  "Message",
													  JOptionPane.WARNING_MESSAGE);

						setBackground(GuiUtils.BLEU_ERREUR);
						erreur = true;
						}
					}
				}
			});
		}

} /*----- Fin de la classe TexteTdom -----*/
