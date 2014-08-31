package epp_pso.tribes_new;

import java.util.ArrayList;
import javalain.math.Calcul;
import javalain.math.PseudoRandomNumbers;


/*--------------*/
/* Classe Tribe */
/*--------------*/
/**
 * La classe <code>Tribe</code> ...
 * 
 * @author	Alain Berro, Souad Larabi
 * @version	5 janvier 2011
 */
public class Tribe_new
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Essaim d'appartenance.
	 */
	private EssaimTribes_new swarm;


	/**
	 * Tribu.
	 */
	private ArrayList<ParticuleTribes_new> tribe;


	/**
	 * Liens entre tribus.
	 */
	private ArrayList<Tribe_new> lien;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Crée et initialise la tribu.
	 * La tribu est créée sans aucune particule.
	 */
	public Tribe_new (EssaimTribes_new swarm)
		{
		this.swarm = swarm;

		/*----- Initialisation de la tribu -----*/
		this.tribe = new ArrayList<ParticuleTribes_new>();
		this.lien = new ArrayList<Tribe_new>();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le lien avec la tribu 'i'.
	 */
	public Tribe_new getLien (int i) { return this.lien.get(i); }


	/**
	 * Ajoute un lien avec une tribu.
	 */
	public void addLien (Tribe_new t) { this.lien.add(t); }


	/**
	 * Supprime un lien avec une tribu.
	 */
	public void removeLien (Tribe_new tr) { this.lien.remove(tr); }


	/**
	 * Retourne le nombre de liens.
	 */
	public int nbLiens () { return this.lien.size(); }


	/**
	 * Ajoute une particule à la tribu.
	 */
	public void addParticule (ParticuleTribes_new p) { this.tribe.add(p); }


	/**
	 * Retourne la taille de la tribu.
	 */
	public int size () { return this.tribe.size(); }


	/**
	 * Calcule la nouvelle position de chaque particule de la tribu.
	 */
	public void updatePosition ()
		{
		ParticuleTribes_new shaman = this.getBestParticleOfTribe();

		ParticuleTribes_new informatrice;
		ParticuleTribes_new particule;
		for (int i=0; i<this.tribe.size(); i++)
			{
			particule = this.tribe.get(i);

			if (particule == shaman)
				{
				/*----- Si la particule est le shaman de la tribu, elle modifie sa position en fonction d'un autre shaman de l'essaim -----*/
				int choix = Calcul.randomInt(this.swarm.getNbTribus());
				if (this != this.swarm.getTribu(choix))
					informatrice = this.swarm.getTribu(choix).getBestParticleOfTribe();
				else
					return;
				}
			else
				/*----- Une particule modifie sa position en fonction de son shaman -----*/
				informatrice = shaman;

			/*----- Elle bouge ! -----*/
			particule.updatePosition(informatrice);
			}
		}


	/**
	 * Calcule la performance pour chaque particule de la tribu.
	 */
	public void updateBest ()
		{
		for (int i=0; i<this.tribe.size(); i++)
			this.tribe.get(i).updateBest();
		}


	/**
	 * Retourne si la tribu est bonne (true) ou non (false).
	 *
	 * Mesure de la qualité d'une tribu suivant CLERC.
	 * Soit nb le nombre de bonnes particules, une tribu est déclarée bonne
	 * si nb est supérieure à la taille_de_la_tribu*random[0,1[.
	 *
	 * Mesure de la qualité d'une tribu suivant COOREN
	 * Une tribu est déclarée bonne avec la probabilité de 0,5
	 * si elle possède au moins une bonne particule.
	 */
	public boolean isGood ()
		{
		/**
		 * CLERC.
		 */

		/*----- Calcule le nombre de bonnes particules dans la tribu -----*/
		int nb = 0;
		for (int i=0; i<this.tribe.size(); i++)
			if (this.tribe.get(i).isGood()) nb++;

		return (nb > this.tribe.size()*PseudoRandomNumbers.random()) ? true : false;	// CLERC
//		return (nb > 0 && PseudoRandomNumbers.random() > 0.5);							// COOREN
//		return (nb > 0);
		}


	/**
	 * Retourne la meilleure particule de la tribu.
	 */
	public ParticuleTribes_new getBestParticleOfTribe ()
		{
		int pos = 0;
		double max = this.tribe.get(0).meilleurI;

		for (int i=1; i<this.tribe.size(); i++)
			if (max < this.tribe.get(i).meilleurI)
				{
				pos = i;
				max = this.tribe.get(i).meilleurI;
				}

		return this.tribe.get(pos);
		}


	/**
	 * Supprime la pire des particules d'une bonne tribu de taille > 1.
	 */
	public void supprimePireParticule ()  // Recherche... Attention on ne peut pas supprimer à n'importe qu'elle condition.
		{
		int pos = 0;
		double min = this.tribe.get(0).meilleurI;

		for (int i=1; i<this.tribe.size(); i++)
			if (min > this.tribe.get(i).meilleurI)
				{
				pos = i;
				min = this.tribe.get(i).meilleurI;
				}

		this.tribe.remove(pos);
		}

} /*----- Fin de la classe Tribe -----*/