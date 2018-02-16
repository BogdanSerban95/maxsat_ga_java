import com.sun.xml.internal.bind.v2.TODO;

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

    GeneticAlgorithm(int popSize, int timeLimit, double chi, MaxSat maxSat, int tournamentSize) {
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

    private void generateInitialPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            String genotype = randomBitString(this.indSize);
            this.population.add(new Individual(genotype, this.maxSat.countSatClauses(genotype)));
//            this.popFitness.add(this.maxSat.countSatClauses(individual));
        }
    }

    private String mutateIndividual(String bits_x) {
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

    private String crossoverOperator(String parent_x, String parent_y) {
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

    private ArrayList<String> crossoverBothOffspring(String parent_x, String parent_y) {
        StringBuilder offspring_1 = new StringBuilder();
        StringBuilder offspring_2 = new StringBuilder();
        for (int i = 0; i < parent_x.length(); i++) {
            if (parent_x.charAt(i) != parent_y.charAt(i)) {
                int bit = randomGenerator.nextInt(2);
                offspring_1.append(bit);
                offspring_2.append(Math.abs(bit - 1));
            } else {
                offspring_1.append(parent_x.charAt(i));
                offspring_2.append(parent_x.charAt(i));
            }
        }

        ArrayList<String> offsprings = new ArrayList<>();
        offsprings.add(offspring_1.toString());
        offsprings.add(offspring_2.toString());

        return offsprings;
    }

    private Individual tournamentSelection() {
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

    private ArrayList<Individual> generateNewPopulation(ArrayList<Individual> newPopulation) {
        while (newPopulation.size() < this.popSize) {
            Individual parent_x = tournamentSelection();
            Individual parent_y = tournamentSelection();

            Individual newIndividual = new Individual();
//                newIndividual.setGenotype(
//                        crossoverOperator(
//                                mutateIndividual(parent_x.getGenotype()),
//                                mutateIndividual(parent_y.getGenotype()))
//                );

            newIndividual.setGenotype(
                    crossoverOperator(parent_x.getGenotype(), parent_y.getGenotype())
            );
            newIndividual.setGenotype(mutateIndividual(newIndividual.getGenotype()));

            newIndividual.setFitness(this.maxSat.countSatClauses(newIndividual.getGenotype()));
            newPopulation.add(newIndividual);
        }
        return newPopulation;
    }

    private ArrayList<Individual> generateNewPopulationCrowding(ArrayList<Individual> newPopulation) {
        while (newPopulation.size() < this.popSize) {
            Individual parent_x = tournamentSelection();
            Individual parent_y = tournamentSelection();

            ArrayList<String> offsprings = crossoverBothOffspring(parent_x.getGenotype(), parent_y.getGenotype());
            String mutant_1 = mutateIndividual(offsprings.get(0));
            String mutant_2 = mutateIndividual(offsprings.get(1));

            Individual child_1 = new Individual();
            Individual child_2 = new Individual();
            child_1.setGenotype(mutant_1);
            child_1.setFitness(maxSat.countSatClauses(mutant_1));
            child_2.setGenotype(mutant_2);
            child_2.setFitness(maxSat.countSatClauses(mutant_2));


//            TODO: Use Hamming distance instead of fitness difference
            if (
                    Math.abs(parent_x.getFitness() - child_1.getFitness()) + Math.abs(parent_y.getFitness() - child_2.getFitness()) <=
                            Math.abs(parent_x.getFitness() - child_2.getFitness()) + Math.abs(parent_y.getFitness() - child_1.getFitness())
                    ) {
                if (child_1.getFitness() > parent_x.getFitness()) newPopulation.add(child_1);
                else newPopulation.add(parent_x);
                if (child_2.getFitness() > parent_y.getFitness()) newPopulation.add(child_2);
                else newPopulation.add(parent_y);
            } else {
                if (child_2.getFitness() > parent_x.getFitness()) newPopulation.add(child_2);
                else newPopulation.add(parent_x);
                if (child_1.getFitness() > parent_y.getFitness()) newPopulation.add(child_1);
                else newPopulation.add(parent_y);
            }
        }
        return newPopulation;
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
//            newPopulation = generateNewPopulation(newPopulation);

            newPopulation = generateNewPopulationCrowding(newPopulation);

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
