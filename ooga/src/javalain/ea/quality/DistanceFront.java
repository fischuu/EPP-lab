package javalain.ea.quality;

import java.util.Arrays;
import javalain.ea.operator.comparator.LexicographicalComparator;
import javalain.math.Calcul;


/*---------------*/
/* DistanceFront */ // A suivre
/*---------------*/
/**
 * Cette classe contient des métriques qui évaluent la qualité
 * du front de Pareto trouvé par une méthode par rapport au véritable front
 * de Pareto d'un certain problème multiobjectif.
 *
 * Article(s) de référence :
 * D.A. Van Veldhuizen and G.B. Lamont
 * "Multiobjective evolutionary algorithm research: A history and analysis"
 * Technical Report TR-98-03, Department of Electrical and Computer Engineering,
 * Air Force Institute of Technology, Wright-Patterson AFB, Ohio, 1998.
 *
 * E. Zitzler and L. Thiele
 * "Multiobjective evolutionary algorithms: A comparative case study
 * and the strength pareto approach"
 * IEEE Trans on Evol. Computation, vol. 3, no. 4, p. 257–271, 1999.
 *
 * Deb K., Pratap A., Agarwal S. and Meyarivan, T.
 * "A fast and elitist multiobjective genetic algorithm: NSGA-II"
 * IEEE Trans. on Evol. Computation 6, p. 182-197, 2002.
 *
 * @author	Alain Berro
 * @version	6 août 2010
 */
public final class DistanceFront
{
	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la 'Generational Distance'.
	 */
	public static double generationalDistance (double[][] front,
											   double[][] trueParetoFront)
		{
		/*----- Valeurs minimales et maximales des objectifs -----*/
		double[] maxValues = getMaxValues(trueParetoFront);
		double[] minValues = getMinValues(trueParetoFront);
		
		/*----- Nomalisation des fronts -----*/
		double[][] normalizedFront = getNormalizedFront(front,minValues,maxValues);
		double[][] normalizedParetoFront = getNormalizedFront(trueParetoFront,minValues,maxValues);

		/*----- Calcule de la 'generational distance' -----*/
		double sommme = 0.0;
		for (int i=0; i<normalizedFront.length; i++)
			sommme += Calcul.pow2(distanceToClosedPoint(normalizedFront[i],normalizedParetoFront));

		return Math.sqrt(sommme)/normalizedFront.length;
		}


	/**
	 * Retourne la 'Inverted Generational Distance'.
	 */
	public static double invertedGenerationalDistance (double[][] front,
													   double[][] trueParetoFront)
		{
		/*----- Valeurs minimales et maximales des objectifs -----*/
		double[] maxValues = getMaxValues(trueParetoFront);
		double[] minValues = getMinValues(trueParetoFront);
		
		/*----- Nomalisation des fronts -----*/
		double[][] normalizedFront = getNormalizedFront(front,minValues,maxValues);
		double[][] normalizedParetoFront = getNormalizedFront(trueParetoFront,minValues,maxValues);

		/*----- Calcule de la 'generational distance' -----*/
		double sommme = 0.0;
		for (int i=0; i<normalizedParetoFront.length; i++)
			sommme += Calcul.pow2(distanceToClosedPoint(normalizedParetoFront[i], normalizedFront));

		return Math.sqrt(sommme)/normalizedParetoFront.length;
		}


