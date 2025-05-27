package algorithms.fast;

import java.util.ArrayList;

public class OrderedVertex {
    public final int value;
    private int mu = 0;
    private boolean isActive = false;
    private final ArrayList<OrderedVertex> covered = new ArrayList<>();

    public OrderedVertex(int value) {
        this.value = value;
    }

    public void setState(boolean active) {
        isActive = active;
        mu = 0;
    }

    public void addCovered(OrderedVertex vertex) {
        covered.add(vertex);
    }

    private int calculateMinOfCovered() {
        var min = Integer.MAX_VALUE;
        for (var vertex : covered) {
            if (vertex.mu < min) {
                min = vertex.mu;
            }
        }
        return min;
    }

    public int calculateMu(int level) {
        var minOfCovered = calculateMinOfCovered();
        if (minOfCovered < level - 1 || isActive) {
            mu = minOfCovered;
        } else {
            mu = level;
        }
        return mu;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(value);
    }
}
