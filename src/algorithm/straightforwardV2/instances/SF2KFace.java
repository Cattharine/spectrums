package algorithm.straightforwardV2.instances;

import java.util.ArrayList;

public class SF2KFace {
    private static String table;
    private static int n;
    private static boolean toPrint = true;

    public static int[] getMaxMu(int faceNum) {
        String name = getName(faceNum);
        print(name);
        int numberOfFreeFactors = getNumberOfFreeFactors(name);
        ArrayList<Integer> vertexes = getVertexes(name, numberOfFreeFactors);
        int[] res = new int[2];
        res[0] = numberOfFreeFactors;
        res[1] = calculateMaxMu(numberOfFreeFactors, vertexes);

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

    private static ArrayList<Integer> getVertexes(String name, int factorsCount) {
        int maxNumberOfVertexes = (int) Math.pow(2, factorsCount);
        ArrayList<Integer> res = new ArrayList<>();

        for (var i = 0; i < maxNumberOfVertexes; i++) {
            int converted = convertVertex(i, name, factorsCount);
            if (table.charAt(converted) == '1')
                res.add(converted);
        }

        return res;
    }

    private static int convertVertex(int i, String name, int factorsCount) {
        int base = 0;
        int c = 0;
        for (int k = 0 ; k < n; k++) {
            base = base << 1;
            if (name.charAt(k) == '2') {
                base++;
            }
            else if (name.charAt(k) == '0') {
                if ((i & (1 << (factorsCount - c - 1))) > 0)
                    base++;
                c++;
            }
        }
        return base;
    }

    private static int getNumberOfFreeFactors(String name) {
        int count = 0;
        for (var i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '0')
                count++;
        }

        return count;
    }

    public static void setCommons(int value, String table) {
        n = value;
        SF2KFace.table = table;
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
