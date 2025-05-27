package algorithms.tree;

import algorithms.Solver;

import java.util.ArrayList;

public class TreeSolver extends Solver {
    private int currentLevel;
    private KFace[][] processing;

    protected TreeSolver() {
    }

    public TreeSolver(String table) {
        getNewEntry(table);
    }

    @Override
    public void calculateSpectrum() {
        spectrum = new int[n + 1];
        currentLevel = 0;

        while (processing[0][0] != null) {
            var level = processing[currentLevel];
            var index = -1;
            if (level[0] != null) {
                index = 0;
            } else if (level[level.length - 1] != null) {
                index = level.length - 1;
            } else {
                currentLevel--;
                continue;
            }
            var face = level[index];
            process(face);
            var faces = face.fixate();
            updateLevel(faces, index);
        }
    }

    protected void process(KFace face) {
        if (face.isNotProcessed()) {
            var mu = face.getMaxMu();
            if (spectrum[face.faceDimension] < mu)
                spectrum[face.faceDimension] = mu;
        }
    }

    private void updateLevel(KFace[] faces, int index) {
        if (faces != null) {
            currentLevel++;
            processing[currentLevel] = faces;
        } else {
            processing[currentLevel][index] = null;
        }
    }

    @Override
    public void getNewEntry(String table) {
        super.getNewEntry(table);
        if (Vertex.binToDist.length < table.length())
            Vertex.setUpBinToDist(table.length());
        setUpProcessing(table);
    }

    private void setUpProcessing(String table) {
        processing = new KFace[n + 1][];
        for (var i = 1; i <= n; i++) {
            processing[i] = new KFace[2];
        }

        processing[0] = new KFace[1];
        processing[0][0] = new KFace(n, getVertexes(table), n);
    }

    protected ArrayList<Vertex> getVertexes(String table) {
        var vertexes = new ArrayList<Vertex>(table.length());
        for (var value = 0; value < table.length(); value++) {
            if (table.charAt(value) == '1') {
                vertexes.add(new Vertex(value));
            }
        }
        return vertexes;
    }

    public static class KFace {
        public final int faceDimension;
        public final int splittingFactor;
        private int toFixate;
        private final ArrayList<Vertex> vertexes;
        private boolean isProcessed = false;

        private KFace(int faceDimension, ArrayList<Vertex> vertexes, int splittingFactor) {
            this.faceDimension = faceDimension;
            this.vertexes = vertexes;
            toFixate = 0;
            this.splittingFactor = splittingFactor;
        }

        public int getMaxMu() {
            isProcessed = true;
            var max = 0;

            for (var vertex : vertexes) {
                var mu = vertex.getMinMu(vertexes, faceDimension);
                if (mu > max)
                    max = mu;
            }

            return max;
        }

        public KFace[] fixate() {
            if (splittingFactor <= toFixate)
                return null;
            var res = new KFace[2];
            var separatedVertexes = separateVertexes();
            res[0] = createFace(separatedVertexes.first);
            res[1] = createFace(separatedVertexes.second);

            toFixate++;
            return res;
        }

        protected KFace createFace(ArrayList<Vertex> faceVertexes) {
            return new KFace(faceDimension - 1, faceVertexes, toFixate);
        }

        private Pair separateVertexes() {
            var first = new ArrayList<Vertex>(1 << (faceDimension - 1));
            var second = new ArrayList<Vertex>(1 << (faceDimension - 1));
            int checker = 1 << toFixate;

            for (var vertex : vertexes) {
                if ((vertex.value & checker) > 0)
                    second.add(vertex);
                else first.add(vertex);
            }

            return new Pair(first, second);
        }

        public boolean isNotProcessed() {
            return !isProcessed;
        }

        private static class Pair {
            private final ArrayList<Vertex> first;
            private final ArrayList<Vertex> second;

            private Pair(ArrayList<Vertex> first, ArrayList<Vertex> second) {
                this.first = first;
                this.second = second;
            }
        }
    }

    public static class Vertex {
        protected static int[] binToDist = new int[0];
        public final int value;

        protected Vertex(int vertex) {
            value = vertex;
        }

        protected int getMinMu(ArrayList<Vertex> vertexes, int currentDimension) {
            var min = currentDimension + 1;
            for (var vertex : vertexes) {
                if (value == vertex.value)
                    continue;
                var distance = binToDist[value ^ vertex.value];
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
