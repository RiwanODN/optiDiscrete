package src.Algorithmes;

import Solution.Permutation;

import javafx.util.Pair;
import src.Solution.Voisin;

import java.sql.SQLOutput;
import java.util.*;

public class Tabou{

    public int[][] distance;
    public int[][] poids;


    public Tabou(int[][] distance, int[][] poids) {
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


    public int[] effectuerTabou(int[] solutionInitiale, int nombreIteration){


        System.out.println("------ Tabou ------");

        //Initialisation
        int[] solutionMinimale = solutionInitiale;
        int fitnessMinimale = this.calculerFitness(solutionInitiale);

       /* int[] solutionActuelle=solutionInitiale;
        int fitnessActuelle=this.calculerFitness(solutionActuelle);*/

        int delta;

        int[] solutionExplo=solutionInitiale;
        int fitnessExplo=fitnessMinimale;

        ArrayList<int[]> mouvementInterdit=new ArrayList<>();
        //nombreIteration

        int nbChangement=0;

        for(int i = 0 ; i <=nombreIteration  ; i++){
            System.out.println("Fitness Minimale "+fitnessMinimale);
            System.out.println("Fitness Explo "+fitnessExplo);

                //Generation de voisins et récupération du meilleur
            Voisin meilleurVoisin= this.genererVoisinAleatoire(solutionExplo.clone(),mouvementInterdit,fitnessExplo);

                if(meilleurVoisin.getDelta() <= 0){
                    //System.out.println("delta <= 0");
                    nbChangement++;
                    solutionMinimale = meilleurVoisin.getSolution() ;
                    fitnessMinimale = meilleurVoisin.getFitness();
                    solutionExplo = meilleurVoisin.getSolution() ;
                    fitnessExplo = meilleurVoisin.getFitness();

                    System.out.println("\n CHANGEMENT "+nbChangement);

                } else {

                    solutionExplo = meilleurVoisin.getSolution() ;
                    fitnessExplo = meilleurVoisin.getFitness();
                    mouvementInterdit=this.majMouvementInterdit(mouvementInterdit,meilleurVoisin);



                    /*if(Math.exp(( (delta * -1) / temperatureActuelle)) > probabiliteAcceptation){
                       // System.out.println("Formule");
                        solutionMinimale = solutionActuelle;
                        fitnessMinimale = fitnessActuelle;
                    }*/
                }

        }


        return solutionMinimale;

    }

    public Voisin genererVoisinAleatoire(int[] solutionActuelle, ArrayList<int[]> listMouvementInterdit, int fitnessActuelle){
        int tmp;
        int mouvementInterdit[];

        ArrayList<Voisin> voisins= new ArrayList<>();
        Random intAleatoire = new Random();

        int nombreElement = solutionActuelle.length;
        int premierElement = 0;


        System.out.println("recherche du meilleur voisin");

        //index aleatoire entre 0 et nombreElement
        int compteur=0;
        int iBestFit=0;
        int BestFit=fitnessActuelle*100;
        for(int i=0; i<=solutionActuelle.length-2;i++){

            premierElement = i;

            for(int j=i+1;j<=solutionActuelle.length-1;j++) {
                //Permuttation de xi et xi-1
               /* int deuxiemeElement = premierElement - 1;*/
                int deuxiemeElement=j;

                int[] sVoisin = Arrays.copyOf(solutionActuelle, nombreElement);

                //boolean permettant de savoir si le mouvement est non autorisé
                boolean b = false;
                if (listMouvementInterdit.size() != 0) {

                    for (int k = 0; k <= listMouvementInterdit.size() - 1; k++) {
                        if (listMouvementInterdit.get(k)[0] == premierElement && listMouvementInterdit.get(k)[1] == deuxiemeElement) {
                            b = true;
                            System.out.println("\n MOUVEMENT INTERDIT------------------------------------------");
                        }
                    }
                }
                if (b == false) {

                    Permutation permutation=new Permutation(premierElement,deuxiemeElement);

                    Voisin voisin = new Voisin(permutation.permuter(solutionActuelle));

                    voisin.setPermutation(permutation);
                    int fitnessOptimisee = 0;
                    if(voisins.size() != 0) {
                        fitnessOptimisee = this.calculerFitnessPartielle(voisins.get(iBestFit).getSolution(), voisin.getSolution(), BestFit, permutation);

                    } else {
                        fitnessOptimisee = this.calculerFitnessPartielle(solutionActuelle, voisin.getSolution(), fitnessActuelle, permutation);

                    }
                    voisin.setFitness(fitnessOptimisee);

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




/*
        //Permuttation de xi et xi+1
        sVoisin= Arrays.copyOf(solutionActuelle,nombreElement);
        deuxiemeElement = premierElement+1;

        b=false;
        if (listMouvementInterdit.size()!=0) {
            //verification que le mouvement n'est pas non autorisée
            for (int i = 0; i <= listMouvementInterdit.size()-1; i++) {
                if (listMouvementInterdit.get(i)[0] == premierElement && listMouvementInterdit.get(i)[1] == deuxiemeElement)
                    b = true;

            }
        }

        if (b==false){
            //System.out.println("deuxieme ele"+ deuxiemeElement);

            tmp = sVoisin[premierElement];

            sVoisin[premierElement] = sVoisin[deuxiemeElement];
            sVoisin[deuxiemeElement] = tmp;

            voisins.add(new Voisin(sVoisin));

            //ajout du mouvement interdit
            //System.out.println("un deuxieme ajout de mvt");
            mouvementInterdit = new int[]{premierElement, deuxiemeElement};
            voisins.get(1).setMouvementInterdit(mouvementInterdit);
        }*/
        System.out.println("le meilleur voisin:"+voisins.get(iBestFit).getFitness());
        System.out.println("Permutation "+voisins.get(iBestFit).getPermutation().getFirstIndice()+":"+voisins.get(iBestFit).getPermutation().getSecondIndice());
        return voisins.get(iBestFit);
    }

    //Genere un voisin aleatoire, renvoi le voisin et la nouvelle fitness, recalculee partiellement
    public int calculerFitnessPartielle(int[] solutionActuelle,int[] voisin, int fitnessInitiale, Permutation permutation){

        int nombreElement = solutionActuelle.length;
        int premierElement = permutation.getFirstIndice();
        int deuxiemeElement = permutation.getSecondIndice();

        //fitness des elements a swaper
        int fitnessAvantSwap = this.calculerFitnessMachine(solutionActuelle, premierElement) + this.calculerFitnessMachine(solutionActuelle, deuxiemeElement);

        //fitness des autres elements aux index premierElement et deuxiemeElement
        int numeroMachineA = 0;
        int numeroMachineB = 0;
        int distance = 0;
        int poids = 0;
        //Pour chaque element, on calcul la fitness des index qui ont etes swapes
        for(int i=0;i< solutionActuelle.length;i++){
            //Index premierElement
            numeroMachineA = solutionActuelle[i];

            numeroMachineB = solutionActuelle[premierElement];

            distance = this.distance[i][premierElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessAvantSwap = fitnessAvantSwap + (distance * poids);

            //Index deuxiemeElement
            numeroMachineB = solutionActuelle[deuxiemeElement];

            distance = this.distance[i][deuxiemeElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessAvantSwap = fitnessAvantSwap + (distance * poids);



        }


        //fitness des elements swapés
        int fitnessApresSwap = this.calculerFitnessMachine(voisin, premierElement) + this.calculerFitnessMachine(voisin, deuxiemeElement);

        //fitness des autres elements aux index premierElement et deuxiemeElement
        for(int i=0;i< solutionActuelle.length;i++){
            numeroMachineA = voisin[i];


            numeroMachineB = voisin[premierElement];

            distance = this.distance[i][premierElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessApresSwap = fitnessApresSwap + (distance * poids);

            numeroMachineB = voisin[deuxiemeElement];

            distance = this.distance[i][deuxiemeElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessApresSwap = fitnessApresSwap + (distance * poids);
        }


        //Pour la fitness finale, on addition a la fitness initiale, la difference des fitness qui ont changees
        int fitnessFinale = fitnessApresSwap-fitnessAvantSwap+fitnessInitiale;


        //retourne fitness voisin
        return fitnessFinale;
    }

    //Calcul la fitness pour une machine
    public int calculerFitnessMachine(int[] solution, int indexMachine){
        int numeroMachineA;
        numeroMachineA = solution[indexMachine];
        int numeroMachineB = 0;
        int distance = 0;
        int poids = 0;
        int fitness = 0;

        for(int j=0;j< solution.length;j++){
            numeroMachineB = solution[j];

            distance = this.distance[indexMachine][j];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitness = fitness + (distance * poids);

        }


        return fitness;
    }


    public ArrayList<Voisin> calculerFitnessVoisins(ArrayList<Voisin> voisins, int fitnessActuelle){
        //calcul la fitness des voisins
        HashMap<int[], Pair<Integer,Integer>> map = new HashMap<>();

        for(int i=0; i<=voisins.size()-1;i++){
            voisins.get(i).setFitness(this.calculerFitness(voisins.get(i).getSolution()));
            voisins.get(i).setDelta(voisins.get(i).getFitness()-fitnessActuelle);
        }
        return voisins;
    }

    public Voisin chercherFitnessMin(ArrayList<Voisin> voisins){

        int fitMin = voisins.get(0).getFitness();
        int iMin=0;



        for (int i =1;i<=voisins.size()-1;i++){

            if (fitMin>voisins.get(i).getFitness()){
                fitMin=voisins.get(i).getFitness();
                iMin=i;
            }

        }
        System.out.println("Je confirme: "+voisins.get(iMin).getFitness());

       // HashMap<int[], Pair> meilleurVoisin= new HashMap<>();
        //meilleurVoisin.put(voisins.get(iMin),map.get(voisins.get(iMin)));

        return voisins.get(iMin);
    }

    public ArrayList<int[]> majMouvementInterdit(ArrayList<int[]> mouvementInterdit, Voisin meilleurVoisin){


        //mouvementInterdit.add(0,meilleurVoisin.getPermutation().permutMoins1());
        mouvementInterdit.add(0,meilleurVoisin.getPermutation().getPermut());

        if (mouvementInterdit.size()>10){
            mouvementInterdit.remove(mouvementInterdit.size()-1);
        }


        return mouvementInterdit;
    }
}


