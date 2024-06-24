package visualizer.painter.contents.cube;

import visualizer.painter.contents.ElementType;

public class CubeContentConstants {
    public static final double p = 60;
    public static final int diameter = 5;
    public static final int selectedDiameterAdd = 12;
    public static final int maxMuDiameterAdd = 8;
    public static final int activeDiameterAdd = 6;

    public static final int[] stringShift = new int[2];

    static {
        stringShift[0] = 4;
        stringShift[1] = 4;
    }

    public static int getDiameter(ElementType type) {
        return switch (type) {
            case SELECTED -> diameter + selectedDiameterAdd;
            case MAX_MU -> diameter + maxMuDiameterAdd;
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
}
