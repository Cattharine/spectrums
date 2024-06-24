package tests;

import algorithms.treeSolver.instances.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class VertexTest {
    @Test
    public void checkCalculatingMinMu() {
        Vertex.setupBinToDist(8);

        var v1 = new Vertex(2, 3);
        var v2 = new Vertex(6, 3);
        var v3 = new Vertex(1, 3);
        var vertexes1 = new ArrayList<Vertex>();
        vertexes1.add(v2);
        for (var i = 3; i > -1; i--) {
            Assert.assertEquals(0, v1.getMinMu(vertexes1, i));
        }
        //two vertexes
        vertexes1.add(v3);
        for (var i = 3; i > -1; i--) {
            Assert.assertEquals(0, v1.getMinMu(vertexes1, i));
        }
        //not always 0
        vertexes1.remove(v2);
        for (var i = 3; i > 0; i--) {
            Assert.assertEquals(1, v1.getMinMu(vertexes1, i));
        }
        Assert.assertEquals(0 ,v1.getMinMu(new ArrayList<>() {{add(v3);}}, 0));
        vertexes1.add(v2);
        for (var i = 3; i > 1; i--) {
            Assert.assertEquals(2, v2.getMinMu(vertexes1, i));
        }
        Assert.assertEquals(1 ,v2.getMinMu(new ArrayList<>() {{add(v3);}}, 1));
        Assert.assertEquals(0 ,v2.getMinMu(new ArrayList<>() {{add(v3);}}, 0));
        //has symmetry
        vertexes1.add(v1);
        for (var i = 3; i > -1; i--) {
            Assert.assertEquals(0, v2.getMinMu(vertexes1, i));
        }
    }
}
