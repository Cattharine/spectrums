package visualizer.painter.swing;

import visualizer.contents.utils.elementInfo.ElementState;
import visualizer.contents.utils.elementInfo.ElementType;

import java.awt.*;

public class SwingDecorConstants {
    private static final Color baseColor = new Color(0, 0, 0);
    private static final Color selectedColor = new Color(255, 204, 0, 240);
    private static final Color semiSelectedColor = new Color(102, 204, 0, 150);
    private static final Color activeColor = new Color(102, 204, 0);
    private static final Color maxColor = new Color(255, 0, 51);
    private static final Color semiMaxColor = new Color(255, 0, 51, 150);
    // transparent light gray
    private static final Color semiBaseColor = new Color(192, 192, 192, 125);
    private static final Color transparent = new Color(0, 0, 0, 0);
    private static final Color baseBackground = new Color(255, 255, 255);
    private static final BasicStroke base = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    private static final BasicStroke semiBold = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    private static final BasicStroke bold = new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    private static final BasicStroke currentBold = new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    private static final BasicStroke superBold = new BasicStroke(7.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f);
    private static final float baseFontSize = 12.0f;

    public static Color getColor(ElementType type, ElementState state) {
        return switch (state) {
            case BASE -> switch (type) {
                case BACKGROUND -> baseBackground;
                case FACE -> semiBaseColor;
                default -> baseColor;
            };
            case SELECTED -> selectedColor;
            case SEMI_SELECTED -> semiSelectedColor;
            case ACTIVE -> activeColor;
            case MAX -> maxColor;
            case SEMI_MAX -> semiMaxColor;
            case HIDDEN -> switch (type) {
                case VERTEX, CURRENT_VERTEX -> semiBaseColor;
                default -> transparent;
            };
        };
    }

    public static BasicStroke getStroke(ElementType type, ElementState state) {
        return switch (state) {
            case MAX, SEMI_MAX -> superBold;
            case BASE -> switch (type) {
                case CURRENT_VERTEX -> currentBold;
                case VERTEX -> bold;
                case CURRENT_EDGE -> semiBold;
                default -> base;
            };
            default -> base;
        };
    }

    public static int getLayer(ElementType type) {
        return switch (type) {
            case EDGE, VERTEX, FACE, BACKGROUND -> 0;
            case PATH -> 1;
            case CURRENT_EDGE, CURRENT_VERTEX -> 2;
        };
    }

    public static int getNumberOfLayers() {
        return 3;
    }

    public static float getFontSize(int shift) {
        return baseFontSize + shift >= 0 ? baseFontSize + shift : 0;
    }

    public static boolean canChangeFontSize(int shift, int addition) {
        return shift + addition + baseFontSize >= 0;
    }
}
