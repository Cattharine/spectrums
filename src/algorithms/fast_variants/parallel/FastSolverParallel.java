package algorithms.fast_variants.parallel;

import algorithms.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FastSolverParallel extends Solver {
    protected boolean[] supportChecker;
    protected ArrayList<OrderedVertex>[] orderedCube;
    private volatile int[] currentFinalSpectrum;
    private final int numberOfThreads;
    private ExecutorService service;
    private volatile CountDownLatch latch;

    public FastSolverParallel(String table) {
        numberOfThreads = Runtime.getRuntime().availableProcessors();
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        currentFinalSpectrum = new int[n + 1];

        latch = new CountDownLatch(numberOfThreads);
        for (int task = 0; task < numberOfThreads; task++) {
            service.submit(new CurrentProcessor(task));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
        spectrum = currentFinalSpectrum;
    }

    protected void calculateCurrentSpectrum(int current, int[] currentSpectrum) {
        int[] muValues = new int[supportChecker.length];
        for (var level = 1; level < n + 1; level++) {
            for (var vertex : orderedCube[level]) {
                int mu = vertex.calculateMu(muValues, level, supportChecker[current ^ vertex.value]);
                muValues[vertex.value] = mu;
                if (mu > currentSpectrum[level])
                    currentSpectrum[level] = mu;
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
        service = Executors.newFixedThreadPool(numberOfThreads);
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
        covering.covered.add(nextValue);
    }

    protected void setSupportChecker(String table) {
        supportChecker = new boolean[table.length()];
        for (var vertex = 0; vertex < supportChecker.length; vertex++) {
            if (table.charAt(vertex) == '1') {
                supportChecker[vertex] = true;
            }
        }
    }

    protected class CurrentProcessor implements Runnable {
        private final int startValue;

        protected CurrentProcessor(int value) {
            startValue = value;
        }

        @Override
        public void run() {
            int[] currentSpectrum = new int[n + 1];
            for (var current = startValue; current < supportChecker.length; current += numberOfThreads) {
                if (supportChecker[current]) {
                    calculateCurrentSpectrum(current, currentSpectrum);
                }
            }
            synchronized (FastSolverParallel.this) {
                for (int dim = 0; dim < currentSpectrum.length; dim++) {
                    if (currentSpectrum[dim] > currentFinalSpectrum[dim])
                        currentFinalSpectrum[dim] = currentSpectrum[dim];
                }
                latch.countDown();
            }
        }
    }

    protected static class OrderedVertex {
        protected final int value;
        protected final ArrayList<Integer> covered = new ArrayList<>();

        private OrderedVertex(int value) {
            this.value = value;
        }

        protected int calculateMu(int[] muValues, int level, boolean isActive) {
            var minOfCovered = calculateMinOfCovered(muValues);
            if (minOfCovered < level - 1 || isActive) {
                return minOfCovered;
            } else return level;
        }

        private int calculateMinOfCovered(int[] muValues) {
            var min = Integer.MAX_VALUE;
            for (var vertex : covered) {
                if (muValues[vertex] < min) {
                    min = muValues[vertex];
                }
            }
            return min;
        }
    }
}
