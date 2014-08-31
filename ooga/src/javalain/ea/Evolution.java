package javalain.ea;


/*------------------*/
/* Classe Evolution */
/*------------------*/
/**
 * Cette classe permet de gérer le processus d'évolution
 * d'un algorithme évolutionnaire.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	25 mai 2009
 */
public abstract class Evolution implements Runnable
{
	/*----------------------*/
	/* Constantes de classe */
	/*----------------------*/

	/**
     * L'évolution est prête à fonctionner.
	 */
	public final static int EXECUTABLE = 0;


	/**
     * L'évolution démarre.
	 */
	public final static int DEMARRE = 1;


	/**
     * L'évolution est en cours d'exécution.
	 */
	public final static int CONTINUE = 2;


	/**
     * L'évolution est en pause.
	 */
	public final static int PAUSE = 3;


	/**
     * L'évolution se termine.
	 */
	public final static int TERMINE = 4;


	/*---------*/
	/* Données */
	/*---------*/

	/**
     * Etat de la tâche.
	 */
	private boolean vivant;


	/**
     * Etats de l'évolution.
	 */
	private int etatCrt;

	private int etatPrec;


	/**
	 * Enregistre qui a demandé la fin de l'évolution.
	 *  -1 : évolution non encore terminée
	 *	 0 : utilisateur
	 *	 1 : le programme
	 */
	private int quiStop;


