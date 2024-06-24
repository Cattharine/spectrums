package visualizer.painter.painter.swing;

import visualizer.painter.contents.ElementType;
import visualizer.painter.painter.StrokeType;
import visualizer.painter.painter.IPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class SwingPainter implements IPainter {
    private Graphics2D g2;
    private JLabel spectrumPlace;
    private int fontSizeShift = 0;

    public void prepare(Object graphics, Object spectrumPlace) {
        g2 = (Graphics2D) graphics;
        this.spectrumPlace = (JLabel) spectrumPlace;
    }

    public void drawLine(int[] from, int[] to, ElementType type, StrokeType strokeType) {
        g2.setStroke(SwingDecorConstants.getStroke(strokeType));
        g2.setColor(SwingDecorConstants.getColor(type));
        g2.drawLine(from[0], from[1], to[0], to[1]);
    }

    public void drawDisc(int[] coordinates, int width, int height, ElementType type) {
        g2.setColor(SwingDecorConstants.getColor(type));
        g2.fillOval(coordinates[0] - width / 2, coordinates[1] - height / 2, width, height);
    }

    public void drawString(String str, int[] position, ElementType type) {
        float fontSize = SwingDecorConstants.getFontSize(fontSizeShift);
        TextLayout textLayout = new TextLayout(str, g2.getFont().deriveFont(fontSize), g2.getFontRenderContext());
        Shape shape = textLayout.getOutline(new AffineTransform(
                1.0, 0.0,
                0.0, 1.0,
                position[0], position[1]));
        g2.setColor(SwingDecorConstants.getBackgroundColor(type));
        g2.draw(shape);
        g2.setColor(SwingDecorConstants.getColor(type));
        g2.fill(shape);
    }

    public void fillPolygon(ArrayList<int[]> points, ElementType type) {
        g2.setColor(SwingDecorConstants.getBackgroundColor(type));
        var n = points.size();
        var x = new int[n];
        var y = new int[n];

        for (var i = 0; i < n; i++) {
            x[i] = points.get(i)[0];
            y[i] = points.get(i)[1];
        }

        g2.fillPolygon(x, y, n);
    }

    public void changeFontSize(int value) {
        if (fontSizeShift + value + SwingDecorConstants.baseFontSize >= 0)
            fontSizeShift += value;
    }

    public void showSpectrum(String spectrum) {
        spectrumPlace.setText("Spectrum: " + spectrum);
    }
}
