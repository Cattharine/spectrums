package tests;

import algorithm.instances.KFace;
import algorithm.instances.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class KFaceTest {
    @Test
    public void checkMaxMu() {
        Vertex.setupBinToDist(8);

        var vertexes = new ArrayList<Vertex>();
        vertexes.add(new Vertex(0, 3));
        vertexes.add(new Vertex(2, 3));
        vertexes.add(new Vertex(4, 3));
        vertexes.add(new Vertex(6, 3));
        vertexes.add(new Vertex(3, 3));
        var kFace = new KFace(3, vertexes, -1, 3);
        Assert.assertEquals(0, kFace.getMaxMu());
        vertexes.remove(new Vertex(2, 3));
        kFace = new KFace(3, vertexes, -1, 3);
        Assert.assertEquals(1, kFace.getMaxMu());
    }
}
