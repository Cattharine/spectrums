package tests.visualizer.contents.tree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tests.visualizer.painter.TestPainter;
import visualizer.contents.tree.TreeContent;
import visualizer.contents.utils.Coordinates;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;

import java.util.HashSet;
import java.util.function.BiFunction;

public class TreeContentTests {
    private TestTree expected;
    private TreeContent content;

    @Before
    public void prepare() {
        expected = new TestTree();
        content = new TreeContent(expected.commonContent);
    }

    @Test
    public void checkCreation() {
        var painter = getPainted();
        checkNodes(painter, null);
        checkOffset(painter);
    }

    @Test
    public void checkShiftAll() {
        checkShiftAll(new int[]{-119, 200});
        prepare();
        checkShiftAll(new int[]{12, -3});
    }

    private void checkShiftAll(int[] difference) {
        shiftAll(difference);
        var painter = getPainted();
        checkNodes(painter, null);
        checkOffset(painter);
    }

    private void shiftAll(int[] difference) {
        getPainted();
        content.shiftAll(difference);

        expected.offset.add(difference);
    }

    @Test
    public void checkScaling() {
        checkScaling(new int[]{-12, 14}, 2.2);
        prepare();
        checkScaling(new int[]{19, -6}, 0.9);
    }

    public void checkScaling(int[] cursorPos, double relationOfScaling) {
        scale(cursorPos, relationOfScaling);
        var painter = getPainted();
        checkNodes(painter, null);
        checkOffset(painter);
    }

    private void scale(int[] cursorPosition, double relationOfScaling) {
        getPainted();
        content.changeScale(cursorPosition, relationOfScaling);

        expected.setCurrentTransform(getBaseScaleTransform(relationOfScaling), relationOfScaling);
        var difference = expected.offset.copySubtract(cursorPosition).copyMul(relationOfScaling);
        expected.offset.setValue(new Coordinates(cursorPosition).copyAdd(difference));
    }

