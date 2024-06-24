package visualizer.painter.contents;

import visualizer.extendedAlgorithms.MetaInfo;
import visualizer.painter.contents.cube.CubeContent;
import visualizer.painter.contents.cube.CubeContentState;
import visualizer.painter.contents.tree.TreeContent;
import visualizer.painter.painter.IPainter;

public class ContentInfo {
    private final CubeContent cubeContent;
    private final TreeContent treeContent;
    private ContentState state = ContentState.CUBE;

    public ContentInfo(MetaInfo info) {
        cubeContent = new CubeContent(info.cube);
        treeContent = new TreeContent(info.tree);
    }

    public void select(int[] position) {
        getActiveContent().select(position);
    }

    public void deselectAll() {
        getActiveContent().deselectAll();
    }

    public void shiftAll(int[] difference) {
        getActiveContent().shiftAll(difference);
    }

    public void moveSelected(int[] difference) {
        getActiveContent().moveSelected(difference);
    }

    public void changeScale(int[] cursorPosition, double rot) {
        var relationOfScaling = Math.pow(1.2, -rot);
        getActiveContent().changeScale(cursorPosition, relationOfScaling);
    }

    public void paint(IPainter painter) {
        getActiveContent().paint(painter);
        painter.showSpectrum(cubeContent.getSpectrum());
    }

    public void changeState() {
        state = switch (state) {
            case CUBE -> ContentState.TREE;
            case TREE -> ContentState.CUBE;
        };
    }

    public ContentState getState() {
        return state;
    }

    public void switchRepresentation() {
        getActiveContent().switchState();
    }

    public void changeFaceNum(int direction) {
        if (state == ContentState.CUBE)
            cubeContent.changeCurrentFaceNum(direction);
    }

    public boolean shouldChangeFaceNum() {
        return state == ContentState.CUBE && cubeContent.getState() == CubeContentState.FACES;
    }

    public boolean canDeselectAll() {
        return getActiveContent().canDeselectAll();
    }

    public void goToSelected() {
        switch (state) {
            case CUBE -> goToSelectedFromCube();
            case TREE -> goToSelectedFromTree();
        }
    }

    public boolean canGoToSelected() {
        return getActiveContent().canGoToSelected();
    }

    private void goToSelectedFromCube() {
        if (cubeContent.canGoToSelected()) {
            state = ContentState.TREE;
            var node = cubeContent.getCurrentFace();
            treeContent.goToNodeFromCube(node);
        }
    }

    private void goToSelectedFromTree() {
        if (treeContent.canGoToSelected()) {
            state = ContentState.CUBE;
            cubeContent.setState(CubeContentState.FACES);
            cubeContent.setCurrentFaceNum(treeContent.getSelectedNodeValue());
        }
    }

    private IContent getActiveContent() {
        return switch (state) {
            case CUBE -> cubeContent;
            case TREE -> treeContent;
        };
    }
}
