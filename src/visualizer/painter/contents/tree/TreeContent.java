package visualizer.painter.contents.tree;

import visualizer.painter.instances.Checker;
import visualizer.painter.contents.ElementType;
import visualizer.painter.contents.IContent;
import visualizer.painter.instances.Coordinates;
import visualizer.painter.painter.IPainter;
import visualizer.painter.painter.StrokeType;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TreeContent implements IContent {
    private final Tree tree;
    private TreeContentState state = TreeContentState.TREE;
    private final HashMap<TreeContentState, Coordinates> offset;
    private final HashMap<TreeContentState, Double> scale;
    private final Checker<Integer> selected = new Checker<>();
    private final HashMap<TreeContentState, Integer> focused;

    public TreeContent(Tree tree) {
        this.tree = tree;

        offset = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TreeContentState.TREE, new Coordinates()),
                new AbstractMap.SimpleEntry<>(TreeContentState.WITHOUT_LINKS, new Coordinates()))
        );

        scale = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TreeContentState.TREE, 1.0),
                new AbstractMap.SimpleEntry<>(TreeContentState.WITHOUT_LINKS, 1.0))
        );

        focused = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TreeContentState.TREE, 0),
                new AbstractMap.SimpleEntry<>(TreeContentState.WITHOUT_LINKS, -1))
        );
    }

    public void shiftAll(int[] difference) {
        offset.get(state).add(difference);
    }

    public void changeScale(int[] cursorPosition, double relationOfScaling) {
        double oldScale = scale.get(state);
        scale.put(state, oldScale * relationOfScaling);
        offset.get(state).setValue(offset
                .get(state)
                .copySubtract(cursorPosition)
                .copyMul(relationOfScaling)
                .copyAdd(cursorPosition));
        tree.rescaleNodes(scale.get(state), oldScale, relationOfScaling, state);
    }

    public void select(int[] position) {
        for (int value = 0; value < tree.getMinUnfilledNode(); value++) {
            if (canSelect(tree.getNode(value), position)) {
                if (selected.check(value)) {
                    selected.remove(value);
                } else selected.add(value);
            }
        }
    }

    private boolean canSelect(TNode node, int[] position) {
        var vertexPos = node.coordinates.get(state);
        var difference = vertexPos
                .copyMul(-1)
                .copySubtract(offset.get(state)).copyAdd(position).getAbs();
        var radius = TreeContentConstants.getDiameter(ElementType.SELECTED) / 2.0;
        boolean isCloseEnough = Math.max(difference.getX(), difference.getY()) <= radius;
        boolean isReachable = tree.isNodeReachable(node, state);
        return isCloseEnough && isReachable;
    }

    public boolean canGoToSelected() {
        return selected.size() == 1;
    }

    public int getSelectedNodeValue() {
        for (var value : selected) {
            selected.remove(value);
            return value;
        }
        return -1;
    }

    public void deselectAll() {
        selected.clear();
    }

    public boolean canDeselectAll() {
        return selected.size() > 0;
    }

    public void focusOn(int node) {
        if (node < tree.getMinUnfilledNode())
            focused.put(state, node);
    }

    public void select(int node) {
        if (node < tree.getMinUnfilledNode())
            selected.add(node);
    }

    public void moveSelected(int[] difference) {
        int y = difference[1];
        difference[1] = 0;
        int[] other = new int[2];
        other[1] = y;
        Checker<Integer> levelsChecker = new Checker<>();
        for (var faceValue : selected) {
            var current = tree.getNode(faceValue);
            current.coordinates.get(state).add(difference);
            levelsChecker.add(current.dimension);
        }
        for (var dimension : levelsChecker) {
            var level = tree.getLevel(dimension);
            for (var i = 0; i < tree.getLevelSize(dimension); i++) {
                level[i].coordinates.get(state).add(other);
            }
        }
    }

    public void switchState() {
        deselectAll();
        state = switch (state) {
            case TREE -> TreeContentState.WITHOUT_LINKS;
            case WITHOUT_LINKS -> TreeContentState.TREE;
        };
    }

    public void paint(IPainter painter) {
        int focus = focused.get(state);
        if (focus > -1) {
            offset.get(state).setValue(tree
                    .getNode(focus)
                    .coordinates
                    .get(state)
                    .copyMul(-1));
            focused.put(state, -1);
        }
        drawNodes(painter);
        if (state == TreeContentState.TREE) {
            drawEdges(painter);
        }
    }

    private void drawNodes(IPainter painter) {
        for (var value = 0; value < tree.getMinUnfilledNode(); value++) {
            var node = tree.getNode(value);
            if (tree.isNodeReachable(node, state)) {
                if (selected.check(value))
                    drawNode(painter, node, ElementType.SELECTED);
                if (tree.isMaxNode(node))
                    drawNode(painter, node, ElementType.MAX_MU);
                drawNode(painter, node, ElementType.BASE);
            }
        }
    }

    private void drawNode(IPainter painter, TNode node, ElementType type) {
        int[] coordinates = node.coordinates.get(state).copyAdd(offset
                .get(state))
                .getIntValue();
        int diameter = TreeContentConstants.getDiameter(type);
        String name = node.name + String.format(" [%d]", node.value);
        painter.drawDisc(coordinates, diameter, diameter, type);
        if (type == ElementType.BASE)
            painter.drawString(name, TreeContentConstants.getShiftedForString(coordinates), type);
    }

    private void drawEdges(IPainter painter) {
        var minUnfilledFace = tree.getMinUnfilledNode();
        for (int value = 0; value < minUnfilledFace; value++) {
            var current = tree.getNode(value);
            for (var child : current) {
                painter.drawLine(current.coordinates
                                .get(state)
                                .copyAdd(offset.get(state)).getIntValue(),
                        child.coordinates
                                .get(state)
                                .copyAdd(offset.get(state)).getIntValue(),
                        ElementType.BASE,
                        StrokeType.BASE);
            }
        }
    }

    public TreeContentState getState() {
        return state;
    }

    public void goToNodeFromCube(int node) {
        deselectAll();
        if (!tree.isMaxNode(tree.getNode(node)))
            state = TreeContentState.TREE;
        select(node);
        focusOn(node);
    }
}
