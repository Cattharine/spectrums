package visualizer.contents;

import algorithms.SolutionsManager;
import algorithms.SolverType;
import visualizer.contents.utils.availabilityUpdater.AvailabilityUpdaterType;
import visualizer.contents.utils.availabilityUpdater.IAvailabilityUpdater;
import visualizer.contents.utils.contentInterfaces.IContentContainer;
import visualizer.painter.IPainter;
import visualizer.painter.PainterType;

import java.util.ArrayList;

public class Dispatcher {
    protected final IPainter painter;
    private final ArrayList<IAvailabilityUpdater> updaters = new ArrayList<>();
    private final ArrayList<ContentInfo> contents = new ArrayList<>();
    private int activeContent = 0;
    private int[] memorizedCursorPosition = new int[2];

    public Dispatcher(PainterType type) {
        painter = PainterType.getPainter(type);
    }

    public void acceptAll(String inputFileName) {
        System.out.println("Started spectrum calculation");
        var manager = new ContentSolutionsManager(inputFileName);
        manager.solveAll();
        manager.writeSolutions();
        System.out.println("Ended spectrum calculation");
    }

    public void addUpdater(IAvailabilityUpdater updater) {
        updaters.add(updater);
    }

    public void updateAvailability() {
        for (var updater : updaters) {
            updater.setEnabled(getState(updater.getType()));
        }
    }

    private boolean getState(AvailabilityUpdaterType type) {
        return switch (type) {
            case GO_TO_SELECTED -> canGoToSelected();
            case DESELECT -> canDeselectAll();
            case CHANGE_FACE -> canChangeFace();
        };
    }

    public void memorizeCursorPosition(int[] position) {
        memorizedCursorPosition = position;
    }

    public void shiftAll(int[] currentPos) {
        var difference = getDifference(currentPos, memorizedCursorPosition);
        getActiveContent().shiftAll(difference);
        memorizedCursorPosition = currentPos;
    }

    public void changeScale(int[] cursorPosition, double rot) {
        getActiveContent().changeScale(cursorPosition, rot);
    }

    public void select(int[] position) {
        getActiveContent().select(position);
        memorizedCursorPosition = position;
    }

    public void deselectAll() {
        getActiveContent().deselectAll();
    }

    public void moveSelected(int[] currentPos) {
        var difference = getDifference(currentPos, memorizedCursorPosition);
        getActiveContent().moveSelected(difference);
        memorizedCursorPosition = currentPos;
    }

    private boolean canGoToSelected() {
        return contents.size() > 0 && contents.get(activeContent).canGoToSelected();
    }

    private boolean canDeselectAll() {
        return contents.size() > 0 && contents.get(activeContent).canDeselectAll();
    }

    public void goToSelected() {
        getActiveContent().goToSelected();
    }

    public void paint(Object panel, Object spectrumPlace, int width, int height) {
        painter.prepare(spectrumPlace, width, height);
        getActiveContent().paint(painter);
        painter.paint(panel);
    }

    public void switchState() {
        getActiveContent().switchState();
    }

    public void switchRepresentation() {
        getActiveContent().switchRepresentation();
    }

    public void changeActiveContent(int direction) {
        activeContent = (activeContent + direction + contents.size()) % contents.size();
    }

    public void changeFace(int direction) {
        getActiveContent().changeFace(direction);
    }

    private boolean canChangeFace() {
        return getActiveContent().canChangeFace();
    }

    public void changeFontSize(int direction) {
        painter.changeFontSize(direction);
    }

    private IContentContainer getActiveContent() {
        if (contents.size() > 0)
            return contents.get(activeContent);
        return ContentInfo.empty;
    }

    private int[] getDifference(int[] value, int[] other) {
        var difference = new int[2];
        difference[0] = value[0] - other[0];
        difference[1] = value[1] - other[1];

        return difference;
    }

    private void addContentInfo(ContentInfo info) {
        contents.add(info);
    }

    protected class ContentSolutionsManager extends SolutionsManager {
        protected ContentSolutionsManager(String inputFileName) {
            super(inputFileName, SolverType.VISUALIZER);
        }

        @Override
        protected void prepare(String input) {
            solver = new ContentInfo(input);
            addContentInfo((ContentInfo) solver);
        }
    }
}
