package algorithms;

import java.io.*;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.TreeMap;

public class SolutionsManager {
    protected TreeMap<String, ArrayList<String>> map = new TreeMap<>();
    private long timeCounter;
    private String m_inputFileName;
    protected ISolver solver;

    public SolutionsManager(String inputFileName, SolverType solverType) {
        solver = SolverType.getSolver(solverType, "00");
        setFileName(inputFileName);
    }

    public void setFileName(String inputFileName) {
        m_inputFileName = inputFileName == null ? "./src/in.txt" : inputFileName;
    }

    public void solveAll() {
        ResponseReader reader = new ResponseReader(m_inputFileName);
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = parseLine(line);
            if (parts == null)
                continue;
            solveAndMemorizeResult(parts[0], parts[1]);
        }
        reader.close();
    }

    private String[] parseLine(String line) {
        if (line == null)
            return null;
        String[] parts = line.split("[\\t\\s]+");

        if (parts.length < 2 || !parts[1].matches("[01]+"))
            return null;

        int responseLength = parts[1].length();
        boolean hasRightLength = (responseLength & (responseLength - 1)) == 0;
        if (!hasRightLength)
            return null;

        return parts;
    }

    public void printSpentTime() {
        System.out.printf("%1$TH:%1$TM:%1$TS:%1$TL%n", timeCounter - TimeZone.getDefault().getRawOffset());
    }

    public void writeSolutions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/out.txt"))) {
            for (var key : map.keySet()) {
                writer.write(String.format("%s = %s", key, map.get(key)));
                writer.newLine();
            }
            writer.write(String.format("%1$TH:%1$TM:%1$TS:%1$TL%n",
                    timeCounter - TimeZone.getDefault().getRawOffset()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void solveAndMemorizeResult(String name, String input) {
        prepare(input);
        var start = System.currentTimeMillis();
        solve(input);
        var difference = System.currentTimeMillis() - start;
        timeCounter += difference;

        memorizeResult(name);
    }

    protected void prepare(String input) {
    }

    private void solve(String input) {
        solver.getNewEntry(input);
        solver.calculateSpectrum();
    }

    private void memorizeResult(String name) {
        var res = solver.getResult();
        if (!map.containsKey(res))
            map.put(res, new ArrayList<>());
        map.get(res).add(name);
    }
}
