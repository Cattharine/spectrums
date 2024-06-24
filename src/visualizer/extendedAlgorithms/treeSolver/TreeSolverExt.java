package visualizer.extendedAlgorithms.treeSolver;

import algorithms.treeSolver.TreeSolver;
import algorithms.treeSolver.instances.KFace;
import visualizer.extendedAlgorithms.ISolverExt;
import visualizer.painter.contents.cube.Cube;
import visualizer.extendedAlgorithms.MetaInfo;

public class TreeSolverExt extends TreeSolver implements ISolverExt {
    private MetaInfo info;

    public TreeSolverExt(String table) {
        super(table);
        Cube cube = new Cube(n, table);
        info = new MetaInfo(cube);
    }

    @Override
    public void calculateSpectrum() {
        super.calculateSpectrum();
        var res = super.getResult();
        info.cube.setSpectrum(res);
    }

    @Override
    protected void process(KFace face) {
        if (face.isNotProcessed()) {
            var faceName = ((KFaceExt) face).name.toString();
            int faceValue = KFaceExt.getValueByName(faceName);
            info.tree.fillNode(faceValue, faceName, face.faceDimension);
            var mu = ((KFaceExt) face).getMaxMu(info, faceValue);
            if (spectrum[n - k] < mu) {
                info.tree.uncheckMaxNodes(n - k);
                info.tree.checkNodeAsMax(faceValue, n - k);
                spectrum[n - k] = mu;
            } else if (spectrum[n - k] == mu && mu != 0) {
                info.tree.checkNodeAsMax(faceValue, n - k);
            }
        }
    }

    @Override
    public void getNewEntry(String table) {
        creator = new VertexExtCreator();
        super.getNewEntry(table);
        var name = new StringBuilder("0".repeat(n));
        processing[0][0] = new KFaceExt(n, vertexes, n - 1, -1, name);

        Cube cube = new Cube(n, table);
        info = new MetaInfo(cube);
    }

    @Override
    public MetaInfo getInfo() {
        return info;
    }
}
