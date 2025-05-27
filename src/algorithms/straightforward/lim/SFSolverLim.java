package algorithms.straightforward.lim;

import algorithms.straightforward.SFSolver;

import java.util.ArrayList;

public class SFSolverLim extends SFSolver {
    public SFSolverLim(String entry) {
        super(entry);
    }

    @Override
    protected int getMaxMu(int dimension, ArrayList<Integer> vertexes) {
        var max = 0;
        for (int vertex : vertexes) {
            var phi = SFVertexLim.getMinMu(vertex, vertexes, dimension);
            if (phi > max)
                max = phi;
        }

        return max;
    }

    protected static class SFVertexLim extends SFVertex {
        protected static int getMinMu(int current, ArrayList<Integer> vertexes, int currentDimension) {
            var min = currentDimension + 1;
            for (var vertex : vertexes) {
                if (current == vertex)
                    continue;
                var distance = binToDist[current ^ vertex];
                if (distance < min)
                    min = distance;
                if (min == 1)
                    break;
            }
            return min - 1;
        }
    }
}
