package epp_pso.tribe;

import indice.Indice;
import java.util.ArrayList;


/*--------------------*/
/* Classe EssaimTribe */
/*--------------------*/
/**
 * La classe <code>EssaimTribe</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	8 juillet 2009
 */
public class EssaimTribe
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Essaim composé de tribus.
	 */
	private ArrayList<Tribu> swarm;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise l'essaim.
	 * L'essaim est créée avec une tribu.
	 */
	public EssaimTribe (Indice indice)
		{
		/*----- Initialisation de l'essaim -----*/
		this.swarm = new ArrayList<Tribu>();

		/*----- Création d'une tribu avec une particule -----*/
		Tribu tr = new Tribu();
		tr.addParticule(new ParticuleTribe(indice));

		/*----- Ajout de la tribu -----*/
		this.swarm.add(tr);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le nombre de tribus de l'essaim.
	 */
	public int getNbTribus () { return this.swarm.size(); }

	public int getNbParticules ()
		{
		int cpt = 0;

		for (int i=0; i<swarm.size(); i++)
			cpt += swarm.get(i).size();

		return cpt;
		}


	/**
	 * 1 ère étape de Tribe.
	 *
	 * Pour chaque particule dans chaque tribu :
	 * . Calcul de la valeur (de l'indice) de la position courante
	 * . Mise à jour de la meilleure position (projection)
	 * . Mise à jour de la performance
	 */
	public void updatePbest ()
		{
		for (int i=0; i<this.swarm.size(); i++)
			this.swarm.get(i).updatePbest();
		}



	/**
	 * 2 ième étape de Tribe.
	 *
	 * Calcule la nouvelle position de chaque particule.
	 *	. bool (si true) : autorise l'application de EDA() après un nombre fixé d'itérations
	 *	. N : le nombre de particules à choisir pour appliquer EDA()
	 */
	public void updatePosition (boolean bool, int N)
		{
		ParticuleTribe g_best = this.getBestParticle().createCopie();

		for (int i=0; i<this.swarm.size(); i++)
			this.swarm.get(i).updatePosition(g_best, bool, N);
		}


	/**
	 * Retourne la meilleure particule dans tout l'essaim. // A faire - A optimiser en gardant en mémoire la meilleure particule de l'essaim lors du calculMeilleurePosition().
	 */
	public ParticuleTribe getBestParticle ()
		{
		ParticuleTribe g_best = this.swarm.get(0).getBestParticle();

		double max = g_best.meilleurI;

		ParticuleTribe p;
		for (int i=1; i<this.swarm.size(); i++)
			{
			p = this.swarm.get(i).getBestParticle();

			if (max < p.meilleurI)
				{
				max = p.meilleurI;
				g_best = p;
				}
			}

		return g_best;
		}


	/**
	 * Supprime la pire monotribu de l'essaim.
	 */
//	 public void supprimePireMonoTribu ()
//		{
//		/*----- Recherche la position de la pire monotribu -----*/
//		double val = Double.POSITIVE_INFINITY;
//		int pos = -1;
//
//		double i_pire;
//		for (int i=0; i<this.swarm.size(); i++)
//			{
//			i_pire = this.swarm.get(i).getIPireParticule();
//
//			if (this.swarm.get(i).size() == 1 && val > i_pire)
//				{
//				val = i_pire;
//				pos = i;
//				}
//			 }
//
//		/*----- Supprime la pire monotribu si elle existe -----*/
//		Tribu la_pire;
//		if (pos != -1)
//			{
//			la_pire = this.swarm.remove(pos);
//
//			/**
//			 * Gestion des liens. // Rem - Est-ce la bonne stratégie ?
//			 *						// Recherche NON voir page 158 du bouquin de Clerc.
//			 */
//
//			/*----- On supprime tous les liens vers la pire tribu -----*/
//			for (int i=0; i<la_pire.nbLiens(); i++)
//				la_pire.getLien(i).removeLien(la_pire);
//
//			boolean b = false;
//			if (this.swarm.get(0).nbLiens() == 0) b = true;
//
//			/*----- Remise à jour des liens -----*/
//			for (int i=1; i<this.swarm.size(); i++)
//				{
//				if (this.swarm.get(i).nbLiens() == 0)
//					{
//					/*----- On relie avec la tribu précédente -----*/
//					this.swarm.get(i).addLien(this.swarm.get(i-1));
//					this.swarm.get(i-1).addLien(this.swarm.get(i));
//					}
//				}
//
//			if (b)
//				{
//				/*----- On relie avec la tribu précédente -----*/
//				this.swarm.get(0).addLien(this.swarm.lastElement());
//				this.swarm.lastElement().addLien(this.swarm.get(0));
//				}
//			}
//		}


	/**
	 * Calcule le nombre de liens de l'essaim.
	 */
	public int nbLiens ()
		{
		int cpt_liens = 0;

		for (int i=0; i<this.swarm.size(); i++)
			cpt_liens += this.swarm.get(i).nbLiens();

		return cpt_liens;
		}


	 /**
	  * Supprime le pire particule de chaque bonne tribu de taille > 1.
	  */
	 public void supprimePireParticule ()
		{
		for (int i=0; i<this.swarm.size(); i++)
			this.swarm.get(i).supprimePireParticule();
		}


	/**
	 * Retourne le nombre de mauvaises tribus de l'essaim.
	 */
//	public int nbBadTribe ()
//		{
//		int cpt = 0;
//		for (int i=0; i<this.swarm.size(); i++)
//			if (!this.swarm.get(i).isPerformante())
//				cpt++;
//
//		return cpt;
//		}


	/**
	 * Création d'une nouvelle tribu.
	 */
	public void creationTribu(Indice indice)
		{
		/*----- Création de la nouvelle tribu (vide) -----*/
		Tribu new_tribe = new Tribu();

		/**
		 * S'il n'y a qu'1 ou 2 tribu(s).
		 */
		if (this.swarm.size() == 1 || this.swarm.size() == 2)
			{
			/*----- Ajout de la particule libre -----*/
			new_tribe.addParticule(new ParticuleTribe(indice));

			/*----- Ajout de la particule confinée (mais comme 1 ou 2 tribu(s) --> libre) -----*/
			new_tribe.addParticule(new ParticuleTribe(indice));

			/*----- Liens entre tribus -----*/
			Tribu tr = this.swarm.get(0);

			new_tribe.addLien(tr);
			tr.addLien(new_tribe);

			/*----- Ajout de la nouvelle tribu à l'essaim -----*/
			this.swarm.add(new_tribe);
			}
		else
			{
			/**
			 * On regarde toutes les tribus et pour chaque tribu non performante
			 * trouvée, on ajoute une particule libre et une confinée.
			 */
			Tribu tr;
			for (int i=0; i<this.swarm.size(); i++)
				{
				tr = this.swarm.get(i);

				if (!tr.isPerformante())
					{
					/*----- Ajout de la particule libre -----*/
					new_tribe.addParticule(new ParticuleTribe(indice));

					/*----- Ajout de la particule confinée -----*/
					new_tribe.addParticule(new ParticuleTribe(indice, this.getBestParticle(), tr.getBestParticle()));

					/*----- Liens entre tribus -----*/
					new_tribe.addLien(tr);
					tr.addLien(new_tribe);
					}
				}

			/*----- Ajout de la nouvelle tribu à l'essaim (si elle n'est pas vide de particules) -----*/
			if (new_tribe.size() != 0) this.swarm.add(new_tribe);
			}
		}


	/**
	 * Première adaptation.
	 *
	 * On vérifie si la 2 ième tribu créée est bonne
	 * sinon on crée une 3 ième tribu.
	 */
	public boolean premiereAdaptation ()
		{
		int i = 0;
		while (i<this.swarm.size() && this.swarm.get(i).isPerformante()) i++;

		return (i == this.swarm.size());
		}


	/**
	 * Adaptation : mise à jour de l'essaim.
	 *
	 * Création de tribu
	 * et/ou suppression de pire monotribu
	 * et/ou suppression de pire particule.
	 */
	public void adaptation (Indice indice)
		{
		this.creationTribu(indice);
//		this.supprimePireMonoTribu();
		this.supprimePireParticule();
		}

}/*----- Fin de la classe EssaimTribe -----*/