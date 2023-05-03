package algorithm.visualizer.painter.contents;

import java.awt.*;

public interface IContent {
    void preShift(Point current);

    void preMoveChosen(int width, int height, Point current);

    void shiftAll(Point current);

    void moveChosen(Point current, int width, int height);

    void changeScale(Point current, int width, int height, double rot);

    void paint(Graphics2D g2, int width, int height);
}
