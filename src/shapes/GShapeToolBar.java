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

	// 도구 유형 enum
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
		this.currentMarqueeTool = EShapeTool.eEllipticalMarquee; // 기본 마키 도구 설정

		// 툴바 설정
		setOrientation(JToolBar.VERTICAL);
		setFloatable(false);
		setBorderPainted(false);
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// 버튼 그룹 생성
		buttonGroup = new ButtonGroup();

		// 메인 도구 버튼 생성 (Move와 Marquee)
		createMainTools();
	}

	// 메인 도구 버튼 생성 (Move 및 Marquee)
	private void createMainTools() {
		// Move Tool 버튼 생성
		moveToolButton = createToolButton(EShapeTool.eMove);
		add(moveToolButton);
		toolButtons.put(EShapeTool.eMove.toString(), moveToolButton);

		// 마키 도구 버튼 생성 (기본값은 Elliptical Marquee)
		marqueeToolButton = createToolButton(EShapeTool.eEllipticalMarquee);
		setupMarqueeToolPopup(marqueeToolButton);
		add(marqueeToolButton);
		toolButtons.put("marquee", marqueeToolButton);

		// 다른 도구 버튼들 추가 (Rectangle, Ellipse 등)
		addToolButtons();

		// 공간 추가
		addSeparator();
	}

	// 도구 아이콘 버튼 생성
	private JButton createToolButton(EShapeTool shapeType) {
		JButton button = new JButton();
		// 아이콘 로드 - 실제 아이콘 파일이 있어야 함, 없으면 텍스트 사용
		ImageIcon icon = loadIcon(shapeType.getIconName());
		if (icon != null) {
			button.setIcon(icon);
		} else {
			// 아이콘이 없는 경우 단축 이름 텍스트 사용
			button.setText(shapeType.getName().substring(0, 1));
		}

		button.setToolTipText(shapeType.getName());
		button.setActionCommand(shapeType.toString());

		// 버튼 스타일 설정
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

	// 아이콘 로드 메소드
	private ImageIcon loadIcon(String iconName) {
		try {
			// 아이콘 파일 경로는 프로젝트에 맞게 조정 필요
			String iconPath = "/icons/" + iconName;
			return new ImageIcon(getClass().getResource(iconPath));
		} catch (Exception e) {
			// 아이콘을 찾을 수 없는 경우 null 반환
			return null;
		}
	}

	// 마키 도구에 대한 팝업 메뉴 설정
	private void setupMarqueeToolPopup(JButton button) {
		JPopupMenu popupMenu = new JPopupMenu();

		// 마키 도구 옵션 추가
		addMarqueeMenuItem(popupMenu, EShapeTool.eEllipticalMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eRectangularMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eSingleRowMarquee);
		addMarqueeMenuItem(popupMenu, EShapeTool.eSingleColumnMarquee);

		// 버튼 우클릭 시 팝업 표시
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e);
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					// 일반 클릭 시 현재 선택된 마키 도구 실행
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
						e.getComponent().getWidth(), // 오른쪽에 표시
						0); // 버튼 상단에서 시작
			}
		});

		// 버튼 클릭 이벤트 추가
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainPanel != null) {
					mainPanel.setEShapeTool(currentMarqueeTool);
				}
			}
		});
	}

	// 팝업 메뉴에 마키 도구 항목 추가
	private void addMarqueeMenuItem(JPopupMenu menu, EShapeTool shapeType) {
		JMenuItem menuItem = new JMenuItem(shapeType.getName());

		// 아이콘 추가
		ImageIcon icon = loadIcon(shapeType.getIconName());
		if (icon != null) {
			menuItem.setIcon(icon);
		}

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 현재 선택된 마키 도구 업데이트
				currentMarqueeTool = shapeType;

				// 마키 버튼 아이콘 업데이트
				ImageIcon newIcon = loadIcon(shapeType.getIconName());
				if (newIcon != null) {
					marqueeToolButton.setIcon(newIcon);
				} else {
					marqueeToolButton.setText(shapeType.getName().substring(0, 1));
				}

				marqueeToolButton.setToolTipText(shapeType.getName());

				// 마키 버튼 선택 및 도구 설정
				marqueeToolButton.setSelected(true);
				if (mainPanel != null) {
					mainPanel.setEShapeTool(shapeType);
				}
			}
		});

		menu.add(menuItem);
	}

	// 추가 도구 버튼들 생성
	private void addToolButtons() {
		// 추가 도구들 생성 (Rectangle, Triangle, Ellipse, Line, Text, Polygon)
		for (EShapeTool shapeType : EShapeTool.values()) {
			// Move와 마키 도구는 이미 별도로 추가했으니 건너뜀
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

			// 클릭 이벤트 리스너는 initialize()에서 추가
		}
	}

	public void initialize() {
		// 모든 버튼에 액션 리스너 추가 (mainPanel 연결 후)
		for (JButton button : toolButtons.values()) {
			if (!button.equals(marqueeToolButton)) { // 마키 버튼은 이미 처리됨
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

		// 기본 도구 선택 (Move Tool)
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