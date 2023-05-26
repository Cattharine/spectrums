package algorithm.straightforwardV2.instances;

import java.util.ArrayList;

public class SF2Vertex {
    public static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }

    public static int getMinMu(int current, ArrayList<Integer> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (current == vertex)
                continue;
            var xor = current ^ vertex;
            var distance = convertBinToDist(xor);
            if (distance < min)
                min = distance;
        }
        return min - 1;
    }
}