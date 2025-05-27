package visualizer.contents.tree;

import visualizer.contents.utils.elementInfo.ElementState;

public class TreeConstants {
    public static final double levelHeight = 40;
    public static final double nodeDistance = 30;

    private static final int baseDiameter = 5;
    private static final int selectedDiameterAdd = 10;
    private static final int hiddenDiameterAdd = 12;
    private static final int maxMuDiameterAdd = 4;
    private static final int[] stringShift = new int[2];

    static {
        stringShift[0] = -5;
        stringShift[1] = 6;
    }

    public static int getDiameter(ElementState state) {
        return switch (state) {
            case SELECTED -> baseDiameter + selectedDiameterAdd;
            case HIDDEN -> baseDiameter + hiddenDiameterAdd;
            case MAX -> baseDiameter + maxMuDiameterAdd;
            default -> baseDiameter;
        };
    }

    public static int[] getShiftedForString(int[] vertexPos) {
        var shifted = new int[2];
        shifted[0] = vertexPos[0] + stringShift[0];
        shifted[1] = vertexPos[1] - stringShift[1];
        return shifted;
    }
}
