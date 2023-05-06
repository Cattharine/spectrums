package algorithm.visualizer.painter.contents;

import algorithm.visualizer.painter.instances.Path;
import algorithm.visualizer.painter.instances.Three;
import algorithm.visualizer.solver.instances.Vertex;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

public class CubeContent implements IContent {
    private final int n;
    private final double p = 60;
    private final int radius = 5;
    private double[][] axes;
    private final int[] axisChecker;
    private double[][] points;
    private Point offset;
    private Point mousePos = new Point(0 , 0);
    private double scale = 1;
    private int chosen = -1;
    private Integer[] active = new Integer[0];
    private HashSet<Integer> activeVertexes = new HashSet<>();
    private ArrayList<Path> paths = new ArrayList<>();
    private int currentPath = 0;

    public CubeContent(int n) {
        this.n = n;
        offset = new Point((int) p/2 * n, (int)p * n);
        axisChecker = new int[n];
        for (var i =0; i < n; i++) {
            axisChecker[i] = 1 << i;
        }
        fillAxes();
        fillPoints();
    }

    public void shiftAll(Point current) {
        offset = new Point(current.x - mousePos.x, current.y - mousePos.y);
    }

    public void memorizePos(Point current) {
        mousePos = new Point(current.x - offset.x, current.y - offset.y);
    }

    public void changeScale(Point current, double rot) {
        var s = Math.pow(1.2, -rot);
        offset = new Point((int)(current.x * (1 - s) + s * offset.x), (int)(current.y * (1 - s) + s * offset.y));
        scale *= s;
    }

    public void findChosen(Point current) {
        for (int axis : axisChecker) {
            if (shouldChoose(axis, current)) {
                chosen = axis;
                break;
            } else chosen = -1;
        }
    }

    private boolean shouldChoose(int vertexNum, Point current) {
        var pos = transform(points[vertexNum][0], points[vertexNum][1]);
        return Math.max(Math.abs(pos.x - current.x), Math.abs(pos.y - current.y)) <= radius / 2 + 1;
    }

    public void moveChosen(Point current) {
        for (var i = 0; i < n; i++) {
            if (chosen == axisChecker[i]) {
                axes[i][0] = (double)(current.x - offset.x)/scale;
                axes[i][1] = -(double)(current.y - offset.y)/scale;
            }
        }
        fillPoints();
    }

