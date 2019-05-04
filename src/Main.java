import Outils.LecteurFichier;

public class Main {

    public static void main(String[] args) {

        System.out.println("QAP");

        LecteurFichier lecteur = new LecteurFichier("tai12a.txt");

        lecteur.lireFichier();

        lecteur.afficherPoids();
        lecteur.afficherDistances();
    }
}
