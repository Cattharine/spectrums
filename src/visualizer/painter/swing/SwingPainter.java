package visualizer.painter.swing;

import visualizer.contents.utils.Coordinates;
import visualizer.contents.utils.elementInfo.ElementInfo;
import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;
import visualizer.painter.IPainted;
import visualizer.painter.IPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SwingPainter implements IPainter {
    private BufferedImage[] images = new BufferedImage[0];
    private volatile BufferedImage assembled;
    private Graphics2D[] graphics2 = new Graphics2D[0];
    private JLabel spectrumPlace;
    private int fontSizeShift = 0;
    private int width = 1;
    private int height = 1;

    public SwingPainter() {
        assembled = createBufferedImage(1, 1);
    }

    public BufferedImage createBufferedImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void prepare(Object spectrumPlace, int width, int height) {
        this.width = width == 0 ? 1 : width;
        this.height = height == 0 ? 1 : height;
        this.spectrumPlace = (JLabel) spectrumPlace;

        reset();
    }

    public void reset() {
        images = new BufferedImage[SwingDecorConstants.getNumberOfLayers()];
        assembled = createBufferedImage(width, height);
        graphics2 = new Graphics2D[images.length];

        for (int layer = 0; layer < images.length; layer++) {
            images[layer] = createBufferedImage(width, height);
            graphics2[layer] = (Graphics2D) images[layer].getGraphics();
            prepareLayer(graphics2[layer], layer);
        }
    }

    private void prepareLayer(Graphics2D g2, int layer) {
        var state = layer == 0 ? ElementState.BASE : ElementState.HIDDEN;
        setColor(g2, ElementType.BACKGROUND, state);
        g2.fillRect(0, 0, width, height);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setStroke(g2, ElementType.EDGE, ElementState.BASE);

        g2.translate(width / 2, height / 2);
    }

    public void drawLine(int[] from, int[] to, ElementInfo info) {
        var g2 = getG2(info);
        setStroke(g2, info);
        setColor(g2, info);
        g2.drawLine(from[0], from[1], to[0], to[1]);
    }

    public void drawDisc(int[] centre, int width, int height, ElementInfo info) {
        var g2 = getG2(info);
        setColor(g2, info);
        g2.fillOval(centre[0] - width / 2, centre[1] - height / 2, width, height);
    }

    public void drawString(String str, int[] position, ElementInfo info) {
        var g2 = getG2(info);
        setStroke(g2, info);
        var shape = getTextShape(g2, str, position);

        setColor(g2, ElementType.BACKGROUND, info.state);
        g2.draw(shape);

        setColor(g2, info);
        g2.fill(shape);
    }

    public void fillPolygon(ArrayList<int[]> points, ElementInfo info) {
        var g2 = getG2(info);
        setColor(g2, info);
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
        if (SwingDecorConstants.canChangeFontSize(fontSizeShift, value))
            fontSizeShift += value;
    }

    public void showSpectrum(String spectrum) {
        if (spectrumPlace != null) {
            SwingUtilities.invokeLater(() -> spectrumPlace.setText("Spectrum: " + spectrum));
        }
    }

    public void offset(Coordinates value) {
        for (var g2 : graphics2) {
            g2.translate(value.getX(), value.getY());
        }
    }

    public void paint(Object panel) {
        assemble();
        SwingUtilities.invokeLater(() -> {
            var canvas = (IPainted) panel;
            paint(canvas);
        });
    }

    private void assemble() {
        for (var image : images) {
            assembled.getGraphics().drawImage(image, 0, 0, width, height, null);
        }
    }

    private void paint(IPainted canvas) {
        canvas.paint(assembled);
        canvas.finish();
    }

    private Graphics2D getG2(ElementInfo info) {
        return graphics2[SwingDecorConstants.getLayer(info.type)];
    }

    private void setColor(Graphics2D g2, ElementInfo info) {
        setColor(g2, info.type, info.state);
    }

    private void setColor(Graphics2D g2, ElementType type, ElementState state) {
        g2.setColor(SwingDecorConstants.getColor(type, state));
    }

    private void setStroke(Graphics2D g2, ElementInfo info) {
        setStroke(g2, info.type, info.state);
    }

    private void setStroke(Graphics2D g2, ElementType type, ElementState state) {
        g2.setStroke(SwingDecorConstants.getStroke(type, state));
    }

    private Shape getTextShape(Graphics2D g2, String text, int[] position) {
        float fontSize = SwingDecorConstants.getFontSize(fontSizeShift);
        TextLayout textLayout = new TextLayout(text, g2.getFont().deriveFont(fontSize), g2.getFontRenderContext());

        var transform = new AffineTransform();
        transform.translate(position[0], position[1]);

        return textLayout.getOutline(transform);
    }
}
