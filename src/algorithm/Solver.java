package algorithm;

import algorithm.instances.*;

import java.util.ArrayList;

public class Solver {
    private final int[] spectrum;
    private final int fullDimension;
    private final ArrayList<Vertex> vertexes;
    private final KFace[][] processing;
    private int currentK = 0;

    public Solver(String table) {
        fullDimension = (int) (Math.log(table.length()) / Math.log(2));
        Vertex.setupBinToDist(table.length());
        vertexes = new ArrayList<>(table.length());
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                vertexes.add(new Vertex(i, fullDimension));
            }
        }
        spectrum = new int[fullDimension + 1];
        processing = new KFace[fullDimension + 1][];
        initiateProcessing();
    }

    @Override
    public String toString() {
        var res = new StringBuilder();
        for (var i = 0; i < spectrum.length - 1; i++) {
            res.append(spectrum[i]);
            res.append(", ");
        }
        res.append(spectrum[spectrum.length - 1]);
        return res.toString();
    }

    public void calculateSpectrum() {
        while (processing[0][0] != null) {
            var currentNodes = processing[currentK];
            var index = -1;
            if (currentNodes.length == 1) {
                index = 0;
            }
            else if (currentNodes[1] != null) {
                index = 1;
            }
            else if (currentNodes[0] != null) {
                index = 0;
            }
            else {
                currentK--;
                continue;
            }
            var currentNode = currentNodes[index];
            if (!currentNode.isProcessed()) {
                var mu = currentNode.getMaxMu();
                if (spectrum[fullDimension - currentK] < mu)
                    spectrum[fullDimension - currentK] = mu;
            }
            var fixating = currentNode.fixate();
            if (fixating != null) {
                processing[currentK + 1] = fixating;
                currentK++;
            }
            else {
                currentNodes[index] = null;
            }
        }
    }

    private void initiateProcessing() {
        for (var i = 1; i <= fullDimension; i++) {
            processing[i] = new KFace[2];
        }

        processing[0] = new KFace[1];
        processing[0][0] = new KFace(fullDimension, vertexes, fullDimension - 1,
                -1, fullDimension);
    }
}
