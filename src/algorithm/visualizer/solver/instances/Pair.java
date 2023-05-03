package algorithm.visualizer.solver.instances;

import java.util.ArrayList;

public class Pair {
    private final ArrayList<Vertex> first;
    private final ArrayList<Vertex> second;

    public Pair(ArrayList<Vertex> first, ArrayList<Vertex> second) {
        this.first = first;
        this.second = second;
    }

    public ArrayList<Vertex> getFirst() {
        return first;
    }

    public ArrayList<Vertex> getSecond() {
        return second;
    }
}
