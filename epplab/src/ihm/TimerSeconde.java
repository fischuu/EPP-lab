package ihm;

import java.util.Timer;
import java.util.TimerTask;


/*---------------------*/
/* Classe TimerSeconde */
/*---------------------*/
/**
 * Cette classe...
 *
 * @author	Alain Berro
 * @version	20 mars 2010
 */
public class TimerSeconde
{
	/*-----------*/
	/* Variables */
	/*-----------*/

	private Timer seconde;
	private int duree;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	public TimerSeconde (int i)
		{
		this.duree = i;
		this.seconde = new Timer();
		this.seconde.schedule(new Tache(), 0, 1*1000);
		}


	/*------------------*/
	/* Classes internes */
	/*------------------*/

	class Tache extends TimerTask
		{
		public void run()
			{
			if (duree > 0)
				{
				System.out.println("Ca bosse dur!");
				duree--;
				}
			else
				{
				System.out.println("Terminé!");
				seconde.cancel();
				}
			}
		}

} /*----- Fin de la classe TimerSeconde -----*/
