package visualizer.contents.cube;

import visualizer.contents.utils.elementInfo.ElementState;

public class CubeConstants {
    private static final double minAxisLength = 60;
    private static final int diameter = 8;
    private static final int selectedDiameterAdd = 12;
    private static final int maxMuDiameterAdd = 8;
    private static final int activeDiameterAdd = 6;

    private static final int[] stringShift = new int[2];

    static {
        stringShift[0] = 4;
        stringShift[1] = 4;
    }

    public static int getDiameter(ElementState state) {
        return switch (state) {
            case SELECTED, SEMI_SELECTED -> diameter + selectedDiameterAdd;
            case MAX, SEMI_MAX -> diameter + maxMuDiameterAdd;
            case ACTIVE -> diameter + activeDiameterAdd;
            default -> diameter;
        };
    }

    public static int[] getShiftedForString(int[] vertexPos) {
        var shifted = new int[2];
        shifted[0] = vertexPos[0] + stringShift[0];
        shifted[1] = vertexPos[1] - stringShift[1];
        return shifted;
    }

    public static double getMinAxisLength() {
        return minAxisLength;
    }
}
