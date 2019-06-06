import Algorithmes.RecuitSimule;
import Algorithmes.Tabou;
import Outils.LecteurFichier;
import Outils.ecritureCSV;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main {


    /*public static void main(String[] args) {

        System.out.println("QAP");
        System.out.println("http://anjos.mgi.polymtl.ca/qaplib/inst.html#Ta");

        //Declare une solution initiale
        Integer[] solutionEssai = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

        //Cree une solution de part aleatoire
        List<Integer> solutionAleatoireList = Arrays.asList(solutionEssai);
        Collections.shuffle(solutionAleatoireList);

        //Affichage des matrices
        LecteurFichier lecteur = new LecteurFichier("tai12a.txt");//Smin = {8,1,6,2,11,10,3,5,9,7,12,4} Fmin = 224416

        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();

        //Liste qui contiendra les différentes fitness calculées lors des tests, afin de créer un CSV, pour des études statistiques
        ArrayList<Integer> donnees = new ArrayList<>();

        //Instanciation du recuit simule avec les matrices
        RecuitSimule recuitSimule = new RecuitSimule(lecteur.getDistances(), lecteur.getPoids());


        //Taux de refroidissement
        double tauxRefroiddisement = 0.99;

        //Genere le nombre de pas N1 ainsi que la temperature sur un grand ensemble de données
        Pair valeursGenerees = (recuitSimule.genererTemperature(1000, tauxRefroiddisement));

        double temperatureGeneree = ((double) (valeursGenerees.getKey()));
        int nombreDePasGenere = ((Double) valeursGenerees.getValue()).intValue();

        int[] solutionRecuitSimule;

        int fitnessInitiale;
        int fitnessRecuit;
        for(int i = 0 ; i<= 2 ; i++){
            //Mélange la solution pour avoir une initialisation aléatoire
            Collections.shuffle(solutionAleatoireList);
            //Affichage solution initiale
            System.out.println("Solution initiale : ");
            Main.afficherSolution(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
            fitnessInitiale = recuitSimule.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
            System.out.println("Fitness : " + fitnessInitiale);

            //Solution du recuit Simule
            solutionRecuitSimule = recuitSimule.effectuerRecuitSimule(
                    solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()
                    , temperatureGeneree,
                    nombreDePasGenere,500, tauxRefroiddisement, fitnessInitiale);

            //Afficher solution du recuit simule
            System.out.println("Solution du recuit simule : ");
            Main.afficherSolution(solutionRecuitSimule);
            fitnessRecuit = recuitSimule.calculerFitness(solutionRecuitSimule);
            System.out.println("fitness du recuit simule : " + fitnessRecuit );

            donnees.add(fitnessRecuit);
        }


        //Creation CSV a partir de la liste "donnees"
        //premier parametre modifiable afin d'indiquer le nom du CSV
        ecritureCSV csv = new ecritureCSV("test", donnees);
    }

    //Affichage d'une matrice
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
    }*/

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

        Tabou Tabou = new Tabou(lecteur.getDistances(), lecteur.getPoids());


        //Parametres recuit simule :
        //int[] solutionInitiale, double temperatureInitiale,
        //int nombreIteration, double tauxRefroidissement

        //Affichage solution initiale
        System.out.println("Solution initiale : ");
        Main.afficherSolution(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
        System.out.println("Fitness : " + Tabou.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()));

        //Solution du recuit Simule
        int[] solutionRecuitSimule = Tabou.effectuerTabou(
                solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()
                , 1000);

        //Afficher solution du recuit simule
        System.out.println("Solution du recuit simule : ");
        Main.afficherSolution(solutionRecuitSimule);
        System.out.println("fitness du recuit simule : " +
                Tabou.calculerFitness(solutionRecuitSimule));



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
