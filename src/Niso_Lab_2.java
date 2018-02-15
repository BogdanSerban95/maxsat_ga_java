
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
                break;
        }

    }


}