    private void fillAxes() {
        axes = new double[n][2];
        var angle = Math.PI / (n + 1);
        double t = p * (n + 1) / (n + 2);
        for (var i = 0; i < n; i++) {
            var length = p + t * i;
            axes[i][0] = length * Math.sin(i * angle);
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

    public void paint(Graphics2D g2, int width, int height, boolean showPaths) {
        drawCube(g2, showPaths);
    }

    private void drawCube(Graphics2D g2, boolean showPaths) {
        if (showPaths && paths.size() > 0) {
            findActive();
            showActive(g2, Color.LIGHT_GRAY);
            drawPath(g2);
        }
        var pos0 = transform(points[0][0], points[0][1]);
        drawPoint(g2, 0, pos0.x, pos0.y);
        for (var i = 0; i < points.length; i++) {
            for (var k = 0; k < n; k++) {
                var j = i ^ axisChecker[k];
                if (i > j) {
                    var first = transform(points[i][0], points[i][1]);
                    var second = transform(points[j][0], points[j][1]);
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
            var base = 0;
            var c = 0;
            for (var k = n - 1 ; k > -1; k--) {
                if (name.charAt(k) == '2') {
                    base = base ^ axisChecker[n - k - 1];
                }
                else if (name.charAt(k) == '0') {
                    if ((i & axisChecker[c]) > 0)
                        base = base ^ axisChecker[n - k - 1];
                    c++;
                }
            }
            allVertexes[i] = base;
        }
        return allVertexes;
    }

    private void drawPath(Graphics2D g2) {
        var path = paths.get(currentPath);
        var best = path.getBest();
        if (best.x > -1) {
            var p = best.x ^ best.y;
            var edges = getEdges(p);
            drawEdgesOnPath(g2, edges, best.x);
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

    private void drawEdgesOnPath(Graphics2D g2, ArrayList<Integer> edges, int start) {
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        g2.setColor(Color.decode("#9E012B"));
        var current = points[start];
        for (var edge : edges) {
            start = start ^ axisChecker[edge];
            var next = points[start];
            var first = transform(current[0], current[1]);
            var second = transform(next[0], next[1]);
            g2.drawLine(first.x, first.y, second.x, second.y);
            current = next;
        }
        g2.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        g2.setColor(Color.black);
    }

    private void showActive(Graphics2D g2, Color color) {
        g2.setColor(color);
        fillActive(g2);
        g2.setColor(Color.BLACK);

        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
        drawActive(g2);
        g2.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
    }

    private void fillActive(Graphics2D g2) {
        var mostLeft = getMostLeft();

        var edges = getSortedEdgesOfCurPath(mostLeft);

        fillActivePolygon(g2, edges, mostLeft);
    }

    private int getMostLeft() {
        var mostLeft = 0;
        var name = paths.get(currentPath).getName();

        for (var i = 0; i < axisChecker.length; i++) {
            var axisPointNum = axisChecker[n - i - 1];
            if (name.charAt(i) == '2' || (name.charAt(i) == '0' &&
                    (points[axisPointNum][0] < 0 || (points[axisPointNum][0] == 0 && points[axisPointNum][1] < 0)))) {
                mostLeft = mostLeft ^ axisPointNum;
            }
        }

        return mostLeft;
    }

    private ArrayList<Three> getSortedEdgesOfCurPath(int mostLeft) {
        var name = paths.get(currentPath).getName();
        var edges = new ArrayList<Three>();

        for (var i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '0') {
                var another = mostLeft ^ axisChecker[n-i-1];
                var edge = new Point2D.Double(points[another][0] - points[mostLeft][0],
                        points[another][1] - points[mostLeft][1]);
                if (edge.getX() * edge.getX() + edge.getY() * edge.getY() > 0) {
                    edges.add(new Three(edge.x, edge.y, n - i -1));
                }
            }
        }

        edges.sort(this::compareEdges);
        return edges;
    }

    private void fillActivePolygon(Graphics2D g2, ArrayList<Three> edges, int mostLeft) {
        if (edges.size() > 0) {
            var x = new int[edges.size() * 2];
            var y = new int[edges.size() * 2];
            var currPoint = transform(points[mostLeft][0], points[mostLeft][1]);
            int current = mostLeft;
            x[0] = currPoint.x;
            y[0] = currPoint.y;
            for (var i = 0; i < edges.size() * 2 - 1; i++) {
                var nextNum = current ^ axisChecker[edges.get(i % edges.size()).getNum()];
                var next = transform(points[nextNum][0], points[nextNum][1]);
                x[i + 1] = next.x;
                y[i + 1] = next.y;
                current = nextNum;
            }

            g2.fillPolygon(new Polygon(x, y, x.length));
        }
    }

    private int compareEdges(Three p1, Three p2) {
        var cos1 = -p1.getY()/Math.sqrt(p1.getX()*p1.getX() + p1.getY() * p1.getY());
        var cos2 = -p2.getY()/Math.sqrt(p2.getX()*p2.getX() + p2.getY() * p2.getY());
        return Double.compare(cos1, cos2);
    }

    private void drawActive(Graphics2D g2) {
        var edges = getActiveEdges();

        drawActiveEdges(g2, edges);
    }

    private ArrayList<Integer> getActiveEdges() {
        var name = paths.get(currentPath).getName();
        var edges = new ArrayList<Integer>();

        for (var i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '0') {
                edges.add(n - i - 1);
            }
        }

        return edges;
    }

    private void drawActiveEdges(Graphics2D g2, ArrayList<Integer> edges) {
        for (var i : active) {
            for (Integer edge : edges) {
                var j = i ^ axisChecker[edge];
                if (i > j) {
                    var first = transform(points[i][0], points[i][1]);
                    var second = transform(points[j][0], points[j][1]);
                    g2.drawLine(first.x, first.y, second.x, second.y);
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
        g2.drawString(String.valueOf(i), x1 + 4, y1 - 4);
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

    public void preMoveChosen(Point current) {
        findChosen(current);
    }

    private Point transform(double x, double y) {
        return new Point(offset.x + (int)(scale * x), offset.y - (int)(scale * y));
    }

    public Point getOffset() {
        return offset;
    }
}
