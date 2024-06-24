package visualizer.painter.contents.tree;

import visualizer.painter.contents.ElementType;

import java.util.ArrayList;

public class TreeContentConstants {
    public static final double levelHeight = 80;
    public static final double nodeDistance = 60;
    public static final ArrayList<TreeContentState> statesToChange = new ArrayList<>();

    static {
        statesToChange.add(TreeContentState.WITHOUT_LINKS);
        statesToChange.add(TreeContentState.TREE);
    }

    public static final int baseDiameter = 5;
    public static final int selectedDiameterAdd = 8;
    public static final int maxMuDiameterAdd = 4;
    public static final int[] stringShift = new int[2];

    static {
        stringShift[0] = 4;
        stringShift[1] = 4;
    }

    public static int getDiameter(ElementType type) {
        return switch (type) {
            case SELECTED -> baseDiameter + selectedDiameterAdd;
            case MAX_MU -> baseDiameter + maxMuDiameterAdd;
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
