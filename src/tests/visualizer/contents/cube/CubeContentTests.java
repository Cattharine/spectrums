package tests.visualizer.contents.cube;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tests.visualizer.painter.TestPainter;
import visualizer.contents.common.Face;
import visualizer.contents.cube.CubeConstants;
import visualizer.contents.cube.CubeContent;
import visualizer.contents.utils.Coordinates;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.function.BiFunction;

public class CubeContentTests {
    private TestCube expected;
    private CubeContent content;

    @Before
    public void prepare() {
        expected = new TestCube();
        content = new CubeContent(expected.commonContent);
    }

    @Test
    public void checkCreation() {
        var painter = getPainted();
        checkVertexes(painter, -1, null);
        Assert.assertEquals(0, painter.face.size());
        checkOffset(painter);
    }

    @Test
    public void checkShiftAll() {
        checkShiftAll(new int[]{-1, 12});
        prepare();
        checkShiftAll(new int[]{117, -20});
    }

    private void checkShiftAll(int[] difference) {
        content.shiftAll(difference);
        expected.offset.add(difference);

        var painter = getPainted();
        checkVertexes(painter, -1, null);
        Assert.assertEquals(0, painter.face.size());

        checkOffset(painter);
    }

    @Test
    public void checkSelectAndDeselect() {
        for (int chosen = 0; chosen < expected.numberOfVertexes; chosen++) {
            checkSelect(getCoordinatesWithOffset(chosen), chosen);
        }
        checkDeselect();
    }

    private void checkSelect(Coordinates coordinates, int chosen) {
        content.select(coordinates.getIntValue());
        var painter = getPainted();
        checkVertexes(painter, chosen, null);
        Assert.assertEquals(chosen > -1, content.canDeselectAll());
    }

    private void checkDeselect() {
        checkSelect(new Coordinates(-900, -900), -1);
        checkSelect(getCoordinatesWithOffset(0), 0);
        content.deselectAll();
        var painter = getPainted();
        checkVertexes(painter, -1, null);
    }

    @Test
    public void checkMoveSelectedNoSelected() {
        content.moveSelected(new int[]{-900, 162});
        var painter = getPainted();
        checkVertexes(painter, -1, null);
        Assert.assertEquals(0, painter.face.size());
        checkOffset(painter);
    }

    @Test
    public void checkMoveSelected() {
        var difference = new int[]{120, -34};
        for (int chosen = 0; chosen < expected.numberOfVertexes; chosen++) {
            prepare();
            checkMoveSelected(chosen, difference);
            content.deselectAll();
            checkMoveSelected(chosen, difference);
        }
    }

    private void checkMoveSelected(int chosen, int[] difference) {
        content.select(getCoordinatesWithOffset(chosen).getIntValue());
        content.moveSelected(difference);
        expected.appendTransform(getMoveSelectedTransform(difference, chosen), 1);
        var painter = getPainted();
        checkVertexes(painter, chosen, null);
    }

    @Test
    public void checkScaling() {
        var relationOfScaling = 2.2;
        for (int chosen = 0; chosen < expected.numberOfVertexes; chosen++) {
            checkScaling(chosen, relationOfScaling);
        }
    }

    private void checkScaling(int chosen, double relationOfScaling) {
        prepare();
        scale(chosen, relationOfScaling);

        var painter = getPainted();
        checkVertexes(painter, -1, null);
        checkOffset(painter);
    }

    private void scale(int chosen, double relationOfScaling) {
        var cursorPosition = getCoordinatesWithOffset(chosen).getIntValue();
        content.changeScale(cursorPosition, relationOfScaling);

        expected.setCurrentTransform(getScaleTransform(relationOfScaling), relationOfScaling);
        var difference = expected.offset.copySubtract(cursorPosition).copyMul(relationOfScaling);
        expected.offset.setValue(new Coordinates(cursorPosition).copyAdd(difference));
    }

    @Test
    public void checkSelectAfterScaling() {
        var relationOfScaling = 2.2;
        for (int chosen = 0; chosen < expected.numberOfVertexes; chosen++) {
            checkSelectAfterScaling(chosen, relationOfScaling);
        }
    }

    private void checkSelectAfterScaling(int chosen, double relationOfScaling) {
        checkScaling(chosen, relationOfScaling);
        for (int vertex = 0; vertex < expected.numberOfVertexes; vertex++) {
            checkSelect(getCoordinatesWithOffset(vertex), vertex);
        }
    }

    @Test
    public void checkSwitchingState() {
        content.switchState();
        var painter = getPainted();
        checkVertexes(painter, -1, getAllVertexes(0));
        checkFace(painter, new int[]{24, 28, 12, 14, 6, 7, 3, 19, 17, 25});
        checkOffset(painter);
    }

