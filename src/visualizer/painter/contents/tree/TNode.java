package visualizer.painter.contents.tree;

import visualizer.painter.instances.Coordinates;

import java.util.*;

public class TNode implements Iterable<TNode> {
    public final int value;
    public final String name;
    public HashMap<TreeContentState, Coordinates> coordinates;
    private final ArrayList<TNode> children = new ArrayList<>();
    public final int dimension;

    public TNode(int value, String name, int dimension) {
        this.value = value;
        this.name = name;
        coordinates = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TreeContentState.TREE, new Coordinates()),
                new AbstractMap.SimpleEntry<>(TreeContentState.WITHOUT_LINKS, new Coordinates()))
        );
        this.dimension = dimension;
    }

    public int getParentValue() {
        for (int spliterator = name.length() - 1; spliterator > -1; spliterator--) {
            if (name.charAt(spliterator) != '0') {
                var parentName = new StringBuilder(name);
                parentName.replace(spliterator, spliterator + 1, "0");
                return Integer.parseInt(parentName.toString(), 3);
            }
        }
        return -1;
    }

    public double getWeightAverageXOfChildren(TreeContentState state) {
        double x = 0;
        double denominator = 0;

        for (var child : children) {
            double weight = 2 * child.dimension - child.children.size() + 1;
            x += child.coordinates.get(state).getX() * weight;
            denominator += weight;
        }

        return x / denominator;
    }

    public void addChild(TNode child) {
        children.add(child);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Iterator<TNode> iterator() {
        return children.iterator();
    }
}
