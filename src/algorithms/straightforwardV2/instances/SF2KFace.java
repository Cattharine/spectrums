package algorithms.straightforwardV2.instances;

import java.util.ArrayList;

public class SF2KFace {
    private static String table;
    private static int n;
    private static boolean toPrint = true;
    private static int[] axisCheckers;

    public static int[] getMaxMu(int faceNum) {
        String name = getName(faceNum);
        print(name);
        ArrayList<Integer> freeFactors = getFreeFactorsInFace(name);
        ArrayList<Integer> vertexes = getVertexes(name, freeFactors);
        int[] res = new int[2];
        res[0] = freeFactors.size();
        res[1] = calculateMaxMu(freeFactors.size(), vertexes);

        return res;
    }

    private static String getName(int faceNum) {
        String name = Integer.toUnsignedString(faceNum, 3);
        String filler = "0".repeat(Math.max(0, n - name.length()));
        return filler + name;
    }

    private static int calculateMaxMu(int numberOfFreeFactors, ArrayList<Integer> vertexes) {
        var max = 0;
        for (int vertex : vertexes) {
            var phi = SF2Vertex.getMinMu(vertex, vertexes, numberOfFreeFactors);
            if (phi > max)
                max = phi;
        }

        return max;
    }

    private static ArrayList<Integer> getVertexes(String name, ArrayList<Integer> factors) {
        var maxVertexesCount = 1 << factors.size();
        var allVertexes = new ArrayList<Integer>(maxVertexesCount);
        var base = getBaseValueOfFace(name);

        for (var value = 0; value < maxVertexesCount; value++) {
            var vertexValue = base;
            for (var valuePos = 0; valuePos < factors.size(); valuePos++) {
                if ((value & axisCheckers[valuePos]) > 0)
                    vertexValue ^= axisCheckers[factors.get(valuePos)];
            }
            if (table.charAt(vertexValue) == '1')
                allVertexes.add(vertexValue);
        }

        return allVertexes;
    }

    private static int getBaseValueOfFace(String faceName) {
        var base = 0;

        for (var factor = 0; factor < n; factor++) {
            if (faceName.charAt(n - factor - 1) == '2') {
                base ^= axisCheckers[factor];
            }
        }

        return base;
    }

    private static ArrayList<Integer> getFreeFactorsInFace(String faceName) {
        var factors = new ArrayList<Integer>();

        for (var factor = 0; factor < n; factor++) {
            if (faceName.charAt(n - factor - 1) == '0') {
                factors.add(factor);
            }
        }

        return factors;
    }

    public static void setCommons(int value, String table) {
        n = value;
        SF2KFace.table = table;
        axisCheckers = new int[n];
        for (int i = 0; i < n; i++) {
            axisCheckers[i] = 1 << i;
        }
    }

    private static void print(String name) {
        if (toPrint) {
            System.out.println(name);
        }
    }

    public static void setPrintingState(boolean state) {
        toPrint = state;
    }
}
