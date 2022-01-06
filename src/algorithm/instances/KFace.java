package algorithm.instances;

import java.util.ArrayList;

public class KFace {
    private final int fullDimension;
    private final int currentDimension;
    private final int currentPos;
    private KFace first;
    private KFace second;
    private final ArrayList<Vertex> vertexes;

    public KFace(int currentDimension, ArrayList<Vertex> vertexes, int currentPos, int fullDimension) {
        this.currentDimension = currentDimension;
        this.vertexes = vertexes;
        this.currentPos = currentPos;
        this.fullDimension = fullDimension;
    }

    public int getPhi() {
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinPhi(vertexes, currentDimension);
            if (phi > max)
                max = phi;
        }
        return max;
    }

    public void divideByFactor() {
        var sep = separateVertexes(currentPos);
        first = new KFace(currentDimension, sep[0], currentPos, fullDimension);
        second = new KFace(currentDimension, sep[1], currentPos, fullDimension);
    }

    public int getMaxPhi() {
        var f = first.getPhi();
        var m = second.getPhi();
        return Math.max(f, m);
    }

    public ArrayList<KFace> divide() {
        if (currentPos + 1 == fullDimension)
            return null;
        var faces = new ArrayList<KFace>();
        if (fullDimension == currentDimension)
            for (var i = currentPos + 1; i < fullDimension; i++) {
                faces.add(new KFace(currentDimension - 1, vertexes, i, fullDimension));
            }
        else {
            for (var i = currentPos + 1; i < fullDimension; i++) {
                faces.add(new KFace(currentDimension - 1, first.vertexes, i, fullDimension));
                faces.add(new KFace(currentDimension - 1, second.vertexes, i, fullDimension));
            }
        }

        return faces;
    }

    private ArrayList[] separateVertexes(int factor) {
        var first = new ArrayList<Vertex>();
        var second = new ArrayList<Vertex>();

        for (var vertex : vertexes) {
            if (vertex.getCharAt(factor) == '1')
                first.add(vertex);
            else second.add(vertex);
        }

        return new ArrayList[] {first, second};
    }

    public int getCurrentDimension() {
        return currentDimension;
    }

    public boolean isEmpty() {
        return vertexes.isEmpty();
    }
}
