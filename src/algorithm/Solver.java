package algorithm;

import algorithm.instances.KFace;
import algorithm.instances.KFacePair;
import algorithm.instances.Vertex;
import java.util.ArrayList;
import java.util.Stack;

public class Solver {
    private final int[] spectrum;
    private final int fullDimension;
    private final ArrayList<Vertex> vertexes = new ArrayList<>();

    public Solver(String table) {
        fullDimension = (int) (Math.log(table.length()) / Math.log(2));
        Vertex.setupBinToDist(table.length());
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                vertexes.add(new Vertex(i, fullDimension));
            }
        }
        spectrum = new int[fullDimension + 1];
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
        var stack = new Stack<KFacePair>();
        var face = new KFace(fullDimension, vertexes, -1, fullDimension);
        var phi = face.getMaxMu();
        spectrum[fullDimension] = phi;
        var next = new ArrayList<KFacePair>();
        next = face.fixate(next);
        if (next != null)
            stack.addAll(next);

        while (!stack.empty()) {
            var facePair = stack.pop();
            if (facePair.isEmpty())
                continue;
            facePair.divideByFactor();
            phi = facePair.getMaxMu();
            if (spectrum[facePair.getCurrentDimension()] < phi)
                spectrum[facePair.getCurrentDimension()] = phi;
            next = facePair.fixate();
            if (next == null)
                continue;
            stack.addAll(next);
        }
    }
}
