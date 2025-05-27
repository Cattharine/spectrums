package visualizer.contents.utils.contentInterfaces;

import visualizer.painter.IPainter;

public interface IContent {
    void shiftAll(int[] difference);

    void changeScale(int[] cursorPosition, double relationOfScaling);

    void select(int[] position);

    void deselectAll();

    void moveSelected(int[] difference);

    boolean canGoToSelected();

    boolean canDeselectAll();

    void paint(IPainter painter);

    void switchState();

    // раньше был только у Cube
    void changeFace(int direction);

    boolean canChangeFace();
}
