
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
    private ArrayList<Double> cumulativeFitnesses;
    private Random randomGenerator;

    public void setIndSize(int indSize) {
        this.indSize = indSize;
    }

    GeneticAlgorithm(int popSize, int timeLimit, double chi, MaxSat maxSat, int tournamentSize) {
        this.popSize = popSize;
        this.timeLimit = timeLimit;
        this.chi = chi;
        this.maxSat = maxSat;
        this.indSize = this.maxSat.getNumVars();
        this.tournamentSize = tournamentSize;
        this.population = new ArrayList<>();
        this.cumulativeFitnesses = new ArrayList<>();
        this.randomGenerator = new Random();
    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }

    public ArrayList<Double> getCumulativeFitnesses() {
        return cumulativeFitnesses;
    }

    public void generateInitialPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            String genotype = randomBitString(this.indSize);
            this.population.add(new Individual(genotype, this.maxSat.countSatClauses(genotype)));
        }
        this.computeCumulativeFitnesses();
    }

    public void computeCumulativeFitnesses() {
        this.cumulativeFitnesses.clear();
        int sum = 0;
        for (Individual ind : this.population) {
            sum += ind.getFitness();
            this.cumulativeFitnesses.add((double) sum);
        }
        for (int i = 0; i < this.popSize; i++) {
            this.cumulativeFitnesses.set(i, this.cumulativeFitnesses.get(i) / sum);
        }
    }

    public String mutateIndividual(String bits_x) {
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

    public String uniformCrossover(String parent_x, String parent_y) {
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

    public ArrayList<String> uniformCrossoverBothOffsprings(String parent_x, String parent_y) {
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

    public ArrayList<String> onePointCrossover(String parent_x, String parent_y) {
        StringBuilder offspring_1 = new StringBuilder();
        StringBuilder offspring_2 = new StringBuilder();

        int pos = randomGenerator.nextInt(this.indSize);
        offspring_1.append(parent_x.substring(0, pos)).append(parent_y.substring(pos, this.indSize));
        offspring_2.append(parent_y.substring(0, pos)).append(parent_x.substring(pos, this.indSize));
        ArrayList<String> offsprings = new ArrayList<>();
        offsprings.add(offspring_1.toString());
        offsprings.add(offspring_2.toString());
        return offsprings;
    }

    public Individual tournamentSelection() {
        Individual winner = new Individual();
        winner.setFitness(0);
        for (int i = 0; i < this.tournamentSize; i++) {
            int contestantIdx = randomGenerator.nextInt(this.popSize);
            Individual contestant = this.population.get(contestantIdx);
            if (contestant.getFitness() > winner.getFitness()) {
                winner = contestant;
            }
        }
        return winner;
    }

    public Individual fitnessProportionateSelection() {
        double prob = randomGenerator.nextDouble();
        for (int i = 0; i < this.popSize; i++) {
            if (prob < this.cumulativeFitnesses.get(i)) {
                return this.population.get(i);
            }
        }
        return null;
    }

    public Individual bestIndividual() {
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
//            Individual parent_x = tournamentSelection();
//            Individual parent_y = tournamentSelection();

            Individual parent_x = fitnessProportionateSelection();
            Individual parent_y = fitnessProportionateSelection();

            Individual newIndividual = new Individual();
//                newIndividual.setGenotype(
//                        uniformCrossover(
//                                mutateIndividual(parent_x.getGenotype()),
//                                mutateIndividual(parent_y.getGenotype()))
//                );

            newIndividual.setGenotype(
                    onePointCrossover(parent_x.getGenotype(), parent_y.getGenotype()).get(0)
            );
            newIndividual.setGenotype(mutateIndividual(newIndividual.getGenotype()));

            newIndividual.setFitness(this.maxSat.countSatClauses(newIndividual.getGenotype()));
            newPopulation.add(newIndividual);
        }
        return newPopulation;
    }

    private ArrayList<Individual> generateNewPopulationCrowding(ArrayList<Individual> newPopulation) {
        while (newPopulation.size() < this.popSize) {
//            Individual parent_x = tournamentSelection();
//            Individual parent_y = tournamentSelection();

            Individual parent_x = fitnessProportionateSelection();
            Individual parent_y = fitnessProportionateSelection();

//  ArrayList<String> offsprings = uniformCrossoverBothOffsprings(parent_x.getGenotype(), parent_y.getGenotype());
//            ArrayList<String> offsprings = onePointCrossover(parent_x.getGenotype(), parent_y.getGenotype());
            ArrayList<String> offsprings = uniformCrossoverBothOffsprings(parent_x.getGenotype(), parent_y.getGenotype());
            String mutant_1 = mutateIndividual(offsprings.get(0));
            String mutant_2 = mutateIndividual(offsprings.get(1));

            Individual child_1 = new Individual();
            Individual child_2 = new Individual();
            child_1.setGenotype(mutant_1);
            child_1.setFitness(maxSat.countSatClauses(mutant_1));
            child_2.setGenotype(mutant_2);
            child_2.setFitness(maxSat.countSatClauses(mutant_2));


            if (
                    dist(parent_x.getGenotype(), child_1.getGenotype()) + dist(parent_y.getGenotype(), child_2.getGenotype()) <=
                            dist(parent_x.getGenotype(), child_2.getGenotype()) + dist(parent_y.getGenotype(), child_1.getGenotype())
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
//            if ((System.currentTimeMillis() - startTime) / 1000.0 > this.timeLimit) {
//                break;
//            }

            if (bestIndividual.getFitness() == this.maxSat.getNumClauses()) {
                break;
            }

            ArrayList<Individual> newPopulation = new ArrayList<>();
            newPopulation.add(bestIndividual);
            newPopulation = generateNewPopulation(newPopulation);

//            newPopulation = generateNewPopulationCrowding(newPopulation);

            this.population = newPopulation;
            generationsCount++;

            if (generationsCount % 20 == 0) {
                StringBuilder sb = new StringBuilder();
                for (Individual ind : population) {
                    sb.append(ind.getFitness()).append(" ");
                }
                System.out.println(sb.toString() + "\n");
                System.out.println("Generation: " + generationsCount + ", Best fit: " + bestIndividual.getFitness() + ", Run time: " + (System.currentTimeMillis() - startTime) / 1000);
//                break;
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

    private int dist(String bits_x, String bits_y) {
        int total = 0;
        for (int i = 0; i < bits_x.length(); i++) {
            if (bits_x.charAt(i) == bits_y.charAt(i)) {
                total += 1;
            }
        }

        return total;
    }
}
