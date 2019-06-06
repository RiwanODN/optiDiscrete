package Solution;

import java.util.Random;

public class Permutation {
    private int firstIndice;
    private int secondIndice;
    private Random random=new Random();

    public Permutation(){

    }

    /**
     * permutation de deux éléments à l'indice a et b
     * @param a
     * @param b
     */
    public Permutation(int a, int b){
        this.firstIndice=a;
        this.secondIndice=b;
    }

    /**
     * une permutation aléatoire
     * il faut assurer que firstIndice < secondIndice
     */

    public Permutation (int nbEquipements) {
        int indice1 = random.nextInt(nbEquipements-1);
        int indice2 = random.nextInt(nbEquipements-indice1-1)+1;

        this.firstIndice=indice1;
        this.secondIndice=indice1+indice2;
    }

    public int[] permuter(int[] equipements) {

        int swap;
        int[] unEquipement = equipements.clone();


        swap = unEquipement[secondIndice];
        int a = unEquipement[firstIndice] ;
        int b = unEquipement[secondIndice];

        unEquipement[secondIndice] = a;//unEquipement[i];
        unEquipement[firstIndice]= b;//swap;


        return unEquipement;
    }

    public int[] permutMoins1() {
        int swap = this.secondIndice;
        this.secondIndice = this.firstIndice;
        this.firstIndice = swap;

        int[] mouvementInterdit={firstIndice, secondIndice};
        return mouvementInterdit;

    }

    public int getFirstIndice() {
        return firstIndice;
    }

    public void setFirstIndice(int firstIndice) {
        this.firstIndice = firstIndice;
    }

    public int getSecondIndice() {
        return secondIndice;
    }

    public void setSecondIndice(int secondIndice) {
        this.secondIndice = secondIndice;
    }
}
