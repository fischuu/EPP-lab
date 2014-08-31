package javalain.ea;

import java.text.DecimalFormat;
import util.GuiUtils;


/*----------*/
/* Solution */
/*----------*/
/**
 * La classe <code>Solution</code> ...
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class Solution implements SolutionEA
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Format d'affichage des réels.
	 */
	private static DecimalFormat DF = GuiUtils.DECIMAL_3;


	/*---------*/
	/* Données */
	/*---------*/

	/*----- Variables de décision -----*/
	private double[] variable;

	/*----- Valeurs des objectifs -----*/
	private double[] objectif;

	/*----- Distance de crowding -----*/
	private double crowdingDistance = 0.0;

	/*----- Rang de la solution -----*/
	private int rank = 0;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée une nouvelle solution (par copie des paramètres).
	 */
	public Solution (double[] pVariable)
		{
		/*----- Création par copie -----*/
		this.variable = new double[pVariable.length];
		System.arraycopy(pVariable, 0, this.variable, 0, pVariable.length);
		}


	/**
	 * Crée une nouvelle solution (par copie des paramètres).
	 */
	public Solution (double[] pVariable, double [] pObjectifs)
		{
		/*----- Création par copie -----*/
		this.variable = new double[pVariable.length];
		System.arraycopy(pVariable, 0, this.variable, 0, pVariable.length);

		this.objectif = new double[pObjectifs.length];
		System.arraycopy(pObjectifs, 0, this.objectif, 0, pObjectifs.length);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le nombre de variables de décision.
	 */
	public int getNbVariable () { return this.variable.length; }


	/**
	 * Retourne le nombre d'objectifs.
	 */
	public int getNbObjectif () { return this.objectif.length; }


	/**
	 * Retourne le vecteur des variables de décision.
	 */
	public double[] getVariable () { return this.variable; }


	/**
	 * Retourne la valeur de la i ème variable de décision.
	 */
	public double getVariable (int i) { return this.variable[i]; }


	/**
	 * Mise à jour de la valeur de la i ème variable de décision.
	 */
	public void setVariable (int i, double d) { this.variable[i] = d; }


	/**
	 * Retourne le vecteur des objectifs.
	 */
	public double[] getObjectif () { return this.objectif; }


	/**
	 * Retourne la valeur du i ème objectif.
	 */
	public double getObjectif (int i) { return this.objectif[i]; }


	/**
	 * Retourne la distance de crowding de la solution.
	 */
	public double getCrowdingDistance () { return this.crowdingDistance; }


	/**
	 * Mise à jour de la distance de crowding de la solution.
	 */
	public void setCrowdingDistance (double pCrowdingDistance) { this.crowdingDistance = pCrowdingDistance; }


	/**
	 * Retourne le rang de la solution.
	 */
	public int getRank () { return this.rank; }


	/**
	 * Mise à jour du rang de la solution.
	 */
	public void setRank (int pRank) { this.rank = pRank; }


	/**
	 * Modifie le format de représentation des réels en chaîne de caractères.
	 * <p>
	 * Par défaut le format est "0.000".
	 */
	public static void setDecimalFormat (DecimalFormat df) { Solution.DF = df; }


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Définit l'égalité entre deux solutions.
	 * La comparaison s'effectue sur les valeurs des objectifs.
	 */
	@Override
	public boolean equals (Object obj)
		{
		if (obj == null) return false;

		if (this.getClass() != obj.getClass()) return false;

		final Solution s = (Solution) obj;
		if (this.objectif.length != s.objectif.length) return false;

		/*----- Comparaison des valeurs des objectifs -----*/
		for (int i=0; i<this.objectif.length; i++)
			if (this.objectif[i] != s.objectif[i])
				return false;

		return true;
		}


	/**
	 * Affichage la solution.
	 */
	@Override
	public String toString()
		{
		StringBuilder sb = new StringBuilder();

		sb.append("x(");
		for (int i=0; i<this.variable.length-1; i++)
			sb.append(Solution.DF.format(this.variable[i])).append(" ");
		sb.append(Solution.DF.format(this.variable[this.variable.length-1])).append(") ");

		sb.append("f(");
		for (int i=0; i<this.objectif.length-1; i++)
			sb.append(Solution.DF.format(this.objectif[i])).append(" ");
		sb.append(Solution.DF.format(this.objectif[this.objectif.length-1])).append(")");

		sb.append(" Rank : ").append(this.rank);

		sb.append(" Crowding distance : ").append(Solution.DF.format(this.crowdingDistance));

		return sb.toString();
		}

} /*----- Fin de la classe Solution -----*/
