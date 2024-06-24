package visualizer.painter.painter.swing;

import visualizer.painter.contents.ElementType;
import visualizer.painter.painter.StrokeType;

import java.awt.*;

public class SwingDecorConstants {
    public static final Color baseColor = Color.BLACK;
    public static final Color selectedColor = Color.decode("#b742ff");
    public static final Color activeVertexColor = Color.decode("#62EA02");
    public static final Color maxMuColor = Color.decode("#ff5f3f");
    // transparent light gray
    public static final Color semiBaseColor = new Color(192, 192, 192, 125);
    public static final Color transparent = new Color(0, 0, 0, 0);
    public static final Color baseBackground = Color.white;
    public static final BasicStroke base = new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    public static final BasicStroke bold = new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    public static final BasicStroke superBold = new BasicStroke(7.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    public static final float baseFontSize = 12.0f;

    public static Color getColor(ElementType type) {
        return switch (type) {
            case BASE, FACE -> baseColor;
            case SELECTED -> selectedColor;
            case ACTIVE -> activeVertexColor;
            case MAX_MU -> maxMuColor;
            case HIDDEN -> semiBaseColor;
        };
    }

    public static Color getBackgroundColor(ElementType type) {
        return switch (type) {
            case BASE -> baseBackground;
            case FACE -> semiBaseColor;
            default -> transparent;
        };
    }

    public static BasicStroke getStroke(StrokeType type) {
        return switch (type) {
            case BASE -> base;
            case BOLD -> bold;
            case SUPER -> superBold;
        };
    }

    public static float getFontSize(int shift) {
        return baseFontSize + shift >= 0 ? baseFontSize + shift : 0;
    }
}