	/**
	 * Retourne le 'spread'.
	 *
	 * Cette métrique ne s'applique que sur des problèmes biobjectifs.	// Recherche... Pourquoi ?
	 */
	public static double spread (double[][] front,
								 double[][] trueParetoFront)
		{
		/*----- Valeurs minimales et maximales des objectifs -----*/
		double[] maxValues = getMaxValues(trueParetoFront);
		double[] minValues = getMinValues(trueParetoFront);

		/*----- Nomalisation des fronts -----*/
		double[][] normalizedFront = getNormalizedFront(front,minValues,maxValues);
		double[][] normalizedParetoFront = getNormalizedFront(trueParetoFront,minValues,maxValues);

		/*----- Tri des solutions -----*/
		LexicographicalComparator lex_comparateur = new LexicographicalComparator();

		Arrays.sort(normalizedFront, lex_comparateur);
		Arrays.sort(normalizedParetoFront, lex_comparateur);

		/*----- Calcule le 'df' et 'dl' -----*/
		double df = Calcul.distanceEuclidienne(normalizedFront[0],normalizedParetoFront[0]);
		double dl = Calcul.distanceEuclidienne(normalizedFront[normalizedFront.length-1],normalizedParetoFront[normalizedParetoFront.length-1]);

		double moyenne = 0.0;
		double somme = df + dl;

		/*----- Calcul de la moyenne des distances entre les points -----*/
		for (int i=0; i<normalizedFront.length-1; i++)
			moyenne += Calcul.distanceEuclidienne(normalizedFront[i],normalizedFront[i+1]);

		moyenne = moyenne/(normalizedFront.length - 1);

		/**
		 * S'il y a plus d'un point alors on calcule sinon on retourne
		 * la plus mauvaise valeur : 1.0.
		 */
		if (normalizedFront.length > 1)
			{
			for (int i=0; i<normalizedFront.length-1; i++)
				somme += Math.abs(Calcul.distanceEuclidienne(normalizedFront[i],normalizedFront[i+1])-moyenne);

			return somme / (df + dl + (normalizedFront.length-1)*moyenne);
			}
		else
			return 1.0;
		}


	/**
	 * Retourne l'hypervolume.
	 */
	public static double hypervolume (double[][] front,
									  double[][] trueParetoFront)
		{
		/*----- Valeurs minimales et maximales des objectifs -----*/
		double[] maxValues = getMaxValues(trueParetoFront);
		double[] minValues = getMinValues(trueParetoFront);

		/*----- Nomalisation des fronts -----*/
		double[][] normalizedFront = getNormalizedFront(front,minValues,maxValues);

		/*----- Inversion du front -----*/
		/*
		   Inverse the pareto front. This is needed because of the original
		   metric by Zitzler is for maximization problems
		 */
		double [][] invertedFront = inverseFront(normalizedFront);

		/*----- Calcul de l'hypervolume (Code java de Zitzler) -----*/
		return calculHypervolume(invertedFront,invertedFront.length,invertedFront[0].length);
		}


	/**
	 * Retourne les valeurs maximales de chaque objectif.
	 */
	private static double[] getMaxValues (double[][] front)
		{
		int nbObj = front[0].length;

		double[] maxValues = new double[nbObj];
		Arrays.fill(maxValues, Double.NEGATIVE_INFINITY);

		for (int i=0; i<front.length; i++)
			for (int j=0; j<nbObj; j++)
				if (front[i][j] > maxValues[j])
					maxValues[j] = front[i][j];

		return maxValues;
		}


	/**
	 * Retourne les valeurs minimales de chaque objectif.
	 */
	private static double[] getMinValues (double[][] front)
		{
		int nbObj = front[0].length;

		double[] minValues = new double[nbObj];
		Arrays.fill(minValues, Double.POSITIVE_INFINITY);

		for (int i=0; i<front.length; i++)
			for (int j=0; j<nbObj; j++)
				if (front[i][j] < minValues[j])
					minValues[j] = front[i][j];

		return minValues;
		}


	/**
	 * Retourne le front normalisé.
	 */
	private static double[][] getNormalizedFront (double[][] front,
												  double[] minimumValue,
												  double[] maximumValue)
		{
		int nbSol = front.length;
		int nbObj = front[0].length;

		double[][] normalizedFront = new double[nbSol][nbObj];
		for (int i=0; i<nbSol; i++)
			for (int j=0; j<nbObj; j++)
				normalizedFront[i][j] = (front[i][j] - minimumValue[j]) / (maximumValue[j] - minimumValue[j]);

		return normalizedFront;
		}

	
	/**
	 * Retourne la distance minimale entre un point et
	 * le point le plus proche du front.
	 */
	private static double distanceToClosedPoint (double[] point,
												 double[][] front)
		{
		double min = Calcul.distanceEuclidienne(point,front[0]);

		double d;
		for (int i=1; i<front.length; i++)
			{
			d = Calcul.distanceEuclidienne(point,front[i]);
			if (d < min)
				min = d;
			}

		return min;
		}


