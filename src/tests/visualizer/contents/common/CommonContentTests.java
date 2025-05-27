package tests.visualizer.contents.common;

import algorithms.Solver;
import org.junit.Test;
import org.junit.Assert;
import visualizer.contents.common.CommonContent;

import java.util.HashMap;
import java.util.function.Function;

public class CommonContentTests {
    // getResult и calculateSpectrum тестируются в SolverTest
    private final FaceTests tests = new FaceTests();
    private final static int maxN = 16;

    @Test
    public void checkGettingN() {
        tests.check(this::checkGettingN);

        CommonContent commonContent = new CommonContent("0".repeat(1 << 13));
        Assert.assertEquals(13, commonContent.getN());
    }

    private void checkGettingN(FaceTests.TestCase testCase) {
        CommonContent common = new CommonContent(testCase.getTable());
        Assert.assertEquals(testCase.getFaceName().length(), common.getN());
    }

    @Test
    public void checkGettingFactorValue() {
        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(1 << i, CommonContent.getFactorValue(i));
        }
    }

    @Test
    public void checkImplementingSpectrum() {
        tests.check(this::checkImplementingSpectrum);
    }

    private void checkImplementingSpectrum(FaceTests.TestCase testCase) {
        CommonContent common = new CommonContent(testCase.getTable());
        int faceValue = Integer.parseInt(testCase.getFaceName(), 3);

        Assert.assertFalse(common.isImplementingSpectrum(faceValue));

        common.calculateSpectrum();

        int mu = tests.getMu(testCase);
        boolean expected = mu == getSpectrumValue(faceValue, common) && mu > 0;
        Assert.assertEquals(expected, common.isImplementingSpectrum(faceValue));
    }

    private int getSpectrumValue(int faceValue, CommonContent common) {
        try {
            var spectrumField = Solver.class.getDeclaredField("spectrum");
            spectrumField.setAccessible(true);
            var spectrum = (int[]) spectrumField.get(common);
            return spectrum[common.cube.getFace(faceValue).info.dimension];
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Cube
    @Test
    public void checkActiveVertexChecking() {
        tests.check(this::checkActiveVertexChecking);
    }

    private void checkActiveVertexChecking(FaceTests.TestCase testCase) {
        String table = testCase.getTable();
        CommonContent content = new CommonContent(table);
        Function<Integer, Boolean> checker = tests.getVertexChecker(table);

        for (int vertex = 0; vertex < table.length(); vertex++) {
            Assert.assertEquals(checker.apply(vertex), content.cube.isActiveVertex(vertex));
        }
    }

    @Test
    public void checkMaxVertexChecking() {
        tests.check(this::checkMaxVertexChecking);
    }

    private void checkMaxVertexChecking(FaceTests.TestCase testCase) {
        CommonContent content = new CommonContent(testCase.getTable());
        int[] maxVertexChecker = tests.getMaxVertexChecker(testCase);

        for (int vertex = 0; vertex < maxVertexChecker.length; vertex++) {
            int face = Integer.parseInt(testCase.getFaceName(), 3);
            Assert.assertEquals(maxVertexChecker[vertex] != 0, content.cube.isMaxVertex(vertex, face));
        }
    }

    @Test
    public void checkGettingVertexName() {
        for (int i = 0; i < 4; i++) {
            CommonContent common = new CommonContent("0".repeat(1 << i));
            var testCases = getTestCases(i);

            for (int value = 0; value < testCases.length; value++) {
                Assert.assertEquals(testCases[value], common.cube.getVertexName(value));
            }
            Assert.assertEquals("", common.cube.getVertexName(-1));
        }
    }

    private String[] getTestCases(int n) {
        return switch (n) {
            case 0 -> getTestCases0D();
            case 1 -> getTestCases1D();
            case 2 -> getTestCases2D();
            default -> getTestCases3D();
        };
    }

    private String[] getTestCases0D() {
        return new String[]{"0"};
    }

    private String[] getTestCases1D() {
        return new String[]{"0", "1", "10"};
    }

    private String[] getTestCases2D() {
        return new String[]{"00", "01", "10", "11", "100", "101"};
    }

    private String[] getTestCases3D() {
        return new String[]{"000", "001", "010", "011", "100", "101", "110", "111", "1000", "1001", "1010"};
    }

    @Test
    public void checkGettingPresentedFactors() {
        CommonContent content = new CommonContent("0".repeat(1 << 3));
        HashMap<Integer, Integer[]> testCases = new HashMap<>() {
            {
                put(0, new Integer[0]);
                put(1, new Integer[]{0});
                put(2, new Integer[]{1});
                put(3, new Integer[]{0, 1});
                put(4, new Integer[]{2});
                put(5, new Integer[]{0, 2});
                put(6, new Integer[]{1, 2});
                put(7, new Integer[]{0, 1, 2});
                put(8, new Integer[0]);
                put(9, new Integer[]{0});
                put(10, new Integer[]{1});
                put(-1, new Integer[]{0, 1, 2});
            }
        };

        for (var testCase : testCases.keySet()) {
            Assert.assertArrayEquals(testCases.get(testCase), content.cube.getPresentedFactors(testCase).toArray());
        }
    }

    @Test
    public void checkGettingFaces() {
        for (int n = 1; n < maxN; n++) {
            try {
                var content = new CommonContent("0".repeat(1 << n));

                int numberOfFaces = (int) Math.pow(3, n);
                for (int face = 0; face < numberOfFaces; face++) {
                    Assert.assertEquals(face, content.cube.getFace(face).info.value);
                }
                // non-existing nodes
                Assert.assertNull(content.cube.getFace(-1));
                Assert.assertNull(content.cube.getFace(numberOfFaces));
                Assert.assertNull(content.cube.getFace(numberOfFaces + 1));
            } catch (OutOfMemoryError e) {
                System.out.printf("Not enough memory for %d%n", n);
                break;
            }
        }
        var content = new CommonContent("0");
        Assert.assertEquals(1, content.cube.getFace(0).info.value);
        Assert.assertNotNull(content.cube.getFace(1));
        // non-existing nodes
        Assert.assertNull(content.cube.getFace(-1));
        Assert.assertNull(content.cube.getFace(2));
    }

    // Tree
    @Test
    public void checkGettingNumberOfFaces() {
        for (int n = 0; n < maxN; n++) {
            try {
                var content = new CommonContent("0".repeat(1 << n));
                Assert.assertEquals((long) Math.pow(3, n), content.getNumberOfFaces());
            } catch (OutOfMemoryError e) {
                System.out.printf("Not enough memory for %d%n", n);
                break;
            }
        }
    }

    @Test
    public void checkGettingNodes() {
        for (int n = 1; n < maxN; n++) {
            try {
                var content = new CommonContent("0".repeat(1 << n));

                int numberOfNodes = (int) Math.pow(3, n);
                for (int node = 0; node < numberOfNodes; node++) {
                    Assert.assertEquals(node, content.tree.getNode(node).face.info.value);
                }
                // non-existing nodes
                Assert.assertNull(content.tree.getNode(-1));
                Assert.assertNull(content.tree.getNode(numberOfNodes));
                Assert.assertNull(content.tree.getNode(numberOfNodes + 1));
            } catch (OutOfMemoryError e) {
                System.out.printf("Not enough memory for %d%n", n);
                break;
            }
        }
        var content = new CommonContent("0");
        Assert.assertEquals(1, content.tree.getNode(0).face.info.value);
        Assert.assertNotNull(content.tree.getNode(1));
        // non-existing nodes
        Assert.assertNull(content.tree.getNode(-1));
        Assert.assertNull(content.tree.getNode(2));
    }

    @Test
    public void checkTNodesParentMethods() {
        CommonContent content = new CommonContent("0".repeat(1 << 13));

        int[] values = new int[]{
                Integer.parseInt("2012101012202", 3),
                Integer.parseInt("2012101012200", 3),
                Integer.parseInt("2012101012000", 3),
                Integer.parseInt("2012101010000", 3),
                Integer.parseInt("2012101000000", 3),
                Integer.parseInt("2012100000000", 3),
                Integer.parseInt("2012000000000", 3),
                Integer.parseInt("2010000000000", 3),
                Integer.parseInt("2000000000000", 3),
                Integer.parseInt("0000000000000", 3),
                -1
        };

        for (int i = 0; i < values.length - 1; i++) {
            var node = content.tree.getNode(values[i]);
            var parent = content.tree.getNode(values[i + 1]);

            Assert.assertEquals(values[i], node.face.info.value);
            Assert.assertEquals(values[i + 1], node.getParentValue());
            Assert.assertEquals(4 + i, node.face.info.dimension);
            if (values[i] != 0) {
                Assert.assertNotEquals(parent.face.info.value, parent.getLastNodeOfSubtree());
                Assert.assertNotEquals(1, parent.getNumberOfLeavesInSubtree());
                Assert.assertNotEquals(0, parent.getNumberOfLeavesInSubtree());
            } else Assert.assertNull(parent);
        }
    }

    @Test
    public void checkTNodesTreeMethods() {
        CommonContent content = new CommonContent("0".repeat(1 << 13));

        int[] values = new int[]{
                Integer.parseInt("2012101012202", 3),
                Integer.parseInt("2012101012200", 3),
                Integer.parseInt("2012101012000", 3),
                Integer.parseInt("2012101010000", 3),
                Integer.parseInt("2012101000000", 3),
                Integer.parseInt("2012100000000", 3),
                Integer.parseInt("2012000000000", 3),
                Integer.parseInt("2010000000000", 3),
                Integer.parseInt("2000000000000", 3),
                Integer.parseInt("0000000000000", 3)
        };


        int[][] expected = new int[][]{
                // last node in subtree, number of leaves in subtree
                new int[]{Integer.parseInt("2012101012202", 3), 1},
                new int[]{Integer.parseInt("2012101012222", 3), 6},
                new int[]{Integer.parseInt("2012101012222", 3), 18},
                new int[]{Integer.parseInt("2012101012222", 3), 54},
                new int[]{Integer.parseInt("2012101222222", 3), 486},
                new int[]{Integer.parseInt("2012122222222", 3), 4374},
                new int[]{Integer.parseInt("2012222222222", 3), 13122},
                new int[]{Integer.parseInt("2012222222222", 3), 39366},
                new int[]{Integer.parseInt("2".repeat(13), 3), (int) Math.pow(3, 11) * 2},
                new int[]{Integer.parseInt("2".repeat(13), 3), (int) Math.pow(3, 12) * 2}
        };

        for (int i = 0; i < values.length; i++) {
            var node = content.tree.getNode(values[i]);
            Assert.assertEquals(expected[i][0], node.getLastNodeOfSubtree());
            Assert.assertEquals(expected[i][1], node.getNumberOfLeavesInSubtree());
        }
    }
}
