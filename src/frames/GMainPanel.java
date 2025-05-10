package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.JPanel;

import shapes.GRectangle;
import shapes.GShape;
import shapes.GShape.EPoints;
import shapes.GShape.EAnchors;
import shapes.GShapeToolBar.EShapeTool;
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
    private Vector<GShape> shapes;
    private GShape currentShape;
    private GShape selectedShape;
    private EShapeTool eShapeTool;
    private EDrawingState eDrawingState;
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Rectangle selectionRect = null;  // 선택 영역 저장
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.graphics2d = (Graphics2D) g;
        for(GShape shape: shapes) {
            shape.draw(graphics2d);
        }
        if (currentShape != null) {
            currentShape.draw(graphics2d);
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
                    if (anchor == GShape.EAnchors.ROTATE) {
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
        shapes.clear();
        selectedShapes.clear();
        currentShape = null;
        selectedShape = null;

        setPreferredSize(new Dimension(width, height));

        switch (background) {
            case "White":
                setBackground(Color.WHITE);
                break;
            case "Background Color":
                setBackground(Color.LIGHT_GRAY);
                break;
            case "Transparent":
                setBackground(new Color(0, 0, 0, 0));
                break;
        }
        revalidate();
        repaint();
    }
    public void initialize() {
        shapes.clear();
        repaint();
    }
    private class MouseEventHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        private int startX, startY;  // 드래그 시작 좌표

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {}
    }
}