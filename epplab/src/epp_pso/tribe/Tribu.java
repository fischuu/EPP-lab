package epp_pso.tribe;

import java.util.ArrayList;
import javalain.math.PseudoRandomNumbers;


/*--------------*/
/* Classe Tribu */
/*--------------*/
/**
 * La classe <code>Tribu</code> ...
 * 
 * @author	Alain Berro, Souad Larabi
 * @version	8 juillet 2009
 */
public class Tribu
{
	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Essaim.
	 */
	private ArrayList<ParticuleTribe> tribe;


	/**
	 * Liens entre tribus.
	 */
	private ArrayList<Tribu> lien;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise la tribu.
	 *
	 * La tribu est créée sans aucune particule.
	 */
	public Tribu ()
		{
		/*----- Initialisation de la tribu -----*/
		this.tribe = new ArrayList<ParticuleTribe>();
		this.lien = new ArrayList<Tribu>();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne une tribu.
	 */
	public Tribu getLien (int i) { return this.lien.get(i); }


	/**
	 * Ajoute un lien avec une autre tribu.
	 */
	public void addLien (Tribu t) { this.lien.add(t); }


	/**
	 * Supprime un lien avec une tribu.
	 */
	public void removeLien (Tribu tr) { this.lien.remove(tr); }


	/**
	 * Retourne le nombre de liens avec d'autres tribus.
	 */
	public int nbLiens () { return this.lien.size(); }


	/**
	 * Ajoute une particule à la tribu.
	 */
	public void addParticule (ParticuleTribe p) { this.tribe.add(p); }


	/**
	 * Retourne la taille de la tribu.
	 */
	public int size () { return this.tribe.size(); }


	/**
	 * Retourne le nombre de bonnes particules dans la tribu.
	 */
	public int nbBestParticles ()
		{
		int nb = 0;
		for (int i=0; i<this.tribe.size(); i++)
			if (this.tribe.get(i).performance_prec == ParticuleTribe.MEILLEURE) nb++;

		return nb;
		}


	/**
	 * 1 ère étape de Tribe.
	 *
	 * Pour chaque particule :
	 * . Calcul de la valeur (de l'indice) de la position courante
	 * . Mise à jour de la meilleure position (projection)
	 * . Mise à jour de la performance
	 */
	public void updatePbest ()
		{
		for (int i=0; i<this.tribe.size(); i++)
			this.tribe.get(i).updatePbest();
		}


	/**
	 * 2 ième étape de Tribe.
	 *
	 * Calcule la nouvelle position de chaque particule.
	 *	. globalBest : la meilleure particule globale
	 *	. bool (si true) : autorise l'application de EDA() après un nombre fixé d'itérations
	 *	. N : le nombre de particules à choisir pour appliquer EDA()
	 */
	public void updatePosition (ParticuleTribe globalBest, boolean bool, int N)
		{
		for (int i=0; i<this.tribe.size(); i++)
			this.tribe.get(i).calculNouvellePosition(globalBest, bool, N);
		}


	/**
	 * La performance d'une tribu : true (bonne), false (false).
	 *
	 * Mesure de la qualité d'une tribu suivant CLERC.
	 */
	public boolean isPerformante ()
		{
		return (this.nbBestParticles() >  this.tribe.size()*PseudoRandomNumbers.random()) ? true : false;
		}


	/**
	 * Retourne la meilleure particule de la tribu.
	 */
	public ParticuleTribe getBestParticle ()
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
	 * Retourne la position de la 'pire' particule de la tribu.
	 */
	public int getPositionPireParticule ()
		{
		int pos = 0;
		double min = this.tribe.get(0).meilleurI;

		for (int i=1; i<this.tribe.size(); i++)
			if (min > this.tribe.get(i).meilleurI)
				{
				pos = i;
				min = this.tribe.get(i).meilleurI;
				}

		return pos;
		}


	/**
	 * Retourne la valeur de I de la 'pire' particule de la tribu.
	 */
	public double getIPireParticule ()
		{
		double min = this.tribe.get(0).meilleurI;

		for (int i=1; i<this.size(); i++)
			if (min > this.tribe.get(i).meilleurI)
				min = this.tribe.get(i).meilleurI;

		return min;
		}


	/**
	 * Supprime la pire des particules d'une bonne tribu de taille > 1.
	 */
	public void supprimePireParticule ()
		{
		if (this.isPerformante() && this.size() > 1)
			/*----- Supprimer la pire des particules -----*/
			 this.tribe.remove(this.getPositionPireParticule());
		}

}/*----- Fin de la classe Tribu -----*/