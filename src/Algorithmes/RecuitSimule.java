package Algorithmes;


import javafx.util.Pair;

import java.util.*;

public class RecuitSimule {

    public int[][] distance;
    public int[][] poids;



    public RecuitSimule(int[][] distance, int[][] poids) {
        this.distance = distance;
        this.poids = poids;

    }

    //Calcul de la fitness d'une solution entiere
    public int calculerFitness(int[] solution){

        int fitness =0;
        int distance = 0;
        int numeroMachineA = 0;
        int numeroMachineB = 0;
        int poids = 0;

        for(int i=0;i< solution.length;i++){
            numeroMachineA = solution[i];
            for(int j=0;j< solution.length;j++){
                numeroMachineB = solution[j];

                distance = this.distance[i][j];
                poids = this.poids[numeroMachineA-1][numeroMachineB-1];

                fitness = fitness + (distance * poids);

            }

        }

        return fitness;
    }


    //Recuit Simule
    public int[] effectuerRecuitSimule(int[] solutionInitiale, double temperatureInitiale,
                                       int nombreIteration, int nombreIterationTemperature, double tauxRefroidissement, int fitnessinitiale){

        System.out.println("------ RECUIT SIMULE ------");
        //Initialisation
        int[] solutionMinimale = solutionInitiale;
        int fitnessMinimale = this.calculerFitness(solutionInitiale);

        int[] solutionActuelle;
        int fitnessActuelle = fitnessinitiale;

        int delta;


        double probabiliteAcceptation;
        double temperatureActuelle = temperatureInitiale;

        Map <int[], Integer> hm = new HashMap<>();
        Map.Entry<int[],Integer> entry ;


        for(int i = 0 ; i <= nombreIteration ; i++){
            //On décide d'arrêter si la température est beaucoup trop faible
            if(temperatureInitiale < 10){
                System.out.println("interruption : temperature < 0.1");
                return solutionMinimale;
            }
            for (int j = 0; j<=nombreIterationTemperature ; j++){
                //Generation d'un voisin aleatoire, on recupere la nouvelle fitness ainsi que le voisin cree
                hm = this.genererVoisinAleatoireOptimisee(solutionMinimale.clone(), fitnessMinimale);
                entry = hm.entrySet().iterator().next();
                solutionActuelle = entry.getKey();
                //Fitness du voisin generer
                fitnessActuelle = hm.entrySet().iterator().next().getValue();

                //Difference de la fitness du voisin et la fitness minimale
                delta = fitnessActuelle - fitnessMinimale;

                //Si la fitness du voisin est meilleur, on la prend
                if(delta <= 0){

                    solutionMinimale = solutionActuelle;
                    fitnessMinimale = fitnessActuelle;

                } else {//Sinon, on effectue le test afin de savoir si on l'accepte quand meme ou non

                    //Genere une probabilite d'acceptation
                    probabiliteAcceptation = Math.random();

                    if(Math.exp(( (delta * -1) / temperatureActuelle)) > probabiliteAcceptation){
                        solutionMinimale = solutionActuelle;
                        fitnessMinimale = fitnessActuelle;
                    }
                }
            }



            //Refroidissement de la temperature
            temperatureActuelle = temperatureActuelle * tauxRefroidissement;

        }


        return solutionMinimale;

    }

    //Genere un voisin aleatoire, renvoi le voisin et la nouvelle fitness, recalculee partiellement
    public Map <int[], Integer> genererVoisinAleatoireOptimisee(int[] solutionActuelle, int fitnessInitiale){

        Random intAleatoire = new Random();

        int nombreElement = solutionActuelle.length;
        int premierElement = 0;
        int deuxiemeElement = 0;

        //index aleatoire entre 0 et nombreElement
        premierElement = intAleatoire.nextInt(nombreElement);

        while(deuxiemeElement == premierElement){
            deuxiemeElement = intAleatoire.nextInt(nombreElement);
        }

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


        //Permuttation des valeurs
        int tmp = solutionActuelle[premierElement];
        solutionActuelle[premierElement] = solutionActuelle[deuxiemeElement];
        solutionActuelle[deuxiemeElement] = tmp;


        //fitness des elements swapés
        int fitnessApresSwap = this.calculerFitnessMachine(solutionActuelle, premierElement) + this.calculerFitnessMachine(solutionActuelle, deuxiemeElement);

        //fitness des autres elements aux index premierElement et deuxiemeElement
        for(int i=0;i< solutionActuelle.length;i++){
            numeroMachineA = solutionActuelle[i];


            numeroMachineB = solutionActuelle[premierElement];

            distance = this.distance[i][premierElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessApresSwap = fitnessApresSwap + (distance * poids);

            numeroMachineB = solutionActuelle[deuxiemeElement];

            distance = this.distance[i][deuxiemeElement];
            poids = this.poids[numeroMachineA-1][numeroMachineB-1];

            fitnessApresSwap = fitnessApresSwap + (distance * poids);
        }


        //Pour la fitness finale, on addition a la fitness initiale, la difference des fitness qui ont changees
        int fitnessFinale = fitnessApresSwap-fitnessAvantSwap+fitnessInitiale;


        Map <int[], Integer> hm = new HashMap<>();

        hm.put(solutionActuelle, fitnessFinale);

        //retourne le voisin ainsi que sa fitness
        return hm;
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

    //Creer un ensemble de solution et genere une temperature ainsi qu'un nombre d'iteration
    public Pair<Double, Double> genererTemperature(int nombreIteration, double refroidissement){


        Integer[] solutionEssai = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

        List<Integer> solutionAleatoireList = Arrays.asList(solutionEssai);

        Integer[] voisinEssai = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

        List<Integer> voisinAleatoireList = Arrays.asList(voisinEssai);

        double fitnessActuelle = 0;
        double fitnessVoisin = 0;
        double deltaMax = 0;

        for(int i = 0 ; i<= nombreIteration ; i++){
            Collections.shuffle(solutionAleatoireList);//genere une nouvelle solution
            fitnessActuelle = this.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());

            for(int j =0; j<= nombreIteration ; j++){
                Collections.shuffle(voisinAleatoireList);//genere une nouvelle solution
                fitnessVoisin = this.calculerFitness(voisinAleatoireList.stream().mapToInt(Integer::intValue).toArray());

                deltaMax = Math.max((Math.abs(fitnessVoisin - fitnessActuelle)), deltaMax);
            }

        }

        System.out.println("Delta max : " + deltaMax + " temperature : " + ((-deltaMax)/Math.log(0.8)));

        double temperatureGeneree = ((-deltaMax)/Math.log(0.8));
        System.out.println(temperatureGeneree);
        double nombrePasGenere = Math.log( -deltaMax / (temperatureGeneree * Math.log(0.01))) / Math.log(0.9993);


        return new Pair(temperatureGeneree, nombrePasGenere);
    }
}
