package algorithm.straightforward.instances;

import java.util.ArrayList;

public class SFKFace {
    private static String table;
    private static int n;
    private final int k;
    private int toFixate;
    private final int t;
    private final StringBuilder name;
    private String currentVal = "1";
    private static boolean toPrint = true;

    public SFKFace(int k, int toFixate, int t, StringBuilder name) {
        this.k = k;
        this.toFixate = toFixate;
        this.t = t;
        this.name = name;
    }

    public int getMaxMu() {
        print();
        var vertexes = getVertexes();
        var max = 0;

        for (var vertex : vertexes) {
            var phi = vertex.getMinMu(vertexes, k);
            if (phi > max)
                max = phi;
        }

        return max;
    }

    public SFKFace fixate() {
        if (toFixate == t)
            return null;
        var face = new SFKFace(k - 1, n - 1, toFixate,
                new StringBuilder(name).replace(toFixate, toFixate + 1, currentVal));
        if (currentVal.equals("1")) {
            currentVal = "2";
        }
        else {
            currentVal = "1";
            toFixate--;
        }
        return face;
    }

    private ArrayList<SFVertex> getVertexes() {
        var res = new ArrayList<SFVertex>();

        for (int i = 0; i < Math.pow(2, k); i++) {
            var replacement = Integer.toBinaryString(i);
            var filler = "0".repeat(Math.max(0, k - replacement.length()));
            replacement = filler + replacement;
            var binary = getVertexBinary(replacement);
            var pos = Integer.parseInt(binary, 2);
            if (table.charAt(pos) == '1') {
                res.add(new SFVertex(binary, pos));
            }
        }

        return res;
    }

    private String getVertexBinary(String replacement) {
        var basis = new StringBuilder();
        var count = 0;

        for (var i = 0; i < n; i++) {
            switch (name.charAt(i)) {
                case '1' -> basis.append('0');
                case '2' -> basis.append('1');
                default -> {
                    basis.append(replacement.charAt(count));
                    count++;
                }
            }
        }

        return basis.toString();
    }

    public static void setCommons(int value, String table) {
        n = value;
        SFKFace.table = table;
    }

    private void print() {
        if (toPrint) {
            System.out.println(name);
        }
    }

    public static void setPrintingState(boolean state) {
        toPrint = state;
    }
}
