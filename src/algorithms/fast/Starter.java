package algorithms.fast;

import algorithms.SolutionsManager;

public class Starter {
    public static void main(String[] args) {
        SolutionsManager sm = new SolutionsManager("./src/in.txt");
        var solver = new FastSolver("0");
        sm.solveAll(solver);
        sm.printSpentTime();
        sm.writeSolutions();
        sm.closeInputFile();
    }
}
