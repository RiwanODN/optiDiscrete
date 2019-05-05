package Algorithmes;


import java.util.Random;

public class RecuitSimule {

    public int[][] distance;
    public int[][] poids;


    public RecuitSimule(int[][] distance, int[][] poids) {
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


    public int[] effectuerRecuitSimule(int[] solutionInitiale, double temperatureInitiale,
                                       int nombreIteration, double tauxRefroidissement){

        System.out.println("------ RECUIT SIMULE ------");
        //Initialisation
        int[] solutionMinimale = solutionInitiale;
        int fitnessMinimale = this.calculerFitness(solutionInitiale);

        int[] solutionActuelle;
        int fitnessActuelle;

        int delta;

        int[] solutionTmp;
        int fitnessTmp;

        double probabiliteAcceptation;
        double temperatureActuelle = temperatureInitiale;


        for(int i = 0 ; i <= nombreIteration ; i++){
            //On décide d'arrêter si la température est beaucoup trop faible
            if(temperatureInitiale < 0.1){
                System.out.println("interruption : temperature < 0.1");
                return solutionMinimale;
            }
            for (int j = 0; j<=50 ; j++){
                //Generation d'un voisin aleatoire
                solutionActuelle = this.genererVoisinAleatoire(solutionMinimale.clone());
                //Fitness du voisin generer
                fitnessActuelle = this.calculerFitness(solutionActuelle);

                //Difference de la fitness du voisin et la fitness minimale
                delta = fitnessActuelle - fitnessMinimale;

               // System.out.println("Fmin : "+fitnessMinimale+" Fi : "+fitnessActuelle+ " delta : "+delta);
                if(delta <= 0){
                    //System.out.println("delta <= 0");
                    //On sauvegarde la meilleure solution afin de la restaurer si necessaire
                    solutionTmp = solutionMinimale;
                    fitnessTmp = fitnessMinimale;

                    solutionMinimale = solutionActuelle;
                    fitnessMinimale = fitnessActuelle;

                } else {

                    probabiliteAcceptation = Math.random();
                    //System.out.println(Math.exp(( (delta * -1) / temperatureActuelle)) +
                         //   " > "+ probabiliteAcceptation);

                    if(Math.exp(( (delta * -1) / temperatureActuelle)) > probabiliteAcceptation){
                       // System.out.println("Formule");
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

    public int[] genererVoisinAleatoire(int[] solutionActuelle){
        Random intAleatoire = new Random();

        int nombreElement = solutionActuelle.length;
        int premierElement = 0;
        int deuxiemeElement = 0;

        //index aleatoire entre 0 et nombreElement
        premierElement = intAleatoire.nextInt(nombreElement);

        while(deuxiemeElement == premierElement){
            deuxiemeElement = intAleatoire.nextInt(nombreElement);
        }

        //Permuttation des valeurs
        int tmp = solutionActuelle[premierElement];
        solutionActuelle[premierElement] = solutionActuelle[deuxiemeElement];
        solutionActuelle[deuxiemeElement] = tmp;

        return solutionActuelle;
    }
}
