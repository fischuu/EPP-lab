package math;

import javalain.math.Calcul;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import util.MsgUtils;


/*---------*/
/* Matrice */
/*---------*/
/**
 * Classe Matrice
 * 
 * @author	Alain Berro
 * @version	25 mai 2010
 */
public class Matrice
{
	/*-----------*/
	/* Constante */
	/*-----------*/

	private static double PRECISION = 1e-12;


	/*---------*/
	/* Données */
	/*---------*/

	/*----- Matrice -----*/
	private double[][] element;

	/*----- Nombre de lignes -----*/
	private int nb_lig;

	/*----- Nombre de colonnes -----*/
	private int nb_col;

	/*----- Nom éventuel de la matrice -----*/
	private String nom;


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise la matrice à partir d'un fichier texte.
	 */
	public Matrice (String nom_fichier)
		{
		this(nom_fichier, 0);
		}

	public Matrice (String nom_fichier, int numero_du_jeu_de_donnees)
		{
		/*----- Lecture du fichier 'nom_fichier' -----*/
		FileReader fichier;
		BufferedReader buffer;
		try	{
			/*----- Fichier -----*/
			fichier = new FileReader(nom_fichier);

			/*----- Buffer -----*/
			buffer = new BufferedReader(fichier);

			StringTokenizer st;
			/*----- Lecture de la première ligne -----*/
			st = new StringTokenizer(buffer.readLine());

			/*----- Lecture du nombre de lignes du fichier -----*/
			int lig = Integer.parseInt(st.nextToken()); // A faire - Si le nb de lignes et/ou de colonnes sont oubliés alors cette ligne provoque une NumberFormatException.

			/*----- Lecture du nombre de colonnes -----*/
			this.nb_col = Integer.parseInt(st.nextToken());

			/*----- Lecture éventuelle du nombre de matrices (jeux de données) contenues dans le fichier -----*/
			if (st.hasMoreTokens())
				this.nb_lig = lig / Integer.parseInt(st.nextToken());
			else
				this.nb_lig = lig;

			/*----- Initialisation de la matrice -----*/
			this.element = new double[this.nb_lig][this.nb_col];

			/*----- Lecture de la deuxième ligne -----*/
			buffer.readLine();

			/*----- Décalage éventuel s'il y a plusieurs matrices (jeux de données) dans le fichier -----*/
			int ligne = numero_du_jeu_de_donnees * this.nb_lig;
			while (ligne > 0)
				{
				buffer.readLine();
				ligne--;
				}
	
			/*----- Lecture de la matrice -----*/ // A faire - Lecture de fichiers 'csv'.
			ligne = 0;
			int colonne;
			while (ligne < this.nb_lig)
				{
				st = new StringTokenizer(buffer.readLine());

				colonne = 0;
				while (st.hasMoreTokens())
					this.element[ligne][colonne++] = Double.parseDouble(st.nextToken());

				ligne++;
				}

			buffer.close();
			fichier.close();

			/*----- Nom de la matrice -----*/
			this.nom = nom_fichier;
			}
		catch (FileNotFoundException fnfe)
			{
			MsgUtils.erreurAndExit(null,
								   "Fichier introuvable '" + nom_fichier + "'",
								   "Chargement de la matrice");
			}
		catch (IOException e)
			{
			MsgUtils.erreurAndExit(null,
								   "Problème lors de la lecture du fichier '" + nom_fichier + "'.",
								   "Chargement de la matrice");
			}
		}


	/**
	 * Construit et initialise une matrice.
	 */
	public Matrice (int n , int p)
		{
		/*----- Dimension de la matrice -----*/
		this.nb_lig = n;
		this.nb_col = p;

		/*----- Initialisation de la matrice -----*/
		this.element = new double[n][p];

		/*----- Nom de la matrice -----*/
		this.nom = "Matrice";
		}


