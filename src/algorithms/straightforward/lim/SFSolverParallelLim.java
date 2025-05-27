package algorithms.straightforward.lim;

import algorithms.straightforward.parallel.SFSolverParallel;

import java.util.ArrayList;

public class SFSolverParallelLim extends SFSolverParallel {
    public SFSolverParallelLim(String entry) {
        super(entry);
    }

    @Override
    protected int getMaxMu(int dimension, ArrayList<Integer> vertexes) {
        var max = 0;
        for (int vertex : vertexes) {
            var phi = SFSolverLim.SFVertexLim.getMinMu(vertex, vertexes, dimension);
            if (phi > max)
                max = phi;
        }

        return max;
    }
}
