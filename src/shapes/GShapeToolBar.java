package shapes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import frames.GMainFrame;
import frames.GMainPanel;
import shapes.GShape.EPoints;
public class GShapeToolBar extends JToolBar {
	// attributes
	private static final long serialVersionUID = 1L;
	private GMainPanel mainPanel;
	private GMainFrame mainFrame;
	private EShapeTool selectedShape;
	private ButtonGroup buttonGroup;
	private JButton moveToolButton;
	private JButton marqueeToolButton;
	private EShapeTool currentMarqueeTool;
	private Map<String, JButton> toolButtons;

	// ���� ���� enum
	public enum EShapeTool {
		eMove("Move Tool", EPoints.e2P, GRectangle.class, "move.png"),
		eSelected("Select Tool", EPoints.e2P, GRectangle.class, "selected.png"),
		eEllipticalMarquee("Elliptical Marquee Tool", EPoints.e2P, GEllipse.class, "elliptical_marquee.png"),
		eRectangularMarquee("Rectangular Marquee Tool", EPoints.e2P, GRectangle.class, "rectangular_marquee.png"),
		eSingleRowMarquee("Single Row Marquee Tool", EPoints.e2P, GRectangle.class, "row_marquee.png"),
		eSingleColumnMarquee("Single Column Marquee Tool", EPoints.e2P, GRectangle.class, "column_marquee.png"),
		eRectangle("Rectangle", EPoints.e2P, GRectangle.class, "rectangle.png"),
		eTriangle("Triangle", EPoints.e2P, GTriangle.class, "triangle.png"),
		eEllipse("Ellipse", EPoints.e2P, GEllipse.class, "ellipse.png"),
		eLine("Line", EPoints.e2P, GLine.class, "line.png"),
		eText("Text", EPoints.e2P, GText.class, "text.png"),
		ePolygon("Polygon", EPoints.enP, GPolygon.class, "polygon.png");
		private String name;
		private EPoints ePoints;
		private Class<?> classShape;
		private String iconName;

		private EShapeTool(String name, EPoints ePoints, Class<?> classShape, String iconName) {
			this.name = name;
			this.ePoints = ePoints;
			this.classShape = classShape;
			this.iconName = iconName;
		}

		public String getName() {
			return this.name;
		}

		public EPoints getEPoints() {
			return this.ePoints;
		}

		public String getIconName() {
			return this.iconName;
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
		this.toolButtons = new HashMap<>();
		this.currentMarqueeTool = EShapeTool.eEllipticalMarquee; // �⺻ ��Ű ���� ����

		// ���� ����
		setOrientation(JToolBar.VERTICAL);
		setFloatable(false);
		setBorderPainted(false);
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// ��ư �׷� ����
		buttonGroup = new ButtonGroup();

		// ���� ���� ��ư ���� (Move�� Marquee)
		createMainTools();
	}

	// ���� ���� ��ư ���� (Move �� Marquee)
	private void createMainTools() {
		// Move Tool ��ư ����
		moveToolButton = createToolButton(EShapeTool.eMove);
		add(moveToolButton);
		toolButtons.put(EShapeTool.eMove.toString(), moveToolButton);

		// ��Ű ���� ��ư ���� (�⺻���� Elliptical Marquee)
		marqueeToolButton = createToolButton(EShapeTool.eEllipticalMarquee);
		setupMarqueeToolPopup(marqueeToolButton);
		add(marqueeToolButton);
		toolButtons.put("marquee", marqueeToolButton);

		// �ٸ� ���� ��ư�� �߰� (Rectangle, Ellipse ��)
		addToolButtons();

		// ���� �߰�
		addSeparator();
	}

	// ���� ������ ��ư ����
	private JButton createToolButton(EShapeTool shapeType) {
		JButton button = new JButton();
		// ������ �ε� - ���� ������ ������ �־�� ��, ������ �ؽ�Ʈ ���
		ImageIcon icon = loadIcon(shapeType.getIconName());
		if (icon != null) {
			button.setIcon(icon);
		} else {
			// �������� ���� ��� ���� �̸� �ؽ�Ʈ ���
			button.setText(shapeType.getName().substring(0, 1));
		}

		button.setToolTipText(shapeType.getName());
		button.setActionCommand(shapeType.toString());

		// ��ư ��Ÿ�� ����
		button.setMargin(new Insets(4, 4, 4, 4));
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(32, 32));
		button.setMinimumSize(new Dimension(32, 32));
		button.setMaximumSize(new Dimension(32, 32));
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setBackground(Color.DARK_GRAY);
		button.setForeground(Color.WHITE);

