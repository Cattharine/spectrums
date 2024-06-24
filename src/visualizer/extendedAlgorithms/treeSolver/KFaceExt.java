package visualizer.extendedAlgorithms.treeSolver;

import algorithms.treeSolver.instances.KFace;
import algorithms.treeSolver.instances.Vertex;
import visualizer.extendedAlgorithms.MetaInfo;

import java.util.ArrayList;

public class KFaceExt extends KFace {
    public final StringBuilder name;

    public KFaceExt(int faceDimension, ArrayList<Vertex> vertexes, int toFixate, int spliterator, StringBuilder name) {
        super(faceDimension, vertexes, toFixate, spliterator, name);
        this.name = name;
    }

    public int getMaxMu(MetaInfo info, int faceValue) {
        isProcessed = true;
        var max = 0;
        for (var vertex : vertexes) {
            var phi = ((VertexExt) vertex).getMinMu(info, vertexes, faceDimension, faceValue);
            if (phi > max) {
                max = phi;
                info.cube.clearMaxMuFace(faceValue);
                info.cube.addMaxMuVertex(faceValue, vertex.pos);
            } else if (phi == max && phi != 0)
                info.cube.addMaxMuVertex(faceValue, vertex.pos);
        }

        return max;
    }

    @Override
    protected KFace faceCreator(ArrayList<Vertex> vertices, String symbol) {
        var usualFace = super.faceCreator(vertices, symbol);
        return new KFaceExt(usualFace.faceDimension, vertices, n - 1, usualFace.spliterator,
                new StringBuilder(name).replace(toFixate, toFixate + 1, symbol));
    }

    public static int getValueByName(String faceName) {
        return Integer.parseInt(faceName, 3);
    }

    public StringBuilder getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
