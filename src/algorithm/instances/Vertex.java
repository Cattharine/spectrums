package algorithm.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Vertex {
    public static int[] binToDist;
//    public static HashMap<Integer, Integer> binToDist;
    private final String binary;
    private final int pos;

    public Vertex(int pos, int fullD) {
        this.pos = pos;
        var bin = Integer.toBinaryString(pos);
        var filler = "0".repeat(Math.max(0, fullD - bin.length()));
        this.binary = filler + bin;
    }

    public int getMinMu(ArrayList<Vertex> vertexes, int currentDimension) {
        var min = currentDimension + 1;
        for (var vertex : vertexes) {
            if (this == vertex)
                continue;
            var distance = binToDist[vertex.pos ^ pos];
            if (distance < min)
                min = distance;
        }
        return min - 1;
    }

//    public void calculateDistances(ArrayList<Vertex> vertexes) {
//        for (var vertex : vertexes) {
//            if (vertex == this)
//                continue;
//            var dist = vertex.pos ^ this.pos;
//            if (binToDist.containsKey(dist))
//                continue;
//            binToDist.put(dist, convertBinToDist(dist));
//        }
//    }

    public static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
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

    public static void cleanDist(int tableLength) {
        binToDist = new int[tableLength];
    }
}
