package javalain.math;

import java.util.Arrays;
import util.MsgUtils;


/*---------------*/
/* Classe Calcul */
/*---------------*/
/**
 * Classe qui regroupe les fonctions mathématiques.
 *
 * @author	Alain Berro, Souâd Larabi
 * @version	8 juin 2009
*/
public final class Calcul
{
	/*--------------*/
	/* Constructeur */
	/*--------------*/

	public Calcul () { }


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne un nombre réel suivant une loi normale ou loi gaussienne.
	 *
	 * @param	m	espérance mathématique ou valeur moyenne
	 * @param	s	écart type
	 *
	 * @return	nombre  réel
	 */
	public static double loiNormale (final double m, final double s)
		{
		double d, rep;

		do { d = PseudoRandomNumbers.random(); } while (d==0.0); rep = s*Math.sqrt(-2*Math.log(d));
		do { d = PseudoRandomNumbers.random(); } while (d==0.0);

		return m + rep*Math.cos(2*Math.PI*d);
		}


	/**
	 * Retourne un nombre réel généré uniformément dans une hypersphère
	 * de centre g et de rayon r = distance (g, p).
	 */
	public static double[] loiHyperspherique (final double[] g, final double[] p)
		{
		double dist = Calcul.distanceEuclidienne(g, p);
		double[] x = new double[g.length];

		for (int i=0; i<x.length; i++)
			x[i] = g[i] - dist + 2*dist*PseudoRandomNumbers.random();

		return x;
		}


	/**
	 * Retourne un nombre entier compris dans [0,nb[.
	 */
	public static int randomInt (final int nb) { return (int)(PseudoRandomNumbers.random()*nb); }


	/**
	 * Retourne un nombre réel compris dans [min,max[.
	 */
	public static double randomDouble (final double min, final double max) { return PseudoRandomNumbers.random()*(max-min) + min; }


	/**
	 * Retourne le minimum de deux nombres réels.
	 */
	public static double min (final double d1, final double d2) { return (d1<=d2) ? d1 : d2; }


	/**
	 * Retourne le minimum de deux nombres entiers.
	 */
	public static int min (final int i1, final int i2) { return (i1<=i2) ? i1 : i2; }


	/**
	 * Retourne le maximum de deux nombres réels.
	 */
	public static double max (final double d1, final double d2) { return (d1>=d2) ? d1 : d2; }


	/**
	 * Retourne le maximum de deux nombres entiers.
	 */
	public static int max (final int i1, final int i2) { return (i1>=i2) ? i1 : i2; }


	/**
	 * Retourne le carré d'un nombre.
	 */
	public static double pow2 (final double d) { return d*d; }


	/**
	 * Retourne le cube d'un nombre.
	 */
	public static double pow3 (final double d) { return d*d*d; }


	/**
	 * Retourne la puissance 4 d'un nombre.
	 */
	public static double pow4 (final double d) { return pow2(pow2(d)); }


	/**
	 * Retourne la puissance 6 d'un nombre.
	 */
	public static double pow6 (final double d) { return pow2(pow3(d)); }


	/*-----------------------------*/
	/* Opérations sur les vecteurs */
	/*-----------------------------*/

	/**
	 * Retourne la distance euclidienne entre 2 vecteurs.
	 */
	public static double distanceEuclidienne (final double[] x, final double[] y)
		{
		double somme = 0.0;

		for (int i=0; i<x.length; i++)
			somme += Calcul.pow2(x[i] - y[i]);

		return Math.sqrt(somme);
		}


	/**
	 * Retourne le produit scalaire entre deux vecteurs.
	 */
	public static double produitScalaireVecteur (final double[] x, final double[] y)
		{
		double somme = 0.0;

		for	(int i=0; i<x.length; i++)
			somme += x[i]*y[i];

		return somme;
		}


	/**
	 * Retourne le produit vectoriel de deux vecteurs.
	 */
	public static double[] produitVectorielVecteur (final double[] x, final double[] y)
		{
		double[] pv = new double[3];

		pv[0] = x[1]*y[2] - x[2]*y[1];
		pv[1] = x[2]*y[0] - x[0]*y[2];
		pv[2] = x[0]*y[1] - x[1]*y[0];

		return pv;
		}


	/**
	 * Retourne la différence entre deux vecteurs.
	 */
	public static double[] differenceVecteur (final double[] x, final double[] y)
		{
		double[] diff = new double[x.length];

		for	(int i=0; i<x.length; i++)
			diff[i] = x[i] - y[i];

		return diff;
		}


	/**
	 * Retourne la somme de 2 vecteurs.
	 */
	public static double[] sommeVecteur (final double[] x, final double[] y)
		{
		double[] somme = new double[x.length];

		for	(int i=0; i<x.length; i++)
			somme[i] = x[i] + y[i];

		return somme;
		}


	/**
	 * Retourne la multiplication d'un réel par un vecteur.
	 */
	public static double[] multReelVecteur (final double d, final double[] x)
		{
		double[] mult = new double[x.length];

		for	(int i=0; i<x.length; i++)
			mult[i] = d*x[i];

		return mult;
		}


