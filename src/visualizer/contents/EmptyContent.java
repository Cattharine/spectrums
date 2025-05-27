package visualizer.contents;

import visualizer.contents.utils.contentInterfaces.IContentContainer;
import visualizer.contents.utils.contentInterfaces.IContentPart;
import visualizer.contents.utils.elementInfo.ElementInfo;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;
import visualizer.painter.IPainter;

public class EmptyContent implements IContentPart, IContentContainer {
    @Override
    public void shiftAll(int[] difference) {
    }

    @Override
    public void changeScale(int[] cursorPosition, double relationOfScaling) {
    }

    @Override
    public void select(int[] position) {
    }

    @Override
    public void deselectAll() {
    }

    @Override
    public void moveSelected(int[] difference) {
    }

    @Override
    public boolean canGoToSelected() {
        return false;
    }

    @Override
    public void goToSelected() {
    }

    @Override
    public int getSelected() {
        return -1;
    }

    @Override
    public void selectFromOtherContent(int value) {
    }

    @Override
    public boolean canDeselectAll() {
        return false;
    }

    @Override
    public void paint(IPainter painter) {
        int[] pos = new int[]{0, 0};
        painter.drawString("Ничего не выбрано", pos, new ElementInfo(ElementType.VERTEX, ElementState.BASE));
    }

    @Override
    public void switchState() {
    }

    @Override
    public void switchRepresentation() {
    }

    @Override
    public void changeFace(int direction) {
    }

    @Override
    public boolean canChangeFace() {
        return false;
    }
}
