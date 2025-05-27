package visualizer.contents.tree;

import visualizer.contents.common.CommonContent;
import visualizer.contents.utils.*;
import visualizer.contents.utils.contentInterfaces.IContentPart;
import visualizer.contents.utils.elementInfo.ElementInfo;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;
import visualizer.painter.IPainter;

import java.util.*;
import java.util.function.Consumer;

public class TreeContent implements IContentPart {
    private final CommonContent common;
    private final TreeCoordinates tree;
    private TreeContentState state = TreeContentState.UNINITIALIZED;
    private final HashSet<Integer> selectedNodes = new HashSet<>();

    public TreeContent(CommonContent commonContent) {
        common = commonContent;
        tree = new TreeCoordinates();
    }

    @Override
    public void shiftAll(int[] difference) {
        tree.shiftAll(difference);
    }

    @Override
    public void changeScale(int[] cursorPosition, double relationOfScaling) {
        tree.changeScale(cursorPosition, relationOfScaling);
    }

    @Override
    public void select(int[] position) {
        for (int node = 0; node < common.tree.getMinUnfilledNode(); node++) {
            if (tree.canSelect(node, position)) {
                if (selectedNodes.contains(node)) {
                    selectedNodes.remove(node);
                } else selectedNodes.add(node);
            }
        }
    }

    private void focusOn(int node) {
        if (common.tree.getNode(node) != null)
            tree.focusOn(node);
    }

    private void select(int node) {
        if (common.tree.getNode(node) != null) {
            selectedNodes.add(node);
        }
    }

    @Override
    public void deselectAll() {
        selectedNodes.clear();
    }

    @Override
    public void moveSelected(int[] difference) {
        tree.moveSelected(difference);
    }

    @Override
    public boolean canGoToSelected() {
        return selectedNodes.size() == 1;
    }

    @Override
    public int getSelected() {
        for (int value : selectedNodes) {
            return value;
        }
        return -1;
    }

    @Override
    public void selectFromOtherContent(int node) {
        //from cube
        if (common.tree.getNode(node) == null)
            return;
        switch (state) {
            case UNINITIALIZED -> initialize(node);
            case TREE -> {
                tree.uncoverNode(node);
                focusOn(node);
            }
        }
        deselectAll();
        select(node);
    }

    @Override
    public boolean canDeselectAll() {
        return selectedNodes.size() > 0;
    }

    @Override
    public void paint(IPainter painter) {
        new TreePainter(painter).paint();
    }

    @Override
    public void switchState() {
        toggleSubtree();
    }

    @Override
    public void changeFace(int direction) {
        if (!canChangeFace())
            return;
        int faceNum = common.getNumberOfFaces();
        int selected = getSelected();
        do {
            selected = (selected + direction + faceNum) % faceNum;
        } while (!tree.isReachable(selected));
        deselectAll();
        select(selected);
    }

    @Override
    public boolean canChangeFace() {
        return selectedNodes.size() == 1;
    }

    private void toggleSubtree() {
        for (var selected : selectedNodes) {
            tree.toggleSubtree(selected);
        }

        selectedNodes.removeIf(selected -> !tree.isReachable(selected));
    }

    private void initialize(int focus) {
        tree.fillCoordinates(tree.scale);
        state = TreeContentState.TREE;
        focusOn(focus);
    }

    public class TreeCoordinates {
        private final double[] xCoordinates;
        private final double[] yCoordinates;
        private double scale = 1.0;
        private final Coordinates offset = new Coordinates();
        private final HashMap<Integer, Integer> hiddenSubtrees = new HashMap<>();

        private TreeCoordinates() {
            xCoordinates = new double[common.getNumberOfFaces()];
            yCoordinates = new double[common.getN() + 1];
        }

        private void fillCoordinates(double scale) {
            fillCoordinatesX(scale);
            fillCoordinatesY(scale);
        }

        private void fillCoordinatesX(double scale) {
            double[] absoluteX = new double[xCoordinates.length];
            double[] mostRightOnLevel = new double[yCoordinates.length];
            HashMap<Integer, Integer> reduction = getHiddenSubtreesReduction();

            for (int nodeValue = 0; nodeValue < xCoordinates.length; nodeValue++) {
                var node = common.tree.getNode(nodeValue);
                int numberOfLeaves = node.getNumberOfLeavesInSubtree();
                if (numberOfLeaves > 1)
                    mostRightOnLevel[node.face.info.dimension - 1] = mostRightOnLevel[node.face.info.dimension];

                if (reduction.containsKey(nodeValue))
                    numberOfLeaves -= reduction.get(nodeValue);
                double addition = TreeConstants.nodeDistance * scale * numberOfLeaves;
                absoluteX[nodeValue] = mostRightOnLevel[node.face.info.dimension] + addition / 2;

                int parentValue = node.getParentValue();
                double parentAbsoluteX = parentValue > -1 ? absoluteX[parentValue] : 0;
                xCoordinates[nodeValue] = absoluteX[nodeValue] - parentAbsoluteX;

                mostRightOnLevel[node.face.info.dimension] += addition;
            }
        }

