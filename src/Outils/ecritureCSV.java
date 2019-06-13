package src.Outils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ecritureCSV {

    private String nomfichier;
    private ArrayList<String> donnees;

    public ecritureCSV(String nomfichier, ArrayList donnees) {
        this.nomfichier = nomfichier;
        this.donnees = donnees;
        try {
            this.creerCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creerCSV() throws IOException {

        FileWriter csvWriter = new FileWriter(this.nomfichier + ".csv");

        for (String rowData : this.donnees) {
            csvWriter.append(rowData + " ");
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();

        System.out.println("Fichier CSV crée.");
    }
}
