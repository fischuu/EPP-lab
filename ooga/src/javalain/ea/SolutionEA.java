package javalain.ea;


/*------------*/
/* SolutionEA */
/*------------*/
/**
 * L'interface <code>SolutionEA</code> spécifie les méthodes à définir afin de
 * créer des classes de solutions pour résoudre un problème réel.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public interface SolutionEA
{
	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Retourne le vecteur des variables de décision.
	 */
	public double[] getVariable ();


	/**
	 * Retourne le vecteur des objectifs.
	 */
	public double[] getObjectif ();


	/*---------------------------------*/
	/* Méthodes abstraites spécifiques */
	/*---------------------------------*/

	/**
	 * Retourne la distance de crowding (NSGA II, SMPSO).
	 */
	public double getCrowdingDistance ();


	/**
	 * Mise à jour de la distance de crowding (NSGA II, SMPSO).
	 */
	public void setCrowdingDistance (double pCrowdingDistance);


	/**
	 * Retourne le rang (NSGA II).
	 */
	public int getRank ();


	/**
	 * Mise à jour du rang (NSGA II).
	 */
	public void setRank (int pRank);

} /*----- Fin de l'interface SolutionEA -----*/
