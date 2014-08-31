package epp_pso.tribe;

import indice.Indice;
import java.util.Arrays;
import javalain.math.Calcul;
import javalain.math.PseudoRandomNumbers;
import util.GuiUtils;


/*-----------------------*/
/* Classe ParticuleTribe */
/*-----------------------*/
/**
 * La classe <code>ParticuleTribe<code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	19 mars 2010
 */
public class ParticuleTribe
{
	/*------------*/
	/* Constantes */
	/*------------*/

	/**
	 * Performance qualitative de la particule.
	 */
	public static final int MEILLEURE	= 0;
	public static final int STATU_QUO	= 1;
	public static final int MAUVAISE	= 2;


	/**
	 * Qualité de la particule.
	 */
	public static final int BONNE	=  5;
	public static final int NEUTRE	=  6;


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
	public double I;


	/**
	 * Meilleure performance de la particule.
	 */
	public double meilleurI;


	/**
	 * Position courante de la particule.
	 */
	public double[] projectionCourante;


	/**
	 * Position de la meilleure performance de la particule.
	 */
	public double[] meilleureProjection;


	/**
	 * Evolution de la performance de la particule.
	 */
	public int performance_prec;

    public int performance_crt;


	/**
	 * Qualité de la particule.
	 */
	public int qualite;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit la particule et initialise ses caractéristiques.
	 *	. I
	 *	. Meilleur I
	 *	. Projection courante
	 *	. Meilleure projection
	 *	. Performance
	 *	. Qualité
	 */
	public ParticuleTribe (Indice ind)
		{
		/*----- Indice -----*/
		this.indice = ind;
		this.P = ind.getNombreParametres();

		/*----- Paramètres de la particule -----*/
		this.projectionCourante = new double[this.P];
		this.meilleureProjection = new double[this.P];

		/*----- Initialisation de la position (projection) de la particule -----*/
		for (int i=0; i<this.P; i++)
			{
			this.projectionCourante[i] = PseudoRandomNumbers.random()*2 - 1;
			this.meilleureProjection[i] = 0.0;
			}

		/*----- Normalisation la position (projection) courante -----*/
		this.projectionCourante = Calcul.normaliseVecteur(this.projectionCourante);

		this.meilleurI = Double.NEGATIVE_INFINITY;

		this.performance_prec = ParticuleTribe.STATU_QUO;
		this.performance_crt = ParticuleTribe.STATU_QUO;

		this.qualite = ParticuleTribe.NEUTRE;
		}


