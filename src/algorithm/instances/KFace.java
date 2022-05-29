package algorithm.instances;

import java.util.ArrayList;

public class KFace {
    private final int fullDimension;
    private final int currentDimension;
    private final int currentK;
    private int currentFixate;
    private boolean isProcessed = false;
    private final ArrayList<Vertex> vertexes;

    public KFace(int currentDimension, ArrayList<Vertex> vertexes, int currentFixate,
                 int currentK, int fullDimension) {
        this.currentDimension = currentDimension;
        this.vertexes = vertexes;
        this.currentFixate = currentFixate;
        this.fullDimension = fullDimension;
        this.currentK = currentK;
    }

    public int getMaxMu() {
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinMu(vertexes, currentDimension);
            if (phi > max)
                max = phi;
        }

        isProcessed = true;
        return max;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public KFace[] fixate() {
        if (currentK == currentFixate)
            return null;
        var res = new KFace[2];
        var separatedVertexes = separateVertexes();
        res[0] = new KFace(currentDimension - 1, separatedVertexes.getFirst(),
                fullDimension - 1, currentFixate, fullDimension);
        res[1] = new KFace(currentDimension - 1, separatedVertexes.getSecond(),
                fullDimension - 1, currentFixate, fullDimension);

        currentFixate--;
        return res;
    }

    private Pair separateVertexes() {
        var first = new ArrayList<Vertex>(vertexes.size());
        var second = new ArrayList<Vertex>(vertexes.size());

        for (var vertex : vertexes) {
            if (vertex.getCharAt(currentFixate) == '1')
                first.add(vertex);
            else second.add(vertex);
        }

        return new Pair(first, second);
    }
}
