package algorithm.visualizer.painter.contents;

import algorithm.visualizer.painter.instances.Path;
import algorithm.visualizer.solver.instances.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class CubeContent implements IContent {
    private final int n;
    private final double p = 20;
    private final int radius = 5;
    private double[][] axes;
    private final int[] axisChecker;
    private double[][] points;
    private Point offset = new Point(0 , 0);
    private Point mousePos = new Point(0 , 0);
    private double scale = 1;
    private int chosen = -1;
    private Integer[] active = new Integer[0];
    private HashSet<Integer> activeVertexes = new HashSet<>();
    private ArrayList<Path> paths = new ArrayList<>();
    private boolean showPaths = false;
    private int currentPath = 0;

    public CubeContent(int n) {
        this.n = n;
        axisChecker = new int[n];
        for (var i =0; i < n; i++) {
            axisChecker[i] = 1 << i;
        }
        fillCoords();
        fillPoints();
    }

    public void shiftAll(Point current) {
        offset = new Point(current.x - mousePos.x, current.y - mousePos.y);
    }

    public void memorizePos(Point current) {
        mousePos = new Point(current.x - offset.x, current.y - offset.y);
    }

    public void changeScale(Point current, int width, int height, double rot) {
        var vec = new Point(current.x - width/2, current.y - height/2);
        offset = new Point((int)((offset.x - vec.x) / scale), (int)((offset.y - vec.y) / scale));
        scale *= Math.pow(1.2, -rot);
        offset = new Point((int) (offset.x * scale) + vec.x, (int) (offset.y * scale) + vec.y);
    }

    public void findChosen(int width, int height, Point current) {
        for (var i = 0; i < points.length; i++) {
            if (shouldChoose(i, width, height, current)) {
                chosen = i;
                break;
            }
            else chosen = -1;
        }
    }

    private boolean shouldChoose(int vertexNum, int width, int height, Point current) {
        var pos = transform(width, height, points[vertexNum][0], points[vertexNum][1]);
        return Math.max(Math.abs(pos.x - current.x), Math.abs(pos.y - current.y)) <= radius / 2 + 1
                && isAxis(vertexNum);
    }

    public void moveChosen(Point current, int width, int height) {
        for (var i = 0; i < n; i++) {
            if (chosen == axisChecker[i]) {
                axes[i][0] = (double)(current.x - width / 2 - offset.x)/scale;
                axes[i][1] = -(double)(current.y - height / 2 - offset.y)/scale;
            }
        }
        fillPoints();
    }

    private void fillCoords() {
        axes = new double[n][2];
        var angle = 2 * Math.PI / (Math.ceil((n + 1.0) / 2) * 2 - 1);
        for (var i = 0; i < n; i++) {
            double t = p * 5 / 2;
            var length = p + t * i;
            axes[i][0] = -length * Math.sin(i * angle);
            axes[i][1] = length * Math.cos(i * angle);
        }
    }

    private void fillPoints() {
        points = new double[(int) Math.pow(2, n)][2];
        for (var i = 0; i < points.length; i++) {
            for (var j = 0; j < axisChecker.length; j++) {
                if ((i & axisChecker[j]) > 0) {
                    points[i][0] += axes[j][0];
                    points[i][1] += axes[j][1];
                }
            }
        }
    }

    public void paint(Graphics2D g2, int width, int height) {
        g2.setColor(Color.white);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));

        drawCube(g2, width, height);
    }

    private void drawCube(Graphics2D g2, int width, int height) {
        var off = new Point(width / 2 + offset.x, height / 2 + offset.y);
        if (showPaths && paths.size() > 0) {
            findActive();
            fillActive(g2, off, active, Color.LIGHT_GRAY);
            drawPath(g2, width, height);
        }
        var pos0 = transform(width, height, points[0][0], points[0][1]);
        drawPoint(g2, 0, pos0.x, pos0.y);
        for (var i = 0; i < points.length; i++) {
            for (var k = 0; k < n; k++) {
                var j = i ^ axisChecker[k];
                if (i > j) {
                    var first = transform(width, height, points[i][0], points[i][1]);
                    var second = transform(width, height, points[j][0], points[j][1]);
                    g2.drawLine(first.x, first.y, second.x, second.y);
                    drawPoint(g2, i, first.x, first.y);
                }
            }
        }
    }

    private void findActive() {
        var name = paths.get(currentPath).getName();

        var count = 0;
        for (var i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '0') {
                count++;
            }
        }
        active = getAllVertexesInFace(count, name);
    }

    private Integer[] getAllVertexesInFace(int count, String name) {
        var allVertexes = new Integer[(int) Math.pow(2, count)];
        for (var i = 0; i < allVertexes.length; i++) {
            var base = new StringBuilder();
            var replacement = Integer.toBinaryString(i);
            var filler = "0".repeat(Math.max(0, count - replacement.length()));
            replacement = filler + replacement;
            var c = 0;
            for (var k = 0; k < n; k++) {
                switch (name.charAt(k)) {
                    case '1' -> base.append('0');
                    case '2' -> base.append('1');
                    default -> {
                        base.append(replacement.charAt(c));
                        c++;
                    }
                }
            }
            allVertexes[i] = Integer.parseInt(base.toString(), 2);
        }
        return allVertexes;
    }

    private void drawPath(Graphics2D g2, int width, int height) {
        var path = paths.get(currentPath);
        var best = path.getBest();
        if (best.x > -1) {
            var p = best.x ^ best.y;
            var edges = getEdges(p);
            drawEdgesOnPath(g2, edges, best.x, width, height);
        }
    }

    private ArrayList<Integer> getEdges(int path) {
        var edges = new ArrayList<Integer>();
        for (var i = 0; i < n; i++) {
            if ((axisChecker[i] & path) > 0) {
                edges.add(i);
            }
        }
        return edges;
    }

    private void drawEdgesOnPath(Graphics2D g2, ArrayList<Integer> edges, int start, int width, int height) {
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        g2.setColor(Color.decode("#9E012B"));
        var current = points[start];
        for (var edge : edges) {
            var next = points[start ^ axisChecker[edge]];
            var first = transform(width, height, current[0], current[1]);
            var second = transform(width, height, next[0], next[1]);
            g2.drawLine(first.x, first.y, second.x, second.y);
            current = next;
            start = start ^ axisChecker[edge];
        }
        g2.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        g2.setColor(Color.black);
    }

    private void fillActive(Graphics2D g2, Point off, Integer[] aPoints, Color color) {
        var set = new HashSet<Integer>();
        Collections.addAll(set, aPoints);

        g2.setColor(color);
        showActive(g2, aPoints, set, off, false);
        g2.setColor(Color.BLACK);

        g2.setStroke(new BasicStroke(0.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        showActive(g2, aPoints, set, off, true);

        g2.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
    }

    private void showPolygon(Graphics2D g2, boolean toDraw, int[] x, int[] y) {
        if (toDraw)
            g2.drawPolygon(new Polygon(x, y, 4));
        else g2.fill(new Polygon(x, y, 4));
    }

    private void showActive(Graphics2D g2, Integer[] aPoints, HashSet<Integer> set, Point off, boolean toDraw) {
        for (int i : aPoints) {
            for (var k = 0; k < n; k++) {
                for (var m = 0; m < n; m++) {
                    var j = i ^ axisChecker[k] ^ axisChecker[m];
                    var j1 = i ^ axisChecker[k];
                    var j2 = i ^ axisChecker[m];
                    if (set.contains(j) && set.contains(j1) && set.contains(j2)) {
                        var x = new int[4];
                        var y = new int[4];
                        x[0] = (int) (scale * points[i][0]) + off.x;
                        x[1] = (int) (scale * points[j1][0]) + off.x;
                        x[2] = (int) (scale * points[j][0]) + off.x;
                        x[3] = (int) (scale * points[j2][0]) + off.x;
                        y[0] = -(int) (scale * points[i][1]) + off.y;
                        y[1] = -(int) (scale * points[j1][1]) + off.y;
                        y[2] = -(int) (scale * points[j][1]) + off.y;
                        y[3] = -(int) (scale * points[j2][1]) + off.y;
                        showPolygon(g2, toDraw, x, y);
                    }
                }
            }
        }
    }

    private void drawPoint(Graphics2D g2, int i, int x1, int y1) {
        if (i == chosen) {
            drawDisc(g2, 10, x1, y1, Color.decode("#cc1100"));
        }
        else if (isAxis(i) || i == 0) {
            drawDisc(g2, 10, x1, y1, Color.decode("#FF7B3F"));
        }
        if (activeVertexes.contains(i)) {
            drawDisc(g2, 6, x1, y1, Color.decode("#62EA02"));
        }

        drawDisc(g2, 0, x1, y1, Color.BLACK);
        g2.drawString(String.valueOf(i), x1 + 3, y1 - 3);
    }

    private void drawDisc(Graphics2D g2, int add, int x1, int y1, Color color) {
        g2.setColor(color);
        g2.fillOval(x1 - radius / 2 - add/2, y1 - radius / 2 - add/2, radius + add, radius + add);
    }

    private boolean isAxis(int i) {
        for (var k=0; k < n; k++) {
            if (i == axisChecker[k])
                return true;
        }
        return false;
    }

    public void setActiveVertexes(ArrayList<Vertex> vertexes) {
        activeVertexes = new HashSet<>();
        for (var vertex: vertexes) {
            activeVertexes.add(vertex.getPos());
        }
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public void setShowPaths(boolean value) {
        showPaths = value;
    }

    public int nextPath() {
        return(currentPath + 1) % paths.size();
    }

    public int prevPath() {
        return (paths.size() + currentPath - 1)%paths.size();
    }

    public void setPath(int value) {
        if (paths.size() > value && value > -1) {
            currentPath = value;
        }
    }

    public void preShift(Point current) {
        memorizePos(current);
    }

    public void preMoveChosen(int width, int height, Point current) {
        findChosen(width, height, current);
    }

    private Point transform(int width, int height, double x, double y) {
        return new Point(width/2 + offset.x + (int)(scale * x),
                height/ 2 + offset.y - (int)(scale * y));
    }

    public Point getOffset() {
        return offset;
    }
}
