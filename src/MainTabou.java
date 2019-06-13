package src;

import src.Algorithmes.Tabou;
import src.Outils.LecteurFichier;
import src.Outils.ecritureCSV;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class MainTabou {


    public static void main(String[] args) {

        //lancement d'un chrono
        Instant start=Instant.now();



        System.out.println("QAP");
        System.out.println("http://anjos.mgi.polymtl.ca/qaplib/inst.html#Ta");



        //Affichage des matrices
        LecteurFichier lecteur = new LecteurFichier("tai12a.txt");//Smin = {8,1,6,2,11,10,3,5,9,7,12,4} Fmin = 224416



        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();

        //Declare une solution initiale
        Integer[] solutionEssai = new Integer[lecteur.getDistances().length];
        for (int i=0;i<=lecteur.getDistances().length-1;i++){
            solutionEssai[i]=i+1;
        }

        //Cree une solution de part aleatoire
        List<Integer> solutionAleatoireList = Arrays.asList(solutionEssai);
        Collections.shuffle(solutionAleatoireList);

        //Liste qui contiendra les différentes fitness calculées lors des tests, afin de créer un CSV, pour des études statistiques
        ArrayList<Integer> donnees = new ArrayList<>();

        //Instanciation du recuit simule avec les matrices
        Tabou Tabou = new Tabou(lecteur.getDistances(), lecteur.getPoids());

        int[] solutionRecuitSimule;

        int fitnessInitiale;
        int fitnessRecuit;
        int nbTest=1000;
        //nbIteration dans l'algo
        int nbIter=1000;
        for(int i = 0 ; i<= nbTest ; i++){
            //Mélange la solution pour avoir une initialisation aléatoire
            Collections.shuffle(solutionAleatoireList);
            //Affichage solution initiale
            //Affichage solution initiale
            System.out.println("Solution initiale : ");
            MainTabou.afficherSolution(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
            System.out.println("Fitness : " + Tabou.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()));

            //Solution du recuit Simule

            solutionRecuitSimule = Tabou.effectuerTabou(
                    solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()
                    , nbIter);

            //Afficher solution du recuit simule
            System.out.println("Solution du Tabou : ");
            MainTabou.afficherSolution(solutionRecuitSimule);
            System.out.println("fitness du Tabou : " +  Tabou.calculerFitness(solutionRecuitSimule));

            donnees.add(Tabou.calculerFitness(solutionRecuitSimule));
        }


        //Creation CSV a partir de la liste "donnees"
        //premier parametre modifiable afin d'indiquer le nom du CSV
        ecritureCSV csv = new ecritureCSV("test", donnees);
        Instant end=Instant.now();
        double time= (Duration.between(start,end).toMillis())/nbTest;
        System.out.println(new Date());
        System.out.println("temps moyen d'execution "+time);
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
