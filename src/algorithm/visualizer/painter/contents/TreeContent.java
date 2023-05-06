package algorithm.visualizer.painter.contents;

import algorithm.visualizer.painter.instances.TNode;
import algorithm.visualizer.solver.instances.KFace;

import java.awt.*;
import java.util.HashSet;

public class TreeContent implements IContent {
    private TNode root;
    private TNode[] table;
    private int n;
    private Point mousePos = new Point(0 , 0);
    private Point offset = new Point(10, 20);
    private HashSet<Integer> chosen = new HashSet<>();
    private double scale = 1;
    private final int radius = 3;
    private Point prev;
    private int[] best;

    public TreeContent(int n) {
        this.n =n;
        root = new TNode("0".repeat(n),null, 0, n+1);
        table = new TNode[(int) Math.pow(3, n)];
        table[0] = root;
        best = new int[n + 1];
    }

    public void addChildren(KFace parent, KFace[] children) {
        if (children != null) {
            var parentNum = Integer.parseInt(parent.getName(), 3);
            var par = table[parentNum];
            var nodes = new TNode[2];
            for (var i = 0; i < 2; i++) {
                var name = children[i].getName();
                var num = Integer.parseInt(name, 3);
                nodes[i] = new TNode(name, par, children[i].getLevel(), n+1);
                table[num] = nodes[i];
            }
            par.addChildren(nodes);
        }
    }

    public void paint(Graphics2D g2, boolean showPaths) {
        TNode.resetMaxPos();
        drawDiscs(g2);
        drawEdges(g2);
    }

    private void drawDiscs(Graphics2D g2) {
        for (var i = table.length -1; i > -1; i--) {
            if (table[i] == null)
                break;
            var pos = table[i].getPos();
            pos = transform(pos.x, pos.y);
            var add = 6;
            if (chosen.contains(i)) {
                drawDisc(g2, pos, Color.decode("#cc1100"), radius + add);
            }
            var bestOnLevel  = best[table[i].getLevel()];
            if (i == 0 || (bestOnLevel > 0 && bestOnLevel == i)) {
                drawDisc(g2, pos, Color.decode("#62EA02"), radius + add);
            }
            drawDisc(g2, pos, Color.BLACK, radius);
            g2.drawString(table[i].getName(), pos.x + 3, pos.y - 3);
        }
        TNode.setValid();
    }

    private void drawDisc(Graphics2D g2, Point pos, Color color, int rad) {
        g2.setColor(color);
        g2.fillOval(pos.x - rad/2, pos.y- rad/2, rad, rad);
    }

    private void drawEdges(Graphics2D g2) {
        for (var node: table) {
            if (node == null)
                break;
            var parent = node.getParent();
            if (parent != null) {
                var pos = node.getPos();
                var parPos = parent.getPos();
                pos = transform(pos.x, pos.y);
                parPos = transform(parPos.x, parPos.y);
                g2.drawLine(pos.x, pos.y, parPos.x, parPos.y);
            }
        }
    }

    public void memorizePos(Point current) {
        mousePos = new Point(current.x - offset.x, current.y - offset.y);
        prev = current;
    }

    public void findChosen(Point current) {
        for (var i = 0; i < table.length; i++) {
            if (table[i] != null) {
                var nodePos = table[i].getPos();
                nodePos = transform(nodePos.x, nodePos.y);
                if (Math.abs(current.x - nodePos.x) <= 3 && Math.abs(current.y - nodePos.y) <= radius) {
                    if (chosen.contains(i))
                        chosen.remove(i);
                    else chosen.add(i);
                    break;
                }
            }
        }
    }

    public void shiftAll(Point current) {
        offset = new Point(current.x - mousePos.x, current.y - mousePos.y);
    }

    public void moveChosen(Point current) {
        var diff = new Point((int)((current.x - prev.x) / scale), (int)((current.y - prev.y) / scale));
        for (var elem : chosen) {
            if (table.length > elem && elem > -1 && table[elem] != null) {
                table[elem].addToPos(diff);
            }
        }
        prev = current;
    }

    public void changeScale(Point current, double rot) {
        var s = Math.pow(1.2, -rot);
        offset = new Point((int)(current.x * (1 - s) + s * offset.x), (int)(current.y * (1 - s) + s * offset.y));
        scale *= s;
    }

    public void unchooseAll() {
        chosen = new HashSet<>();
    }

    public int getChosen() {
        if (chosen.size() == 1) {
            for (var elem: chosen) {
                return elem;
            }
        }
        return -1;
    }

    public void makeBest(KFace face) {
        var level = face.getLevel();
        best[level] = Integer.parseInt(face.getName(), 3);
    }

    public void preShift(Point current) {
        memorizePos(current);
    }

    public void preMoveChosen(Point current) {
        findChosen(current);
        memorizePos(current);
    }

    private Point transform(double x, double y) {
        return new Point((int)(scale * x) + offset.x, (int)(scale * y) + offset.y);
    }
}