	/**
	 * Construit la particule confinée et initialise ses caractéristiques.
	 *	. I
	 *	. Meilleur I
	 *	. Projection courante
	 *	. Meilleure projection
	 *	. Performance
	 *	. Qualité
	 */
	public ParticuleTribe (Indice ind, ParticuleTribe globalBest, ParticuleTribe bestParticule)
		{
		/*----- Indice -----*/
		this.indice = ind;
		this.P = ind.getNombreParametres();

		/*----- Paramètres de la particule -----*/
		this.projectionCourante = Calcul.loiHyperspherique(globalBest.meilleureProjection, bestParticule.meilleureProjection);
		this.meilleureProjection = new double[this.P];

		/*----- Initialisation de la position (projection) de la particule -----*/
		for (int i=0; i<this.P; i++)
			this.meilleureProjection[i]	= 0.0;

		/*----- Normalisation la position (projection) courante -----*/
		this.projectionCourante = Calcul.normaliseVecteur(this.projectionCourante);

		this.meilleurI = Double.NEGATIVE_INFINITY;

		this.performance_prec = ParticuleTribe.STATU_QUO;
		this.performance_crt = ParticuleTribe.STATU_QUO;

		this.qualite = ParticuleTribe.NEUTRE;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Calcule la performance de la particule.
	 */
	public void updatePbest ()
		{
		double d = this.I;

		/*----- Calcul de la performance (valeur de l'indice) -----*/
		this.I = this.indice.calcul(this.projectionCourante);

		/*----- Qualité de la particule -----*/
		this.performance_crt = ParticuleTribe.STATU_QUO;

		/*----- Meilleure position ? -----*/
		if (this.I > this.meilleurI)
			{
			this.meilleurI = this.I;
			System.arraycopy(this.projectionCourante, 0, this.meilleureProjection, 0, this.P);

			this.performance_crt = ParticuleTribe.MEILLEURE;
			}
		}


	/**
	 * Calcul de la nouvelle position de la particule en fonction
	 * de sa performance.
	 *
	 * . globalBest : la meilleure particule globale
	 * . bool : autorise l'application de EDA() après un nombre fixé d'itérations
	 * . N : nombre de particules à choisir pour appliquer EDA()
	 */
	public void calculNouvellePosition (ParticuleTribe globalBest, boolean bool, int N)
		{
		boolean verif = false;

		if ((this.performance_prec == ParticuleTribe.MAUVAISE	&& performance_crt == ParticuleTribe.MAUVAISE)		||
			(this.performance_prec == ParticuleTribe.STATU_QUO	&& performance_crt == ParticuleTribe.MAUVAISE)		||
			(this.performance_prec == ParticuleTribe.MAUVAISE	&& performance_crt == ParticuleTribe.STATU_QUO)	||
			(this.performance_prec == ParticuleTribe.STATU_QUO	&& performance_crt == ParticuleTribe.STATU_QUO)    ||
            (this.performance_prec == ParticuleTribe.MEILLEURE   && performance_crt == ParticuleTribe.MAUVAISE))
			verif = true;

		if (verif && !bool)
			this.pivot(globalBest);

		if ((this.performance_prec == ParticuleTribe.MEILLEURE	&& performance_crt == ParticuleTribe.STATU_QUO)	||
			(this.performance_prec == ParticuleTribe.MAUVAISE	&& performance_crt == ParticuleTribe.MEILLEURE))
			this.pivotBruite(globalBest);

		if ((this.performance_prec == ParticuleTribe.STATU_QUO	&& performance_crt == ParticuleTribe.MEILLEURE)	||
			(this.performance_prec == ParticuleTribe.MEILLEURE	&& performance_crt == ParticuleTribe.MEILLEURE))
			this.gaussienneIndependante(globalBest);

		/* // Rem - Utilité EDA.
		 * Dans notre cas, EDA n'est jamais utilisé car bool = false. En effet,
		 * EDA est fait (dixit Clerc) pour éviter la convergence prématurée
		 * de TRIBES. Or pour nous, nous souhaitons que l'algorithme s'enferme
		 * dans des optima locaux donc EDA ne nous ai pas utile.
		 */
		if (verif && bool)
			this.EDA(N);

		/*----- Actualise l'état (la performance) de la particule -----*/
		this.performance_prec = performance_crt;

		/**
		 * Normalise la projection courante.
		 */
		this.projectionCourante = Calcul.normaliseVecteur(this.projectionCourante);
		}


	/*---------------------------------------------------*
	 * Stratégies de deplacement :
	 * - Pivot.
	 * - Pivot bruité.
	 * - Recherche locale par gaussienne independante.
	 * - Estimation de distribution (EDA).
	 *---------------------------------------------------*/

	/**
	 * Calcule la nouvelle position en appliquant un déplacement par pivot.
	 */
	public void pivot (ParticuleTribe globalBest)
		{
		double c2 = this.meilleurI / (this.meilleurI + globalBest.meilleurI);
		double c3 = globalBest.meilleurI / (this.meilleurI + globalBest.meilleurI);

		double[] alea_Hp = Calcul.loiHyperspherique(this.meilleureProjection, globalBest.meilleureProjection);
		double[] alea_Hg = Calcul.loiHyperspherique(globalBest.meilleureProjection, this.meilleureProjection);

		for (int i=0; i<this.P; i++)
			this.projectionCourante[i] = c2 * alea_Hp[i] + c3 * alea_Hg[i];
		}


	/**
	 * Calcule la nouvelle position en appliquant un déplacement
	 * par pivot bruité.
	 */
	public void pivotBruite (ParticuleTribe globalBest)
		{
		/*----- Pivot -----*/
		this.pivot(globalBest);

		double sigma = (this.meilleurI - globalBest.meilleurI) / (this.meilleurI + globalBest.meilleurI);
		double c = Calcul.loiNormale(0, sigma);

		for (int i=0; i<this.P; i++ )
			this.projectionCourante[i] = (1+c)*this.projectionCourante[i];
		}


	/**
	 * Calcule le nouvelle position en appliquant un déplacement
	 * par gaussienne indépendante.
	 */
	public void gaussienneIndependante (ParticuleTribe globalBest)
		{
		double d, sigma;
		for (int i=0; i<this.P; i++)
			{
			d = globalBest.meilleureProjection[i] - this.projectionCourante[i]; // On peut prendre aussi d = 0;
			sigma = Math.abs(globalBest.meilleureProjection[i] - this.projectionCourante[i]);

			this.projectionCourante[i] = this.meilleureProjection[i] + Calcul.loiNormale(d, sigma);
			}
		}


	/**
	 * Calcule la nouvelle position en appliquant un déplacement
	 * par estimation de distribution.
	 *
	 * Choisir un sous ensemble de particules de taille N = ?
	 * prendre la meilleure position de chaque particule : Pi
	 */
	public void EDA (int N)
		{
		/*----- Si les variables (particules) sont indépendantes -----*/
		double c, sigma;
		double som;

		for (int i=0; i<this.P; i++)
			{
			som = 0.0;

			for (int j=0; j<N; j++)
				som += this.meilleureProjection[j];

			c = 1.0/N * som;

			som = 0.0;
			for (int j=1; j<N; j++)
				som += Calcul.pow2(this.meilleureProjection[j] - c);

			sigma = Math.sqrt(1.0/(N-1) * som);

			this.projectionCourante[i] = Calcul.loiNormale(c, sigma);
			}

		/**
		 * Si les variables sont dépendantes utiliser la distribution gaussienne
		 * jointe à l'aide d'une méthode utilisant la factorisation de Cholesky.
		 */
		}


	/**
	 * Crée une copie d'une particule.
	 */
	public ParticuleTribe createCopie ()
		{
		ParticuleTribe p = new ParticuleTribe(this.indice);

		p.I = this.I;
		p.meilleurI = this.meilleurI;

		for (int i=0; i<this.P; i++)
			{
			p.projectionCourante[i] = this.projectionCourante[i];
			p.meilleureProjection[i] = this.meilleureProjection[i];
			}

		p.performance_prec = this.performance_prec;

		return p;
		}


	/**
	 * Retourne la représentation de la particule en caractères.
	 */
	@Override
	public String toString ()
		{
		StringBuilder s = new StringBuilder();
		s.append("----- Particule (").append(this.P).append(")\n");
		s.append("I                    : ").append(GuiUtils.DECIMAL_12.format(this.I)).append("\n");
		s.append("Meilleur I           : ").append(GuiUtils.DECIMAL_12.format(this.meilleurI)).append("\n");

		s.append("Projection courante  : ");
		for (int i=0; i<this.P -1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.projectionCourante[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.projectionCourante[this.P -1])).append("\n");

		s.append("Meilleure projection : ");
		for (int i=0; i<this.P -1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[this.P -1])).append("\n");

		s.append("-----");

		return s.toString();
		}

} /*----- Fin de la classe ParticuleTribe -----*/