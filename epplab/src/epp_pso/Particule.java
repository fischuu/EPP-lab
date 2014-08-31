package epp_pso;

import indice.Indice;
import javalain.math.Calcul;
import javalain.math.PseudoRandomNumbers;
import math.Matrice;

import util.GuiUtils;


/*------------------*/
/* Classe Particule */
/*------------------*/
/**
 * La classe <code>Particule</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	3 novembre 2010
 */
public class Particule
{
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
	 * Performance (valeur de l'indice) de la particule.
	 */
	private double I;


	/**
	 * Meilleure performance (valeur de l'indice) par la particule.
	 */
	public double meilleurI;


	/**
	 * Position (projection) courante de la particule.
	 */
	public double[] projectionCourante;


	/**
	 * Position (projection) de la meilleure performance de la particule.
	 */
	public double[] meilleureProjection;


	/**
	 * Position de la meilleure informatrice (voisin).
	 */
	public double[] meilleureProjectionVoisine ;


	/**
	 * Vitesse de la particule.
	 */
	private double[] vitesse;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise la particule.
	 */
	public Particule (Indice ind)
		{
		/*----- Indice -----*/
		this.indice = ind;
		this.P = ind.getNombreParametres();

		/*----- Paramètres de la particule -----*/
		this.projectionCourante			= new double[this.P] ;
		this.vitesse					= new double[this.P] ;
		this.meilleureProjection		= new double[this.P] ;
		this.meilleureProjectionVoisine	= new double[this.P] ;

		/*----- Initialisation de la position de la particule et de sa vitesse -----*/
		for (int i=0; i<this.P; i++)
			{
			this.projectionCourante[i]			= PseudoRandomNumbers.random()*2 - 1;
			this.vitesse[i]						= PseudoRandomNumbers.random()*2 - 1;
			this.meilleureProjection[i]			= 0.0;
			this.meilleureProjectionVoisine[i]	= 0.0;
			}

		/*----- Normalisation la projection courante -----*/
		this.projectionCourante = Calcul.normaliseVecteur(this.projectionCourante);

		/*----- ICI ICI -----*/

		this.meilleurI = Double.NEGATIVE_INFINITY;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Calcule la valeur (de l'indice) de la position courante et
	 * mise à jour de la meilleure position (projection).
	 */
	public void calculMeilleurePosition ()
		{
		/*----- Valeur de l'indice -----*/
		this.I = this.indice.calcul(this.projectionCourante);

		/*----- Meilleure position (projection) ? -----*/
		if (this.I > this.meilleurI)
			{
			this.meilleurI = this.I;
			for (int k=0; k<this.P; k++)
				this.meilleureProjection[k] = this.projectionCourante[k];
			}
		}


	/**
	 * Calcule la nouvelle position de la particule en fonction de sa vitesse.
	 */
	public void calculNouvellePosition ()
		{
		/*----- Version 1 du PSO -----*/
		double c1 = 0.7;
		double Cmax = 1.43 ;	// Cmax doit appartenir à [1.5, 1.7]

		/**
		 * Construire les deux matrices diagonales AA et BB représentant
		 * les valeurs de confiance.
		 */
		Matrice AA = new Matrice(this.P);
		Matrice BB = new Matrice(this.P);

		for (int i=0; i<this.P; i++)
			for (int j=0; j<this.P; j++)
				if (i==j)
					{
					AA.setElement(i, i, Cmax * PseudoRandomNumbers.random());
					BB.setElement(i, i, Cmax * PseudoRandomNumbers.random());
					}
				else
					{
					AA.setElement(i, j, 0.0);
					BB.setElement(i, j, 0.0);
					}

		/**
		 * Calcul de la nouvelle position.
		 */
		double v;
		for (int i=0; i<this.P; i++)
			{
			v = c1*this.vitesse[i];

			if (v > 1.0)
				v = 1.0;
			else
			if (v < -1.0)
				v = -1.0;

			this.vitesse[i] = v + AA.element(i,i)*(this.meilleureProjection[i] - this.projectionCourante[i])
								+ BB.element(i,i)*(this.meilleureProjectionVoisine[i] - this.projectionCourante[i]);

			if (this.vitesse[i] > 1.0)
				this.vitesse[i] = 1.0;
			else
			if (this.vitesse[i] < -1.0)
				this.vitesse[i] = -1.0;

			this.projectionCourante[i] += this.vitesse[i];
			}

		/**
		 * Normalise la projection courante.
		 */
		this.projectionCourante = Calcul.normaliseVecteur(this.projectionCourante);
		}


	/**
	 * Retourne la représentation de la particule en caractères.
	 */
	@Override
	public String toString ()
		{
		StringBuilder s = new StringBuilder();

		s.append("----- Particule (n = ").append(this.P).append(")\n");
		s.append("I                    : ").append(GuiUtils.DECIMAL_12.format(this.I)).append("\n");
		s.append("Meilleur I           : ").append(GuiUtils.DECIMAL_12.format(this.meilleurI)).append("\n");

		s.append("Projection courante  : ");
		for (int i=0; i<this.P-1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.projectionCourante[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.projectionCourante[this.P-1])).append("\n");

		s.append("Meilleure projection : ");
		for (int i=0; i<this.P-1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjection[this.P-1])).append("\n");

		s.append("Meilleure voisine    : ");
		for (int i=0; i<this.P-1; i++)
			s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjectionVoisine[i])).append(", ");
		s.append(GuiUtils.DECIMAL_12.format(this.meilleureProjectionVoisine[this.P-1])).append("\n");
		s.append("-----");

		return s.toString();
		}

} /*----- Fin de la classe Particule -----*/