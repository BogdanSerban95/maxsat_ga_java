public class OperatorTests {
    public static void main(String[] args) {
//        Genetic operators tests:
//        MaxSat sat = new MaxSat();
//        sat.loadClauses("C:\\Users\\serba\\Desktop\\workspace\\uni\\second_semester\\advanced_aspects_of_funny_things\\labs\\lab_2\\project_java\\data\\test5.wcnf");
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(20, 120, 2, sat, 4);
//
//        geneticAlgorithm.generateInitialPopulation();
//        System.out.println("Selection test, random population");
//        StringBuilder sb = new StringBuilder();
//        for (Individual ind : geneticAlgorithm.getPopulation()) {
//            sb.append(ind.getFitness()).append(" ");
//        }
//        System.out.println(sb.toString());
//        System.out.println();
//        sb = new StringBuilder();
//        for (double ind : geneticAlgorithm.getCumulativeFitnesses()) {
//            sb.append(ind).append(" ");
//        }
//        System.out.println(sb.toString());
//
//        for (int i = 0; i < 10; i++) {
//            Individual selected = geneticAlgorithm.fitnessProportionateSelection();
//            System.out.println(selected.getGenotype() + "(" + selected.getFitness() + ")");
//        }

//        System.out.println("Mutation operator test runs:");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(geneticAlgorithm.mutateIndividual("000000"));
//        }

//        System.out.println("\nCrossover operator test runs (parents: 000000 and 111111):");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(geneticAlgorithm.onePointCrossover("000000", "111111"));
//        }

//        System.out.println("\nCrossover operator (both offspring) test runs (parents: 000000 and 111111):");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(String.join(" ", geneticAlgorithm.onePointCrossover("000000", "111111")));
//        }

        long startTime = System.currentTimeMillis();
        int tot = 0;
        for (int i = 0; i < 1000000; i++) {
            tot += 1;
        }
        System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
    }
}
