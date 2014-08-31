package javalain.algorithmegenetique;

import javalain.math.PseudoRandomNumbers;

/*--------------*/
/* GeneComplexe */
/*--------------*/
/**
 * La classe <code>GeneComplexe</code> permet l'utilisation de plusieurs
 * <code>Gene</code>s différents pour représenter une caractéristique
 * de l'individu.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	1 septembre 2002
 */
public class GeneComplexe extends Gene implements Cloneable
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Liste des <code>Gene</code>s.
	 */
	private Gene tabGene[];


	/**
	 * Nombre de <code>Gene</code>s composant le <code>GeneComplexe</code>.
	 */
	private int	nbGene;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit un <code>GeneComplexe</code> vide.
	 *
	 * @param	taille	taille du <code>GeneComplexe</code>
	 */
	public GeneComplexe (int taille)
		{
		this.nbGene = taille;
		this.tabGene = new Gene[taille];
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Ajoute un <code>Gene</code> au <code>GeneComplexe</code>.
	 *
	 * @exception	IndexOutOfBoundsException si le <code>GeneComplexe</code>
	 *				est déjà plein
	 */
	public void addGene (Gene ge)
		{
		int i = 0;
		while (i < this.nbGene && this.tabGene[i] != null) i++;

		if	(i == this.nbGene)
			throw new IndexOutOfBoundsException("GeneComplexe, addGene (Gene ge) : impossible d'ajouter un Gene, le GeneComplexe est plein.");
		else
			this.tabGene[i] = ge;
		}


	/**
	 * Retourne le <code>Gene</code> en position <code>i</code> dans
	 * le <code>GeneComplexe</code>.
	 */
	public Gene getGene (int i)
		{
		try	{
			return this.tabGene[i];
			} catch (ArrayIndexOutOfBoundsException e)
				{ throw new IndexOutOfBoundsException("GeneComplexe, getGene (int i) : indice 'i' hors des limites."); }
		}


	/**
	 * Retourne la taille du <code>GeneComplexe</code>.
	 * <p>
	 * La taille représente le nombre de <code>Gene</code>s composant
	 * le <code>GeneComplexe</code>.
	 */
	public int size () { return this.nbGene; }


	/**
	 * Croisement de deux <code>GeneComplexe</code>s.
	 *
	 * @param	g	<code>Gene</code> qui va être croisé avec this
	 *
	 * @return	un tableau de deux <code>GeneComplexe</code>s
	 */
	public GeneComplexe[] croisement (GeneComplexe g)
		{
		GeneComplexe g0, g1;

		try	{
			g0 = (GeneComplexe)this.clone();
			g1 = (GeneComplexe)this.clone();
			} catch (CloneNotSupportedException e) { return null; }

		g0.tabGene = new Gene[this.nbGene];
		g1.tabGene = new Gene[this.nbGene];

		int i;
		int point = (int)(PseudoRandomNumbers.random()*(this.nbGene-1)) + 1;

		for (i=0 ; i<point ; i++)
			{
			g0.tabGene[i] = this.tabGene[i].copier();
			g1.tabGene[i] = g.tabGene[i].copier();
			}
		for (i=point ; i<this.nbGene ; i++)
			{
			g0.tabGene[i] = g.tabGene[i].copier();
			g1.tabGene[i] = this.tabGene[i].copier();
			}

		GeneComplexe[] gc = new GeneComplexe[2];
		gc[0] = g0;
		gc[1] = g1;

		return gc;
		}


	/*-------------------------------------------*/
	/* Définition de méthodes abstraites de Gene */
	/*-------------------------------------------*/

	/**
	 * Mélange de deux <code>GeneComplexe</code>s.
	 */
	public Gene melanger (Gene g)
		{
		try	{
			GeneComplexe gc = (GeneComplexe)this.clone();
			gc.tabGene = new Gene[this.nbGene];

			GeneComplexe ge = (GeneComplexe)g;
			for (int i=0 ; i<this.nbGene ; i++) gc.tabGene[i] = this.tabGene[i].melanger(ge.tabGene[i]);

			return gc;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
	 * Mutation d'un <code>GeneComplexe</code>.
	 */
	public void mutation () { this.tabGene[(int)(PseudoRandomNumbers.random()*this.nbGene)].mutation(); }


	/**
	 * Crée une copie d'un <code>GeneComplexe</code> ("deep copy").
	 */
	public Gene copier ()
		{
		try	{
			GeneComplexe gc = (GeneComplexe)this.clone();
			gc.tabGene = new Gene[this.nbGene];

			for (int i=0 ; i<this.nbGene ; i++) gc.tabGene[i] = this.tabGene[i].copier();

			return gc;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
	 * Crée un nouveau <code>GeneComplexe</code>.
	 */
	public Gene creer ()
		{
		try	{
			GeneComplexe gc = (GeneComplexe)this.clone();
			gc.tabGene = new Gene[this.nbGene];

			for (int i=0 ; i<this.nbGene ; i++) gc.tabGene[i] = this.tabGene[i].creer();

			return gc;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
	 * Crée un nouveau <code>GeneComplexe</code> en fonction d'un autre
	 * <code>GeneComplexe</code>.
	 * <p>
	 * Le nouveau <code>GeneComplexe</code> est créé par application de
	 * la méthode <code>creer (Gene g)</code> sur chaque <code>Gene</code>
	 * qui compose <code>this</code>.
	 *
	 * @param	g	<code>GeneComplexe</code> utilisé pour initialiser
	 *				la valeur du nouveau <code>GeneComplexe</code>
	 */
	@Override
	public Gene creer (Gene g)
		{
		try	{
			GeneComplexe gc = (GeneComplexe)this.clone();
			gc.tabGene = new Gene[this.nbGene];

			GeneComplexe ge = (GeneComplexe)g;
			for (int i=0 ; i<this.nbGene ; i++) gc.tabGene[i] = this.tabGene[i].creer(ge.tabGene[i]);

			return gc;
			} catch (CloneNotSupportedException e) { return null; }
		}


	/**
	 * Compare la valeur de deux <code>GeneComplexe</code>s.
	 *
	 * @param	g	<code>GeneComplexe</code> comparé
	 *
	 * @return	<code>true</code> si les deux valeurs sont égales,
	 *			<code>false</code> sinon
	 */
	public boolean estEgalA (Gene g)
		{
		if	(g instanceof GeneComplexe)
			{
			GeneComplexe gc = (GeneComplexe)g;
			if	(this.nbGene == gc.nbGene)
				{
				for (int i=0 ; i<this.nbGene ; i++)
					if (!this.tabGene[i].estEgalA(gc.tabGene[i])) return false;

				return true;
				}
			}

		return false;
		}


	/**
	 * Compare si <code>this</code> est plus général que <code>g</code>.
	 * <p>
	 * Cette méthode teste si chaque <code>Gene</code> qui compose this est plus
	 * général que le <code>Gene</code> de g situé à la même position.
	 * <p>
	 * @param	g	<code>GeneComplexe</code> comparé
	 *
	 * @return	<code>true</code> si this
	 */
	@Override
	public boolean estPlusGeneralQue (Gene g)
		{
		if	(g instanceof GeneComplexe)
			{
			GeneComplexe gc = (GeneComplexe)g;
			if	(this.nbGene == gc.nbGene)
				{
				for (int i=0 ; i<this.nbGene ; i++)
					if (!this.tabGene[i].estPlusGeneralQue(gc.tabGene[i])) return false;

				return true;
				}
			}

		return false;
		}


	/**
	 * Retourne la représentation sous forme de chaîne de caractères
	 * d'un <code>GeneComplexe</code>.
	 */
	public StringBuilder afficher ()
		{
		int taille = this.nbGene-1;
		StringBuilder sb = new StringBuilder("<");

		for (int i=0 ; i<taille ; i++)
			{
			sb.append(this.tabGene[i].afficher());
			sb.append(" ");
			}
		sb.append(this.tabGene[taille].afficher());
		sb.append(">");

		return sb;
		}

} /*----- Fin de la classe GeneComplexe -----*/
