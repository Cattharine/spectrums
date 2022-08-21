package straightforward;

import java.io.*;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.TreeMap;

public class SFStarter {
    public static TreeMap<String, ArrayList<String>> map = new TreeMap<>();
    private static long timeCounter;

    public static void main(String[] args) {
        startSolving();
        System.out.printf("%1$TH:%1$TM:%1$TS:%1$TL%n", timeCounter - TimeZone.getDefault().getRawOffset());
        writeSortedMap();
    }

    private static void startSolving() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader("./src/in.txt"))) {
                String line = reader.readLine();
                while (line != null) {
                    var parts = line.split("[\\t\\s]+");
                    if (checkLine(parts))
                        solve(parts[0], parts[1]);
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkLine(String[] parts) {
        if (parts.length < 2 || !parts[1].matches("[01]+"))
            return false;
        var checkNum = (int) Math.pow(2, (int) (Math.log(parts[1].length()) / Math.log(2)));
        return checkNum == parts[1].length();
    }

    private static void writeSortedMap() {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/out.txt"))) {
                for (var key: map.keySet()) {
                    writer.write(String.format("%s = %s", key, map.get(key)));
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solve(String name, String input) {
        var start = System.currentTimeMillis();
        var solver = new SFSolver(input);
        solver.calculateSpectrum();
        var end = System.currentTimeMillis() - start;
        timeCounter += end;
        var res = solver.toString();
        if (map.containsKey(res))
            map.get(res).add(name);
        else map.put(res, new ArrayList<>() {
            {
                add(name);
            }
        });
    }
}