	/**
	 * Retourne le vecteur normalisé.
	 */
	public static double[] normaliseVecteur (final double[] v)
		{
		double norme = 1.0/Calcul.normeVecteur(v);

		double[] n = new double[v.length];
		for (int i=0; i<v.length; i++)
			n[i] = v[i]*norme;

		return n;
		}


	/**
	 * Retourne le carré de la norme d'un vecteur.
	 */
	public static double normeCarreeVecteur (final double[] v)
		{
		double somme = 0;

		for (int i=0; i <v.length; i++)
			somme += v[i]*v[i];

		return somme;
		}


	/**
	 * Retourne la norme d'un vecteur.
	 */
	public static double normeVecteur (final double[] v) { return Math.sqrt(Calcul.normeCarreeVecteur(v)); }


	/**
	 * Retourne le cosinus entre 2 vecteurs.
	 */
	public static double cosinus (final double[] x, final double[] y)
		{
		/*----- Est-ce que les vecteurs ont la même taille -----*/
		if (x.length != y.length)
			MsgUtils.erreurAndExit(null,
								   "Les vecteurs ne sont pas de la même taille.",
								   "Cosinus entre vecteurs");

		double a=0, b=0;
		for (int i=0; i<x.length; i++)
			a += x[i]*x[i];

		for (int i=0; i<y.length; i++)
			b += y[i]*y[i];

		double cosinus = (Calcul.produitScalaireVecteur(x,y) / (Math.sqrt(a) * Math.sqrt(b)));

		if (cosinus > 1.0) cosinus = 1.0;
		else
		if (cosinus < -1.0) cosinus = -1.0;

		return cosinus;
		}


	/**
	 * Retourne l'angle en radian entre deux vecteurs.
	 */
	public static double arcCosinus (final double[] x, final double[] y)
		{
		return Math.acos(Calcul.cosinus(x, y));
		}


	/*-------------------------*/
	/* Opérations statistiques */
	/*-------------------------*/

	/**
	 * Retourne la moyenne des données.
	 */
	public static double moyenne (final double[] tab)
		{
		double somme = 0.0;

		for (int i=0; i<tab.length; i++)
			somme += tab[i];

		return somme/tab.length;
		}


	/**
	 * Calcule la médiane des données.
	 */
	public static double med (final double[] tab)
		{
		/*----- Copie du tableau pour ne pas modifier les données initiales -----*/
		double[] t = new double[tab.length];
		System.arraycopy(tab, 0, t, 0, tab.length);

		return Calcul.mediane(t);
		}


	private static double mediane (double[] tab)
		{
		/*----- Tri des données par ordre ascendant -----*/
		Arrays.sort(tab);

		/*----- Retourne la valeur médiane -----*/
		return ((tab.length-1)%2 == 0 ? tab[(tab.length-1)/2] : (tab[(tab.length-1)/2] + tab[(tab.length-1)/2+1])/2.0);
		}


	/**
	 * Calcule la 'median absolute deviation' des données (MAD).
	 */
	public static double mad (final double[] tab)
		{
		/*----- Copie du tableau pour ne pas modifier les données initiales -----*/
		double[] t = new double[tab.length];
		System.arraycopy(tab, 0, t, 0, tab.length);

		/*----- Calcul de la médiane -----*/
		double med = Calcul.mediane(t);

		/*----- Calcul des 'absolute deviations' -----*/
		for (int i=0; i<t.length; i++)
			t[i] = Math.abs(t[i]-med);

		/*----- Calcul de la 'median absolute deviation' -----*/
		return Calcul.mediane(t);
		}


	/**
	 * Variance.
	 */
	public static double variance (final double[] tab)
		{
		double moy = Calcul.moyenne(tab);
		double somme = 0.0;

		double d;
		for (int i=0; i<tab.length; i++)
			{
			d = tab[i] - moy;
			somme += d*d;
			}

		return somme/tab.length;
		}


	/**
	 * Ecart-type.
	 */
	public static double ecartType (final double[] tab) { return Math.sqrt(Calcul.variance(tab)); }


	/*---------------------*/
	/* Dominance de Pareto */
	/*---------------------*/

	/**
	 * Calcule la dominance de Pareto (Cas de maximisation)
	 *  f : vecteur de valeurs objectifs de la solution X
	 *  g : vecteur de valeurs objectifs de la solution Y
	 *  f domine g ssi
	 *    qq soit i, fi >= gi
	 *    et il existe au moins un i tel que fi > gi
	 */
	public static boolean paretoDominanceMax (double[] f, double[] g)
		{
		int i = 0;
		while (i < f.length && f[i] >= g[i]) i++;

		if (i != f.length) return false;

		i = 0;
		while (i < f.length && !(f[i] > g[i])) i++;

		if (i != f.length) return true;

		return false;
		}


    /**
     * Retourne vrai si x ne domine pas y et y ne domine pas x.
     */
    public static boolean nonDomineesMax (double[] x, double[] y)
        {
		return !paretoDominanceMax(x,y) && !paretoDominanceMax(y,x);
        }

} /*----- Fin de la classe Calcul -----*/
