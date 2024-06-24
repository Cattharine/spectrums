package visualizer.painter.painter.swing;

import visualizer.painter.Dispatcher;
import visualizer.painter.painter.IPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingVisualizer extends JPanel {
    private final Dispatcher dispatcher;
    private final JMenuBar menuBar;
    private final IPainter painter;
    private final JLabel spectrumPlace;

    public SwingVisualizer(Dispatcher dispatcher, IPainter painter) {
        this.dispatcher = dispatcher;

        setDoubleBuffered(true);
        setFocusable(true);

        menuBar = new JMenuBar();
        menuBar.add(createContentMenu());
        menuBar.setBackground(new Color(240, 240, 240));
        updateAvailability();

        spectrumPlace = new JLabel();
        menuBar.add(spectrumPlace);

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        menuBar.add(buttonPanel);

        var buttonPrev = new JButton();
        buttonPanel.add(buttonPrev);

        buttonPrev.setIcon(new ImageIcon("src/visualizer/painter/icons/prev.png"));
        buttonPrev.setRolloverIcon(new ImageIcon("src/visualizer/painter/icons/prev_pressed_rollover.png"));
        buttonPrev.setPressedIcon(new ImageIcon("src/visualizer/painter/icons/prev_pressed_rollover.png"));
        buttonPrev.setFocusable(false);
        buttonPrev.setContentAreaFilled(false);
        buttonPrev.setBorderPainted(false);

        var buttonNext = new JButton();
        buttonPanel.add(buttonNext);
        buttonNext.setIcon(new ImageIcon("src/visualizer/painter/icons/next.png"));
        buttonNext.setRolloverIcon(new ImageIcon("src/visualizer/painter/icons/next_pressed_rollover.png"));
        buttonNext.setPressedIcon(new ImageIcon("src/visualizer/painter/icons/next_pressed_rollover.png"));
        buttonNext.setFocusable(false);
        buttonNext.setContentAreaFilled(false);
        buttonNext.setBorderPainted(false);

        buttonNext.addActionListener(e -> nextPressed());

        buttonPrev.addActionListener(e -> prevPressed());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON2 -> pressedMiddleMouseButton(e);
                    case MouseEvent.BUTTON1 -> pressedLeftMouseButton(e);
                }
                e.consume();
                invalidate();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (e.getModifiersEx()) {
                    case MouseEvent.BUTTON2_DOWN_MASK -> draggedMiddleMouseButton(e);
                    case MouseEvent.BUTTON1_DOWN_MASK -> draggedLeftMouseButton(e);
                }
                e.consume();
                invalidate();
                repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                movedMouseWheel(e);
                e.consume();
                invalidate();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_S -> switchState();
                    case KeyEvent.VK_R -> switchRepresentation();
                    case KeyEvent.VK_ENTER -> goToSelected();
                    case KeyEvent.VK_ESCAPE -> deselectAll();
                    case KeyEvent.VK_RIGHT -> nextPressed();
                    case KeyEvent.VK_LEFT -> prevPressed();
                    case KeyEvent.VK_EQUALS, KeyEvent.VK_ADD -> increaseFontSize();
                    case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> decreaseFontSize();
                }
                e.consume();
                invalidate();
                repaint();
            }
        });

        this.painter = painter;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void switchState() {
        dispatcher.switchState();
        updateAvailability();
        invalidate();
        repaint();
    }

    private void switchRepresentation() {
        dispatcher.switchRepresentation();
        updateAvailability();
        invalidate();
        repaint();
    }

    private void deselectAll() {
        dispatcher.deselectAll();
        updateAvailability();
        invalidate();
        repaint();
    }

    private void goToSelected() {
        dispatcher.goToSelected();
        updateAvailability();
        invalidate();
        repaint();
    }

    private void nextPressed() {
        dispatcher.changeActiveContent(1);
        updateAvailability();
        invalidate();
        repaint();
    }

    private void prevPressed() {
        dispatcher.changeActiveContent(-1);
        updateAvailability();
        invalidate();
        repaint();
    }

    private void updateAvailability() {
        var selection = menuBar.getMenu(0).getItem(4);
        selection.setEnabled(dispatcher.canGoToSelected());
        var deselection = menuBar.getMenu(0).getItem(5);
        deselection.setEnabled(dispatcher.canDeselectAll());
    }

    private void increaseFontSize() {
        painter.changeFontSize(1);
        invalidate();
        repaint();
    }

    private void decreaseFontSize() {
        painter.changeFontSize(-1);
        invalidate();
        repaint();
    }

    private JMenu createContentMenu() {
        JMenu contentMenu = new JMenu("Actions");

        addChangeModItem(contentMenu);
        contentMenu.addSeparator();
        addChangeRepresentationItem(contentMenu);
        contentMenu.addSeparator();
        addGoToSelectedItem(contentMenu);
        addDeselectAll(contentMenu);
        contentMenu.addSeparator();
        addIncreaseFontSize(contentMenu);
        addDecreaseFontSize(contentMenu);

        return contentMenu;
    }

    private void addChangeModItem(JMenu menu) {
        JMenuItem mode = new JMenuItem("Change mode");
        menu.add(mode);
        mode.addActionListener(e -> switchState());
    }

    private void addChangeRepresentationItem(JMenu menu) {
        JMenuItem representation = new JMenuItem("Switch representation");
        menu.add(representation);
        representation.addActionListener(e -> switchRepresentation());
    }

    private void addGoToSelectedItem(JMenu menu) {
        JMenuItem selection = new JMenuItem("Go to selected");
        menu.add(selection);
        selection.addActionListener(e -> goToSelected());
    }

    private void addDeselectAll(JMenu menu) {
        JMenuItem deselection = new JMenuItem("Deselect all");
        menu.add(deselection);
        deselection.addActionListener(e -> deselectAll());
    }

    private void addIncreaseFontSize(JMenu menu) {
        JMenuItem increasing = new JMenuItem("Increase font size");
        menu.add(increasing);
        increasing.addActionListener(e -> increaseFontSize());
    }

    private void addDecreaseFontSize(JMenu menu) {
        JMenuItem decreasing = new JMenuItem("Decrease font size");
        menu.add(decreasing);
        decreasing.addActionListener(e -> decreaseFontSize());
    }

    private void pressedMiddleMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.memorizeCursorPosition(position);
    }

    private void pressedLeftMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.select(position);
        updateAvailability();
    }

    private void draggedMiddleMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.shiftAll(position);
    }

    private void draggedLeftMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.moveSelected(position);
    }

    private void movedMouseWheel(MouseWheelEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.changeScale(position, e.getPreciseWheelRotation());
    }

    @Override
    public void paint(Graphics g) {
        var g2 = (Graphics2D) g;
        g2.setColor(SwingDecorConstants.baseBackground);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(SwingDecorConstants.baseColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(SwingDecorConstants.base);
        g2.translate(getWidth() / 2, getHeight() / 2);
        painter.prepare(g2, spectrumPlace);
        if (!dispatcher.paint(painter))
            g2.drawString("Ничего не выбрано", 20 - getWidth() / 2, 20 - getHeight() / 2);
    }
}
