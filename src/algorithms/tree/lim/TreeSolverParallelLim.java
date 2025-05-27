package algorithms.tree.lim;

import algorithms.tree.parallel.TreeSolverParallel;

import java.util.ArrayList;

public class TreeSolverParallelLim extends TreeSolverParallel {
    public TreeSolverParallelLim(String entry) {
        super(entry);
    }

    @Override
    protected ArrayList<Vertex> getVertexes(String table) {
        var vertexes = new ArrayList<Vertex>(table.length());
        for (var value = 0; value < table.length(); value++) {
            if (table.charAt(value) == '1') {
                vertexes.add(new VertexLim(value));
            }
        }
        return vertexes;
    }

    public static class VertexLim extends Vertex {
        private VertexLim(int vertex) {
            super(vertex);
        }

        @Override
        protected int getMinMu(ArrayList<Vertex> vertexes, int currentDimension) {
            var min = currentDimension + 1;
            for (var vertex : vertexes) {
                if (value == vertex.value)
                    continue;
                var distance = binToDist[value ^ vertex.value];
                if (distance < min)
                    min = distance;
                if (min == 1)
                    break;
            }
            return min - 1;
        }
    }
}
