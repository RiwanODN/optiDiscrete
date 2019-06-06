package Outils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
    Permet la lecture des fichiers Taillards, ainsi que la création des matrices de distances et de poids
 */
public class LecteurFichier {

    private String nomFichier;
    private int[][] poids;
    private int[][] distances;

    private Boolean matricePoidsRemplie;
    private Boolean matriceDistanceRemplie;

    private Boolean matricePoidsEnCours;
    private Boolean matriceDistanceEnCours;


    public LecteurFichier(String nomFichier) {
        this.nomFichier = nomFichier;
        this.matriceDistanceRemplie = false;
        this.matricePoidsRemplie = false;
        this.matriceDistanceEnCours = false;
        this.matricePoidsEnCours = false;
    }

    public void lireFichier(){
        BufferedReader reader;

        try{
            reader = new BufferedReader(new FileReader(
                    "./Taillards/"+this.nomFichier));

            String ligne = reader.readLine();
            String retirerPremierEspace;
            String[] nombres;

            int ligneActuelle =0;

            while (ligne != null){
                retirerPremierEspace = ligne.replaceFirst(" ","");

                nombres = retirerPremierEspace.split(" ");

                if(nombres.length == 1){
                    if(nombres[0].matches("-?\\d+(\\.\\d+)?")){
                        int taille = Integer.parseInt(nombres[0]);
                        this.initialiserMatrices(taille);
                    }else{
                        if(!this.matriceDistanceRemplie && !this.matriceDistanceEnCours){
                            this.matriceDistanceEnCours = true;
                        }else if(this.matriceDistanceEnCours){
                            this.matriceDistanceRemplie = true;
                            this.matriceDistanceEnCours = false;

                            ligneActuelle = 0;

                            this.matricePoidsEnCours = true;
                        }
                    }
                } else {
                    this.ajouterLigne(ligneActuelle, 0, nombres);
                    ligneActuelle = ligneActuelle + 1;
                }
                ligne = reader.readLine();
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initialiserMatrices(int tailleMatrice){

        this.poids = new int[tailleMatrice][tailleMatrice];
        this.distances = new int[tailleMatrice][tailleMatrice];
        System.out.println("Matrices initialiser de taille : "+tailleMatrice);
    }

    public void ajouterLigne(int ligne, int colonne, String[] nombres){
        if(this.matricePoidsEnCours){
            for(int i=0;i<nombres.length;i++){
                if(nombres[i].matches("-?\\d+(\\.\\d+)?")) {
                    this.poids[ligne][colonne] = Integer.parseInt(nombres[i]);
                    colonne = colonne +1;
                }
            }

        }else{
            for(int i=0;i<nombres.length;i++){
                if(nombres[i].matches("-?\\d+(\\.\\d+)?")) {
                    this.distances[ligne][colonne] = Integer.parseInt(nombres[i]);
                    colonne = colonne +1;

                }
            }
        }
    }

    public void afficherPoids(){
        System.out.println("Matrice des poids : ");

        for (int i = 0; i < this.poids.length; i++)
        {
            for (int j = 0; j < this.poids.length; j++)
            {
                System.out.print(this.poids[i][j]+"\t");
            }

            System.out.println();
        }
    }

    public void afficherDistances(){
        System.out.println("Matrice des distances : ");

        for (int i = 0; i < this.distances.length; i++)
        {
            for (int j = 0; j < this.distances.length; j++)
            {
                System.out.print(this.distances[i][j]+"\t");
            }

            System.out.println();
        }
    }



    //Getters et Setters
    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public int[][] getPoids() {
        return poids;
    }

    public void setPoids(int[][] poids) {
        this.poids = poids;
    }

    public int[][] getDistances() {
        return distances;
    }

    public void setDistances(int[][] distances) {
        this.distances = distances;
    }

    public Boolean getMatricePoidsRemplie() {
        return matricePoidsRemplie;
    }

    public void setMatricePoidsRemplie(Boolean matricePoidsRemplie) {
        this.matricePoidsRemplie = matricePoidsRemplie;
    }

    public Boolean getMatriceDistanceRemplie() {
        return matriceDistanceRemplie;
    }

    public void setMatriceDistanceRemplie(Boolean matriceDistanceRemplie) {
        this.matriceDistanceRemplie = matriceDistanceRemplie;
    }
}
