package javalain.algorithmegenetique;

import javalain.math.PseudoRandomNumbers;

/*----------------*/
/* ChromosomeFixe */
/*----------------*/
/**
 * Un <code>ChromosomeFixe</code> a une taille invariable et
 * ses élèments peuvent être des <code>Gene</code>s de types différents.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	5 août 2010
 */
public class ChromosomeFixe extends Chromosome
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

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
	 * pour former les deux enfants.
	 * <p>
	 * Ce mode permet de mélanger les <code>Gene</code>s de façon à augmenter
	 * la diversité génétique.
	 */
	public final static int MELANGE = 0x00000010;


	/**
     * Mode de croisement par CARACTERE.
	 * <p>
	 * Ce mode est identique au mode GENE mais ne s'applique que sur des
	 * <code>GeneComplexe</code>s. Il permet de choisir un point de croisement
	 * interne à un <code>GeneComplexe</code>.
	 */
	public final static int CARACTERE = 0x00000101;


	/**
     * Type de <code>ChromosomeFixe</code> dont les <code>Gene</code>s sont
	 * simples, par défaut.
	 */
	private final static int SIMPLE = 0x00000011;


	/**
     * Type de <code>ChromosomeFixe</code> dont les <code>Gene</code>s sont du
	 * même type <code>GeneComplexe</code>.
	 */
	private final static int COMPLEXE = 0x00000111;


	/*---------*/
	/* Données */
	/*---------*/

	/**
     * Type du <code>ChromosomeFixe</code>.
	 */
	private int type;


	/**
     * Liste des <code>Gene</code>s composant le <code>ChromosomeFixe</code>.
	 */
	private Gene tabGene[];


	/**
     * Nombre de <code>Gene</code>s composant le <code>ChromosomeFixe</code>.
	 */
	private int nbGene;


	/**
	 * Mode de croisement du <code>ChromosomeFixe</code>.
	 */
	protected int modeCroisement;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
     * Construit un <code>ChromosomeFixe</code> vide.
	 *
	 * @param	taille		nombre de <code>Gene</code>s qui composeront le
	 *						<code>ChromosomeFixe</code>
	 * @param	mode_crois	mode de croisement du <code>ChromosomeFixe</code>
	 */
	public ChromosomeFixe (int taille,
						   int mode_crois)
		{
		this.nbGene = taille;
		this.tabGene = new Gene[taille];

		this.type = ChromosomeFixe.SIMPLE;

		this.modeCroisement = mode_crois;
		}


	/**
     * Construit et initialise un <code>ChromosomeFixe</code> avec le type de
	 * <code>Gene</code> passé en paramètre.
	 *
	 * @param	g			type du <code>Gene</code> composant le
	 *						<code>ChromosomeFixe</code>
	 * @param	taille		nombre de <code>Gene</code>s composant le
	 *						<code>ChromosomeFixe</code>
	 * @param	mode_crois	mode de croisement du <code>ChromosomeFixe</code>
	 */
	public ChromosomeFixe (Gene	g,
						   int	taille,
						   int	mode_crois)
		{
		this.nbGene = taille;
		this.tabGene = new Gene[taille];

		for (int i=0 ; i<taille ; i++) this.tabGene[i] = g.creer();

		if	(g instanceof GeneComplexe)
			type = ChromosomeFixe.COMPLEXE;
		else
			type = ChromosomeFixe.SIMPLE;

		this.modeCroisement = mode_crois;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
     * Ajoute un <code>Gene</code> au <code>ChromosomeFixe</code>.
	 *
	 * @exception	IndexOutOfBoundsException si le <code>ChromosomeFixe</code>
	 *				est déjà plein
	 */
	public void addGene (Gene ge)
		{
		int i = 0;
		while (i < this.nbGene && this.tabGene[i] != null) i++;

		if	(i == this.nbGene)
			throw new IndexOutOfBoundsException("ChromosomeFixe, addGene (Gene ge) : impossible d'ajouter un Gene, le ChromosomeFixe est plein.");
		else
			this.tabGene[i] = ge;
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
			throw new IndexOutOfBoundsException("ChromosomeFixe, getGene (int i) : indice 'i' hors des limites.");

		return this.tabGene[i];
		}


	/**
     * Mise à jour d'un <code>Gene</code> en position <code>i</code>.
	 *
	 * @param	ge	le nouveau <code>Gene</code>
	 *
	 * @return	le <code>Gene</code> remplacé
	 *
	 * @exception	IllegalArgumentException si le nouveau <code>Gene</code> est
	 *				différent du <code>Gene</code> remplacé dans le
	 *				<code>ChromosomeFixe/code>
	 *
	 * @exception	IndexOutOfBoundsException si <code>i</code> est hors des
	 *				limites de l'espace de stockage
	 */
	public Gene setGene (int i, Gene ge)
		{
		if	(i >= this.nbGene || i < 0)
			throw new IndexOutOfBoundsException("ChromosomeFixe, setGene (int i, Gene ge) : indice 'i' hors des limites.");

		if	(!this.tabGene[i].getClass().isInstance(ge))
			throw new IllegalArgumentException("ChromosomeFixe, setGene (int i, Gene ge) : le nouveau gene n'est pas du meme type que le gene remplace.");

		Gene g = this.tabGene[i];
		this.tabGene[i] = ge;
		return g;
		}


	/**
     * Retourne le nombre de <code>Gene</code>s composant le
	 * <code>ChromosomeFixe</code>.
	 */
	public int size () { return this.nbGene; }


	/*---------------------------------------*/
	/* Définition des méthodes de Chromosome */
	/*---------------------------------------*/

	/**
     * Croisement de deux <code>ChromosomeFixe</code>s.
	 *
	 * @param	parent	<code>ChromosomeFixe</code> qui va être croisé avec this
	 *
	 * @return	un tableau contenant deux enfants
	 *
	 * @exception	IllegalArgumentException si le mode de croisement est
	 *				incorrect
	 */
	public Chromosome[] croisement (Chromosome	parent)
		{
		ChromosomeFixe cf0, cf1;

		try	{
			cf0 = (ChromosomeFixe)this.clone();
			cf1 = (ChromosomeFixe)this.clone();
			} catch (CloneNotSupportedException e) { return null; }

		cf0.tabGene = new Gene[this.nbGene];
		cf1.tabGene = new Gene[this.nbGene];

		switch(this.modeCroisement & this.type)
			{
			case ChromosomeFixe.GENE		:	//----- mode GENE
					{
					ChromosomeFixe cf_parent = (ChromosomeFixe)parent;

					int i;
					/*----- Point de croisement -----*/
					int point = (int)(PseudoRandomNumbers.random()*(this.nbGene-1)) + 1;

					for (i=0 ; i<point ; i++)
						{
						cf0.tabGene[i] = this.tabGene[i].copier();
						cf1.tabGene[i] = cf_parent.tabGene[i].copier();
						}
					for (i=point ; i<this.nbGene ; i++)
						{
						cf0.tabGene[i] = cf_parent.tabGene[i].copier();
						cf1.tabGene[i] = this.tabGene[i].copier();
						}
					} break;

			case ChromosomeFixe.CARACTERE	:	//----- mode CARACTERE
					{
					ChromosomeFixe cf_parent = (ChromosomeFixe)parent;

					int i;
					int point = (int)(PseudoRandomNumbers.random()*this.nbGene);

					for (i=0 ; i<point ; i++)
						{
						cf0.tabGene[i] = this.tabGene[i].copier();
						cf1.tabGene[i] = cf_parent.tabGene[i].copier();
						}

					GeneComplexe[] gc = ((GeneComplexe)this.tabGene[point]).croisement((GeneComplexe)cf_parent.tabGene[point]);

					cf0.tabGene[i] = gc[0];
					cf1.tabGene[i] = gc[1];

					for (i=point+1 ; i<this.nbGene ; i++)
						{
						cf0.tabGene[i] = cf_parent.tabGene[i].copier();
						cf1.tabGene[i] = this.tabGene[i].copier();
						}
					} break;

			case ChromosomeFixe.MELANGE		:	//----- mode MELANGE
					{
					ChromosomeFixe cf_parent = (ChromosomeFixe)parent;

					int i;
					/*----- Points de croisement -----*/
					int point1 = (int)(PseudoRandomNumbers.random()*(this.nbGene+1));
					int point2 = (int)(PseudoRandomNumbers.random()*(this.nbGene+1));

					if	(point1 < point2)
						{
						for (i=0 ; i<point1 ; i++)
							{
							cf0.tabGene[i] = this.tabGene[i].copier();
							cf1.tabGene[i] = cf_parent.tabGene[i].copier();
							}
						for (i=point1 ; i<point2 ; i++)
							{
							Gene g = this.tabGene[i].melanger(cf_parent.tabGene[i]);
							cf0.tabGene[i] = g;
							cf1.tabGene[i] = g.copier();
							}
						for (i=point2 ; i<this.nbGene ; i++)
							{
							cf0.tabGene[i] = this.tabGene[i].copier();
							cf1.tabGene[i] = cf_parent.tabGene[i].copier();
							}
						}
					else
						{
						for (i=0 ; i<point2 ; i++)
							{
							Gene g = this.tabGene[i].melanger(cf_parent.tabGene[i]);
							cf0.tabGene[i] = g;
							cf1.tabGene[i] = g.copier();
							}
						for (i=point2 ; i<point1 ; i++)
							{
							cf0.tabGene[i] = this.tabGene[i].copier();
							cf1.tabGene[i] = cf_parent.tabGene[i].copier();
							}
						for (i=point1 ; i<this.nbGene ; i++)
							{
							Gene g = this.tabGene[i].melanger(cf_parent.tabGene[i]);
							cf0.tabGene[i] = g;
							cf1.tabGene[i] = g.copier();
							}
						}
					} break;

			default : throw new IllegalArgumentException("ChromosomeFixe, croisement (Chromosome parent, int mode) : mode de croisement incorrect.");
			}

		ChromosomeFixe[] cf = new ChromosomeFixe[2];
		cf[0] = cf0;
		cf[1] = cf1;

		return cf;
		}


	/**
     * Mutation du <code>ChromosomeFixe</code>.
	 * <p>
	 * Un <code>Gene</code> choisi au hasard subit la mutation.
	 *
	 * @param	Pm	Probabilité de mutation
	 */
	public boolean mutation (double Pm)
		{
		if (PseudoRandomNumbers.random() <= Pm)
			{
			this.tabGene[(int)(PseudoRandomNumbers.random()*this.nbGene)].mutation();
			return true;
			}

		return false;
		}


	/**
     * Crée une copie du <code>ChromosomeFixe</code> ("deep copy").
	 * <p>
	 * Le nouveau <code>ChromosomeFixe</code> sera identique à son créateur.
	 */
	public Chromosome copier ()
		{
		try {
			ChromosomeFixe cf = (ChromosomeFixe)this.clone();
			cf.tabGene = new Gene[this.nbGene];

			for (int i=0 ; i<this.nbGene ; i++) cf.tabGene[i] = this.tabGene[i].copier();

			return cf;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
     * Crée un nouveau <code>ChromosomeFixe</code>.
	 * <p>
	 * Le nouveau <code>ChromosomeFixe</code> aura la même structure que
	 * son créateur mais toutes ses données seront initialisées aléatoirement.
	 */
	public Chromosome creer ()
		{
		try {
			ChromosomeFixe cf = (ChromosomeFixe)this.clone();
			cf.tabGene = new Gene[this.nbGene];

			for (int i=0 ; i<this.nbGene ; i++) cf.tabGene[i] = this.tabGene[i].creer();

			return cf;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
     * Compare la valeur de deux <code>ChromosomeFixe</code>s.
	 *
	 * @param	c	<code>ChromosomeFixe</code> comparé
	 *
	 * @return	<code>true</code> si les deux <code>ChromosomeFixe</code>s
	 *			sont égaux, <code>false</code> sinon
	 */
	public boolean estEgalA (Chromosome c)
		{
		ChromosomeFixe cf = (ChromosomeFixe)c;
		if (this.nbGene == cf.nbGene)
			{
			for	(int i=0 ; i<this.nbGene ; i++)
				if (!this.tabGene[i].estEgalA(cf.tabGene[i])) return false;

			return true;
			}

		return false;
		}


	/**
     * Retourne la représentation sous forme de chaîne de caractères d'un
	 * <code>ChromosomeFixe</code>.
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
	 * @param	c	<code>ChromosomeFixe</code> comparé
	 *
	 * @return	<code>true</code> si this est plus général ou
	 *			égal à <code>c</code>
	 */
	@Override
	public boolean estPlusGeneralQue (Chromosome c)
		{
		ChromosomeFixe cf = (ChromosomeFixe)c;
		if (this.nbGene == cf.nbGene)
			{
			for	(int i=0 ; i<this.nbGene ; i++)
				if (!this.tabGene[i].estPlusGeneralQue(cf.tabGene[i])) return false;

			return true;
			}

		return false;
		}

} /*----- Fin de la classe ChromosomeFixe -----*/
