package tests.visualizer.contents.tree;

import visualizer.contents.common.CommonContent;
import visualizer.contents.tree.TreeConstants;
import visualizer.contents.utils.Coordinates;

import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TestTree {
    public final CommonContent commonContent = new CommonContent("00000001000110000101000001000000");
    public final int n = 5;
    public final int numberOfNodes = (int) Math.pow(3, n);
    private BiFunction<Coordinates, Integer, Coordinates> currentTransform;
    private double scale = 1;
    public Coordinates offset;
    public final int toHide = Integer.parseInt("12000", 3);
    public boolean isHidden = false;

    public TestTree() {
        assert commonContent.getN() == n;
        offset = getCoordinates(0).copyMul(-1);
        currentTransform = (Coordinates value, Integer node) -> value;
    }

    public void setCurrentTransform(BiFunction<Coordinates, Integer, Coordinates> value, double relationOfScaling) {
        currentTransform = value;
        scale = relationOfScaling;
    }

    public boolean shouldNotCheck(int node) {
        var predecessors = getPredecessors(node);
        return isHidden && predecessors.contains(toHide) && node != toHide;
    }

    private HashSet<Integer> getPredecessors(int node) {
        var predecessors = new HashSet<Integer>();

        var current = node;
        while (current != -1) {
            predecessors.add(current);
            current = commonContent.tree.getNode(current).getParentValue();
        }

        return predecessors;
    }

    public BiFunction<Coordinates, Integer, Coordinates> getMoveBySelectedTransform(int[] difference, int selectedToMove) {
        return (Coordinates coordinates, Integer node) -> {
            var predecessors = getPredecessors(node);
            double x = coordinates.getX();
            double y = coordinates.getY();
            if (predecessors.contains(selectedToMove)) {
                x += difference[0];
            }
            if (getLevel(node) == getLevel(selectedToMove)) {
                y += difference[1];
            }
            return new Coordinates(x, y);
        };
    }

    private boolean isClose(int[] value, Coordinates coordinates) {
        var difference = coordinates.copySubtract(value).getAbs().getIntValue();
        return Math.max(difference[0], difference[1]) < 2 * Math.max(scale, 1);
    }

    public int map(int[] value) {
        for (int node = 0; node < numberOfNodes; node++) {
            var coordinates = getTransformedCoordinates(node);
            if (isClose(value, coordinates))
                return node;
        }
        return -1;
    }

    public Coordinates getTransformedCoordinates(int node) {
        return currentTransform.apply(getCoordinates(node), node);
    }

    private Coordinates getCoordinates(int node) {
        return new Coordinates(getX(node), getY(node));
    }

    private double getX(int node) {
        Function<Integer, Double> getter = isHidden ? this::getHiddenX : this::getBaseX;
        var x = getter.apply(node);
        return (-x + (getter.apply(1) + 0.5)) * TreeConstants.nodeDistance;
    }

    private double getBaseX(int node) {
        return switch (node) {
            case 0, 81, 108, 117, 120 -> 80.5;
            case 1 -> 161;
            case 2 -> 160;
            case 3 -> 158.5;
            case 4 -> 159;
            case 5 -> 158;
            case 6 -> 156.5;
            case 7 -> 157;
            case 8 -> 156;
            case 9, 12 -> 152.5;
            case 10 -> 155;
            case 11 -> 154;
            case 13 -> 153;
            case 14 -> 152;
            case 15 -> 150.5;
            case 16 -> 151;
            case 17 -> 150;
            case 18, 21 -> 146.5;
            case 19 -> 149;
            case 20 -> 148;
            case 22 -> 147;
            case 23 -> 146;
            case 24 -> 144.5;
            case 25 -> 145;
            case 26 -> 144;
            case 27, 36, 39 -> 134.5;
            case 28 -> 143;
            case 29 -> 142;
            case 30 -> 140.5;
            case 31 -> 141;
            case 32 -> 140;
            case 33 -> 138.5;
            case 34 -> 139;
            case 35 -> 138;
            case 37 -> 137;
            case 38 -> 136;
            case 40 -> 135;
            case 41 -> 134;
            case 42 -> 132.5;
            case 43 -> 133;
            case 44 -> 132;
            case 45, 48 -> 128.5;
            case 46 -> 131;
            case 47 -> 130;
            case 49 -> 129;
            case 50 -> 128;
            case 51 -> 126.5;
            case 52 -> 127;
            case 53 -> 126;
            case 54, 63, 66 -> 116.5;
            case 55 -> 125;
            case 56 -> 124;
            case 57 -> 122.5;
            case 58 -> 123;
            case 59 -> 122;
            case 60 -> 120.5;
            case 61 -> 121;
            case 62 -> 120;
            case 64 -> 119;
            case 65 -> 118;
            case 67 -> 117;
            case 68 -> 116;
            case 69 -> 114.5;
            case 70 -> 115;
            case 71 -> 114;
            case 72, 75 -> 110.5;
            case 73 -> 113;
            case 74 -> 112;
            case 76 -> 111;
            case 77 -> 110;
            case 78 -> 108.5;
            case 79 -> 109;
            case 80 -> 108;
            case 82 -> 107;
            case 83 -> 106;
            case 84 -> 104.5;
            case 85 -> 105;
            case 86 -> 104;
            case 87 -> 102.5;
            case 88 -> 103;
            case 89 -> 102;
            case 90, 93 -> 98.5;
            case 91 -> 101;
            case 92 -> 100;
            case 94 -> 99;
            case 95 -> 98;
            case 96 -> 96.5;
            case 97 -> 97;
            case 98 -> 96;
            case 99, 102 -> 92.5;
            case 100 -> 95;
            case 101 -> 94;
            case 103 -> 93;
            case 104 -> 92;
            case 105 -> 90.5;
            case 106 -> 91;
            case 107 -> 90;
            case 109 -> 89;
            case 110 -> 88;
            case 111 -> 86.5;
            case 112 -> 87;
            case 113 -> 86;
            case 114 -> 84.5;
            case 115 -> 85;
            case 116 -> 84;
            case 118 -> 83;
            case 119 -> 82;
            case 121 -> 81;
            case 122 -> 80;
            case 123 -> 78.5;
            case 124 -> 79;
            case 125 -> 78;
            case 126, 129 -> 74.5;
            case 127 -> 77;
            case 128 -> 76;
            case 130 -> 75;
            case 131 -> 74;
            case 132 -> 72.5;
            case 133 -> 73;
            case 134 -> 72;
            case 135, 144, 147 -> 62.5;
            case 136 -> 71;
            case 137 -> 70;
            case 138 -> 68.5;
            case 139 -> 69;
            case 140 -> 68;
            case 141 -> 66.5;
            case 142 -> 67;
            case 143 -> 66;
            case 145 -> 65;
            case 146 -> 64;
            case 148 -> 63;
            case 149 -> 62;
            case 150 -> 60.5;
            case 151 -> 61;
            case 152 -> 60;
            case 153, 156 -> 56.5;
            case 154 -> 59;
            case 155 -> 58;
            case 157 -> 57;
            case 158 -> 56;
            case 159 -> 54.5;
            case 160 -> 55;
            case 161 -> 54;
            case 162, 189, 198, 201 -> 26.5;
            case 163 -> 53;
            case 164 -> 52;
            case 165 -> 50.5;
            case 166 -> 51;
            case 167 -> 50;
            case 168 -> 48.5;
            case 169 -> 49;
            case 170 -> 48;
            case 171, 174 -> 44.5;
            case 172 -> 47;
            case 173 -> 46;
            case 175 -> 45;
            case 176 -> 44;
            case 177 -> 42.5;
            case 178 -> 43;
            case 179 -> 42;
            case 180, 183 -> 38.5;
            case 181 -> 41;
            case 182 -> 40;
            case 184 -> 39;
            case 185 -> 38;
            case 186 -> 36.5;
            case 187 -> 37;
            case 188 -> 36;
            case 190 -> 35;
            case 191 -> 34;
            case 192 -> 32.5;
            case 193 -> 33;
            case 194 -> 32;
            case 195 -> 30.5;
            case 196 -> 31;
            case 197 -> 30;
            case 199 -> 29;
            case 200 -> 28;
            case 202 -> 27;
            case 203 -> 26;
            case 204 -> 24.5;
            case 205 -> 25;
            case 206 -> 24;
            case 207, 210 -> 20.5;
            case 208 -> 23;
            case 209 -> 22;
            case 211 -> 21;
            case 212 -> 20;
            case 213 -> 18.5;
            case 214 -> 19;
            case 215 -> 18;
            case 216, 225, 228 -> 8.5;
            case 217 -> 17;
            case 218 -> 16;
            case 219 -> 14.5;
            case 220 -> 15;
            case 221 -> 14;
            case 222 -> 12.5;
            case 223 -> 13;
            case 224 -> 12;
            case 226 -> 11;
            case 227 -> 10;
            case 229 -> 9;
            case 230 -> 8;
            case 231 -> 6.5;
            case 232 -> 7;
            case 233 -> 6;
            case 234, 237 -> 2.5;
            case 235 -> 5;
            case 236 -> 4;
            case 238 -> 3;
            case 239 -> 2;
            case 240 -> 0.5;
            case 241 -> 1;
            case 242 -> 0;
            default -> -1;
        };
    }

    private double getHiddenX(int node) {
        if (node > 161)
            return getBaseX(node);
        if (node > 134)
            return 54;
        if (node == 81)
            return (getHiddenX(toHide) + getHiddenX(82)) / 2;
        if (node == 0)
            return (getHiddenX(numberOfNodes - 1) + getHiddenX(1)) / 2;
        return getBaseX(node) - 17;
    }

    private double getY(int node) {
        var level = getLevel(node);
        return -level * level * TreeConstants.levelHeight;
    }

    public int getLevel(int node) {
        return commonContent.tree.getNode(node).face.info.dimension;
    }

    public int getParent(int node) {
        return commonContent.tree.getNode(node).getParentValue();
    }
}