		return button;
	}

	// ������ �ε� �޼ҵ�
	private ImageIcon loadIcon(String iconName) {
		try {
			// ������ ���� ��δ� ������Ʈ�� �°� ���� �ʿ�
			String iconPath = "/icons/" + iconName;
			return new ImageIcon(getClass().getResource(iconPath));
		} catch (Exception e) {
			// �������� ã�� �� ���� ��� null ��ȯ
			return null;
		}
	}

	// ��Ű ������ ���� �˾� �޴� ����
	private void setupMarqueeToolPopup(JButton button) {
		JPopupMenu popupMenu = new JPopupMenu();

		// ��Ű ���� �ɼ� �߰�
		addMarqueeMenuItem(popupMenu, EShapeTool.eEllipticalMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eRectangularMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eSingleRowMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eSingleColumnMarquee);

		// ��ư ��Ŭ�� �� �˾� ǥ��
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e);
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					// �Ϲ� Ŭ�� �� ���� ���õ� ��Ű ���� ����
					button.setSelected(true);
					if (mainPanel != null) {
						mainPanel.setEShapeTool(currentMarqueeTool);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e);
				}
			}

			private void showPopup(MouseEvent e) {
				popupMenu.show(e.getComponent(),
						e.getComponent().getWidth(), // �����ʿ� ǥ��
						0); // ��ư ��ܿ��� ����
			}
		});

		// ��ư Ŭ�� �̺�Ʈ �߰�
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainPanel != null) {
					mainPanel.setEShapeTool(currentMarqueeTool);
				}
			}
		});
	}

	// �˾� �޴��� ��Ű ���� �׸� �߰�
	private void addMarqueeMenuItem(JPopupMenu menu, EShapeTool shapeType) {
		JMenuItem menuItem = new JMenuItem(shapeType.getName());

		// ������ �߰�
		ImageIcon icon = loadIcon(shapeType.getIconName());
		if (icon != null) {
			menuItem.setIcon(icon);
		}

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ���� ���õ� ��Ű ���� ������Ʈ
				currentMarqueeTool = shapeType;

				// ��Ű ��ư ������ ������Ʈ
				ImageIcon newIcon = loadIcon(shapeType.getIconName());
				if (newIcon != null) {
					marqueeToolButton.setIcon(newIcon);
				} else {
					marqueeToolButton.setText(shapeType.getName().substring(0, 1));
				}

				marqueeToolButton.setToolTipText(shapeType.getName());

				// ��Ű ��ư ���� �� ���� ����
				marqueeToolButton.setSelected(true);
				if (mainPanel != null) {
					mainPanel.setEShapeTool(shapeType);
				}
			}
		});

		menu.add(menuItem);
	}

	// �߰� ���� ��ư�� ����
	private void addToolButtons() {
		// �߰� ������ ���� (Rectangle, Triangle, Ellipse, Line, Text, Polygon)
		for (EShapeTool shapeType : EShapeTool.values()) {
			// Move�� ��Ű ������ �̹� ������ �߰������� �ǳʶ�
			if (shapeType == EShapeTool.eMove ||
					shapeType == EShapeTool.eEllipticalMarquee ||
					shapeType == EShapeTool.eRectangularMarquee ||
					shapeType == EShapeTool.eSingleRowMarquee ||
					shapeType == EShapeTool.eSingleColumnMarquee) {
				continue;
			}

			JButton button = createToolButton(shapeType);
			add(button);
			toolButtons.put(shapeType.toString(), button);

			// Ŭ�� �̺�Ʈ �����ʴ� initialize()���� �߰�
		}
	}

	public void initialize() {
		// ��� ��ư�� �׼� ������ �߰� (mainPanel ���� ��)
		for (JButton button : toolButtons.values()) {
			if (!button.equals(marqueeToolButton)) { // ��Ű ��ư�� �̹� ó����
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (mainPanel != null) {
							String command = e.getActionCommand();
							if (command != null && !command.isEmpty()) {
								try {
									EShapeTool shapeType = EShapeTool.valueOf(command);
									mainPanel.setEShapeTool(shapeType);
								} catch (IllegalArgumentException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				});
			}
		}

		// �⺻ ���� ���� (Move Tool)
		if (mainPanel != null && moveToolButton != null) {
			moveToolButton.setSelected(true);
			mainPanel.setEShapeTool(EShapeTool.eMove);
		}
	}

	public void associate(GMainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public EShapeTool getSelectedShape() {
		return selectedShape;
	}
}