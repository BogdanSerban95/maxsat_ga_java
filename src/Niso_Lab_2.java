
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
                        120, 5, maxSat1, 3);
                long startTime = System.currentTimeMillis();
                System.out.println(geneticAlgorithm.runGa());
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println("Run time: " + endTime / 1000.0);
                break;
        }

    }
}
