package algorithm.instances;

import java.util.ArrayList;

public class KFace {
    private static int n;
    private final int k;
    private final int t;
    private int toFixate;
    private final ArrayList<Vertex> vertexes;
    private final StringBuilder name;
    private boolean wasPrinted = false;

    public KFace(int k, ArrayList<Vertex> vertexes, int toFixate, int t, StringBuilder name) {
        this.name = name;
        this.k = k;
        this.vertexes = vertexes;
        this.toFixate = toFixate;
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
        if (!wasPrinted) {
            System.out.println(name.toString());
            wasPrinted = true;
        }
        if (t == toFixate)
            return null;
        var res = new KFace[2];
        var separatedVertexes = separateVertexes();
        res[0] = faceCreator(separatedVertexes.getFirst(), "2");
        res[1] = faceCreator(separatedVertexes.getSecond(), "1");

        toFixate--;
        return res;
    }

    private KFace faceCreator(ArrayList<Vertex> vertices, String symbol) {
        return new KFace(k - 1, vertices, n - 1,
                toFixate, new StringBuilder(name).replace(toFixate, toFixate + 1, symbol));
    }

    private Pair separateVertexes() {
        var first = new ArrayList<Vertex>(vertexes.size());
        var second = new ArrayList<Vertex>(vertexes.size());

        for (var vertex : vertexes) {
            if (vertex.getCharAt(toFixate) == '1')
                first.add(vertex);
            else second.add(vertex);
        }

        return new Pair(first, second);
    }

    public static void setN(int value) {
        n = value;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
