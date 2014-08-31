package javalain.algorithmegenetique.genome;

import javalain.algorithmegenetique.Chromosome;
import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.chromosome.ChromoFixeReel;
import javalain.algorithmegenetique.gene.ReelDouble;
import javalain.ea.Solution;
import javalain.ea.operator.Crossover;
import javalain.ea.operator.Mutator;


/*---------------*/
/* ChromosomeFct */
/*---------------*/
/**
 * <code>Chromosome</code> composé uniquement de gènes <code>ReelDouble</code>.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public final class ChromosomeFct extends ChromoFixeReel
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
     * SBX : Simulated Binary Crossover.
	 */
	public final static int SBX = 0x00000100;


	/**
	 * Mode de mutation par MUT_GENE.
	 * <p>
	 * Ce mode effectue une mutation sur un seul <code>Gene</code> du
	 * chromosome choisi au hasard. La mutation produite est celle definie
	 * dans le <code>Gene</code> lui-même.
	 * <p>
	 *
	 * Mode de mutation par MUT_POLYNOMIALE.
	 * <p>
	 * Ce mode effectue une mutation polynomiale sur les réels qui composent le
	 * chromosome.
	 */
	public static enum mode_mutation { MUT_GENE, MUT_POLYNOMIALE};


	/*---------*/
	/* Données */
	/*---------*/

	/*----- Mode de mutation -----*/
	private mode_mutation modeMutation;

	/*----- Génome d'appartenance -----*/
	private GenomeFct genome;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise un <code>ChromosomeFct</code>.
	 *
	 * @param	pTaille			taille du chromosome
	 * @param	pModeCroisement	mode de croisement
	 * @param	pModeMutation	mode de mutation
	 */
	public ChromosomeFct (int			pTaille,
						  int			pModeCroisement,
						  mode_mutation	pModeMutation,
						  GenomeFct		pGenome)
		{
		super(pTaille,pModeCroisement);

		this.modeMutation = pModeMutation;
		this.genome = pGenome;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
     * Croisement de deux <code>ChromosomeFct</code>s.
	 *
	 * @param	parent	<code>ChromosomeFct</code> qui va être croisé avec this
	 *
	 * @return	un tableau contenant les deux enfants
	 *
	 * @exception	IllegalArgumentException si le mode de croisement est
	 *				incorrect
	 */
	@Override
	public Chromosome[] croisement (Chromosome parent) // A voir - Peut-on simplifier SBX ? Utilisation de new Solution(double[]) ?
		{
		/*----- Croisement SBX -----*/
		if (this.modeCroisement == ChromosomeFct.SBX)
			{
			/*----- Parents -----*/
			Solution p1 = new Solution(this.toArray());
			Solution p2 = new Solution(((ChromoFixeReel)parent).toArray());

			/*----- Croisement SBX -----*/
			Solution[] enfants = Crossover.SBX(1.0, p1, p2, this.genome.getFct(), 20.0);

			/*----- Enfants -----*/
			ChromosomeFct ch1 = new ChromosomeFct(this.size(), this.modeCroisement, this.modeMutation, this.genome);
			ChromosomeFct ch2 = new ChromosomeFct(this.size(), this.modeCroisement, this.modeMutation, this.genome);

			ReelDouble rd;
			for (int i=0; i<enfants[0].getNbVariable(); i++)
				{
				rd = new ReelDouble(new DomaineReel(this.genome.getFct().getLowerLimit(i), this.genome.getFct().getUpperLimit(i)));
				rd.set(enfants[0].getVariable(i));
				ch1.addGene(rd);

				rd = new ReelDouble(new DomaineReel(this.genome.getFct().getLowerLimit(i), this.genome.getFct().getUpperLimit(i)));
				rd.set(enfants[1].getVariable(i));
				ch2.addGene(rd);
				}

			ChromosomeFct[] ch = new ChromosomeFct[2];
			ch[0] = ch1;
			ch[1] = ch2;

			return ch;
			}
		
		/*----- Croisement hérité -----*/
		return super.croisement(parent);
		}


	/**
     * Mutation du <code>ChromosomeFct</code>.
	 */
	@Override
	public boolean mutation (double Pm)
		{
		/*----- Mutation polynomiale -----*/
		if (this.modeMutation == mode_mutation.MUT_POLYNOMIALE)
			{
			ReelDouble rd;
			for (int i=0; i<this.size(); i++)
				{
				rd = (ReelDouble)this.getGene(i);

				/**
				 * Mutation polynomiale avec l'index de mutation de 20.0.
				 */
				rd.set(Mutator.mutationPolynomiale(Pm, rd.get(), rd.getIntervalle().getMin(), rd.getIntervalle().getMax(), 20.0));
				}

			return true;
			}

		/*----- Mutation héritée -----*/
		return super.mutation(Pm);
		}

} /*----- Fin de la classe ChromosomeFct -----*/
