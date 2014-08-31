package javalain.algorithmegenetique.ihm.controle;

import javalain.algorithmegenetique.ihm.ControleAlgoG;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import util.GuiUtils;


/*----------------*/
/* TexteFrontiere */
/*----------------*/
/**
 * La classe <code>TexteFrontiere</code> gère le paramètre
 * <code>sigma<sub>share</sub></code> de l'heuristique de partage (Sharing).
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 mai 2009
 */
public final class TexteFrontiere extends JTextField implements CaretListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private ControleAlgoG ctrlAG;

	private boolean erreur;

	private double valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	 public TexteFrontiere (ControleAlgoG c)
		{
		super(5);
		this.addCaretListener(this);

		this.ctrlAG = c;
		this.erreur = false;
		this.valeur = Double.POSITIVE_INFINITY;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	public void setValeur (double d)
		{
		if	(this.valeur != d)
			{
			this.setText(GuiUtils.DECIMAL.format(d));
			this.valeur = d;
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
					double d = Double.parseDouble(getText());

					if	(d < 0.0)
						throw new NumberFormatException();
					else
						{
						valeur = d;
						ctrlAG.getEvolution().getPopulation().getParametreAG().setSigmaShare(d);
						setBackground(Color.white);
						erreur = false;
						}
					}
				catch (NumberFormatException nfe)
					{
					if	(!erreur)
						{
						JOptionPane.showMessageDialog(ctrlAG,
													  "<html>Sigma<sub>share</sub><br/>-----<br/>Vous devez entrer un nombre réel positif<br>Exemple : 3.14</html>",
													  "Message",
													  JOptionPane.WARNING_MESSAGE);

						setBackground(GuiUtils.BLEU_ERREUR);
						erreur = true;
						}
					}
				}
			});
		}

} /*----- Fin de la classe TexteFrontiere -----*/
