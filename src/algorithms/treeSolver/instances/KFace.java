package algorithms.treeSolver.instances;

import java.util.ArrayList;

public class KFace {
    protected static int n;
    public final int faceDimension;
    public final int spliterator;
    protected int toFixate;
    protected final ArrayList<Vertex> vertexes;
    protected boolean isProcessed = false;

    public KFace(int faceDimension, ArrayList<Vertex> vertexes, int toFixate, int spliterator, StringBuilder name) {
        this.faceDimension = faceDimension;
        this.vertexes = vertexes;
        this.toFixate = toFixate;
        this.spliterator = spliterator;
    }

    public int getMaxMu() {
        isProcessed = true;
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinMu(vertexes, faceDimension);
            if (phi > max)
                max = phi;
        }

        return max;
    }

    public KFace[] fixate() {
        if (spliterator == toFixate)
            return null;
        var res = new KFace[2];
        var separatedVertexes = separateVertexes();
        res[0] = faceCreator(separatedVertexes.getFirst(), "2");
        res[1] = faceCreator(separatedVertexes.getSecond(), "1");

        toFixate--;
        return res;
    }

    protected KFace faceCreator(ArrayList<Vertex> vertices, String symbol) {
        return new KFace(faceDimension - 1, vertices, n - 1, toFixate, new StringBuilder());
    }

    private Pair separateVertexes() {
        var first = new ArrayList<Vertex>(1 << (faceDimension - 1));
        var second = new ArrayList<Vertex>(1 << (faceDimension - 1));
        int checker = 1 << (n - toFixate - 1);

        for (var vertex : vertexes) {
            if ((vertex.pos & checker) > 0)
                first.add(vertex);
            else second.add(vertex);
        }

        return new Pair(first, second);
    }

    public static void setN(int value) {
        n = value;
    }

    public boolean isNotProcessed() {
        return !isProcessed;
    }
}
