package algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.TreeMap;

public class Starter {
    public static TreeMap<String, ArrayList<String>> map = new TreeMap<>();

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        startSolving();
        var end = System.currentTimeMillis() - start - TimeZone.getDefault().getRawOffset();
        System.out.printf("%1$TH:%1$TM:%1$TS:%1$TL%n", end);
        writeSortedMap();
    }

    private static void startSolving() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader("./src/in.txt"))) {
                String str = reader.readLine();
                var list = str.split("[\\t\\s]+");
                if (list.length > 1 && list[1].matches("[01]+"))
                    solve(str);
                while ((str = reader.readLine()) != null)
                    solve(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static void solve(String input) {
        var list = input.split("[\\t\\s]+");
        var res = new Solver(list[1]).solve();
        if (map.containsKey(res))
            map.get(res).add(list[0]);
        else map.put(res, new ArrayList<>() {
            {
                add(list[0]);
            }
        });
    }
}
