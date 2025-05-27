package algorithms;

public interface ISolver {
    void calculateSpectrum();

    String getResult();

    void getNewEntry(String table);

    static int calculateN(String table) {
        return (int) (Math.log(table.length()) / Math.log(2));
    }
}
