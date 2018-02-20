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
    private Random randomGenerator;

    GeneticAlgorithm(int popSize, int timeLimit, double chi, MaxSat maxSat, int tournamentSize) {
        this.popSize = popSize;
        this.timeLimit = timeLimit;
        this.chi = chi;
        this.maxSat = maxSat;
        this.indSize = this.maxSat.getNumVars();
        this.tournamentSize = tournamentSize;
        this.population = new ArrayList<>();
        this.randomGenerator = new Random();
    }

    private void generateInitialPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            String genotype = randomBitString(this.indSize);
            this.population.add(new Individual(genotype, this.maxSat.countSatClauses(genotype)));
        }
    }

    private String mutateIndividual(String bits_x) {
        double mutationRate = this.chi / this.indSize;
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

    private String uniformCrossover(String parent_x, String parent_y) {
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

    private Individual tournamentSelection() {
        Individual winner = new Individual();
        winner.setFitness(0);
        ArrayList<Individual> winners = new ArrayList<>();
        for (int i = 0; i < this.tournamentSize; i++) {
            int contestantIdx = randomGenerator.nextInt(this.popSize);
            Individual contestant = this.population.get(contestantIdx);
            if (contestant.getFitness() > winner.getFitness()) {
                winner = contestant;
                winners.clear();
                winners.add(winner);
            } else if (contestant.getFitness() == winner.getFitness()) {
                winners.add(contestant);
            }
        }

        return winners.get(randomGenerator.nextInt(winners.size()));
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

            newIndividual.setGenotype(
                    uniformCrossover(parent_x.getGenotype(), parent_y.getGenotype())
            );
            newIndividual.setGenotype(mutateIndividual(newIndividual.getGenotype()));

            newIndividual.setFitness(this.maxSat.countSatClauses(newIndividual.getGenotype()));
            newPopulation.add(newIndividual);
        }
        return newPopulation;
    }

    public ArrayList<String> runGa() {
        Individual bestIndividual = null;
        int generationsCount = 1;

        this.generateInitialPopulation();
        long startTime = System.currentTimeMillis();
        while (true) {
            bestIndividual = bestIndividual();
            if ((System.currentTimeMillis() - startTime) / 1000.0 > this.timeLimit) {
                break;
            }

            ArrayList<Individual> newPopulation = new ArrayList<>();
            newPopulation.add(bestIndividual);
            newPopulation = generateNewPopulation(newPopulation);

            this.population = newPopulation;
            generationsCount++;
        }
        ArrayList<String> results = new ArrayList<>();
        results.add(String.valueOf(generationsCount * popSize));
        results.add(String.valueOf(bestIndividual.getFitness()));
        results.add(bestIndividual.getGenotype());
        return results;
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
