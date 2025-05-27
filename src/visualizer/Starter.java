package visualizer;

import visualizer.contents.Dispatcher;
import visualizer.painter.PainterType;
import visualizer.painter.swing.SwingVisualizer;

import javax.swing.*;
import java.awt.*;

public class Starter extends JFrame {
    public Starter(Dispatcher dispatcher) {
        super("Boolean Cube Visualizer");
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        SwingVisualizer visualizer = new SwingVisualizer(dispatcher);
        setJMenuBar(visualizer.getMenuBar());
        add(visualizer);
    }

    public static void main(String[] args) {
        var dispatcher = new Dispatcher(PainterType.SWING);
        var frame = new Starter(dispatcher);

        frame.setVisible(true);
        dispatcher.acceptAll("./src/in.txt");
        frame.invalidate();
        frame.repaint();
    }
}
