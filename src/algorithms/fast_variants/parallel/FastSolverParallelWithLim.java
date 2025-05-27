package algorithms.fast_variants.parallel;

public class FastSolverParallelWithLim extends FastSolverParallel {
    public FastSolverParallelWithLim(String table) {
        super(table);
    }

    @Override
    protected void calculateCurrentSpectrum(int current, int[] currentSpectrum) {
        int limitation = prepare(current);
        int[] muValues = new int[supportChecker.length];
        for (var level = 1; level < n + 1; level++) {
            for (var vertex : orderedCube[level]) {
                if ((vertex.value & limitation) > 0)
                    continue;
                int mu = vertex.calculateMu(muValues, level, supportChecker[current ^ vertex.value]);
                muValues[vertex.value] = mu;
                if (mu > currentSpectrum[level])
                    currentSpectrum[level] = mu;
            }
        }
    }

    private int prepare(int current) {
        int limitation = 0;
        if (n == 0)
            return limitation;
        for (var vertex : orderedCube[1]) {
            if (supportChecker[vertex.value ^ current]) {
                limitation ^= vertex.value;
            }
        }
        return limitation;
    }
}
