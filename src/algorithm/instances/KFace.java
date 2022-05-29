package algorithm.instances;

import java.util.ArrayList;

public class KFace {
    private static int n;
    private final int k;
    private final int t;
    private int currentFixating;
    private final ArrayList<Vertex> vertexes;

    public KFace(int k, ArrayList<Vertex> vertexes, int currentFixating, int t) {
        this.k = k;
        this.vertexes = vertexes;
        this.currentFixating = currentFixating;
        this.t = t;
    }

    public int getMaxMu() {
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinMu(vertexes, k);
            if (phi > max)
                max = phi;
        }

        return max;
    }

    public KFace[] fixate() {
        if (t == currentFixating)
            return null;
        var res = new KFace[2];
        var separatedVertexes = separateVertexes();
        res[0] = new KFace(k - 1, separatedVertexes.getFirst(), n - 1, currentFixating);
        res[1] = new KFace(k - 1, separatedVertexes.getSecond(), n - 1, currentFixating);

        currentFixating--;
        return res;
    }

    private Pair separateVertexes() {
        var first = new ArrayList<Vertex>(vertexes.size());
        var second = new ArrayList<Vertex>(vertexes.size());

        for (var vertex : vertexes) {
            if (vertex.getCharAt(currentFixating) == '1')
                first.add(vertex);
            else second.add(vertex);
        }

        return new Pair(first, second);
    }

    public static void setN(int value) {
        n = value;
    }
}
