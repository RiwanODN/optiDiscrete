package src.Algorithmes;

import Solution.Permutation;
import src.Solution.Voisin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Descente {
    public int[][] distance;
    public int[][] poids;


    public Descente(int[][] distance, int[][] poids) {
        this.distance = distance;
        this.poids = poids;
    }

    public int calculerFitness(int[] solution){

        int fitness =0;
        int poids = 0;
        int numeroMachineA = 0;
        int numeroMachineB = 0;
        int distance = 0;

        for(int i=0;i< solution.length;i++){
            numeroMachineA = solution[i];

            for(int j=0;j< solution.length;j++){
                numeroMachineB = solution[j];

                poids = this.distance[i][j];
                distance = this.poids[numeroMachineA-1][numeroMachineB-1];

                fitness = fitness + (poids * distance);

            }

        }

        return fitness;
    }

    public int fitnessAmeliore(int[] ancienneSol, int ancienneFitness, Permutation p, int[] voisin ) {

        int newFitness = ancienneFitness;
        int premierePermut=0;
        int premierePermutx=0;
        int deuxiemePermuty=0;
        int deuxiemePermut=0;
        int premierePermutAPRES=0;
        int deuxiemePermutAPRES = 0;
        int premierePermutxAPRES=0;
        int deuxiemePermutyAPRES=0;

        for(int i=0; i< ancienneSol.length ; i++ ) {

            premierePermut += this.distance[p.getFirstIndice()][i]*this.poids[ancienneSol[p.getFirstIndice()]-1][ancienneSol[i]-1];
            deuxiemePermut += this.distance[p.getSecondIndice()][i]*this.poids[ ancienneSol[p.getSecondIndice()]-1][ancienneSol[i]-1 ];

            premierePermutx += this.distance[i][p.getFirstIndice()]*this.poids[ancienneSol[i]-1][ancienneSol[p.getFirstIndice()]-1] ;
            deuxiemePermuty += this.distance[i][p.getSecondIndice()]*this.poids[ancienneSol[i]-1][ancienneSol[p.getSecondIndice()]-1];

            //
            premierePermutAPRES += this.distance[p.getFirstIndice()][i]*this.poids[voisin[p.getFirstIndice()]-1][voisin[i]-1];
            deuxiemePermutAPRES += this.distance[p.getSecondIndice()][i]*this.poids[voisin[p.getSecondIndice()]-1][voisin[i]-1];

            premierePermutxAPRES += this.distance[i][p.getFirstIndice()]*this.poids[voisin[i]-1][voisin[p.getFirstIndice()]-1] ;
            deuxiemePermutyAPRES += this.distance[i][p.getSecondIndice()]*this.poids[voisin[i]-1][voisin[p.getSecondIndice()]-1];

        }

        int first = 	premierePermut + deuxiemePermut + premierePermutx + deuxiemePermuty;
        int sec = 	premierePermutAPRES + deuxiemePermutAPRES + premierePermutxAPRES + deuxiemePermutyAPRES;

        return newFitness = sec - first + newFitness ;

    }


    public int[] effectuerDescente(int[] solutionInitiale, int nombreIteration){


        System.out.println("------ Descente ------");

        //Initialisation
        int[] solutionMinimale = solutionInitiale;
        int fitnessMinimale = this.calculerFitness(solutionInitiale);

       /* int[] solutionActuelle=solutionInitiale;
        int fitnessActuelle=this.calculerFitness(solutionActuelle);*/

        int[] solutionExplo=solutionInitiale;
        int fitnessExplo=fitnessMinimale;

        //nombreIteration

        int nbChangement=0;

        for(int i = 0 ; i <=nombreIteration  ; i++){


            //Generation de voisins et récupération du meilleur
            Voisin meilleurVoisin= this.genererTousVoisin(solutionExplo.clone(),fitnessExplo);

            if (meilleurVoisin.getFitness()<fitnessMinimale){
                nbChangement++;
                solutionMinimale = meilleurVoisin.getSolution() ;
                fitnessMinimale = meilleurVoisin.getFitness();

            }
        }


        return solutionMinimale;

    }


    //methode permettant de générer tous les voisins possibles
    public Voisin genererTousVoisin(int[] solutionActuelle, int fitnessActuelle){
        int tmp;
        int mouvementInterdit[];

        ArrayList<Voisin> voisins= new ArrayList<>();
        Random intAleatoire = new Random();

        int nombreElement = solutionActuelle.length;
        int premierElement = 0;

        //index aleatoire entre 0 et nombreElement
        int compteur=0;
        int iBestFit=0;
        int BestFit=Integer.MAX_VALUE;
        for(int i=0; i<=solutionActuelle.length-2;i++){

            premierElement = i;

            for(int j=i+1;j<=solutionActuelle.length-1;j++) {
                //Permuttation de xi et xi-1
                /* int deuxiemeElement = premierElement - 1;*/
                int deuxiemeElement=j;

                int[] sVoisin = Arrays.copyOf(solutionActuelle, nombreElement);

                //boolean permettant de savoir si le mouvement est non autorisé
                boolean b = false;
                if (b == false) {

                    Permutation permutation=new Permutation(premierElement,deuxiemeElement);

                    Voisin voisin = new Voisin(permutation.permuter(solutionActuelle));

                    voisin.setPermutation(permutation);

                    voisin.setFitness(fitnessAmeliore(solutionActuelle,fitnessActuelle,permutation,voisin.getSolution()));

                    voisin.setDelta(voisin.getFitness()-fitnessActuelle);

                    voisins.add(voisin);
                    if (voisin.getFitness()<BestFit){
                        iBestFit=compteur;
                        BestFit=voisin.getFitness();
                    }
                    compteur++;
                }

            }

        }
        return voisins.get(iBestFit);
    }
}
