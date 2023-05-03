package algorithm.visualizer;

import algorithm.visualizer.painter.Painter;
import algorithm.visualizer.solver.Solver;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class Starter extends JFrame {
    public static TreeMap<String, ArrayList<String>> map = new TreeMap<>();
    private static Painter painter;

    public Starter() {
        super("Boolean Cube Visualizer");
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        painter = new Painter();
        add(painter);
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
        var fullDimension = (int) (Math.log(input.length()) / Math.log(2));
        var num = painter.addContents(fullDimension);
        var solver = new Solver(input);
        painter.setSolver(num, solver);
        solver.setPrintingState(false);
        solver.calculateSpectrum();
        var res = solver.getResult();
        if (map.containsKey(res))
            map.get(res).add(name);
        else map.put(res, new ArrayList<>() {
            {
                add(name);
            }
        });
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

    public static void main(String[] args) {
        var frame = new Starter();
        frame.setVisible(true);
        startSolving();
        writeSortedMap();
    }
}