package visualizer.painter.instances;

public class EdgeInfo {
    public final Coordinates coordinates;
    public final int factor;

    public EdgeInfo(Coordinates coordinates, int factor) {
        this.coordinates = coordinates;
        this.factor = factor;
    }

    public static int compareEdges(EdgeInfo first, EdgeInfo second) {
        var p1 = first.coordinates;
        var p2 = second.coordinates;
        var cos1 = -p1.getY() / Math.sqrt(p1.getX() * p1.getX() + p1.getY() * p1.getY());
        var cos2 = -p2.getY() / Math.sqrt(p2.getX() * p2.getX() + p2.getY() * p2.getY());
        return Double.compare(cos1, cos2);
    }
}
