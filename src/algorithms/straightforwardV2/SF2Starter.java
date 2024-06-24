package algorithms.straightforwardV2;

import algorithms.SolutionsManager;

public class SF2Starter {
    public static void main(String[] args) {
        SolutionsManager sm = new SolutionsManager("./src/in.txt");
        var solver = new SF2Solver("0");
        sm.solveAll(solver);
        sm.printSpentTime();
        sm.writeSolutions();
        sm.closeInputFile();
    }
}
