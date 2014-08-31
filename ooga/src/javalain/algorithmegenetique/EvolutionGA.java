package javalain.algorithmegenetique;

import javalain.algorithmegenetique.ihm.ControleAlgoG;


/*-------------*/
/* EvolutionGA */
/*-------------*/
/**
 * Cette classe permet de gérer le processus d'évolution d'un algorithme
 * génétique.
 *
 * @author	<a href="mailto:alain.berro@univ-tlse1.fr">Alain Berro</a>
 * @version	4 janvier 2004
 */
public abstract class EvolutionGA implements Runnable
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/*----- Les différents états de l'évolution -----*/

	/**
     * <code>Evolution</code> prête à fonctionner.
	 */
	public final static int EXECUTABLE = 0;


	/**
     * <code>Evolution</code> en cours d'exécution.
	 */
	public final static int MARCHE = 1;


	/**
     * <code>Evolution</code> en pause.
	 */
	public final static int PAUSE = 2;


	/**
     * Lance l'exécution de l'<code>Evolution</code>.
	 */
	public final static int DEMARRE = 5;


	/**
     * Termine l'<code>Evolution</code>.
	 */
	public final static int TERMINE = 6;


	/*---------*/
	/* Données */
	/*---------*/

	/**
     * <code>Population</code> génétique.
	 */
	protected Population pop;


	/**
     * Données statistiques sur l'<code>Evolution</code>.
	 */
	protected int	nbGeneration;

	protected long	tpsSel;		// Temps total de sélection
	protected long	tpsCrois;	// Temps total de croisement
	protected long	tpsMut;		// Temps total de mutation
	protected long	tpsFit;		// Temps total de fitness
	protected long	tpsTotal;	// Temps total d'éxécution


	/**
     * Etat de la tâche.
	 */
	private boolean vivant;


	/**
     * Etat de l'évolution.
	 */
	private int etat;


	/**
     * Panneau de contrôle (éventuel) attaché à l'évolution.
	 */
	protected ControleAlgoG controleur;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
     * Construit une <code>Evolution</code>.
	 * <p>
	 * <b>Paramètre par défaut :</b>
	 * <ul>
	 *	<li>Etat de l'<code>Evolution</code> : <code>EXECUTABLE</code>.
	 * </ul>
	 *
	 * @param	p	<code>Population</code> attachée à l'<code>Evolution</code>
	 *
	 * @exception	RuntimeException si la référence sur la
	 *				<code>Population</code> est nulle
	 */
	@SuppressWarnings("CallToThreadStartDuringObjectConstruction")
	public EvolutionGA (Population p)
		{
		Thread th	= new Thread(this);
		this.vivant	= true;
		this.etat	= EvolutionGA.EXECUTABLE;

		if	(p == null)
			throw(new RuntimeException("Evolution, Evolution (Population p) : la référence sur la population est nulle."));

		this.pop = p;
		th.setPriority(Thread.MIN_PRIORITY) ;
		th.start();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne la référence sur la <code>Population</code> de travail.
	 */
	public Population getPopulation () { return pop; }


	/**
	 * Attache un panneau de contrôle à l'<code>Evolution</code>.
	 */
	public void setControleAlgoG (ControleAlgoG c)
		{
		this.controleur = c;
		this.controleur.setEvolution(this);
		}


	/**
	 * Retourne le contrôle attaché à l'<code>Evolution</code>.
	 */
	public ControleAlgoG getControleAlgoG () { return this.controleur; }


	/**
	 * Modifie l'état de l'<code>Evolution</code>.
	 * <p>
	 * Cette méthode permet de gérer le processus (thread) d'exécution de
	 * l'<code>Evolution</code>. Le cycle d'exécution est le suivant :
	 * <ol>
	 * 	<li><code>EXECUTABLE</code> : le thread est vivant,
	 *		l'<code>Evolution</code> est prête à démarrer (2).
	 *	<li><code>DEMARRE</code> : lance l'<code>Evolution</code>, la
	 *		<code>Population</code> est initialisée puis on passe
	 *		automatiquement dans l'état <code>MARCHE</code> (3).
	 *	<li><code>MARCHE</code> : l'<code>Evolution</code> s'exécute, la
	 *		<code>conditionEvolution()</code> de <code>Population</code> est
	 *		testée si elle retourne true alors on passe à la génération
	 *		suivante sinon on passe dans l'état <code>TERMINE</code> (5).
	 *	<li><code>PAUSE</code> : l'<code>Evolution</code> est suspendue, pour la
	 *		relancer il faut passer dans l'état <code>MARCHE</code> (3).
	 *	<li><code>TERMINE</code> : arrête l'<code>Evolution</code> puis on
	 *		passe automatiquement dans l'état <code>EXECUTABLE</code> (1).
	 * </ol>
	 *
	 * @param	e	état de l'<code>Evolution</code> : <code>EXECUTABLE</code>,
	 *				<code>DEMARRE</code>, <code>MARCHE</code>,
	 *				<code>PAUSE</code> ou <code>TERMINE</code>.
	 *
	 * @exception	IllegalArgumentException si l'état est inconnu
	 */
	synchronized public void setEtat (int e)
		{
		if	(e == EvolutionGA.EXECUTABLE	||
			 e == EvolutionGA.DEMARRE		||
			 e == EvolutionGA.MARCHE		||
			 e == EvolutionGA.PAUSE			||
			 e == EvolutionGA.TERMINE)
			this.etat = e;
		else
			throw(new IllegalArgumentException("Evolution, setEtat (int e) : état inconnu."));

		if	(this.controleur != null) this.controleur.updateEtatEvolution();
		}


	/**
	 * Retourne l'état de l'<code>Evolution</code>.
	 */
	synchronized public int getEtat () { return this.etat; }


	/**
	 * Execution de la tâche.
	 */
	@SuppressWarnings("SleepWhileHoldingLock")
	public void run ()
		{
		long debut = 0;
		long fin;

		while (this.vivant)
			{
			switch(this.etat)
				{
				case EvolutionGA.MARCHE :
					{
					if	(this.conditionEvolution())
						{
						this.pop.generation();
						this.nbGeneration++;

						/*----- Temps de calcul -----*/
						fin = System.currentTimeMillis();
						this.tpsTotal += fin-debut;
						debut = fin;

						this.tpsSel		+= this.pop.getDureeSelection();
						this.tpsCrois	+= this.pop.getDureeCroisement();
						this.tpsMut		+= this.pop.getDureeMutation();
						this.tpsFit		+= this.pop.getDureeFitness();

						/*----- Mise à jour des statistiques sur l'ihm -----*/
						if	(this.controleur != null)
							{
							this.controleur.setNbIndividu(this.pop.size());
							this.controleur.setNbGeneration(this.nbGeneration);
							this.controleur.setNoteMax(this.pop.getNoteMax());
							this.controleur.setNoteMoyenne(this.pop.getNoteMoyenne());
							this.controleur.setStatCpu(this.tpsSel,this.tpsCrois,this.tpsMut,this.tpsFit,this.tpsTotal);
							}
						}
					else
						this.etat = EvolutionGA.TERMINE;
					} break;

				case EvolutionGA.DEMARRE :
					{
					debut = System.currentTimeMillis();

					this.pop.initialise();

					this.nbGeneration	= 0;
					this.tpsSel			= 0;
					this.tpsCrois		= 0;
					this.tpsMut			= 0;
					this.tpsFit			= 0;
					this.tpsTotal		= 0;

					if	(this.controleur != null) this.controleur.demarre();

					this.demarre();

					this.setEtat(EvolutionGA.MARCHE);
					} break;

				case EvolutionGA.TERMINE :
					{
					if	(this.controleur != null) this.controleur.termine();

					this.setEtat(EvolutionGA.EXECUTABLE);
					this.termine();
					} break;

				default : //----- PAUSE et EXECUTABLE
					{
					try	{
						Thread.sleep(10);
						} catch (InterruptedException e) {}

					debut = System.currentTimeMillis();
					}
				}
			}

		if	(this.controleur != null) this.controleur.stop();
		}


	/**
	 * Arrête le thread d'exécution.
	 */
	public void stopEvolution () { this.vivant = false; }


	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Méthode qui s'exécute lors du lancement d'une <code>Evolution</code>
	 * juste après l'initialisation de la <code>Population</code>.
	 */
	public abstract void demarre();


	/**
	 * Méthode qui s'exécute lorsqu'une <code>Evolution</code> se termine.
	 * <p>
	 * Une évolution se termine :
	 * <ul>
	 *	<li>soit par une interruption de l'utilisateur,
	 *	<li>soit parce que la méthode <code>conditionEvolution()</code>
	 *		a renvoyé <code>false</code>.
	 * </ul>
	 */
	public abstract void termine ();


	/**
	 * Condition d'arrêt de l'<code>Evolution</code>.
	 * <p>
	 * On peut utiliser la définition de cette fonction pour :
	 * <ul>
	 *	<li>afficher des informations sur l'état de la population,
	 *	<li>afficher le nombre de génération,
	 *	<li>...
	 * </ul>
	 *
	 * @return	<code>false</code> si la condition d'arrêt de l'évolution est
	 *			atteinte <code>true</code> sinon.
	 */
	public abstract boolean conditionEvolution ();

} /*----- Fin de la classe Evolution -----*/
