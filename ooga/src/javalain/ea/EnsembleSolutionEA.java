package javalain.ea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javalain.ea.operator.comparator.ObjectiveComparator;
import javalain.math.fonction.Fct;


/*--------------------*/
/* EnsembleSolutionEA */
/*--------------------*/
/**
 * Un <code>EnsembleSolutionEA</code> regroupe un ensemble de solutions
 * à un problème multiobjectif.
 *
 * @author	Alain Berro
 * @version	13 aout 2010
 */
public class EnsembleSolutionEA
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Ensemble de solutions -----*/
	protected ArrayList<SolutionEA> liste;

	/*----- Problème à résoudre -----*/
	private Fct fct;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Crée un ensemble de solutions.
	 */
	public EnsembleSolutionEA (Fct pFct) { this(10,pFct); }

	public EnsembleSolutionEA (int pTaille,
							   Fct pFct)
		{
		this.liste = new ArrayList<SolutionEA>(pTaille);
		this.fct = pFct;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la taille de l'ensemble.
	 */
	public int size () { return this.liste.size(); }


	/**
	 * Efface le contenu de l'ensemble.
	 */
	public void clear () { this.liste.clear(); }


	/**
	 * Ajoute une solution à l'ensemble.
	 */
	public boolean add (SolutionEA s)
		{
		this.liste.add(s);

		return true;
		}


	/**
	 * Retourne la solution en position 'i' de l'ensemble.
	 */
	public SolutionEA get (int i) { return this.liste.get(i); }


	/**
	 * Retourne l'itérateur de l'ensemble.
	 */
	public Iterator<SolutionEA> iterator () { return this.liste.iterator(); }


	/**
	 * Retourne l'union de 'this' et de 'ensb'.
	 */
	public EnsembleSolutionEA union (EnsembleSolutionEA ensb)
		{
		if (!this.fct.equals(ensb.fct))
			throw(new IllegalArgumentException("EnsembleSolutionEA, union (EnsembleSolutionEA) : ensembles de solutions de problèmes différents : " + this.fct.getNomFct() + " et " + ensb.fct.getNomFct() + "."));

		EnsembleSolutionEA union = new EnsembleSolutionEA(this.size() + ensb.size(), this.fct);

		/*----- Ajout des éléments de 'this' -----*/
		for (int i=0; i<this.size(); i++) union.add(this.get(i));

		/*----- Ajout des éléments de 'ensb' -----*/
		for (int i=0; i<ensb.size(); i++) union.add(ensb.get(i));

		return union;
		}


	/**
	 * Retourne l'ensemble des solutions sous la forme d'une matrice
	 * des objectifs.
	 */
	public double[][] toMatriceObjectif ()
		{
		if (this.liste.isEmpty()) return null;

		int nbObj = this.liste.get(0).getObjectif().length;
		double[][] tab = new double[this.liste.size()][nbObj];

		for (int i=0; i<this.liste.size(); i++)
			System.arraycopy(this.liste.get(i).getObjectif(),0,tab[i],0,nbObj);

		return tab;
		}


	/**
	 * Calcul de la distance de crowding de toutes les solutions
	 * contenues dans l'ensemble.
	 */
	public void crowdingDistance ()
		{
		int size = this.liste.size();

		if (size == 0) return;

		if (size == 1)
			{
			this.liste.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
			}

		if (size == 2)
			{
			this.liste.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			this.liste.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
			}

		/*----- Initialisation des distances de crowding -----*/
		for (int i=0; i<size; i++)
			this.liste.get(i).setCrowdingDistance(0.0);

		double etendueObj;
		double distance;

		for (int i=0; i<this.fct.getNbFctObjectif(); i++)
			{
			/*----- Tri croissant des valeurs de l'ensemble des solutions en fonction du i ème objectif -----*/
			Collections.sort(this.liste,new ObjectiveComparator(i));

			etendueObj = this.liste.get(this.liste.size()-1).getObjectif()[i] - this.liste.get(0).getObjectif()[i];

			/*----- Calcul de la distance de crowding -----*/
			this.liste.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			this.liste.get(size-1).setCrowdingDistance(Double.POSITIVE_INFINITY);

			for (int j=1; j<size-1; j++)
				{
				distance = this.liste.get(j+1).getObjectif()[i] - this.liste.get(j-1).getObjectif()[i];
				distance = distance / etendueObj;
				distance += this.liste.get(j).getCrowdingDistance();

				this.liste.get(j).setCrowdingDistance(distance);
				}
			}
		}


	/**
	 * Tri de l'ensemble des solutions.
	 */
	public void sort (Comparator<SolutionEA> comp)
		{
		Collections.sort(this.liste, comp);
		}


	/**
	 * Affichage de l'ensemble.
	 */
	@Override
	public String toString()
		{
		StringBuilder sb = new StringBuilder();

		sb.append(">---> ").append(this.fct.getNomFct()).append(" - nombre de solutions=").append(this.liste.size()).append(", (x1...xn) n=").append(this.liste.get(0).getVariable().length).append(", (f1...fm) m=").append(this.liste.get(0).getObjectif().length).append("\n");
		for (int i=0; i<this.liste.size(); i++)
			sb.append(this.liste.get(i).toString()).append('\n');
		sb.append("<---< (Fin)");

		return sb.toString();
		}

} /*----- Fin de la classe EnsembleSolutionEA -----*/
