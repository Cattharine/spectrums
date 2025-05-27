package visualizer.contents.cube;

import visualizer.contents.common.CommonContent;
import visualizer.contents.utils.*;
import visualizer.contents.utils.contentInterfaces.IContentPart;
import visualizer.contents.utils.elementInfo.ElementInfo;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;
import visualizer.painter.IPainter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CubeContent implements IContentPart {
    private final CommonContent common;
    private final CubeCoordinates coordinates;
    private int selectedVertex = -1;
    private int currentFace = 0;
    private SoftReference<HashSet<Integer>> vertexesOfCurrentFace;
    private CubeContentState state = CubeContentState.PLAIN;

    public CubeContent(CommonContent commonContent) {
        common = commonContent;
        coordinates = new CubeCoordinates();
        var currentVertexes = common.cube.getFace(currentFace).info.getAllVertexes();
        vertexesOfCurrentFace = new SoftReference<>(currentVertexes);
        coordinates.focusOnCentreOfVertexes(currentVertexes);
    }

    @Override
    public void shiftAll(int[] difference) {
        coordinates.changeOffset(difference);
    }

    @Override
    public void changeScale(int[] cursorPosition, double relationOfScaling) {
        coordinates.changeScale(cursorPosition, relationOfScaling);
    }

    @Override
    public void select(int[] position) {
        int numberOfVertexes = 1 << common.getN();
        for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
            if (coordinates.canSelect(vertex, position)) {
                selectedVertex = vertex;
                return;
            }
        }

        selectedVertex = -1;
    }

    @Override
    public void deselectAll() {
        selectedVertex = -1;
    }

    @Override
    public void moveSelected(int[] difference) {
        if (selectedVertex > -1) {
            coordinates.moveSelected(difference);
        }
    }

    @Override
    public boolean canGoToSelected() {
        return state == CubeContentState.FACES;
    }

    @Override
    public int getSelected() {
        return currentFace;
    }

    @Override
    public void selectFromOtherContent(int faceValue) {
        // selected from tree
        if (common.cube.getFace(faceValue) == null)
            return;
        state = CubeContentState.FACES;
        currentFace = faceValue;
        if (common.cube.getFace(currentFace).info.dimension == 0)
            changeFace(1);
        var currentVertexes = common.cube.getFace(currentFace).info.getAllVertexes();
        vertexesOfCurrentFace = new SoftReference<>(currentVertexes);
        coordinates.focusOnCentreOfVertexes(currentVertexes);
    }

    @Override
    public boolean canDeselectAll() {
        return selectedVertex > -1;
    }

    @Override
    public void paint(IPainter painter) {
        new CubePainter(painter).paint();
    }

    @Override
    public void switchState() {
        state = switch (state) {
            case PLAIN -> CubeContentState.FACES;
            case FACES -> CubeContentState.PLAIN;
        };
    }

    @Override
    public void changeFace(int direction) {
        if (!canChangeFace())
            return;
        int numberOfFaces = common.getNumberOfFaces();
        do {
            currentFace = (currentFace + direction + numberOfFaces) % numberOfFaces;
        } while (common.cube.getFace(currentFace).info.dimension == 0);

        var currentVertexes = common.cube.getFace(currentFace).info.getAllVertexes();
        vertexesOfCurrentFace = new SoftReference<>(currentVertexes);
    }

    @Override
    public boolean canChangeFace() {
        return state == CubeContentState.FACES;
    }

    public class CubeCoordinates {
        private Coordinates[] axisCoordinates;
        private Coordinates[] vertexCoordinates;
        private final Coordinates offset = new Coordinates();
        private double scale = 1;

        private CubeCoordinates() {
            fillAxisCoordinates();
            fillVertexCoordinates();
        }

        private void fillAxisCoordinates() {
            int n = common.getN();
            axisCoordinates = new Coordinates[n];
            var angle = 2 * Math.PI / (n / 2 * 2 + 1);
            var minLength = CubeConstants.getMinAxisLength();
            for (var i = 0; i < n; i++) {
                var length = minLength * (i + 1);
                var x = length * Math.sin(i * angle);
                var y = length * Math.cos(i * angle);
                axisCoordinates[i] = new Coordinates(x, y);
            }
        }

        private void fillVertexCoordinates() {
            vertexCoordinates = new Coordinates[1 << common.getN()];
            for (var vertex = 0; vertex < vertexCoordinates.length; vertex++) {
                vertexCoordinates[vertex] = new Coordinates();
                for (var factor = 0; factor < common.getN(); factor++) {
                    if (common.cube.isFactorPresented(vertex, factor)) {
                        vertexCoordinates[vertex].add(axisCoordinates[factor].copyMul(scale));
                    }
                }
            }
        }

        private void focusOnCentreOfVertexes(HashSet<Integer> vertexes) {
            var centre = new Coordinates();
            for (var vertex : vertexes) {
                centre.add(vertexCoordinates[vertex]);
            }

            if (vertexes.size() > 0)
                centre.mul(-1.0 / vertexes.size());
            offset.setValue(centre);
        }

        private void changeOffset(int[] difference) {
            offset.add(difference);
        }

        private void changeScale(int[] cursorPosition, double relationOfScaling) {
            scale *= relationOfScaling;
            offset.setValue(offset
                    .copySubtract(cursorPosition)
                    .copyMul(relationOfScaling)
                    .copyAdd(cursorPosition)
            );

            fillVertexCoordinates();
        }

        public boolean canSelect(int vertexValue, int[] position) {
            var vertexPos = vertexCoordinates[vertexValue];
            var difference = vertexPos.copyAdd(offset).copySubtract(position).getAbs();
            var radius = CubeConstants.getDiameter(ElementState.SELECTED) / 2.0;
            return Math.max(difference.getX(), difference.getY()) <= radius;
        }

        private void moveSelected(int[] difference) {
            ArrayList<Integer> presentedFactors = common.cube.getPresentedFactors(selectedVertex);
            var divider = 1.0 / (presentedFactors.size() * scale);
            var dividedDifference = new Coordinates(difference).copyMul(divider);

            for (var factor : presentedFactors) {
                axisCoordinates[factor].add(dividedDifference);
            }
            fillVertexCoordinates();
        }

        private int getMostLeftVertexInFace(int faceValue) {
            int mostLeft = 0;

            for (int factor = 0; factor < common.getN(); factor++) {
                var face = common.cube.getFace(faceValue).info;
                boolean isPartOfBaseVertex = face.isFactorPresented(factor);
                boolean canMoveLeftOrStraightUp = face.isFactorFree(factor) && isAxisDirectedLeftOrStraightUp(factor);

                if (isPartOfBaseVertex || canMoveLeftOrStraightUp)
                    mostLeft = mostLeft ^ CommonContent.getFactorValue(factor);
            }

            return mostLeft;
        }

        private boolean isAxisDirectedLeftOrStraightUp(int factor) {
            var coordinates = axisCoordinates[factor];
            return coordinates.getX() < 0 || (coordinates.getX() == 0 && coordinates.getY() < 0);
        }

        private ArrayList<EdgeInfo> getSortedEdgesOfFace(int face, int mostLeft) {
            var edges = new ArrayList<EdgeInfo>();

            for (var factor = 0; factor < common.getN(); factor++) {
                if (common.cube.getFace(face).info.isFactorFree(factor)) {
                    var edgeEnd = mostLeft ^ CommonContent.getFactorValue(factor);
                    var edge = vertexCoordinates[edgeEnd].copySubtract(vertexCoordinates[mostLeft]);
                    if (edge.getSquaredLength() > 0) {
                        edges.add(new EdgeInfo(edge, factor));
                    }
                }
            }

            // по возрастанию синусов во внутреннем представлении (визуально по убыванию)
            edges.sort(EdgeInfo::compareEdges);
            return edges;
        }

        private int[] transformToDrawable(Coordinates coordinates) {
            return coordinates.getIntValue();
        }

        private int[] transformToDrawable(int value) {
            return transformToDrawable(vertexCoordinates[value]);
        }
    }

    public static class EdgeInfo {
        public final Coordinates coordinates;
        public final int factor;

        public EdgeInfo(Coordinates coordinates, int factor) {
            this.coordinates = coordinates;
            this.factor = factor;
        }

        public static int compareEdges(EdgeInfo first, EdgeInfo second) {
            var p1 = first.coordinates;
            var p2 = second.coordinates;
            var squaredLength1 = p1.getSquaredLength();
            var squaredLength2 = p2.getSquaredLength();
            var sin1 = p1.getY() / Math.sqrt(squaredLength1);
            var sin2 = p2.getY() / Math.sqrt(squaredLength2);
            int result = Double.compare(sin1, sin2);
            if (result != 0)
                return result;
            System.out.println(12);
            return Double.compare(squaredLength1, squaredLength2);
        }
    }

    public class CubePainter {
        private final IPainter painter;

        private CubePainter(IPainter painter) {
            this.painter = painter;
        }

        private void paint() {
            painter.offset(coordinates.offset);
            drawEdges();
            drawVertexes();
            highlightFace();
        }

        private void drawEdges() {
            int n = common.getN();
            int numberOfVertexes = 1 << n;
            var baseOfPath = getBaseOfPath();
            for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
                for (int factor = 0; factor < n; factor++) {
                    drawEdge(vertex, factor, baseOfPath.getOrDefault(vertex, null));
                }
                baseOfPath.remove(vertex);
            }
        }

        private HashMap<Integer, HashSet<Integer>> getBaseOfPath() {
            if (state == CubeContentState.PLAIN || !isCurrentVertex(selectedVertex))
                return new HashMap<>();
            return common.cube.getFace(currentFace).getPathsToClosestVertexes(selectedVertex);
        }

        private void drawEdge(int vertex, int factor, HashSet<Integer> baseOfPath) {
            int otherVertex = vertex ^ CommonContent.getFactorValue(factor);

            if (vertex > otherVertex)
                return;

            var first = coordinates.transformToDrawable(vertex);
            var second = coordinates.transformToDrawable(otherVertex);
            var elements = collectEdgeInfo(vertex, otherVertex, baseOfPath);

            drawLines(first, second, elements);
        }

        private ArrayList<ElementInfo> collectEdgeInfo(int vertex, int otherVertex, HashSet<Integer> baseOfPath) {
            var elements = new ArrayList<ElementInfo>();

            var type = isCurrent(vertex, otherVertex) ? ElementType.CURRENT_EDGE : ElementType.EDGE;
            elements.add(new ElementInfo(type, ElementState.BASE));

            if (baseOfPath != null && baseOfPath.contains(otherVertex)) {
                if (common.isImplementingSpectrum(currentFace))
                    elements.add(new ElementInfo(ElementType.PATH, ElementState.MAX));
                else elements.add(new ElementInfo(ElementType.PATH, ElementState.SEMI_MAX));
            }

            return elements;
        }

        private boolean isCurrent(int vertex, int other) {
            boolean shouldDraw = state == CubeContentState.FACES;
            boolean isPartOfFace = isCurrentVertex(vertex) && isCurrentVertex(other);
            return shouldDraw && isPartOfFace;
        }

        private boolean isCurrentVertex(int vertex) {
            var value = vertexesOfCurrentFace.get();
            if (value != null)
                return value.contains(vertex);
            return common.cube.getFace(currentFace).info.containsVertex(vertex);
        }

        private void drawLines(int[] first, int[] second, ArrayList<ElementInfo> elements) {
            for (var info : elements) {
                painter.drawLine(first, second, info);
            }
        }

        private void drawVertexes() {
            int numberOfVertexes = 1 << common.getN();

            for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
                ArrayList<ElementInfo> elements = collectVertexInfo(vertex);
                drawVertex(elements, vertex);
            }
        }

        private ArrayList<ElementInfo> collectVertexInfo(int vertex) {
            ArrayList<ElementInfo> elements = new ArrayList<>();
            ElementType type = isCurrent(vertex, vertex) ? ElementType.CURRENT_VERTEX : ElementType.VERTEX;

            if (vertex == selectedVertex)
                elements.add(new ElementInfo(type, ElementState.SELECTED));
            if (isPathEnd(vertex))
                elements.add(new ElementInfo(type, ElementState.SEMI_SELECTED));
            if (common.cube.isActiveVertex(vertex))
                elements.add(new ElementInfo(type, ElementState.ACTIVE));
            if (state != CubeContentState.PLAIN && common.cube.isMaxVertex(vertex, currentFace)) {
                if (common.isImplementingSpectrum(currentFace))
                    elements.add(new ElementInfo(type, ElementState.MAX));
                else elements.add(new ElementInfo(type, ElementState.SEMI_MAX));
            }
            elements.add(new ElementInfo(type, ElementState.BASE));
            return elements;
        }

        private boolean isPathEnd(int vertex) {
            var hasPath = selectedVertex > -1 && state == CubeContentState.FACES;
            if (!hasPath)
                return false;
            var closestVertexes = common.cube.getFace(currentFace).getClosestVertexes(selectedVertex);
            return closestVertexes.contains(vertex);
        }

        private void drawVertex(ArrayList<ElementInfo> elements, int value) {
            var transformed = coordinates.transformToDrawable(value);
            for (var info : elements) {
                var diameter = CubeConstants.getDiameter(info.state);
                painter.drawDisc(transformed, diameter, diameter, info);
                var name = common.cube.getVertexName(value);
                if (info.state == ElementState.BASE)
                    painter.drawString(name, CubeConstants.getShiftedForString(transformed), info);
            }
        }

        private void highlightFace() {
            if (state == CubeContentState.PLAIN)
                return;
            int mostLeft = coordinates.getMostLeftVertexInFace(currentFace);
            var edges = coordinates.getSortedEdgesOfFace(currentFace, mostLeft);
            fillFace(mostLeft, edges);
        }

        private void fillFace(int mostLeft, ArrayList<EdgeInfo> edges) {
            int numberOfSides = edges.size();
            if (numberOfSides == 0)
                return;
            var points = new ArrayList<int[]>();
            int current = mostLeft;
            for (int side = 0; side < numberOfSides * 2; side++) {
                points.add(coordinates.transformToDrawable(current));
                var edge = edges.get(side % numberOfSides);
                current = current ^ CommonContent.getFactorValue(edge.factor);
            }
            painter.fillPolygon(points, new ElementInfo(ElementType.FACE, ElementState.BASE));
        }
    }
}
