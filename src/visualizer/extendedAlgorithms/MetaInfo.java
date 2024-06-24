package visualizer.extendedAlgorithms;

import visualizer.painter.contents.tree.Tree;
import visualizer.painter.contents.cube.Cube;

public class MetaInfo {
    public final Cube cube;
    public final Tree tree;

    public MetaInfo(Cube cube) {
        this.cube = cube;
        tree = new Tree(cube.n);
    }
}
