package frame.bar;

import java.awt.*;
import javax.swing.*;
import frame.GMainFrame;
import constants.ShapeType;

public class GToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    private JButton rectangleButton;
    private JButton triangleButton;
    private JButton ovalButton;
    private JButton polygonButton;
    private JButton textBoxButton;
    private ButtonGroup toolButtonGroup;
    private ShapeType selectedShape;
    private GMainFrame mainFrame;

    // constructor
    public GToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.selectedShape = ShapeType.RECTANGLE; // 기본값

        setOrientation(JToolBar.VERTICAL);
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(40, 0)); // 좁은 너비 설정

        this.toolButtonGroup = new ButtonGroup();

        this.rectangleButton = createToolButton("R", "Rectangle");
        this.triangleButton = createToolButton("T", "Triangle");
        this.ovalButton = createToolButton("O", "Oval");
        this.polygonButton = createToolButton("P", "Polygon");
        this.textBoxButton = createToolButton("A", "Text");
    }

    private JButton createToolButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    // methods
    public ShapeType getSelectedShape() {
        return selectedShape;
    }

    // initialize
    public void initialize() {
        rectangleButton.addActionListener(e -> {
            selectedShape = ShapeType.RECTANGLE;
            updateDrawingState();
        });

        triangleButton.addActionListener(e -> {
            selectedShape = ShapeType.TRIANGLE;
            updateDrawingState();
        });

        ovalButton.addActionListener(e -> {
            selectedShape = ShapeType.OVAL;
            updateDrawingState();
        });

        polygonButton.addActionListener(e -> {
            selectedShape = ShapeType.POLYGON;
            updateDrawingState();
        });

        textBoxButton.addActionListener(e -> {
            selectedShape = ShapeType.TEXT;
            updateDrawingState();
        });

        rectangleButton.doClick();
    }

    private void updateDrawingState() {
        mainFrame.updateDrawingState();
    }
}