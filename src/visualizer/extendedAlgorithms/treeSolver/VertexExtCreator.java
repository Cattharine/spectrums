package visualizer.extendedAlgorithms.treeSolver;

import algorithms.treeSolver.instances.Vertex;
import algorithms.treeSolver.instances.VertexCreator;

public class VertexExtCreator extends VertexCreator {
    @Override
    public Vertex create(int pos, int fullD) {
        return new VertexExt(pos, fullD);
    }
}
