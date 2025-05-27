package visualizer.contents.common;

import java.util.ArrayList;
import java.util.HashSet;

public class FaceInfo {
    public final int value;
    public final String name;
    public final int dimension;

    public FaceInfo(String faceName) {
        value = Integer.parseInt(faceName, 3);
        name = faceName;
        var freeFactors = getFreeFactors();
        dimension = freeFactors.size();
    }

    public boolean isFactorNotPresented(int factor) {
        return checkFactor(factor, '1');
    }

    public boolean isFactorPresented(int factor) {
        return checkFactor(factor, '2');
    }

    public boolean isFactorFree(int factor) {
        return checkFactor(factor, '0');
    }

    private boolean checkFactor(int factor, Character value) {
        return name.charAt(name.length() - 1 - factor) == value;
    }

    public boolean containsVertex(int vertex) {
        if (vertex < 0 || vertex + 1 > 1 << name.length())
            return false;
        for (int factor = 0; factor < name.length(); factor++) {
            if (isFactorFree(factor))
                continue;
            if (isFactorNotPresented(factor) && (vertex & CommonContent.getFactorValue(factor)) == 0)
                continue;
            if (isFactorPresented(factor) && (vertex & CommonContent.getFactorValue(factor)) > 0)
                continue;
            return false;
        }
        return true;
    }

    public HashSet<Integer> getAllVertexes() {
        var factors = getFreeFactors();
        int numberOfVertexes = 1 << factors.size();
        HashSet<Integer> allVertexes = new HashSet<>();
        int base = getBaseVertex();

        // в порядке возрастания
        for (int relativeValue = 0; relativeValue < numberOfVertexes; relativeValue++) {
            int realValue = base;
            // проверяем присутствие каждого из факторов в относительном представлении вершины
            for (int factorRelativePos = 0; factorRelativePos < factors.size(); factorRelativePos++) {
                if ((relativeValue & CommonContent.getFactorValue(factorRelativePos)) > 0)
                    // если фактор присутсвует, добавляем к настоящему представлению вершины значение этого фактора
                    realValue ^= CommonContent.getFactorValue(factors.get(factorRelativePos));
            }
            allVertexes.add(realValue);
        }

        return allVertexes;
    }

    public ArrayList<Integer> getFreeFactors() {
        var factors = new ArrayList<Integer>();

        for (var factor = 0; factor < name.length(); factor++) {
            if (isFactorFree(factor)) {
                factors.add(factor);
            }
        }

        return factors;
    }

    private int getBaseVertex() {
        var base = 0;

        for (var factor = 0; factor < name.length(); factor++) {
            if (isFactorPresented(factor)) {
                base ^= CommonContent.getFactorValue(factor);
            }
        }

        return base;
    }
}
