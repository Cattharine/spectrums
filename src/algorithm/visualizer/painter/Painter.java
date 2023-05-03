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
    private ArrayList<CubeContent> cubes = new ArrayList<>();
    private ArrayList<TreeContent> trees = new ArrayList<>();
    private TreeContent activeTC;
    private PainterState state = PainterState.PLAIN;
    private IContent currentContent;

    public Painter() {
        setDoubleBuffered(true);
        setFocusable(true);

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
    }

    private void mousePressed2(MouseEvent e) {
        if (currentContent != null) {
            currentContent.preShift(e.getPoint());
        }
    }

    private void mousePressed1(MouseEvent e) {
        if (currentContent != null) {
            currentContent.preMoveChosen(getWidth(), getHeight(), e.getPoint());
        }
    }

    private void mouseDragged2(MouseEvent e) {
        if (currentContent != null)
            currentContent.shiftAll(e.getPoint());
    }

    private void mouseDragged1(MouseEvent e) {
        if (currentContent != null)
            currentContent.moveChosen(e.getPoint(), getWidth(), getHeight());
    }

    private void mouseWheelMoved0(MouseWheelEvent e) {
        if (currentContent != null) {
            currentContent.changeScale(e.getPoint(), getWidth(), getHeight(), e.getPreciseWheelRotation());
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
        } else {
            currentContent = activeTC;
            state = PainterState.SHOWING_TREE;
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
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        var g2 = (Graphics2D) g;
        if (currentContent != null)
            currentContent.paint(g2, getWidth(), getHeight());
        else {
            g2.drawString("Ничего не выбрано", 20, 20);
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