        private HashMap<Integer, Integer> getHiddenSubtreesReduction() {
            HashMap<Integer, Integer> result = new HashMap<>();

            for (int start : hiddenSubtrees.keySet()) {
                if (!isReachable(start))
                    continue;
                int difference = common.tree.getNode(start).getNumberOfLeavesInSubtree() - 1;
                int current = start;
                while (current > -1) {
                    result.put(current, result.getOrDefault(current, 0) + difference);
                    current = common.tree.getNode(current).getParentValue();
                }
            }
            return result;
        }

        private void fillCoordinatesY(double scale) {
            for (int dimension = 0; dimension < yCoordinates.length; dimension++) {
                double y = -dimension * dimension * TreeConstants.levelHeight * scale;
                yCoordinates[dimension] = y;
            }
        }

        private void shiftAll(int[] difference) {
            offset.add(difference);
        }

        public void changeScale(int[] cursorPosition, double relationOfScaling) {
            double oldScale = scale;
            scale = oldScale * relationOfScaling;
            offset.setValue(offset.copySubtract(cursorPosition).copyMul(relationOfScaling).copyAdd(cursorPosition));
            rescaleNodes(oldScale, relationOfScaling);
        }

        private void rescaleNodes(double oldScale, double relationOfScaling) {
            rescaleAxis(oldScale, relationOfScaling, this::fillCoordinatesX, xCoordinates);
            rescaleAxis(oldScale, relationOfScaling, this::fillCoordinatesY, yCoordinates);
        }

        private void rescaleAxis(double oldScale, double relationOfScaling, Consumer<Double> fillCoordinates,
                                 double[] coordinates) {
            double[] oldCoordinates = coordinates.clone();
            fillCoordinates.accept(oldScale);
            for (int value = 0; value < coordinates.length; value++) {
                oldCoordinates[value] -= coordinates[value];
                oldCoordinates[value] *= relationOfScaling;
            }
            fillCoordinates.accept(scale);
            for (int value = 0; value < coordinates.length; value++) {
                coordinates[value] += oldCoordinates[value];
            }
        }

        private boolean canSelect(int node, int[] cursorPosition) {
            var nodePosition = getCoordinates(node).copyAdd(offset);
            var difference = nodePosition.copySubtract(cursorPosition).getAbs();
            var radius = TreeConstants.getDiameter(ElementState.SELECTED) / 2.0;
            boolean isCloseEnough = Math.max(difference.getX(), difference.getY()) <= radius;
            return isCloseEnough && isReachable(node);
        }

        private void moveSelected(int[] difference) {
            int x = difference[0];
            int y = difference[1];
            HashSet<Integer> levelsChecker = new HashSet<>();
            for (int nodeValue : selectedNodes) {
                var face = common.cube.getFace(nodeValue).info;
                xCoordinates[face.value] += x;
                levelsChecker.add(face.dimension);
            }
            for (int dimension : levelsChecker) {
                yCoordinates[dimension] += y;
            }
        }

        private void focusOn(int nodeValue) {
            var coordinates = getCoordinates(nodeValue);
            offset.setValue(coordinates.copyMul(-1));
        }

        private boolean isSubtreeHidden(int node) {
            return hiddenSubtrees.containsKey(node);
        }

        private boolean isReachable(int node) {
            int current = node;
            while (current > -1) {
                current = common.tree.getNode(current).getParentValue();
                if (hiddenSubtrees.containsKey(current))
                    return false;
            }
            return true;
        }

        private void uncoverNode(int node) {
            int current = node;
            while (current > -1) {
                current = common.tree.getNode(current).getParentValue();
                if (hiddenSubtrees.containsKey(current))
                    toggleSubtree(current);
            }
        }

