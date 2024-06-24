package algorithms.treeSolver;

import algorithms.ISolver;
import algorithms.Solver;
import algorithms.treeSolver.instances.*;

import java.util.ArrayList;

public class TreeSolver extends Solver implements ISolver {
    protected int k;
    protected ArrayList<Vertex> vertexes;
    protected KFace[][] processing;
    protected VertexCreator creator = new VertexCreator();

    public TreeSolver(String table) {
        getNewEntry(table);
    }

    public void calculateSpectrum() {
        while (processing[0][0] != null) {
            var level = processing[k];
            var index = -1;
            if (level[level.length - 1] != null) {
                index = level.length - 1;
            } else if (level[0] != null) {
                index = 0;
            } else {
                k--;
                continue;
            }
            var face = level[index];
            process(face);
            var faces = face.fixate();
            updateLevel(faces, index);
        }
    }

    protected void process(KFace face) {
        if (face.isNotProcessed()) {
            var mu = face.getMaxMu();
            if (spectrum[n - k] < mu)
                spectrum[n - k] = mu;
        }
    }

    private void updateLevel(KFace[] faces, int index) {
        if (faces != null) {
            k++;
            processing[k] = faces;
        } else {
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

    public void getNewEntry(String table) {
        k = 0;
        super.getNewEntry(table);
        Vertex.setupBinToDist(table.length());
        vertexes = new ArrayList<>(table.length());
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                vertexes.add(creator.create(i, n));
            }
        }
        KFace.setN(n);
        spectrum = new int[n + 1];
        processing = new KFace[n + 1][];
        setUpProcessing();
    }

    public void setPrintingState(boolean state) {
    }
}
