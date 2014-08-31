package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/*-----------*/
/* FileUtils */
/*-----------*/
/**
 * Ensemble d'utilitaires de manipulation de fichiers.
 *
 * @author	Alain Berro, Emilie Chabbert
 * @version	6 juin 2010
 */
public final class FileUtils
{
	/*------------*/
	/* Constantes */
	/*------------*/

	public final static String ISO_8859_1 = "ISO-8859-1";

	public final static String UTF_8 = "UTF-8";


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Retourne la liste des fichiers contenus dans un répertoire.
	 */
	public static Vector<String> listeFichiers (File rep)
		{
		Vector<String> v = new Vector<String>();

		if (!rep.exists())
			rep.mkdirs();
		else
			{
			File[] fichiers = null;

			if (rep.isDirectory())
				fichiers = rep.listFiles();

			for (int i=0; i<fichiers.length; i++)
				if(fichiers[i].isFile())
					v.add(fichiers[i].getName());
			}

		return v;
		}


	/**
	 * Enregistre la liste des fichiers contenus dans un répertoire 'rep' dans
	 * la liste 'liste'.
	 */
	public static void listeFichiers (File rep, Vector<String> liste)
		{
		/*----- Effacement de la liste -----*/
		liste.clear();

		if (!rep.exists())
			rep.mkdirs();
		else
			{
			File[] fichiers = null;

			if (rep.isDirectory())
				fichiers = rep.listFiles();

			for (int i=0; i<fichiers.length; i++)
				if(fichiers[i].isFile())
					liste.add(fichiers[i].getName());
			}
		}


	/**
	 * Effectue la copie d'un fichier.
	 */
	public static boolean copierFichier (String nomInputFile, String nomOutputFile) throws IOException
		{
		return copierFichier(new File(nomInputFile), new File(nomOutputFile));
		}


	public static boolean copierFichier (File inputFile, File outputFile) throws IOException
		{
		try {
			int bufSize = 1024;
			FileInputStream fis = new FileInputStream(inputFile);
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedInputStream in = new BufferedInputStream(fis, bufSize);
			BufferedOutputStream out = new BufferedOutputStream(fos, bufSize);

			int length = 32;
			byte[] ch = new byte[length];
			while ((length = in.read(ch)) != -1)
				out.write(ch, 0, length);

			/*----- Tout doit être bien fermé ! -----*/
			out.flush();
			out.close();
			in.close();
			fos.close();
			fis.close();

			return true;
			}
		catch (IOException ex) { throw ex; }
		}


	public static boolean copierFichier (InputStream fis, String outputFile) throws IOException
		{
		try {
			int bufSize = 1024;
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedInputStream in = new BufferedInputStream(fis);
			BufferedOutputStream out = new BufferedOutputStream(fos, bufSize);
			int length = 32;
			byte[] ch = new byte[length];
			while ((length = in.read(ch)) != -1)
				out.write(ch, 0, length);

			/*----- Tout doit être bien fermé ! -----*/
			out.flush();
			out.close();
			in.close();
			fos.close();
			fis.close();

			return true;
			}
		catch (IOException ex) { throw ex; }
		}


	/**
	 * Sauvegarde d'un StringBuilder dans un fichier.
	 */
	public static void saveStringToFich (String path, String nomFich, String texte)
		{
		FileUtils.saveStringToFich(path, nomFich, new StringBuilder(texte));
		}

	public static void saveStringToFich (String path, String nomFich, StringBuilder texte)
		{
		FileUtils.saveStringToFich(path, nomFich, texte, FileUtils.ISO_8859_1);
		}

	public static void saveStringToFich (String path, String nomFich, StringBuilder texte, String encodage)
		{
		File rep = new File(path);
		if (!rep.exists()) rep.mkdirs();

		PrintWriter out = null;
		try {
			out = new PrintWriter(path + File.separatorChar + nomFich, encodage);
			}
		catch (IOException e) {}

		out.print(texte.toString());
		out.close();
		}


	/**
	 * Crée un nouveau nom de fichier 'jpg' en fonction des fichiers existants.
	 *
	 * Si la racine est 'image', le nouveau nom sera 'imageN.jpg'.
	 */
	public static String creeNomFichierJpg (String path, String racine_nom)
		{
		File rep = new File(path);
		if (!rep.exists()) rep.mkdirs();

		rep = null;
		String nouveauNom = null;
		int i = 1;
		while (rep == null || rep.exists())
			{
			nouveauNom = racine_nom + i + ".jpg";
			rep = new File(path + File.separatorChar + nouveauNom);
			i++;
			}

		return nouveauNom;
		}


	/**
	 * Crée un nouveau nom de fichier du style 'basedunom_copie_N.ext'.
	 */
	public static String creeNomFichier (String path, String nom)
		{
		File f = new File(path + File.separator + nom);

		if (!f.exists())
			return nom;

		String nouveauNom	= "";
		int pos_point		= nom.lastIndexOf('.');
		String base_du_nom	= (pos_point != -1 ? nom.substring(0,pos_point)	: nom);
		String extension	= (pos_point != -1 ? nom.substring(pos_point)	: "");
		int cpt = 1;

		while (f.exists())
			{
			nouveauNom = base_du_nom + "_copie_" + cpt + extension ;
			f = new File(path + File.separatorChar + nouveauNom);
			cpt++;
			}

		return nouveauNom;
		}


	/**
	 * Enregistre un JPanel dans un fichier image (.jpg).
	 */
	public static void saveJPanelToFile (JPanel panneau, File fichier) throws IOException
		{
		BufferedImage tampon = new BufferedImage(panneau.getPreferredSize().width,panneau.getPreferredSize().height,BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = tampon.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,panneau.getPreferredSize().width,panneau.getPreferredSize().height);
		panneau.paint(g);

		ImageIO.write(tampon,"JPG",fichier);
		}

} /*----- Fin de la classe FileUtils -----*/
