package epp_pso;

import indice.Indice;
import javalain.math.Calcul;


/* A faire - amélioration
 Il faut ajouter une fonction du type iteration()
 contenant
 		{
 		swarm.calculMeilleurePosition();
		swarm.calculNouvelleProjectionCourante();
		}

*/

/*---------------*/
/* Classe Essaim */
/*---------------*/
/**
 * La classe <code>Essaim</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	24 novembre 2010
 */
public class Essaim
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Essaim.
	 */
	private final Particule[] swarm;


	/**
	 * Nombres de particules de l'essaim.
	 */
	private final int nbParticules ;


	/**
	 * Paramètres de l'algorithme.
	 */
	private final ParametrePSO parametrePSO;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise l'essaim.
	 */
	public Essaim (ParametrePSO pso, Indice indice, int nb)
		{
		/*----- Paramètres PSO -----*/
		this.parametrePSO = pso;

		/*----- Initialisation de l'essaim -----*/
		this.nbParticules = nb;
		this.swarm = new Particule[this.nbParticules];

		for (int i=0; i<this.nbParticules; i++)
			this.swarm[i] = new Particule(indice);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la taille de l'essaim.
	 */
	public int getTaille () { return this.nbParticules; }


	/**
	 * 1 ère étape du PSO.
	 * Recherche de la meilleure position (projection) pour chaque particule.
	 */
	public void calculMeilleurePosition () // A faire - Changer le nom de la fonction !
		{
		for (int i=0; i<this.nbParticules; i++)
			this.swarm[i].calculMeilleurePosition();
		}


	/**
	 * 2 ième étape du PSO.
	 */
	public void calculNouvellePosition () // A faire - Changer le nom de la fonction !
		{
		/**
		 * Recherche la meilleure particule voisine pour chaque particule.
		 */

		/*----- Voisinage COSINUS -----*/
		if (this.parametrePSO.getVoisinage().equals(ParametrePSO.VOISINAGE_COSINUS))
			{
			for (int i=0; i<this.nbParticules; i++)
				this.swarm[i].meilleureProjectionVoisine = this.swarm[this.voisinageCosinus(i)].meilleureProjection;
			}

		/*----- Voisinage GLOBAL -----*/
		else
		if (this.parametrePSO.getVoisinage().equals(ParametrePSO.VOISINAGE_GLOBAL))
			{
			int k = 0;
			double maximum = this.swarm[0].meilleurI;

			for (int i=0; i<this.nbParticules; i++)
				{
				if (this.swarm[i].meilleurI > maximum)
					{
					maximum = this.swarm[i].meilleurI;
					k = i;
					}
				}

			for (int i=0; i<this.nbParticules; i++)
				this.swarm[i].meilleureProjectionVoisine = this.swarm[k].meilleureProjection;
			}

		/*----- Sans voisinage -----*/
		else
		if (this.parametrePSO.getVoisinage().equals(ParametrePSO.SANS_VOISINAGE))
			for (int i=0; i<this.nbParticules; i++)
				this.swarm[i].meilleureProjectionVoisine = this.swarm[i].meilleureProjection;


		/**
		 * Calcule la nouvelle position pour chaque particule.
		 */
		for (int i=0; i<this.nbParticules; i++)
			this.swarm[i].calculNouvellePosition();
		}


	/**
	 * Retourne la meilleure particule.
	 */
	public Particule getMeilleureParticule ()
		{
		int pos = 0;
		double max = this.swarm[0].meilleurI;

		for (int i=1; i<this.nbParticules; i++)
			if (max < this.swarm[i].meilleurI)
				{
				pos = i;
				max = this.swarm[i].meilleurI;
				}

		return this.swarm[pos];
		}


	/**
	 * Retourne la particule la plus proche au sens du voisinage cosinus.
	 */
	private int voisinageCosinus (int pos)
		{
		/*----- Compte le nombre de voisins -----*/
		int nb_voisins = 0;
		double angle;
		int k = pos;
		double max = this.swarm[pos].meilleurI;

		/*----- Position (projection) courante -----*/
		double[] proj = this.swarm[pos].projectionCourante;

		for (int i=0; i<this.nbParticules; i++)
			{
			angle = Calcul.arcCosinus(proj, this.swarm[i].projectionCourante);

			/**
			 * On ne prend que 3 voisons au plus (la particule incluse).
			 */
			if ((angle < Math.PI/6 || angle > Math.PI*5/6)
				&& (nb_voisins < 2 && pos != i))
				{
				nb_voisins++;

				if (this.swarm[i].meilleurI > max)
					{
					max = this.swarm[i].meilleurI;
					k = i ;
					}
				}
			}

		return k;
		}

}/*----- Fin de la classe Essaim -----*/