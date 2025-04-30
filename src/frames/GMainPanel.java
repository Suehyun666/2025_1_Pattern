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
    	eSelect,  // 선택 상태 추가
    	eMove     // 이동 상태 추가
    }
    private Vector<GShape> shapes;
    private GShape currentShape;
    private GShape selectedShape; // 선택된 도형을 추적하는 변수
    private EShapeType eShapeType;
    private EDrawingState eDrawingState;
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Point lastPoint; // 마지막 마우스 위치 저장
    
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
        
        // 선택된 도형 표시 (나중에 선택 표시를 위한 코드 추가 가능)
    }
    
    private void startDrawing(int x, int y) {
    	if (eShapeType == null) return;
        
        // Select 모드인 경우
        if (eShapeType == EShapeType.eSelect) {
            eDrawingState = EDrawingState.eSelect;
            // 선택 영역을 나타내는 사각형 생성
            currentShape = eShapeType.newShape();
            if (currentShape != null) {
                graphics2d = (Graphics2D) getGraphics();
                graphics2d.setXORMode(getBackground());
                
                transformer = new GDrawer(currentShape);
                transformer.start(graphics2d, x, y);
            }
            return;
        }
        
        // Move 모드인 경우
        if (eShapeType == EShapeType.eMove) {
            // 클릭한 위치에 도형이 있는지 확인
            selectedShape = findShapeAt(x, y);
            if (selectedShape != null) {
                eDrawingState = EDrawingState.eMove;
                lastPoint.x = x;
                lastPoint.y = y;
            }
            return;
        }
        
        // 일반 그리기 모드
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
    	// 현재 상태가 idle이거나 transformer가 필요한데 없는 경우 바로 종료
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
            // 선택 영역 드래그
            transformer.drag(graphics2d, x, y);
        } else if (eDrawingState == EDrawingState.eMove && selectedShape != null) {
            // 선택된 도형 이동
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
            // 선택 영역에 포함된 도형 찾기
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
            // 이동 완료
            eDrawingState = EDrawingState.eidle;
            repaint();
        }
    }
    
    // 특정 좌표에 있는 도형 찾기 (클릭 선택)
    private GShape findShapeAt(int x, int y) {
        // 역순으로 확인 (나중에 그려진 도형이 먼저 선택되도록)
        for (int i = shapes.size() - 1; i >= 0; i--) {
            GShape shape = shapes.get(i);
            if (shape != null && shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
    
    // 선택 영역 내에 있는 도형 찾기 (영역 선택)
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
        // 도구 변경 시 초기화
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
        	// 다각형 그리기 모드에서만 미리보기 선을 업데이트
            if (eDrawingState == EDrawingState.enP && currentShape instanceof GPolygon) {
                keepDrawing(e.getX(), e.getY());
            }
        }
    }
}