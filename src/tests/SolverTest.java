package tests;

import algorithm.Solver;
import org.junit.Assert;
import org.junit.Test;

public class SolverTest {
    @Test
    public void checkSolve() {
        var solver = new Solver("10111010");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("10011010");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 1", solver.toString());
        solver = new Solver("0110");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1", solver.toString());
    }
}
