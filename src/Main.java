import Algorithmes.RecuitSimule;
import Outils.LecteurFichier;

public class Main {

    public static void main(String[] args) {

        System.out.println("QAP");
        System.out.println("http://anjos.mgi.polymtl.ca/qaplib/inst.html#Ta");

        int[] solutionEssai = {8,1,6,2,11,10,3,5,9,7,12,4};

        LecteurFichier lecteur = new LecteurFichier("tai12a.txt");

        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();

        RecuitSimule recuitSimule = new RecuitSimule(lecteur.getDistances(), lecteur.getPoids());



        System.out.println("Fitness : "+ recuitSimule.calculerFitness(solutionEssai));
    }
}
