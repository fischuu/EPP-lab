package javalain.algorithmegenetique.ihm.controle;

import javalain.algorithmegenetique.ihm.ControleAlgoG;

import java.awt.Dimension;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/*************************/
/* Classe SliderMutation */
/*************************/
/**
 * La classe <code>SliderMutation</code> gère le taux de mutation.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	17 avril 2004
 */
public final class SliderMutation extends JSlider implements ChangeListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private ControleAlgoG ctrlAG;

	private JTextField textMut;

	private int valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public SliderMutation (ControleAlgoG	c,
	 					   JTextField		jt)
		{
		super(0,100,0);
		this.addChangeListener(this);
		this.putClientProperty("JSlider.isFilled",Boolean.TRUE);
		this.setPreferredSize(new Dimension(100,16));
		this.setEnabled(false);

		this.ctrlAG = c;
		this.textMut = jt;
		this.textMut.setText("0");
		this.valeur = 0;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	public void setValeur (double f)
		{
		int i = (int)(f*100.0);
		if	(this.valeur != i)
			{
			this.valeur = i;
			this.setValue(i);
			this.textMut.setText(Integer.toString(i));
			}
		}


	public void stateChanged(ChangeEvent e)
		{
		JSlider source = (JSlider)e.getSource();
		this.valeur = source.getValue();
		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setTauxMutation(this.valeur/100.0);
		this.textMut.setText(Integer.toString(this.valeur));
		}

} /*----- Fin de la classe SliderMutation -----*/