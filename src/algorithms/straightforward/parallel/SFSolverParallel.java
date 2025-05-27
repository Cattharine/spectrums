package algorithms.straightforward.parallel;

import algorithms.straightforward.SFSolver;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SFSolverParallel extends SFSolver {
    private final int numberOfThreads;
    private ExecutorService executor;
    private volatile int[] currentFinalSpectrum;
    private volatile CountDownLatch latch;
    private volatile int faceCount;

    public SFSolverParallel(String table) {
        this.numberOfThreads = Runtime.getRuntime().availableProcessors();
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        currentFinalSpectrum = new int[n + 1];
        latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++)
            executor.submit(new FaceProcessor(i));

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        spectrum = currentFinalSpectrum;
    }

    protected void processFace(int faceNum, int[] currentSpectrum) {
        String faceName = getName(faceNum);
        ArrayList<Integer> freeFactors = getFreeFactorsInFace(faceName);
        ArrayList<Integer> vertexes = getVertexes(faceName, freeFactors);
        int dimension = freeFactors.size();
        int mu = getMaxMu(dimension, vertexes);

        if (mu > currentSpectrum[dimension])
            currentSpectrum[dimension] = mu;
    }

    @Override
    public void getNewEntry(String table) {
        super.getNewEntry(table);
        executor = Executors.newFixedThreadPool(numberOfThreads);
        faceCount = (int) Math.pow(3, n);
    }

    private class FaceProcessor implements Runnable {
        private final int startValue;

        private FaceProcessor(int value) {
            startValue = value;
        }

        @Override
        public void run() {
            int[] currentSpectrum = new int[n + 1];
            for (int faceNum = startValue; faceNum < faceCount; faceNum += numberOfThreads) {
                processFace(faceNum, currentSpectrum);
            }
            synchronized (SFSolverParallel.this) {
                for (int dim = 0; dim < currentSpectrum.length; dim++) {
                    if (currentSpectrum[dim] > currentFinalSpectrum[dim])
                        currentFinalSpectrum[dim] = currentSpectrum[dim];
                }
                latch.countDown();
            }
        }
    }
}
