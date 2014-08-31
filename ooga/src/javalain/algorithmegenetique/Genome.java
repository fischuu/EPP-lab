package javalain.algorithmegenetique;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Comparator;
import util.GuiUtils;
import javalain.math.PseudoRandomNumbers;


/*--------*/
/* Genome */
/*--------*/
/**
 * Classe pour définir le génome d'un individu.
 * <p>
 * <b>Utilisation:</b>
 * <ul>
 *	<li>Hériter de cette classe.
 *	<li>Définir toutes les données communes (en static de préférence).
 *	<li>Définir un constructeur qui ajoute les différents
 *		<code>Chromosome</code>s avec leur mode de croisement.
 *	<li>Définir la fonction de notation <code>fitness()</code>.
 *</ul>
 *
 * @author  <a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public abstract class Genome implements Cloneable, Serializable, Comparator<Genome>
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
	 * Format d'affichage des réels (par exemple : les notes).
	 */
	protected static DecimalFormat DF = GuiUtils.DECIMAL_3;


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Liste des <code>Chromosome</code>s.
	 */
	private Chromosome[] tabChromo;


	/**
	 * Nombre de <code>Chromosome</code>s composant le <code>Genome</code>.
	 */
	private int	nbChromo;


	/**
	 * Référence à la <code>Population</code> d'appartenance.
	 */
	protected Population population;


	/**
	 * Nombre de notes de l'individu.
	 * <p>
	 * Par défaut un individu a une seule note. Mais dans le cas d'optimisation
	 * multiobjectif un individu aura une note par objectif.
	 */
	private int nbNotes;


	/**
	 * Note(s) du <code>Genome</code> c'est-à-dire note(s) de l'individu.
	 */
	protected double notes[];


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit un <code>Genome</code> vide.
	 *
	 * @param	nbC	nombre de <code>Chromosome</code>s composant
	 *			le <code>Genome</code>
	 *
	 * @exception	IllegalArgumentException si <code>nbC</code> est <= 0
	 */
	public Genome (int nbC)
		{
		if	(nbC > 0)
			this.nbChromo = nbC;
		else
			throw(new IllegalArgumentException("Genome, Genome (int nbC) : le nombre de chromosome doit être > 0."));

		this.tabChromo		= new Chromosome[nbC];
		this.nbNotes		= 1;
		this.notes			= new double[1];
		}


	/**
	 * Construit un <code>Genome</code> vide.
	 *
	 * @param	nbC			nombre de <code>Chromosome</code>s composant
	 *						le <code>Genome</code>
	 * @param	nbObjectif	nombre d'objectifs
	 *
	 * @exception	IllegalArgumentException si <code>nbC</code> est <= 0
	 * @exception	IllegalArgumentException si <code>nbObjectif</code> est <= 0
	 */
	public Genome (int nbC,
				   int nbObjectif)
		{
		if	(nbC > 0)
			this.nbChromo = nbC;
		else
			throw(new IllegalArgumentException("Genome, Genome (int nbC, int nbOjectif) : le nombre de chromosome doit être > 0."));

		this.tabChromo		= new Chromosome[nbC];

		if	(nbObjectif > 0)
			{
			this.nbNotes = nbObjectif;
			this.notes = new double[this.nbNotes];
			}
		else
			throw(new IllegalArgumentException("Genome, Genome (int nbC, int nbOjectif) : le nombre d'objectif doit être > 0."));
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie la note de l'individu.
	 */
	public void setNote (double d) { this.notes[0] = d; }


	/**
	 * Retourne la note de l'individu.
	 */
	public double getNote () { return this.notes[0]; }


	/**
	 * Modifie la note de l'individu pour le <code>i</code>ème objectif.
	 * Le premier objectfi est à l'indice 0.
	 */
	public void setNote (double d, int i) { this.notes[i] = d; }


	/**
	 * Retourne la note de l'individu pour le <code>i</code>ème objectif.
	 */
	public double getNote (int i) { return this.notes[i]; }


	/**
	 * Retourne le nombre d'objectifs.
	 */
	public int getNbObjectif () { return this.nbNotes; }


	/**
	 * Relie l'individu à sa <code>Population</code> d'appartenance.
	 */
	public void setPopulation (Population p) { this.population = p; }


	/**
	 * Retourne la <code>Population</code> d'appartenance de l'individu.
 	 */
 	public Population getPopulation () { return this.population; }


	/**
	 * Ajoute un <code>Chromosome</code> au <code>Genome</code> de l'individu.
	 *
	 * @param	ch		<code>Chromosome</code> ajouté
	 *
	 * @exception	IndexOutOfBoundsException si le <code>Genome</code> est
	 *				déjà plein
	 */
	public void addChromosome (Chromosome	ch)
		{
		int i = 0;
		while (i < this.nbChromo && this.tabChromo[i] != null) i++;

		if	(i == this.nbChromo)
			throw new IndexOutOfBoundsException("Genome, addChromosome  (Chromosome ch, int mode) : impossible d'ajouter un Chromosome, le Genome est plein.");
		else
			this.tabChromo[i] = ch;
		}


	/**
	 * Retourne le <code>Chromosome</code> en position <code>i</code>.
	 */
	public Chromosome getChromosome (int i)
		{
		try	{
			return this.tabChromo[i];
			} catch (ArrayIndexOutOfBoundsException e)
				{ throw new IndexOutOfBoundsException("Genome, getChromosome (int i) : indice 'i' hors des limites."); }
		}


	/**
	 * Retourne le nombre de <code>Chromosome</code>s composant le
	 * <code>Genome</code> de l'individu.
	 */
	public int size () { return this.nbChromo; }


	/**
	 * Croisement de deux individus.
	 *
	 * @param	g	<code>Genome</code> qui va être croisé avec this
	 *
	 * @return	un tableau de deux <code>Genome</code>s
	 */
	public Genome[] croisement (Genome g)
		{
		Genome g0, g1;

		try	{
			g0 = (Genome)this.clone();
			g1 = (Genome)this.clone();
			} catch (CloneNotSupportedException x) { return null; }

		g0.tabChromo		= new Chromosome[this.nbChromo];
		g0.notes			= new double[this.nbNotes];

		g1.tabChromo		= new Chromosome[this.nbChromo];
		g1.notes			= new double[this.nbNotes];

		for (int i=0 ; i<this.nbChromo ; i++)
			{
			Chromosome[] ch = this.tabChromo[i].croisement(g.tabChromo[i]);

			g0.tabChromo[i] = ch[0];
			g1.tabChromo[i] = ch[1];
			}

		Genome[] ge = new Genome[2];
		ge[0] = g0;
		ge[1] = g1;

		return ge;
		}


	/**
	 * Mutation sur un individu.
	 */
	public boolean mutation (double Pm) { return this.tabChromo[(int)(PseudoRandomNumbers.random()*this.nbChromo)].mutation(Pm); }


	/**
	 * Crée une copie de l'individu ("deep copy").
	 */
	public Genome copier ()
		{
		try	{
			Genome g = (Genome)this.clone();

			g.tabChromo	= new Chromosome[this.nbChromo];
			g.notes		= new double[this.nbNotes];

			for (int i=0 ; i<this.nbChromo ; i++) g.tabChromo[i] = this.tabChromo[i].copier();
			System.arraycopy(this.notes, 0, g.notes, 0, this.nbNotes);

			return g;
			} catch (CloneNotSupportedException x) { return null; }
		}


	/**
	 * Crée un nouveau individu.
	 * <p>
	 * Le <code>Genome</code> de cet individu aura la même structure que
	 * son créateur, même mode de croisement par <code>Chromosome</code>,
	 * mais ses données seront initialisées aléatoirement.
	 */
	public Genome creer ()
		{
		try	{
			Genome g = (Genome)this.clone();

			g.tabChromo	= new Chromosome[this.nbChromo];
			g.notes		= new double[this.nbNotes];

			for (int i=0 ; i<this.nbChromo ; i++) g.tabChromo[i] = this.tabChromo[i].creer();

			return g;
			} catch (CloneNotSupportedException x) { return null; }
		}


	/**
	 * Retourne la représentation sous forme de chaîne de caractères
	 * d'un individu.
	 */
	public StringBuilder afficher ()
		{
		StringBuilder sb = new StringBuilder();

		for (int i=0 ; i<this.nbChromo ; i++)
			sb.append(this.tabChromo[i].afficher()).append('\n');

		sb.append("Fitness : ");
		for (int i=0 ; i<this.nbNotes-1 ; i++)
			sb.append(Genome.DF.format(this.notes[i])).append(" ");
		sb.append(Genome.DF.format(this.notes[this.nbNotes-1]));

		return sb;
		}


	/**
	 * Modifie le format de représentation des réels (par exemple : les notes)
	 * en chaîne de caractères.
	 * <p>
	 * Par défaut le format est "0.000".
	 */
	public static void setDecimalFormat (DecimalFormat df) { Genome.DF = df; }


	/**
	 * Définition de la méthode <code>compare()</code>.
	 * <p>
	 * La comparaison entre les deux <code>Genome</code>s s'effectue sur leur
	 * capacité de survie (fitness).
	 *
	 * @param	g1	premier <code>Genome</code> à comparer
	 * @param	g2	second <code>Genome</code> à comparer
	 *
	 * @return	un nombre négatif, nul ou positif si la capacité de survie
	 *			(fitness) du premier <code>Genome</code> est plus petite, égale
	 *			ou plus grande que celle du second
	 */
	public int compare (Genome g1, Genome g2)
		{
		if	(g1.getNote() > g2.getNote())
			return 1;
		else
			if	(g1.getNote() < g2.getNote())
				return -1;
			else
				return 0;
		}


	/**
	 * Redéfinition de la méthode <code>toString()</code> de
	 * la classe <code>Object</code>.
	 * <p>
	 * Cette méthode ne peut pas être outrepassée par les classes dérivées
	 * de <code>Genome</code>.
	 */
	@Override
	public final String toString () { return this.afficher().toString(); }


	/*----------------------*/
	/* Méthodes à redéfinir */
	/*----------------------*/

	/**
	 * Calcule de la distance génotypique entre deux individus.
	 * <p>
	 * Si l'on souhaite utiliser l'heuristique de partage (Sharing)
	 * ou la compétition parent-enfant (Crowding),
	 * cette méthode doit être redéfinie en fonction du problème.
	 * <p>
	 * Elle doit renvoyer la distance génotypique entre les <code>Genome</code>s
	 * des deux individus.
	 *
	 * @return	valeur de la distance, 0.0 par défaut
	 */
	public double distance (Genome g) { return 0.0; }


	/**
	 * Regarde si l'individu <code>this</code> domine au sens de Pareto
	 * l'individu <code>g</code>.
	 * <p>
	 * Si l'on souhaite utiliser l'optimisation multiobjectif,
	 * cette méthode doit être redéfinie en fonction de problème.
	 * <p>
	 * Elle doit renvoyer <code>true</code> si l'individu <code>this</code>
	 * domine <code>g</code>.
	 *
	 * @return	<code>false</code> par défaut
	 */
	public boolean dominePareto (Genome g) { return false; }


	/*-------------------*/
	/* Méthode abstraite */
	/*-------------------*/

	/**
	 * Calcul de la note (fitness).
	 * <p>
	 * Cette méthode doit être obligatoirement définie pour
	 * affecter la ou les note(s) en fonction du problème à résoudre.
	 */
	abstract public void fitness ();

} /*----- Fin de la classe Genome -----*/
