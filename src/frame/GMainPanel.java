package frame;

import interfaces.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage canvasImage;
    private List<GShape> shapes = new ArrayList<>();
    private GShape currentShape;
    private DrawingState currentState;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private float strokeWidth = 1.0f;

    public GMainPanel() {
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
    }

    public void createNewCanvas(int width, int height) {
        this.removeAll();
        shapes.clear();

        // new image
        canvasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // set BackGround
        Graphics2D g2d = canvasImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        // mouse listener
        setupMouseListeners();
        // reset
        this.revalidate();
        this.repaint();
    }

    public void clearShapes() {
        shapes.clear();
        repaint();
    }

    public void setCurrentShape(GShape shape) {
        this.currentShape = shape;
    }

    public GShape getCurrentShape() {
        return this.currentShape;
    }

    public void setDrawingState(DrawingState state) {
        this.currentState = state;
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentState != null) {
                    currentState.mousePressed(e);
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentState != null) {
                    currentState.mouseDragged(e);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentState != null) {
                    currentState.mouseReleased(e);
                    if (currentShape != null) {
                        shapes.add(currentShape);
                        currentShape = null;
                    }
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (canvasImage != null) {
            g.drawImage(canvasImage, 0, 0, this);
        }

        Graphics2D g2d = (Graphics2D) g;
        for (GShape shape : shapes) {
            shape.draw(g2d);
        }

        if (currentShape != null) {
            currentShape.draw(g2d);
        }
    }

    public Image getCanvasImage() {
        return this.canvasImage;
    }

    public void initialize() {}

    public GShape findShapeAt(int x, int y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            GShape shape = shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
}