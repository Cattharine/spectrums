package visualizer.painter.contents.cube;


import visualizer.painter.instances.Checker;

import java.util.ArrayList;
import java.util.HashMap;

public class Cube {
    public final int n;
    private final int faceCount;
    private final int[] axisCheckers;
    private final Checker<Integer> activeVertexes = new Checker<>();
    private final HashMap<Integer, Checker<Integer>> faceMaxMuVertexes;
    private final HashMap<Integer, HashMap<Integer, Checker<Integer>>> facePaths;
    private String spectrum = "";

    public Cube(int n, String table) {
        this.n = n;
        faceCount = (int) Math.pow(3, n);
        faceMaxMuVertexes = new HashMap<>();

        facePaths = new HashMap<>();

        // здесь номера факторов берутся с конца, то есть нулевой фактор для 0001 равен 1, а остальные 3 равны 0
        axisCheckers = new int[n];
        for (int i = 0; i < n; i++) {
            axisCheckers[i] = 1 << i;
        }

        setActiveVertexes(table);
    }

    public void setActiveVertexes(String table) {
        for (var i = 0; i < table.length(); i++) {
            if (table.charAt(i) == '1')
                activeVertexes.add(i);
        }
    }

    public void setSpectrum(String res) {
        spectrum = res;
    }

    public boolean isActiveVertex(int value) {
        return activeVertexes.check(value);
    }

    public boolean isMaxMuVertex(int vertexValue, int faceValue) {
        return faceMaxMuVertexes.containsKey(faceValue) && faceMaxMuVertexes.get(faceValue).check(vertexValue);
    }

    public void addMaxMuVertex(int face, int vertexValue) {
        if (!faceMaxMuVertexes.containsKey(face))
            faceMaxMuVertexes.put(face, new Checker<>());
        faceMaxMuVertexes.get(face).add(vertexValue);
    }

    public void clearMaxMuFace(int face) {
        if (faceMaxMuVertexes.containsKey(face))
            faceMaxMuVertexes.get(face).clear();
    }

    public boolean isPath(int face, int vertex, int otherValue) {
        return facePaths.containsKey(face) &&
                facePaths.get(face).containsKey(vertex) &&
                facePaths.get(face).get(vertex).check(otherValue);
    }

    public void clearFacePathsForVertex(int face, int vertex) {
        if (facePaths.containsKey(face))
            if (facePaths.get(face).containsKey(vertex))
                facePaths.get(face).get(vertex).clear();
    }

    public void addFacePathForVertex(int face, int vertex, int otherValue) {
        if (!facePaths.containsKey(face))
            facePaths.put(face, new HashMap<>());
        if (!facePaths.get(face).containsKey(vertex))
            facePaths.get(face).put(vertex, new Checker<>());
        facePaths.get(face).get(vertex).add(otherValue);
    }

    public int getAxisChecker(int factor) {
        return axisCheckers[factor];
    }

    public int getNumberOfFaces() {
        return faceCount;
    }

    public String getVertexName(int value) {
        return getName(value, 2);
    }

    public String getFaceName(int value) {
        return getName(value, 3);
    }

    private String getName(int value, int radix) {
        var base = Integer.toString(value, radix);
        var filler = "0".repeat(n - base.length());
        return filler + base;
    }

    public boolean isFactorFixedAsZero(int factor, String faceName) {
        return isFaceNameCharAt(factor, '1', faceName);
    }

    public boolean isFactorFixedAsOne(int factor, String faceName) {
        return isFaceNameCharAt(factor, '2', faceName);
    }

    public boolean isFactorFree(int factor, String faceName) {
        return isFaceNameCharAt(factor, '0', faceName);
    }

    private boolean isFaceNameCharAt(int factor, Character value, String faceName) {
        return faceName.charAt(n - 1 - factor) == value;
    }

    public int[] getAllVertexesInFace(String name) {
        var factors = getFreeFactorsInFace(name);
        var allVertexes = new int[1 << factors.size()];
        var base = getBaseValueOfFace(name);

        // in ascending order
        for (var value = 0; value < allVertexes.length; value++) {
            var vertexValue = base;
            for (var valuePos = 0; valuePos < factors.size(); valuePos++) {
                if ((value & axisCheckers[valuePos]) > 0)
                    vertexValue ^= axisCheckers[factors.get(valuePos)];
            }
            allVertexes[value] = vertexValue;
        }

        return allVertexes;
    }

    public int[] getAllVertexesInFace(int value) {
        return getAllVertexesInFace(getFaceName(value));
    }

    private int getBaseValueOfFace(String faceName) {
        var base = 0;

        for (var factor = 0; factor < n; factor++) {
            if (isFactorFixedAsOne(factor, faceName)) {
                base ^= axisCheckers[factor];
            }
        }

        return base;
    }

    public ArrayList<Integer> getFreeFactorsInFace(String faceName) {
        var factors = new ArrayList<Integer>();

        for (var factor = 0; factor < n; factor++) {
            if (isFactorFree(factor, faceName)) {
                factors.add(factor);
            }
        }

        return factors;
    }

    public ArrayList<Integer> getFreeFactorsInFace(int faceValue) {
        var faceName = getFaceName(faceValue);
        return getFreeFactorsInFace(faceName);
    }

    public ArrayList<Integer> getActiveFactors(int vertexValue) {
        var res = new ArrayList<Integer>();

        for (var i = 0; i < n; i++) {
            if ((vertexValue & axisCheckers[i]) > 0) {
                res.add(i);
            }
        }

        return res;
    }

    public boolean isVertexInFace(int face, int vertex) {
        var faceName = getFaceName(face);
        var vertexName = getVertexName(vertex);

        for (var i = 0; i < faceName.length(); i++) {
            if (faceName.charAt(i) == '0')
                continue;
            if (faceName.charAt(i) == '1' && vertexName.charAt(i) == '0')
                continue;
            if (faceName.charAt(i) == '2' && vertexName.charAt(i) == '1')
                continue;
            return false;
        }
        return true;
    }

    public String getSpectrum() {
        return spectrum;
    }
}
