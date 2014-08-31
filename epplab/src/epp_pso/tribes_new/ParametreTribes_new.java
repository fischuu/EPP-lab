package epp_pso.tribes_new;


/*------------------------*/
/* Classe ParametreTribes */
/*------------------------*/
/**
 * La classe <code>ParametreTribes</code> regroupe les paramètres
 * d'évolution de TRIBE.
 *
 * @author	Alain Berro
 * @version	3 novembre 2010
 */
public class ParametreTribes_new
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
	public ParametreTribes_new (String s)
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

} /*----- Fin de la classe ParametreTribes -----*/