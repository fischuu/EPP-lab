package javalain.algorithmegenetique.genome;

import javalain.algorithmegenetique.DomaineReel;
import javalain.algorithmegenetique.Genome;
import javalain.algorithmegenetique.gene.ReelDouble;
import javalain.ea.SolutionEA;
import javalain.math.fonction.Fct;


/*-----------*/
/* GenomeFct */
/*-----------*/
/**
 * Cette classe décrit la composition du génome ...
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public final class GenomeFct extends Genome implements SolutionEA
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Fonction à résoudre -----*/
	private static Fct fct;

	/*----- Distance de crowding -----*/
	private double crowdingDistance = 0.0;

	/*----- Rang de la solution -----*/
	private int rank = 0;

	/*----- Nombre d'évaluations -----*/
	public static int cpt_evaluation = 0;

	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	public GenomeFct (Fct pFct)
		{
		super(1, pFct.getNbFctObjectif());

		/*----- Fonction -----*/
		GenomeFct.fct = pFct;

		/*----- Création du chromosome -----*/
		int nbVar = pFct.getNbVariable();
		ChromosomeFct cf = new ChromosomeFct(nbVar,
											 ChromosomeFct.SBX,
											 ChromosomeFct.mode_mutation.MUT_POLYNOMIALE,
											 this);

		for (int i=0; i<nbVar; i++)
			cf.addGene(new ReelDouble(new DomaineReel(GenomeFct.fct.getLowerLimit(i), GenomeFct.fct.getUpperLimit(i))));

		/*----- Création du génome -----*/
		this.addChromosome(cf);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Renvoie la fonction à optimiser.
	 */
	public Fct getFct () { return GenomeFct.fct; }


	/**
	 * Retourne le vecteur des variables de décision.
	 */
	public double[] getVariable () { return ((ChromosomeFct)this.getChromosome(0)).toArray(); }
		// A voir - Optimisation de cette méthode, est-elle utile ?
		// On pourrait par exemple mettre un booléen indiquant si le génome
		// a changé (a été muté ou croisé). Ainsi s'il a changé, on assigne
		// un double[] en propriété et s'il n'a pas changé
		// on ne fait que renvoyer le double[].


	/**
	 * Retourne le vecteur des objectifs.
	 */
	public double[] getObjectif () { return this.notes; }


	/**
	 * Retourne la distance de crowding.
	 */
	public double getCrowdingDistance () { return this.crowdingDistance; }


	/**
	 * Mise à jour de la distance de crowding.
	 */
	public void setCrowdingDistance (double pCrowdingDistance) { this.crowdingDistance = pCrowdingDistance; }


	/**
	 * Retourne le rang.
	 */
	public int getRank () { return this.rank; }


	/**
	 * Mise à jour du rang.
	 */
	public void setRank (int pRank) { this.rank = pRank; }


	/**
	 * Calcul de la note.
	 */
	public void fitness()
		{
		GenomeFct.cpt_evaluation++;

		this.notes = GenomeFct.fct.compute(((ChromosomeFct)this.getChromosome(0)).toArray());
		}


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Retourne la représentation sous forme de chaîne de caractères
	 * d'un individu.
	 */
	@Override
	public StringBuilder afficher ()
		{
		StringBuilder sb = new StringBuilder();

		double[] d = this.getVariable();

		sb.append("x(");
		for (int i=0; i<d.length-1; i++)
			sb.append(Genome.DF.format(d[i])).append(" ");
		sb.append(Genome.DF.format(d[d.length-1])).append(") ");

		d = this.getObjectif();

		sb.append("f(");
		for (int i=0; i<d.length-1; i++)
			sb.append(Genome.DF.format(d[i])).append(" ");
		sb.append(Genome.DF.format(d[d.length-1])).append(")");

		sb.append(" Rank : ").append(this.rank);

		sb.append(" Crowding distance : ").append(Genome.DF.format(this.crowdingDistance));

		return sb;
		}

} /*----- Fin de la classe GenomeFct -----*/
