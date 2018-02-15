import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    private int popSize;
    private int indSize;
    private int timeLimit;
    private double chi;
    private MaxSat maxSat;
    private int tournamentSize;
    private ArrayList<Individual> population;
    //    private ArrayList<Integer> popFitness;
    private Random randomGenerator;

    public GeneticAlgorithm(int popSize, int timeLimit, double chi, MaxSat maxSat, int tournamentSize) {
        this.popSize = popSize;
        this.timeLimit = timeLimit;
        this.chi = chi;
        this.maxSat = maxSat;
        this.indSize = this.maxSat.getNumVars();
        this.tournamentSize = tournamentSize;
        this.population = new ArrayList<>();
//        this.popFitness = new ArrayList<>();
        this.randomGenerator = new Random();
    }

    public void generateInitialPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            String genotype = randomBitString(this.indSize);
            this.population.add(new Individual(genotype, this.maxSat.countSatClauses(genotype)));
//            this.popFitness.add(this.maxSat.countSatClauses(individual));
        }
    }

    public String mutateIndividual(String bits_x) {
        double mutationRate = this.chi / this.popSize;
        StringBuilder mutant = new StringBuilder();
        for (char c : bits_x.toCharArray()) {
            double prob = this.randomGenerator.nextDouble();
            if (prob < mutationRate) {
                mutant.append(this.bitNot(c));
            } else {
                mutant.append(c);
            }
        }

        return mutant.toString();
    }

    public String crossoverOperator(String parent_x, String parent_y) {
        StringBuilder offspring = new StringBuilder();
        for (int i = 0; i < parent_x.length(); i++) {
            if (parent_x.charAt(i) != parent_y.charAt(i)) {
                offspring.append(randomGenerator.nextInt(2));
            } else {
                offspring.append(parent_x.charAt(i));
            }
        }

        return offspring.toString();
    }

    public Individual tournamentSelection() {
//        String winner = null;
//        int winnerFitness = 0;
        Individual winner = this.population.get(0);
        for (int i = 0; i < this.tournamentSize; i++) {
            int contestantIdx = randomGenerator.nextInt(this.popSize);
            if (this.population.get(contestantIdx).getFitness() > winner.getFitness()) {
                winner = this.population.get(contestantIdx);
            }
        }
        return winner;
    }

    private Individual bestIndividual() {
        Individual bestIndividual = this.population.get(0);
        for (int i = 0; i < this.popSize; i++) {
            if (this.population.get(i).getFitness() > bestIndividual.getFitness()) {
                bestIndividual = this.population.get(i);
            }
        }
        return bestIndividual;
    }

    public String runGa() {
        Individual bestIndividual = null;
        int generationsCount = 1;

        this.generateInitialPopulation();
        long startTime = System.currentTimeMillis();
        while (true) {
            bestIndividual = bestIndividual();
            if ((System.currentTimeMillis() - startTime) / 1000.0 > this.timeLimit) {
                break;
            }

            if (bestIndividual.getFitness() == this.maxSat.getNumClauses()) {
                break;
            }

            ArrayList<Individual> newPopulation = new ArrayList<>();
            newPopulation.add(bestIndividual);
            while (newPopulation.size() < this.popSize) {
                Individual parent_x = tournamentSelection();
                Individual parent_y = tournamentSelection();

                Individual newIndividual = new Individual();
                newIndividual.setGenotype(
                        crossoverOperator(
                                mutateIndividual(parent_x.getGenotype()),
                                mutateIndividual(parent_y.getGenotype()))
                );

                newIndividual.setFitness(this.maxSat.countSatClauses(newIndividual.getGenotype()));
                newPopulation.add(newIndividual);
            }
            this.population = newPopulation;
            generationsCount++;

            if (generationsCount % 50 == 0) {
                System.out.println("Generation: " + generationsCount + ", Best fit: " + bestIndividual.getFitness());
            }
        }
        return generationsCount + " " + bestIndividual.getFitness();
//                + bestIndividual.getGenotype();
    }

    private char bitNot(char c) {
        return c == '1' ? '0' : '1';
    }

    private String randomBitString(int length) {
        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int bit = this.randomGenerator.nextInt(2);
            bitString.append(bit);
        }
        return bitString.toString();
    }
}
