package visualizer.painter;

import visualizer.contents.utils.Coordinates;
import visualizer.contents.utils.elementInfo.ElementInfo;

import java.util.ArrayList;

public interface IPainter {
    void prepare(Object spectrumPlace, int width, int height);

    void drawLine(int[] from, int[] to, ElementInfo info);

    void drawDisc(int[] centre, int width, int height, ElementInfo info);

    void drawString(String str, int[] position, ElementInfo info);

    void fillPolygon(ArrayList<int[]> points, ElementInfo info);

    void changeFontSize(int value);

    void showSpectrum(String spectrum);

    void offset(Coordinates value);

    void paint(Object panel);
}