    @Test
    public void checkChangingFace() {
        checkBaseChangingFace(Integer.parseInt("01011", 3), new int[]{16, 20, 4, 0});
        checkBaseChangingFace(Integer.parseInt("10100", 3), new int[]{8, 10, 2, 3, 1, 9});
        checkBaseChangingFace(Integer.parseInt("12120", 3), new int[]{10, 11});
        checkBaseChangingFace(Integer.parseInt("20012", 3), new int[]{25, 29, 21, 17});
    }

    private void checkBaseChangingFace(int face, int[] expected) {
        prepare();
        content.switchState();
        checkChangingFace(face, expected, true);
    }

    @Test
    public void checkConvexHullOfFace() {
        checkConvexHullOfFace(0, new int[]{16, 24, 28, 30, 31, 15, 7, 3, 1, 0},
                new Coordinates[]{
                        new Coordinates(0, 60),
                        new Coordinates(45, 45),
                        new Coordinates(75, 25),
                        new Coordinates(110, 0),
                        new Coordinates(0, -70)
                }, true);
        checkConvexHullOfFace(Integer.parseInt("20100", 3), new int[]{24, 16, 18, 19, 27, 25},
                new Coordinates[]{
                        new Coordinates(0, 60),
                        new Coordinates(45, 34),
                        new Coordinates(-200, 200),
                        new Coordinates(-100, 100),
                        new Coordinates(-12, -12)
                }, true);
        // cannot find every vertex so should not check them
        checkConvexHullOfFace(Integer.parseInt("22010", 3), new int[]{24, 25},
                new Coordinates[]{
                        new Coordinates(0, 60),
                        new Coordinates(45, 45),
                        new Coordinates(0, 0),
                        new Coordinates(100, 0),
                        new Coordinates(45, -12)
                }, false);
        checkConvexHullOfFace(Integer.parseInt("02020", 3), new int[]{10, 14, 30, 31, 27, 11},
                new Coordinates[]{
                        new Coordinates(0, 6),
                        new Coordinates(14, 15),
                        new Coordinates(10, 0),
                        new Coordinates(-12, 12),
                        new Coordinates(20, 0)
                }, true);
    }

    private void checkConvexHullOfFace(int face, int[] expectedHull,
                                       Coordinates[] axisCoordinates, boolean checkVertexes) {
        prepare();
        content.switchState();
        expected.setCurrentTransform(getAxisCoordinatesTransform(axisCoordinates), 1);
        checkChangingFace(face, expectedHull, checkVertexes);
    }

    private void checkChangingFace(int face, int[] expectedHull, boolean checkVertexes) {
        content.changeFace(face);

        var painter = getPainted();
        if (checkVertexes)
            checkVertexes(painter, -1, getAllVertexes(face));
        checkFace(painter, expectedHull);
        checkOffset(painter);

        //select 0
        if (checkVertexes) {
            content.select(expected.offset.getIntValue());
            painter = getPainted();
            checkVertexes(painter, 0, getAllVertexes(face));
        }
    }

    @Test
    public void checkSelectingFromOtherContent() {
        var length = CubeConstants.getMinAxisLength();
        checkSelectingFromOtherContent(0, new int[]{24, 28, 12, 14, 6, 7, 3, 19, 17, 25},
                expected.offset);
        var faceToCheck = "20202";
        checkSelectingFromOtherContent(Integer.parseInt(faceToCheck, 3), new int[]{29, 31, 23, 21},
                expected.getBaseOffset(faceToCheck).copyMul(length));
        faceToCheck = "10120";
        checkSelectingFromOtherContent(Integer.parseInt(faceToCheck, 3), new int[]{10, 2, 3, 11},
                expected.getBaseOffset(faceToCheck).copyMul(length));
    }

    private void checkSelectingFromOtherContent(int face, int[] expectedHull, Coordinates offset) {
        prepare();
        content.switchState();
        content.selectFromOtherContent(face);
        expected.offset = offset;

        var painter = getPainted();
        checkVertexes(painter, -1, getAllVertexes(face));
        checkFace(painter, expectedHull);
        checkOffset(painter);

        //select 0
        content.select(expected.offset.getIntValue());
        painter = getPainted();
        checkVertexes(painter, 0, getAllVertexes(face));
    }

    private void checkFace(TestPainter painter, int[] expectedHull) {
        Assert.assertEquals(painter.face.size(), expectedHull.length);
        for (int i = 0; i < expectedHull.length; i++) {
            Assert.assertEquals(expectedHull[i], (int) painter.face.get(i));
        }
        Assert.assertEquals(expectedHull.length > 0, content.canChangeFace());
    }

