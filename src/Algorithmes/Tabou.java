package src.Algorithmes;

import Solution.Permutation;

import javafx.util.Pair;
import src.Solution.Voisin;
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

    public int fitnessOpti(int[] sol,int[] newSol, Permutation p, int fitness ) {

        int newFitness = fitness;
        int produit1=0;
        int produit2=0;
        int produit3 = 0;
        int produit4=0;

        for(int i=0; i<= sol.length-1 ; i++ ) {
            if( i!= p.getFirstIndice() ||  i!=p.getSecondIndice() ) {

                produit1 = this.poids[sol[i]-1][sol[p.getFirstIndice()]-1]* this.distance[i][p.getFirstIndice()];

                produit2 = this.poids[sol[i]-1][sol[p.getSecondIndice()]-1]* this.distance[i][p.getSecondIndice()];

                produit3 = this.poids[newSol[i]-1][newSol[p.getFirstIndice()]-1]* this.distance[i][p.getFirstIndice()];

                produit4 = this.poids[newSol[i]-1][newSol[p.getSecondIndice()]-1]* this.distance[i][p.getSecondIndice()];

                newFitness = newFitness  - produit1 - produit2 + produit3 + produit4 ;
            }
        }



        return newFitness ;

    }


    public int[] effectuerTabou(int[] solutionInitiale, int nombreIteration){


        System.out.println("------ Tabou ------");

        //Initialisation
        int[] solutionMinimale = solutionInitiale;
        int fitnessMinimale = this.calculerFitness(solutionInitiale);

       /* int[] solutionActuelle=solutionInitiale;
        int fitnessActuelle=this.calculerFitness(solutionActuelle);*/

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

                if(meilleurVoisin.getDelta() >= 0){
                    mouvementInterdit=this.majMouvementInterdit(mouvementInterdit,meilleurVoisin);

                }
                if (meilleurVoisin.getFitness()<fitnessMinimale){
                    nbChangement++;
                    solutionMinimale = meilleurVoisin.getSolution() ;
                    fitnessMinimale = meilleurVoisin.getFitness();
                    /*if(Math.exp(( (delta * -1) / temperatureActuelle)) > probabiliteAcceptation){
                       // System.out.println("Formule");
                        solutionMinimale = solutionActuelle;
                        fitnessMinimale = fitnessActuelle;
                    }*/
                }
            solutionExplo = meilleurVoisin.getSolution() ;
            fitnessExplo = meilleurVoisin.getFitness();

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


       // System.out.println("recherche du meilleur voisin");

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
                if (listMouvementInterdit.size() != 0) {

                    for (int k = 0; k <= listMouvementInterdit.size() - 1; k++) {
                        if (listMouvementInterdit.get(k)[0] == premierElement && listMouvementInterdit.get(k)[1] == deuxiemeElement) {
                            b = true;
                            //System.out.println("\n MOUVEMENT INTERDIT------------------------------------------");
                        }
                    }
                }
                if (b == false) {

                    Permutation permutation=new Permutation(premierElement,deuxiemeElement);

                    Voisin voisin = new Voisin(permutation.permuter(solutionActuelle));

                    voisin.setPermutation(permutation);

                   // System.out.println("Calcul fitness + "+ this.calculerFitness(voisin.getSolution()));
                    //System.out.println("calcul fitness opti + "+this.fitnessOpti(solutionActuelle,voisin.getSolution(),permutation,fitnessActuelle));

                    voisin.setFitness(this.calculerFitness(voisin.getSolution()));

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
        //System.out.println("le meilleur voisin:"+voisins.get(iBestFit).getFitness());
        //System.out.println("Permutation "+voisins.get(iBestFit).getPermutation().getFirstIndice()+":"+voisins.get(iBestFit).getPermutation().getSecondIndice());
        return voisins.get(iBestFit);
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

        if (mouvementInterdit.size()>14){
            mouvementInterdit.remove(mouvementInterdit.size()-1);
        }


        return mouvementInterdit;
    }
    
}


