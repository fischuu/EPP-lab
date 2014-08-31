package javalain.algorithmegenetique;

import javalain.math.PseudoRandomNumbers;

/*--------------------*/
/* ChromosomeVariable */
/*--------------------*/
/**
 * Un <code>ChromosomeVariable</code> a une taille variable et
 * tous ses éléments sont des <code>Genes</code> de même type.
 * <p>
 * Vous devez impérativement créer une instance de classe
 * <code>DomaineEntier</code> afin de définir le domaine de valeurs de
 * la longueur de votre <code>ChromosomeVariable</code>. Ce domaine ne sert que
 * pour la création aléatoire du chromosome. Mais après croisement ou mutation,
 * le chromosome peut avoir une longueur hors du domaine.
 * <p>
 * <b>Conseil:</b>	Déclarer l'instance de <code>DomaineEntier</code> en
 *					<i>static</i> dans les données de votre <code>Genome</code>
 *					afin de limiter la place mémoire occupée.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromosomeVariable extends Chromosome
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
     * Nombre de cases ajoutées à <code>tabGene</code> lors du croisement et
	 * de la création.
	 * <p>
	 * Cela évite un nombre important d'augmentation de la taille de
	 * <code>tabGene</code>.
	 */
	private final static int PLUS = 2;


	/**
     * Mode de croisement par GENE.
	 * <p>
	 * Sur chaque parent, un point de croisement situé à la même position est
	 * choisi puis les segments à droite de ce point sont échangés pour former
	 * les deux enfants.
	 * <p>
	 * Ce mode permet de diffuser les <code>Gene</code>s dans la population.
	 */
	public final static int GENE = 0x00000001;


	/**
     * Mode de croisement par MELANGE.
	 * <p>
	 * Sur chaque parent, un segment de <code>Gene</code>s de même longueur est
	 * choisi . Les deux segments sont ensuite mélangés. Le nouveau segment
	 * obtenu remplace le segment ayant participé au mélange de chaque parent
	 * pour former les deux enfants. La longueur du segment est supérieur à 0
	 * et inférieur à la moitié du + petit chromosome.
	 * <p>
	 * Ce mode permet de mélanger les <code>Gene</code>s de façon à augmenter
	 * la diversité génétique.
	 */
	public final static int MELANGE = 0x00000010;


	/**
     * Mode de croisement par CARACTERE.
	 * <p>
	 * Ce mode est identique au mode GENE mais ne s'applique que sur des
	 * <code>GeneComplexe</code>. Il permet de choisir un point de croisement
	 * interne au <code>GeneComplexe</code>.
	 */
	public final static int CARACTERE = 0x00000101;


	/**
     * Type de <code>ChromosomeVariable</code> dont tous les <code>Gene</code>s
	 * sont du même type.
	 */
	private final static int SIMPLE = 0x00000011;


	/**
     * Type de <code>ChromosomeVariable</code> dont tous les <code>Gene</code>s
	 * sont du même type <code>GeneComplexe</code>.
	 */
	private final static int COMPLEXE = 0x00000111;


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Type du <code>ChromosomeVariable</code>.
	 */
	private int type;


	/**
     * Liste des <code>Gene</code>s composant le
	 * <code>ChromosomeVariable</code>.
	 */
	private Gene tabGene[];


	/**
     * Nombre de <code>Gene</code>s composant le
	 * <code>ChromosomeVariable</code>.
	 */
	private int	nbGene;


	/**
	 * Mode de croisement du <code>ChromosomeVariable</code>.
	 */
	protected int modeCroisement;


	/**
	 * Mode de mutation du <code>ChromosomeVariable</code>.
	 */
	protected int modeMutation;


	/**
     * Domaine d'initialisation de la longueur du
	 * <code>ChromosomeVariable</code>.
	 */
	public DomaineEntier lg;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
     * Construit et initialise un <code>ChromosomeVariable</code> avec le type
	 * de <code>Gene</code> passé en paramètre.
	 * <p>
	 * Sa taille sera choisie aléatoirement dans le domaine de valeurs
	 * <code>de</code>.
	 *
	 * @param	g			type du <code>Gene</code> composant le
	 *						<code>ChromosomeVariable</code>
	 * @param	mode_crois	mode de croisement du <code>ChromosomeVariable</code>
	 * @param	de			domaine de valeurs pour l'initialisation du nombre
	 *						de <code>Gene</code>s du
	 *						<code>ChromosomeVariable</code>
	 */
	public ChromosomeVariable (Gene				g,
							   int				mode_crois,
							   DomaineEntier	de)
		{
		this.lg			= de;
		this.nbGene		= (int)(PseudoRandomNumbers.random()*de.getTaille()) + de.getMin();
		this.tabGene	= new Gene[this.nbGene];

		for (int i=0 ; i<this.nbGene ; i++) this.tabGene[i] = g.creer();

		if	(g instanceof GeneComplexe)
			type = ChromosomeVariable.COMPLEXE;
		else
			type = ChromosomeVariable.SIMPLE;

		this.modeCroisement = mode_crois;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
     * Ajoute un <code>Gene</code> au <code>ChromosomeVariable</code>.
	 *
	 * @exception	IllegalArgumentException si le <code>Gene</code> ajouté est
	 *				différent des <code>Gene</code>s déjà inclus dans le
	 *				<code>ChromosomeVariable</code>
	 */
	public void addGene (Gene ge)
		{
		if	(this.nbGene != 0 && !this.tabGene[0].getClass().isInstance(ge))
			throw new IllegalArgumentException("ChromosomeVariable, addGene (Gene ge) : tous les genes doivent etre de meme type.");

		int taille = this.tabGene.length;

		if	(this.nbGene == taille)
			{
			int n_taille	= (taille*3)/2+1;
			Gene[] tab		= this.tabGene;
			this.tabGene	= new Gene[n_taille];

			System.arraycopy(tab, 0, this.tabGene, 0, taille);
			}

		this.tabGene[this.nbGene++] = ge;
		}


	/**
     * Ajoute un <code>Gene</code> au <code>ChromosomeVariable</code> en
	 * position <code>i</code>.
	 *
	 * @exception	IllegalArgumentException si le <code>Gene</code> ajouté est
	 *				différent des <code>Gene</code>s déjà inclus dans le
	 *				<code>ChromosomeVariable</code>
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	public void addGene (int i,
						 Gene ge)
		{
		if	(i >= this.nbGene || i < 0)
			throw new IndexOutOfBoundsException("ChromosomeVariable, addGene (int i, Gene g) : indice 'i' hors des limites.");

		if	(this.nbGene != 0 && !this.tabGene[0].getClass().isInstance(ge))
			throw new IllegalArgumentException("ChromosomeVariable, addGene (int i, Gene g) : tous les genes doivent etre de meme type.");

		int taille = this.tabGene.length;
		int j;

		if	(this.nbGene == taille)
			{
			int n_taille	= (taille*3)/2+1;
			Gene[] tab		= this.tabGene;
			this.tabGene	= new Gene[n_taille];

			for	(j=0; j<taille ; j++) this.tabGene[j] = tab[j];
			}

		for	(j=this.nbGene ; j>i ; j--) this.tabGene[j] = this.tabGene[j-1];

		this.tabGene[i] = ge;
		this.nbGene++;
		}


	/**
     * Retourne le <code>Gene</code> de locus (position) <code>i</code>.
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	public Gene getGene (int i)
		{
		if	(i >= this.nbGene || i < 0)
			throw new IndexOutOfBoundsException("ChromosomeVariable, getGene (int i) : indice 'i' hors des limites.");

		return this.tabGene[i];
		}


	/**
     * Supprime le <code>Gene</code> en position <code>i</code>.
	 *
	 * @return	le <code>Gene</code> supprimé
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	public Gene removeGene (int i)
		{
		if	(i >= this.nbGene || i < 0)
			throw new IndexOutOfBoundsException("ChromosomeVariable, removeGene (int i) : indice 'i' hors des limites.");

		Gene g = this.tabGene[i];
		this.nbGene--;

		for	(int j=i ; j<this.nbGene ; j++) this.tabGene[j] = this.tabGene[j+1];

		this.tabGene[this.nbGene] = null;

		return g;
		}


	/**
     * Mise à jour d'un <code>Gene</code> en position <code>i</code>.
	 *
	 * @param	ge	le nouveau <code>Gene</code>
	 *
	 * @return	le <code>Gene</code> remplacé
	 *
	 * @exception	IllegalArgumentException si le <code>Gene</code> remplaçant
	 *				est différent des <code>Gene</code>s déjà inclus dans le
	 *				<code>ChromosomeVariable</code>
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	public Gene setGene (int i,
						 Gene ge)
		{
		if	(i >= this.nbGene || i < 0)
			throw new IndexOutOfBoundsException("ChromosomeVariable, setGene (int i, Gene ge) : indice 'i' hors des limites.");

		if	(this.nbGene != 0 && !this.tabGene[0].getClass().isInstance(ge))
			throw new IllegalArgumentException("ChromosomeVariable, setGene (int i, Gene ge) : tous les genes doivent etre de meme type.");

		Gene g = this.tabGene[i];
		this.tabGene[i] = ge;
		return g;
		}


	/**
     * Retourne le nombre de <code>Gene</code>s composant le
	 * <code>ChromosomeVariable</code>.
	 */
	public int size () { return this.nbGene; }


	/*---------------------------------------*/
	/* Définition des méthodes de Chromosome */
	/*---------------------------------------*/

	/**
     * Croisement de deux <code>ChromosomeVariable</code>s.
	 *
	 * @param	parent	<code>ChromosomeVariable</code> qui va être croisé
	 *					avec this
	 *
	 * @return	un tableau contenant <b>deux</b> enfants
	 *
	 * @exception	IllegalArgumentException si le mode de croisement est
	 *				incorrect
	 */
	public Chromosome[] croisement (Chromosome	parent)
		{
		int i;
		int point1;
		int point2;
		ChromosomeVariable cv_parent = (ChromosomeVariable)parent;

		ChromosomeVariable cv0, cv1;

		try	{
			cv0 = (ChromosomeVariable)this.clone();
			cv1 = (ChromosomeVariable)this.clone();
			} catch (CloneNotSupportedException e) { return null; }

		/*
		 * Croisement, si les deux chromosomes ont une longueur égale à 1,
		 * les fils auront une taille de 1.
		 */
		switch(this.modeCroisement & this.type)
			{
			case ChromosomeVariable.GENE		:	//----- mode GENE
					{
					do	{
						point1 = (int)(PseudoRandomNumbers.random()*(this.nbGene+1));
						point2 = (int)(PseudoRandomNumbers.random()*(cv_parent.nbGene+1));
						}
					while ((point1 == 0 && point2 == cv_parent.nbGene) || (point1 == this.nbGene && point2 == 0));

					cv0.nbGene = 0;
					cv0.tabGene = new Gene[point1+cv_parent.nbGene-point2+ChromosomeVariable.PLUS];
					cv1.nbGene = 0;
					cv1.tabGene = new Gene[this.nbGene-point1+point2+ChromosomeVariable.PLUS];

					for (i=0 ; i<point1 ; i++)					cv0.tabGene[cv0.nbGene++] = this.tabGene[i].copier();
					for (i=point2 ; i<cv_parent.nbGene ; i++)	cv0.tabGene[cv0.nbGene++] = cv_parent.tabGene[i].copier();
					for (i=0 ; i<point2 ; i++)					cv1.tabGene[cv1.nbGene++] = cv_parent.tabGene[i].copier();
					for (i=point1 ; i<this.nbGene ; i++)		cv1.tabGene[cv1.nbGene++] = this.tabGene[i].copier();
					} break;

			case ChromosomeVariable.CARACTERE	:	//----- mode CARACTERE
					{

					point1 = (int)(PseudoRandomNumbers.random()*this.nbGene);
					point2 = (int)(PseudoRandomNumbers.random()*cv_parent.nbGene);

					cv0.nbGene = 0;
					cv0.tabGene = new Gene[point1+cv_parent.nbGene-point2+ChromosomeVariable.PLUS];
					cv1.nbGene = 0;
					cv1.tabGene = new Gene[this.nbGene-point1+point2+ChromosomeVariable.PLUS];

					for (i=0 ; i<point1 ; i++) cv0.tabGene[cv0.nbGene++] = this.tabGene[i].copier();
					for (i=0 ; i<point2 ; i++) cv1.tabGene[cv1.nbGene++] = cv_parent.tabGene[i].copier();

					GeneComplexe[] gc = ((GeneComplexe)this.tabGene[point1]).croisement((GeneComplexe)cv_parent.tabGene[point2]);

					cv0.tabGene[cv0.nbGene++] = gc[0];
					cv1.tabGene[cv1.nbGene++] = gc[1];

					for (i=point2+1 ; i<cv_parent.nbGene ; i++)	cv0.tabGene[cv0.nbGene++] = cv_parent.tabGene[i].copier();
					for (i=point1+1 ; i<this.nbGene ; i++)		cv1.tabGene[cv1.nbGene++] = this.tabGene[i].copier();
					} break;

			case ChromosomeVariable.MELANGE		:	//----- mode MELANGE
					{
					int lg_segment;

					if	(this.nbGene < cv_parent.nbGene)
						lg_segment = (int)(PseudoRandomNumbers.random() * (this.nbGene/2)) + 1;
					else
						lg_segment = (int)(PseudoRandomNumbers.random() * (cv_parent.nbGene/2)) + 1;

					point1 = (int)(PseudoRandomNumbers.random()*(this.nbGene-lg_segment+1));
					point2 = (int)(PseudoRandomNumbers.random()*(cv_parent.nbGene-lg_segment+1));

					int choix1 = (int)(PseudoRandomNumbers.random()*2);
					int choix2 = (int)(PseudoRandomNumbers.random()*2);

					if	(choix1 == 0)
						{
						cv0.nbGene = point1+ChromosomeVariable.PLUS;
						cv1.nbGene = point2+ChromosomeVariable.PLUS;
						}
					else
						{
						cv0.nbGene = point2+ChromosomeVariable.PLUS;
						cv1.nbGene = point1+ChromosomeVariable.PLUS;
						}

					if	(choix2 == 0)
						{
						cv0.nbGene += this.nbGene-point1;
						cv1.nbGene += cv_parent.nbGene-point2;
						}
					else
						{
						cv0.nbGene += cv_parent.nbGene-point2;
						cv1.nbGene += this.nbGene-point1;
						}

					cv0.tabGene = new Gene[cv0.nbGene]; cv0.nbGene = 0;
					cv1.tabGene = new Gene[cv1.nbGene]; cv1.nbGene = 0;

					if	(choix1 == 0)
						{
						for (i=0 ; i<point1 ; i++) cv0.tabGene[cv0.nbGene++] = this.tabGene[i].copier();
						for (i=0 ; i<point2 ; i++) cv1.tabGene[cv1.nbGene++] = cv_parent.tabGene[i].copier();
						}
					else
						{
						for (i=0 ; i<point1 ; i++) cv1.tabGene[cv1.nbGene++] = this.tabGene[i].copier();
						for (i=0 ; i<point2 ; i++) cv0.tabGene[cv0.nbGene++] = cv_parent.tabGene[i].copier();
						}

					for (i=0 ; i<lg_segment ; i++)
						{
						Gene g = this.tabGene[point1+i].melanger(cv_parent.tabGene[point2+i]);
						cv0.tabGene[cv0.nbGene++] = g;
						cv1.tabGene[cv1.nbGene++] = g.copier();
						}

					if	(choix2 == 0)
						{
						for (i=point1+lg_segment ; i<this.nbGene ; i++)			cv0.tabGene[cv0.nbGene++] = this.tabGene[i].copier();
						for (i=point2+lg_segment ; i<cv_parent.nbGene ; i++)	cv1.tabGene[cv1.nbGene++] = cv_parent.tabGene[i].copier();
						}
					else
						{
						for (i=point1+lg_segment ; i<this.nbGene ; i++)			cv1.tabGene[cv1.nbGene++] = this.tabGene[i].copier();
						for (i=point2+lg_segment ; i<cv_parent.nbGene ; i++)	cv0.tabGene[cv0.nbGene++] = cv_parent.tabGene[i].copier();
						}
					} break;

			default : throw new IllegalArgumentException("ChromosomeVariable, croisement (Chromosome parent, int mode) : mode de croisement incorrect.");
			}

		ChromosomeVariable[] cv = new ChromosomeVariable[2];
		cv[0] = cv0;
		cv[1] = cv1;

		return cv;
		}


	/**
     * Mutation du <code>ChromosomeVariable</code>.
	 * <p>
	 * Un <code>ChromosomeVariable</code> peut muter de quatre manières
	 * différentes :
	 * <ul>
	 *	<li>Mutation d'un <code>Gene</code> choisi au hasard.
	 *	<li>Ajout d'un <code>Gene</code>.
	 *	<li>Suppression d'un <code>Gene</code> à condition que la taille du
	 *		<code>ChromosomeVariable</code> soit > à 1.
	 *	<li>Changement de position de deux <code>Gene</code>s.
	 * </ul>
	 * Chaque mutation a la même probabilité de survenir.
	 *
	 * @param	Pm	Probabilité de mutation
	 */
	public boolean mutation (double Pm)
		{
		if (PseudoRandomNumbers.random() <= Pm)
			{
			int i = (int)(PseudoRandomNumbers.random()*this.nbGene);

			switch((int)(PseudoRandomNumbers.random()*4))
				{
				case 0	:	//----- Changement de valeur
							this.tabGene[i].mutation(); break;

				case 1	:	//----- Ajout d'un Gene en position i
							this.addGene(i,this.tabGene[0].creer()); break;

				case 2	:	//----- Suppresion d'un Gene
							if	(this.nbGene > 1) this.removeGene(i); break;

				default :	//----- Repositionnement de certains Genes
							if	(this.nbGene > 1)
								{
								int j;
								do	{ j = (int)(PseudoRandomNumbers.random()*this.nbGene); } while (i==j);

								Gene g = this.tabGene[i];
								this.tabGene[i] = this.tabGene[j];
								this.tabGene[j] = g;
								}
				}

			return true;
			}

		return false;
		}


	/**
     * Crée une copie du <code>ChromosomeVariable</code> ("deep copy").
	 * <p>
	 * Le nouveau <code>ChromosomeVariable</code> sera identique à son créateur.
	 */
	public Chromosome copier ()
		{
		try {
			ChromosomeVariable cv = (ChromosomeVariable)this.clone();
			cv.tabGene = new Gene[this.tabGene.length];

			for (int i=0 ; i<this.nbGene ; i++) cv.tabGene[i] = this.tabGene[i].copier();

			return cv;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
     * Crée un nouveau <code>ChromosomeVariable</code>.
	 * <p>
	 * Le nouveau <code>ChromosomeVariable</code> aura la même structure que
	 * son créateur mais toutes ses données seront initialisées aléatoirement.
	 */
	public Chromosome creer()
		{
		try	{
			ChromosomeVariable cv = (ChromosomeVariable)this.clone();

			int taille = (int)(PseudoRandomNumbers.random()*this.lg.getTaille()) + this.lg.getMin();
			cv.tabGene = new Gene[taille+ChromosomeVariable.PLUS];
			cv.nbGene = taille;

			Gene g = this.tabGene[0];
			for (int i=0 ; i<taille ; i++) cv.tabGene[i] = g.creer();

			return cv;
			} catch (CloneNotSupportedException x) { return null; }
		}


	/**
     * Compare la valeur de deux <code>ChromosomeVariable</code>s.
	 *
	 * @param	c	<code>ChromosomeVariable</code> comparé
	 *
	 * @return	<code>true</code> si les deux <code>ChromosomeVariable</code>s
	 *			sont égaux, <code>false</code> sinon
	 */
	public boolean estEgalA (Chromosome c)
		{
		ChromosomeVariable cv = (ChromosomeVariable)c;
		if (this.nbGene == cv.nbGene)
			{
			for	(int i=0 ; i<this.nbGene ; i++)
				if (!this.tabGene[i].estEgalA(cv.tabGene[i])) return false;

			return true;
			}

		return false;
		}


	/**
     * Retourne la représentation sous forme de chaîne de caractères d'un
	 *	<code>ChromosomeVariable</code>.
	 */
	public StringBuilder afficher ()
		{
		int taille = this.nbGene-1;
		StringBuilder sb = new StringBuilder("<debut<");

		for (int i=0 ; i<taille ; i++)
			{
			sb.append(this.tabGene[i].afficher());
			sb.append(" ");
			}
		sb.append(this.tabGene[taille].afficher());
		sb.append(">fin>");

		return sb;
		}


	/*-----------------------------------------*/
	/* Redéfinition des méthodes de Chromosome */
	/*-----------------------------------------*/

	/**
     * Compare si this est plus général que <code>c</code>.
	 *
	 * @param	c	<code>ChromosomeVariable</code> comparé
	 *
	 * @return	<code>true</code> si this est plus général ou
	 *			égal à <code>c</code>
	 */
	@Override
	public boolean estPlusGeneralQue (Chromosome c)
		{
		ChromosomeVariable cv = (ChromosomeVariable)c;
		if (this.nbGene == cv.nbGene)
			{
			for	(int i=0 ; i<this.nbGene ; i++)
				if (!this.tabGene[i].estPlusGeneralQue(cv.tabGene[i])) return false;

			return true;
			}

		return false;
		}

} /*----- Fin de la classe ChromosomeVariable -----*/
