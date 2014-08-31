package javalain.ea;

import java.util.Comparator;
import java.util.Iterator;
import javalain.math.fonction.Fct;


/*---------*/
/* Archive */
/*---------*/
/**
 * Une <code>Archive</code> regroupe un ensemble de solutions à un problème
 * multiobjectif.
 *
 * @author	Alain Berro
 * @version	13 aout 2010
 */
public abstract class Archive extends EnsembleSolutionEA
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Taille maximale de l'archive -----*/
	protected int tailleMaxArchive;

	/*----- Comparateur de solutions -----*/
	private Comparator<SolutionEA> comparateur;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée une archive pour un problème donné.
	 */
	public Archive (int size, Fct fct)
		{
		super(size,fct);

		this.tailleMaxArchive = size;
		this.comparateur = fct.comparator();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la taille maximale de l'archive.
	 */
	public int getSizeMax () { return this.tailleMaxArchive; }


	/**
	 * Modifie la taille maximale de l'archive.
	 */
	public void setSizeMax (int max_size)
		{
		this.tailleMaxArchive = max_size;
		this.reduce();
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Ajoute une solution à l'archive.
	 */
	@Override
	public boolean add (SolutionEA new_sol)
		{
		Iterator<SolutionEA> iterator = this.liste.iterator();

		int domine;
		SolutionEA sol_arch;
		while (iterator.hasNext())
			{
			sol_arch = iterator.next();
			domine = this.comparateur.compare(new_sol, sol_arch);

			if (domine == 1)
				/*----- La nouvelle solution domine la solution de l'archive, on supprime cette dernière -----*/
				iterator.remove();
			else
				{
				/*----- La nouvelle solution est dominée, on quitte sans l'ajouter -----*/
				if (domine == -1)
					return false;
				else
					{
					/*----- On regarde si les deux solutions ne sont pas identiques -----*/ // A voir - à epsilon près ?
					if (new_sol.equals(sol_arch))
						return false;
					}
				}
			}

		/*----- On ajoute la nouvelle solution dans l'archive -----*/
		this.liste.add(new_sol);

		return true;
		}


	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Réduction de la taille de l'archive.
	 */
	public abstract void reduce ();

} /*----- Fin de la classe Archive -----*/
