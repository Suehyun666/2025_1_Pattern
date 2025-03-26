package frame.bar;

import java.awt.*;
import javax.swing.*;

public class GToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    private JButton rectangleButton;
    private JButton triangleButton;
    private JButton ovalButton;
    private JButton polygonButton;
    private JButton textBoxButton;
    private ButtonGroup toolButtonGroup;

    // constructor
    public GToolBar() {
        // 세로 방향 설정
        setOrientation(JToolBar.VERTICAL);
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(40, 0)); // 좁은 너비 설정

        this.toolButtonGroup = new ButtonGroup();

        // 좁은 툴바에 맞는 작은 버튼 생성
        this.rectangleButton = createToolButton("R", "Rectangle");
        this.triangleButton = createToolButton("T", "Triangle");
        this.ovalButton = createToolButton("O", "Oval");
        this.polygonButton = createToolButton("P", "Polygon");
        this.textBoxButton = createToolButton("A", "Text");

    }

    private JButton createToolButton(String text, String tooltip) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }

    // methods
    public String getSelectedShape() {
        return "Rectangle"; // 기본값
    }

    // initialize
    public void initialize() {
        // 기본 선택
    }
}