package ihm.controle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
import epp.EPPLab;
import util.GuiUtils;


/*--------------------*/
/* ButtonTabComponent */
/*--------------------*/
/**
 * Onglet personnalisé.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	5 juin 2010
 */
public final class ButtonTabComponent extends JPanel
{
	/*---------*/
	/* Données */
	/*---------*/

	private JTabbedPane pane;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public ButtonTabComponent (final JTabbedPane pane)
		{
		super(GuiUtils.FL_LEFT_00);

		this.pane = pane;
		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));

		/*----- Récupère le libellé du composant initial -----*/
		JLabel label = new JLabel()
			{
			@Override
			public String getText()
				{
				int i = pane.indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1)
					return pane.getTitleAt(i);

				return null;
				}
			};
		label.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		this.add(label);

		/*----- Bouton -----*/
		TabButton tab = new TabButton();
		this.add(tab);
		}


	/*----------*/
	/* Listener */
	/*----------*/

	private final static MouseListener buttonMouseListener = new MouseAdapter()
		{
		@Override
		public void mouseEntered(MouseEvent e)
			{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
				((AbstractButton)component).setBorderPainted(true);
			}

		@Override
		public void mouseExited(MouseEvent e)
			{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
				((AbstractButton) component).setBorderPainted(false);
			}
		};


	/*----------------*/
	/* Classe interne */
	/*----------------*/

	private class TabButton extends JButton implements ActionListener
		{
		/**
		 * Constructeurs.
		 */
		public TabButton ()
			{
			int size = 16;
			this.setPreferredSize(new Dimension(size,size));
			this.setToolTipText(EPPLab.msg.getString("onglet.croix"));
			this.setUI(new BasicButtonUI());

			/*----- Transparence -----*/
			this.setContentAreaFilled(false);

			/*----- Pas de focus -----*/
			this.setFocusable(false);

			/*----- Bordure -----*/
			this.setBorder(BorderFactory.createEtchedBorder());
			this.setBorderPainted(false);

			/*----- Effet de rollover -----*/
			this.addMouseListener(buttonMouseListener);
			this.setRolloverEnabled(true);

			/*----- Action -----*/
			this.addActionListener(this);
			}


		/**
		 * Méthodes.
		 */

		/*-----Action de clic -----*/
		public void actionPerformed (ActionEvent e)
			{
			int i = pane.indexOfTabComponent(ButtonTabComponent.this);
			if (i != -1)
				pane.remove(i);
			}

		/*----- Suppression de la mise à jour de l'UI du bouton -----*/
		@Override
		public void updateUI () { }

		/*----- Tracé de la croix -----*/
		@Override
		protected void paintComponent (Graphics g)
			{
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D)g.create();

			/*----- Déplace l'image lors du clic -----*/
			if (getModel().isPressed())
				g2.translate(1,1);

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GRAY);
			if (getModel().isRollover())
				g2.setColor(Color.BLACK);

			/*----- Trace -----*/
			int delta = 5;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
			}
		}

} /*----- Fin de la classe ButtonTabComponent -----*/
