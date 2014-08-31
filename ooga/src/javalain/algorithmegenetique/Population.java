package javalain.algorithmegenetique;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import javalain.algorithmegenetique.genome.GenomeFct;
import javalain.ea.EnsembleSolutionEA;
import javalain.ea.operator.Ranking_NSGAII;
import javalain.ea.operator.comparator.CrowdingComparator_NSGAII;
import javalain.ea.operator.comparator.CrowdingDistanceComparator;
import javalain.math.PseudoRandomNumbers;


/*------------*/
/* Population */
/*------------*/
/**
 * La classe <code>Population</code>.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	13 août 2010
 */
public class Population implements Serializable
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Liste des individus.
	 */
	private Genome[] tabGenome;


	/**
	 * Taille de la <code>Population</code>.
	 */
	private int nbGenome;


	/**
	 * Taille initiale de la <code>Population</code>.
	 */
	private int nbGenomeInitial;


	/**
	 * <code>Genome</code> de référence.
	 */
	private Genome pere;


	/**
	 * Paramètres de l'algorithme génétique.
	 */
	private ParametreAG paramAG;


	/**
	 * Indicateur d'initialisation de la <code>Population</code>.
	 */
	private boolean	estInitialisee;


	/**
	 * Données statistiques.
	 */
	private double	noteMax;
	private double	noteMin;
	private double	noteMoyenne;
	private int		positionMax;


	/**
	 * Enregistrement de données statistiques pendant <code>lgStat</code> générations.
	 */
	private boolean	enrgStat;		// Indicateur d'activation des statistiques
	private int		lgStat;			// Taille des tableaux statistiques
	private int		indiceStat;		// Position de la statistique courante dans les tableaux
	private double	statMax[];
	private double	statMoyenne[];


	/**
	 * Mesure de la durée d'une génération et de ses différentes étapes.
	 * <p>
	 * Il y a 6 étapes :
	 * <ul>
	 *	<li>de 0 à 1 : Initialisation.
	 *	<li>de 1 à 2 : Sélection.
	 *	<li>de 2 à 3 : Croisement.
	 *	<li>de 3 à 4 : Mutation.
	 *	<li>de 4 à 5 : Notation.
	 *	<li>de 5 à 6 : Calcul des statistiques.
	 * </ul>
	 */
	private long[]	temps = new long[7];


	/**
	 * Paramètres d'évolution de l'algorithme génétique.
	 */
	private String	algorithme;
	private double	tauxMutation;
	private double	tauxCroisement;
	private int		selection;
	private int		nbPartTournoi;
	private boolean	scaling;
	private boolean	sharing;
	private double	alpha;
	private double	sigmashare;
	private boolean	crowding;
	private boolean	croisementRestreint;
	private boolean	elitisme;
	private int		nbMeilleurs;
	private int		remplacement;
	private int		nbOverlap;
	private int		tdom;


	/**
	 * Données liées à la sélection par reste stochastique.
	 */
	private double	marge;
	private double	ecart;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit une <code>Population</code> vide.
	 * <p>
	 * Ce constructeur intialise le génome de référence, la taille de la
	 * <code>Population</code> et les paramètres d'évolution mais il ne crée pas
	 * les individus de cette <code>Population</code>.
	 * <p>
	 * Le <b>génome de référence</b> est le génome qui va servir à créer toute
	 * la <code>Population</code>. Chaque individu aura une structure génétique
	 * identique au génome de référence.
	 *
	 * @param	g	<code>Genome</code> de référence
	 * @param	nbG	 taille initiale de la population
	 * @param	p	ensemble des paramètres d'évolution
	 *
	 * @exception	IllegalArgumentException si <code>nbG</code> est <= 0
	 */
	public Population (Genome		g,
					   int			nbG,
					   ParametreAG	p)
		{
		if	(nbG > 0)
			this.nbGenomeInitial = nbG;
		else
			throw(new IllegalArgumentException("Population, Population (Genome g, int nbG, ParametreAG p) : la taille initiale de la population doit être > 0."));

		this.tabGenome			= new Genome[nbG];
		this.pere				= g;
		this.paramAG			= p;
		this.nbGenome			= 0;
		this.estInitialisee		= false;
		this.enrgStat			= false;
		}


	/**
	 * Initialise aléatoirement les individus de la population.
	 * <p>
	 * Cette méthode crée et d'initialise aléatoirement
	 * les individus de la <code>Population</code>.
	 */
	synchronized public void initialise ()
		{
		/*----- Initialisation -----*/
		this.nbGenome = 0;
		for (int i=0 ; i<this.nbGenomeInitial ; i++) this.add(this.pere.creer());
		for (int i=0 ; i<this.nbGenomeInitial ; i++) this.tabGenome[i].fitness();

		this.statistique();
		this.estInitialisee = true;

		/*----- Mise à jour des paramètres de l'algorithme -----*/
		this.updateParametreAG();

		/*
		   Initialisation spécifique pour NSGA II.
		   Recherche des différents fronts de Pareto afin de calculer
		   le rang de chaque individu et leur distance de crowding.
		 */
		if (this.algorithme.equals(ParametreAG.NSGA_2))
			{
			/*----- Transtypage du tableau des individus -----*/
			GenomeFct[] tabFct = new GenomeFct[this.nbGenome];
			System.arraycopy(this.tabGenome,0,tabFct,0,this.nbGenome);

			Ranking_NSGAII rk = new Ranking_NSGAII(tabFct);

			for (int i=0; i<rk.getNbFrontsPareto(); i++)
				rk.getFront(i).crowdingDistance();
			}

		/*----- Affichage de la population initial -----*/
//		for (int i=0; i<this.nbGenomeInitial; i++)
//			System.out.println(">---> Individu " + i + "\n" + this.tabGenome[i]);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie les paramètres d'évolution de l'algorithme génétique.
	 */
	public void setParametreAG (ParametreAG p) { this.paramAG = p; }


	/**
	 * Retourne les paramètres d'évolution de l'algorithme génétique.
	 */
	public ParametreAG getParametreAG () { return this.paramAG; }


	/**
	 * Mise à jour des paramètres d'évolution de l'algorithme génétique.
	 */
	private void updateParametreAG ()
		{
		this.algorithme				= this.paramAG.getAlgorithme();
		this.tauxMutation			= this.paramAG.getTauxMutation();
		this.tauxCroisement			= this.paramAG.getTauxCroisement();
		this.selection				= this.paramAG.getSelection();
		this.nbPartTournoi			= this.paramAG.getNbPartTournoi();
		this.scaling				= this.paramAG.getScaling();
		this.sharing				= this.paramAG.getSharing();
		this.alpha					= this.paramAG.getAlpha();
		this.sigmashare				= this.paramAG.getSigmaShare();
		this.crowding				= this.paramAG.getCrowding();
		this.croisementRestreint	= this.paramAG.getCroisementRestreint();
		this.elitisme				= this.paramAG.getElitisme();
		this.nbMeilleurs			= this.paramAG.getNbMeilleurs();
		this.remplacement			= this.paramAG.getRemplacement();
		this.nbOverlap				= this.paramAG.getNbOverlap();
		this.tdom					= this.paramAG.getTdom();
		}


	/**
	 * Ajoute un individu à la <code>Population</code>.
	 * <p>
	 * Cette méthode crée un nouvel individu aléatoirement, l'ajoute à la
	 * <code>Population</code> et calcule sa fitness.
	 */
	synchronized public void addIndividu ()
		{
		int taille = this.tabGenome.length;

		if	(this.nbGenome == taille)
			{
			int n_taille	= (taille*105)/100+1;
			Genome[] tab	= this.tabGenome;
			this.tabGenome	= new Genome[n_taille];

			System.arraycopy(tab,0,this.tabGenome,0,taille);
			}

		Genome g = this.pere.creer();
		g.population = this;
		this.tabGenome[this.nbGenome++] = g;
		g.fitness();
		}


	/**
	 * Ajoute un individu à la <code>Population</code>.
	 * <p>
	 * La fitness de l'individu est calculée après son ajout à la
	 * <code>Population</code>.
	 *
	 * @param	g	<code>Genome</code> de l'individu ajouté
	 *
	 * @exception	IllegalArgumentException si le <code>Genome</code ajouté est
	 *				différent des <code>Geneome</code>s déjà inclus dans la
	 *				<code>Population</code>
	 */
	synchronized public void addIndividu (Genome g)
		{
		if	(this.nbGenome != 0 && !this.pere.getClass().isInstance(g))
			throw new IllegalArgumentException("Population, addIndividu (Genome g) : tous les genomes doivent etre de meme type.");

		int taille = this.tabGenome.length;

		if	(this.nbGenome == taille)
			{
			int n_taille	= (taille*105)/100+1;
			Genome[] tab	= this.tabGenome;
			this.tabGenome	= new Genome[n_taille];

			System.arraycopy(tab,0,this.tabGenome,0,taille);
			}

		g.population = this;
		this.tabGenome[this.nbGenome++] = g;
		g.fitness();
		}


	/**
	 * Ajoute un individu à la <code>Population</code>.
	 */
	private void add (Genome g)
		{
		g.population = this;
		this.tabGenome[this.nbGenome++] = g;
		}


	/**
	 * Retourne l'individu en position <code>i</code>.
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	synchronized public Genome getIndividu (int i)
		{
		if	(i > this.nbGenome || i < 0)
			throw new IndexOutOfBoundsException("Population, getGenome (int i) : indice 'i' hors des limites.");

		return this.tabGenome[i];
		}


	/**
     * Supprime l'<code>Individu</code> en position <code>i</code>.
	 *
	 * @return	l'<code>Individu</code> supprimé
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	synchronized public Genome removeIndividu (int i)
		{
		if	(i >= this.nbGenome || i < 0)
			throw new IndexOutOfBoundsException("Population, removeIndividu (int i) : indice 'i' hors des limites.");

		Genome g = this.tabGenome[i];
		this.nbGenome--;

		for	(int j=i ; j<this.nbGenome ; j++) this.tabGenome[j] = this.tabGenome[j+1];

		this.tabGenome[this.nbGenome] = null;

		return g;
		}


	/**
     * Mise à jour de l'<code>Individu</code> en position <code>i</code>.
	 *
	 * @param	ge	le nouveau <code>Individu</code>
	 *
	 * @return	l'<code>Individu</code> remplacé
	 *
	 * @exception	IllegalArgumentException si l'<code>Individu</code>
	 *				remplaçant est différent des <code>Individu</code>s déjà
	 *				inclus dans la <code>Population</code>
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	synchronized public Genome setIndividu (int i, Genome ge)
		{
		if	(i >= this.nbGenome || i < 0)
			throw new IndexOutOfBoundsException("Population, setIndividu (int i, Genome ge) : indice 'i' hors des limites.");

		if	(this.nbGenome != 0 && !this.tabGenome[0].getClass().isInstance(ge))
			throw new IllegalArgumentException("Population, setIndividu (int i, Genome ge) : tous les genomes doivent etre de meme type.");

		Genome g = this.tabGenome[i];
		this.tabGenome[i] = ge;
		return g;
		}


	/**
	 * Retourne le <code>Genome</code> de référence.
	 */
	public Genome getParent () { return this.pere; }


	/**
	 * Activation et création des tableaux de sauvegardes statistiques.
	 *
	 * @param	taille	taille des tableaux statistiques
	 */
	public void setEnrgStat (int taille)
		{
		this.enrgStat		= true;
		this.lgStat			= taille;
		this.statMax		= new double[taille];
		this.statMoyenne	= new double[taille];

		this.indiceStat		= 0;
		this.statMax[0]		= this.noteMax;
		this.statMoyenne[0]	= this.noteMoyenne;
		}


	/**
	 * Teste l'état de la <code>Population</code>.
	 *
	 * @return	<code>true</code> si la <code>Population</code> est initialisée,
	 *			<code>false</code> sinon
	 */
	public boolean estInitialisee () { return this.estInitialisee; }


	/**
	 * Change le mode de croisement du <code>Chromosome</code> en position
	 * <code>i</code> de tous les individus.
	 */
//	synchronized public void setModeCroisement (int posChromosome,
//												int mode)
//		{
//		this.pere.setModeCroisement(posChromosome,mode);
//		}


	/**
	 * Retourne la taille de la <code>Population</code>.
	 */
	public int size () { return this.nbGenome; }


	/*-----------------------*/
	/* Opérations génétiques */
	/*-----------------------*/

	/**
	 * Calcule les notes de tous les individus de la <code>Population</code>.
	 * <p>
	 * Suite au calcul des notes, les statistiques sont recalculées.
	 */
	synchronized public void fitnessPopulation ()
		{
		for (int i=0 ; i<this.nbGenome ; i++)
			this.tabGenome[i].fitness();

		/*----- Calculs statistiques -----*/
		this.statistique();

		if	(this.enrgStat)
			{
			this.indiceStat = (this.indiceStat+1) % this.lgStat;
			this.statMax[this.indiceStat] = this.noteMax;
			this.statMoyenne[this.indiceStat] = this.noteMoyenne;
			}
		}


	/**
	 * Sélection par reste stochastique.
	 * <p>
	 * <b>Remarque:<b> Cette sélection ne marche que pour des notes positives.
	 */
	private int resteStochastique (double	tab_somme[],
								   int		numero)
		{
		int binf	= 0;
		int bsup	= this.nbGenome-1;
		int ctr;
		double d;
		double nb	= this.ecart*numero+this.marge;

		while (binf < bsup)
			{
			ctr	= (binf+bsup)>>1;
			d	= tab_somme[ctr];

			if	(d < nb)
				binf = ctr+1;
			else
				if	(d > nb)
					bsup = ctr-1;
				else
					return ctr;
			}

		return binf;
		}


	/**
	 * Sélection par roulette pipée.
	 * <p>
	 * <b>Remarque:<b> Cette sélection ne marche que pour des notes positives.
	 * <p>
	 * La roulette pipée est aussi utilisée pour la sélection par
	 * <code>RANG</code>. Dans ce cas les notes peuvent être négatives.
	 */
	private int roulettePipee (double tab_somme[])
		{
		int		binf	= 0;
		int		bsup	= this.nbGenome-1;
		int		ctr;
		double	d;
		double	nb		= PseudoRandomNumbers.random()*tab_somme[bsup];

		while (binf < bsup)
			{
			ctr	= (binf+bsup)>>1;
			d	= tab_somme[ctr];

			if	(d < nb)
				binf = ctr+1;
			else
				if	(d > nb)
					bsup = ctr-1;
				else
					return ctr;
			}

		return binf;
		}


	/**
	 * Tournoi à <code>N</code> participants.
	 * <p>
	 * <code>N</code> individus sont sélectionnés aléatoirement pour participer
	 * au tournoi, le vainqueur est celui qui a la plus forte note.
	 *
	 * @param	tab_note	tableau de notes des individus
	 *
	 * @return	l'indice de l'individu sélectionné
	 */
	private int tournoi (double tab_note[])
		{
		int		choix	= (int)(PseudoRandomNumbers.random()*this.nbGenome);
		int		indmax	= choix;
		double	notemax	= tab_note[choix];
		double	note;

		for (int i=1 ; i<this.nbPartTournoi ; i++)
			{
			choix = (int)(PseudoRandomNumbers.random()*this.nbGenome);
			note = tab_note[choix];

			if	(note > notemax)
				{
				notemax = note;
				indmax = choix;
				}
			}

		return indmax;
		}


	/**
	 * Tournoi inversé à <code>N</code> participants.
	 * <p>
	 * <code>N</code> individus sont sélectionnés aléatoirement pour participer
	 * au tournoi inversé, le vainqueur est celui qui a la plus petite note.
	 *
	 * @param	tab_note	tableau de notes des individus
	 *
	 * @return	l'indice de l'individu sélectionné
	 */
	private int tournoiInverse (double tab_note[])
		{
		int		choix	= (int)(PseudoRandomNumbers.random()*this.nbGenome);
		int		indmin	= choix;
		double	notemin	= tab_note[choix];
		double	note;

		for (int i=1 ; i<this.nbPartTournoi ; i++)
			{
			choix = (int)(PseudoRandomNumbers.random()*this.nbGenome);
			note = tab_note[choix];

			if	(note < notemin)
				{
				notemin = note;
				indmin = choix;
				}
			}

		return indmin;
		}


	/**
	 * Passage à la génération suivante.
	 */
	synchronized public void generation ()
		{
		this.temps[0] = System.currentTimeMillis();

		/*----- Variables locales -----*/
		int i,j,k;
		int cpt;

		Genome parent1, parent2;

		/*----- Populations intermédiaires -----*/
		int[]		p1;			// Population après la sélection
		Genome[]	p2;			// Population après les opérateurs génétiques
		int			nb_ind_pi;	// Nombre d'individus des populations intermédiaires p1 et p2

		/*----- Tableaux de calculs intermédiaires -----*/
		boolean	tab_change[];								// Tableau d'états des individus (CROISEMENT, MUTATION, ELITISME, NOTATION)
		double	tab_note[]	= new double[this.nbGenome];	// Tableau des notes des individus (SELECTION)
		boolean	tab_copie[]	= new boolean[this.nbGenome];	// Tableau des copies des individus (CROISEMENT)
		double	tab_somme[]	= new double[this.nbGenome];	// Tableau des sommes pour la roulette pipée, rang et reste stochastique (SELECTION)
		double	tab_share[];								// Tableau des "niche count" des individus (SELECTION)

		/*----- Mise à jour des paramètres de l'algorithme -----*/
		this.updateParametreAG();


		/*--------------------------------------------------------------------*/
		/* SELECTION : passage de la population this à p1                     */
		/*--------------------------------------------------------------------*/
		this.temps[1] = System.currentTimeMillis();


		/*-------------*/
		/* GGA ou SSGA */
		/*-------------*/
		if	(this.algorithme.equals(ParametreAG.GGA)	||
			 this.algorithme.equals(ParametreAG.SSGA))
			{
			/*----- Taille des populations intermédiaires -----*/
			if	(this.algorithme.equals(ParametreAG.SSGA))
				nb_ind_pi = this.nbOverlap;
			else
				nb_ind_pi = this.nbGenome;

			/*----- Initialisation du tableau des notes -----*/
			for (i=0 ; i<this.nbGenome ; i++)
				tab_note[i] = this.tabGenome[i].getNote();

			/*----- Application de l'heuristique de mise à l'échelle (Scaling) -----*/
			if	(this.scaling) this.fctScaling(tab_note);

			/*----- Application de l'heuristique de partage sur les notes (Sharing) -----*/
			if	(this.sharing)
				{
				tab_share = this.share();

				for (i=0 ; i<this.nbGenome ; i++) tab_note[i] /= tab_share[i];
				}

			/*----- Initialisation des modes de sélection -----*/
			switch(this.selection)
				{
				case ParametreAG.TOURNOI : break;

				case ParametreAG.RANG :
					{
					System.arraycopy(tab_note,0,tab_somme,0,this.nbGenome);

					Arrays.sort(tab_somme);

					for (i=0 ; i<this.nbGenome ; i++)
						{
						k = Arrays.binarySearch(tab_somme,tab_note[i]);

						/*
						   Traitement pour trouver l'indice le plus petit
						   correspondant à la note.
						 */
						while (k != 0 && tab_somme[k] == tab_somme[k-1]) k--;

						tab_note[i] = k;
						}

					tab_somme[0] = tab_note[0];
					for (i=1 ; i<this.nbGenome ; i++)
						tab_somme[i] = tab_somme[i-1]+tab_note[i];
					} break;

				case ParametreAG.ROULETTE_PIPEE :
					{
					if	(this.noteMin < 0.0)
						throw new IllegalArgumentException("Population, generation () : la selection par roulette pipee ne peut s'appliquer que sur un ensemble de notes positives.");

					tab_somme[0] = tab_note[0];
					for (i=1 ; i<this.nbGenome ; i++)
						tab_somme[i] = tab_somme[i-1]+tab_note[i];
					} break;

				case ParametreAG.RESTE_STOCHASTIQUE :
					{
					if	(this.noteMin < 0.0)
						throw new IllegalArgumentException("Population, generation () : la selection par reste stochastique ne peut s'appliquer que sur un ensemble de notes positives.");

					tab_somme[0] = tab_note[0];
					for (i=1 ; i<this.nbGenome ; i++)
						tab_somme[i] = tab_somme[i-1]+tab_note[i];

					this.ecart = tab_somme[this.nbGenome-1]/this.nbGenome;
					this.marge = PseudoRandomNumbers.random()*this.ecart;
					} break;

				default : throw new IllegalArgumentException("Population, generation () : methode de selection inconnue.");
				}

			/*----- Passage de la population this à p1 -----*/
			switch(this.selection)
				{
				case ParametreAG.TOURNOI :
					{
					/*----- Instanciation de p1 -----*/
					p1 = new int[nb_ind_pi];

					i = nb_ind_pi;
					while (i-- > 0) p1[i] = this.tournoi(tab_note);
					} break;

				case ParametreAG.RANG :

				case ParametreAG.ROULETTE_PIPEE :
					{
					/*----- Instanciation de p1 -----*/
					p1 = new int[nb_ind_pi];

					i = nb_ind_pi;
					while (i-- > 0) p1[i] = this.roulettePipee(tab_somme);
					} break;

				case ParametreAG.RESTE_STOCHASTIQUE	:
					{
					/*----- Instanciation de p1 -----*/
					p1 = new int[this.nbGenome];

					i = this.nbGenome;
					while (i-- > 0) p1[i] = this.resteStochastique(tab_somme,i);

					/*
					   Mélange aléatoire des individus sélectionnés.
					   Traitement spécifique pour éviter que les individus
					   identiques soient côte à côte. Cela serait dommageable
					   lors du croisement.
					 */
					i = 0;
					while (i < nb_ind_pi)
						{
						j = (int)(PseudoRandomNumbers.random()*(this.nbGenome-i)) + i;

						k = p1[i];
						p1[i] = p1[j];
						p1[j] = k;

						i++;
						}
					} break;

				default : throw new IllegalArgumentException("Population, generation () : methode de selection inconnue.");
				}
			}


		else
		/*------*/
		/* NPGA */
		/*------*/
		if	(this.algorithme.equals(ParametreAG.NPGA))
			{
			/*----- Taille des populations intermédiaires -----*/
			nb_ind_pi = this.nbGenome;

			/*----- Instanciation de p1 -----*/
			p1 = new int[nb_ind_pi];

//			tab_share = this.sharePareto();
			tab_share = this.share();

			/*----- Application de l'heuristique de mise à l'échelle (Scaling) -----*/
			if	(this.scaling) this.fctScaling(tab_share);

			/*----- Passage à la population this à p1 -----*/
			i = this.nbGenome;
			while (i-- > 0) p1[i] = NPGA(tab_share);
			}


		else
		/*------*/
		/* MOGA */
		/*------*/
		if	(this.algorithme.equals(ParametreAG.MOGA))
			{
			/*----- Taille des populations intermédiaires -----*/
			nb_ind_pi = this.nbGenome;

			/*----- Calcul du rang de dominance de chaque individu -----*/
			Arrays.fill(tab_note,1.0);

			for (i=0 ; i<this.nbGenome ; i++)
				for (j=0 ; j<this.nbGenome ; j++)
					if	(this.tabGenome[j].dominePareto(this.tabGenome[i])) tab_note[i]++;

			/*----- Fonction de transformation -----*/
			this.fctTransfMoga(tab_note);

			/*----- Application de l'heuristique de mise à l'échelle (Scaling) -----*/
			if	(this.scaling) this.fctScaling(tab_note);

			/*----- Application de l'heuristique de partage sur les notes (Sharing) -----*/
			if	(this.sharing)
				{
				tab_share = this.share();

				for (i=0 ; i<this.nbGenome ; i++) tab_note[i] /= tab_share[i];
				}

			/*----- Initialisation de la roullette pipée -----*/
			tab_somme[0] = tab_note[0];
			for (i=1 ; i<this.nbGenome ; i++)
				tab_somme[i] = tab_somme[i-1]+tab_note[i];

			/*----- Instanciation de p1 -----*/
			p1 = new int[nb_ind_pi];

			/*----- Passage à la population this à p1 par roulette pipée -----*/
			i = nb_ind_pi;
			while (i-- > 0) p1[i] = this.roulettePipee(tab_somme);
			}


		else
		/*------*/
		/* VEGA */
		/*------*/
		if	(this.algorithme.equals(ParametreAG.VEGA))
			{
			/*----- Taille des populations intermédiaires -----*/
			nb_ind_pi = this.nbGenome;

			/*----- Instanciation de p1 -----*/
			p1 = new int[nb_ind_pi];

			/*----- Variables locales -----*/
			int nb_obj			= this.tabGenome[0].getNbObjectif();	// Nombre d'objectifs
			int taille_partie	= this.nbGenome/nb_obj;					// Taille d'une partie
			int taille_sous_pop;										// Taille de la sous population

			/*----- Traitement pour chaque sous populations -----*/
			k = 0;
			do	{
				/*----- Calcul de la taille des sous populations -----*/
				if	(k < nb_obj-1)
					taille_sous_pop = taille_partie;
				else
					taille_sous_pop = this.nbGenome - k*taille_partie;

				/*----- Mise à jour du tableau des notes en fonction du k ième objectif -----*/
				for (i=0 ; i<this.nbGenome ; i++)
					tab_note[i] = this.tabGenome[i].getNote(k+1);

				/*----- Initialisation de la sélection -----*/
				switch(this.selection)
					{
					case ParametreAG.TOURNOI : break;

					case ParametreAG.RANG :
						{
						System.arraycopy(tab_note,0,tab_somme,0,this.nbGenome);

						Arrays.sort(tab_somme);

						for (i=0 ; i<this.nbGenome ; i++)
							{
							j = Arrays.binarySearch(tab_somme,tab_note[i]);

							/*
							   Traitement pour trouver l'indice le plus petit
							   correspondant à la note.
							 */
							while (j != 0 && tab_somme[j] == tab_somme[j-1]) j--;

							tab_note[i] = j;
							}

						tab_somme[0] = tab_note[0];
						for (i=1 ; i<this.nbGenome ; i++)
							tab_somme[i] = tab_somme[i-1]+tab_note[i];
						} break;

					case ParametreAG.RESTE_STOCHASTIQUE :

					case ParametreAG.ROULETTE_PIPEE :
						{
						if	(this.noteMin < 0.0)
							throw new IllegalArgumentException("Population, generation () : la selection par roulette pipee ne peut s'appliquer que sur un ensemble de notes positives.");

						tab_somme[0] = tab_note[0];
						for (i=1 ; i<this.nbGenome ; i++)
							tab_somme[i] = tab_somme[i-1]+tab_note[i];
						} break;

					default : throw new IllegalArgumentException("Population, generation () : methode de selection inconnue.");
					}

				/*----- Passage à la population this à p1 -----*/
				i = taille_sous_pop;
				switch(this.selection)
					{
					case ParametreAG.TOURNOI :
						{
						while (i-- > 0) p1[i + k*taille_partie] = this.tournoi(tab_note);
						} break;

					case ParametreAG.RANG :

					case ParametreAG.RESTE_STOCHASTIQUE :

					case ParametreAG.ROULETTE_PIPEE :
						{
						while (i-- > 0) p1[i + k*taille_partie] = this.roulettePipee(tab_somme);
						} break;

					default : throw new IllegalArgumentException("Population, generation () : methode de selection inconnue.");
					}

				/*----- Partie suivante -----*/
				k++;
				} while (k < nb_obj);

			/*----- Mélange des indices des individus sélectionnés avant l'application des opérateurs génétiques -----*/
			i = this.nbGenome;
			while (i > 0)
				{
				j = (int)(PseudoRandomNumbers.random()*(i--));

				k = p1[i];
				p1[i] = p1[j];
				p1[j] = k;
				}
			}


		else
		/*---------*/
		/* NSGA II */
		/*---------*/
		if	(this.algorithme.equals(ParametreAG.NSGA_2))
			{
			/*----- Taille des populations intermédiaires -----*/
			nb_ind_pi = this.nbGenome;

			/*----- Instanciation de p1 -----*/
			p1 = new int[nb_ind_pi];

			/*----- Passage à la population this à p1 par tournoi binaire NSGA II -----*/
			int indice1, indice2, choix;

			CrowdingComparator_NSGAII comparateur = new CrowdingComparator_NSGAII();

			i = nb_ind_pi;
			while (i-- > 0)
				{
				/*
				   Tournoi binaire NSGA II.
				 */
				indice1 = (int)(PseudoRandomNumbers.random()*nb_ind_pi);
				indice2 = (int)(PseudoRandomNumbers.random()*nb_ind_pi);

				choix = comparateur.compare((GenomeFct)this.tabGenome[indice1],(GenomeFct)this.tabGenome[indice2]);

				p1[i] = (choix == -1) ? indice2 : indice1;
				}
			}


//		else
//		/*----*/
//		/* ES */
//		/*----*/
//		if	(this.algorithme.equals(ParametreAG.ES))
//			{
//			System.out.println(ParametreAG.ES);
//			System.out.println("this.nbGenome = " + this.nbGenome);
//			System.out.println("this.nbOverlap = " + this.nbOverlap);
//			nb_ind_pi = this.nbGenome + this.nbOverlap;
//			System.out.println("nb_ind_pi = " + nb_ind_pi);
//
//			p1 = new int[nb_ind_pi];
//			}


		else
			throw new IllegalArgumentException("Population, generation () : methode evolutionnaire inconnue.");


		/*--------------------------------------------------------------------*/
		/* CROISEMENT : passage de la population p1 à p2                      */
		/*--------------------------------------------------------------------*/
		this.temps[2] = System.currentTimeMillis();

		/*----- Instanciation de p2 et de tab_change -----*/
		p2			= new Genome[nb_ind_pi];
		tab_change	= new boolean[nb_ind_pi];

		/*----- Initialisation du compteur sur la population p1 -----*/
		i = nb_ind_pi;

		/*----- Si la taille de p1 (et p2) est impaire, on copie et on mute un individu -----*/
		cpt = 0;
		if	(i % 2 == 1)
			{
			j = p1[--i];
			p2[cpt++] = this.tabGenome[j].copier();
			tab_change[0] = p2[0].mutation(1.0);
			}

		/*----- Calcul du nombre d'individus devant être croisés -----*/
		int nb;
		if	(this.algorithme.equals(ParametreAG.SSGA))
			nb = this.nbOverlap;
		else
			nb = (int)(this.nbGenome*this.tauxCroisement);

		/*----- Si NSGA II alors tab_copie est initialisé à 'true' car on effectue des copies systématiques -----*/
		if (this.algorithme.equals(ParametreAG.NSGA_2))
			Arrays.fill(tab_copie, true);

		/*----- Croisement 2 à 2 -----*/
		while (i>0)
			{
			j = p1[--i];
			parent1 = this.tabGenome[j];

			k = p1[--i];
			parent2 = this.tabGenome[k];

			if	(/*----- Condition sur le taux de croisement (PseudoRandomNumbers.random() <= this.tauxCroisement) -----*/
				cpt < nb &&
				 /*----- Croisement restreint -----*/
				(!this.croisementRestreint ||
				 (this.croisementRestreint && this.voisinCroisRestreint(parent1,parent2))))
				{
				Genome[] enfant = parent1.croisement(parent2);

				if	(this.crowding)
					{
					/*----- Crowding -----*/
					p2[cpt] = enfant[0]; tab_change[cpt++] = true;
					p2[cpt] = enfant[1]; tab_change[cpt++] = true;
					}
				else
 					{
					p2[cpt] = enfant[0]; tab_change[cpt++] = true;
					p2[cpt] = enfant[1]; tab_change[cpt++] = true;
					}
				}
			else
				/*----- Pas de croisement -----*/
				{
				if	(tab_copie[j])
					{
					p2[cpt++] = parent1.copier();
					}
				else
					{
					p2[cpt++] = parent1; tab_copie[j] = true;
					}

				if	(tab_copie[k])
					{
					p2[cpt++] = parent2.copier();
					}
				else
					{
					p2[cpt++] = parent2; tab_copie[k] = true;
					}
				}
			}


		/*--------------------------------------------------------------------*/
		/* MUTATION sur la population p2                                      */
		/*--------------------------------------------------------------------*/
		this.temps[3] = System.currentTimeMillis();

		for (i=0 ; i<nb_ind_pi ; i++)
			tab_change[i] |= p2[i].mutation(this.tauxMutation);


		/*--------------------------------------------------------------------*/
		/* NOTATION                                                           */
		/*--------------------------------------------------------------------*/
		this.temps[4] = System.currentTimeMillis();

		for (i=0 ; i<nb_ind_pi ; i++)
			if	(tab_change[i]) p2[i].fitness();

		/*----- Version parallèle -----*/

//		Il faudrait peut-être créer les thread à l'initialisation de
//		la population et non pas à chaque génération.

//		int					nb_thread	= 2;											// Nombre de threads
//		ThreadNotation[]	tab_thread	= new ThreadNotation[nb_thread];				// Tableau des threads
//		Barriere			barriere	= new Barriere(nb_thread);						// Barrière
//		Ordonnanceur		ord_stat	= new Ordonnanceur(0, p2.length-1, nb_thread);	// Ordonnanceur
//
//		for	(int l=0; l<nb_thread; l++)
//			{
//			Bloc bloc = ord_stat.getBloc();
//			tab_thread[l] = new ThreadNotation(p2, tab_change, bloc.debut, bloc.fin, barriere);
//			tab_thread[l].start();
//			}
//
//		for	(int l=0; l<nb_thread; l++)
//			{
//			try	{
//				tab_thread[l].join();
//				} catch (InterruptedException e) { }
//			}


		/*--------------------------------------------------------------------*/
		/* REMPLACEMENT                                                       */
		/*--------------------------------------------------------------------*/


		/*------*/
		/* SSGA */
		/*------*/
		if	(this.algorithme.equals(ParametreAG.SSGA))
			{
			i = nb_ind_pi;

			switch(this.remplacement)
				{
				case ParametreAG.LES_PLUS_VIEUX :

				case ParametreAG.PERSO :

				case ParametreAG.LES_PLUS_MAUVAIS :
					{
					/*----- Remplacement : regroupement et tri -----*/
					Genome[] p3 = new Genome[this.nbGenome+this.nbOverlap];

					System.arraycopy(this.tabGenome,0,p3,0,this.nbGenome);
					System.arraycopy(p2,0,p3,this.nbGenome,this.nbOverlap);

					/*
					   Tri de la population. Cela n'a aucune conséquence pour
					   la sélection suivante.
					 */
					Arrays.sort(p3,this.tabGenome[0]);

					System.arraycopy(p3,this.nbOverlap,this.tabGenome,0,this.nbGenome);
					} break;

				case ParametreAG.ALEATOIRE :
					{
					while (i-- > 0) this.tabGenome[(int)(PseudoRandomNumbers.random()*this.nbGenome)] = p2[i];
					} break;

				case ParametreAG.TOURNOI_INVERSE :
					{
					while (i-- > 0) this.tabGenome[this.tournoiInverse(tab_note)] = p2[i];
					} break;

				default : throw new IllegalArgumentException("Population, generation () : methode de remplacement inconnue.");
				}
			}


		else
		/*---------*/
		/* NSGA II */
		/*---------*/
		if	(this.algorithme.equals(ParametreAG.NSGA_2))
			{
			/*----- Union de this.tabGenome (Pt) avec p2 (Qt) -----*/
			GenomeFct[] Rt = new GenomeFct[this.nbGenome+nb_ind_pi];

			System.arraycopy(this.tabGenome,0,Rt,0,this.nbGenome);
			System.arraycopy(p2,0,Rt,this.nbGenome,nb_ind_pi);

			/*----- Recherche des différents fronts de Pareto -----*/
			Ranking_NSGAII rkn = new Ranking_NSGAII(Rt);

			/*----- Construction de la prochaine population -----*/
			i = this.nbGenomeInitial;
			k = 0;
			EnsembleSolutionEA front = rkn.getFront(k);

			while (i>=front.size())
				{
				/*----- Calcul des distances de crowding -----*/
				front.crowdingDistance();

				/*----- Ajout des éléments du front dans la prochaine génération -----*/
				i -= front.size();
				for (j=front.size()-1; j>=0; j--)
					this.tabGenome[i+j] = (GenomeFct)front.get(j);

				/*----- Front suivant -----*/
				front = rkn.getFront(++k);
				}

			/*----- On complète la prochaine génération par les éléments du front ayant les distances de crowding les plus grandes -----*/
			if (i>0)
				{
				/*----- Calcul des distances de crowding -----*/
				front.crowdingDistance();

				/*----- Tri du front en fonction de la distance de crowding -----*/
				front.sort(new CrowdingDistanceComparator());

				/*----- Ajout des éléments -----*/
				for (j=i-1; j>=0; j--)
					this.tabGenome[j] = (GenomeFct)front.get(j);
				}
			}


		else
			{
			/*----- GGA et élitisme -----*/
			if (this.elitisme && this.algorithme.equals(ParametreAG.GGA))
				{
				i = 5;
				do	{
					j = (int)(PseudoRandomNumbers.random()*nb_ind_pi);
					} while (i-- > 0 && p2[j].getNote() > this.noteMax);

				p2[j] = this.tabGenome[this.positionMax].copier();
				tab_change[j] = false;
				}

			/*
			   Pas de remplacement spécifique, la population formée par
			   les enfants remplace celle des parents.
			 */
			this.tabGenome = p2;
			}


		/*--------------------------------------------------------------------*/
		/* STATISTIQUES : notes et durée de la génération                     */
		/*--------------------------------------------------------------------*/
		this.temps[5] = System.currentTimeMillis();

		this.statistique();

		if	(this.enrgStat)
			{
			this.indiceStat = (this.indiceStat+1) % this.lgStat;
			this.statMax[this.indiceStat] = this.noteMax;
			this.statMoyenne[this.indiceStat] = this.noteMoyenne;
			}

		this.temps[6] = System.currentTimeMillis();
		} /*----- Fin de generation -----*/


	/*----------------------------------------------------*/
	/* Méthodes de calculs statistiques sur la population */
	/*----------------------------------------------------*/

	/**
	 * Retourne la note la plus élevée de la <code>Population</code>.
	 */
	public double getNoteMax () { return this.noteMax; }


	/**
	 * Retourne la note la moins élevée de la <code>Population</code>.
	 */
	public double getNoteMin () { return this.noteMin; }


	/**
	 * Retourne la note moyenne de la <code>Population</code>.
	 */
	public double getNoteMoyenne () { return this.noteMoyenne; }


	/**
	 * Retourne la position du meilleur individu de la <code>Population</code>.
	 */
	public int getPositionMax () { return this.positionMax; }


	/**
	 * Retourne la note la plus élevée d'une génération précédente.
	 *
	 * @param	i	représente le décalage entre le génération courante et la
	 *				génération précédente dont on veut obtenir la note maximale
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est supérieur à
	 *				la taille des tableaux statistiques
	 */
	public double statMax (int i)
		{
		if	(i >= this.lgStat)
			throw new IndexOutOfBoundsException("Population, statMax (int i) : statistique indisponible (tableau trop petit ou indice mal gere).");

		int indice = this.indiceStat - i;
		if	(indice < 0) indice += this.lgStat;

		return this.statMax[indice];
		}


	/**
	 * Retourne la note moyenne d'une génération précédente.
	 *
	 * @param	i	représente le décalage entre le génération courante et la
	 *				génération précédente dont on veut obtenir la note moyenne
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est supérieur à
	 *				la taille des tableaux statistiques
	 */
	public double statMoyenne (int i)
		{
		if	(i >= this.lgStat)
			throw new IndexOutOfBoundsException("Population, statMoyenne (int i) : statistique indisponible (tableau trop petit ou indice mal gere).");

		int indice = this.indiceStat - i;
		if	(indice < 0) indice += this.lgStat;

		return this.statMoyenne[indice];
		}


	/**
	 * Effectue les calculs statistiques sur la <code>Population</code>.
	 */
	private void statistique ()
		{
		double	valeur;
		double	somme = 0.0;
		double	max = Double.NEGATIVE_INFINITY;
		double	min = Double.POSITIVE_INFINITY;
		int		pos = 0;

		for (int i=0 ; i<this.nbGenome ; i++)
			{
			valeur = this.tabGenome[i].getNote();
			somme += valeur;

			if	(valeur > max)
				{
				max = valeur;
				pos = i;
				}
			if	(valeur < min) min = valeur;
			}

		this.noteMax = max;
		this.noteMin = min;
		this.positionMax = pos;
		this.noteMoyenne = somme/this.nbGenome;
		}


	/**
	 * Affiche les statistiques sur les notes.
	 */
	public void affStatistique ()
		{
		DecimalFormat df = new DecimalFormat("0.0000000");

		StringBuilder sb = new StringBuilder("Notes : Max  ");
		sb.append(df.format(this.noteMax));
		sb.append("\tMoyenne  ");
		sb.append(df.format(this.noteMoyenne));

		System.out.println(sb.toString());
		}


	/**
	 * Retourne la durée de la génération.
	 */
	public long getDureeGeneration () { return this.temps[6]-this.temps[0]; }


	/**
	 * Retourne le temps de calcul de la sélection.
	 */
	public long getDureeSelection () { return this.temps[2]-this.temps[1]; }


	/**
	 * Retourne le temps de calcul du croisement.
	 */
	public long getDureeCroisement () { return this.temps[3]-this.temps[2]; }


	/**
	 * Retourne le temps de calcul de la mutation.
	 */
	public long getDureeMutation () { return this.temps[4]-this.temps[3]; }


	/**
	 * Retourne le temps de calcul de la fitness.
	 */
	public long getDureeFitness () { return this.temps[5]-this.temps[4]; }


	/**
	 * Affiche les statistiques sur le temps de calcul d'une génération.
	 */
	public void affStatTpsGeneration ()
		{
		DecimalFormat df = new DecimalFormat("0.00");
		double d = this.temps[6]-this.temps[0];
		StringBuilder sb;

		if	(d == 0.0)
			sb = new StringBuilder("Temps : ?");
		else
			{
			sb = new StringBuilder("Temps : S ");
			sb.append(df.format((this.temps[2]-this.temps[1])*100.0/d));
			sb.append("%  C ");
			sb.append(df.format((this.temps[3]-this.temps[2])*100.0/d));
			sb.append("%  M ");
			sb.append(df.format((this.temps[4]-this.temps[3])*100.0/d));
			sb.append("%  N ");
			sb.append(df.format((this.temps[5]-this.temps[4])*100.0/d));
			sb.append("%");
			}

		System.out.println(sb.toString());
		}


	/**
	 * Affiche le meilleur individu.
	 */
	synchronized public void affMeilleurIndividu ()
		{
		StringBuilder sb = new StringBuilder("Meilleur Individu : \n");
		sb.append(this.tabGenome[this.positionMax].afficher());

		System.out.println(sb.toString());
		}


	/*---------*/
	/* Scaling */
	/*---------*/

	/**
	 * Fonction de transformation appliquée pour la mise à l'échelle (Scaling)
	 * des notes avant la sélection des individus. Le but de cette fonction est
	 * de modifier l'influence des notes les unes par rapport aux autres.
	 * <p>
	 * Par défaut, cette fonction ne modifie pas les notes. Mais l'utilisateur
	 * peut définir sa propre fonction en rédéfinissant cette méthode.
	 * <ul>
	 *	<li>Scaling linéaire.
	 *	<li>Scaling exponentiel.
	 *	<li>Scaling en fonction des l'âge de l'individu.
	 *	<li>...
	 * </ul>
	 *
	 * @param	tab	tableau contenant la note de chaque individu en entrée, et,
	 *				en sortie les notes obtenues après l'application de la
	 *				fonction de mise à l'échelle
	 *
	 * @see		javalain.algorithmegenetique.ParametreAG#setScaling
	 */
	public void fctScaling (double[] tab) { }


	/*----------------------*/
	/* Croisement restreint */
	/*----------------------*/

	/**
	 * Regarde si deux <code>Genome</code>s sont génotypiquement voisins pour
	 * l'utilisation de l'heuristique de croisement restreint.
	 * <p>
	 * Par défaut cette méthode retourne <code>true</code>.
	 *
	 * @param	g1	<code>Genome</code>
	 * @param	g2	<code>Genome</code>
	 *
	 * @return	<code>true</code> si <code>g2</code> est dans le voisinage
	 *			génotypique de <code>g1</code>, <code>false</code> sinon
	 *
	 * @see		javalain.algorithmegenetique.Genome#distance
	 */
	public boolean voisinCroisRestreint (Genome g1,
										 Genome g2)
		{
		return true;
		}


	/*---------*/
	/* Sharing */
	/*---------*/

	/**
	 * Regarde si deux <code>Genome</code>s sont voisins pour l'utilisation de
	 * l'heuristique de partage (Sharing).
	 * <p>
	 * Par défaut cette méthode regarde si la distance entre <code>g1</code> et
	 * <code>g2</code> est inférieure ou égale à <code>sigmashare</code>.
	 *
	 * @param	g1	<code>Genome</code>
	 * @param	g2	<code>Genome</code>
	 *
	 * @return	<code>true</code> si <code>g2</code> est dans le voisinage de
	 *			<code>g1</code>, <code>false</code> sinon
	 *
	 * @see		javalain.algorithmegenetique.Genome#distance
	 */
	public boolean voisinSharing (Genome g1,
								  Genome g2)
		{
		if	(g1.distance(g2) <= this.sigmashare)
			return true;
		else
			return false;
		}


	/**
	 * Calcule le "niche count" <code>mi</code> de chaque individu.
	 * <p>
	 * Le sharing utilisé est :
	 * <p>
	 * <b>Remarque:</b> L'utilisation de cette méthode suppose que la
	 *					distance définie est telle que dist(a,b) = dist(b,a)
	 *					et que tous les individus ont la même valeur de
	 *					voisinage <code>sigmashare</code>.
	 *
	 * @return	tableau des "niche count" <code>mi</code> de chaque individu
	 */
	private double[] share ()
		{
		int			taille = this.nbGenome;
		double[]	tab = new double[taille];

		Genome	g;
		double	distance;
		int		i,j;

		Arrays.fill(tab,1.0);
		for (i=taille-2 ; i>=0; i--)
			{
			g = this.tabGenome[i];
			for (j=i+1 ; j<taille ; j++)
				{
				distance = g.distance(this.tabGenome[j]);
				if	(distance <= this.sigmashare)
					{
					distance = (1 - Math.pow(distance/this.sigmashare,this.alpha));
					tab[i] += distance;
					tab[j] += distance;
					}
				}
			}

		return tab;
		}


	/*---------------*/
	/* Multiobjectif */
	/*---------------*/

	/**
	 * Calcule la distance du plus proche voisin pour Pareto (Sharing).
	 * <p>
	 * <b>Remarque:</b> L'utilisation de cette méthode suppose que la
	 *					distance définie est telle que dist(a,b) = dist(b,a).
	 *
	 * @return	tableau des distance des plus proches voisins
	 */
	private double[] sharePareto ()
		{
		int			taille = this.nbGenome;
		double[]	tab = new double[taille];

		Genome	g;
		double	distance;
		int		i,j;

		g = this.tabGenome[0];
		tab[0] = g.distance(this.tabGenome[1]);

		tab[1] = tab[0];
		for (i=2 ; i<this.nbGenome ; i++)
			{
			tab[i] = g.distance(this.tabGenome[i]);
			if	(tab[i] < tab[0]) tab[0] = tab[i];
			}

		for (i=this.nbGenome-2 ; i>0 ; i--)
			{
			g = this.tabGenome[i];
			for (j=i+1 ; j<this.nbGenome ; j++)
				{
				distance = g.distance(this.tabGenome[j]);
				if	(distance < tab[i]) tab[i] = distance;
				if	(distance < tab[j]) tab[j] = distance;
				}
			}

		return tab;
		}


	/**
	 * Méthode de Horn (Niched Pareto GA).
	 *
	 * @param	tab_share	tableau de valeurs des "niche count"
	 *
	 * @return	la position dans la <code>Population</code> de l'individu
	 *			sélectionné
	 */
	private int NPGA (double tab_share[])
		{
		/*----- Sélection de deux individus -----*/
		int a = (int)(PseudoRandomNumbers.random()*this.nbGenome);
		int b = (int)(PseudoRandomNumbers.random()*this.nbGenome);

		/*----- Domination directe entre deux individus -----*/
		//if	(this.getIndividu(a).dominePareto(this.getIndividu(b))) return a;
		//if	(this.getIndividu(b).dominePareto(this.getIndividu(a))) return b;

		/*----- Domination sur un ensemble d'individus -----*/
		boolean a_est_domine = false;
		boolean b_est_domine = false;

		Genome g;
		int cpt = this.tdom;

		while (cpt > 0)
			{
			g = this.tabGenome[(int)(PseudoRandomNumbers.random()*this.nbGenome)];
			if	(g.dominePareto(this.tabGenome[a])) a_est_domine = true;
			if	(g.dominePareto(this.tabGenome[b])) b_est_domine = true;

			cpt--;
			}

		if	(a_est_domine)
			{
			if	(b_est_domine)
				return ((tab_share[a] < tab_share[b]) ? a : b);	// à utiliser avec la fonction de sharing "share"
//				return ((tab_share[a] > tab_share[b]) ? a : b);	// à utiliser avec la fonction de sharing "sharePareto"
			else
				return b;	// a dominé et b non dominé
			}
		else
			{
			if	(b_est_domine)
				return a;	// a non dominé et b dominé
			else
				return ((tab_share[a] < tab_share[b]) ? a : b);	// à utiliser avec la fonction de sharing "share"
//				return ((tab_share[a] > tab_share[b]) ? a : b);	// à utiliser avec la fonction de sharing "sharePareto"
			}
		}


	/**
	 * Fonction de transformation appliquée lors de l'utilisation de
	 * la méthode <code>MOGA</code>.
	 * <p>
	 * Par défaut, cette fonction est <code>max_rang + 1 - rang</code>.
	 * Mais l'utilisateur peut définir sa propre fonction en rédéfinissant
	 * la méthode et en respectant le principe : le plus fort = meilleure note.
	 * <p>
	 * L'utilisateur peut également appliquer un changement d'échelle (Scaling)
	 * qui sera effectué après la fonction de transformation.
	 *
	 * @param	tab	tableau contenant le rang de chaque individu en entrée, et,
	 *				en sortie les notes obtenues par application de la fonction
	 *				de transformation sur le rang de chaque individu
	 *
	 * @see	javalain.algorithmegenetique.Population#fctScaling
	 */
	public void fctTransfMoga (double tab[])
		{
		int i;
		double max = 0.0;

		for (i=0 ; i<this.nbGenome ; i++)
			if	(tab[i] > max) max = tab[i];

		max += 1.0;
		for (i=0 ; i<this.nbGenome ; i++) tab[i] = max - tab[i];
		}

} /*----- Fin de la classe Population -----*/
