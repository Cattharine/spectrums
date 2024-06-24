package algorithms;

import java.io.*;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.TreeMap;

public class SolutionsManager {
    public TreeMap<String, ArrayList<String>> map = new TreeMap<>();
    private long timeCounter;
    private BufferedReader reader;

    public SolutionsManager(String inputFileName) {
        openInputFile(inputFileName);
    }

    public InputStates solveNext(ISolver solver) {
        String line;
        try {
            line = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return InputStates.DONE;
        }
        if (line == null)
            return InputStates.DONE;
        String[] parts = line.split("[\\t\\s]+");
        if (!checkLine(parts))
            return InputStates.NEXT;
        solve(solver, parts[0], parts[1]);
        return InputStates.OK;
    }

    private boolean checkLine(String[] parts) {
        if (parts.length < 2 || !parts[1].matches("[01]+"))
            return false;
        var checkNum = (int) Math.pow(2, (int) (Math.log(parts[1].length()) / Math.log(2)));
        return checkNum == parts[1].length();
    }

    public void solveAll(ISolver solver) {
        InputStates hasNext = InputStates.NEXT;
        while (hasNext != InputStates.DONE) {
            hasNext = solveNext(solver);
        }
    }

    public void closeInputFile() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openInputFile(String inputFileName) {
        inputFileName = inputFileName == null ? "./src/in.txt" : inputFileName;
        if (reader != null)
            closeInputFile();
        try {
            reader = new BufferedReader(new FileReader(inputFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printSpentTime() {
        System.out.printf("%1$TH:%1$TM:%1$TS:%1$TL%n", timeCounter - TimeZone.getDefault().getRawOffset());
    }

    public void writeSolutions() {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/out.txt"))) {
                for (var key : map.keySet()) {
                    writer.write(String.format("%s = %s", key, map.get(key)));
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void solve(ISolver solver, String name, String input) {
        var start = System.currentTimeMillis();
        solver.getNewEntry(input);
        solver.setPrintingState(false);
        solver.calculateSpectrum();
        var difference = System.currentTimeMillis() - start;
        timeCounter += difference;
        var res = solver.getResult();
        if (map.containsKey(res))
            map.get(res).add(name);
        else map.put(res, new ArrayList<>() {
            {
                add(name);
            }
        });
    }
}
