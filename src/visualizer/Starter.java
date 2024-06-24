package visualizer;

import visualizer.extendedAlgorithms.treeSolver.TreeSolverExt;
import visualizer.painter.Dispatcher;
import visualizer.painter.painter.swing.SwingVisualizer;
import visualizer.painter.painter.swing.SwingPainter;

import javax.swing.*;
import java.awt.*;

public class Starter extends JFrame {
    public Starter(Dispatcher dispatcher) {
        super("Boolean Cube Visualizer");
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        SwingVisualizer visualizer = new SwingVisualizer(dispatcher, new SwingPainter());
        setJMenuBar(visualizer.getMenuBar());
        add(visualizer);
    }

    public static void main(String[] args) {
        var dispatcher = new Dispatcher();
        var solver = new TreeSolverExt("00");
        var sm = new DispatchedSolutionsManager("./src/in.txt", dispatcher);
        var frame = new Starter(dispatcher);

        frame.setVisible(true);
        sm.solveAll(solver);
        sm.writeSolutions();
        sm.closeInputFile();
    }
}
