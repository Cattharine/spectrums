package algorithm.instances;

import java.util.ArrayList;

public class KFacePair {
    private final int fullDimension;
    private final int currentDimension;
    private final int currentPos;
    private KFace first;
    private KFace second;
    private final ArrayList<Vertex> vertexes;

    public KFacePair(int currentDimension, ArrayList<Vertex> vertexes, int currentPos, int fullDimension) {
        this.currentDimension = currentDimension;
        this.vertexes = vertexes;
        this.currentPos = currentPos;
        this.fullDimension = fullDimension;
    }

    public void divideByFactor() {
        var sep = separateVertexes(currentPos);
        first = new KFace(currentDimension, sep[0], currentPos, fullDimension);
        second = new KFace(currentDimension, sep[1], currentPos, fullDimension);
    }

    public int getMaxMu() {
        var firstValue = first.getMaxMu();
        var secondValue = second.getMaxMu();
        return Math.max(firstValue, secondValue);
    }

    public ArrayList<KFacePair> fixate() {
        var res = new ArrayList<KFacePair>(2 * currentDimension);
        var firstDivision = first.fixate(res);
        var secondDivision = second.fixate(res);
        if (firstDivision == null && secondDivision == null)
            return null;
        return res;
    }

    private ArrayList[] separateVertexes(int factor) {
        var first = new ArrayList<Vertex>(vertexes.size());
        var second = new ArrayList<Vertex>(vertexes.size());

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
