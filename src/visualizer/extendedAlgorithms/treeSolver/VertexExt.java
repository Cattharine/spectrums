package visualizer.extendedAlgorithms.treeSolver;

import algorithms.treeSolver.instances.Vertex;
import visualizer.extendedAlgorithms.MetaInfo;

import java.util.ArrayList;

public class VertexExt extends Vertex {
    public VertexExt(int pos, int fullD) {
        super(pos, fullD);
    }

    public int getMinMu(MetaInfo info, ArrayList<Vertex> vertexes, int currentDimension, int faceValue) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (this == vertex)
                continue;
            var distance = binToDist[pos ^ vertex.pos];
            if (distance < min) {
                min = distance;
                info.cube.clearFacePathsForVertex(faceValue, pos);
                info.cube.addFacePathForVertex(faceValue, pos, vertex.pos);
            }
            else if (distance == min) {
                info.cube.addFacePathForVertex(faceValue, pos, vertex.pos);
            }
        }
        return min - 1;
    }
}
