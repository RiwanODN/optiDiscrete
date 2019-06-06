package Algorithmes;

import Solution.Permutation;
import Solution.Voisin;
import javafx.util.Pair;

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

        int[] solutionTmp;
        int fitnessTmp;

        ArrayList<int[]> mouvementInterdit=new ArrayList<>();
        //nombreIteration
        for(int i = 0 ; i <=nombreIteration  ; i++){

                //Generation d'un voisin aleatoire
                ArrayList<Voisin> voisins= this.genererVoisinAleatoire(solutionMinimale.clone(),mouvementInterdit,fitnessMinimale);

                //Fitness du voisin generer
                //voisins = this.calculerFitnessVoisins(voisins,fitnessMinimale);
            System.out.println("FA "+fitnessMinimale);
            //System.out.println("FV1 "+voisins.get(0).getFitness());
           // System.out.println("FV2 "+voisins.get(1).getFitness());

                //selection fitness la plus faible
                Voisin meilleurVoisin=this.chercherFitnessMin(voisins);
            System.out.println("F M "+meilleurVoisin.getFitness());
            System.out.println("D M" + meilleurVoisin.getDelta());


                // System.out.println("Fmin : "+fitnessMinimale+" Fi : "+fitnessActuelle+ " delta : "+delta);
                if(meilleurVoisin.getDelta() <= 0){
                    //System.out.println("delta <= 0");

                    solutionMinimale = meilleurVoisin.getSolution() ;
                    fitnessMinimale = meilleurVoisin.getFitness();

                } else {

                    /*solutionMinimale = meilleurVoisin.getSolution() ;
                    fitnessMinimale = meilleurVoisin.getFitness();*/
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

    public ArrayList genererVoisinAleatoire(int[] solutionActuelle, ArrayList<int[]> listMouvementInterdit, int fitnessActuelle){
        int tmp;
        int mouvementInterdit[];

        ArrayList<Voisin> voisins= new ArrayList<>();
        Random intAleatoire = new Random();

        int nombreElement = solutionActuelle.length;
        int premierElement = 0;

        System.out.println("recherche de voisins");

        //index aleatoire entre 0 et nombreElement
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
                            System.out.println("MOUVEMENT INTERDIT");
                        }
                    }
                }
                if (b == false) {
                    Permutation permutation=new Permutation(premierElement,deuxiemeElement);

                    Voisin voisin = new Voisin(permutation.permuter(solutionActuelle));

                    voisin.setPermutation(permutation);

                    voisin.setFitness(this.calculerFitness(voisin.getSolution()));

                    voisin.setDelta(voisin.getFitness()-fitnessActuelle);

                    voisins.add(voisin);
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

        return voisins;
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

       // HashMap<int[], Pair> meilleurVoisin= new HashMap<>();
        //meilleurVoisin.put(voisins.get(iMin),map.get(voisins.get(iMin)));

        return voisins.get(iMin);
    }

    public ArrayList<int[]> majMouvementInterdit(ArrayList<int[]> mouvementInterdit, Voisin meilleurVoisin){


        mouvementInterdit.add(0,meilleurVoisin.getPermutation().permutMoins1());

        if (mouvementInterdit.size()>10){
            mouvementInterdit.remove(mouvementInterdit.size()-1);
        }


        return mouvementInterdit;
    }
}


