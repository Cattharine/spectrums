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

    @Test
    public void check3D() {
        var solver = new Solver("00000000");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 0, 0, 0", solver.toString());
        solver = new Solver("00000001");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 3", solver.toString());
        solver = new Solver("00000011");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("00000110");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 1", solver.toString());
        solver = new Solver("00000111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("00001111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 0, 0", solver.toString());
        solver = new Solver("00010110");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 1", solver.toString());
        solver = new Solver("00010111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("00011000");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 2", solver.toString());
        solver = new Solver("00011001");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 1", solver.toString());
        solver = new Solver("00011011");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("00011110");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 1", solver.toString());
        solver = new Solver("00011111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 2, 0", solver.toString());
        solver = new Solver("00111100");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1, 0", solver.toString());
        solver = new Solver("00111101");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1, 0", solver.toString());
        solver = new Solver("00111111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 0, 0", solver.toString());
        solver = new Solver("01101001");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1, 1", solver.toString());
        solver = new Solver("01101011");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1, 1", solver.toString());
        solver = new Solver("01101111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 1, 0", solver.toString());
        solver = new Solver("01111110");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 0, 0", solver.toString());
        solver = new Solver("01111111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 1, 0, 0", solver.toString());
        solver = new Solver("11111111");
        solver.calculateSpectrum();
        Assert.assertEquals("0, 0, 0, 0", solver.toString());
        solver = new Solver("11111111");
        solver.calculateSpectrum();
        Assert.assertNotEquals("0, 0, 0, 1", solver.toString());
    }
}
