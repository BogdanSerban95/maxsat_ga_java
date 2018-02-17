
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


                String repsStr = parser.getArgument(args, "-repetitions");
                int reps = repsStr != null ? Integer.parseInt(repsStr) : 1;

                for (int i = 0; i < reps; i++) {
                    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(10,
                            Integer.parseInt(parser.getArgument(args, "-time_budget")), 0.6, maxSat1, 3);
                    System.out.println(geneticAlgorithm.runGa());
                }
                break;
        }
    }
}
