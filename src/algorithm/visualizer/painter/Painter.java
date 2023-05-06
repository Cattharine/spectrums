package algorithm.visualizer.painter;

import algorithm.visualizer.painter.contents.CubeContent;
import algorithm.visualizer.painter.contents.IContent;
import algorithm.visualizer.painter.contents.TreeContent;
import algorithm.visualizer.solver.Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Painter extends JPanel {
    private CubeContent activeCC;
    private int number = -1;
    private final ArrayList<CubeContent> cubes = new ArrayList<>();
    private final ArrayList<TreeContent> trees = new ArrayList<>();
    private TreeContent activeTC;
    private PainterState state = PainterState.PLAIN;
    private IContent currentContent;
    private final Button buttonTree;
    private final Button buttonShowPathOrEsc;
    private final JPanel southButtonPanel;
    private final Button buttonGoToFace;

    public Painter() {
        setDoubleBuffered(true);
        setFocusable(true);
        setLayout(new BorderLayout());

        southButtonPanel = new JPanel();
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        southButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        add(southButtonPanel, BorderLayout.SOUTH);
        southButtonPanel.setFocusable(false);

        buttonGoToFace = new Button("Go to chosen face");
        southButtonPanel.add(buttonGoToFace);
        buttonGoToFace.setFocusable(false);
        if (state != PainterState.SHOWING_TREE) {
            buttonGoToFace.setEnabled(false);
            buttonGoToFace.setVisible(false);
        }

        var title = state == PainterState.SHOWING_TREE? "Show cube" : "Show tree";
        buttonTree = new Button(title);
        southButtonPanel.add(buttonTree);
        buttonTree.setFocusable(false);

        title = state == PainterState.SHOWING_TREE? "Unchoose all" : "Show path";
        buttonShowPathOrEsc = new Button(title);
        southButtonPanel.add(buttonShowPathOrEsc);
        buttonShowPathOrEsc.setFocusable(false);

        var buttonPrev = new Button("Prev");
        southButtonPanel.add(buttonPrev);
        buttonPrev.setFocusable(false);

        var buttonNext = new Button("Next");
        southButtonPanel.add(buttonNext);
        buttonNext.setFocusable(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON2 -> mousePressed2(e);
                    case MouseEvent.BUTTON1 -> mousePressed1(e);
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
                        case MouseEvent.BUTTON2_DOWN_MASK -> mouseDragged2(e);
                        case MouseEvent.BUTTON1_DOWN_MASK -> mouseDragged1(e);
                    }
                e.consume();
                invalidate();
                repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                mouseWheelMoved0(e);
                e.consume();
                invalidate();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_S -> keyPressedS();
                    case KeyEvent.VK_T -> keyPressedT();
                    case KeyEvent.VK_RIGHT -> {
                        if (activeCC != null)
                            keyPressedDir(activeCC.nextPath(), (number + 1) % cubes.size());
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (activeCC != null)
                            keyPressedDir(activeCC.prevPath(), (cubes.size() + number - 1) % cubes.size());
                    }
                    case KeyEvent.VK_ESCAPE -> keyPressedEscape();
                    case KeyEvent.VK_ENTER -> keyPressedEnter();
                }
                e.consume();
                invalidate();
                repaint();
            }
        });

        buttonNext.addActionListener(e -> {
            if (activeCC != null)
                keyPressedDir(activeCC.nextPath(), (number + 1) % cubes.size());
            invalidate();
            repaint();
        });

        buttonPrev.addActionListener(e -> {
            if (activeCC != null)
                keyPressedDir(activeCC.prevPath(), (cubes.size() + number - 1) % cubes.size());
            invalidate();
            repaint();
        });

        buttonTree.addActionListener(e -> {
            keyPressedT();
            invalidate();
            repaint();
        });

        buttonShowPathOrEsc.addActionListener(e -> {
            if (state == PainterState.SHOWING_TREE)
                keyPressedEscape();
            else keyPressedS();
            invalidate();
            repaint();
        });

        buttonGoToFace.addActionListener(e -> {
            keyPressedEnter();
            invalidate();
            repaint();
        });
    }

    private void mousePressed2(MouseEvent e) {
        if (currentContent != null) {
            currentContent.preShift(e.getPoint());
        }
    }

    private void mousePressed1(MouseEvent e) {
        if (currentContent != null) {
            currentContent.preMoveChosen(e.getPoint());
        }
    }

    private void mouseDragged2(MouseEvent e) {
        if (currentContent != null)
            currentContent.shiftAll(e.getPoint());
    }

    private void mouseDragged1(MouseEvent e) {
        if (currentContent != null)
            currentContent.moveChosen(e.getPoint());
    }

    private void mouseWheelMoved0(MouseWheelEvent e) {
        if (currentContent != null) {
            currentContent.changeScale(e.getPoint(), e.getPreciseWheelRotation());
        }
    }

    private void keyPressedS() {
        switch (state) {
            case PLAIN -> {
                currentContent = activeCC;
                activeCC.setShowPaths(true);
                state = PainterState.SHOWING_PATH;
            }
            case SHOWING_PATH -> {
                currentContent = activeCC;
                activeCC.setShowPaths(false);
                state = PainterState.PLAIN;
            }
        }
    }

    private void keyPressedT() {
        if (state == PainterState.SHOWING_TREE) {
            currentContent = activeCC;
            state = PainterState.PLAIN;
            setButtonStateSC();
        } else {
            currentContent = activeTC;
            state = PainterState.SHOWING_TREE;
            buttonTree.setLabel("Show cube");
            buttonShowPathOrEsc.setLabel("Unchoose all");
            buttonShowPathOrEsc.setSize(buttonShowPathOrEsc.getPreferredSize());
            buttonGoToFace.setEnabled(true);
            buttonGoToFace.setVisible(true);
            southButtonPanel.revalidate();
        }
        activeCC.setShowPaths(false);
    }

    private void keyPressedDir(int pathNum, int contentNumber) {
        if (state == PainterState.SHOWING_PATH)
            activeCC.setPath(pathNum);
        else {
            number = contentNumber;
            activeCC = cubes.get(number);
            activeTC = trees.get(number);
            if (state == PainterState.PLAIN) {
                currentContent = activeCC;
            }
            else currentContent = activeTC;
        }
    }

    private void keyPressedEscape() {
        if (state == PainterState.SHOWING_TREE) {
            if (activeTC != null) {
                activeTC.unchooseAll();
            }
        }
    }

    private void keyPressedEnter() {
        if (state == PainterState.SHOWING_TREE) {
            if (activeTC != null) {
                var chosen = activeTC.getChosen();
                if (chosen > -1) {
                    state = PainterState.SHOWING_PATH;
                    activeCC.setShowPaths(true);
                    activeCC.setPath(chosen);
                    currentContent = activeCC;
                    setButtonStateSC();
                }
            }
        }
    }

    private void setButtonStateSC() {
        buttonTree.setLabel("Show tree");
        buttonShowPathOrEsc.setLabel("Show path");
        buttonShowPathOrEsc.setSize(buttonShowPathOrEsc.getPreferredSize());
        buttonGoToFace.setEnabled(false);
        buttonGoToFace.setVisible(false);
        southButtonPanel.revalidate();
    }

    @Override
    public void paint(Graphics g) {
        var g2 = (Graphics2D) g;
        paintAdditionalButton();
        if (currentContent != null)
            currentContent.paint(g2, getWidth(), getHeight());
        else {
            g2.drawString("Ничего не выбрано", 20, 20);
        }
    }

    private void paintAdditionalButton() {
        if (state == PainterState.SHOWING_TREE) {
            if (activeTC != null) {
                var chosen = activeTC.getChosen();
                buttonGoToFace.setEnabled(chosen > -1);
            }
        }
    }

    public int addContents(int fullD) {
        cubes.add(new CubeContent(fullD));
        trees.add(new TreeContent(fullD));
        setUpContent();
        return cubes.size() - 1;
    }

    private void setUpContent() {
        if (number == -1) {
            number = 0;
        }
        if ((activeCC == null || activeTC == null) && cubes.size() > 0 && trees.size() > 0) {
            activeCC = cubes.get(number);
            activeTC = trees.get(number);
            if (state == PainterState.SHOWING_TREE) {
                currentContent = activeTC;
            }
            else currentContent = activeCC;
        }
    }

    public void setSolver(int num, Solver solver) {
        cubes.get(num).setActiveVertexes(solver.getVertexes());
        cubes.get(num).setPaths(solver.getPaths());
        solver.setTree(trees.get(num));
        solver.setNum(num);
    }
}
