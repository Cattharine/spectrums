package algorithm.visualizer.painter.instances;

import java.awt.*;

public class Path {
    private String name;
    private int fBest;
    private int sBest;

    public Path(String name, int fBest, int sBest) {
        this.name = name;
        this.fBest = fBest;
        this.sBest = sBest;
    }

    public Point getBest() {
        return new Point(fBest, sBest);
    }

    public String getName() {
        return name;
    }
}
