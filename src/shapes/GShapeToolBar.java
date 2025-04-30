package shapes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import constants.ShapeType;
import frames.GMainFrame;
import frames.GMainPanel;
import frames.GMainPanel.EDrawingType;
import shapes.GShapeToolBar.EShapeType;

public class GShapeToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;
    private GMainPanel mainPanel;
	private GMainFrame mainFrame;
	private ShapeType selectedShape;
    
    public enum EShapeType {
    	eSelect("select", EDrawingType.e2P, GRectangle.class),
    	eMove("Move", EDrawingType.e2P, GRectangle.class), // GShape.class���� GRectangle.class�� ����
    	eRectangle("rectangle", EDrawingType.e2P, GRectangle.class),
    	eEllipse("ellipse", EDrawingType.e2P, GEllipse.class),
    	eLine("line", EDrawingType.e2P, GLine.class),
    	ePolygon("polygon", EDrawingType.enP, GPolygon.class);
    	
    	private String name;
    	private EDrawingType eDrawingType;
    	private Class<?> classShape;
    	
		private EShapeType(String name, EDrawingType eDrawingType, Class<?> classShape) {
			this.name = name;
			this.eDrawingType = eDrawingType;
			this.classShape = classShape;
		}
		
		public String getName() {
			return this.name;
		}
		public EDrawingType getDrawingType() {
			return this.eDrawingType;
		}
		public GShape newShape(){
			try {
				GShape shape = (GShape) classShape.getConstructor().newInstance();
				return shape;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}
    }
    public GShapeToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.selectedShape = null;
        
        setOrientation(JToolBar.VERTICAL);
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);
        ButtonGroup buttongroup =new ButtonGroup();
        for (EShapeType eshapeType : EShapeType.values()) {
        	JRadioButton radiobutton = new JRadioButton(eshapeType.getName());
        	ActionHandler actionhandler = new ActionHandler();
        	radiobutton.addActionListener(actionhandler);
        	radiobutton.setActionCommand(eshapeType.toString());
        	
        	buttongroup.add(radiobutton); 
        	this.add(radiobutton);
        	//����� ���� ���� �ڱⰡ �����ִ��� �� �� ����. �θ� �ȴ�.
        	//���� �ְ� ���̴� ��ġ���� �����Ѵ�.
        	//���� ��ȣ�ۿ��Ϸ��� �� ���� �θ𿡴� ���ٳ��ƾ��Ѵ�. (���� : ��ȣ�ۿ��� ���� �������� )
        	//jpanel�� ������ �شٸ� handler�� frame�� �־���Ѵ�. jpanel�� ����� ��������
        }
    }
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	public void associate(GMainPanel mainPanel) {
		this.mainPanel=mainPanel;
	}
	private class ActionHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String sShapeType = e.getActionCommand();
			EShapeType eshapeType=EShapeType.valueOf(sShapeType);
			mainPanel.setEShapeType(eshapeType);
		}//�� �ҽ��ڵ带 mainframe�� �θ� panel�̶� associate���ص��ȴ�. 
		
	}
	public ShapeType getSelectedShape() {
		return selectedShape;
	}
}