package javalain.math.fonction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import javalain.ea.SolutionEA;


/*-----*/
/* Fct */
/*-----*/
/**
 * Fonction.
 *
 * @author	Alain Berro
 * @version	12 août 2010
 */
public abstract class Fct
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Front de la fonction.
	 *
	 * Pour une fonction simple objectif, le front n'est composé que de
	 * l'optimum global (ou des optima globaux dans le cas
	 * de fonction multimodale).
	 * Pour une fonction multiobjectif, il s'agit du front de Pareto.
	 */
	protected double[][] front = null;


	/**
	 * Nombre de variables de décision.
	 */
	protected int nbVariable;


	/**
	 * Bornes des variables de décision.
	 */
	protected double[] lowerLimit;
	protected double[] upperLimit;


	/**
	 * Nombre de fonctions objectifs.
	 */
	protected int nbFctObjectif ;


	/**
	 * Nom de la fonction.
	 */
	protected String nomFct;


	/**
	 * Nombre de fois que la fonction a été évaluée.
	 */
	protected int nbEvaluation;


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les limites inférieures et supérieures des variables de décision.
	 */
	public double[] getLowerLimit () { return this.lowerLimit; } // A voir - Utilisation de getLowerLimit() ou (int i) ?
	public double[] getUpperLimit () { return this.upperLimit; }

	public double getLowerLimit (int i) { return this.lowerLimit[i]; }
	public double getUpperLimit (int i) { return this.upperLimit[i]; }

	public int getNbVariable () { return this.nbVariable; }
	public int getNbFctObjectif () { return this.nbFctObjectif; }


	/**
	 * Modifie les limites inférieures et supérieures des variables de décision.
	 */
	public void setLowerLimit (double[] x)
		{
		if (this.lowerLimit != null && this.lowerLimit.length == x.length)
			System.arraycopy(x,0,this.lowerLimit,0,x.length);
		}

	public void setUpperLimit (double[] x)
		{
		if (this.upperLimit != null && this.upperLimit.length == x.length)
			System.arraycopy(x,0,this.upperLimit,0,x.length);
		}


	/**
	 * Retourne le nom de la fonction.
	 */
	public String getNomFct () { return this.nomFct; }


	/**
	 * Mise à zéro/retourne le nombre de fois que la fonction a été évaluée.
	 */
	public void razNbEvalution () { this.nbEvaluation = 0; }

	public int getNbEvaluation () { return this.nbEvaluation; }


	/**
	 * Vérifie si les variables de décision sont dans les limites fixées
	 * par la fonction.
	 */
	public double[] limite (double[] x)
		{
		/*----- Principe du mur -----*/
		for (int i=0; i<x.length; i++)
			{
			if (x[i] < lowerLimit[i]) x[i] = lowerLimit[i];
			if (x[i] > upperLimit[i]) x[i] = upperLimit[i];
			}

		/*----- Rebond -----*/ // Recherche... - Le rebond pourrait se faire avec une mutation.

		return x;
		}


	/**
	 * Lecture du front de Pareto dans un fichier.
	 */
	protected void readParetoFront (String nom_fich)
		{
		try {
			InputStreamReader is = new InputStreamReader(this.getClass().getResourceAsStream("../../../ea/pf/" + nom_fich));
			BufferedReader buffer = new BufferedReader(is);

			String s = buffer.readLine();
			StringTokenizer st = new StringTokenizer(s);

			int nbSol = 0;
			int nbObj = st.countTokens();

			/*----- Lecture du fichier -----*/
			ArrayList<String> liste = new ArrayList<String>(1000);
			do  {
				st = new StringTokenizer(s);
				while (st.hasMoreTokens())
					liste.add(st.nextToken());

				nbSol++;
				} while ((s = buffer.readLine()) != null);

			buffer.close();
			is.close();

			/*----- Front de pareto -----*/
			this.front = new double[liste.size()/nbObj][nbObj];
			for (int i=0; i<nbSol; i++)
				for (int j=0; j<nbObj; j++)
					this.front[i][j] = Double.parseDouble(liste.get(i*nbObj + j));
			}
		catch (Exception e)
			{
			System.err.println("Fct, readParetoFront (String) : lecture du fichier '" + nom_fich + "' impossible.");
			}
		}


	/**
	 * Teste si deux fonctions sont identiques.
	 */
	@Override
	public boolean equals (Object obj)
		{
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;

		final Fct other = (Fct) obj;
		if ((this.nomFct == null) ? (other.nomFct != null) : !this.nomFct.equals(other.nomFct)) return false;

		return true;
		}


	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Retourne les résultats des fonctions objectifs.
	 */
	public abstract double[] compute (double[] x);


	/**
	 * Retourne le front.
	 */
	public abstract double[][] getFront ();


	/**
	 * Retourne le comparateur.
	 */
	public abstract Comparator<SolutionEA> comparator ();

} /*----- Fin de la classe Fct -----*/
