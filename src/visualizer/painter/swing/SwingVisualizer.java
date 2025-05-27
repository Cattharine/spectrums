package visualizer.painter.swing;

import visualizer.contents.Dispatcher;
import visualizer.contents.utils.availabilityUpdater.AvailabilityUpdaterType;
import visualizer.painter.IPainted;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class SwingVisualizer extends JPanel {
    private final Dispatcher dispatcher;
    private final JMenuBar menuBar;
    private final JLabel spectrumPlace;
    private final SwingPainted painted;

    public SwingVisualizer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        painted = new SwingPainted();

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

        var buttonPrev = createButton("prev");
        buttonPanel.add(buttonPrev);
        buttonPrev.addActionListener(e -> prevPressed());

        var buttonPrevFace = createButton("prev_face");
        buttonPanel.add(buttonPrevFace);
        buttonPrevFace.addActionListener(e -> prevFacePressed());
        dispatcher.addUpdater(new SwingAvailabilityUpdater(buttonPrevFace, AvailabilityUpdaterType.CHANGE_FACE));

        var buttonNextFace = createButton("next_face");
        buttonPanel.add(buttonNextFace);
        buttonNextFace.addActionListener(e -> nextFacePressed());
        dispatcher.addUpdater(new SwingAvailabilityUpdater(buttonNextFace, AvailabilityUpdaterType.CHANGE_FACE));

        var buttonNext = createButton("next");
        buttonPanel.add(buttonNext);
        buttonNext.addActionListener(e -> nextPressed());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                var mask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK;
                switch (e.getModifiersEx() & mask) {
                    case MouseEvent.BUTTON1_DOWN_MASK -> pressedLeftMouseButton(e);
                    case MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK -> pressedLeftMouseButtonWithShift(e);
                }
                e.consume();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                var mask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK;
                switch (e.getModifiersEx() & mask) {
                    case MouseEvent.BUTTON1_DOWN_MASK -> draggedLeftMouseButton(e);
                    case MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK -> draggedLeftMouseButtonWithShift(e);
                }
                e.consume();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                movedMouseWheel(e);
                e.consume();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                var shiftPressed = (e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_S -> switchState();
                    case KeyEvent.VK_R -> switchRepresentation();
                    case KeyEvent.VK_ENTER -> goToSelected();
                    case KeyEvent.VK_ESCAPE -> deselectAll();
                    case KeyEvent.VK_RIGHT -> nextPressed(shiftPressed);
                    case KeyEvent.VK_LEFT -> prevPressed(shiftPressed);
                    case KeyEvent.VK_EQUALS, KeyEvent.VK_ADD -> increaseFontSize();
                    case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> decreaseFontSize();
                }
                e.consume();
            }
        });

        dispatcher.updateAvailability();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private JButton createButton(String name) {
        var button = new JButton();
        var iconFileName = String.format("src/visualizer/painter/icons/%s.png", name);
        button.setIcon(new ImageIcon(iconFileName));
        var activeIconFileName = String.format("src/visualizer/painter/icons/%s_pressed_rollover.png", name);
        button.setRolloverIcon(new ImageIcon(activeIconFileName));
        button.setPressedIcon(new ImageIcon(activeIconFileName));
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        return button;
    }

    private void switchState() {
        dispatcher.switchState();
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void switchRepresentation() {
        dispatcher.switchRepresentation();
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void deselectAll() {
        dispatcher.deselectAll();
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void goToSelected() {
        dispatcher.goToSelected();
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void nextPressed(boolean shiftPressed) {
        if (shiftPressed)
            nextPressed();
        else nextFacePressed();
    }

    private void prevPressed(boolean shiftPressed) {
        if (shiftPressed)
            prevPressed();
        else prevFacePressed();
    }

    private void nextPressed() {
        dispatcher.changeActiveContent(1);
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void prevPressed() {
        dispatcher.changeActiveContent(-1);
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void nextFacePressed() {
        dispatcher.changeFace(1);
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void prevFacePressed() {
        dispatcher.changeFace(-1);
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void updateAvailability() {
        dispatcher.updateAvailability();
    }

    private void increaseFontSize() {
        dispatcher.changeFontSize(1);
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void decreaseFontSize() {
        dispatcher.changeFontSize(-1);
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
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
        dispatcher.addUpdater(new SwingAvailabilityUpdater(selection, AvailabilityUpdaterType.GO_TO_SELECTED));
    }

    private void addDeselectAll(JMenu menu) {
        JMenuItem deselection = new JMenuItem("Deselect all");
        menu.add(deselection);
        deselection.addActionListener(e -> deselectAll());
        dispatcher.addUpdater(new SwingAvailabilityUpdater(deselection, AvailabilityUpdaterType.DESELECT));
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

    private void pressedLeftMouseButtonWithShift(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.memorizeCursorPosition(position);
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void pressedLeftMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.select(position);
        updateAvailability();
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void draggedLeftMouseButtonWithShift(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.shiftAll(position);
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void draggedLeftMouseButton(MouseEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.moveSelected(position);
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    private void movedMouseWheel(MouseWheelEvent e) {
        int[] position = new int[2];
        position[0] = e.getPoint().x - getWidth() / 2;
        position[1] = e.getPoint().y - getHeight() / 2;
        dispatcher.changeScale(position, e.getPreciseWheelRotation());
        dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics panel) {
        if (!painted.dispatcherCheck)
            dispatcher.paint(painted, spectrumPlace, getWidth(), getHeight());
        else {
            panel.drawImage(painted.canvas, 0, 0, getWidth(), getHeight(), null);
            painted.dispatcherCheck = false;
        }
    }

    public class SwingPainted implements IPainted {
        private BufferedImage canvas;
        private boolean dispatcherCheck = false;

        private SwingPainted() {
        }

        public void paint(Object image) {
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            canvas.getGraphics().drawImage((Image) image, 0, 0, getWidth(), getHeight(), null);
        }

        public void finish() {
            dispatcherCheck = true;
            SwingVisualizer.this.invalidate();
            SwingVisualizer.this.repaint();
        }
    }
}
