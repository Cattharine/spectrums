package straightforward;

import straightforward.instances.SFKFace;

public class SFSolver {
    private int n;
    private String table;
    private int[] spectrum;
    private int k = 0;
    private SFKFace[] processing;

    public SFSolver(String table) {
        n = (int) (Math.log(table.length()) / Math.log(2));
        SFKFace.setCommons(n, table);
        this.table = table;
        spectrum = new int[n + 1];
        processing = new SFKFace[n + 1];
        processing[0] = new SFKFace(n, n - 1, -1, new StringBuilder("0".repeat(n)));
    }

    public void calculateSpectrum() {
        spectrum[n] = processing[0].getMaxMu();

        while (processing[0] != null) {
            var face = processing[k];
            var current = face.fixate();
            if (current == null) {
                processing[k] = null;
                k--;
                continue;
            }
            else {
                k++;
                processing[k] = current;
            }
            var mu = current.getMaxMu();
            if (mu > spectrum[n - k])
                spectrum[n - k] = mu;
        }
    }

    @Override
    public String toString() {
        var res = new StringBuilder();
        for (var i = 0; i < spectrum.length - 1; i++) {
            res.append(spectrum[i]);
            res.append(", ");
        }
        res.append(spectrum[spectrum.length - 1]);
        return res.toString();
    }
}
