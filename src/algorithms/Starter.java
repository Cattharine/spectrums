package algorithms;

public class Starter {
    public static void main(String[] args) {
        var type = args.length > 0 ? SolverType.getType(args[0]) : SolverType.TREE;
        SolutionsManager sm = new SolutionsManager("./src/in.txt", type);
        sm.solveAll();
        sm.printSpentTime();
        sm.writeSolutions();
    }
}
