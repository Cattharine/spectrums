package algorithm.straightforward.instances;

import java.util.ArrayList;
import java.util.Objects;

public class SFVertex {
    private final String binary;
    private final int pos;

    public SFVertex(String binary, int pos) {
        this.binary = binary;
        this.pos = pos;
    }

    public int getMinMu(ArrayList<SFVertex> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (this == vertex)
                continue;
            var xor = vertex.pos ^ pos;
            var distance = convertBinToDist(xor);
            if (distance < min)
                min = distance;
        }
        return min - 1;
    }

    public static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SFVertex vertex = (SFVertex) o;
        return pos == vertex.pos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }

    @Override
    public String toString() {
        return binary;
    }
}
