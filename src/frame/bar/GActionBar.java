package frame.bar;

import java.awt.*;
import javax.swing.*;

public class GActionBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    private JButton drawButton;
    private JButton moveButton;
    private JButton resizeButton;
    private JButton rotateButton;
    private JButton clearButton;

    // constructor
    public GActionBar() {
        // 가로 방향 설정
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);

        // 작은 버튼 생성
        this.drawButton = createActionButton("Draw");
        this.moveButton = createActionButton("Move");
        this.resizeButton = createActionButton("Resize");
        this.rotateButton = createActionButton("Rotate");
        this.clearButton = createActionButton("Clear");
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setToolTipText(text);
        button.setFocusPainted(false);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setMargin(new Insets(2, 5, 2, 5));

        add(button);
        return button;
    }

    // methods
    public String getSelectedAction() {
        // 구현 필요
        return "Draw"; // 기본값
    }

    // initialize
    public void initialize() {
        // 기본 선택
    }
}