package algorithms;

public interface ISolver {
    void calculateSpectrum();

    String getResult();

    void getNewEntry(String table);

    void setPrintingState(boolean state);
}
