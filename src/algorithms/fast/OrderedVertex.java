package algorithms.fast;

import java.util.ArrayList;

public class OrderedVertex {
    private final int value;
    private final String binary;
    private int m = 0;
    private int l = 0;
    private final ArrayList<OrderedVertex> covered = new ArrayList<>();

    public OrderedVertex(int value, int fullD) {
        this.value = value;
        var bin = Integer.toBinaryString(value);
        var filler = "0".repeat(Math.max(0, fullD - bin.length()));
        this.binary = filler + bin;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void addCovered(OrderedVertex vertex) {
        covered.add(vertex);
    }

    public int getValue() {
        return value;
    }

    private int calculateP() {
        var p = Integer.MAX_VALUE;
        for (var vertex : covered) {
            if (vertex.m < p) {
                p = vertex.m;
            }
        }
        return p;
    }

    public int calculateM(int k) {
        var p = calculateP();
        if (p < k - 1 || (p == k - 1 && l == 1)) {
            m = p;
        } else if (p == k - 1 && l == 0) {
            m = k;
        }
        return m;
    }

    @Override
    public String toString() {
        return binary;
    }
}
