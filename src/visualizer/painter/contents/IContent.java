package visualizer.painter.contents;

import visualizer.painter.painter.IPainter;

public interface IContent {
    void shiftAll(int[] difference);

    void changeScale(int[] cursorPosition, double relationOfScaling);

    void select(int[] position);

    void deselectAll();

    void moveSelected(int[] difference);

    void paint(IPainter painter);

    void switchState();

    IContentState getState();

    boolean canGoToSelected();

    boolean canDeselectAll();
}
