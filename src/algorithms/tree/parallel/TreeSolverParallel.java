package algorithms.tree.parallel;

import algorithms.tree.TreeSolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TreeSolverParallel extends TreeSolver {
    private volatile int[] currentSpectrum;
    private final int numberOfThreads;
    private ExecutorService executor;

    public TreeSolverParallel(String table) {
        numberOfThreads = Runtime.getRuntime().availableProcessors();
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        currentSpectrum = new int[n + 1];
        super.calculateSpectrum();

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        spectrum = currentSpectrum;
    }

    @Override
    protected void process(KFace face) {
        executor.submit(new FaceProcessor(face));
    }

    @Override
    public void getNewEntry(String table) {
        super.getNewEntry(table);
        executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    private class FaceProcessor implements Runnable {
        private final KFace face;

        private FaceProcessor(KFace current) {
            face = current;
        }

        @Override
        public void run() {
            synchronized (face) {
                if (face.isNotProcessed()) {
                    var mu = face.getMaxMu();
                    synchronized (TreeSolverParallel.this) {
                        if (currentSpectrum[face.faceDimension] < mu)
                            currentSpectrum[face.faceDimension] = mu;
                    }
                }
            }
        }
    }
}
