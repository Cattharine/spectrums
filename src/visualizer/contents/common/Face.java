package visualizer.contents.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class Face {
    public final FaceInfo info;
    private final Function<Integer, Boolean> isActiveVertex;
    private int mu = -1;
    private SoftReference<HashMap<Integer, HashSet<Integer>>> closestVertexes = new SoftReference<>(null);
    private SoftReference<HashSet<Integer>> maxVertexes = new SoftReference<>(null);

    public Face(String faceName, Function<Integer, Boolean> isActiveVertex) {
        info = new FaceInfo(faceName);
        this.isActiveVertex = isActiveVertex;
    }

    public boolean isMaxVertex(int vertex) {
        var maxes = getMaxVertexes();
        cacheMaxVertexes(maxes);
        return maxes.contains(vertex);
    }

    private HashSet<Integer> getMaxVertexes() {
        var value = maxVertexes.get();
        if (value != null)
            return value;
        var maxes = new HashSet<Integer>();
        updateMaxes(mu, maxes);
        return maxes;
    }

    private void cacheMaxVertexes(HashSet<Integer> maxes) {
        var value = maxVertexes.get();
        if (value == null)
            maxVertexes = new SoftReference<>(maxes);
    }

    public void updateMaxes() {
        var maxes = new HashSet<Integer>();
        updateMaxes(0, maxes);
    }

    private void updateMaxes(int max, HashSet<Integer> maxes) {
        maxes.clear();
        var allVertexesInFace = info.getAllVertexes();

        for (int vertex : allVertexesInFace) {
            int min = calculateMinMu(vertex, allVertexesInFace, null);

            if (min > max) {
                max = min;
                maxes.clear();
            }
            if (min == max && min != 0) {
                maxes.add(vertex);
            }
        }

        mu = max;
    }

    private int calculateMinMu(int vertex, HashSet<Integer> allVertexes, HashSet<Integer> closest) {
        if (!isActiveVertex.apply(vertex))
            return 0;
        if (closest != null)
            closest.clear();

        int min = info.dimension + 1;

        for (int otherVertex : allVertexes) {
            if (!isActiveVertex.apply(otherVertex) || otherVertex == vertex)
                continue;
            int distance = convertBinToDist(vertex ^ otherVertex);

            if (distance < min) {
                min = distance;
                if (closest != null)
                    closest.clear();
            }
            if (distance == min && closest != null)
                closest.add(otherVertex);
        }

        return min - 1;
    }

    public HashMap<Integer, HashSet<Integer>> getPathsToClosestVertexes(int vertex) {
        var closest = getClosestVertexes(vertex);
        HashMap<Integer, HashSet<Integer>> result = new HashMap<>();
        for (int otherVertex : closest) {
            int base = vertex & otherVertex;
            addPath(base, base ^ vertex, result);
            addPath(base, base ^ otherVertex, result);
        }
        cacheClosestVertexes(vertex, closest);
        return result;
    }

    private void addPath(int vertex, int difference, HashMap<Integer, HashSet<Integer>> paths) {
        int current = vertex;
        for (int factor = 0; factor < info.name.length(); factor++) {
            if ((difference & CommonContent.getFactorValue(factor)) == 0)
                continue;
            int next = current ^ CommonContent.getFactorValue(factor);
            if (!paths.containsKey(current))
                paths.put(current, new HashSet<>());
            paths.get(current).add(next);
            current = next;
        }
    }

    private void cacheClosestVertexes(int vertex, HashSet<Integer> closest) {
        var value = closestVertexes.get();
        if (value == null) {
            var newValue = new HashMap<Integer, HashSet<Integer>>();
            newValue.put(vertex, closest);
            closestVertexes = new SoftReference<>(newValue);
        } else value.put(vertex, closest);
    }

    public HashSet<Integer> getClosestVertexes(int vertex) {
        var value = closestVertexes.get();
        if (value != null && value.containsKey(vertex))
            return value.get(vertex);
        var closest = new HashSet<Integer>();
        fillClosestVertexes(vertex, closest);
        return closest;
    }

    private void fillClosestVertexes(int vertex, HashSet<Integer> closest) {
        if (!info.containsVertex(vertex))
            return;
        calculateMinMu(vertex, info.getAllVertexes(), closest);
    }

    public int getMu() {
        return mu;
    }

    @Override
    public String toString() {
        return info.name;
    }

    private static int convertBinToDist(int dist) {
        var count = 0;
        for (count = 0; dist > 0; ++count) {
            dist &= dist - 1;
        }
        return count;
    }
}
