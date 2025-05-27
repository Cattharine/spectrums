package tests.visualizer.contents.cube;

import visualizer.contents.common.CommonContent;
import visualizer.contents.cube.CubeConstants;
import visualizer.contents.utils.Coordinates;

import java.util.HashSet;
import java.util.function.BiFunction;

public class TestCube {
    public final CommonContent commonContent = new CommonContent("00000001000110000101000001000000");
    public final int n = commonContent.getN();
    public final int numberOfVertexes = 1 << n;
    public final HashSet<Integer> activeVertexes = new HashSet<>();
    public Coordinates offset;
    private BiFunction<Coordinates, Integer, Coordinates> currentTransform;
    private double scale = 1;
    private final Coordinates[] baseAxes = new Coordinates[]{
            getAxis(1, 0),
            getAxis(2, 2.0 / 5),
            getAxis(3, 4.0 / 5),
            getAxis(4, 6.0 / 5),
            getAxis(5, 8.0 / 5)
    };

    public TestCube() {
        assert n == 5;
        activeVertexes.add(7);
        activeVertexes.add(11);
        activeVertexes.add(12);
        activeVertexes.add(17);
        activeVertexes.add(19);
        activeVertexes.add(25);
        offset = getCoordinates(-1);
        currentTransform = (Coordinates coordinates, Integer vertex) -> coordinates;
    }

    public void setCurrentTransform(BiFunction<Coordinates, Integer, Coordinates> transform, double relationOfScaling) {
        currentTransform = transform;
        scale = relationOfScaling;
    }

    public void appendTransform(BiFunction<Coordinates, Integer, Coordinates> toAppend, double relationOfScaling) {
        var transform = currentTransform;
        currentTransform = (Coordinates value, Integer vertex) -> {
            var coordinates = transform.apply(value, vertex);
            return toAppend.apply(coordinates, vertex);
        };
        scale *= relationOfScaling;
    }

    public Coordinates getTransformedCoordinates(int vertex) {
        return currentTransform.apply(getCoordinates(vertex), vertex);
    }

    public int map(int[] value) {
        for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
            var coordinates = getTransformedCoordinates(vertex);
            if (isClose(value, coordinates))
                return vertex;
        }
        return -1;
    }

    private boolean isClose(int[] value, Coordinates coordinates) {
        var difference = coordinates.copySubtract(value).getAbs().getIntValue();
        return Math.max(difference[0], difference[1]) < 2 * Math.max(scale, 1);
    }

    public Coordinates getBaseOffset(String faceName) {
        var result = new Coordinates();
        var freeAddition = new Coordinates();
        var dimension = 0;

        for (int factor = 0; factor < n; factor++) {
            if (faceName.charAt(n - 1 - factor) == '2')
                result.add(baseAxes[factor]);
            if (faceName.charAt(n - 1 - factor) == '0') {
                freeAddition.add(baseAxes[factor]);
                dimension++;
            }
        }

        freeAddition.mul(1.0 / (1 << (dimension - 1)));
        return result.copyAdd(freeAddition).copyMul(-1);
    }

    public Coordinates getCoordinates(int vertex) {
        var zero = new Coordinates(0, 0);
        var firstAxis = baseAxes[0];
        var secondAxis = baseAxes[1];
        var thirdAxis = baseAxes[2];
        var fourthAxis = baseAxes[3];
        var fifthAxis = baseAxes[4];
        var length = CubeConstants.getMinAxisLength();
        var result = switch (vertex) {
            case 0 -> zero;
            case 1 -> firstAxis;
            case 2 -> secondAxis;
            case 3 -> addAll(firstAxis, secondAxis);
            case 4 -> thirdAxis;
            case 5 -> addAll(firstAxis, thirdAxis);
            case 6 -> addAll(secondAxis, thirdAxis);
            case 7 -> addAll(firstAxis, secondAxis, thirdAxis);
            case 8 -> fourthAxis;
            case 9 -> addAll(firstAxis, fourthAxis);
            case 10 -> addAll(secondAxis, fourthAxis);
            case 11 -> addAll(firstAxis, secondAxis, fourthAxis);
            case 12 -> addAll(thirdAxis, fourthAxis);
            case 13 -> addAll(firstAxis, thirdAxis, fourthAxis);
            case 14 -> addAll(secondAxis, thirdAxis, fourthAxis);
            case 15 -> addAll(firstAxis, secondAxis, thirdAxis, fourthAxis);
            case 16 -> fifthAxis;
            case 17 -> addAll(firstAxis, fifthAxis);
            case 18 -> addAll(secondAxis, fifthAxis);
            case 19 -> addAll(firstAxis, secondAxis, fifthAxis);
            case 20 -> addAll(thirdAxis, fifthAxis);
            case 21 -> addAll(firstAxis, thirdAxis, fifthAxis);
            case 22 -> addAll(secondAxis, thirdAxis, fifthAxis);
            case 23 -> addAll(firstAxis, secondAxis, thirdAxis, fifthAxis);
            case 24 -> addAll(fourthAxis, fifthAxis);
            case 25 -> addAll(firstAxis, fourthAxis, fifthAxis);
            case 26 -> addAll(secondAxis, fourthAxis, fifthAxis);
            case 27 -> addAll(firstAxis, secondAxis, fourthAxis, fifthAxis);
            case 28 -> addAll(thirdAxis, fourthAxis, fifthAxis);
            case 29 -> addAll(firstAxis, thirdAxis, fourthAxis, fifthAxis);
            case 30 -> addAll(secondAxis, thirdAxis, fourthAxis, fifthAxis);
            case 31 -> addAll(firstAxis, secondAxis, thirdAxis, fourthAxis, fifthAxis);
            // initial offset
            default -> addAll(firstAxis, secondAxis, thirdAxis, fourthAxis, fifthAxis).copyMul(-1.0 / 2);
        };
        return result.copyMul(length);
    }

    private Coordinates getAxis(double length, double anglePart) {
        return new Coordinates(length * Math.sin(Math.PI * anglePart), length * Math.cos(Math.PI * anglePart));
    }

    private Coordinates addAll(Coordinates... coordinates) {
        var result = new Coordinates();
        for (var value : coordinates) {
            result.add(value);
        }
        return result;
    }

    public HashSet<Integer> getAllVertexes(int faceName) {
        return commonContent.cube.getFace(faceName).info.getAllVertexes();
    }
}
