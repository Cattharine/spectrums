package tests;

import algorithms.ISolver;
import algorithms.fast.FastSolver;
import algorithms.treeSolver.TreeSolver;
import algorithms.straightforwardV2.SF2Solver;
import visualizer.extendedAlgorithms.treeSolver.TreeSolverExt;
import org.junit.Assert;
import org.junit.Test;

public class SolverTest {
    @Test
    public void checkTreeSolve() {
        var solver = new TreeSolver("1");
        checkCommonSolve(solver);
    }

    @Test
    public void checkExtTreeSolve() {
        var solver = new TreeSolverExt("1");
        checkCommonSolve(solver);
    }

    @Test
    public void checkSF2Solve() {
        var solver = new SF2Solver("1");
        checkCommonSolve(solver);
    }

    @Test
    public void checkFastSolve() {
        var solver = new FastSolver("1");
        checkCommonSolve(solver);
    }

    private void checkCommonSolve(ISolver solver) {
        solver.setPrintingState(false);
        checkEq(solver, "10111010", "0, 1, 2, 0");
        checkEq(solver, "10011010", "0, 1, 2, 1");
        checkEq(solver, "0110", "0, 1, 1");
    }

    @Test
    public void checkTree3D() {
        var solver = new TreeSolver("0");
        check3D(solver);
    }

    @Test
    public void checkExtTree3D() {
        var solver = new TreeSolverExt("0");
        check3D(solver);
    }

    @Test
    public void checkSF23D() {
        var solver = new SF2Solver("00000000");
        check3D(solver);
    }

    @Test
    public void checkFast3D() {
        var solver = new FastSolver("00000000");
        check3D(solver);
    }

    private void check3D(ISolver solver) {
        solver.setPrintingState(false);
        checkEq(solver, "00000000", "0, 0, 0, 0");
        checkEq(solver, "00000001", "0, 1, 2, 3");
        checkEq(solver, "00000011", "0, 1, 2, 0");
        checkEq(solver, "00000110", "0, 1, 2, 1");
        checkEq(solver, "00000111", "0, 1, 2, 0");
        checkEq(solver, "00001111", "0, 1, 0, 0");
        checkEq(solver, "00010110", "0, 1, 2, 1");
        checkEq(solver, "00010111", "0, 1, 2, 0");
        checkEq(solver, "00011000", "0, 1, 2, 2");
        checkEq(solver, "00011001", "0, 1, 2, 1");
        checkEq(solver, "00011011", "0, 1, 2, 0");
        checkEq(solver, "00011110", "0, 1, 2, 1");
        checkEq(solver, "00011111", "0, 1, 2, 0");
        checkEq(solver, "00111100", "0, 1, 1, 0");
        checkEq(solver, "00111101", "0, 1, 1, 0");
        checkEq(solver, "00111111", "0, 1, 0, 0");
        checkEq(solver, "01101001", "0, 1, 1, 1");
        checkEq(solver, "01101011", "0, 1, 1, 1");
        checkEq(solver, "01101111", "0, 1, 1, 0");
        checkEq(solver, "01111110", "0, 1, 0, 0");
        checkEq(solver, "01111111", "0, 1, 0, 0");
        checkEq(solver, "11111111", "0, 0, 0, 0");
        checkNotEq(solver, "11111111", "0, 0, 0, 1");
    }

    @Test
    public void checkTree4D() {
        var solver = new TreeSolver("0");
        check4D(solver);
    }

    @Test
    public void checkExtTree4D() {
        var solver = new TreeSolverExt("0");
        check4D(solver);
    }

    @Test
    public void checkSF24D() {
        var solver = new SF2Solver("0");
        check4D(solver);
    }

    @Test
    public void checkFast4D() {
        var solver = new FastSolver("0");
        check4D(solver);
    }

