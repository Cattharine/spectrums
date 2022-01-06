package algorithm.instances;

import java.util.ArrayList;

public class KFace {
    private final int fullDimension;
    private final int currentDimension;
    private final int currentPos;
    private final ArrayList<Vertex> vertexes;

    public KFace (int currentDimension, ArrayList<Vertex> vertexes, int currentPos, int fullDimension) {
        this.currentDimension = currentDimension;
        this.vertexes = vertexes;
        this.currentPos = currentPos;
        this.fullDimension = fullDimension;
    }

    public int getMaxMu() {
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinMu(vertexes, currentDimension);
            if (phi > max)
                max = phi;
        }
        return max;
    }

    public ArrayList<KFacePair> fixate(ArrayList<KFacePair> faces) {
        if (currentPos + 1 == fullDimension)
            return null;
        for (var i = currentPos + 1; i < fullDimension; i++) {
            faces.add(new KFacePair(currentDimension - 1, vertexes, i, fullDimension));
        }

        return faces;
    }
}
