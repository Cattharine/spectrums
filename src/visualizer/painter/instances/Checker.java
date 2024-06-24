package visualizer.painter.instances;

import java.util.HashSet;
import java.util.Iterator;

public class Checker<T> implements Iterable<T> {
    private final HashSet<T> checker = new HashSet<>();

    public boolean check(T value) {
        return checker.contains(value);
    }

    public void add(T value) {
        checker.add(value);
    }

    public void clear() {
        checker.clear();
    }

    public void remove(T value) {
        checker.remove(value);
    }

    @Override
    public Iterator<T> iterator() {
        return checker.iterator();
    }

    public int size() {
        return checker.size();
    }
}
