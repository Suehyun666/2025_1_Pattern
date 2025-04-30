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
    	eMove("Move", EDrawingType.e2P, GRectangle.class), // GShape.class에서 GRectangle.class로 변경
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
        	//여기다 붙인 이유 자기가 눌려있는지 알 수 없다. 부모가 안다.
        	//눌린 애가 보이는 위치에서 감지한다.
        	//서로 상호작용하려면 그 들의 부모에다 갖다놓아야한다. (전제 : 상호작용은 같은 레벨에서 )
        	//jpanel에 영향을 준다면 handler는 frame에 있어야한다. jpanel에 명령을 내려야함
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
		}//이 소스코드를 mainframe에 두면 panel이랑 associate안해도된다. 
		
	}
	public ShapeType getSelectedShape() {
		return selectedShape;
	}
}