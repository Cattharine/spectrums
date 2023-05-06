package algorithm.visualizer.painter.instances;

import java.awt.*;
import java.util.ArrayList;

public class TNode {
    private final String name;
    private final TNode parent;
    private final ArrayList<TNode> children = new ArrayList<>();
    private int normalWidth = 60;
    private int normalHeight = 80;
    private final int level;
    private Point pos;
    private static int levelNum;
    private static int[] maxPos;
    private static boolean isValid = false;

    public TNode(String name, TNode parent, int level, int levelNum) {
        this.name = name;
        this.parent = parent;
        this.level = level;
        TNode.levelNum = levelNum;
        normalHeight = (int)(normalHeight * Math.sqrt(levelNum));
        if (maxPos == null) {
            maxPos = new int[levelNum];
        }
    }

    public void addChildren(TNode[] cNodes) {
        children.add(cNodes[0]);
        children.add(cNodes[1]);
        resetMaxPos();
        isValid = false;
    }

    public String getName() {
        return name;
    }

    public Point getPos() {
        if (pos != null && isValid)
            return pos;
        if (children.size() == 0) {
            pos = new Point(maxPos[level], level * normalHeight);
            maxPos[level] = maxPos[level] + normalWidth;
            for (var k = level; k < levelNum; k++) {
                maxPos[k] = maxPos[level];
            }
        }
        else {
            pos = new Point((maxPos[level] + maxPos[level + 1] - normalWidth)/2, level * normalHeight);
            maxPos[level] = maxPos[level + 1];
        }
        return pos;
    }

    @Override
    public String toString() {
        return name;
    }

    public static void resetMaxPos() {
        maxPos = new int[levelNum];
    }

    public static void setValid() {
         isValid = true;
    }

    public TNode getParent() {
        return parent;
    }

    public void addToPos(Point value) {
        pos = new Point(value.x + pos.x, pos.y);
    }

    public int getLevel() {
        return level;
    }
}
