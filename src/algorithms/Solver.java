package algorithms;

public abstract class Solver implements ISolver {
    protected int n;
    protected int[] spectrum;

    @Override
    public abstract void calculateSpectrum();

    @Override
    public String getResult() {
        var res = new StringBuilder();
        for (var i = 0; i < spectrum.length - 1; i++) {
            res.append(spectrum[i]);
            res.append(", ");
        }
        res.append(spectrum[spectrum.length - 1]);
        return res.toString();
    }

    @Override
    public void getNewEntry(String table) {
        n = (int) (Math.log(table.length()) / Math.log(2));
    }

    @Override
    public abstract void setPrintingState(boolean state);
}
