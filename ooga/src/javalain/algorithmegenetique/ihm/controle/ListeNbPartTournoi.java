package javalain.algorithmegenetique.ihm.controle;

import javalain.algorithmegenetique.ihm.ControleAlgoG;
import javalain.algorithmegenetique.ihm.composant.JComboBoxEA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import util.GuiUtils;


/*--------------------*/
/* ListeNbPartTournoi */
/*--------------------*/
/**
 * La classe <code>ListeNbPartTournoi</code> ...
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	29 avril 2006
 */
public final class ListeNbPartTournoi extends JComboBoxEA implements ActionListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private static final String[] listeNbPartTournoi = {"2","3","4","5"};

	private ControleAlgoG ctrlAG;

	private boolean erreur;

	private int valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public ListeNbPartTournoi (ControleAlgoG c)
		{
		super(listeNbPartTournoi);
		this.setSelectedIndex(0);
		this.setToolTipText("Nombre de participants");
		this.setEditable(true);
		this.setPreferredSize(new Dimension(45,21));
		this.addActionListener(this);

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
			this.setSelectedItem(Integer.toString(i));
			}
		}


	@Override
	public void actionPerformed (ActionEvent e)
		{
		try	{
			int i = Integer.parseInt(((String)this.getSelectedItem()));

			if	(i <= 1)
				throw new NumberFormatException();
			else
				{
				valeur = i;
				ctrlAG.getEvolution().getPopulation().getParametreAG().setNbPartTournoi(i);
				setBackground(Color.white);
				erreur = false;
				}
			}
		catch (NumberFormatException nfe)
			{
			if	(!erreur)
				{
				JOptionPane.showMessageDialog(ctrlAG,
											  "<html>Nombre de participants au tournoi : <br><br>Vous devez entrer un nombre entier supérieur à 1</html>",
											  "Message",
											  JOptionPane.WARNING_MESSAGE);
				setBackground(GuiUtils.BLEU_ERREUR);
				erreur = true;
				}
			}
		}

} /*----- Fin de la classe ListeNbPartTournoi -----*/
