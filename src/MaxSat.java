import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MaxSat {
    private int numClauses;
    private int numVars;
    private ArrayList<MaxSatClause> clauses;

    public MaxSat(int numClauses, int numVars) {
        this.clauses = new ArrayList<>();
        this.numClauses = numClauses;
        this.numVars = numVars;
    }

    public int getNumClauses() {
        return numClauses;
    }

    public int getNumVars() {
        return numVars;
    }

    public ArrayList<MaxSatClause> getClauses() {
        return clauses;
    }

    public void loadClauses(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("c")) {
                        continue;

                    } else if (line.startsWith("p")) {
                        String[] splitLine = line.split(" ");
                        this.numVars = Integer.parseInt(splitLine[2]);
                        this.numClauses = Integer.parseInt(splitLine[3]);
                    } else {
                        this.clauses.add(new MaxSatClause(line));
                    }
                    MaxSatClause clause = new MaxSatClause(line);
                    this.clauses.add(clause);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int countSatClauses(String assignment) {
        int total = 0;
        for (MaxSatClause clause : this.clauses) {
            total += clause.checkSat(assignment);
        }
        return total;
    }

    public static class MaxSatClause {
        private ArrayList<Integer> variables;
        private ArrayList<Integer> absoluteVariables;

        public MaxSatClause(String line) {
            this.variables = new ArrayList<>();
            this.absoluteVariables = new ArrayList<>();

            String[] splitLine = line.split(" ");
            for (int i = 1; i < splitLine.length - 1; i++) {
                int val = Integer.parseInt(splitLine[i]);
                this.variables.add(val);
                this.absoluteVariables.add(Math.abs(Math.abs(val)));
            }
        }

        public int checkSat(String assignment) {
            for (int i = 0; i < this.variables.size(); i++) {
                int cl_idx = this.absoluteVariables.get(i) - 1;
                if (this.variables.get(i) > 0 && assignment.charAt(cl_idx) == '1') {
                    return 1;
                } else if (this.variables.get(i) > 0 && assignment.charAt(cl_idx) == '0') {
                    return 1;
                }
            }
            return 0;
        }
    }
}
