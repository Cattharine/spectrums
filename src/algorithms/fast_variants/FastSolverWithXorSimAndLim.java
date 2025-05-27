package algorithms.fast_variants;

import algorithms.fast.OrderedVertex;

public class FastSolverWithXorSimAndLim extends FastSolverWithXorSim {
    private int limitation;

    public FastSolverWithXorSimAndLim(String table) {
        super(table);
    }

    @Override
    protected void processVertex(OrderedVertex vertex, int level) {
        if ((vertex.value & limitation) > 0)
            return;
        int mu = vertex.calculateMu(level);
        if (mu > spectrum[level])
            spectrum[level] = mu;
    }

    @Override
    protected void preprocess(int current) {
        super.preprocess(current);
        limitation = 0;
        if (n == 0)
            return;
        for (var vertex : orderedCube[1]) {
            if (supportChecker[vertex.value ^ current]) {
                limitation ^= vertex.value;
            }
        }
    }
}
