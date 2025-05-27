package tests.visualizer.contents.common;

import org.junit.Assert;
import org.junit.Test;
import visualizer.contents.common.FaceInfo;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class FaceInfoTests {
    private final static int maxTestVertex = 1 << 14;

    @Test
    public void checkValues() {
        check(this::checkValue);
    }

    private void checkValue(TestCase testCase) {
        var face = new FaceInfo(testCase.getFaceName());
        Assert.assertEquals(testCase.getFaceName(), face.name);
        Assert.assertEquals(Integer.parseInt(testCase.getFaceName(), 3), face.value);
        int[] freeFactors = getFreeFactorsCheckers(testCase);
        Assert.assertNotNull(freeFactors);
        Assert.assertEquals(Arrays.stream(freeFactors).sum(), face.dimension);
    }

    @Test
    public void checkFactorCheckers() {
        check(this::checkFactorCheckers);
    }

    private void checkFactorCheckers(TestCase testCase) {
        var face = new FaceInfo(testCase.getFaceName());
        int[] free = getFreeFactorsCheckers(testCase);
        int[] notPresented = getNotPresentedFactorCheckers(testCase);
        int[] presented = getPresentedFactorCheckers(testCase);

        Assert.assertNotNull(free);
        Assert.assertNotNull(presented);
        Assert.assertNotNull(notPresented);
        for (int factor = 0; factor < free.length; factor++) {
            Assert.assertEquals(free[factor] != 0, face.isFactorFree(factor));
            Assert.assertEquals(presented[factor] != 0, face.isFactorPresented(factor));
            Assert.assertEquals(notPresented[factor] != 0, face.isFactorNotPresented(factor));
            Assert.assertTrue((free[factor] ^ presented[factor] ^ notPresented[factor]) != 0);
            Assert.assertFalse((free[factor] & presented[factor] & notPresented[factor]) != 0);
        }
    }

    @Test
    public void checkVertexContaining() {
        check(this::checkVertexContaining);
    }

    private void checkVertexContaining(TestCase testCase) {
        var face = new FaceInfo(testCase.getFaceName());
        int[] vertexes = getVertexesInFace(testCase);
        int[] expected = new int[1 << testCase.getFaceName().length()];

        Assert.assertNotNull(vertexes);
        for (var vertex : vertexes) {
            expected[vertex] = 1;
        }

        for (int vertex = -maxTestVertex; vertex <= maxTestVertex; vertex++) {
            boolean notExpectedAnswer = vertex < 0 || vertex >= expected.length || expected[vertex] == 0;
            Assert.assertEquals(!notExpectedAnswer, face.containsVertex(vertex));
        }
    }

    @Test
    public void checkGettingAllVertexes() {
        check(this::checkGettingAllVertexes);
    }

    private void checkGettingAllVertexes(TestCase testCase) {
        var face = new FaceInfo(testCase.getFaceName());
        var vertexes = face.getAllVertexes();
        var expected = getVertexesInFace(testCase);

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.length, vertexes.size());
        Assert.assertEquals(expected.length, 1 << face.dimension);
        for (int vertex : expected) {
            Assert.assertTrue(vertexes.contains(vertex));
        }
    }

    @Test
    public void checkGettingFreeFactors() {
        check(this::checkGettingFreeFactors);
    }

    private void checkGettingFreeFactors(TestCase testCase) {
        var face = new FaceInfo(testCase.getFaceName());
        var freeFactors = face.getFreeFactors();
        int[] expectedFree = getFreeFactorsCheckers(testCase);
        int[] expectedNotPresented = getNotPresentedFactorCheckers(testCase);
        int[] expectedPresented = getPresentedFactorCheckers(testCase);

        Assert.assertNotNull(expectedFree);
        Assert.assertNotNull(expectedNotPresented);
        Assert.assertNotNull(expectedPresented);
        for (int factor : freeFactors) {
            Assert.assertTrue(expectedFree[factor] != 0);
            Assert.assertFalse(expectedNotPresented[factor] != 0);
            Assert.assertFalse(expectedPresented[factor] != 0);
        }
    }

    private void check(Consumer<TestCase> check) {
        var testCasesSmall = TestCase.values();
        for (var testCase : testCasesSmall) {
            check.accept(testCase);
        }
    }

    private int[] getVertexesInFace(TestCase face) {
        return switch (face) {
            case F0, F10, F110, F1110 -> new int[]{0, 1};
            case F1, F11, F111, F1111 -> new int[]{0};
            case F2, F12, F112, F1112 -> new int[]{1};
            case F00, F100, F1100 -> new int[]{0, 1, 2, 3};
            case F01, F101, F1101 -> new int[]{0, 2};
            case F02, F102, F1102 -> new int[]{1, 3};
            case F20, F120, F1120 -> new int[]{2, 3};
            case F21, F121, F1121 -> new int[]{2};
            case F22, F122, F1122 -> new int[]{3};
            case F000, F1000 -> new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            case F001, F1001 -> new int[]{0, 2, 4, 6};
            case F002, F1002 -> new int[]{1, 3, 5, 7};
            case F010, F1010 -> new int[]{0, 1, 4, 5};
            case F011, F1011 -> new int[]{0, 4};
            case F012, F1012 -> new int[]{1, 5};
            case F020, F1020 -> new int[]{2, 3, 6, 7};
            case F021, F1021 -> new int[]{2, 6};
            case F022, F1022 -> new int[]{3, 7};
            case F200, F1200 -> new int[]{4, 5, 6, 7};
            case F201, F1201 -> new int[]{4, 6};
            case F202, F1202 -> new int[]{5, 7};
            case F210, F1210 -> new int[]{4, 5};
            case F211, F1211 -> new int[]{4};
            case F212, F1212 -> new int[]{5};
            case F220, F1220 -> new int[]{6, 7};
            case F221, F1221 -> new int[]{6};
            case F222, F1222 -> new int[]{7};
            case F0000 -> new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            case F0001 -> new int[]{0, 2, 4, 6, 8, 10, 12, 14};
            case F0002 -> new int[]{1, 3, 5, 7, 9, 11, 13, 15};
            case F0010 -> new int[]{0, 1, 4, 5, 8, 9, 12, 13};
            case F0011 -> new int[]{0, 4, 8, 12};
            case F0012 -> new int[]{1, 5, 9, 13};
            case F0020 -> new int[]{2, 3, 6, 7, 10, 11, 14, 15};
            case F0021 -> new int[]{2, 6, 10, 14};
            case F0022 -> new int[]{3, 7, 11, 15};
            case F0100 -> new int[]{0, 1, 2, 3, 8, 9, 10, 11};
            case F0101 -> new int[]{0, 2, 8, 10};
            case F0102 -> new int[]{1, 3, 9, 11};
            case F0110 -> new int[]{0, 1, 8, 9};
            case F0111 -> new int[]{0, 8};
            case F0112 -> new int[]{1, 9};
            case F0120 -> new int[]{2, 3, 10, 11};
            case F0121 -> new int[]{2, 10};
            case F0122 -> new int[]{3, 11};
            case F0200 -> new int[]{4, 5, 6, 7, 12, 13, 14, 15};
            case F0201 -> new int[]{4, 6, 12, 14};
            case F0202 -> new int[]{5, 7, 13, 15};
            case F0210 -> new int[]{4, 5, 12, 13};
            case F0211 -> new int[]{4, 12};
            case F0212 -> new int[]{5, 13};
            case F0220 -> new int[]{6, 7, 14, 15};
            case F0221 -> new int[]{6, 14};
            case F0222 -> new int[]{7, 15};
            case F2000 -> new int[]{8, 9, 10, 11, 12, 13, 14, 15};
            case F2001 -> new int[]{8, 10, 12, 14};
            case F2002 -> new int[]{9, 11, 13, 15};
            case F2010 -> new int[]{8, 9, 12, 13};
            case F2011 -> new int[]{8, 12};
            case F2012 -> new int[]{9, 13};
            case F2020 -> new int[]{10, 11, 14, 15};
            case F2021 -> new int[]{10, 14};
            case F2022 -> new int[]{11, 15};
            case F2100 -> new int[]{8, 9, 10, 11};
            case F2101 -> new int[]{8, 10};
            case F2102 -> new int[]{9, 11};
            case F2110 -> new int[]{8, 9};
            case F2111 -> new int[]{8};
            case F2112 -> new int[]{9};
            case F2120 -> new int[]{10, 11};
            case F2121 -> new int[]{10};
            case F2122 -> new int[]{11};
            case F2200 -> new int[]{12, 13, 14, 15};
            case F2201 -> new int[]{12, 14};
            case F2202 -> new int[]{13, 15};
            case F2210 -> new int[]{12, 13};
            case F2211 -> new int[]{12};
            case F2212 -> new int[]{13};
            case F2220 -> new int[]{14, 15};
            case F2221 -> new int[]{14};
            case F2222 -> new int[]{15};
            case F20121010122000 -> getAllVertexesForTestCase14D();
        };
    }

    private int[] getAllVertexesForTestCase14D() {
        // test face is [20121010122000]
        Function<String, Integer> convert = (String vertex) -> Integer.parseInt(vertex, 2);
        return new int[]{
                convert.apply("10010000011000"),
                convert.apply("10010000011001"),
                convert.apply("10010000011010"),
                convert.apply("10010000011011"),
                convert.apply("10010000011100"),
                convert.apply("10010000011101"),
                convert.apply("10010000011110"),
                convert.apply("10010000011111"),
                convert.apply("10010001011000"),
                convert.apply("10010001011001"),
                convert.apply("10010001011010"),
                convert.apply("10010001011011"),
                convert.apply("10010001011100"),
                convert.apply("10010001011101"),
                convert.apply("10010001011110"),
                convert.apply("10010001011111"),
                convert.apply("10010100011000"),
                convert.apply("10010100011001"),
                convert.apply("10010100011010"),
                convert.apply("10010100011011"),
                convert.apply("10010100011100"),
                convert.apply("10010100011101"),
                convert.apply("10010100011110"),
                convert.apply("10010100011111"),
                convert.apply("10010101011000"),
                convert.apply("10010101011001"),
                convert.apply("10010101011010"),
                convert.apply("10010101011011"),
                convert.apply("10010101011100"),
                convert.apply("10010101011101"),
                convert.apply("10010101011110"),
                convert.apply("10010101011111"),
                convert.apply("11010000011000"),
                convert.apply("11010000011001"),
                convert.apply("11010000011010"),
                convert.apply("11010000011011"),
                convert.apply("11010000011100"),
                convert.apply("11010000011101"),
                convert.apply("11010000011110"),
                convert.apply("11010000011111"),
                convert.apply("11010001011000"),
                convert.apply("11010001011001"),
                convert.apply("11010001011010"),
                convert.apply("11010001011011"),
                convert.apply("11010001011100"),
                convert.apply("11010001011101"),
                convert.apply("11010001011110"),
                convert.apply("11010001011111"),
                convert.apply("11010100011000"),
                convert.apply("11010100011001"),
                convert.apply("11010100011010"),
                convert.apply("11010100011011"),
                convert.apply("11010100011100"),
                convert.apply("11010100011101"),
                convert.apply("11010100011110"),
                convert.apply("11010100011111"),
                convert.apply("11010101011000"),
                convert.apply("11010101011001"),
                convert.apply("11010101011010"),
                convert.apply("11010101011011"),
                convert.apply("11010101011100"),
                convert.apply("11010101011101"),
                convert.apply("11010101011110"),
                convert.apply("11010101011111"),
        };
    }

    private int[] getFreeFactorsCheckers(TestCase face) {
        return switch (face) {
            case F0 -> new int[]{1};
            case F1, F2 -> new int[]{0};
            case F00 -> new int[]{1, 1};
            case F01, F02 -> new int[]{0, 1};
            case F10, F20 -> new int[]{1, 0};
            case F11, F12, F21, F22 -> new int[]{0, 0};
            case F000 -> new int[]{1, 1, 1};
            case F001, F002 -> new int[]{0, 1, 1};
            case F010, F020 -> new int[]{1, 0, 1};
            case F011, F012, F021, F022 -> new int[]{0, 0, 1};
            case F100, F200 -> new int[]{1, 1, 0};
            case F101, F102, F201, F202 -> new int[]{0, 1, 0};
            case F110, F120, F210, F220 -> new int[]{1, 0, 0};
            case F111, F112, F121, F122, F211, F212, F221, F222 -> new int[]{0, 0, 0};
            case F0000 -> new int[]{1, 1, 1, 1};
            case F0001, F0002 -> new int[]{0, 1, 1, 1};
            case F0010, F0020 -> new int[]{1, 0, 1, 1};
            case F0011, F0012, F0021, F0022 -> new int[]{0, 0, 1, 1};
            case F0100, F0200 -> new int[]{1, 1, 0, 1};
            case F0101, F0102, F0201, F0202 -> new int[]{0, 1, 0, 1};
            case F0110, F0120, F0210, F0220 -> new int[]{1, 0, 0, 1};
            case F0111, F0112, F0121, F0122, F0211, F0212, F0221, F0222 -> new int[]{0, 0, 0, 1};
            case F1000, F2000 -> new int[]{1, 1, 1, 0};
            case F1001, F1002, F2001, F2002 -> new int[]{0, 1, 1, 0};
            case F1010, F1020, F2010, F2020 -> new int[]{1, 0, 1, 0};
            case F1011, F1012, F1021, F1022, F2011, F2012, F2021, F2022 -> new int[]{0, 0, 1, 0};
            case F1100, F1200, F2100, F2200 -> new int[]{1, 1, 0, 0};
            case F1101, F1102, F1201, F1202, F2101, F2102, F2201, F2202 -> new int[]{0, 1, 0, 0};
            case F1110, F1120, F1210, F1220, F2110, F2120, F2210, F2220 -> new int[]{1, 0, 0, 0};
            case F1111, F1112, F1121, F1122, F1211, F1212, F1221, F1222,
                    F2111, F2112, F2121, F2122, F2211, F2212, F2221, F2222 -> new int[]{0, 0, 0, 0};
            case F20121010122000 -> new int[]{1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0};
        };
    }

    private int[] getNotPresentedFactorCheckers(TestCase face) {
        return switch (face) {
            case F0, F2 -> new int[]{0};
            case F1 -> new int[]{1};
            case F00, F02, F20, F22 -> new int[]{0, 0};
            case F01, F21 -> new int[]{1, 0};
            case F10, F12 -> new int[]{0, 1};
            case F11 -> new int[]{1, 1};
            case F000, F002, F020, F022, F200, F202, F220, F222 -> new int[]{0, 0, 0};
            case F001, F021, F201, F221 -> new int[]{1, 0, 0};
            case F010, F012, F210, F212 -> new int[]{0, 1, 0};
            case F011, F211 -> new int[]{1, 1, 0};
            case F100, F102, F120, F122 -> new int[]{0, 0, 1};
            case F101, F121 -> new int[]{1, 0, 1};
            case F110, F112 -> new int[]{0, 1, 1};
            case F111 -> new int[]{1, 1, 1};
            case F0000, F0002, F0020, F0022, F0200, F0202, F0220, F0222,
                    F2000, F2002, F2020, F2022, F2200, F2202, F2220, F2222 -> new int[]{0, 0, 0, 0};
            case F0001, F0021, F0201, F0221, F2001, F2021, F2201, F2221 -> new int[]{1, 0, 0, 0};
            case F0010, F0012, F0210, F0212, F2010, F2012, F2210, F2212 -> new int[]{0, 1, 0, 0};
            case F0011, F0211, F2011, F2211 -> new int[]{1, 1, 0, 0};
            case F0100, F0102, F0120, F0122, F2100, F2102, F2120, F2122 -> new int[]{0, 0, 1, 0};
            case F0101, F0121, F2101, F2121 -> new int[]{1, 0, 1, 0};
            case F0110, F0112, F2110, F2112 -> new int[]{0, 1, 1, 0};
            case F0111, F2111 -> new int[]{1, 1, 1, 0};
            case F1000, F1002, F1020, F1022, F1200, F1202, F1220, F1222 -> new int[]{0, 0, 0, 1};
            case F1001, F1021, F1201, F1221 -> new int[]{1, 0, 0, 1};
            case F1010, F1012, F1210, F1212 -> new int[]{0, 1, 0, 1};
            case F1011, F1211 -> new int[]{1, 1, 0, 1};
            case F1100, F1102, F1120, F1122 -> new int[]{0, 0, 1, 1};
            case F1101, F1121 -> new int[]{1, 0, 1, 1};
            case F1110, F1112 -> new int[]{0, 1, 1, 1};
            case F1111 -> new int[]{1, 1, 1, 1};
            case F20121010122000 -> new int[]{0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0};
        };
    }

    private int[] getPresentedFactorCheckers(TestCase face) {
        return switch (face) {
            case F0, F1 -> new int[]{0};
            case F2 -> new int[]{1};
            case F00, F01, F10, F11 -> new int[]{0, 0};
            case F02, F12 -> new int[]{1, 0};
            case F20, F21 -> new int[]{0, 1};
            case F22 -> new int[]{1, 1};
            case F000, F001, F010, F011, F100, F101, F110, F111 -> new int[]{0, 0, 0};
            case F002, F012, F102, F112 -> new int[]{1, 0, 0};
            case F020, F021, F120, F121 -> new int[]{0, 1, 0};
            case F022, F122 -> new int[]{1, 1, 0};
            case F200, F201, F210, F211 -> new int[]{0, 0, 1};
            case F202, F212 -> new int[]{1, 0, 1};
            case F220, F221 -> new int[]{0, 1, 1};
            case F222 -> new int[]{1, 1, 1};
            case F0000, F0001, F0010, F0011, F0100, F0101, F0110, F0111,
                    F1000, F1001, F1010, F1011, F1100, F1101, F1110, F1111 -> new int[]{0, 0, 0, 0};
            case F0002, F0012, F0102, F0112, F1002, F1012, F1102, F1112 -> new int[]{1, 0, 0, 0};
            case F0020, F0021, F0120, F0121, F1020, F1021, F1120, F1121 -> new int[]{0, 1, 0, 0};
            case F0022, F0122, F1022, F1122 -> new int[]{1, 1, 0, 0};
            case F0200, F0201, F0210, F0211, F1200, F1201, F1210, F1211 -> new int[]{0, 0, 1, 0};
            case F0202, F0212, F1202, F1212 -> new int[]{1, 0, 1, 0};
            case F0220, F0221, F1220, F1221 -> new int[]{0, 1, 1, 0};
            case F0222, F1222 -> new int[]{1, 1, 1, 0};
            case F2000, F2001, F2010, F2011, F2100, F2101, F2110, F2111 -> new int[]{0, 0, 0, 1};
            case F2002, F2012, F2102, F2112 -> new int[]{1, 0, 0, 1};
            case F2020, F2021, F2120, F2121 -> new int[]{0, 1, 0, 1};
            case F2022, F2122 -> new int[]{1, 1, 0, 1};
            case F2200, F2201, F2210, F2211 -> new int[]{0, 0, 1, 1};
            case F2202, F2212 -> new int[]{1, 0, 1, 1};
            case F2220, F2221 -> new int[]{0, 1, 1, 1};
            case F2222 -> new int[]{1, 1, 1, 1};
            case F20121010122000 -> new int[]{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1};
        };
    }

    private enum TestCase {
        F0, F1, F2,
        F00, F01, F02, F10, F11, F12, F20, F21, F22,
        F000, F001, F002, F010, F011, F012, F020, F021, F022,
        F100, F101, F102, F110, F111, F112, F120, F121, F122,
        F200, F201, F202, F210, F211, F212, F220, F221, F222,
        F0000, F0001, F0002, F0010, F0011, F0012, F0020, F0021, F0022,
        F0100, F0101, F0102, F0110, F0111, F0112, F0120, F0121, F0122,
        F0200, F0201, F0202, F0210, F0211, F0212, F0220, F0221, F0222,
        F1000, F1001, F1002, F1010, F1011, F1012, F1020, F1021, F1022,
        F1100, F1101, F1102, F1110, F1111, F1112, F1120, F1121, F1122,
        F1200, F1201, F1202, F1210, F1211, F1212, F1220, F1221, F1222,
        F2000, F2001, F2002, F2010, F2011, F2012, F2020, F2021, F2022,
        F2100, F2101, F2102, F2110, F2111, F2112, F2120, F2121, F2122,
        F2200, F2201, F2202, F2210, F2211, F2212, F2220, F2221, F2222,
        F20121010122000;

        private String getFaceName() {
            return toString().substring(1);
        }
    }
}