	/**
	 * Inverse le front de Pareto.
	 */
	private static double[][] inverseFront (double [][] front)
		{
		double[][] inverseFront = new double[front.length][];

		for (int i=0; i<front.length; i++)
			{
			inverseFront[i] = new double[front[i].length];

			for (int j=0; j<front[i].length; j++)
				{
				if (front[i][j] <= 1.0 && front[i][j]>= 0.0)
					inverseFront[i][j] = 1.0 - front[i][j];
				else
				if (front[i][j] > 1.0)
					inverseFront[i][j] = 0.0;
				else
				if (front[i][j] < 0.0)
					inverseFront[i][j] = 1.0;
				}
			}

		return inverseFront;
		}




























	
	/**
	 * COde java de Zitzler
	 */
	private static double calculHypervolume (double[][]	front,
											 int		noPoints,
											 int		noObjectives)
		{
		int	n;
		double volume, distance;

		volume = 0;
		distance = 0;
		n = noPoints;
		while (n > 0)
			{
			int noNondominatedPoints;
			double tempVolume, tempDistance;

			noNondominatedPoints = filterNondominatedSet(front,n,noObjectives-1);
			tempVolume = 0;
			if (noObjectives < 3)
				{
				if (noNondominatedPoints < 1)
				System.err.println("run-time error");

				tempVolume = front[0][0];
				}
			else
			tempVolume = calculHypervolume(front,noNondominatedPoints,noObjectives - 1);

			tempDistance = surfaceUnchangedTo(front, n, noObjectives - 1);
			volume += tempVolume * (tempDistance - distance);
			distance = tempDistance;
			n = reduceNondominatedSet(front, n, noObjectives - 1, distance);
			}

		return volume;
		}






















	/* all nondominated points regarding the first 'noObjectives' dimensions
  are collected; the points referenced by 'front[0..noPoints-1]' are
  considered; 'front' is resorted, such that 'front[0..n-1]' contains
  the nondominated points; n is returned */
  static int  filterNondominatedSet(double [][] front, int  noPoints, int  noObjectives){ // A suivre teminer la prigrammation
    int  i, j;
    int  n;

    n = noPoints;
    i = 0;
    while (i < n) {
      j = i + 1;
      while (j < n) {
        if (dominates(front[i], front[j], noObjectives)) {
	/* remove point 'j' */
	  n--;
	  swap(front, j, n);
        } else if (dominates(front[j], front[i], noObjectives)) {
	/* remove point 'i'; ensure that the point copied to index 'i'
	   is considered in the next outer loop (thus, decrement i) */
	  n--;
	  swap(front, i, n);
	  i--;
	  break;
        } else
	  j++;
      }
      i++;
    }
    return n;
  } // FilterNondominatedSet


  /* calculate next value regarding dimension 'objective'; consider
     points referenced in 'front[0..noPoints-1]' */
  static double  surfaceUnchangedTo(double [][] front, int  noPoints, int  objective) {
    int     i;
    double  minValue, value;

    if (noPoints < 1)
      System.err.println("run-time error");

    minValue = front[0][objective];
    for (i = 1; i < noPoints; i++) {
      value = front[i][objective];
      if (value < minValue)
        minValue = value;
    }
    return minValue;
  } // SurfaceUnchangedTo

  /* remove all points which have a value <= 'threshold' regarding the
     dimension 'objective'; the points referenced by
     'front[0..noPoints-1]' are considered; 'front' is resorted, such that
     'front[0..n-1]' contains the remaining points; 'n' is returned */
 static int  reduceNondominatedSet(double [][] front, int  noPoints, int  objective,
			   double  threshold){
    int  n;
    int  i;

    n = noPoints;
    for (i = 0; i < n; i++)
      if (front[i][objective] <= threshold) {
        n--;
        swap(front, i, n);
      }

    return n;
  } // ReduceNondominatedSet

  /*
   returns true if 'point1' dominates 'points2' with respect to the
   to the first 'noObjectives' objectives
   */
  static boolean  dominates(double  point1[], double  point2[], int  noObjectives) {
    int  i;
    int  betterInAnyObjective;

    betterInAnyObjective = 0;
    for (i = 0; i < noObjectives && point1[i] >= point2[i]; i++)
      if (point1[i] > point2[i])
	betterInAnyObjective = 1;

    return ((i >= noObjectives) && (betterInAnyObjective>0));
  } //Dominates

  static void  swap(double [][] front, int  i, int  j){
    double  [] temp;

    temp = front[i];
    front[i] = front[j];
    front[j] = temp;
  } // Swap

} /*----- Fin de la classe DistanceFront -----*/
