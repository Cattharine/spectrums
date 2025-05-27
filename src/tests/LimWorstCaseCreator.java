package tests;

import java.util.ArrayList;

public class LimWorstCaseCreator {
    private static final ArrayList<Integer> movers = new ArrayList<>();

    public static void main(String[] args) {
        setUpMover();
        for (int n = 0; n < 16; n++) {
            System.out.println(getWorstCase(n));
        }
    }

    private static String getWorstCase(int n) {
        var builder = new StringBuilder("0".repeat(1 << n));
        int maxVertex = builder.length() - 1;
        builder.setCharAt(maxVertex, '1');
        for (int vertex = maxVertex; vertex > -1; vertex--) {
            if (builder.charAt(vertex) == '0')
                continue;
            for (var mover : movers) {
                if (mover > maxVertex)
                    break;
                var next = vertex ^ mover;
                builder.setCharAt(next, '1');
            }
        }
        return builder.toString();
    }

    private static void setUpMover() {
        int maxValue = 1 << 30;
        for (int i = 0; i < maxValue; i++) {
            int dist = convertBinToDist(i);
            if (dist == 2)
                movers.add(i);
        }
    }

    private static int convertBinToDist(int dist) {
        int count = 0;
        for (; dist > 0; count++) {
            dist &= dist - 1;
        }
        return count;
    }
}
