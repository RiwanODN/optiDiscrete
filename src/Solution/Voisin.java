package src.Solution;

import Solution.Permutation;

public class Voisin {
    private int[] solution;
    private int fitness;
    private int delta;
    //private int[] mouvementInterdit;
    private Permutation permutation;


    public Voisin(int[] solution) {
        this.solution = solution;
    }

    public int[] getSolution() {
        return solution;
    }

    public void setSolution(int[] solution) {
        this.solution = solution;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

   /* public int[] getMouvementInterdit() {
        return mouvementInterdit;
    }

    public void setMouvementInterdit(int[] mouvementInterdit) {
        this.mouvementInterdit = mouvementInterdit;
    }*/

    public Permutation getPermutation() {
        return permutation;
    }

    public void setPermutation(Permutation permutation) {
        this.permutation = permutation;
    }




}