    @Test
    public void checkSelectAndDeselect() {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            prepare();
            checkSelect(node);
        }
        checkDeselectAll();
    }

    @Test
    public void checkSelectAfterShiftAll() {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            prepare();
            shiftAll(new int[]{12, -9});
            checkSelect(node);
        }
        checkDeselectAll();
    }

    @Test
    public void checkSelectAfterScaling() {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            prepare();
            scale(new int[]{-9, 1}, 1.5);
            checkSelect(node);
        }
        checkDeselectAll();
    }

    private void checkSelect(int selectedNode) {
        var selected = new HashSet<Integer>();
        selectNode(9, selected);
        selectNode(16, selected);
        selectNode(77, selected);
        selectNode(214, selected);
        selectNode(162, selected);
        selectNode(selectedNode, selected);
    }

    private void selectNode(int node, HashSet<Integer> selected) {
        getPainted();
        var coordinates = getCoordinatesWithOffset(node);
        content.select(coordinates.getIntValue());
        if (selected.contains(node))
            selected.remove(node);
        else selected.add(node);
        var painter = getPainted();
        checkNodes(painter, selected);
        Assert.assertTrue(content.canDeselectAll());
    }

    private void checkDeselectAll() {
        getPainted();
        content.deselectAll();
        var painter = getPainted();
        checkNodes(painter, null);
    }

    @Test
    public void checkMoveSelected() {
        checkMoveSelected(new int[]{55, 3});
        checkMoveSelected(new int[]{-26, -9});
    }

    private void checkMoveSelected(int[] difference) {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            checkMoveSelected(node, difference);
        }
    }

    private void checkMoveSelected(int selectedNode, int[] difference) {
        prepare();
        var selected = new HashSet<Integer>();
        selectNode(selectedNode, selected);

        content.moveSelected(difference);
        expected.setCurrentTransform(expected.getMoveBySelectedTransform(difference, selectedNode), 1);

        var painter = getPainted();
        checkNodes(painter, selected);
        checkOffset(painter);
    }

    @Test
    public void checkSelectAfterMoveSelected() {
        checkMoveSelected(Integer.parseInt("12000", 3), new int[]{-10000, 16});
        for (int node = 0; node < expected.numberOfNodes; node++) {
            content.deselectAll();
            checkSelect(node);
        }
        checkDeselectAll();
    }

    @Test
    public void checkToggleSubtree() {
        toggleAndCheck(true, new HashSet<>());
        toggleAndCheck(true, new HashSet<>());
    }

    private void toggleAndCheck(boolean shouldDeselect, HashSet<Integer> selected) {
        selected = toggleSubtree(shouldDeselect, selected);
        var painter = getPainted();
        checkNodes(painter, selected);
        checkOffset(painter);
    }

    private HashSet<Integer> toggleSubtree(boolean shouldDeselect, HashSet<Integer> selected) {
        getPainted();
        if (shouldDeselect) {
            content.deselectAll();
            selected = new HashSet<>();
        }
        selectNode(expected.toHide, selected);
        content.switchState();
        var difference = expected.getTransformedCoordinates(0);
        expected.isHidden = !expected.isHidden;
        difference.subtract(expected.getTransformedCoordinates(0));
        expected.offset.add(difference);
        return selected;
    }

    @Test
    public void checkToggleSubtreeWithSelected() {
        var selected = new HashSet<Integer>();
        selectNode(Integer.parseInt("12020", 3), selected);
        selectNode(Integer.parseInt("12202", 3), selected);
        toggleAndCheck(false, selected);
    }

    @Test
    public void checkChangingFace() {
        checkChangingFace(1);
        prepare();
        checkChangingFace(2);
    }

    @Test
    public void checkChangingFaceAfterToggleSubtree() {
        toggleSubtree(true, new HashSet<>());
        content.deselectAll();
        checkChangingFace(1);
    }

    private void checkChangingFace(int step) {
        var selected = new HashSet<Integer>();
        selectNode(0, selected);
        for (int node = step; node < expected.numberOfNodes; node += step) {
            checkChangingFace(node, step, selected);
        }
    }

    private void checkChangingFace(int node, int step, HashSet<Integer> selected) {
        Assert.assertTrue(content.canChangeFace());
        content.changeFace(step);
        if (expected.shouldNotCheck(node)) {
            checkNotSelected(node);
            content.changeFace(-step);
            return;
        }
        selected.clear();
        selected.add(node);
        var painter = getPainted();
        checkNodes(painter, selected);
        checkOffset(painter);
    }

    @Test
    public void checkSelectFromOtherContent() {
        getPainted();
        var selected = new HashSet<Integer>();
        for (int node = 0; node < expected.numberOfNodes; node++) {
            checkSelectFromOtherContent(node, selected);
        }
    }

    private void checkSelectFromOtherContent(int node, HashSet<Integer> selected) {
        content.deselectAll();
        content.selectFromOtherContent(node);
        selected.clear();
        selected.add(node);
        var painter = getPainted();
        checkNodes(painter, selected);
        expected.offset = expected.getTransformedCoordinates(node).copyMul(-1);
        checkOffset(painter);
    }

    @Test
    public void checkSelectFromOtherContentAfterToggleSubtree() {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            checkSelectFromOtherContentAfterToggleSubtree(node);
        }
    }

    private void checkSelectFromOtherContentAfterToggleSubtree(int node) {
        prepare();
        getPainted();
        toggleSubtree(true, new HashSet<>());
        if (expected.shouldNotCheck(node))
            expected.isHidden = false;
        checkSelectFromOtherContent(node, new HashSet<>());
    }

    private void checkNodes(TestPainter painter, HashSet<Integer> selected) {
        for (int node = 0; node < expected.numberOfNodes; node++) {
            if (expected.shouldNotCheck(node)) {
                checkNotSelected(node);
                continue;
            }
            checkNode(node, painter, selected != null && selected.contains(node));
            checkEdgesOfNode(node, painter);
        }
    }

    private void checkNotSelected(int node) {
        try {
            var selectedField = content.getClass().getDeclaredField("selectedNodes");
            selectedField.setAccessible(true);
            var selected = (HashSet<Integer>) selectedField.get(content);
            Assert.assertFalse("Node is selected", selected.contains(node));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void checkEdgesOfNode(int node, TestPainter painter) {
        if (node == 0)
            return;
        int parent = expected.getParent(node);
        Assert.assertTrue(painter.edges.get(ElementType.EDGE).containsKey(node));
        Assert.assertTrue(painter.edges.get(ElementType.EDGE).get(node).contains(parent));
    }

    private void checkNode(int node, TestPainter painter, boolean isSelected) {
        String message = String.format("Failed on node %d", node);
        var vertexCheck = checkNode(node, ElementType.VERTEX, painter);
        var currentVertexCheck = checkNode(node, ElementType.CURRENT_VERTEX, painter);
        Assert.assertTrue(message, vertexCheck || currentVertexCheck);
        Assert.assertTrue(message, painter.vertexesStates.containsKey(node));
        Assert.assertTrue(message, painter.vertexesStates.get(node).contains(ElementState.BASE));
        Assert.assertEquals(message, isSelected, painter.vertexesStates.get(node).contains(ElementState.SELECTED));
    }

    private boolean checkNode(int node, ElementType key, TestPainter painter) {
        return painter.vertexes.containsKey(key) && painter.vertexes.get(key).contains(node);
    }

    private void checkOffset(TestPainter painter) {
        String message = "Failed on offset";
        Assert.assertEquals(message, expected.offset.getX(), painter.offset.getX(), 1e-6);
        Assert.assertEquals(message, expected.offset.getY(), painter.offset.getY(), 1e-6);
    }

    private TestPainter getPainted() {
        var painter = new TestPainter(this::map);
        while (!painter.isInitialized) {
            painter = new TestPainter(this::map);
            content.paint(painter);
        }
        return painter;
    }

    private BiFunction<Coordinates, Integer, Coordinates> getBaseScaleTransform(double relationOfScaling) {
        return (Coordinates value, Integer node) -> value.copyMul(relationOfScaling);
    }

    private int map(int[] value) {
        return expected.map(value);
    }

    private Coordinates getCoordinatesWithOffset(int node) {
        return expected.getTransformedCoordinates(node).copyAdd(expected.offset);
    }
}
