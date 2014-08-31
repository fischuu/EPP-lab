package util;

import java.util.Calendar;


/*-------------*/
/* StringUtils */
/*-------------*/
/**
 * Ensemble d'utilitaires de manipulation de chaînes de caractères.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	20 octobre 2010
 */
public final class StringUtils
{
	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Convertion d'une durée en nanosecondes dans un format h, m, s, ms et ns.
	 */
	public static String convertHMSmsns (long d)
		{
		long q = d / 1000000;

		StringBuilder sb;
		if (q != 0)
			sb = new StringBuilder(convertHMSms(q)).append(" ");
		else
			sb = new StringBuilder();

		return sb.append(d % 1000000).append("ns").toString();
		}


	/**
	 * Convertion d'une durée en millisecondes dans un format h, m, s et ms.
	 */
	public static String convertHMSms (long d)
		{
		StringBuilder sb = new StringBuilder();

		long i = d/3600000;
		if (i != 0)
			{
			d -= i*3600000;
			sb.append(i).append("h ");
			}

		i = d/60000;
		if (i != 0)
			{
			d -= i*60000;
			sb.append(i).append("m ");
			}

		i = d/1000;
		if (i != 0)
			{
			d -= i*1000;
			sb.append(i).append("s ");
			}

		if (d != 0)
			sb.append(d).append("ms");
		else
			if (sb.length() == 0) sb.append("0ms");

		return sb.toString().trim();
		}


	/**
	 * Convertion d'une durée en millisecondes dans un format h, m, s.
	 */
	public static String convertHMS (long d)
		{
		StringBuilder sb = new StringBuilder();

		long i = d/3600000;
		if (i != 0)
			{
			d -= i*3600000;
			sb.append(i).append("h ");
			}

		i = d/60000;
		if (i != 0)
			{
			d -= i*60000;
			sb.append(i).append("m ");
			}

		i = d/1000;
		if (i != 0)
			{
			d -= i*1000;
			sb.append(i).append("s ");
			}

		return sb.toString().trim();
		}


	/**
	 * Remplace toutes les occurences dans une chaîne de caractères.
	 */
	public static StringBuilder replaceAll (StringBuilder sb, String textToFind, String textToReplace)
		{
		int pos = 0;
		while ((pos = sb.indexOf(textToFind, pos)) != -1)
			{
			sb.replace(pos, pos + textToFind.length(), textToReplace);
			pos += textToReplace.length();
			}

		return sb;
		}


	/**
	 * Retourne la date du jour sous le format '2010-03-01'.
	 */
	public static StringBuilder getDate ()
		{
		int i;
		Calendar c = Calendar.getInstance();

		StringBuilder sb = new StringBuilder(10);
		sb.append(c.get(Calendar.YEAR)).append('-');
		i = c.get(Calendar.MONTH)+1;		sb.append((i<10) ? "0"+i : i).append('-');
		i = c.get(Calendar.DAY_OF_MONTH);	sb.append((i<10) ? "0"+i : i);

		return sb;
		}


	/**
	 * Retourne l'heure actuelle sous la forme '10h35m3s'.
	 */
	public static StringBuilder getHeure ()
		{
		int i;
		Calendar c = Calendar.getInstance();

		StringBuilder sb = new StringBuilder(9);
		i = c.get(Calendar.HOUR_OF_DAY);	sb.append((i<10) ? "0"+i : i).append('h');
		i = c.get(Calendar.MINUTE);			sb.append((i<10) ? "0"+i : i).append('m');
		i = c.get(Calendar.SECOND);			sb.append((i<10) ? "0"+i : i).append('s');

		return sb;
		}


	/**
	 * Retourne la date et l'heure actuelle sous le format '2010-03-01:10h35m3s'.
	 */
	public static StringBuilder getDateHeure ()
		{
		return StringUtils.getDate().append(':').append(StringUtils.getHeure());
		}

} /*----- Fin de la classe StringUtils -----*/
