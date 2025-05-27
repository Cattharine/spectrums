package tests.visualizer.painter;

import visualizer.contents.utils.Coordinates;
import visualizer.contents.utils.elementInfo.ElementInfo;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;
import visualizer.painter.IPainter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class TestPainter implements IPainter {
    private final Function<int[], Integer> map;
    public final HashMap<ElementType, HashMap<Integer, HashSet<Integer>>> edges = new HashMap<>();
    public final HashMap<ElementType, HashSet<Integer>> vertexes = new HashMap<>();
    public final ArrayList<Integer> face = new ArrayList<>();
    public Coordinates offset = new Coordinates();
    public final HashMap<Integer, HashSet<ElementState>> vertexesStates = new HashMap<>();
    public boolean isInitialized;

    public TestPainter(Function<int[], Integer> map) {
        this.map = map;
        isInitialized = false;
    }

    @Override
    public void prepare(Object spectrumPlace, int width, int height) {

    }

    @Override
    public void drawLine(int[] from, int[] to, ElementInfo info) {
        isInitialized = true;
        int fromVertex = map.apply(from);
        int toVertex = map.apply(to);
        if (!edges.containsKey(info.type))
            edges.put(info.type, new HashMap<>());
        if (!edges.get(info.type).containsKey(fromVertex))
            edges.get(info.type).put(fromVertex, new HashSet<>());
        edges.get(info.type).get(fromVertex).add(toVertex);
    }

    @Override
    public void drawDisc(int[] centre, int width, int height, ElementInfo info) {
        isInitialized = true;
        int centreVertex = map.apply(centre);
        if (!vertexes.containsKey(info.type))
            vertexes.put(info.type, new HashSet<>());
        vertexes.get(info.type).add(centreVertex);
        if (!vertexesStates.containsKey(centreVertex))
            vertexesStates.put(centreVertex, new HashSet<>());
        vertexesStates.get(centreVertex).add(info.state);
    }

    @Override
    public void drawString(String str, int[] position, ElementInfo info) {
    }

    @Override
    public void fillPolygon(ArrayList<int[]> points, ElementInfo info) {
        isInitialized = true;
        for (var point : points) {
            face.add(map.apply(point));
        }
    }

    @Override
    public void changeFontSize(int value) {
    }

    @Override
    public void showSpectrum(String spectrum) {
    }

    @Override
    public void offset(Coordinates value) {
        offset.setValue(value);
    }

    @Override
    public void paint(Object panel) {
    }
}
