package epp_pso;


/*---------------------*/
/* Classe ParametrePSO */
/*---------------------*/
/**
 * La classe <code>ParametrePSO</code> regroupe les paramètres
 * d'évolution d'un PSO.
 *
 * @author	Alain Berro
 * @version	1 septembre 2009
 */
public class ParametrePSO
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Type de voisinage.
	 */
	public final static String VOISINAGE_COSINUS	= "VOISINAGE_COSINUS";
	public final static String VOISINAGE_GLOBAL		= "VOISINAGE_GLOBAL";
	public final static String SANS_VOISINAGE		= "SANS_VOISINAGE";


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Nom de l'<code>Essaim</code>.
	 */
	private String nom;


	/**
	 * Voisinage.
	 */
	private String voisinage;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise les paramètres d'évolution du PSO.
	 */
	public ParametrePSO (String s)
		{
		this.nom = s;

		/*----- Paramètres par défaut -----*/
		this.voisinage = ParametrePSO.VOISINAGE_COSINUS;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le nom de l'<code>Essaim</code>.
	 */
	public String getNom () { return this.nom; }


	/**
	 * Retourne le voisinage utilisée.
	 */
	public String getVoisinage () { return this.voisinage; }


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Affiche l'ensemble des paramètres.
	 */
	@Override
	public String toString ()
		{
		StringBuilder sb;

		sb = new StringBuilder();
		sb.append("-----------------------\n");
		sb.append("Nom de l'essaim        : "); sb.append(this.nom);		sb.append("\n");
		sb.append("Voisinage              : "); sb.append(this.voisinage);	sb.append("\n");
		sb.append("-----------------------\n");

		return sb.toString();
		}

} /*----- Fin de la classe ParametrePSO -----*/
