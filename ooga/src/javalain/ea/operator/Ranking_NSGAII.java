package javalain.ea.operator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javalain.algorithmegenetique.genome.GenomeFct;
import javalain.ea.EnsembleSolutionEA;
import javalain.ea.SolutionEA;
import javalain.ea.operator.comparator.DominanceMinComparator;


/*----------------*/
/* Ranking_NSGAII */
/*----------------*/
/**
 * Ranking_NSGAII permet de rechercher les différents fronts de Pareto.
 * Technique proposée dans NSGA II.
 *
 * Article(s) de référence :
 * Deb K., Pratap A., Agarwal S. and Meyarivan, T.
 * "A fast and elitist multiobjective genetic algorithm: NSGA-II"
 * IEEE Trans. on Evol. Computation, vol. 6, p. 182-197, 2002.
 *
 * @author	Alain Berro, Souad Larabi
 * @version	13 août 2010
 */
public class Ranking_NSGAII
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/*----- Comparateur -----*/
	private static final Comparator<SolutionEA> comparateur = new DominanceMinComparator();


	/*---------*/
	/* Données */
	/*---------*/

	/*----- Tableau des fronts de Pareto trouvés -----*/
	private EnsembleSolutionEA[] tabFrontPareto;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Recherche et crée les différents fronts de Pareto.
	 */
	public Ranking_NSGAII (GenomeFct[] ensb)
		{
		/*----- Tableau contenant le nombre de solutions qui domine 'i' -----*/
		int[] nbDominants = new int[ensb.length];

		/*----- Tableau contenant la liste des solutions dominées par 'i' -----*/
		List<Integer>[] listeDomines = new List[ensb.length];

		/*----- Tableau des fronts -----*/
		List<Integer>[] fronts = new List[ensb.length];

		/*----- Initialisation des fronts -----*/
		for (int i = 0; i < fronts.length; i++)
			fronts[i] = new LinkedList<Integer>();

		/**
		 * Tri.
		 */
		int domine;

		/*----- Pour tous les p -----*/
		for (int p=0; p<ensb.length; p++)
			{
			/*----- Initialise le nombre de fois que 'p' est dominé -----*/
			nbDominants[p] = 0;

			/*----- Initialise la liste des solutions dominées par p -----*/
			listeDomines[p] = new LinkedList<Integer>();

			/*----- Pour tous les q -----*/
			for (int q=0; q<ensb.length; q++)
				{
				domine = Ranking_NSGAII.comparateur.compare(ensb[p],ensb[q]);

				if (domine == 1)
					listeDomines[p].add(new Integer(q));
				else
				if (domine == -1)
					nbDominants[p]++;
				}

			/*----- Si p n'est pas dominé alors il appartient au premier front -----*/
			if (nbDominants[p] == 0)
				{
				fronts[0].add(new Integer(p));
				ensb[p].setRank(0);
				}
			}

		/*----- Calcul des autres fronts -----*/
		int indice;
		int num_front = 0;
		Iterator<Integer> i1, i2;

		while (!fronts[num_front].isEmpty())
			{
			num_front++;
			i1 = fronts[num_front-1].iterator();

			while (i1.hasNext())
				{
				i2 = listeDomines[i1.next().intValue()].iterator();

				while (i2.hasNext())
					{
					indice = i2.next().intValue();
					nbDominants[indice]--;

					if (nbDominants[indice] == 0)
						{
						fronts[num_front].add(new Integer(indice));
						ensb[indice].setRank(num_front);
						}
					}
				}
			}

		/*----- Les fronts de Pareto -----*/
		this.tabFrontPareto = new EnsembleSolutionEA[num_front];

		for (int i=0; i<num_front; i++)
			{
			this.tabFrontPareto[i] = new EnsembleSolutionEA(fronts[i].size(), ensb[0].getFct());

			i1 = fronts[i].iterator();
			while (i1.hasNext())
				this.tabFrontPareto[i].add(ensb[i1.next().intValue()]);
			}
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le front de rang 'i'.
	 */
	public EnsembleSolutionEA getFront (int rang) { return this.tabFrontPareto[rang]; }


	/**
	 * Retourne le nombre total de fronts trouvés.
	 */
	public int getNbFrontsPareto () { return this.tabFrontPareto.length; }

} /*----- Fin de la classe Ranking_NSGAII -----*/
