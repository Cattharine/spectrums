package algorithm;

public interface ISolver {
    void calculateSpectrum();

    String getResult();

    void getNewEnter(String table);

    void setPrintingState(boolean state);
}
