import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Experiments {
    public static void main(String[] args) {
        ArgParser parser = ArgParser.getInstance();
        int experiment = Integer.parseInt(parser.getArgument(args, "-experiment"));

        MaxSat maxSat = new MaxSat();
        maxSat.loadClauses(new File(".").getAbsolutePath().replace(".", "..\\") + "data\\test3.wcnf");

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        switch (experiment) {
            case 1:
//                Population size experiment
                for (int i = 10; i <= 40; i += 5) {
                    System.out.println("Pop size: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + j);
                        }
                        GeneticAlgorithm ga = new GeneticAlgorithm(i, 15, 1.2, maxSat, 3);
                        expResults.add(ga.runGa().get(1));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\population_size.csv", results, labels);
                break;
            case 2:
//                Mutation rate experiment
                for (double i = 0.4; i <= 2.8; i += 0.4) {
                    System.out.println("Mutation rate: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + j);
                        }
                        GeneticAlgorithm ga = new GeneticAlgorithm(20, 15, i, maxSat, 3);
                        expResults.add(ga.runGa().get(1));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\mutation_rate.csv", results, labels);
                break;
            case 3:
//                Tournament size experiment
                for (int i = 2; i <= 5; i++) {
                    System.out.println("Tournament size: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + j);
                        }
                        GeneticAlgorithm ga = new GeneticAlgorithm(20, 15, 1.2, maxSat, i);
                        expResults.add(ga.runGa().get(1));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\tournament_size.csv", results, labels);
        }
    }

    private static void writeResultsToCsv(String fileName, ArrayList<ArrayList<String>> results, ArrayList<String> labels) {
        try (FileWriter fr = new FileWriter(fileName)) {
            try (BufferedWriter br = new BufferedWriter(fr)) {
                for (int i = 0; i < results.size(); i++) {
                    br.write(labels.get(i) + "," + String.join(",", results.get(i)));
                    br.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
