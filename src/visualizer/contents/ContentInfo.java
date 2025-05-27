package visualizer.contents;

import algorithms.ISolver;
import visualizer.contents.common.CommonContent;
import visualizer.contents.cube.CubeContent;
import visualizer.contents.tree.TreeContent;
import visualizer.contents.utils.ContentState;
import visualizer.contents.utils.contentInterfaces.IContentContainer;
import visualizer.contents.utils.contentInterfaces.IContentPart;
import visualizer.painter.IPainter;

public class ContentInfo implements ISolver, IContentContainer {
    public static final EmptyContent empty = new EmptyContent();
    private CubeContent cubeContent;
    private TreeContent treeContent;
    private CommonContent commonContent;
    private ContentState state = ContentState.CUBE;

    public ContentInfo(String table) {
        getNewEntry(table);
    }

    public void shiftAll(int[] difference) {
        getActiveContent().shiftAll(difference);
    }

    public void changeScale(int[] cursorPosition, double rot) {
        var relationOfScaling = Math.pow(1.2, -rot);
        getActiveContent().changeScale(cursorPosition, relationOfScaling);
    }

    public void select(int[] position) {
        getActiveContent().select(position);
    }

    public void deselectAll() {
        getActiveContent().deselectAll();
    }

    public void moveSelected(int[] difference) {
        getActiveContent().moveSelected(difference);
    }

    public boolean canGoToSelected() {
        return getActiveContent().canGoToSelected();
    }

    public void goToSelected() {
        if (getActiveContent().canGoToSelected()) {
            int selected = getActiveContent().getSelected();
            switchState();
            getActiveContent().selectFromOtherContent(selected);
        }
    }

    public boolean canDeselectAll() {
        return getActiveContent().canDeselectAll();
    }

    public void paint(IPainter painter) {
        getActiveContent().paint(painter);
        painter.showSpectrum(commonContent.getResult());
    }

    public void switchState() {
        state = switch (state) {
            case CUBE -> ContentState.TREE;
            case TREE -> ContentState.CUBE;
        };
    }

    public void switchRepresentation() {
        getActiveContent().switchState();
    }

    public void changeFace(int direction) {
        getActiveContent().changeFace(direction);
    }

    public boolean canChangeFace() {
        return getActiveContent().canChangeFace();
    }

    private IContentPart getActiveContent() {
        if (commonContent == null)
            return empty;
        return switch (state) {
            case CUBE -> cubeContent;
            case TREE -> treeContent;
        };
    }

    @Override
    public void calculateSpectrum() {
        commonContent.calculateSpectrum();
    }

    @Override
    public String getResult() {
        return commonContent.getResult();
    }

    @Override
    public void getNewEntry(String table) {
        commonContent = new CommonContent(table);
        cubeContent = new CubeContent(commonContent);
        treeContent = new TreeContent(commonContent);
    }
}
