package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JPanel;

import layers.shapes.GAnchors.EAnchors;
import layers.shapes.GRectangle;
import layers.shapes.GShape;
import layers.shapes.GShape.EPoints;
import layers.GShapeToolBar.EShapeTool;
import transformers.*;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum EDrawingState{
        eidle,
        e2P,
        enP,
        eSelect,
        eResize, eRotate, eMove
    }
    private Vector<GShape> selectedShapes = new Vector<GShape>();
    private Color canvasBackground = Color.WHITE;
    private Vector<GShape> shapes;
    private GShape currentShape;
    private GShape selectedShape;
    private EShapeTool eShapeTool;
    private EDrawingState eDrawingState;
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Rectangle canvasBounds;
    private double zoomLevel = 1.0;
    private Rectangle canvasBound;
    private BufferedImage backgroundImage;
    private Stack<Vector<GShape>> undoStack;
    private Stack<Vector<GShape>> redoStack;
    private GShape copiedShape;
    private Color defaultFillColor = Color.WHITE;
    private Color defaultStrokeColor = Color.BLACK;
    private float defaultStrokeWidth = 1.0f;
    private String defaultStrokeStyle = "Solid";

    public GMainPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        MouseEventHandler mouseHandler = new MouseEventHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        this.shapes = new Vector<GShape>();
        this.eShapeTool = null;
        this.eDrawingState = EDrawingState.eidle;
        this.currentShape = null;
        this.selectedShape = null;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        copiedShape = null;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.scale(zoomLevel, zoomLevel);

        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, (int)(getWidth() / zoomLevel), (int)(getHeight() / zoomLevel));

        if (canvasBounds != null) {
            // 캔버스 그림자
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRect(canvasBounds.x + 5, canvasBounds.y + 5,
                    canvasBounds.width, canvasBounds.height);

            // 캔버스 배경색
            g2d.setColor(canvasBackground);
            g2d.fillRect(canvasBounds.x, canvasBounds.y,
                    canvasBounds.width, canvasBounds.height);

            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, canvasBounds.x, canvasBounds.y,
                        canvasBounds.width, canvasBounds.height, null);
            }

            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(canvasBounds.x, canvasBounds.y,
                    canvasBounds.width - 1, canvasBounds.height - 1);
        }
        for(GShape shape: shapes) {
            shape.draw(g2d);
        }
        if (currentShape != null) {
            currentShape.draw(g2d);
        }
    }

    private void startTransform(int x, int y) {
        if (eShapeTool == null) return;
        if (eShapeTool == EShapeTool.eSelected) {
            this.currentShape = this.eShapeTool.newShape();
            this.shapes.add(this.currentShape);
            GShape clickedShape = onShape(x, y);
            if (clickedShape == null) {
                for (GShape shape : this.shapes) {
                    shape.setSelected(false);
                }
                this.selectedShapes.clear();
                this.transformer = new GDrawer(this.currentShape);
            } else {
                if (!clickedShape.isSelected()) {
                    for (GShape shape : this.shapes) {
                        shape.setSelected(false);
                    }
                    this.selectedShapes.clear();
                    clickedShape.setSelected(true);
                    this.selectedShapes.add(clickedShape);
                }
                this.transformer = new GMover(clickedShape, this.selectedShapes);
                this.eDrawingState = EDrawingState.eMove;
            }
            for (GShape shape : selectedShapes) {
                EAnchors anchor = shape.onAnchor(x, y);
                if (anchor != null) {
                    if (anchor == EAnchors.RR) {
                        this.transformer = new GRotator(shape, selectedShapes);
                        this.transformer.start((Graphics2D)getGraphics(), x, y);
                        this.eDrawingState = EDrawingState.eRotate;
                        this.repaint();
                        return;
                    } else {
                        this.transformer = new GResizer(shape, anchor);
                        this.transformer.start((Graphics2D)getGraphics(), x, y);
                        this.eDrawingState = EDrawingState.eResize;
                        this.repaint();
                        return;
                    }
                }
            }        } else {
            this.currentShape = this.eShapeTool.newShape();
            this.shapes.add(this.currentShape);
            this.transformer = new GDrawer(this.currentShape);
        }
        this.transformer.start((Graphics2D)getGraphics(), x, y);
        this.repaint();
    }

    private GShape onShape(int x, int y) {
        for(GShape shape:this.shapes) {
            if(shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
    private void keepTransform(int x, int y) {
        this.transformer.drag(graphics2d, x, y);
        this.repaint();
    }
    private void addPoint(int x, int y) {
        this.transformer.addpoint((Graphics2D) getGraphics(), x, y);
    }
    private void finishTransform(int x, int y) {
    	this.currentShape.setSelected(true);
        this.transformer.finish((Graphics2D) getGraphics(), x, y);
        if(this.eShapeTool==EShapeTool.eSelected) {
            Rectangle selectionBounds = ((GRectangle)this.currentShape).getBounds();
            for(GShape shape : this.shapes) {
                shape.setSelected(false);
            }
            this.selectedShapes.clear();
            for(GShape shape : this.shapes) {
                if(shape != this.currentShape && isShapeInSelection(shape, selectionBounds)) {
                    shape.setSelected(true);
                    this.selectedShapes.add(shape);
                }
            }
            this.shapes.removeLast();
        }
        this.repaint();
    }
    private boolean isShapeInSelection(GShape shape, Rectangle selectionBounds) {
        Rectangle2D shapeBounds = shape.getBounds();
        return selectionBounds.contains(shapeBounds.getX(), shapeBounds.getY()) &&
                selectionBounds.contains(shapeBounds.getX() + shapeBounds.getWidth(),
                        shapeBounds.getY() + shapeBounds.getHeight());
    }
    public void setEShapeTool(EShapeTool shapetool) {this.eShapeTool=shapetool;}
    public void createNewCanvas(int width, int height, String background) {
        canvasBounds = new Rectangle(100, 100, width, height);
        setPreferredSize(new Dimension(width + 200, height + 200));

        shapes.clear();
        selectedShapes.clear();
        currentShape = null;
        selectedShape = null;
        backgroundImage = null;

        switch (background) {
            case "White":
                canvasBackground = Color.WHITE;
                break;
            case "Background Color":
                canvasBackground = Color.LIGHT_GRAY;
                break;
            case "Transparent":
                canvasBackground = new Color(0, 0, 0, 0);
                break;
        }
        revalidate();
        repaint();
    }
    public void initialize() {
        shapes.clear();
        repaint();
    }
    public Rectangle getCanvasBounds() {
        return canvasBounds;
    }

    public Color getCanvasBackground() {
        return canvasBackground;
    }
    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        repaint();
    }
    public void setCanvasBackground(Color color) {
        this.canvasBackground = color;
        repaint();
    }
    public Vector<GShape> getShapes() {
        return shapes;
    }
    public void setShapes(Vector<GShape> shapes) {
        this.shapes = shapes;
    }
    public void paintShapes(Graphics2D g2d) {
        for (GShape shape : shapes) {
            shape.draw(g2d);
        }
    }
    public void setCanvasBounds(Rectangle bounds) {
        this.canvasBounds = bounds;
        if (bounds != null) {
            setPreferredSize(new Dimension(bounds.width + 200, bounds.height + 200));
            revalidate();
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            redoStack.push(currentState);

            Vector<GShape> previousState = undoStack.pop();
            setShapes(previousState);
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            undoStack.push(currentState);

            Vector<GShape> nextState = redoStack.pop();
            setShapes(nextState);
            repaint();
        }
    }
    public void saveState() {
        Vector<GShape> currentState = getClonedShapes();
        undoStack.push(currentState);
        redoStack.clear(); 
    }
    public void copyShape(GShape shape) {
        if (shape != null) {
            copiedShape = shape.clone();
        }
    }
    public void cutShape(GShape shape) {
        if (shape != null) {
            copyShape(shape);
            deleteShape(shape);
        }
    }
    public void pasteShape() {
        if (copiedShape != null) {
            saveState();

            GShape pastedShape = copiedShape.clone();
            pastedShape.setLocation(pastedShape.getX() + 20, pastedShape.getY() + 20);

            addShape(pastedShape);
            setSelectedShape(pastedShape);
            repaint();
        }
    }
    public boolean hasCopiedShape() {
        return copiedShape != null;
    }
    public void deleteShape(GShape shape) {
        if (shape != null) {
            saveState(); 

            shapes.remove(shape);
            if (selectedShape == shape) {
                selectedShape = null;
            }
            repaint();
        }
    }
    public void moveShapeForward(GShape shape) {
        int index = shapes.indexOf(shape);
        if (index < shapes.size() - 1 && index >= 0) {
            saveState(); 

            shapes.remove(index);
            shapes.add(index + 1, shape);
            repaint();
        }
    }

    public void moveShapeBackward(GShape shape) {
        int index = shapes.indexOf(shape);
        if (index > 0) {
            saveState(); 

            shapes.remove(index);
            shapes.add(index - 1, shape);
            repaint();
        }
    }

    private Vector<GShape> getClonedShapes() {
        Vector<GShape> clonedShapes = new Vector<>();
        for (GShape shape : shapes) {
            clonedShapes.add(shape.clone());
        }
        return clonedShapes;
    }

    public Color getDefaultFillColor() {
        return defaultFillColor;
    }

    public void setDefaultFillColor(Color color) {
        this.defaultFillColor = color;
    }

    public Color getDefaultStrokeColor() {
        return defaultStrokeColor;
    }

    public void setDefaultStrokeColor(Color color) {
        this.defaultStrokeColor = color;
    }

    public float getDefaultStrokeWidth() {
        return defaultStrokeWidth;
    }

    public void setDefaultStrokeWidth(float width) {
        this.defaultStrokeWidth = width;
    }

    public String getDefaultStrokeStyle() {
        return defaultStrokeStyle;
    }

    public void setDefaultStrokeStyle(String style) {
        this.defaultStrokeStyle = style;
    }

    public void addShape(GShape shape) {
        shapes.add(shape);
    }

    public GShape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(GShape shape) {
        this.selectedShape = shape;
    }
    private class MouseEventHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                this.mouse1Clicked(e);
            } else if (e.getClickCount() == 2) {
                this.mouse2Clicked(e);
            }
        }
        private Point toCanvasPoint(Point screenPoint) {
            int x = (int)(screenPoint.x / zoomLevel);
            int y = (int)(screenPoint.y / zoomLevel);
            return new Point(x, y);
        }

        private Point toScreenPoint(Point canvasPoint) {
            int x = (int)(canvasPoint.x * zoomLevel);
            int y = (int)(canvasPoint.y * zoomLevel);
            return new Point(x, y);
        }
        private void mouse1Clicked(MouseEvent e) {
            Point canvasPoint = toCanvasPoint(e.getPoint());
            int x = canvasPoint.x;
            int y = canvasPoint.y;

            if (eDrawingState == EDrawingState.eidle) {
                if (eShapeTool.getEPoints() == EPoints.e2P) {
                    startTransform(x, y);
                    eDrawingState = EDrawingState.e2P;
                } else if (eShapeTool.getEPoints() == EPoints.enP) {
                    startTransform(x, y);
                    eDrawingState = EDrawingState.enP;
                }
            } else if (eDrawingState == EDrawingState.e2P) {
                finishTransform(x, y);
                eDrawingState = EDrawingState.eidle;
            } else if (eDrawingState == EDrawingState.enP) {
                addPoint(x, y);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.enP) {
                Point canvasPoint = toCanvasPoint(e.getPoint());
                keepTransform(canvasPoint.x, canvasPoint.y);
            }
        }

        private void mouse2Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.enP) {
                finishTransform(e.getX(), e.getY());
                eDrawingState = EDrawingState.eidle;
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseDragged(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) { }
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.isControlDown()) {
                int rotation = e.getWheelRotation();
                Point mousePoint = e.getPoint();

                double scaleFactor = 1.1;
                if (rotation < 0) {
                    zoomLevel *= scaleFactor;
                } else {
                    zoomLevel /= scaleFactor;
                }
                zoomLevel = Math.max(0.1, Math.min(10.0, zoomLevel));

                repaint();
            }
        }
        private void updatePanelSize() {
            if (canvasBounds != null) {
                int width = (int)((canvasBounds.width + 200) * zoomLevel);
                int height = (int)((canvasBounds.height + 200) * zoomLevel);
                setPreferredSize(new Dimension(width, height));
                revalidate();
            }
        }

    }
}