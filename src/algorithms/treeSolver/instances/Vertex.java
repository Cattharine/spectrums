package algorithms.treeSolver.instances;

import java.util.ArrayList;

public class Vertex {
    public static int[] binToDist;
    public final int pos;

    public Vertex(int pos, int fullD) {
        this.pos = pos;
    }

    public int getMinMu(ArrayList<Vertex> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (this.pos == vertex.pos)
                continue;
            var distance = binToDist[pos ^ vertex.pos];
            if (distance < min)
                min = distance;
        }
        return min - 1;
    }

    public static void setupBinToDist(int tableLength) {
        binToDist = new int[tableLength];
        for (var i = 0; i < tableLength; i++)
            Vertex.binToDist[i] = convertBinToDist(i);
    }

    private static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }
}
