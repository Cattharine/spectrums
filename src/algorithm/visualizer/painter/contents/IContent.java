package algorithm.visualizer.painter.contents;

import java.awt.*;

public interface IContent {
    void preShift(Point current);

    void preMoveChosen(Point current);

    void shiftAll(Point current);

    void moveChosen(Point current);

    void changeScale(Point current, double rot);

    void paint(Graphics2D g2, int width, int height, boolean showPaths);
}