    private void checkVertexes(TestPainter painter, int chosen, HashSet<Integer> currentVertexes) {
        for (int vertex = 0; vertex < expected.numberOfVertexes; vertex++) {
            var isCurrent = currentVertexes != null && currentVertexes.contains(vertex);
            checkVertex(vertex, painter, vertex == chosen, isCurrent);
            checkEdgesOfVertex(vertex, painter, currentVertexes);
        }
    }

    private void checkVertex(int vertex, TestPainter painter, boolean isSelected, boolean isCurrent) {
        var type = isCurrent ? ElementType.CURRENT_VERTEX : ElementType.VERTEX;
        Assert.assertTrue(painter.vertexes.get(type).contains(vertex));
        Assert.assertTrue(painter.vertexesStates.containsKey(vertex));
        Assert.assertTrue(painter.vertexesStates.get(vertex).contains(ElementState.BASE));
        Assert.assertEquals(expected.activeVertexes.contains(vertex),
                painter.vertexesStates.get(vertex).contains(ElementState.ACTIVE));
        Assert.assertEquals(isSelected, painter.vertexesStates.get(vertex).contains(ElementState.SELECTED));
    }

    private void checkEdgesOfVertex(int vertex, TestPainter painter, HashSet<Integer> current) {
        for (int other = 0; other < expected.numberOfVertexes; other++) {
            int difference = vertex ^ other;
            if ((difference & (difference - 1)) != 0 || difference == 0)
                continue;
            int min = Math.min(vertex, other);
            int max = Math.max(vertex, other);
            boolean isCurrent = current != null && current.contains(min) && current.contains(max);
            ElementType type = isCurrent ? ElementType.CURRENT_EDGE : ElementType.EDGE;
            Assert.assertTrue(painter.edges.get(type).containsKey(min));
            Assert.assertTrue(painter.edges.get(type).get(min).contains(max));
        }
    }

    private void checkOffset(TestPainter painter) {
        Assert.assertEquals(expected.offset.getX(), painter.offset.getX(), 1e-6);
        Assert.assertEquals(expected.offset.getY(), painter.offset.getY(), 1e-6);
    }

    public Integer map(int[] value) {
        return expected.map(value);
    }

    private BiFunction<Coordinates, Integer, Coordinates> getMoveSelectedTransform(int[] difference, int selected) {
        return (Coordinates value, Integer vertex) -> {
            int numberOfFactors = getBinToDist(selected);
            double numberOfCommonFactors = getBinToDist(vertex & selected);
            if (numberOfFactors > 0)
                return value.copyAdd(new Coordinates(difference).copyMul(numberOfCommonFactors / numberOfFactors));
            return value;
        };
    }

    private int getBinToDist(int value) {
        try {
            var binToDistMethod = Face.class.getDeclaredMethod("convertBinToDist", int.class);
            binToDistMethod.setAccessible(true);
            return (int) binToDistMethod.invoke(null, value);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private BiFunction<Coordinates, Integer, Coordinates> getScaleTransform(double relationOfScaling) {
        return (Coordinates value, Integer vertex) -> value.copyMul(relationOfScaling);
    }

    private TestPainter getPainted() {
        var painter = new TestPainter(this::map);
        content.paint(painter);
        return painter;
    }

    private Coordinates getCoordinatesWithOffset(int vertex) {
        return expected.getTransformedCoordinates(vertex).copyAdd(expected.offset);
    }

    private BiFunction<Coordinates, Integer, Coordinates> getAxisCoordinatesTransform(Coordinates[] axisCoordinates) {
        try {
            var coordinatesClass = CubeContent.CubeCoordinates.class;
            var instance = getFieldValue(CubeContent.class, "coordinates", content);

            var axisCoordinatesField = coordinatesClass.getDeclaredField("axisCoordinates");
            axisCoordinatesField.setAccessible(true);
            axisCoordinatesField.set(instance, axisCoordinates);

            var fillCoordinatesMethod = coordinatesClass.getDeclaredMethod("fillVertexCoordinates");
            fillCoordinatesMethod.setAccessible(true);
            fillCoordinatesMethod.invoke(instance);

            var fieldName = "vertexCoordinates";
            var vertexes = (Coordinates[]) getFieldValue(coordinatesClass, fieldName, instance);
            return (Coordinates value, Integer vertex) -> vertexes[vertex];
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getFieldValue(Class<?> clazz, String fieldName, Object toTakeFrom)
            throws NoSuchFieldException, IllegalAccessException {
        var field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(toTakeFrom);
    }

    private HashSet<Integer> getAllVertexes(int faceName) {
        return expected.getAllVertexes(faceName);
    }
}