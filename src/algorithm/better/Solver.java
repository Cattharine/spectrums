package algorithm.better;

import algorithm.ISolver;
import algorithm.better.instances.*;

import java.util.ArrayList;

public class Solver implements ISolver {
    private boolean toPrint = true;
    private int[] spectrum;
    private int n;
    private int k;
    private ArrayList<Vertex> vertexes;
    private KFace[][] processing;

    public Solver(String table) {
        getNewEnter(table);
    }

    public String getResult() {
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
            var level = processing[k];
            var index = -1;
            if (level[level.length - 1] != null) {
                index = level.length - 1;
            }
            else if (level[0] != null) {
                index = 0;
            }
            else {
                k--;
                continue;
            }
            var face = level[index];
            process(face);
            var faces = face.fixate();
            updateLevel(faces, index);
        }
    }

    private void process(KFace face) {
        if (!face.isProcessed()) {
            var mu = face.getMaxMu();
            if (spectrum[n - k] < mu)
                spectrum[n - k] = mu;
            if (toPrint)
                System.out.println(face);
        }
    }

    private void updateLevel(KFace[] faces, int index) {
        if (faces != null) {
            k++;
            processing[k] = faces;
        }
        else {
            processing[k][index] = null;
        }
    }

    private void setUpProcessing() {
        for (var i = 1; i <= n; i++) {
            processing[i] = new KFace[2];
        }

        processing[0] = new KFace[1];
        var name = new StringBuilder("0".repeat(n));

        processing[0][0] = new KFace(n, vertexes, n - 1, -1, name);
    }

    public void getNewEnter(String table) {
        k = 0;
        n = (int) (Math.log(table.length()) / Math.log(2));
        Vertex.setupBinToDist(table.length());
        vertexes = new ArrayList<>(table.length());
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                vertexes.add(new Vertex(i, n));
            }
        }
        KFace.setN(n);
        spectrum = new int[n + 1];
        processing = new KFace[n + 1][];
        setUpProcessing();
    }

    public void setPrintingState(boolean state) {
        toPrint = state;
    }
}