	/**
	 * Construit une matrice carrée.
	 */
	public Matrice (int n)
		{
		this(n,n);
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne les dimensions de la matrice.
	 */
	public int nbLigne () { return this.nb_lig; }

	public int nbColonne () { return this.nb_col; }


	/**
	 * Retourne la valeur de l'élément en ligne 'i' et colonne 'j'.
	 */
	public double element (int i, int j) { return this.element[i][j]; }


	/**
	 * Affecte la valeur de l'élement (i, j).
	 */
	public void setElement (int i, int j, double d)
		{
		this.element[i][j] = d;
		}


	/**
	 * Retourne la ligne 'i'.
	 */
	public double[] ligne (int i)
		{
		double[] lig = new double[this.nb_col];
		System.arraycopy(this.element[i], 0, lig, 0, this.nb_col);

		return lig;
		}


	/**
	 * Retourne la colonne 'j'.
	 */
	public double[] colonne (int j)
		{
		double[] col = new double[this.nb_lig];

		for (int i=0; i<this.nb_lig; i++)
			col[i] = this.element[i][j];

		return col;
		}


	/**
	 * Inverse les cononnes 'i' et 'j'.
	 */
	public void swapColonne (int i, int j) { swap(this, i, j); }

	private static void swap (Matrice m, int i, int j)
		{
		double d;
		for (int k=0; k<m.nb_lig; k++)
			{
			d = m.element[k][i];
			m.element[k][i] = m.element[k][j];
			m.element[k][j] = d;
			}
		}


	/**
	 * Affecte un nom à la matrice.
	 */
	public void setNom (String s) { this.nom = s; }


	/**
	 * Retourne le nom de la matrice.
	 */
	public String getNom () { return this.nom; }


	/**
	 * Initialise la matrice à la matrice identité.
	 */
	public void setIdentite ()
		{
		/*----- La matrice est-elle carrée ? -----*/
		if (this.nb_lig != this.nb_col)
			{
			MsgUtils.erreurAndExit(null,
								   "Initialisation impossible car la matrice n'est pas carrée.",
								   "Matrice identité");
			}

		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				if (i == j)
					this.element[i][j] = 1.0;
				else
					this.element[i][j] = 0.0;
		}


	/**
	 * Retoutne si la matrice est symétrique.
	 */
	public boolean isSymetric ()
		{
		for (int i=0; i<this.nb_lig-1; i++)
			for (int j=i+1; j<nb_lig; j++)
				if (this.element[i][j] != this.element[j][i])
					return false;

		return true;
		}


	/**
	 * Sauvegarde de la matrice dans le fichier 'nom'.
	 */
	public void sauvegardeDansFichier (String nom)
		{
		/*----- Chaîne de caractères -----*/
		StringBuilder sb = new StringBuilder();
		sb.append(this.nb_lig).append(" ").append(this.nb_col).append("\n\n");
		for (int i=0; i<this.nb_lig; i++)
			{
			for (int j=0; j<this.nb_col-1; j++)
				sb.append(this.element[i][j]).append(" ");

			sb.append(this.element[i][this.nb_col-1]).append("\n");
			}

		/*----- Fichier d'écriture -----*/
		PrintWriter out = null;
		try {
			out = new PrintWriter(nom, "ISO-8859-1");
			out.print(sb.toString());
			}
		catch (IOException e) {}

		if (out != null) out.close();
		}


	/**
	 * Affichage de la matrice.
	 */
	@Override
	public String toString()
		{
		/*----- Format d'affichage d'un nombre -----*/
		DecimalFormat df = new DecimalFormat("0.000000000000");

		/*----- Recherche du nombre le plus grand en valeur absolu -----*/
		double max = this.element[0][0];

		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				if (Math.abs(this.element[i][j]) > max)
					max = Math.abs(this.element[i][j]);

		/*----- Calcul du nombre de chiffres composant la partie entière de 'max' -----*/
		int nb_chiffre_max = 1;
		while (max >= 10)
			{
			max /= 10;
			nb_chiffre_max++;
			}

		/**
		 * Forme la chaîne de caractères.
		 */
		StringBuilder sb = new StringBuilder("----- ").append(this.nom).append(" (").append(this.nb_lig).append(",").append(this.nb_col).append(")\n");

		double nb;
		double abs;
		int nb_chiffre;
		int k;
		for (int i=0; i<this.nb_lig; i++)
			{
			for (int j=0; j<this.nb_col; j++)
				{
				nb = this.element[i][j];

				/*----- Nombre de chiffres composant la partie entière du nombre -----*/
				nb_chiffre = 1;
				abs = Math.abs(nb);
				while (abs >= 10)
					{
					abs /= 10;
					nb_chiffre++;
					}

				/*----- Signe -----*/
				if (nb >= 0) sb.append(" ");

				/*----- Espace -----*/
				k = 0;
				while (k < (nb_chiffre_max - nb_chiffre))
					{
					sb.append(" ");
					k++;
					}

				sb.append(df.format(nb)).append(" ");
				}

			sb.append("\n");
			}

		sb.append("-----");

		return sb.toString();
		}


	/*-----------------------------*/
	/* Opérations sur les matrices */
	/*-----------------------------*/

	/**
	 * Retourne la multiplication de la matrice 'this' par une autre matrice.
	 */
	public Matrice mul (Matrice b)
		{
		if (this.nb_col != b.nb_lig)
			{
			JOptionPane.showMessageDialog(null,
										  "Les dimensions des matrices ne sont pas compatibles.",
										  "Multiplication de matrices",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		/*----- Création de la nouvelle matrice -----*/
		Matrice mul = new Matrice(this.nb_lig, b.nb_col);

		/*----- Multiplication des éléments -----*/
		double somme;
		for (int i=0; i<this.nb_lig; i++)
			{
			for (int j=0; j<b.nb_col; j++)
				{
				somme = 0.0;

				for (int k=0; k<this.nb_col; k++)
					somme += this.element[i][k]*b.element[k][j];

				mul.element[i][j] = somme;
				}
			}

		return mul;
		}


	/**
	 * Retourne l'addition de la matrice 'this' à une autre matrice.
	 */
	public Matrice addition (Matrice b)
		{
		if ((this.nb_lig != b.nb_lig) && (this.nb_col != b.nb_col))
			{
			JOptionPane.showMessageDialog(null,
										  "Les dimensions des matrices ne sont pas compatibles.",
										  "Addition de matrices",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		/*----- Création de la nouvelle matrice -----*/
		Matrice add = new Matrice(this.nb_lig, this.nb_col);

		/*----- Addition des elements -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<b.nb_col; j++)
				add.element[i][j]= this.element[i][j] + b.element[i][j];

		return add;
		}


	/**
	 * Retourne la soustraction de la matrice 'this' à une autre matrice.
	 */
	public Matrice soustraction (Matrice b)
		{
		if ((this.nb_lig != b.nb_lig) && (this.nb_col != b.nb_col))
			{
			JOptionPane.showMessageDialog(null,
										  "Les dimensions des matrices ne sont pas compatibles.",
										  "Soustraction de matrices",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		/*----- Création de la nouvelle matrice -----*/
		Matrice add = new Matrice(this.nb_lig, this.nb_col);

		/*----- Addition des elements -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<b.nb_col; j++)
				add.element[i][j]= this.element[i][j] - b.element[i][j];

		return add;
		}


	/**
	 * Retourne la puissance 'nb' de la matrice 'this'.
	 */
	@SuppressWarnings("ManualArrayToCollectionCopy")
	public Matrice pow (double nb)
		{
		/*----- La matrice est-elle carrée ? -----*/
		if (this.nb_col != this.nb_lig)
			{
			JOptionPane.showMessageDialog(null,
										  "La matrice n'est pas carrée.",
										  "Puissance de la matrice",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		/*----- Calcul des vecteurs et valeurs propres par la méthode de Jacobi -----*/
		Matrice  m_vecteurs_propres = new Matrice(this.nb_lig);
		double[] valeurs_propres = new double[this.nb_lig];

		Matrice.jacobi(this, valeurs_propres, m_vecteurs_propres);

		/*----- Création de la matrice diagonale de valeurs propres -----*/
		Matrice m_valeurs_propres = new Matrice(this.nb_lig);
		m_valeurs_propres.setIdentite();

		for (int i=0; i<this.nb_lig; i++)
			m_valeurs_propres.element[i][i] = valeurs_propres[i];

		/*----- Elévation à la puissance de la diagonale de la matrice des valeurs propres -----*/
		for (int i=0; i<m_valeurs_propres.nb_lig; i++)
			{
			if (-Matrice.PRECISION < m_valeurs_propres.element[i][i] && m_valeurs_propres.element[i][i] < Matrice.PRECISION)
				m_valeurs_propres.element[i][i] = 0.0;
			else
				m_valeurs_propres.element[i][i] = Math.pow(m_valeurs_propres.element[i][i], nb);
			}
		/*----- Calcul de la puissance de la matrice -----*/
		return m_vecteurs_propres.mul(m_valeurs_propres).mul(m_vecteurs_propres.transpose());
		}


	/**
	 * Retourne la transposée de la matrice 'this'.
	 */
	public Matrice transpose ()
		{
		Matrice m = new Matrice(this.nb_col, this.nb_lig);

		/*----- Affectation des éléments -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				m.element[j][i] = this.element[i][j];

		return m;
		}


	/**
	 * Retourne la multiplication de la matrice par un scalaire.
	 */
	public Matrice mulScalaire (double d)
		{
		Matrice m = new Matrice(this.nb_lig, this.nb_col);

		/*----- Multiplication des éléments -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				m.element[i][j] = this.element[i][j]*d;

		return m;
		}


	/**
	 * Retourne la matrice centrée de la matrice 'this'.
	 */
	public Matrice centre ()
		{
		double[] moyenne_en_colonne = new double[this.nb_col];

		/*----- Calcul des moyennes en colonne -----*/
		double somme;
		for (int j=0; j<this.nb_col; j++)
			{
			somme = 0.0;

			for (int i=0; i<this.nb_lig; i++)
				somme += this.element[i][j];

			moyenne_en_colonne[j] = somme/this.nb_lig;
			}

		/*----- Matrice centrée -----*/
		Matrice m = new Matrice(this.nb_lig, this.nb_col);

		/*----- Calcul des valeurs de la matrice centrée -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				m.element[i][j] = this.element[i][j] - moyenne_en_colonne[j];

		return m;
		}


	/**
	 * Retourne la matrice centrée réduite de la matrice 'this'.
	 */
	public Matrice centreReduite ()
		{
		double[] moyenne_en_colonne = new double[this.nb_col];
		double[] ecart_type_en_colonne = new double[this.nb_col];

		/*----- Calcul des moyennes et écarts-type en colonne -----*/
		double[] col;
		for (int i=0; i<this.nb_col; i++)
			{
			col = this.colonne(i);
			moyenne_en_colonne[i] = Calcul.moyenne(col);
			ecart_type_en_colonne[i] = Calcul.ecartType(col);
			}

		/*----- Matrice centrée réduite -----*/
		Matrice m = new Matrice(this.nb_lig, this.nb_col);

		/*----- Calcul des valeurs de la matrice centrée réduite -----*/
		for (int i=0; i<this.nb_lig; i++)
			for (int j=0; j<this.nb_col; j++)
				m.element[i][j] = (this.element[i][j] - moyenne_en_colonne[j])/ecart_type_en_colonne[j];

		return m;
		}


	/**
	 * Retourne la matrice de variance-covariance de la matrice 'this'.
	 *
	 * Nous considérons que les observations sont en ligne et
	 * les paramètres en colonne.
	 */
	public Matrice varianceCovariance ()
		{
		Matrice mat_centree = this.centre();

		return mat_centree.transpose().mul(mat_centree).mulScalaire(1.0/(this.nb_lig-1));
		}


	/**
	 * Retourne la matrice avec les données sphériques.
	 */
	public Matrice spherique ()
		{
		/*----- Calcul de la matrice de variance-covariance -----*/
		Matrice mat_cov = this.varianceCovariance();

		/*----- Calcul des données sphériques -----*/
		return this.centre().mul(mat_cov.pow(-0.5));
		}


	/*--------------------*/
	/* Méthodes statiques */
	/*--------------------*/

	/**
	 * Retourne une matrice colonne.
	 */
	public static Matrice createMatriceColonne (final double[] a)
		{
		Matrice mat = new Matrice(a.length, 1);

		for (int i=0; i<a.length; i++)
			mat.element[i][0] = a[i];

		return mat;
		}


	/**
	 * Retourne une matrice ligne.
	 */
	public static Matrice createMatriceLigne (final double[] a)
		{
		Matrice mat = new Matrice(1, a.length);

		System.arraycopy(a, 0, mat.element[0], 0, a.length);

		return mat;
		}


	/**
	 * Calcule les valeurs propres et les vecteurs propres pour des matrices
	 * symétriques réelles par la méthode de Jacobi.
	 *
	 * Les vecteurs propres sont présentés en colonne dans la matrice 'E'.
	 * Les valeurs propres sont ordonnées de manière décroissante dans 'e'.
	 */
	public static void jacobi (final Matrice mat, double[] e, Matrice E)
		{
		/*----- La matrice est-elle carrée ? -----*/
		if (mat.nb_col != mat.nb_lig)
			{
			JOptionPane.showMessageDialog(null,
										  "La matrice n'est pas carrée.",
										  "Méthode de Jacobi",
										  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			}

		/*----- Dimension de la matrice -----*/
		int n = mat.nb_lig;

		/*----- Initialisation de S(n,n) -----*/
		double[][] S = new double[n][n];
		for (int i=0; i<n; i++)
			System.arraycopy(mat.element[i], 0, S[i], 0, n);

		/*----- Variables -----*/
		int			i, k, l, m;
		double		c, p, s, t, y;
		int			state;
		int[]		ind = new int[n];
		boolean[]	changed = new boolean[n];


		/**
		 * Initialisation.
		 */
		state = n;

		/*----- Matrice des vecteurs propres -----*/
		E.setNom("Vecteurs propres");
		E.setIdentite();

		/*----- Vecteur des valeurs propres -----*/
		for (k=0; k<n ; k++)
			{
			ind[k] = maxint(S, n, k);
			e[k] = S[k][k];
			changed[k] = true;
			}


		/**
		 * Rotation.
		 */
		while (state != 0)
			{
			/*----- Recherche l'indice (k,l) pivot -----*/
			m = 0;
			for (k=1; k<n-1; k++)
				if (Math.abs(S[k][ind[k]]) > Math.abs(S[m][ind[m]]))
					m = k;

			k = m;
			l = ind[m];
			p = S[k][l];

			/*----- Calcul c = cos(psi), s = sin(psi) -----*/
			y = (e[l] - e[k]) / 2;
			t = Math.abs(y) + Math.sqrt(p*p + y*y);
			s = Math.sqrt(p*p + t*t);
			c = t/s;
			s = p/s;
			t = p*p/t;

			if (y<0)
				{
				s = -s;
				t = -t;
				}

			S[k][l] = 0.0;

			state += update(changed, e, k,-t);
			state += update(changed, e, l,t);

			/*----- Rotation des lignes et colonnes k et l de S -----*/
			for (i=0; i<k; i++)   rotate(S, i, k, i, l, c, s);
			for (i=k+1; i<l; i++) rotate(S, k, i, i, l, c, s);
			for (i=l+1; i<n; i++) rotate(S, k, i, l, i, c, s);

			/*----- Rotation des vecteurs propres -----*/
			for (i=0; i<n; i++) rotate(E.element, k, i, l, i, c, s);

			/*----- Les lignes k et l ont changé, mise à jour des indices -----*/
			ind[k] = maxint(S, n, k);
			ind[l] = maxint(S, n, l);
			}


		/**
		 * On transpose la matrice de manière à présenter
		 * les vecteurs propres en colonne.
		 */
		for (k=0; k<E.nb_lig; k++)
			for (l=k+1; l<E.nb_lig; l++)
				{
				c = E.element[k][l];
				E.element[k][l] = E.element[l][k];
				E.element[l][k] = c;
				}


		/**
		 * Classement par ordre décroissant des valeurs propres.
		 */
		double d;
		for (k=0; k<n; k++)
			{
			m = k;
			for (l=k+1; l<n; l++)
				{
				if (e[l] > e[m])
					m = l;
				}

			if (k != m)
				{
				d = e[m];
				e[m] = e[k];
				e[k] = d;

				swap(E, m, k);
				}
			}
		}


	/**
	 * Retourne l'indice de l'élément le plus grand de la ligne k.
	 */
	private static int maxint (final double[][] S, final int n, final int k)
		{
		int m = k+1;

		for (int i=k+2; i<n; i++)
			if (Math.abs(S[k][i]) > Math.abs(S[k][m]))
				m = i;

		return m;
		}


	/**
	 * Mise à jour de e[k] et de son statut.
	 */
	private static int update (boolean[] changed, double[] e, int k, double t)
		{
		double y = e[k];
		e[k] = y+t;

		if (changed[k] && y == e[k])
			{
			changed[k] = false;
			return -1;
			}
		else
		if (!changed[k] && y != e[k])
			{
			changed[k] = true;
			return 1;
			}

		return 0;
		}


	/**
	 * Rotation de deux éléments.
	 */
	private static void rotate (double[][] S, int k, int l, int i, int j, double c, double s)
		{
		double kl = S[k][l];
		double ij = S[i][j];

		S[k][l] = c*kl - s*ij;
		S[i][j] = s*kl + c*ij;
		}


	/*-----------------*/
	/* Autres méthodes */
	/*-----------------*/

	/**
	 * Supprime des lignes de la matrice. Les numéros des lignes à supprimer
	 * sont donnés en paramètres.
	 */
	public void supprLignes (int[] tab_num_lig)
		{
		/*----- Tri de la liste des numéros de ligne -----*/
		Arrays.sort(tab_num_lig);

		double[][] new_mat = new double[this.nb_lig - tab_num_lig.length][this.nb_col];

		int cpt = 0;
		for (int lig=0; lig<(this.nb_lig-tab_num_lig.length); lig++)
			{
			if (cpt < tab_num_lig.length && (lig == (tab_num_lig[cpt]-1-cpt))) cpt++;

			System.arraycopy(this.element[lig+cpt], 0, new_mat[lig], 0, this.nb_col);
			}

		this.nb_lig -= tab_num_lig.length;
		this.element = new_mat;
		}

} /*----- Fin de la classe Matrice -----*/







//	/*----- Inverse d'une matrice (Méthode de Jordan) : M1 <- inv(M) -----*/
//	public void inv ()
//		{
//		int i,j,k,l;
//		int n = this.getNumRow();
//		boolean test;
//		double max, coef, pivot;
//
//		Matrice t = new Matrice(n,2*n);
//
//		for	(i=0; i<n; i++)
//			for	(j=0; j<n; j++)
//				{
//				t.setElement(i,j,this.getElement(i,j));
//				if	(i==j) t.setElement(i,j+n,1.0);
//				else	   t.setElement(i,j+n,0.0);
//				}
//
//		test = true;
//		k = 0;
//
//		while (test && k<n)
//			{
//			//----- Recherche du nombre pivot (le plus grand(est ce utile ?))
//
//			max = Math.abs(t.getElement(k,k));
//			l=k;
//			for	(i=k+1; i<n; i++)
//				if	(max<Math.abs(t.getElement(i,k))) { max = Math.abs(t.getElement(i,k)); l=i; }
//
//			if	(max != 0.0)
//				{
//				//----- Echange des lignes k et l
//
//				for	(j=0; j<2*n; j++)
//					{
//					max = t.getElement(k,j);
//					t.setElement(k,j,t.getElement(l,j));
//					t.setElement(l,j,max);
//					}
//
//				//----- Division de la ligne k par le nombre pivot
//
//				pivot = t.getElement(k,k);
//				for	(j=k; j<2*n; j++) t.setElement(k,j,t.getElement(k,j) / pivot);
//
//				//----- Création de la colonne de zéros
//
//				for	(i=0; i<n; i++)
//					if	(i!=k)
//						{
//						coef = t.getElement(i,k);	// On pourrait tester si le coef = 0 (pour accélérer ?)
//						for	(j=k; j<2*n; j++) t.setElement(i,j,t.getElement(i,j) - coef*t.getElement(k,j));
//						}
//				}
//			else
//				{
//				System.out.println("Calcul de matrice inverse Cette matrice n'est pas inversible");
//				}
//			k++;
//			}
//
//		for	(i=0; i<n; i++)
//			for	(j=0; j<n; j++) this.setElement(i,j,t.getElement(i,j+n));
//		}
