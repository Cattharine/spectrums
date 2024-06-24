package visualizer.painter.contents.cube;

import visualizer.painter.contents.ElementType;
import visualizer.painter.instances.Coordinates;
import visualizer.painter.instances.EdgeInfo;

import java.util.ArrayList;

public class CubeCoordinates {
    public final Cube cube;
    private Coordinates[] axisCoordinates;
    private Coordinates[] vertexCoordinates;
    private final Coordinates offset;
    private double scale = 1;

    public CubeCoordinates(Cube cube) {
        this.cube = cube;
        fillAxisCoordinates();
        fillVertexCoordinates();
        offset = getCentreOfVertexes().copyMul(-1);
    }

    private void fillAxisCoordinates() {
        axisCoordinates = new Coordinates[cube.n];
        var angle = Math.PI / (cube.n + 1);
        double t = CubeContentConstants.p * (cube.n + 1) / (cube.n + 2);
        for (var i = 0; i < cube.n; i++) {
            axisCoordinates[i] = new Coordinates();
            var length = CubeContentConstants.p + t * i;
            axisCoordinates[i].setValue(length * Math.sin(i * angle), length * Math.cos(i * angle));
        }
    }

    private void fillVertexCoordinates() {
        vertexCoordinates = new Coordinates[1 << cube.n];
        for (var i = 0; i < vertexCoordinates.length; i++) {
            vertexCoordinates[i] = new Coordinates();
            for (var j = 0; j < cube.n; j++) {
                if ((i & cube.getAxisChecker(j)) > 0) {
                    vertexCoordinates[i].add(axisCoordinates[j].copyMul(scale));
                }
            }
        }
    }

    private Coordinates getCentreOfVertexes() {
        var centre = new Coordinates();
        for (var vertex : vertexCoordinates) {
            centre.add(vertex);
        }
        if (vertexCoordinates.length > 0)
            centre.mul(1.0 / vertexCoordinates.length);
        return centre;
    }

    public void focusOnTheCentreOfVertexes(int[] vertexes) {
        var centre = new Coordinates();
        for (var value : vertexes) {
            centre.add(vertexCoordinates[value]);
        }

        if (vertexes.length > 0)
            centre.mul(-1.0 / vertexes.length);
        offset.setValue(centre);
    }

    public void changeOffset(int[] difference) {
        offset.add(difference);
    }

    public void changeScale(int[] cursorPosition, double relationOfScaling) {
        scale *= relationOfScaling;
        offset.setValue(offset
                .copySubtract(cursorPosition)
                .copyMul(relationOfScaling)
                .copyAdd(cursorPosition));

        fillVertexCoordinates();
    }

    public boolean canSelect(int vertexValue, int[] position) {
        var vertexPos = vertexCoordinates[vertexValue];
        var difference = vertexPos
                .copyMul(-1)
                .copySubtract(offset)
                .copyAdd(position).getAbs();
        var radius = CubeContentConstants.getDiameter(ElementType.SELECTED) / 2.0;
        return Math.max(difference.getX(), difference.getY()) <= radius;
    }

    public void moveSelected(int[] difference, ArrayList<Integer> activeFactors) {
        var diff = new Coordinates();
        diff.setValue(difference);
        var k = activeFactors.size();
        var commonDiff = diff.copyMul(1.0 / (k * scale));

        for (var factor : activeFactors) {
            axisCoordinates[factor].add(commonDiff);
        }
        fillVertexCoordinates();
    }

    public int getMostLeftValueInFace(String name) {
        var mostLeft = 0;

        for (var factor = 0; factor < name.length(); factor++) {
            if (cube.isFactorFixedAsOne(factor, name) || isLefterOrRightAbove(name, factor))
                mostLeft = mostLeft ^ cube.getAxisChecker(factor);
        }

        return mostLeft;
    }

    private boolean isLefterOrRightAbove(String name, int factorPos) {
        var coordinates = axisCoordinates[factorPos];
        return cube.isFactorFree(factorPos, name) &&
                (coordinates.getX() < 0 || (coordinates.getX() == 0 && coordinates.getY() < 0));
    }

    public ArrayList<EdgeInfo> getSortedEdgesOfFace(String name, int mostLeft) {
        var edges = new ArrayList<EdgeInfo>();

        for (var factor = 0; factor < name.length(); factor++) {
            if (cube.isFactorFree(factor, name)) {
                var edgeEndValue = mostLeft ^ cube.getAxisChecker(factor);
                var edge = vertexCoordinates[mostLeft]
                        .copySubtract(vertexCoordinates[edgeEndValue]);
                if (edge.getSquaredLength() > 0) {
                    edges.add(new EdgeInfo(edge, factor));
                }
            }
        }

        edges.sort(EdgeInfo::compareEdges);
        return edges;
    }

    private int[] transformToDrawable(Coordinates coordinates) {
        return coordinates.copyAdd(offset).getIntValue();
    }

    public int[] transformToDrawable(int value) {
        return transformToDrawable(vertexCoordinates[value]);
    }
}
