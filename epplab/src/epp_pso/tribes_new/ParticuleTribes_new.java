package epp_pso.tribes_new;

import indice.Indice;
import javalain.math.Calcul;
import javalain.math.PseudoRandomNumbers;
import util.GuiUtils;


/*------------------------*/
/* Classe ParticuleTribes */
/*------------------------*/
/**
 * La classe <code>ParticuleTribes<code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	5 janvier 2011
 */
public class ParticuleTribes_new
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Précision.
	 */
	private static double EPS = 1e-14;


	/**
	 * Désigne si la performance de la particule a augmenté (AMELIORATION),
	 * n'a pas changé (STATU_QUO) ou a diminué (DETERIORATION)
	 * lors du dernier déplacement
	 */
	private static final int AMELIORATION	= 0;
	private static final int STATU_QUO		= 1;
	private static final int DETERIORATION	= 2;


	/**
	 * La particule est dite BONNE si elle vient d'améliorer sa meilleure
	 * performance lors de son dernier déplacement.
	 */
	private static final int BONNE		= 5;
	private static final int NEUTRE		= 6;


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Indice.
	 */
	private final Indice indice;


	/**
	 * Taille de la particule.
	 */
	private final int P;


	/**
	 * Performance de la particule.
	 */
	private double I;


	/**
	 * Meilleure performance de la particule.
	 */
	public double meilleurI;


	/**
	 * Position courante de la particule.
	 */
	private double[] projection;


	/**
	 * Position de la meilleure performance de la particule.
	 */
	public double[] meilleureProjection;


	/**
	 * Evolution de la performance de la particule à t et t-1.
	 */
	private int evolPerfPrec; // Recherche... - Remplacer par un tableau ? Que pourrait apporter une réflexion sur plus de temps ?
    private int evolPerf;


	/**
	 * Qualité de la particule.
	 */
	private int qualite;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée et initialise la particule (Particule libre).
	 *	. I
	 *	. Meilleur I
	 *	. Projection
	 *	. Meilleure projection
	 *	. Performance
	 *	. Qualité
	 */
	public ParticuleTribes_new (Indice ind)
		{
		/*----- Indice -----*/
		this.indice = ind;
		this.P = ind.getNombreParametres();

		/*----- Paramètres de la particule -----*/
		this.projection = new double[this.P];
		this.meilleureProjection = new double[this.P];

		/*----- Initialise aléatoirement la position de la particule -----*/
		for (int i=0; i<this.P; i++)
			this.projection[i] = PseudoRandomNumbers.random()*2 - 1;

		/*----- Normalise la position de la particule -----*/
		this.projection = Calcul.normaliseVecteur(this.projection);

		/*----- Performance de la particule (valeur de l'indice) -----*/
		this.I = this.indice.calcul(this.projection);

		/*----- Meilleure performance de la particule -----*/
		this.meilleurI = this.I;
		System.arraycopy(this.projection,0,this.meilleureProjection,0,this.P);

		/*----- Evolution de la performance et qualité de la particule -----*/
		this.evolPerfPrec	= ParticuleTribes_new.STATU_QUO;
		this.evolPerf		= ParticuleTribes_new.STATU_QUO;

		this.qualite		= ParticuleTribes_new.NEUTRE;
		}


	private ParticuleTribes_new (ParticuleTribes_new p)
		{
		/*----- Indice -----*/
		this.indice = p.indice;
		this.P = p.P;

		/*----- Paramètres de la particule -----*/
		this.projection = new double[this.P];
		this.meilleureProjection = new double[this.P];

		System.arraycopy(p.projection, 0, this.projection, 0, this.P);
		System.arraycopy(p.meilleureProjection, 0, this.meilleureProjection, 0, this.P);

		/*----- Performance de la particule (valeur de l'indice) -----*/
		this.I = p.I;

		/*----- Meilleure performance de la particule -----*/
		this.meilleurI = p.meilleurI;

		/*----- Evolution de la performance et qualité de la particule -----*/
		this.evolPerfPrec	= p.evolPerfPrec;
		this.evolPerf		= p.evolPerf;

		this.qualite		= p.qualite;
		}


	/**
	 * Crée et initialise la particule (Particule confinée).
	 *	. I
	 *	. Meilleur I
	 *	. Projection
	 *	. Meilleure projection
	 *	. Performance
	 *	. Qualité
	 */
	public ParticuleTribes_new (Indice ind, ParticuleTribes_new bestParticuleOfSwarm, ParticuleTribes_new bestParticuleOfTribe)
		{
		/*----- Indice -----*/
		this.indice = ind;
		this.P = ind.getNombreParametres();

		/*----- Paramètres de la particule -----*/
		this.projection = Calcul.loiHyperspherique(bestParticuleOfSwarm.meilleureProjection, bestParticuleOfTribe.meilleureProjection);
		this.meilleureProjection = new double[this.P];

		/*----- Normalise la position courante -----*/
		this.projection = Calcul.normaliseVecteur(this.projection);

		/*----- Performance de la particule (valeur de l'indice) -----*/
		this.I = this.indice.calcul(this.projection);

		/*----- Meilleure performance de la particule -----*/
		this.meilleurI = this.I;
		System.arraycopy(this.projection,0,this.meilleureProjection,0,this.P);

		/*----- Evolution de la performance et qualité de la particule -----*/
		this.evolPerfPrec	= ParticuleTribes_new.STATU_QUO;
		this.evolPerf		= ParticuleTribes_new.STATU_QUO;

		this.qualite		= ParticuleTribes_new.NEUTRE;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne 'true' si la particule est BONNE.
	 */
	// Recherche... - Changer la définition : bonne si évolue entre 2 adaptations ?
	public boolean isGood () { return this.qualite == ParticuleTribes_new.BONNE; }
//	public boolean isGood () { return this.evolPerfPrec == ParticuleTribes.AMELIORATION; }

	/**
	 * Retourne une copie de la particule.
	 */
	public ParticuleTribes_new createCopie () { return new ParticuleTribes_new(this); }


	/**
	 * Calcule la nouvelle position de la particule
	 * en fonction de la position de son informatrice et
	 * de l'évolution de se performance.
	 *
	 * Si son informatrice est trop proche de la particule, elle ne bouge pas.
	 */
	public void updatePosition (ParticuleTribes_new informatrice)
		{
		if ((this.evolPerfPrec == ParticuleTribes_new.DETERIORATION	&& this.evolPerf == ParticuleTribes_new.DETERIORATION)	||
			(this.evolPerfPrec == ParticuleTribes_new.STATU_QUO		&& this.evolPerf == ParticuleTribes_new.DETERIORATION)	||
			(this.evolPerfPrec == ParticuleTribes_new.DETERIORATION	&& this.evolPerf == ParticuleTribes_new.STATU_QUO)		||
			(this.evolPerfPrec == ParticuleTribes_new.STATU_QUO		&& this.evolPerf == ParticuleTribes_new.STATU_QUO)		||
            (this.evolPerfPrec == ParticuleTribes_new.AMELIORATION	&& this.evolPerf == ParticuleTribes_new.DETERIORATION))
			this.pivot(informatrice);

		if ((this.evolPerfPrec == ParticuleTribes_new.AMELIORATION	&& this.evolPerf == ParticuleTribes_new.STATU_QUO)		||
			(this.evolPerfPrec == ParticuleTribes_new.DETERIORATION	&& this.evolPerf == ParticuleTribes_new.AMELIORATION))
			this.pivotBruite(informatrice);

		if ((this.evolPerfPrec == ParticuleTribes_new.STATU_QUO		&& this.evolPerf == ParticuleTribes_new.AMELIORATION)	||
			(this.evolPerfPrec == ParticuleTribes_new.AMELIORATION	&& this.evolPerf == ParticuleTribes_new.AMELIORATION))
			this.gaussienneIndependante(informatrice);

		/*----- Normalise la position courante -----*/
		this.projection = Calcul.normaliseVecteur(this.projection);
		}


	/**
	 * Calcule la performance de la particule.
	 */
	public void updateBest ()
		{
		/*----- Performance de la particule (valeur de l'indice) -----*/
		double d = this.I;
		this.I = this.indice.calcul(this.projection);

		/*----- Evolution de la performance de la particule -----*/
		this.evolPerfPrec = this.evolPerf;							// A voir - N'est pas mis à jour dans le "else" ou c'est alors ce qu'il y a ci dessous

//		if (Math.abs(d-this.I) < EPS)
//			this.evolPerf = ParticuleTribes.STATU_QUO;
//		else
//			if (this.I > d)
//				this.evolPerf = ParticuleTribes.AMELIORATION;
//			else
//				this.evolPerf = ParticuleTribes.DETERIORATION;

		/*----- Evolution de la meilleure performance de la particule -----*/
		if (this.I > this.meilleurI)
			{
			this.meilleurI = this.I;
			System.arraycopy(this.projection,0,this.meilleureProjection,0,this.P);

			this.qualite = ParticuleTribes_new.BONNE;

			this.evolPerf = ParticuleTribes_new.AMELIORATION;
			}
		else
			this.qualite = ParticuleTribes_new.NEUTRE;


		}


	/*---------------------------------------------------*
	 * Stratégies de déplacement :
	 * - Pivot.
	 * - Pivot bruité.
	 * - Recherche locale par gaussienne independante.
	 * - Estimation de distribution (EDA).
	 *---------------------------------------------------*/

	/**
	 * Calcule la nouvelle position en appliquant un déplacement par pivot.
	 */
	private void pivot (ParticuleTribes_new informatrice)
		{
		double c2;
		double c3;
		double somme = this.meilleurI + informatrice.meilleurI;

		if (somme < EPS) // Evite la division par 0.
			c2 = PseudoRandomNumbers.random();
		else
			c2 = this.meilleurI / somme;

		c3 = 1.0-c2;

		double[] alea_Hp = Calcul.loiHyperspherique(this.meilleureProjection, informatrice.meilleureProjection);
		double[] alea_Hg = Calcul.loiHyperspherique(informatrice.meilleureProjection, this.meilleureProjection);

		for (int i=0; i<this.P; i++)
			this.projection[i] = c2 * alea_Hp[i] + c3 * alea_Hg[i];
		}



	/**
	 * Calcule la nouvelle position en appliquant un déplacement
	 * par pivot bruité.
	 */
	private void pivotBruite (ParticuleTribes_new informatrice)
		{
		/*----- Pivot -----*/
		double c2;
		double c3;
		double somme = this.meilleurI + informatrice.meilleurI;

		if (somme < EPS) // Evite la division par 0.
			c2 = PseudoRandomNumbers.random();
		else
			c2 = this.meilleurI / somme;

		c3 = 1.0-c2;

		double[] alea_Hp = Calcul.loiHyperspherique(this.meilleureProjection, informatrice.meilleureProjection);
		double[] alea_Hg = Calcul.loiHyperspherique(informatrice.meilleureProjection, this.meilleureProjection);

		for (int i=0; i<this.P; i++)
			this.projection[i] = c2 * alea_Hp[i] + c3 * alea_Hg[i];

		/*----- Ajout du bruit -----*/
		double sigma = (somme < EPS ? 1.0 : (this.meilleurI - informatrice.meilleurI) / somme);
		double c = Calcul.loiNormale(0, sigma);

		for (int i=0; i<this.P; i++ )
			this.projection[i] = (1+c)*this.projection[i];
		}


	/**
	 * Calcule le nouvelle position en appliquant un déplacement
	 * par gaussienne indépendante.
	 */
	private void gaussienneIndependante (ParticuleTribes_new informatrice)
		{
		double d, sigma;
		for (int i=0; i<this.P; i++)
			{
			d = informatrice.meilleureProjection[i] - this.projection[i]; // On peut prendre aussi d = 0;
			sigma = Math.abs(informatrice.meilleureProjection[i] - this.projection[i]);

			this.projection[i] = this.meilleureProjection[i] + Calcul.loiNormale(d, sigma);
			}
		}


	/**
	 * Retourne la représentation de la particule en caractères.
	 */
	@Override
	public String toString ()
		{
		StringBuilder s = new StringBuilder();
		s.append("----- Particule (").append(this.P).append(")\n");
		s.append("Performance          : ").append(GuiUtils.DECIMAL_12.format(this.I)).append("\n");
		s.append("Meilleur performance : ").append(GuiUtils.DECIMAL_12.format(this.meilleurI)).append("\n");

		s.append("Position             : ");
		for (int i=0; i<this.P-1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.projection[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.projection[this.P-1])).append("\n");

		s.append("Meilleure position   : ");
		for (int i=0; i<this.P-1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[this.P-1])).append("\n");
		s.append("-----");

		return s.toString();
		}

} /*----- Fin de la classe ParticuleTribes -----*/