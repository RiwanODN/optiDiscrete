import Algorithmes.RecuitSimule;
import Outils.LecteurFichier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("QAP");
        System.out.println("http://anjos.mgi.polymtl.ca/qaplib/inst.html#Ta");

        Integer[] solutionEssai = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

        List<Integer> solutionAleatoireList = Arrays.asList(solutionEssai);
        Collections.shuffle(solutionAleatoireList);

        LecteurFichier lecteur = new LecteurFichier("tai12a.txt");//Smin = {8,1,6,2,11,10,3,5,9,7,12,4} Fmin = 224416

        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();

        RecuitSimule recuitSimule = new RecuitSimule(lecteur.getDistances(), lecteur.getPoids());


        //Parametres recuit simule :
        //int[] solutionInitiale, double temperatureInitiale,
        //int nombreIteration, double tauxRefroidissement

        //Affichage solution initiale
        System.out.println("Solution initiale : ");
        Main.afficherSolution(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
        System.out.println("Fitness : " + recuitSimule.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()));

        //Solution du recuit Simule
        int[] solutionRecuitSimule = recuitSimule.effectuerRecuitSimule(
                solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()
                , 15000,
                1000, 0.99);

        //Afficher solution du recuit simule
        System.out.println("Solution du recuit simule : ");
        Main.afficherSolution(solutionRecuitSimule);
        System.out.println("fitness du recuit simule : " +
                recuitSimule.calculerFitness(solutionRecuitSimule));



    }

    public static void afficherSolution(int[] solution){
        System.out.print("(");
        for(int i = 0; i<solution.length;i++){
            System.out.print(solution[i]);
            if(i ==solution.length-1){
                System.out.println(")");
            }else{
                System.out.print(" , ");
            }
        }
    }
}
