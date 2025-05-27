package visualizer.contents.common;

import algorithms.ISolver;
import algorithms.Solver;

import java.util.ArrayList;
import java.util.HashSet;

public class CommonContent extends Solver {
    private String result;
    private boolean calculatedSpectrum = false;
    public final Cube cube;
    public final Tree tree;

    public CommonContent(String table) {
        n = ISolver.calculateN(table);
        cube = new Cube(table);
        tree = new Tree();

        spectrum = new int[n + 1];
    }

    @Override
    public void calculateSpectrum() {
        for (int faceValue = 0; faceValue < tree.numberOfFaces; faceValue++) {
            var face = cube.getFace(faceValue);

            face.updateMaxes();
            int mu = face.getMu();

            if (mu > spectrum[face.info.dimension])
                spectrum[face.info.dimension] = mu;
        }

        calculatedSpectrum = true;
    }

    @Override
    public String getResult() {
        if (calculatedSpectrum) {
            if (result == null)
                result = super.getResult();
            return result;
        }
        return super.getResult();
    }

    @Override
    public void getNewEntry(String table) {
    }

    public int getN() {
        return n;
    }

    public int getNumberOfFaces() {
        return tree.numberOfFaces;
    }

    private String getName(int value, int radix) {
        var base = Integer.toString(value, radix);
        var filler = "0".repeat(Math.max(0, n - base.length()));
        return filler + base;
    }

    public static int getFactorValue(int factor) {
        // здесь номера факторов берутся с конца, то есть нулевой фактор для 0001 равен 1, а остальные 3 равны 0
        return 1 << factor;
    }

    public boolean isImplementingSpectrum(int faceValue) {
        var face = cube.getFace(faceValue);
        if (face == null)
            return false;
        int mu = face.getMu();
        return calculatedSpectrum && spectrum[face.info.dimension] == mu && mu > 0;
    }

    public class Cube {
        private final HashSet<Integer> activeVertexes = new HashSet<>();

        private Cube(String table) {
            setActiveVertexes(table);
        }

        private void setActiveVertexes(String table) {
            for (int vertex = 0; vertex < table.length(); vertex++) {
                if (table.charAt(vertex) == '1')
                    activeVertexes.add(vertex);
            }
        }

        public boolean isActiveVertex(int vertex) {
            return activeVertexes.contains(vertex);
        }

        public boolean isMaxVertex(int vertex, int face) {
            if (!isActiveVertex(vertex))
                return false;
            return getFace(face).isMaxVertex(vertex);
        }

        public String getVertexName(int vertex) {
            if (vertex < 0)
                return "";
            return getName(vertex, 2);
        }

        public ArrayList<Integer> getPresentedFactors(int vertexValue) {
            var res = new ArrayList<Integer>();

            for (var factor = 0; factor < n; factor++) {
                if (isFactorPresented(vertexValue, factor)) {
                    res.add(factor);
                }
            }

            return res;
        }

        public boolean isFactorPresented(int vertex, int factor) {
            return factor < n && (vertex & getFactorValue(factor)) > 0;
        }

        public Face getFace(int value) {
            var node = tree.getNode(value);
            if (node == null)
                return null;
            return node.face;
        }
    }

    public class Tree {
        private final TNode[] nodes;
        private int minUnfilledNode = 0;
        private final int numberOfFaces;

        private Tree() {
            numberOfFaces = (int) Math.pow(3, n);

            nodes = new TNode[numberOfFaces];
            if (numberOfFaces > 1)
                for (int node = 0; node < numberOfFaces; node++)
                    fillNode(node);
            else {
                fillSoleNode();
            }
        }

        private void fillNode(int node) {
            nodes[node] = new TNode(node);

            minUnfilledNode++;
        }

        private void fillSoleNode() {
            nodes[0] = new TNode(1);

            minUnfilledNode++;
        }

        public TNode getNode(int node) {
            if (node > -1 && node < nodes.length)
                return nodes[node];
            if (numberOfFaces == 1 && (node == 0 || node == 1))
                return nodes[0];
            return null;
        }

        public int getMinUnfilledNode() {
            return minUnfilledNode;
        }

        public class TNode {
            public final Face face;
            private final int parentValue;
            private final int numberOfTrailingZeros;

            private TNode(int nodeValue) {
                face = new Face(getName(nodeValue, 3), cube::isActiveVertex);
                int[] values = calculateParentValueAndNumberOfTrailingZeros();
                parentValue = values[0];
                numberOfTrailingZeros = values[1];
            }

            private int[] calculateParentValueAndNumberOfTrailingZeros() {
                // факторы считаются с конца строкового представления
                for (int factor = 0; factor < n; factor++) {
                    if (!face.info.isFactorFree(factor)) {
                        int index = n - 1 - factor;
                        int parent = face.info.value - Integer.parseInt(face.info.name.substring(index), 3);
                        return new int[]{parent, factor};
                    }
                }
                return new int[]{-1, n};
            }

            public int getParentValue() {
                return parentValue;
            }

            public int getNumberOfLeavesInSubtree() {
                if (numberOfTrailingZeros == 0)
                    return 1;
                return (int) Math.pow(3, numberOfTrailingZeros - 1) * 2;
            }

            public int getLastNodeOfSubtree() {
                return face.info.value + (int) Math.pow(3, numberOfTrailingZeros) - 1;
            }
        }
    }
}
