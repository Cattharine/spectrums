package algorithms.straightforward;

import algorithms.Solver;

import java.util.ArrayList;

public class SFSolver extends Solver {
    private String table;
    private static int[] axisCheckers = new int[0];

    protected SFSolver() {
    }

    public SFSolver(String table) {
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        spectrum = new int[n + 1];
        int faceCount = (int) Math.pow(3, n);
        for (var faceNum = 0; faceNum < faceCount; faceNum++) {
            processFace(faceNum);
        }
    }

    protected void processFace(int faceNum) {
        String faceName = getName(faceNum);
        ArrayList<Integer> freeFactors = getFreeFactorsInFace(faceName);
        ArrayList<Integer> vertexes = getVertexes(faceName, freeFactors);
        int dimension = freeFactors.size();
        int mu = getMaxMu(dimension, vertexes);

        if (mu > spectrum[dimension])
            spectrum[dimension] = mu;
    }

    protected String getName(int faceNum) {
        String name = Integer.toUnsignedString(faceNum, 3);
        String filler = "0".repeat(Math.max(0, n - name.length()));
        return filler + name;
    }

    protected int getMaxMu(int dimension, ArrayList<Integer> vertexes) {
        var max = 0;
        for (int vertex : vertexes) {
            var mu = SFVertex.getMinMu(vertex, vertexes, dimension);
            if (mu > max)
                max = mu;
        }

        return max;
    }

    protected ArrayList<Integer> getVertexes(String name, ArrayList<Integer> factors) {
        var maxNumberOfVertexes = 1 << factors.size();
        var allVertexes = new ArrayList<Integer>();
        var base = getBaseValueOfFace(name);

        for (var relativeValue = 0; relativeValue < maxNumberOfVertexes; relativeValue++) {
            var realValue = base;
            for (var relativeFactor = 0; relativeFactor < factors.size(); relativeFactor++) {
                if ((relativeValue & axisCheckers[relativeFactor]) > 0)
                    realValue ^= axisCheckers[factors.get(relativeFactor)];
            }
            if (table.charAt(realValue) == '1')
                allVertexes.add(realValue);
        }

        return allVertexes;
    }

    private int getBaseValueOfFace(String faceName) {
        var base = 0;

        for (var factor = 0; factor < n; factor++) {
            if (faceName.charAt(n - factor - 1) == '2') {
                base ^= axisCheckers[factor];
            }
        }

        return base;
    }

    protected ArrayList<Integer> getFreeFactorsInFace(String faceName) {
        var factors = new ArrayList<Integer>();

        for (var factor = 0; factor < n; factor++) {
            if (faceName.charAt(n - factor - 1) == '0') {
                factors.add(factor);
            }
        }

        return factors;
    }

    @Override
    public void getNewEntry(String table) {
        super.getNewEntry(table);
        if (SFVertex.binToDist.length < table.length())
            SFVertex.setUpBinToDist(table.length());
        if (axisCheckers.length < n)
            setUpAxisCheckers();
        this.table = table;
    }

    private void setUpAxisCheckers() {
        axisCheckers = new int[n];
        for (int i = 0; i < n; i++) {
            axisCheckers[i] = 1 << i;
        }
    }

    protected static class SFVertex {
        protected static int[] binToDist = new int[0];

        protected static int getMinMu(int current, ArrayList<Integer> vertexes, int currentDimension) {
            var min = currentDimension + 1;
            for (var vertex : vertexes) {
                if (current == vertex)
                    continue;
                var distance = binToDist[current ^ vertex];
                if (distance < min)
                    min = distance;
            }
            return min - 1;
        }

        private static void setUpBinToDist(int tableLength) {
            binToDist = new int[tableLength];
            for (var i = 0; i < tableLength; i++)
                binToDist[i] = convertBinToDist(i);
        }

        private static int convertBinToDist(int dist) {
            var count = 0;
            for (count = 0; dist > 0; ++count) {
                dist &= dist - 1;
            }
            return count;
        }
    }
}
