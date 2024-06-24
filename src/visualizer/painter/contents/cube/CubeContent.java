package visualizer.painter.contents.cube;

import visualizer.painter.contents.ElementType;
import visualizer.painter.contents.IContent;
import visualizer.painter.instances.EdgeInfo;
import visualizer.painter.painter.IPainter;
import visualizer.painter.painter.StrokeType;

import java.util.ArrayList;

public class CubeContent implements IContent {
    private final CubeCoordinates cube;
    private int selected = -1;
    private int currentFace = 0;
    private int[] vertexesOfCurrentFace;
    private CubeContentState state = CubeContentState.PLAIN;

    public CubeContent(Cube cube) {
        this.cube = new CubeCoordinates(cube);
        vertexesOfCurrentFace = cube.getAllVertexesInFace(currentFace);
    }

    public void shiftAll(int[] difference) {
        cube.changeOffset(difference);
    }

    public void changeScale(int[] cursorPosition, double relationOfScaling) {
        cube.changeScale(cursorPosition, relationOfScaling);
    }

    public void select(int[] position) {
        int numberOfVertexes = 1 << cube.cube.n;
        for (var i = 0; i < numberOfVertexes; i++) {
            if (cube.canSelect(i, position)) {
                selected = i;
                return;
            }
        }
        selected = -1;
    }

    public void deselectAll() {
        selected = -1;
    }

    public void moveSelected(int[] difference) {
        if (selected > -1) {
            var activeFactors = cube.cube.getActiveFactors(selected);
            cube.moveSelected(difference, activeFactors);
        }
    }

    public void paint(IPainter painter) {
        drawBaseEdges(painter);
        if (state != CubeContentState.PLAIN)
            highlightCurrentFace(painter);
        drawVertexes(painter);
    }

    private void drawBaseEdges(IPainter painter) {
        var numberOfVertexes = 1 << cube.cube.n;
        for (var vertexValue = 0; vertexValue < numberOfVertexes; vertexValue++) {
            for (var factor = 0; factor < cube.cube.n; factor++) {
                var secondValue = vertexValue ^ cube.cube.getAxisChecker(factor);
                if (vertexValue > secondValue) {
                    var first = cube.transformToDrawable(vertexValue);
                    var second = cube.transformToDrawable(secondValue);
                    painter.drawLine(first, second, ElementType.BASE, StrokeType.BASE);
                }
            }
        }
    }

    private void highlightCurrentFace(IPainter painter) {
        var faceName = cube.cube.getFaceName(currentFace);
        fillFace(painter, faceName);
        highlightEdgesOfFace(painter, faceName);
    }

    private void fillFace(IPainter painter, String faceName) {
        var mostLeftValue = cube.getMostLeftValueInFace(faceName);
        var edges = cube.getSortedEdgesOfFace(faceName, mostLeftValue);
        fillOutlined(painter, edges, mostLeftValue);
    }

    private void fillOutlined(IPainter painter, ArrayList<EdgeInfo> edges, int mostLeft) {
        int m = edges.size();
        if (m > 0) {
            var points = new ArrayList<int[]>();
            points.add(cube.transformToDrawable(mostLeft));
            int current = mostLeft;
            for (var i = 0; i < m * 2 - 1; i++) {
                var nextValue = current ^ cube.cube.getAxisChecker(edges.get(i % m).factor);
                points.add(cube.transformToDrawable(nextValue));
                current = nextValue;
            }

            painter.fillPolygon(points, ElementType.FACE);
        }
    }

    private void highlightEdgesOfFace(IPainter painter, String name) {
        var freeFactors = cube.cube.getFreeFactorsInFace(name);
        highlightPaths(painter);
        drawActiveEdges(painter, freeFactors);
    }

    private void highlightPaths(IPainter painter) {
        if (selected > -1 && cube.cube.isVertexInFace(currentFace, selected))
            for (var otherValue : vertexesOfCurrentFace) {
                if (cube.cube.isPath(currentFace, selected, otherValue)) {
                    drawPath(painter, selected, otherValue);
                }
            }
    }