    private void check4D(ISolver solver) {
        solver.setPrintingState(false);
        checkEq(solver, "0000000000000000", "0, 0, 0, 0, 0");
        checkEq(solver, "0000000000000001", "0, 1, 2, 3, 4");
        checkEq(solver, "0000000000000011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000000000110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000000000111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000000001111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000000000010110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000000010111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000000011000", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000000011001", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000000011011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000000011110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000000011111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000000111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000000000111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000000000111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000000001101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000000001101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000000001101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000000001111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000000001111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000000011111111", "0, 1, 0, 0, 0");
        checkEq(solver, "0000000100010110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100010111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000100011000", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000100011001", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100011010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100011011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000100011110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100101100", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100101101", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100101111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000100111100", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100111101", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000100111110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000100111111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000101101000", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000101101001", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000101101010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000101101011", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000101101110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000101101111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000101111110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000101111111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110000000", "0, 1, 2, 3, 3");
        checkEq(solver, "0000000110000001", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000110000010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110000011", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110000110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110000111", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110001001", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110001011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110001111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110010110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110010111", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110011000", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110011001", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110011010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110011011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110011110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110011111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110101000", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000110101001", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110101010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110101011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110101100", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110101101", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110101110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110101111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110111100", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110111101", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000110111110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000110111111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000111101000", "0, 1, 2, 3, 2");
        checkEq(solver, "0000000111101001", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000111101010", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000111101011", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000111101110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000111101111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000000111111110", "0, 1, 2, 3, 1");
        checkEq(solver, "0000000111111111", "0, 1, 2, 3, 0");
        checkEq(solver, "0000001100111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001100111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001100111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001101010110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101010111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001101011000", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101011001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101011010", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101011011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101011110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101011111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001101101000", "0, 1, 2, 2, 1");
        checkEq(solver, "0000001101101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101101010", "0, 1, 2, 2, 1");
        checkEq(solver, "0000001101101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101101100", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101101101", "0, 1, 2, 1, 1");
        checkEq(solver, "0000001101101110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001101111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001111000000", "0, 1, 2, 2, 0");
        checkEq(solver, "0000001111000001", "0, 1, 2, 2, 0");
        checkEq(solver, "0000001111000011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111000101", "0, 1, 2, 2, 0");
        checkEq(solver, "0000001111000110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111000111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111001111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001111010100", "0, 1, 2, 2, 0");
        checkEq(solver, "0000001111010101", "0, 1, 2, 2, 0");
        checkEq(solver, "0000001111010110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111010111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011000", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011001", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011011", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001111011100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111011111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000001111111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000001111111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011001100000", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001100001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001100010", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001100011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001100110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001100111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001110010", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001110011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001110110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001110111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001111000", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001111001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001111010", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001111011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011001111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011001111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011010010000", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010010001", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010010011", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010010110", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010010111", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010011111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011010011111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011010110000", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010110001", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010110010", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010110011", "0, 1, 2, 2, 1");
        checkEq(solver, "0000011010110100", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010110101", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010110110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011010110111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011010111001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010111011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010111101", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011010111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011011110000", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011110001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011110010", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011110011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011110110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011011110111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011011111001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011111011", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011011111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011101110110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011101110111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011101111000", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011101111001", "0, 1, 2, 1, 1");
        checkEq(solver, "0000011101111010", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011101111011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011101111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011101111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011110110000", "0, 1, 2, 2, 0");
        checkEq(solver, "0000011110110001", "0, 1, 2, 2, 0");
        checkEq(solver, "0000011110110011", "0, 1, 2, 2, 0");
        checkEq(solver, "0000011110110100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110110101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110110110", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110110111", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011110111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111100000", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111100001", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111100010", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111100011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111100110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111100111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111101001", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111101011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111101111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111110000", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111110001", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111110010", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111110011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111110110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111110111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111111000", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111111001", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111111010", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111111011", "0, 1, 2, 1, 0");
        checkEq(solver, "0000011111111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0000011111111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0000111111110000", "0, 1, 1, 0, 0");
        checkEq(solver, "0000111111110001", "0, 1, 2, 0, 0");
        checkEq(solver, "0000111111110011", "0, 1, 1, 0, 0");
        checkEq(solver, "0000111111110110", "0, 1, 1, 0, 0");
        checkEq(solver, "0000111111110111", "0, 1, 1, 0, 0");
        checkEq(solver, "0000111111111111", "0, 1, 0, 0, 0");
        checkEq(solver, "0001011001101000", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001101010", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001101110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001101111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011001111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011001111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011010000001", "0, 1, 2, 2, 2");
        checkEq(solver, "0001011010000011", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010000110", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010000111", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010001001", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010001011", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010001110", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010001111", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011010010110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010010111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010011000", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010011001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010011010", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010011011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010011110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011010011111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011010101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010101100", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010101101", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010101110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010101111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010111100", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010111101", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011010111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011010111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011011101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011011101010", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011011101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011011101110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011011101111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011011111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011011111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011101111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001011101111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001011110000001", "0, 1, 2, 2, 2");
        checkEq(solver, "0001011110000011", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011110000111", "0, 1, 2, 2, 1");
        checkEq(solver, "0001011110001001", "0, 1, 2, 2, 0");
        checkEq(solver, "0001011110001011", "0, 1, 2, 2, 0");
        checkEq(solver, "0001011110001110", "0, 1, 2, 2, 0");
        checkEq(solver, "0001011110001111", "0, 1, 2, 2, 0");
        checkEq(solver, "0001011110010110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011110010111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001011110011000", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110011001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110011010", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110011011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110011110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110011111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101100", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101101", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110111100", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110111101", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011110111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001011110111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001011111101000", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111101001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111101010", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111101011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111101110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001011111111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001011111111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001100011100111", "0, 1, 2, 2, 1");
        checkEq(solver, "0001100011101111", "0, 1, 2, 2, 1");
        checkEq(solver, "0001100011111111", "0, 1, 2, 2, 0");
        checkEq(solver, "0001100111100001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111100011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111100110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111100111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111101001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111101010", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111101011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111101110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111110001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111110011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111110110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111110111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001100111111000", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111111001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111111010", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111111011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001100111111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111010110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111010111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111011000", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111011001", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111011011", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111011110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111011111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111100100", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111100101", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111100111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111101100", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111101101", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111101110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111101111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001101111111100", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111111101", "0, 1, 2, 0, 0");
        checkEq(solver, "0001101111111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111011100001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011100011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011100110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011100111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011101001", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011101011", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011101110", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011101111", "0, 1, 2, 1, 1");
        checkEq(solver, "0001111011110001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011110011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011110110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011110111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111001", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111010", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111011", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111110", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111011111111", "0, 1, 2, 1, 0");
        checkEq(solver, "0001111111110001", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111110010", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111110011", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111110110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111110111", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111000", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111001", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111010", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111011", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111110", "0, 1, 2, 0, 0");
        checkEq(solver, "0001111111111111", "0, 1, 2, 0, 0");
        checkEq(solver, "0011110011000011", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011000111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011001111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011010111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011011011", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011011111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110011111111", "0, 1, 1, 0, 0");
        checkEq(solver, "0011110111010110", "0, 1, 1, 1, 1");
        checkEq(solver, "0011110111010111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110111011010", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110111011011", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110111011110", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110111011111", "0, 1, 1, 1, 0");
        checkEq(solver, "0011110111101101", "0, 1, 1, 0, 0");
        checkEq(solver, "0011110111101111", "0, 1, 1, 0, 0");
        checkEq(solver, "0011110111111101", "0, 1, 1, 0, 0");
        checkEq(solver, "0011110111111110", "0, 1, 1, 0, 0");
        checkEq(solver, "0011110111111111", "0, 1, 1, 0, 0");
        checkEq(solver, "0011111111111100", "0, 1, 0, 0, 0");
        checkEq(solver, "0011111111111101", "0, 1, 0, 0, 0");
        checkEq(solver, "0011111111111111", "0, 1, 0, 0, 0");
        checkEq(solver, "0110100110010110", "0, 1, 1, 1, 1");
        checkEq(solver, "0110100110010111", "0, 1, 1, 1, 1");
        checkEq(solver, "0110100110011111", "0, 1, 1, 1, 1");
        checkEq(solver, "0110100110111111", "0, 1, 1, 1, 1");
        checkEq(solver, "0110100111111111", "0, 1, 1, 1, 0");
        checkEq(solver, "0110101110111101", "0, 1, 1, 1, 1");
        checkEq(solver, "0110101110111111", "0, 1, 1, 1, 1");
        checkEq(solver, "0110101111010110", "0, 1, 1, 1, 0");
        checkEq(solver, "0110101111010111", "0, 1, 1, 1, 0");
        checkEq(solver, "0110101111011111", "0, 1, 1, 1, 0");
        checkEq(solver, "0110101111111101", "0, 1, 1, 1, 0");
        checkEq(solver, "0110101111111111", "0, 1, 1, 1, 0");
        checkEq(solver, "0110111111110110", "0, 1, 1, 0, 0");
        checkEq(solver, "0110111111110111", "0, 1, 1, 0, 0");
        checkEq(solver, "0110111111111001", "0, 1, 1, 0, 0");
        checkEq(solver, "0110111111111011", "0, 1, 1, 0, 0");
        checkEq(solver, "0111111011111111", "0, 1, 0, 0, 0");
        checkEq(solver, "0111111111111110", "0, 1, 0, 0, 0");
        checkEq(solver, "0111111111111111", "0, 1, 0, 0, 0");
        checkEq(solver, "1111111111111111", "0, 0, 0, 0, 0");
        checkNotEq(solver, "1111111111111111", "0, 1, 1, 0, 0");
    }

    private void checkEq(ISolver sol, String table, String res) {
        sol.getNewEntry(table);
        sol.calculateSpectrum();
        Assert.assertEquals(res, sol.getResult());
    }

    private void checkNotEq(ISolver sol, String table, String res) {
        sol.getNewEntry(table);
        sol.calculateSpectrum();
        Assert.assertNotEquals(res, sol.getResult());
    }
}
