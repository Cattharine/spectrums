package algorithms.fast;

import algorithms.ISolver;
import algorithms.Solver;

import java.util.ArrayList;
import java.util.Arrays;

public class FastSolver extends Solver implements ISolver {
    private int[] supportChecker;
    private ArrayList<OrderedVertex>[] orderedCube;

    public FastSolver(String table) {
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        spectrum = new int[n + 1];

        for (var current = 0; current < supportChecker.length; current++) {
            if (supportChecker[current] == 1) {
                calculateCurrentSpectrum(current);
            }
        }
    }

    private void calculateCurrentSpectrum(int current) {
        preprocess(current);
        var currentSpectrum = new int[n + 1];
        for (var k = 1; k < n + 1; k++) {
            for (var vertex : orderedCube[k]) {
                int m = vertex.calculateM(k);
                if (m > currentSpectrum[k])
                    currentSpectrum[k] = m;
            }
            if (currentSpectrum[k] > spectrum[k])
                spectrum[k] = currentSpectrum[k];
        }
    }

    private void preprocess(int current) {
        for (var k = 0; k < n + 1; k++) {
            for (var vertex : orderedCube[k]) {
                vertex.setL(supportChecker[vertex.getValue() ^ current]);
            }
        }
    }

    @Override
    public void getNewEntry(String table) {
        var currentN = n;
        super.getNewEntry(table);
        if (currentN != n)
            prepare();
        setSupportChecker(table);
    }

    private void prepare() {
        orderedCube = new ArrayList[n + 1];

        var numberOfVertexes = 1 << n;
        var indexes = new int[numberOfVertexes];
        Arrays.fill(indexes, -1);

        fillOrderedCube(indexes, numberOfVertexes);
    }

    private void fillOrderedCube(int[] indexes, int numberOfVertexes) {
        var factors = getAxes();
        orderedCube[n] = new ArrayList<>();
        orderedCube[n].add(new OrderedVertex(numberOfVertexes - 1, n));
        indexes[numberOfVertexes - 1] = 0;

        for (var k = n - 1; k > -1; k--) {
            fillLevelOfOrderedCube(indexes, factors, k);
        }
    }

    private int[] getAxes() {
        var axes = new int[n];

        for (var k = 0; k < n; k++) {
            axes[k] = 1 << k;
        }

        return axes;
    }


    private void fillLevelOfOrderedCube(int[] indexes, int[] factors, int k) {
        orderedCube[k] = new ArrayList<>();
        for (var covering : orderedCube[k + 1]) {
            for (var factor : factors) {
                var nextValue = covering.getValue() ^ factor;
                if (nextValue < covering.getValue()) {
                    coverVertex(nextValue, covering, indexes, k);
                }
            }
        }
    }

    private void coverVertex(int coveredValue, OrderedVertex covering, int[] indexes, int k) {
        if (indexes[coveredValue] == -1) {
            indexes[coveredValue] = orderedCube[k].size();
            orderedCube[k].add(new OrderedVertex(coveredValue, n));
        }
        covering.addCovered(orderedCube[k].get(indexes[coveredValue]));
    }

    private void setSupportChecker(String table) {
        supportChecker = new int[1 << n];
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1') {
                supportChecker[i] = 1;
            }
        }
    }

    @Override
    public void setPrintingState(boolean state) {
    }
}