    private void drawPath(IPainter painter, int vertex, int other) {
        var diff = vertex ^ other;
        var activeFactors = cube.cube.getActiveFactors(diff);
        var current = vertex;
        for (var factor : activeFactors) {
            var next = current ^ cube.cube.getAxisChecker(factor);

            var first = cube.transformToDrawable(current);
            var second = cube.transformToDrawable(next);

            painter.drawLine(first, second, ElementType.MAX_MU, StrokeType.SUPER);

            current = next;
        }
    }

    private void drawActiveEdges(IPainter painter, ArrayList<Integer> factors) {
        for (var vertexValue : vertexesOfCurrentFace) {
            for (Integer factor : factors) {
                var otherVertexValue = vertexValue ^ cube.cube.getAxisChecker(factor);
                if (vertexValue > otherVertexValue) {
                    var first = cube.transformToDrawable(vertexValue);
                    var second = cube.transformToDrawable(otherVertexValue);

                    painter.drawLine(first, second, ElementType.BASE, StrokeType.BOLD);
                }
            }
        }
    }

    private void drawVertexes(IPainter painter) {
        if (selected > -1)
            drawVertex(painter, selected, ElementType.SELECTED);
        var numberOfVertexes = 1 << cube.cube.n;
        var activeIndex = 0;
        for (var value = 0; value < numberOfVertexes; value++) {
            if (state != CubeContentState.PLAIN && cube.cube.isMaxMuVertex(value, currentFace))
                drawVertex(painter, value, ElementType.MAX_MU);
            if (cube.cube.isActiveVertex(value) &&
                    (state == CubeContentState.PLAIN || !cube.cube.isMaxMuVertex(value, currentFace)))
                drawVertex(painter, value, ElementType.ACTIVE);
            switch (state) {
                case PLAIN -> drawVertex(painter, value, ElementType.BASE);
                case FACES -> {
                    if (activeIndex < vertexesOfCurrentFace.length && value == vertexesOfCurrentFace[activeIndex]) {
                        drawVertex(painter, value, ElementType.BASE);
                        activeIndex++;
                    } else drawVertex(painter, value, ElementType.HIDDEN);
                }
            }
        }
    }

    private void drawVertex(IPainter painter, int value, ElementType type) {
        var transformed = cube.transformToDrawable(value);
        var diameter = CubeContentConstants.getDiameter(type);
        painter.drawDisc(transformed, diameter, diameter, type);
        var name = cube.cube.getVertexName(value);
        switch (type) {
            case BASE, HIDDEN -> painter.drawString(name, CubeContentConstants.getShiftedForString(transformed), type);
            default -> {
            }
        }
    }

    public void changeCurrentFaceNum(int direction) {
        var faceNum = cube.cube.getNumberOfFaces();
        currentFace = (currentFace + direction + faceNum) % faceNum;
        while (currentFaceIsVertex()) {
            currentFace = (currentFace + direction + faceNum) % faceNum;
        }
        vertexesOfCurrentFace = cube.cube.getAllVertexesInFace(currentFace);
    }

    private boolean currentFaceIsVertex() {
        return cube.cube.getFreeFactorsInFace(currentFace).isEmpty();
    }

    public void setCurrentFaceNum(int newValue) {
        assert newValue > -1 && newValue < cube.cube.getNumberOfFaces();
        currentFace = newValue;
        if (currentFaceIsVertex())
            changeCurrentFaceNum(1);
        vertexesOfCurrentFace = cube.cube.getAllVertexesInFace(currentFace);
        cube.focusOnTheCentreOfVertexes(vertexesOfCurrentFace);
    }

    public void switchState() {
        state = switch (state) {
            case PLAIN -> CubeContentState.FACES;
            case FACES -> CubeContentState.PLAIN;
        };
    }

    public void setState(CubeContentState newState) {
        state = newState;
    }

    public CubeContentState getState() {
        return state;
    }

    public int getCurrentFace() {
        return currentFace;
    }

    public boolean canGoToSelected() {
        return state == CubeContentState.FACES;
    }

    public boolean canDeselectAll() {
        return selected > -1;
    }

    public String getSpectrum() {
        return cube.cube.getSpectrum();
    }
}
