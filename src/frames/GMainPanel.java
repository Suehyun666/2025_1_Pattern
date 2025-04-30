package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;
import javax.swing.JPanel;

import shapes.GPolygon;
import shapes.GShape;
import shapes.GShapeToolBar.EShapeType;
import transformers.GDrawer;
import transformers.GTransFormer;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public enum EDrawingType{
    	e2P,
    	enP
    }
    public enum EDrawingState{
    	eidle,
    	e2P,
    	enP,
    	eSelect,  // ���� ���� �߰�
    	eMove     // �̵� ���� �߰�
    }
    private Vector<GShape> shapes;
    private GShape currentShape;
    private GShape selectedShape; // ���õ� ������ �����ϴ� ����
    private EShapeType eShapeType;
    private EDrawingState eDrawingState;
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Point lastPoint; // ������ ���콺 ��ġ ����
    
    public GMainPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        MouseEventHandler mouseHandler = new MouseEventHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        this.shapes = new Vector<GShape>();
        this.eShapeType = null;
        this.eDrawingState = EDrawingState.eidle;
        this.currentShape = null;
        this.selectedShape = null;
        this.lastPoint = new Point(0, 0);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(GShape shape: shapes) {
        	shape.draw((Graphics2D) g);
        }
        if (currentShape != null) {
            currentShape.draw((Graphics2D) g);
        }
        
        // ���õ� ���� ǥ�� (���߿� ���� ǥ�ø� ���� �ڵ� �߰� ����)
    }
    
    private void startDrawing(int x, int y) {
    	if (eShapeType == null) return;
        
        // Select ����� ���
        if (eShapeType == EShapeType.eSelect) {
            eDrawingState = EDrawingState.eSelect;
            // ���� ������ ��Ÿ���� �簢�� ����
            currentShape = eShapeType.newShape();
            if (currentShape != null) {
                graphics2d = (Graphics2D) getGraphics();
                graphics2d.setXORMode(getBackground());
                
                transformer = new GDrawer(currentShape);
                transformer.start(graphics2d, x, y);
            }
            return;
        }
        
        // Move ����� ���
        if (eShapeType == EShapeType.eMove) {
            // Ŭ���� ��ġ�� ������ �ִ��� Ȯ��
            selectedShape = findShapeAt(x, y);
            if (selectedShape != null) {
                eDrawingState = EDrawingState.eMove;
                lastPoint.x = x;
                lastPoint.y = y;
            }
            return;
        }
        
        // �Ϲ� �׸��� ���
        currentShape = eShapeType.newShape();
        if (currentShape == null) return;
        
        if (eShapeType.getDrawingType() == EDrawingType.enP) {
            ((GPolygon)currentShape).setPoint(x, y);
            eDrawingState = EDrawingState.enP;
            repaint();
        } else if (eShapeType.getDrawingType() == EDrawingType.e2P) {
            graphics2d = (Graphics2D) getGraphics();
            graphics2d.setXORMode(getBackground());
            
            transformer = new GDrawer(currentShape);
            transformer.start(graphics2d, x, y);
            
            eDrawingState = EDrawingState.e2P;
        }
    }
    
    private void keepDrawing(int x, int y) {
    	// ���� ���°� idle�̰ų� transformer�� �ʿ��ѵ� ���� ��� �ٷ� ����
        if (eDrawingState == EDrawingState.eidle) {
            return;
        }
        
        if ((eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.eSelect) 
                && transformer == null) {
            return;
        }
        
    	if (eDrawingState == EDrawingState.e2P && transformer != null) {
            transformer.drag(graphics2d, x, y);
        } else if (eDrawingState == EDrawingState.enP && currentShape instanceof GPolygon) {
            ((GPolygon)currentShape).dragPoint(x, y);
            repaint();
        } else if (eDrawingState == EDrawingState.eSelect && transformer != null) {
            // ���� ���� �巡��
            transformer.drag(graphics2d, x, y);
        } else if (eDrawingState == EDrawingState.eMove && selectedShape != null) {
            // ���õ� ���� �̵�
            int dx = x - lastPoint.x;
            int dy = y - lastPoint.y;
            selectedShape.move(dx, dy);
            lastPoint.x = x;
            lastPoint.y = y;
            repaint();
        }
    }
    
    private void addPoint(int x, int y) {
    	if (eDrawingState == EDrawingState.enP && currentShape instanceof GPolygon) {
            ((GPolygon)currentShape).addPoint(x, y);
            repaint();
        }
    }
    
    private void finishDrawing(int x, int y) {
    	if (eDrawingState == EDrawingState.e2P && transformer != null) {
            GShape shape = transformer.finish(graphics2d, x, y);
            shapes.add(shape);
            currentShape = null;
            repaint();  
            
            eDrawingState = EDrawingState.eidle;
            
            if (graphics2d != null) {
                graphics2d.dispose();
                graphics2d = null;
            }
            transformer = null;
        } else if (eDrawingState == EDrawingState.enP && currentShape != null) {
            shapes.add(currentShape);  
            currentShape = null;
            eDrawingState = EDrawingState.eidle;
            repaint();
        } else if (eDrawingState == EDrawingState.eSelect && transformer != null) {
            // ���� ������ ���Ե� ���� ã��
            GShape selectionArea = transformer.finish(graphics2d, x, y);
            if (selectionArea != null) {
                selectedShape = findShapeInArea(selectionArea);
            }
            currentShape = null;
            eDrawingState = EDrawingState.eidle;
            
            if (graphics2d != null) {
                graphics2d.dispose();
                graphics2d = null;
            }
            transformer = null;
            repaint();
        } else if (eDrawingState == EDrawingState.eMove) {
            // �̵� �Ϸ�
            eDrawingState = EDrawingState.eidle;
            repaint();
        }
    }
    
    // Ư�� ��ǥ�� �ִ� ���� ã�� (Ŭ�� ����)
    private GShape findShapeAt(int x, int y) {
        // �������� Ȯ�� (���߿� �׷��� ������ ���� ���õǵ���)
        for (int i = shapes.size() - 1; i >= 0; i--) {
            GShape shape = shapes.get(i);
            if (shape != null && shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
    
    // ���� ���� ���� �ִ� ���� ã�� (���� ����)
    private GShape findShapeInArea(GShape selectionArea) {
        if (selectionArea == null) return null;
        
        for (int i = shapes.size() - 1; i >= 0; i--) {
            GShape shape = shapes.get(i);
            if (shape != null && shape.intersects(selectionArea)) {
                return shape;
            }
        }
        return null;
    }
    
    public void initialize() {
    	shapes.clear();
        selectedShape = null;
        currentShape = null;
        eDrawingState = EDrawingState.eidle;
        repaint();
    }
    
    public void setEShapeType(EShapeType shapetype) {
    	this.eShapeType = shapetype;
        // ���� ���� �� �ʱ�ȭ
        if (shapetype != EShapeType.eMove && shapetype != EShapeType.eSelect) {
            selectedShape = null;
        }
        eDrawingState = EDrawingState.eidle;
    }

    private class MouseEventHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        	if (eDrawingState == EDrawingState.eidle) {
                if (e.getClickCount() == 2) {
                    initialize();
                    return;
                }
            }
        	if (eDrawingState == EDrawingState.enP && currentShape instanceof GPolygon) {
                if (e.getClickCount() == 1) {
                    ((GPolygon)currentShape).addPoint(e.getX(), e.getY());
                    repaint();
                } else if (e.getClickCount() == 2) {
                    shapes.add(currentShape);  
                    currentShape = null;
                    eDrawingState = EDrawingState.eidle;
                    repaint();
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
        	if (eDrawingState == EDrawingState.eidle) {
                startDrawing(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        	keepDrawing(e.getX(), e.getY());
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
        	if (eDrawingState == EDrawingState.e2P || 
                eDrawingState == EDrawingState.eSelect || 
                eDrawingState == EDrawingState.eMove) {
                finishDrawing(e.getX(), e.getY());
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
        	// �ٰ��� �׸��� ��忡���� �̸����� ���� ������Ʈ
            if (eDrawingState == EDrawingState.enP && currentShape instanceof GPolygon) {
                keepDrawing(e.getX(), e.getY());
            }
        }
    }
}