package javalain.algorithmegenetique;


/*-------------*/
/* ParametreAG */
/*-------------*/
/**
 * Cette classe regroupe les paramètres d'évolution d'un algorithme
 * évolutionnaire.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	18 mai 2008
 */
public class ParametreAG
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/*----- Sélection stochastique -----*/

	/**
	 * Sélection par rang.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setSelection
	 */
	public final static int RANG = 0;


	/**
	 * Sélection par tournoi à <code>N</code> participants.
	 * <p>
	 * <code>N</code> individus sont sélectionnés aléatoirement pour participer
	 * au tournoi, le vainqueur est celui qui a la plus forte note.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setSelection
	 * @see	javalain.algorithmegenetique.ParametreAG#setNbPartTournoi
	 */
	public final static int TOURNOI = 1;


	/**
	 * Sélection par roulette pipée.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setSelection
	 */
	public final static int ROULETTE_PIPEE = 2;


	/**
	 * Sélection par reste stochastique.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setSelection
	 */
	public final static int RESTE_STOCHASTIQUE = 3;


	/*----- Algorithme -----*/

	/**
	 * Generational Genetic Algorithm.
	 * <p>
	 * Appelé aussi <code>Simple Genetic Algorithm</code>, il utilise
	 * une sélection stochastique pour sélectionner <code>N</code> parents
	 * (population <code>p<sup>'</sup></code>).
	 * Certains parents peuvent donc être sélectionnés plusieurs fois et
	 * d'autres aucune fois en fonction de leur capacité de survie.
	 * Ces <code>N</code> parents génèrent ensuite <code>N</code> enfants
	 * (population <code>p<sup>''</sup></code>) par application des opérateurs génétiques
	 * de croisement et de mutation.
	 * Enfin ces <code>N</code> enfants remplacent purement et simplement
	 * les <code>N</code> parents pour créer la génération suivante.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 */
	public final static String GGA = "Generational GA";


	/**
	 * Steady-State Genetic Algorithm.
	 * <p>
	 * Cet algorithme utilise la technique du chevauchement (overlapping).
	 * A partir d'une population initiale <code>p<sub>0</sub></code> de taille
	 * <code>N</code>, une sélection stochastique est utilisée pour sélectionner
	 * <code>M (M&lt;N)</code> individus
	 * (population <code>p<sub>0</sub><sup>'</sup></code>). Ces <code>M</code>
	 * parents générent ensuite <code>M</code> enfants par application des
	 * opérateurs génétiques de croisement et de mutation (population
	 * <code>p<sub>0</sub><sup>''</sup></code>). Nous obtenons ainsi une
	 * population de <code>N+M</code> individus (<code>p<sub>0</sub>+
	 * p<sub>0</sub><sup>''</sup></code>). Pour passer à la génération suivante
	 * de taille <code>N</code>, une heuristique de remplacement est appliquée :
	 * <ul>
	 *	<li><code>ALEATOIRE</code> : les <code>M</code> individus de
	 *		<code>p<sub>0</sub><sup>''</sup></code> remplacent <code>M</code>
	 *		parmi <code>N</code> de <code>p<sub>0</sub></code> aléatoirement.
	 *	<li><code>LES_PLUS_MAUVAIS</code> : les <code>N</code> meilleurs des
	 *		<code>N+M</code> survivent.
	 *	<li><code>TOURNOI_INVERSE</code> : les <code>M</code> individus de
	 *		<code>p<sub>0</sub><sup>''</sup></code> remplacent <code>M</code>
	 *		mauvais parmi <code>N</code> de <code>p<sub>0</sub></code> par
	 *		tournoi inversé.
	 * </ul>
	 * <p>
	 * <b>Remarque:</b>	Le taux de croisement n'a aucune importance dans cet
	 *					algorithme. En effet le passage de la population
	 *					<code>p<sub>0</sub><sup>'</sup></code> à
	 *					<code>p<sub>0</sub><sup>''</sup></code> s'effectue par
	 *					croisement 2 à 2 des individus (si <code>M</code> est
	 *					impair le dernier individus est créé par mutation).<br>
	 *					Par contre le taux de mutation conserve toute son
	 *					importance, après le croisement les enfants obtenus
	 *					peuvent subir une mutation.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 */
	public final static String SSGA = "Steady-State GA";


	/**
	 * Evolution Strategy.
	 * <p>
	 * Les stratégies d'évolution sont basées uniquement sur la mutation. Il
	 * existe plusieurs modes d'évolution différents regroupés sous deux formes
	 * algébriques : <code>(µ,y)-ES</code> et <code>(µ+y)-ES</code>.
	 * <ul>
	 *	<li><code>µ</code> est le nombre d'individus de la population initiale.
	 *	<li><code>y</code> est le nombre d'individus générés par mutation
	 *		(<code>y &gt;= µ</code>).
	 *	<li><code>(µ,y)-ES</code> signifie que pour générer la génération
	 *		suivante on sélectionne les <code>µ</code> meillleurs individus
	 *		parmi les <code>y</code>.
	 *	<li><code>(µ+y)-ES</code> signifie que pour générer la génération
	 *		suivante on sélectionne les <code>µ</code> meillleurs individus
	 *		parmi les <code>µ+y</code>.
	 * </ul>
	 * <p>
	 * Dans tous les cas l'algorithme se déroule de la façon suivante :
	 * <ul>
	 *	<li>On sélectionne de manière uniforme un parent.
	 *	<li>On génére un enfant par mutation de ce parent.
	 *	<li>Le parent est remis dans la population initiale.
	 *	<li>On recommence jusqu'à la génération des <code>y</code> enfants.
	 * </ul>
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 */
	public final static String ES = "Evolution Strategy";


	/**
	 * MultiObjective Genetic Algorithm.
	 * <p>
	 * <code>MOGA</code> est une méthode de sélection par <code>RANG</code>
	 * spécifique.
	 * <p>
	 * En 1993 Fonseca et Fleming propose une méthode dans laquelle chaque
	 * individu de la population est rangé en fonction du nombre d'individus qui
	 * le dominent. Ensuite, ils utilisent une fonction de notation permettant
	 * de prendre en compte le rang de l'individu et le nombre d'individus ayant
	 * même rang. Soit un individu <code>x<sub>i</sub></code> à la génération
	 * <code>t</code>, dominé par <code>p<sub>i</sub>(t)</code> individus. Le
	 * rang de cet individu est :
	 * <code>rang(x<sub>i</sub>, t) = 1 + p<sub>i</sub>(t)</code>
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 * @see	javalain.algorithmegenetique.Population#fctTransfMoga
	 */
	public final static String MOGA = "MultiObjective GA";


	/**
	 * Niched Pareto Genetic Algorithm.
	 * <p>
	 * <code>NPGA</code> est une méthode de sélection par <code>TOURNOI</code>
	 * spécifique.
	 * <p>
	 * En 1993 Horn et Napfliotis propose une méthode qui utilise un tournoi
	 * basé sur la notion de dominance de Pareto. Elle compare deux individus
	 * pris au hasard avec une sous-population de taille
	 * <code>t<sub>dom</sub></code> également choisie au hasard. Si un seul de
	 * ces deux individus domine le sous-groupe, il est alors positionné dans
	 * la population suivante. Dans les autres cas une fonction de sharing
	 * est appliquée pour sélectionner l'individu.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 */
	public final static String NPGA = "Niched Pareto GA";


	/**
	 * Nondominated Sorting Genetic Algorithm.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 */
	public final static String NSGA_2 = "Nondominated Sorting GA 2";


	/**
	 * Vector Evaluated Genetic Algorithm.
	 * <p>
	 * En 1985 Schaffer propose une extension d’un algorithme génétique simple
	 * (SGA ou GGA) pour la résolution de problèmes multiobjectifs. Cette
	 * méthode est appelée Vector Evaluated Genetic Algorithm. La seule
	 * différence avec un GGA est la manière dont s’effectue la sélection.
	 * L’idée est simple. Si nous avons <code>k</code> objectifs et une
	 * population de <code>n</code> individus, une sélection de <code>n/k</code>
	 * individus est effectuée pour chaque objectif. Ainsi <code>k</code>
	 * sous-populations vont être créées, chacune d’entre elles contenant les
	 * <code>n/k</code> meilleurs individus pour un objectif particulier.
	 * Les <code>k</code> sous-populations sont ensuite mélangées afin d'obtenir
	 * une nouvelle population de taille <code>n</code>. Le processus se termine
	 * par l’application des opérateurs génétiques de modification (croisement
	 * et mutation).
	 * <p>
	 * <b>Remarque:</b>	L'utilisateur peut associer à cette méthode les modes de
	 *					sélection suivants : <code>TOURNOI</code>,
	 *					<code>ROULETTE_PIPEE</code> et <code>RANG</code>.
	 *					La sélection par <code>RESTE_STOCHASTIQUE</code> est
	 *					automatiquement remplacée par une
	 *					<code>ROULETTE_PIPEE</code>.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setAlgorithme
	 */
	public final static String VEGA = "Vector Evaluated GA";


	/*----- Méthode de remplacement -----*/

	/**
	 * Les enfants remplacent leurs parents.
	 */
	private final static int ENFANT_PARENT = 20;


	/**
	 * Le remplacement s'effectue en choisissant aléatoirement l'individu à
	 * remplacer dans la population initiale.
	 * <p>
	 * Le tirage aléatoire est uniforme avec remise.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setRemplacement
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 */
	public final static int ALEATOIRE = 21;


	/**
	 * On remplace les individus les plus mauvais de la population initiale.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setRemplacement
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 */
	public final static int LES_PLUS_MAUVAIS = 22;


	/**
	 * La sélection des individus remplacés s'effectue à l'aide d'un tournoi
	 * inversé c'est-à-dire un tournoi qui sélectionne le plus mauvais
	 * participant.
	 * <p>
	 * Le nombre de participants du tournoi inversé est fixé comme pour
	 * le tournoi normal.
	 * <p>
	 * <b>Remarque:</b>	Si un même individu est sélectionné plusieurs fois
	 *					seul le dernier remplacement est effectué.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setRemplacement
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 * @see	javalain.algorithmegenetique.ParametreAG#setNbPartTournoi
	 */
	public final static int TOURNOI_INVERSE = 23;


	/**
	 * On remplace les individus les plus vieux de la population initiale.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setRemplacement
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 */
	public final static int LES_PLUS_VIEUX = 24;


	/**
	 * La méthode de remplacement est donné par l'utilisateur.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setRemplacement
	 * @see javalain.algorithmegenetique.ParametreAG#setNbOverlap
	 */
	public final static int PERSO = 25;


	/*---------*/
	/* Données */
	/*---------*/

	/**
	 * Nom de la <code>Population</code>.
	 */
	private String nom;


	/**
	 * Algorithme.
	 */
	private String algorithme;


	/**
	 * Taux de mutation inclus dans [0,1[.
	 */
	private double tauxMutation;


	/**
	 * Taux de croisement inclus dans [0,1[.
	 */
	private double tauxCroisement;


	/**
	 * Mode de sélection.
	 */
	private int selection;


	/**
	 * Nombre de participants au tournoi.
	 */
	private int nbPartTournoi;


	/**
	 * Mise à l'échelle.
	 */
	private boolean scaling;


	/**
	 * Heuristique de partage.
	 */
	private boolean sharing;


	/**
	 * Paramètre d'influence dans l'heuristique de partage (Sharing).
	 */
	private double alpha;


	/**
	 * Paramètre permettant de délimiter le voisinage dans l'heuristique
	 * de partage (Sharing).
	 */
	private double sigmaShare;


	/**
	 * Heuristique de remplacement.
	 */
	private boolean crowding;


	/**
	 * Croisement restreint.
	 */
	private boolean croisementRestreint;


	/**
	 * Heuristique d'élitisme, copie du ou des meilleur(s) dans
	 * la génération suivante.
	 */
	private boolean elitisme;


	/**
	 * Nombre de meilleurs individus copiés dans la génération suivante.
	 */
	private int nbMeilleurs;


	/**
	 * Méthode de remplacement utilisée.
	 */
	private int remplacement;


	/**
	 * Nombre d'individus supplémentaires pour l'algorithme <code>SSGA</code>.
	 */
	private int nbOverlap;


	/**
	 * Taille de la sous poppulation dans la méthode <code>NPGA</code>.
	 */
	private int tdom;


	/*---------------*/
	/* Constructeurs */
	/*---------------*/

	/**
	 * Construit et initialise les paramètres d'évolution de l'algorithme
	 * génétique.
	 * <p>
	 * <b>Valeurs par défaut:</b>
	 * <ul>
	 *	<li>Algorithme : <code>Generational GA</code>
	 *	<li>Taux de mutation : <code>0.05</code>
	 *	<li>Taux de croisement : <code>0.65</code>
	 *	<li>Sélection : <code>TOURNOI</code>
	 *	<li>Nombre de participant au tournoi : <code>3</code>
	 *	<li>Mise à l'échelle : <code>false</code>
	 *	<li>Heuristique de partage : <code>false</code>
	 *	<li>Alpha : <code>0.5</code>
	 *	<li>SigmaShare : <code>0.0</code>
	 *	<li>Heuristique de remplacement : <code>false</code>
	 *	<li>Croisement restreint : <code>false</code>
	 *	<li>Elitisme : <code>false</code>
	 *	<li>Nombre de meilleurs individus copiés : <code>1</code>
	 *	<li>Méthode de remplacement : <code>LES_PLUS_MAUVAIS</code>
	 *	<li>Nombre d'individus supplémentaires : <code>1</code>
	 *	<li>Tdom : <code>10</code>
	 * </ul>
	 *
	 * @param	s	nom de l'évolution
	 */
	public ParametreAG (String s)
		{
		this.nom					= s;

		/*----- Paramètres par défaut -----*/
		this.algorithme				= ParametreAG.GGA;
		this.tauxMutation			= 0.05;
		this.tauxCroisement			= 0.65;
		this.selection				= ParametreAG.TOURNOI;
		this.nbPartTournoi			= 3;
		this.scaling				= false;
		this.sharing				= false;
		this.alpha					= 0.5;
		this.sigmaShare				= 0.0;
		this.crowding				= false;
		this.croisementRestreint	= false;
		this.elitisme				= false;
		this.nbMeilleurs			= 1;
		this.remplacement			= ParametreAG.LES_PLUS_MAUVAIS;
		this.nbOverlap				= 1;
		this.tdom					= 10;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le nom de la <code>Population</code>.
	 */
	public String getNom () { return this.nom; }


	/**
	 * Modifie l'algorithme à utiliser.
	 *
	 * @param	type_algo	Algorithme : <code>GGA</code>, <code>SSGA</code>
	 *						<code>ES</code>, <code>MOGA</code>,
	 *						<code>NPGA</code>, <code>NSGA</code> ou
	 *						<code>VEGA</code>,
	 *
	 * @exception	IllegalArgumentException si le type d'algorithme
	 *				évolutionnaire est inconnu
	 */
	public void setAlgorithme (String type_algo)
		{
		if	(type_algo.equals(ParametreAG.GGA)		||
			 type_algo.equals(ParametreAG.SSGA)		||
			 type_algo.equals(ParametreAG.ES)		||
			 type_algo.equals(ParametreAG.MOGA)		||
			 type_algo.equals(ParametreAG.NPGA)		||
			 type_algo.equals(ParametreAG.NSGA_2)	||
			 type_algo.equals(ParametreAG.VEGA))
			this.algorithme = type_algo;
		else
			throw(new IllegalArgumentException("ParametreAG, setAlgorithme (String type_algo) : algorithme inconnu."));
		}


	/**
	 * Retourne l'algorithme utilisé.
	 */
	public String getAlgorithme () { return this.algorithme; }


	/**
	 * Modifie le taux de mutation.
	 *
	 * @exception	IllegalArgumentException si le taux de mutation n'est pas
	 *				compris entre 0 et 1
	 */
	public void setTauxMutation (double d)
		{
		if	(d < 0.0 || d > 1.0)
			throw(new IllegalArgumentException("ParametreAG, setTauxMutation (double d) : le taux de mutation doit etre compris entre 0 et 1."));
		else
			this.tauxMutation = d;
		}


	/**
	 * Retourne le taux de mutation.
	 */
	public double getTauxMutation () { return this.tauxMutation; }


	/**
	 * Modifie le taux de croisement.
	 *
	 * @exception	IllegalArgumentException si le taux de croisement n'est pas
	 *				compris entre 0 et 1
	 */
	public void setTauxCroisement (double d)
		{
		if	(d < 0.0 || d > 1.0)
			throw(new IllegalArgumentException("ParametreAG, setTauxCroisement (double d) : le taux de croisement doit etre compris entre 0 et 1."));
		else
			this.tauxCroisement = d;
		}


	/**
	 * Retourne le taux de croisement.
	 */
	public double getTauxCroisement () { return this.tauxCroisement; }


	/**
	 * Modifie le mode de sélection.
	 * <p>
	 * <b>Remarque:</b>	Pour certains algorithmes le mode de sélection est fixé.
	 *					L'utilisateur ne pourra pas modifier ce mode.
	 * <ul>
	 *	<li><code>MOGA</code> : sélection par <code>RANG</code>.
	 *	<li><code>NPGA</code> : sélection par <code>TOURNOI</code>.
	 *	<li><code>VEGA</code> : sélection par <code>RESTE_STOCHASTIQUE</code>
	 *		remplacée par <code>ROULETTE_PIPEE</code>.
	 * </ul>
	 *
	 * @param	sel	mode de sélection : <code>RANG</code>, <code>TOURNOI</code>,
	 *				<code>ROULETTE_PIPEE</code> ou
	 *				<code>RESTE_STOCHASTIQUE</code>
	 *
	 * @exception	IllegalArgumentException si le mode de sélection est inconnu
	 */
	public void setSelection (int sel)
		{
		if	(sel == ParametreAG.RANG				||
			 sel == ParametreAG.TOURNOI				||
			 sel == ParametreAG.ROULETTE_PIPEE		||
			 sel == ParametreAG.RESTE_STOCHASTIQUE)
			this.selection = sel;
		else
			throw(new IllegalArgumentException("ParametreAG, setSelection (int sel) : mode de selection inconnu."));
		}


	/**
	 * Retourne le mode de sélection.
	 */
	public int getSelection ()
		{
		if	(this.algorithme.equals(ParametreAG.NPGA)) return ParametreAG.TOURNOI;
		else
		if	(this.algorithme.equals(ParametreAG.MOGA)) return ParametreAG.RANG;
		else
		if	(this.algorithme.equals(ParametreAG.VEGA) && this.selection == ParametreAG.RESTE_STOCHASTIQUE) return ParametreAG.ROULETTE_PIPEE;
		else
			return this.selection;
		}


	/**
	 * Modifie le nombre de participants au tournoi.
	 *
	 * @exception	IllegalArgumentException si le nombre de participants est
	 *				inférieur à 2
	 */
	public void setNbPartTournoi (int i)
		{
		if	(i < 2)
			throw(new IllegalArgumentException("ParametreAG, setNbPartTournoi (int i) : le nombre de participants au tournoi doit etre superieur a 1."));
		else
			this.nbPartTournoi = i;
		}


	/**
	 * Retourne le nombre de participants au tournoi.
	 */
	public int getNbPartTournoi () { return this.nbPartTournoi; }


	/**
	 * Active/Désactive l'heuristique de mise à l'échelle (Scaling).
	 * <p>
	 * <b>Remarque:</b>	L'heuristique de mise à l'échelle n'est pas utilisable
	 *					avec les algorithmes <code>SSGA</code> et
	 *					<code>VEGA</code>.
	 *
	 * @see	javalain.algorithmegenetique.Population#fctScaling
	 */
	public void setScaling (boolean b) { this.scaling = b; }


	/**
	 * Etat de la mise à l'échelle (Scaling).
	 *
	 * @return	<code>true</code> si la mise à l'échelle (Scaling) est activée
	 */
	public boolean getScaling ()
		{
		if	(this.algorithme.equals(ParametreAG.SSGA) || this.algorithme.equals(ParametreAG.VEGA))
			return false;
		else
			return this.scaling;
		}


	/**
	 * Active/Désactive l'heuristique de partage (Sharing).
	 * <p>
	 * <b>Remarque:</b>	L'heuritique de partage est à <code>true</code> si on
	 *					utilise le <code>NPGA</code> et n'est pas utilisable
	 *					avec <code>VEGA</code>.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#NPGA
	 */
	public void setSharing (boolean b) { this.sharing = b; }


	/**
	 * Etat de l'heuristique de partage (Sharing).
	 *
	 * @return	<code>true</code> si l'heuristique de partage (Sharing) est activée
	 */
	public boolean getSharing ()
		{
		if	(this.algorithme.equals(ParametreAG.NPGA))
			return true;
		else
			if	(this.algorithme.equals(ParametreAG.VEGA))
				return false;
			else
				return this.sharing;
		}


	/**
	 * Modifie la valeur de <code>alpha</code>.
	 * <p>
	 * <code>alpha</code> est un paramètre de l'heuristique de partage (Sharing).
	 *
	 * @exception	IllegalArgumentException si <code>alpha</code> < 0.0
	 */
	public void setAlpha (double d)
		{
		if	(d < 0.0)
			throw(new IllegalArgumentException("ParametreAG, setAlpha (double d) : alpha doit etre > 0.0."));
		else
			this.alpha = d;
		}


	/**
	 * Retourne la valeur de alpha.
	 */
	public double getAlpha () { return this.alpha; }


	/**
	 * Modifie la valeur de <code>sigma<sub>share</sub></code>.
	 * <p>
	 * <code>sigma<sub>share</sub></code> est un paramètre de l'heuristique de
	 * partage (Sharing).
	 *
	 * @exception	IllegalArgumentException
	 *				si <code>sigma<sub>share</sub></code> < 0.0
	 */
	public void setSigmaShare (double d)
		{
		if	(d < 0.0)
			throw(new IllegalArgumentException("ParametreAG, setSigmaShare (double d) : sigmashare doit etre > 0.0."));
		else
			this.sigmaShare = d;
		}


	/**
	 * Retourne la valeur de <code>sigma<sub>share</sub></code>.
	 */
	public double getSigmaShare () { return this.sigmaShare; }


	/**
	 * Active/Désactive l'heuristique de remplacement (Crowding).
	 */
	public void setCrowding (boolean b) { this.crowding = b; }


	/**
	 * Etat de l'heuristique de remplacement (Crowding).
	 *
	 * @return	<code>true</code> si l'heuristique de remplacement (Crowding)
	 *			est activée
	 */
	public boolean getCrowding () { return this.crowding; }


	/**
	 * Active/Désactive le croisement restreint.
	 */
	public void setCroisementRestreint (boolean b) { this.croisementRestreint = b; }


	/**
	 * Etat du croisement restreint.
	 *
	 * @return	<code>true</code> si le croisement restreint est activé
	 */
	public boolean getCroisementRestreint () { return this.croisementRestreint; }



	/**
	 * Active/Désactive l'heuristique d'élitisme.
	 * <p>
	 * Lors de la création d'une nouvelle population, il y a de fortes chances
	 * que les meilleurs individus soient perdus après l'application des
	 * opérations génétiques. Pour éviter cela, on utilise l'heuristique de
	 * l'élitisme qui consiste à copier un ou plusieurs des meilleurs
	 * individus dans la nouvelle génération.
	 * <p>
	 * <b>Remarque:</b>	L'élitisme ne s'applique qu'avec le
	 *					<code>Generational GA</code>.
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setNbMeilleurs
	 */
	public void setElitisme (boolean b) { this.elitisme = b; }


	/**
	 * Etat de l'heuristique d'élitisme.
	 *
	 * @return	<code>true</code> si l'heuristique d'élitisme est activée
	 */
	public boolean getElitisme ()
		{
		if	(this.algorithme.equals(ParametreAG.GGA))
			return this.elitisme;
		else
			return false;
		}


	/**
	 * Modifie le nombre de meilleurs individus copiés dans la génération
	 * suivante.
	 *
	 * @exception	IllegalArgumentException si le nombre d'individus copiés
	 *				est inférieur à 1
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#setElitisme
	 */
	public void setNbMeilleurs (int i)
		{
		if	(i < 1)
			throw(new IllegalArgumentException("ParametreAG, setNbMeilleurs (int i) : le nombre de meilleurs individus doit etre superieur a 0."));
		else
			this.nbMeilleurs = i;
		}


	/**
	 * Retourne le nombre de meilleurs individus copiés dans la génération
	 * suivante.
	 */
	public int getNbMeilleurs () { return this.nbMeilleurs; }


	/**
	 * Modifie la méthode de remplacement.
	 *
	 * @param	rempl	méthode de remplacement : <code>ALEATOIRE</code>,
	 *					<code>LES_PLUS_MAUVAIS</code>,
	 *					<code>TOURNOI_INVERSE</code>,
	 *					<code>LES_PLUS_VIEUX</code> ou <code>PERSO</code>
	 *
	 * @exception	IllegalArgumentException si la méthode de remplacement
	 *				est inconnue
	 *
	 * @see	javalain.algorithmegenetique.ParametreAG#SSGA
	 */
	public void setRemplacement (int rempl)
		{
		if	(rempl == ParametreAG.ALEATOIRE			||
			 rempl == ParametreAG.LES_PLUS_MAUVAIS	||
			 rempl == ParametreAG.TOURNOI_INVERSE	||
			 rempl == ParametreAG.LES_PLUS_VIEUX	||
			 rempl == ParametreAG.PERSO)
			this.remplacement = rempl;
		else
			throw(new IllegalArgumentException("ParametreAG, setRemplacement (int rempl) : méthode de remplacement incorrecte."));
		}


	/**
	 * Retourne la méthode de remplacement.
	 */
	public int getRemplacement ()
		{
		if	(this.algorithme.equals(SSGA))
			return this.remplacement;
		else
			return ParametreAG.ENFANT_PARENT;
		}


	/**
	 * Modifie le nombre d'individus supplémentaires pour le chevauchement.
	 *
	 * @exception	IllegalArgumentException si le nombre d'individus
	 *				supplémentaires est inférieur à 1
	 */
	public void setNbOverlap (int i)
		{
		if	(i < 1)
			throw(new IllegalArgumentException("ParametreAG, setNbOverlap (int i) : le nombre d'individus supplementaires doit etre superieur a 0."));
		else
			this.nbOverlap = i;
		}


	/**
	 * Retourne le nombre d'individus supplémentaires pour le chevauchement.
	 */
	public int getNbOverlap () { return this.nbOverlap; }


	/**
	 * Modifie la taille de la sous poppulation dans <code>NPGA</code>.
	 *
	 * @exception	IllegalArgumentException si la taille est <= 0
	 *
	 */
	public void setTdom (int i)
		{
		if	(i <= 0)
			throw(new IllegalArgumentException("ParametreAG, setTdom (int i) : la taille de la sous population de NPGA doit etre > 0."));
		else
			this.tdom = i;
		}


	/**
	 * Retourne la taille de la sous poppulation dans <code>NPGA</code>.
	 */
	public int getTdom () { return this.tdom; }


	/*---------------------*/
	/* Méthodes redéfinies */
	/*---------------------*/

	/**
	 * Affiche l'ensemble des paramètres.
	 */
	@Override
	public String toString ()
		{
		StringBuilder sb;

		sb = new StringBuilder();
		sb.append("-----------------------\n");
		sb.append("Nom de la population   : "); sb.append(this.nom);					sb.append("\n");
		sb.append("Algorithme             : "); sb.append(this.getAlgorithme());		sb.append("\n");
		sb.append("Taux de mutation       : "); sb.append(this.getTauxMutation());		sb.append("\n");
		sb.append("Taux de croisement     : "); sb.append(this.getTauxCroisement());	sb.append("\n");
		sb.append("Sélection              : ");
		switch(this.getSelection())
			{
			case RANG				: sb.append("RANG");				break;
			case ROULETTE_PIPEE		: sb.append("ROULETTE_PIPEE");		break;
			case RESTE_STOCHASTIQUE	: sb.append("RESTE_STOCHASTIQUE");	break;
			case TOURNOI			: sb.append("TOURNOI");				break;
			default					: sb.append("ERREUR");
			}
		sb.append("\n");
		sb.append("Nb de participant      : "); sb.append(this.getNbPartTournoi());								sb.append("\n");
		sb.append("Scaling                : "); sb.append((this.getScaling() ? "TRUE" : "FALSE"));				sb.append("\n");
		sb.append("Sharing                : "); sb.append((this.getSharing() ? "TRUE" : "FALSE"));				sb.append("\n");
		sb.append("Alpha                  : "); sb.append(this.getAlpha());										sb.append("\n");
		sb.append("SigmaShare             : "); sb.append(this.getSigmaShare());								sb.append("\n");
		sb.append("Crowding               : "); sb.append((this.getCrowding() ? "TRUE" : "FALSE"));				sb.append("\n");
		sb.append("Croisement restreint   : "); sb.append((this.getCroisementRestreint() ? "TRUE" : "FALSE"));	sb.append("\n");
		sb.append("Elitisme               : "); sb.append((this.getElitisme() ? "TRUE" : "FALSE"));				sb.append("\n");
		sb.append("Nb de meilleurs copiés : "); sb.append(this.getNbMeilleurs());								sb.append("\n");
		sb.append("Remplacement           : ");
		switch(this.getRemplacement())
			{
			case ENFANT_PARENT		: sb.append("ENFANT_PARENT");		break;
			case LES_PLUS_MAUVAIS	: sb.append("LES_PLUS_MAUVAIS");	break;
			case ALEATOIRE			: sb.append("ALEATOIRE");			break;
			case TOURNOI_INVERSE	: sb.append("TOURNOI_INVERSE");		break;
			case LES_PLUS_VIEUX		: sb.append("LES_PLUS_VIEUX");		break;
			case PERSO				: sb.append("PERSO");				break;
			default					: sb.append("ERREUR");
			}
		sb.append("\n");
		sb.append("Nb d'individus en supp : "); sb.append(this.getNbOverlap());	sb.append("\n");
		sb.append("Tdom                   : "); sb.append(this.getTdom());		sb.append("\n");
		sb.append("-----------------------\n");

		return sb.toString();
		}

} /*----- Fin de la classe ParametreAG -----*/