package ihm.controle;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javalain.algorithmegenetique.ihm.composant.JMenuItemEA;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


/*-----------------*/
/* Classe JPanelEA */
/*-----------------*/
/**
 * La classe <code>JPanelEA</code> ...
 *
 * @author	Alain Berro
 * @version	24 mai 2010
 */
public class JPanelEA extends JPanel
{
	/*---------*/
	/* Données */
	/*---------*/

	private JPopupMenu popup;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public JPanelEA ()
		{
		/*----- Menu contextuel -----*/
        this.popup = new JPopupMenu();

		/*----- Item 'imprimer" -----*/
		JMenuItemEA menu = new JMenuItemEA("Imprimer"); // A faire - Traduire
		menu.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				/*----- Action -----*/
				}
			});

//		this.popup.add(menu);

		/*----- Listener -----*/
		MouseListener popupListener = new PopupListener(popup);
		this.addMouseListener(popupListener);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	public JPopupMenu getMenuContextuel () { return this.popup; }


	/*----------------*/
	/* Classe interne */
	/*----------------*/

	class PopupListener extends MouseAdapter
		{
		JPopupMenu popup;

		PopupListener (JPopupMenu popupMenu) { popup = popupMenu; }

		@Override
		public void mousePressed (MouseEvent e) { maybeShowPopup(e); }

		@Override
		public void mouseReleased (MouseEvent e) { maybeShowPopup(e); }

		private void maybeShowPopup (MouseEvent e)
			{
			if (e.isPopupTrigger())
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

} /*----- Fin de la classe JPanelEA -----*/
