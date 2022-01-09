package tests;

import algorithm.Solver;
import org.junit.Assert;
import org.junit.Test;

public class SolverTest {
    @Test
    public void checkSolve() {
        var solver = new Solver("10111010");
        Assert.assertEquals("0, 1, 2, 0", solver.solve());
        solver = new Solver("10011010");
        Assert.assertEquals("0, 1, 2, 1", solver.solve());
        solver = new Solver("0110");
        Assert.assertEquals("0, 1, 1", solver.solve());
    }
}
