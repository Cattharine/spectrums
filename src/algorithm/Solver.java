package algorithm;

import algorithm.instances.KFace;
import algorithm.instances.Vertex;
import java.util.ArrayList;
import java.util.Stack;

public class Solver {
    private final int[] spectrum;
    private final int fullDimension;
    private final ArrayList<Vertex> vertexes = new ArrayList<>();

    public Solver(String table) {
        fullDimension = (int) (Math.log(table.length()) / Math.log(2));
        Vertex.cleanDist();
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                vertexes.add(new Vertex(i, fullDimension));
            }
        }
        spectrum = new int[fullDimension + 1];
        calculateDistances();
    }

    private void calculateDistances() {
        for (var vertex : vertexes)
            vertex.calculateDistances(vertexes);
    }

    public String solve() {
        calculateSpectrum();
        var res = new StringBuilder();
        for (var i = 0; i < spectrum.length - 1; i++) {
            res.append(spectrum[i]);
            res.append(", ");
        }
        res.append(spectrum[spectrum.length - 1]);
        return res.toString();
    }

    private void calculateSpectrum() {
        var stack = new Stack<KFace>();
        var face = new KFace(fullDimension, vertexes, -1, fullDimension);
        var phi = face.getPhi();
        spectrum[fullDimension] = phi;
        var next = face.divide();
        if (next != null)
            stack.addAll(next);

        while (!stack.empty()) {
            face = stack.pop();
            if (face.isEmpty())
                continue;
            face.divideByFactor();
            phi = face.getMaxPhi();
            if (spectrum[face.getCurrentDimension()] < phi)
                spectrum[face.getCurrentDimension()] = phi;
            next = face.divide();
            if (next == null)
                continue;
            stack.addAll(next);
        }
    }
}
