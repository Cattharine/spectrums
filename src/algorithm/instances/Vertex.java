package algorithm.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

public class Vertex {
    private static HashMap<Integer, Integer> binToDist;
    private final String binary;
    private final int pos;
//    private final HashMap<Vertex, Integer> distances;

    public Vertex(int pos, int fullD) {
        this.pos = pos;
        var bin = Integer.toBinaryString(pos);
        var filler = "0".repeat(Math.max(0, fullD - bin.length()));
        this.binary = filler + bin;
//        this.distances = new HashMap<>();
    }

    public int getMinPhi(ArrayList<Vertex> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (this == vertex)
                continue;
            var distance = binToDist.get(vertex.pos ^ pos);
            if (distance < min)
                min = distance;
//            var distance = this.calculateDistance(vertex);
//            if (distance < min)
//                min = distance;
        }
        return min - 1;
    }

    public void calculateDistances(ArrayList<Vertex> vertexes) {
        for (var vertex : vertexes) {
            if (vertex == this)
                continue;
            var dist = vertex.pos ^ this.pos;
            if (binToDist.containsKey(dist))
                continue;
            binToDist.put(dist, convertBinToDist(dist));
        }
//        for (var vertex : vertexes) {
//            if (vertex == this)
//                continue;
//            var distance = 0;
//            if (vertex.distances.containsKey(this))
//                distance = vertex.distances.get(this);
//            else distance = calculateDistance(vertex);
//            this.distances.put(vertex, distance);
//        }
    }

    public int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }

    private int calculateDistance(Vertex other) {
        var val = pos ^ other.pos;
        var count = 0;
        for (count = 0; val > 0; ++count) {
            val &= val - 1;
        }
        return count;
//        var otherB = other.binary;
//        var res = 0;
//        for (var i = 0; i < Math.max(binary.length(), otherB.length()); i++) {
//            var first = binary.charAt(i);
//            var second = otherB.charAt(i);
//            if (first != second)
//                res++;
//        }
//        return res;
    }

    public char getCharAt(int pos) {
        return binary.charAt(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
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

    public static void cleanDist() {
        binToDist = new HashMap<>();
    }
}
