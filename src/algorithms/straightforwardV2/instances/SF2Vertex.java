package algorithms.straightforwardV2.instances;

import java.util.ArrayList;

public class SF2Vertex {
    public static int[] binToDist;

    public static int getMinMu(int current, ArrayList<Integer> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (current == vertex)
                continue;
            var distance = binToDist[current ^ vertex];
            if (distance < min)
                min = distance;
        }
        return min - 1;
    }

    public static void setupBinToDist(int tableLength) {
        binToDist = new int[tableLength];
        for (var i = 0; i < tableLength; i++)
            SF2Vertex.binToDist[i] = convertBinToDist(i);
    }

    private static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }
}