	/**
	 * Temps d'exécution de l'évolution en cours.
	 */
	private long tpsTotal;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
     * Construit une <code>Evolution</code>.
	 * <p>
	 * <b>Paramètre par défaut :</b>
	 *	<ul>
	 *		<li>Etat de l'<code>Evolution</code> : <code>EXECUTABLE</code>.</li>
	 *	</ul>
	 */
	@SuppressWarnings("CallToThreadStartDuringObjectConstruction")
	public Evolution ()
		{
		Thread th		= new Thread(this);
		this.vivant		= true;
		this.etatCrt	= Evolution.EXECUTABLE;
		this.etatPrec	= -1;

		th.setPriority(Thread.MIN_PRIORITY) ;
		th.start();
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Modifie l'état de l'<code>Evolution</code>.
	 * <p>
	 * Cette méthode permet de gérer le processus (thread) d'exécution de
	 * l'<code>Evolution</code>. Le cycle d'exécution est le suivant :
	 *	<ol>
	 *		<li><code>EXECUTABLE</code> : le thread est vivant,
	 * 			l'<code>Evolution</code> est prête à démarrer (2).</li>
	 *		<li><code>DEMARRE</code> : lance l'<code>Evolution</code>,
	 *			puis on passe dans l'état <code>MARCHE</code> (3).</li>
	 *		<li><code>CONTINUE</code> : l'<code>Evolution</code> s'exécute, la
	 *			méthode <code>conditionEvolution()</code> est testée,
	 *			si elle retourne <code>true</code> alors on passe à
	 *			la génération suivante sinon on passe dans
	 *			l'état <code>TERMINE</code> (5).</li>
	 *		<li><code>PAUSE</code> : l'<code>Evolution</code> est suspendue,
	 *			pour la relancer il faut passer dans l'état
	 *			<code>CONTINUE</code> (3).</li>
	 *		<li><code>TERMINE</code> : arrête l'<code>Evolution</code> puis on
	 *			passe automatiquement dans l'état <code>EXECUTABLE</code> sauf
	 *			si l'utilisateur en décide autrement (1).</li>
	 *	</ol>
	 *
	 * @param	e	état de l'<code>Evolution</code> : <code>EXECUTABLE</code>,
	 *				<code>DEMARRE</code>, <code>CONTINUE</code>,
	 *				<code>PAUSE</code> ou <code>TERMINE</code>.
	 */
	synchronized public void setEtat (int e)
		{
		if	(e == Evolution.EXECUTABLE	||
			 e == Evolution.DEMARRE		||
			 e == Evolution.CONTINUE	||
			 e == Evolution.PAUSE		||
			 e == Evolution.TERMINE)
			{
			this.etatPrec = this.etatCrt;
			this.etatCrt = e;

			if (e == Evolution.TERMINE)
				this.quiStop = 0;
			}
		}


	/**
	 * Retourne l'état de l'<code>Evolution</code>.
	 */
	synchronized public int getEtat () { return this.etatCrt; }


	/**
	 * Retourne l'état précédent de l'<code>Evolution</code>.
	 */
	synchronized public int getEtatPrecedent () { return this.etatPrec; }


	/**
	 * Vérifie qui est l'auteur de la demande d'arrêt de l'évolution.
	 */
	synchronized public boolean isStopUser () { return (this.quiStop == 0); }

	synchronized public boolean isStopProg () { return (this.quiStop == 1); }


	/**
	 * Retourne le temps d'exécution de l'évolution.
	 */
	public long getElapsedTime () { return this.tpsTotal; }


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
			switch(this.etatCrt)
				{
				case Evolution.DEMARRE :
					{
					debut = System.currentTimeMillis();

					this.demarre();

					/*----- Initialisation du temps d'exécution de l'évolution en cours -----*/
					this.tpsTotal = 0;

					/*----- Etats de l'évolution -----*/
					this.etatPrec = Evolution.DEMARRE;
					this.etatCrt = Evolution.CONTINUE;
					this.quiStop = -1;
					} break;

				case Evolution.CONTINUE:
					{
					if	(this.conditionEvolution())
						{
						this.itere();

						/*----- Calcul du temps d'exécution de l'évolution en cours -----*/
						fin = System.currentTimeMillis();
						this.tpsTotal += fin-debut;
						debut = fin;
						}
					else
						{
						/*----- Etats de l'évolution -----*/
						this.etatPrec = Evolution.CONTINUE;
						this.etatCrt = Evolution.TERMINE;
						this.quiStop = 1;
						}
					} break;

				case Evolution.TERMINE :
					{
					this.termine();

					/*----- Etats de l'évolution -----*/
					if (this.etatCrt == Evolution.TERMINE)
						{
						this.etatPrec = Evolution.TERMINE;
						this.etatCrt = Evolution.EXECUTABLE;
						}
					} break;

				default :
					{
					/*----- PAUSE et EXECUTABLE -----*/
					try	{
						/*----- Pause de 10 millisecondes -----*/
						Thread.sleep(10);
						} catch (InterruptedException e) {}

					debut = System.currentTimeMillis();
					}
				}
			}
		}


	/**
	 * Arrête le thread d'exécution.
	 */
	public void stopEvolution () { this.vivant = false; }


	/**
	 * Attend que l'exécution se 'termine' et passe en état 'exécutable'.
	 */
	@SuppressWarnings("SleepWhileHoldingLock")
	public static void attendre (Evolution e) throws InterruptedException
		{
		while (e.getEtat() != Evolution.EXECUTABLE) Thread.sleep(10);
		}


	/*---------------------*/
	/* Méthodes abstraites */
	/*---------------------*/

	/**
	 * Méthode qui s'exécute lors du lancement de l'<code>Evolution</code>.
	 * <p>
	 * On peut utiliser la définition de cette fonction pour :
	 * 	<ul>
	 *		<li>initiliser les structures de données  (population, ...),</li>
	 *		<li>afficher des informations sur l'état initial de l'exécution,</li>
	 *		<li>...</li>
	 *	</ul>
	 * </p>
	 */
	public abstract void demarre ();


	/**
	 * Méthode qui permet de passer à l'étape suivante du processus
	 * d'<code>Evolution</code>. Elle s'exécute si la méthode
	 * <code>conditionEvolution()</code> a renvoyé <code>true</code>.
	 * <p>
	 * L'utilisateur doit reféfinir cette fonction afin de décrire la manière
	 * de passer à la génération suivante.
	 * </p>
	 */
	public abstract void itere ();


	/**
	 * Méthode qui s'exécute lorsque l'<code>Evolution</code> se termine.
	 * <p>
	 * Une évolution se termine :
	 *	<ul>
	 *		<li>soit par une interruption de l'utilisateur,</li>
	 *		<li>soit parce que la méthode <code>conditionEvolution()</code>
	 * 			a renvoyé <code>false</code>.</li>
	 *	</ul>
	 * </p>
	 */
	public abstract void termine ();


	/**
	 * Condition d'arrêt de l'<code>Evolution</code>.
	 * <p>
	 * On peut utiliser la définition de cette fonction pour :
	 *	<ul>
	 *		<li>afficher des informations sur l'état de l'<code>Evolution</code>,</li>
	 *		<li>afficher le nombre de génération,</li>
	 *		<li>...</li>
	 *	</ul>
	 * </p>
	 *
	 * @return	<code>false</code> si la condition d'arrêt de l'évolution est
	 *			atteinte <code>true</code> sinon.
	 */
	public abstract boolean conditionEvolution ();

} /*----- Fin de la classe Evolution -----*/
