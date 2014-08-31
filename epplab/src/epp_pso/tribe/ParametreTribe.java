package epp_pso.tribe;


/*-----------------------*/
/* Classe ParametreTribe */
/*-----------------------*/
/**
 * La classe <code>ParametrePSO</code> regroupe les paramètres
 * d'évolution de TRIBE.
 *
 * @author	Alain Berro
 * @version	1 septembre 2009
 */
public class ParametreTribe
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Nom de l'<code>Essaim</code>.
	 */
	private String nom;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise les paramètres d'évolution de TRIBE.
	 */
	public ParametreTribe (String s)
		{
		this.nom = s;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le nom de l'<code>Essaim</code>.
	 */
	public String getNom () { return this.nom; }


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
		sb.append("-----------------------\n");

		return sb.toString();
		}

} /*----- Fin de la classe ParametreTribe -----*/