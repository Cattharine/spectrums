package visualizer.painter;

import tests.visualizer.painter.TestPainter;
import visualizer.painter.swing.SwingPainter;

public enum PainterType {
    SWING,
    TEST;

    public static IPainter getPainter(PainterType type) {
        return switch (type) {
            case SWING -> new SwingPainter();
            case TEST -> new TestPainter(null);
        };
    }
}
