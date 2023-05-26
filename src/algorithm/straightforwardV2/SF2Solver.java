package algorithm.straightforwardV2;

import algorithm.ISolver;
import algorithm.straightforwardV2.instances.SF2KFace;

public class SF2Solver implements ISolver {
    private int n;
    private int[] spectrum;

    public SF2Solver(String table) {
        getNewEnter(table);
    }

    public void calculateSpectrum() {
        int faceCount = (int) Math.pow(3, n);
        for (var faceNum = 0; faceNum < faceCount; faceNum++) {
            int[] mu = SF2KFace.getMaxMu(faceNum);

            if (mu[1] > spectrum[mu[0]])
                spectrum[mu[0]] = mu[1];
        }
    }

    public String getResult() {
        var res = new StringBuilder();
        for (var i = 0; i < spectrum.length - 1; i++) {
            res.append(spectrum[i]);
            res.append(", ");
        }
        res.append(spectrum[spectrum.length - 1]);
        return res.toString();
    }

    public void getNewEnter(String table) {
        n = (int) (Math.log(table.length()) / Math.log(2));
        SF2KFace.setCommons(n, table);
        spectrum = new int[n + 1];
    }

    public void setPrintingState(boolean state) {
        SF2KFace.setPrintingState(state);
    }
}
