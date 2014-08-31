package javalain.algorithmegenetique.ihm.controle;

import javalain.algorithmegenetique.ihm.ControleAlgoG;

import java.awt.Dimension;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/***************************/
/* Classe SliderCroisement */
/***************************/
/**
 * La classe <code>SliderCroisement</code> gère le taux de croisement.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	17 avril 2004
 */
public final class SliderCroisement extends JSlider implements ChangeListener
{
	/*---------*/
	/* Données */
	/*---------*/

	private ControleAlgoG ctrlAG;

	private JTextField textCrois;

	private int valeur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	 public SliderCroisement (ControleAlgoG	c,
	 						  JTextField	jt)
		{
		super(0,100,0);
		this.addChangeListener(this);
		this.putClientProperty("JSlider.isFilled",Boolean.TRUE);
		this.setPreferredSize(new Dimension(100,16));
		this.setEnabled(false);

		this.ctrlAG = c;
		this.textCrois = jt;
		this.textCrois.setText("0");
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
			this.textCrois.setText(Integer.toString(i));
			}
		}


	public void stateChanged(ChangeEvent e)
		{
		JSlider source = (JSlider)e.getSource();
		this.valeur = source.getValue();
		this.ctrlAG.getEvolution().getPopulation().getParametreAG().setTauxCroisement(this.valeur/100.0);
		this.textCrois.setText(Integer.toString(this.valeur));
		}

} /*----- Fin de la classe SliderCroisement -----*/