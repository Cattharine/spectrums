package algorithms.fast;

import algorithms.Solver;

import java.util.ArrayList;
import java.util.Arrays;

public class FastSolver extends Solver {
    protected boolean[] supportChecker;
    protected ArrayList<OrderedVertex>[] orderedCube;

    public FastSolver(String table) {
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        spectrum = new int[n + 1];

        for (var current = 0; current < supportChecker.length; current++) {
            if (supportChecker[current]) {
                preprocess(current);
                processCurrent(current);
            }
        }
    }

    protected void processCurrent(int current) {
        for (var level = 1; level < n + 1; level++) {
            for (var vertex : orderedCube[level]) {
                processVertex(vertex, level);
            }
        }
    }

    protected void processVertex(OrderedVertex vertex, int level) {
        int mu = vertex.calculateMu(level);
        if (mu > spectrum[level])
            spectrum[level] = mu;
    }

    protected void preprocess(int current) {
        for (var level = 0; level < n + 1; level++) {
            for (var vertex : orderedCube[level]) {
                vertex.setState(supportChecker[vertex.value ^ current]);
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

        fillOrderedCube(1 << n);
    }

    private void fillOrderedCube(int numberOfVertexes) {
        var indexes = new int[numberOfVertexes];
        Arrays.fill(indexes, -1);

        var factors = getFactors();
        orderedCube[n] = new ArrayList<>();
        orderedCube[n].add(new OrderedVertex(numberOfVertexes - 1));
        indexes[numberOfVertexes - 1] = 0;

        for (var level = n - 1; level > -1; level--) {
            fillLevelOfOrderedCube(indexes, factors, level);
        }
    }

    private int[] getFactors() {
        var axes = new int[n];

        for (var factor = 0; factor < n; factor++) {
            axes[factor] = 1 << factor;
        }

        return axes;
    }

    private void fillLevelOfOrderedCube(int[] indexes, int[] factors, int level) {
        orderedCube[level] = new ArrayList<>();
        for (var covering : orderedCube[level + 1]) {
            for (var factor : factors) {
                fillVertexOfOrderedCube(indexes, factor, level, covering);
            }
        }
    }

    private void fillVertexOfOrderedCube(int[] indexes, int factor, int level, OrderedVertex covering) {
        var nextValue = covering.value ^ factor;
        if (nextValue > covering.value)
            return;
        if (indexes[nextValue] == -1) {
            indexes[nextValue] = orderedCube[level].size();
            orderedCube[level].add(new OrderedVertex(nextValue));
        }
        covering.addCovered(orderedCube[level].get(indexes[nextValue]));
    }

    protected void setSupportChecker(String table) {
        supportChecker = new boolean[table.length()];
        for (var vertex = 0; vertex < supportChecker.length; vertex++) {
            if (table.charAt(vertex) == '1') {
                supportChecker[vertex] = true;
            }
        }
    }
}
