package visualizer.painter.contents.tree;


import visualizer.painter.instances.Checker;
import visualizer.painter.instances.Coordinates;

import java.util.ArrayList;

public class Tree {
    private final Checker<Integer>[] maxNodes;
    private final TNode[][] nodesByLevels;
    private final int[] minUnfilledNodesByLevels;
    private final TNode[] nodes;
    private int minUnfilledNode = 0;
    public final int n;

    public Tree(int n) {
        this.n = n;

        maxNodes = new Checker[n + 1];
        for (var i = 0; i < n + 1; i++) {
            maxNodes[i] = new Checker<>();
        }

        nodesByLevels = new TNode[n + 1][];
        for (int k = 0; k < n + 1; k++) {
            nodesByLevels[k] = new TNode[(1 << (n - k)) * getNumberOfCombinations(n, k)];
        }
        minUnfilledNodesByLevels = new int[n + 1];

        int numberOfFaces = (int) Math.pow(3, n);
        nodes = new TNode[numberOfFaces];
    }

    private int getNumberOfCombinations(int n, int p) {
        int numerator = 1;
        for (int i = n - p + 1; i < n + 1; i++) {
            numerator *= i;
        }

        int denominator = 1;
        for (int i = 1; i < p + 1; i++) {
            denominator *= i;
        }
        return numerator / denominator;
    }

    public void fillNode(int value, String name, int dimension) {
        nodes[value] = new TNode(value, name, dimension);
        nodesByLevels[dimension][minUnfilledNodesByLevels[dimension]] = nodes[value];

        connectNodeToParent(nodes[value]);

        minUnfilledNodesByLevels[dimension]++;
        minUnfilledNode++;

        fillCoordinates(TreeContentConstants.statesToChange, 1.0);
    }

    private void connectNodeToParent(TNode current) {
        int parentValue = current.getParentValue();
        if (parentValue == -1)
            return;
        nodes[parentValue].addChild(current);
    }

    public void checkNodeAsMax(int value, int dimension) {
        maxNodes[dimension].add(value);
    }

    public void uncheckMaxNodes(int dimension) {
        maxNodes[dimension].clear();
    }

    public void rescaleNodes(double newScale, double oldScale, double relationOfScaling, TreeContentState state) {
        Coordinates[] oldCoordinates = copyNodeCoordinates(state);
        fillCoordinates(state, oldScale);
        for (int value = 0; value < minUnfilledNode; value++) {
            oldCoordinates[value].subtract(nodes[value].coordinates.get(state));
            oldCoordinates[value].mul(relationOfScaling);
        }
        fillCoordinates(state, newScale);
        for (int value = 0; value < minUnfilledNode; value++) {
            nodes[value].coordinates.get(state).add(oldCoordinates[value]);
        }
    }

    private Coordinates[] copyNodeCoordinates(TreeContentState state) {
        Coordinates[] result = new Coordinates[nodes.length];
        for (int value = 0; value < minUnfilledNode; value++) {
            result[value] = nodes[value].coordinates.get(state).getCopy();
        }
        return result;
    }

    public void fillCoordinates(ArrayList<TreeContentState> states, double scale) {
        for (var state : states) {
            fillCoordinates(state, scale);
        }
    }

    public void fillCoordinates(TreeContentState state, double scale) {
        switch (state) {
            case TREE -> fillCoordinatesTree(scale);
            case WITHOUT_LINKS -> fillCoordinatesWithoutLinks(scale);
        }
    }

    private void fillCoordinatesTree(double scale) {
        double theMostLeftX = 0;
        for (int value = minUnfilledNode - 1; value > -1; value--) {
            var current = nodes[value];
            double y = -current.dimension * Math.log(current.dimension + 1) * TreeContentConstants.levelHeight * scale;
            double x;
            if (current.hasChildren()) {
                x = current.getWeightAverageXOfChildren(TreeContentState.TREE);
            } else {
                x = theMostLeftX;
                theMostLeftX -= TreeContentConstants.nodeDistance * scale;
            }
            current.coordinates.get(TreeContentState.TREE).setValue(x, y);
        }
    }

    private void fillCoordinatesWithoutLinks(double scale) {
        for (var level = 0; level < nodesByLevels.length; level++) {
            var length = maxNodes[level].size();
            double baseOffset = TreeContentConstants.nodeDistance * scale * (length - 1) / 2;
            int i = 0;
            for (var value : maxNodes[level]) {
                nodes[value].coordinates.get(TreeContentState.WITHOUT_LINKS).setValue(
                        -baseOffset + i * TreeContentConstants.nodeDistance * scale,
                        (n - level) * TreeContentConstants.levelHeight * scale
                );
                i++;
            }
        }
    }

    public TNode getNode(int value) {
        return nodes[value];
    }

    public boolean isNodeReachable(TNode node, TreeContentState state) {
        return state != TreeContentState.WITHOUT_LINKS || maxNodes[node.dimension].check(node.value);
    }

    public TNode[] getLevel(int level) {
        return nodesByLevels[level];
    }

    public int getLevelSize(int level) {
        return minUnfilledNodesByLevels[level];
    }

    public int getMinUnfilledNode() {
        return minUnfilledNode;
    }

    public boolean isMaxNode(TNode face) {
        return maxNodes[face.dimension].check(face.value);
    }
}
