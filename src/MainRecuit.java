import javafx.util.Pair;
import src.Algorithmes.RecuitSimule;

import src.Outils.LecteurFichier;
import src.Outils.ecritureCSV;

import java.util.*;


public class MainRecuit {


    public static void main(String[] args) {

        System.out.println("QAP");
        System.out.println("http://anjos.mgi.polymtl.ca/qaplib/inst.html#Ta");



        //Affichage des matrices
        LecteurFichier lecteur = new LecteurFichier("tai60a.txt");//Smin = {8,1,6,2,11,10,3,5,9,7,12,4} Fmin = 224416

        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();

        //Declare une solution initiale
        Integer[] solutionEssai = new Integer[lecteur.getDistances().length];

        Arrays.setAll(solutionEssai, i -> i + 1);
        //Cree une solution de depart aleatoire
        List<Integer> solutionAleatoireList = Arrays.asList(solutionEssai);
        Collections.shuffle(solutionAleatoireList);


        //Liste qui contiendra les différentes fitness calculées lors des tests, afin de créer un CSV, pour des études statistiques
        ArrayList<Integer> donnees = new ArrayList<>();
        ArrayList<Double> ameliorations = new ArrayList<>();

        //Instanciation du recuit simule avec les matrices
        RecuitSimule recuitSimule = new RecuitSimule(lecteur.getDistances(), lecteur.getPoids());


        //Taux de refroidissement
        double tauxRefroiddisement = 0.99;

        //Genere le nombre de pas N1 ainsi que la temperature sur un grand ensemble de données
        Pair valeursGenerees = (recuitSimule.genererTemperature(1000, solutionEssai.length));

        double temperatureGeneree = ((double) (valeursGenerees.getKey()));
        int nombreDePasGenere = ((Double) valeursGenerees.getValue()).intValue();

        int[] solutionRecuitSimule;

        int fitnessInitiale;
        int fitnessRecuit;
        System.out.println("Solution initiale : ");
        Collections.shuffle(solutionAleatoireList);
        MainRecuit.afficherSolution(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
        //Mesure du temps
double amelioration = 0;
        long debutCode = System.nanoTime();
        //for(int i = 0 ; i<= 1000 ; i++){

            fitnessInitiale = recuitSimule.calculerFitness(solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray());
            System.out.println("Fitness : " + fitnessInitiale);

            //Solution du recuit Simule
            solutionRecuitSimule = recuitSimule.effectuerRecuitSimule(
                    solutionAleatoireList.stream().mapToInt(Integer::intValue).toArray()
                    , temperatureGeneree,
                    nombreDePasGenere,500, tauxRefroiddisement, fitnessInitiale);


            fitnessRecuit = recuitSimule.calculerFitness(solutionRecuitSimule);
            System.out.println("fitness du recuit simule : " + fitnessRecuit );

            amelioration = -(((fitnessRecuit-fitnessInitiale)*100)/fitnessInitiale);
            ameliorations.add(amelioration);
            donnees.add(fitnessRecuit);
        //}

        IntSummaryStatistics stats = donnees
                .stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        DoubleSummaryStatistics statsAmelioration = ameliorations
                .stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        System.out.println("Fitness moyenne : " + stats.getAverage());
        System.out.println("Amelioration moyenne :" + statsAmelioration.getAverage());

        long finCode = System.nanoTime();
        long tempsExecution = finCode - debutCode;
        System.out.println("Execution en " + tempsExecution / 1000000 + "ms");
        //Afficher solution du recuit simule
        //System.out.println("Solution du recuit simule : ");
        //MainRecuit.afficherSolution(solutionRecuitSimule);
        //Creation CSV a partir de la liste "donnees"
        //premier parametre modifiable afin d'indiquer le nom du CSV
        ecritureCSV csv = new ecritureCSV("test", donnees);
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
