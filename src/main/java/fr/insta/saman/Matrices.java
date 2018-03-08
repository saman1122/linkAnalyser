package fr.insta.saman;

public class Matrices {
    //calcul de dterminant
    public  double determinant(double matrice[][])
    {
        double determinant = 0;
        double matricenouvelle[][] = new double[matrice.length - 1][matrice.length - 1];

        for(int boucle = 1; boucle <= matrice.length - 1; boucle++){
            //calcul le cofacteur pour la premire coordonne de toute les lignes
            matricenouvelle = cofacteur(matrice,boucle,1);

            //calcul le dterminant de la nouvelle matrice cr, lorsque que la matrice atteint une dimension de 3X3
            //La mthode de sarrus est utilis autrement on continue avec la mthode des cofacteurs.
            if (matricenouvelle.length - 1 != 3)
                determinant += (Math.pow(-1, boucle+1) * matrice[boucle][1]) * determinant(matricenouvelle);
            else
                determinant += (Math.pow(-1, boucle+1) * matrice[boucle][1]) * sarrus(matricenouvelle);
        }
        return determinant;
    }

    //mthodes de sarrus, consiste a calculculer le dterminant d'une matrice 3X3
    private  double sarrus(double matrice[][])
    {
        double produit = 1;
        double determinantsarrus = 0;
        double matriceSarrus[][] = new double[4][6];

        for(int boucle = 1; boucle <= 3; boucle ++)
            for(int boucle1 = 1; boucle1 <= 3; boucle1 ++)
                matriceSarrus[boucle][boucle1] = matrice[boucle][boucle1];

        for (int boucle = 1; boucle <= 3; boucle++)
            for (int boucle1 = 4; boucle1 <= 5; boucle1++)
                matriceSarrus[boucle][boucle1] = matriceSarrus[boucle][boucle1 - 3];

        for (int boucle1 = 0; boucle1 <= 2; boucle1++){
            for (int boucle = 1; boucle <= 3; boucle ++)
                produit *= matriceSarrus[boucle][boucle + boucle1];
            determinantsarrus += produit;
            produit = 1;
        }


        for (int boucle1 = 0; boucle1 <= 2; boucle1++){
            for (int boucle = 5, boucle2 = 1; boucle >= 3; boucle --, boucle2 ++)
                produit *= matriceSarrus[boucle2][boucle - boucle1];
            determinantsarrus -= produit;
            produit = 1;
        }

        return determinantsarrus;
    }

    //multiplie deux matrices, en multipliant chaque ligne avec sa colonne correspondante.
    public  double[][] multiplication(double matriceA[][], double matriceB[][])
    {
        double matriceC[][] = new double [matriceA.length][matriceA.length];

        for (int boucle = 1; boucle <= matriceA.length - 1; boucle++)
            for (int boucle1 = 1; boucle1 <= matriceB[0].length - 1; boucle1++)
                for (int boucle2 = 1; boucle2 <= matriceB.length - 1; boucle2++)
                    matriceC[boucle][boucle1] += matriceA[boucle][boucle2] * matriceB[boucle2][boucle1];

        return matriceC;
    }

    //Somme de deux matrices, soustrait chaque coordonnes avec sa coordonne correspondante
    public  double[][] addition(double matriceA[][], double matriceB[][])
    {
        double matriceC[][] = new double [matriceA.length][matriceA.length];

        for(int boucle = 1; boucle <= matriceA.length - 1; boucle ++)
            for(int boucle1 = 1; boucle1 <= matriceA[0].length - 1; boucle1 ++)
                matriceC[boucle][boucle1] = matriceA[boucle][boucle1] + matriceB[boucle][boucle1];

        return matriceC;
    }

    //Diffrence de deux matrices
    public  double[][] soustraction(double matriceA[][], double matriceB[][])
    {
        double matriceC[][] = new double [matriceA.length][matriceA.length];

        for(int boucle = 1; boucle <= matriceA.length - 1 ; boucle ++)
            for(int boucle1 = 1; boucle1 <= matriceA[0].length - 1; boucle1 ++)
                matriceC[boucle][boucle1] = matriceA[boucle][boucle1] - matriceB[boucle][boucle1];

        return matriceC;
    }

    //Calcul l'inverse d'un matrice.
    public  double[][] inverse(double matrice[][])
    {
        double determinant;
        double inverse[][] = new double [matrice.length][matrice.length];

        determinant = determinant(matrice);

        //chaque coordonne de la matrice inverse est constitu du determinant du cofacteur de chaque coordonne de la matrice de dpart
        for (int boucle = 1; boucle <= matrice.length - 1; boucle++)
            for (int boucle1 = 1; boucle1 <= matrice.length - 1; boucle1++)
                inverse[boucle][boucle1] = determinant (cofacteur(matrice,boucle,boucle1));

        //on transpose la matrice.
        inverse = transpose(inverse);

        //et on divise chaque coordonne par le determinant de la matrice de dpart
        for (int boucle = 1; boucle <= matrice.length - 1; boucle++)
            for (int boucle1 = 1; boucle1 <= matrice.length - 1; boucle1++)
                inverse[boucle][boucle1] = inverse[boucle][boucle1]/determinant;

        return inverse;
    }

    //un cofacteur est un matrice a laquelle on a retir une ligne et une colonne. EX: le cofacteur d'une matrice en se basant sur sa coordonne
    //3,3 est cette meme matrice sans la ligne 3 et la colonne 3.
    private double[][] cofacteur(double matrice[][], int ligne, int colonne)
    {
        double cofacteur[][] = new double[matrice.length - 1][matrice.length - 1];
        int boucle1;
        int boucle;


        for(boucle = 1; boucle < ligne; boucle ++ ){
            for(boucle1 = 1; boucle1 < colonne; boucle1++)
                cofacteur[boucle][boucle1] = matrice[boucle][boucle1];

            for(boucle1 = boucle1; boucle1 <= cofacteur.length - 1; boucle1++)
                cofacteur[boucle][boucle1] = matrice[boucle][boucle1+1];
        }

        for(boucle = boucle; boucle <= cofacteur.length - 1; boucle ++ ){
            for(boucle1 = 1; boucle1 < colonne; boucle1++)
                cofacteur[boucle][boucle1] = matrice[boucle + 1][boucle1];

            for(boucle1 = boucle1; boucle1 <= cofacteur.length - 1; boucle1++)
                cofacteur[boucle][boucle1] = matrice[boucle + 1][boucle1+1];
        }

        return cofacteur;
    }

    //transposition de matrice, chaque colonne devient une ligne et vice-versa
    public double[][] transpose(double matrice[][])
    {
        double transpose[][] = new double[matrice[0].length][matrice.length];

        for(int boucle = 1; boucle <= transpose.length - 1; boucle++)
            for(int boucle1 = 1; boucle1 <= transpose.length - 1; boucle1++)
                transpose[boucle][boucle1] = matrice[boucle1][boucle];
        return transpose;

    }

}

