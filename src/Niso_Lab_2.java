
public class Niso_Lab_2 {
    public static void main(String[] args) {
        ArgParser parser = ArgParser.getInstance();
        int question = Integer.parseInt(parser.getArgument(args, "-question"));
        switch (question) {
            case 1:
                MaxSat.MaxSatClause clause = new MaxSat.MaxSatClause(parser.getArgument(args, "-clause"));
                System.out.println(clause.checkSat(parser.getArgument(args, "-assignment")));
                break;
            case 2:
                MaxSat maxSat = new MaxSat();
                maxSat.loadClauses(parser.getArgument(args, "-wdimacs"));
                System.out.println(maxSat.countSatClauses(parser.getArgument(args, "-assignment")));
                break;
            case 3:
                MaxSat maxSat1 = new MaxSat();
                maxSat1.loadClauses(parser.getArgument(args, "-wdimacs"));
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(10,
                        Integer.parseInt(parser.getArgument(args,"-time_budget")), 0.6, maxSat1, 2);
                long startTime = System.currentTimeMillis();
                System.out.println(geneticAlgorithm.runGa());
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println("Run time: " + endTime / 1000.0);
                break;
        }
//
//////        Genetic operators tests:
//        MaxSat sat = new MaxSat();
//        sat.loadClauses("C:\\Users\\serba\\Desktop\\workspace\\uni\\second_semester\\advanced_aspects_of_funny_things\\labs\\lab_2\\project_java\\data\\test5.wcnf");
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(20, 120, 2, sat, 4);
////        geneticAlgorithm.setIndSize(6);
//        geneticAlgorithm.generateInitialPopulation();
////
//        System.out.println("Selection test, random population");
//        StringBuilder sb = new StringBuilder();
//        for (Individual ind : geneticAlgorithm.getPopulation()) {
//            sb.append(ind.getFitness()).append(" ");
//        }
//        System.out.println(sb.toString());
////        System.out.println();
////        sb = new StringBuilder();
////        for (double ind : geneticAlgorithm.getCumulativeFitnesses()) {
////            sb.append(ind).append(" ");
////        }
////        System.out.println(sb.toString());
//////
//        for (int i = 0; i < 10; i++) {
//            Individual selected = geneticAlgorithm.fitnessProportionateSelection();
//            System.out.println(selected.getGenotype() + "(" + selected.getFitness() + ")");
//        }
//        System.out.println("Mutation operator test runs:");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(geneticAlgorithm.mutateIndividual("000000"));
//        }
//
//        System.out.println("\nCrossover operator test runs (parents: 000000 and 111111):");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(geneticAlgorithm.crossoverOperator("000000", "111111"));
//        }
//
//        System.out.println("\nCrossover operator (both offspring) test runs (parents: 000000 and 111111):");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(String.join(" ", geneticAlgorithm.onePointCrossover("000000", "111111")));
//        }


    }
}
