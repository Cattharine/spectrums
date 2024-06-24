package algorithms.straightforwardV2;

import algorithms.ISolver;
import algorithms.Solver;
import algorithms.straightforwardV2.instances.SF2KFace;
import algorithms.straightforwardV2.instances.SF2Vertex;

public class SF2Solver extends Solver implements ISolver {
    public SF2Solver(String table) {
        getNewEntry(table);
    }

    public void calculateSpectrum() {
        int faceCount = (int) Math.pow(3, n);
        for (var faceNum = 0; faceNum < faceCount; faceNum++) {
            int[] mu = SF2KFace.getMaxMu(faceNum);

            if (mu[1] > spectrum[mu[0]])
                spectrum[mu[0]] = mu[1];
        }
    }

    public void getNewEntry(String table) {
        super.getNewEntry(table);
        SF2Vertex.setupBinToDist(table.length());
        SF2KFace.setCommons(n, table);
        spectrum = new int[n + 1];
    }

    public void setPrintingState(boolean state) {
        SF2KFace.setPrintingState(state);
    }
}
