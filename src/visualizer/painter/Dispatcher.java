package visualizer.painter;

import visualizer.extendedAlgorithms.MetaInfo;
import visualizer.painter.contents.ContentInfo;
import visualizer.painter.painter.IPainter;

import java.util.ArrayList;

public class Dispatcher {
    private final ArrayList<ContentInfo> contents = new ArrayList<>();
    private int activeContent = 0;
    private int[] memorizedCursorPosition = new int[2];

    public void addContentInfo(MetaInfo info) {
        contents.add(new ContentInfo(info));
    }

    public void select(int[] position) {
        if (contents.size() > 0)
            contents.get(activeContent).select(position);
        memorizedCursorPosition = position;
    }

    public void deselectAll() {
        if (contents.size() > 0) {
            contents.get(activeContent).deselectAll();
        }
    }

    public void memorizeCursorPosition(int[] position) {
        memorizedCursorPosition = position;
    }

    public void shiftAll(int[] currentPos) {
        var difference = getDifference(currentPos, memorizedCursorPosition);
        if (contents.size() > 0)
            contents.get(activeContent).shiftAll(difference);
        memorizedCursorPosition = currentPos;
    }

    public void moveSelected(int[] currentPos) {
        var difference = getDifference(currentPos, memorizedCursorPosition);
        if (contents.size() > 0)
            contents.get(activeContent).moveSelected(difference);
        memorizedCursorPosition = currentPos;
    }

    public void changeScale(int[] cursorPosition, double rot) {
        if (contents.size() > 0)
            contents.get(activeContent).changeScale(cursorPosition, rot);
    }

    public void changeActiveContent(int direction) {
        if (canChangeFace())
            changeFace(direction);
        else {
            activeContent = (activeContent + direction + contents.size()) % contents.size();
        }
    }

    private void changeFace(int direction) {
        if (contents.size() > 0) {
            contents.get(activeContent).changeFaceNum(direction);
        }
    }

    private boolean canChangeFace() {
        return contents.size() > 0 && contents.get(activeContent).shouldChangeFaceNum();
    }

    public boolean paint(IPainter painter) {
        if (contents.size() > 0) {
            contents.get(activeContent).paint(painter);
            return true;
        } else return false;
    }

    public void switchRepresentation() {
        if (contents.size() > 0) {
            contents.get(activeContent).switchRepresentation();
        }
    }

    public void switchState() {
        if (contents.size() > 0) {
            contents.get(activeContent).changeState();
        }
    }

    public void goToSelected() {
        if (contents.size() > 0) {
            contents.get(activeContent).goToSelected();
        }
    }

    public boolean canGoToSelected() {
        return contents.size() > 0 && contents.get(activeContent).canGoToSelected();
    }

    public boolean canDeselectAll() {
        return contents.size() > 0 && contents.get(activeContent).canDeselectAll();
    }

    private int[] getDifference(int[] value, int[] other) {
        var difference = new int[2];
        difference[0] = value[0] - other[0];
        difference[1] = value[1] - other[1];

        return difference;
    }
}