        private void toggleSubtree(int nodeValue) {
            double[] oldXCoordinates = xCoordinates.clone();
            fillCoordinatesX(scale);
            double[] oldStandardXCoordinates = xCoordinates.clone();

            updateHiddenSubtrees(nodeValue);

            fillCoordinatesX(scale);

            for (int node = 0; node < xCoordinates.length; node++) {
                double difference = oldXCoordinates[node] - oldStandardXCoordinates[node];
                xCoordinates[node] += difference;
            }

            offset.add(new double[]{oldXCoordinates[0] - xCoordinates[0], 0});
        }

        private void updateHiddenSubtrees(int nodeValue) {
            if (hiddenSubtrees.containsKey(nodeValue)) {
                hiddenSubtrees.remove(nodeValue);
            } else {
                int lastNodeOfSubtree = common.tree.getNode(nodeValue).getLastNodeOfSubtree();
                if (lastNodeOfSubtree != nodeValue) {
                    hiddenSubtrees.put(nodeValue, lastNodeOfSubtree);
                }
            }
        }

        private int getEndOfHiddenSubtree(int start) {
            return hiddenSubtrees.get(start);
        }

        private Coordinates getCoordinates(int nodeValue) {
            double xCoordinate = 0;
            int current = nodeValue;
            while (current > -1) {
                xCoordinate += xCoordinates[current];
                current = common.tree.getNode(current).getParentValue();
            }

            return new Coordinates(xCoordinate, yCoordinates[common.tree.getNode(nodeValue).face.info.dimension]);
        }

        private int[] transformToDrawable(int nodeValue) {
            return getCoordinates(nodeValue).getIntValue();
        }
    }

    public class TreePainter {
        private final IPainter painter;

        private TreePainter(IPainter painter) {
            this.painter = painter;
        }

        public void paint() {
            switch (state) {
                case UNINITIALIZED -> actUninitialized();
                case TREE -> paintTree();
            }
        }

        private void actUninitialized() {
            int minUnfilledNode = common.tree.getMinUnfilledNode();
            if (minUnfilledNode == common.getNumberOfFaces()) {
                initialize(0);
                paintTree();
            }
        }

        private void paintTree() {
            painter.offset(tree.offset);
            drawEdges();
            drawNodes();
        }

        private void drawNodes() {
            for (int nodeValue = 0; nodeValue < common.tree.getMinUnfilledNode(); nodeValue++) {
                var elements = new ArrayList<ElementInfo>();
                var isCurrent = selectedNodes.contains(nodeValue);
                var type = isCurrent ? ElementType.CURRENT_VERTEX : ElementType.VERTEX;
                if (tree.isSubtreeHidden(nodeValue))
                    elements.add(new ElementInfo(type, ElementState.HIDDEN));
                if (selectedNodes.contains(nodeValue))
                    elements.add(new ElementInfo(type, ElementState.SELECTED));
                if (common.isImplementingSpectrum(nodeValue))
                    elements.add(new ElementInfo(type, ElementState.MAX));
                elements.add(new ElementInfo(type, ElementState.BASE));
                drawNode(elements, nodeValue);
                if (tree.isSubtreeHidden(nodeValue)) {
                    nodeValue = tree.getEndOfHiddenSubtree(nodeValue);
                }
            }
        }

        private void drawNode(ArrayList<ElementInfo> elements, int node) {
            int[] coordinates = tree.transformToDrawable(node);
            for (var info : elements) {
                var diameter = TreeConstants.getDiameter(info.state);
                var face = common.cube.getFace(node);
                String name = String.format("%d", face.info.value);
                if (info.type == ElementType.CURRENT_VERTEX)
                    name = String.format("%s [%s]", name, face.info.name);
                painter.drawDisc(coordinates, diameter, diameter, info);
                if (info.state == ElementState.BASE) {
                    painter.drawString(name, TreeConstants.getShiftedForString(coordinates), info);
                }
            }
        }

        private void drawEdges() {
            for (int nodeValue = 0; nodeValue < common.tree.getMinUnfilledNode(); nodeValue++) {
                drawEdge(nodeValue);
                if (tree.isSubtreeHidden(nodeValue)) {
                    nodeValue = tree.getEndOfHiddenSubtree(nodeValue);
                }
            }
        }

        private void drawEdge(int nodeValue) {
            if (nodeValue == 0)
                return;
            var node = common.tree.getNode(nodeValue);
            int[] nodeCoordinates = tree.transformToDrawable(nodeValue);
            int[] parentCoordinates = tree.transformToDrawable(node.getParentValue());
            painter.drawLine(nodeCoordinates, parentCoordinates, new ElementInfo(ElementType.EDGE, ElementState.BASE));
        }
    }
}
