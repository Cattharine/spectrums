package algorithms.fast_variants;

import algorithms.fast.FastSolver;

import java.util.HashSet;

public class FastSolverWithXorSim extends FastSolver {
    private HashSet<String> checked;
    private String currentCube;

    public FastSolverWithXorSim(String table) {
        super(table);
    }

    @Override
    public void getNewEntry(String table) {
        super.getNewEntry(table);
        checked = new HashSet<>();
    }

    @Override
    protected void processCurrent(int current) {
        if (checked.contains(currentCube))
            return;
        super.processCurrent(current);
        checked.add(currentCube);
    }

    @Override
    protected void preprocess(int current) {
        super.preprocess(current);
        var currentCubeBuilder = new StringBuilder();
        for (int vertex = 0; vertex < supportChecker.length; vertex++) {
            currentCubeBuilder.append(supportChecker[vertex ^ current]);
        }
        currentCube = currentCubeBuilder.toString();
    }
}
