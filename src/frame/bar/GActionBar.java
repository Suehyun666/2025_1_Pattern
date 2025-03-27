package frame.bar;

import java.awt.*;
import javax.swing.*;
import frame.GMainFrame;
import constants.ActionType;

public class GActionBar extends JToolBar {
    private static final long serialVersionUID = 1L;
    //components
    private JButton drawButton;
    private JButton moveButton;
    private JButton resizeButton;
    private JButton rotateButton;
    private JButton clearButton;
    private ActionType selectedAction;
    private GMainFrame mainFrame;

    // constructor
    public GActionBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.selectedAction = ActionType.DRAW;
        // attribute
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);

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
    public ActionType getSelectedAction() {
        return selectedAction;
    }

    // initialize
    public void initialize() {
        drawButton.addActionListener(e -> {
            selectedAction = ActionType.DRAW;
            updateDrawingState();
        });

        moveButton.addActionListener(e -> {
            selectedAction = ActionType.MOVE;
            updateDrawingState();
        });

        resizeButton.addActionListener(e -> {
            selectedAction = ActionType.RESIZE;
            updateDrawingState();
        });

        rotateButton.addActionListener(e -> {
            selectedAction = ActionType.ROTATE;
            updateDrawingState();
        });

        clearButton.addActionListener(e -> {
            selectedAction = ActionType.CLEAR;
            mainFrame.getMainPanel().clearShapes();
        });

        drawButton.doClick();
    }

    private void updateDrawingState() {
        mainFrame.updateDrawingState();
    }
}