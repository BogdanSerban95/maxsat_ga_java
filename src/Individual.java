public class Individual {
    private int fitness;
    private String genotype;

    public Individual() {

    }

    public Individual(String genotype, int fitness) {
        this.genotype = genotype;
        this.fitness = fitness;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }
}
