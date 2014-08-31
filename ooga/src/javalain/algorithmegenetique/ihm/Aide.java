package javalain.algorithmegenetique.ihm;

import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;


/*------*/
/* Aide */
/*------*/
/**
 * La classe <code>Aide</code> ...
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	29 décembre 2003
 */
public class Aide extends JFrame
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Aide () throws Exception
		{
		/*----- Titre de la fenêtre -----*/
		super("Api");

		/*----- Localisation de la fenêtre -----*/
		this.setLocation(0,0);
		this.setResizable(true);

		/*----- Conteneur de la fenêtre -----*/
		Container conteneur = this.getContentPane();

		/*----- Editeur -----*/
		final JEditorPane editeur = new JEditorPane();
		editeur.setEditable(false);

		try	{
			URL url = this.getClass().getResource("../../../doc/index.html");

			editeur.setPage(url);

			editeur.addHyperlinkListener(new HyperlinkListener()
				{
				public void hyperlinkUpdate (HyperlinkEvent e)
					{
					if	(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
						{
						if	(e instanceof HTMLFrameHyperlinkEvent)
							{
							((HTMLDocument)editeur.getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent)e);
							}
						else {
							try	{
								editeur.setPage(e.getURL());
								} catch (IOException ioe) { }
							}
						}
					}
				});

			/*----- Panneau déroulant -----*/
			JScrollPane scroller = new JScrollPane(editeur);
			scroller.setPreferredSize(new Dimension(1000,600));
			scroller.setMinimumSize(new Dimension(100,100));

			conteneur.add(scroller);

			/*----- Fenêtre visible -----*/
			this.pack();
			this.setVisible(true);

			} catch (Exception e) { throw new Exception(); }
		}

} /*----- Fin de la classe Aide -----*/
