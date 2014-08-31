package epp_pso.tribes_new;

import indice.Indice;
import java.util.ArrayList;
import java.util.Iterator;


/*---------------------*/
/* Classe EssaimTribes */
/*---------------------*/
/**
 * La classe <code>EssaimTribes</code> ...
 *
 * @author	Alain Berro, Souad Larabi
 * @version	5 janvier 2011
 */
public class EssaimTribes_new
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Essaim composé de tribus.
	 */
	private ArrayList<Tribe_new> swarm;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée et initialise l'essaim.
	 * L'essaim est créée avec une tribu.
	 */
	public EssaimTribes_new (Indice indice)
		{
		/*----- Initialisation de l'essaim -----*/
		this.swarm = new ArrayList<Tribe_new>();

		/*----- Création d'une tribu avec une seule particule -----*/
		Tribe_new tr = new Tribe_new(this);
		tr.addParticule(new ParticuleTribes_new(indice));

		/*----- Ajout de la tribu -----*/
		this.swarm.add(tr);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la tribu 'i'de l'essaim.
	 */
	public Tribe_new getTribu (int i) { return this.swarm.get(i); }


	/**
	 * Retourne le nombre de tribus/de particules de l'essaim.
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
	 * Calcule la nouvelle position de chaque particule de l'essaim.
	 */
	public void updatePosition (boolean bool, int N)
		{
		for (int i=0; i<this.swarm.size(); i++)
			this.swarm.get(i).updatePosition();
		}


	/**
	 * Calcule la performance pour chaque particule de l'essaim.
	 */
	public void updateBest ()
		{
		for (int i=0; i<this.swarm.size(); i++)
			this.swarm.get(i).updateBest();
		}


	/**
	 * Recherche de la meilleure particule dans tout l'essaim.
	 */
	public ParticuleTribes_new getBestParticle ()
		{
		ParticuleTribes_new global_best = this.swarm.get(0).getBestParticleOfTribe();

		double max = global_best.meilleurI;

		ParticuleTribes_new p;
		for (int i=1; i<this.swarm.size(); i++)
			{
			p = this.swarm.get(i).getBestParticleOfTribe();

			if (max < p.meilleurI)
				{
				max = p.meilleurI;
				global_best = p;
				}
			}

		return global_best;
		}


	/**
	 * Adaptation : mise à jour de l'essaim.
	 *
	 * Création de tribu.
	 * et/ou suppression de pire monotribu.
	 * et/ou suppression de pire particule.
	 */
	public void adaptation (Indice indice)
		{
		/*----- Shaman des shamans -----*/
		ParticuleTribes_new best_shaman = this.getBestParticle();

		/*----- Création de la nouvelle tribu (vide) -----*/
		Tribe_new new_tribe = new Tribe_new(this);

		Iterator<Tribe_new> iterator = this.swarm.iterator();

		Tribe_new tribu;
		ParticuleTribes_new shaman;
		while (iterator.hasNext())
			{
			tribu = iterator.next();
			shaman = tribu.getBestParticleOfTribe();

			if (!tribu.isGood())
				{
				/**
				 * Si la tribu n'est pas bonne alors on génère une particule
				 * libre et une particule confinée.
				 */

				/*----- Particule libre -----*/
				new_tribe.addParticule(new ParticuleTribes_new(indice));

				/*----- Particule confinée -----*/
				if (shaman != best_shaman)
					new_tribe.addParticule(new ParticuleTribes_new(indice, best_shaman, shaman));
				else
					new_tribe.addParticule(new ParticuleTribes_new(indice));
				}
			else
				{
				/**
				 * Si la tribu est bonne alors on élimine la plus mauvaise
				 * particule de la tribu.
				 */
				if (tribu.size() == 1)
					/**
					 * Tribu de taille 1 donc on n'élimine que si
					 * une de ses informatrices est plus forte qu'elle.
					 */
					{
					if (shaman != best_shaman) // Recherche... Cas d'un graphe complet ! PROBLEMME ON DOIT SUPPRIMER SI MAUVAISE
						iterator.remove();
					}
				else
					{
					/**
					 * Tribu de taille > 1 alors on supprime la plus mauvaise
					 * particule.
					 */
					tribu.supprimePireParticule();
					}
				}
			}

		/*----- Ajout de la nouvelle tribu à l'essaim (si elle n'est pas vide de particules) -----*/
		if (new_tribe.size() != 0) this.swarm.add(new_tribe);
		}


	/**
	 * Calcule le nombre de liens de l'essaim. // Recherche... Cas d'un graphe complet !
	 */
	public int nbLiens ()
		{
		int k;

		int cpt = 0;
		for (int i=0; i<this.swarm.size(); i++)
			{
			k = this.swarm.get(i).size();
			cpt += k*k;
			}

		k = this.getNbTribus();

		return cpt + k*(k-1);
		}

}/*----- Fin de la classe EssaimTribes -----*/