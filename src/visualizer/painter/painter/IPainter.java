package visualizer.painter.painter;

import visualizer.painter.contents.ElementType;

import java.util.ArrayList;

public interface IPainter {
    void prepare(Object graphics, Object spectrumPlace);

    void drawLine(int[] from, int[] to, ElementType type, StrokeType strokeType);

    void drawDisc(int[] coordinates, int width, int height, ElementType type);

    void drawString(String str, int[] position, ElementType type);

    void fillPolygon(ArrayList<int[]> points, ElementType type);

    void changeFontSize(int value);

    void showSpectrum(String spectrum);
}
