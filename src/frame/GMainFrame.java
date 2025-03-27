package frame;

import frame.bar.GActionBar;
import frame.bar.GMenubar;
import frame.bar.GToolBar;
import frame.shape.DrawOvalState;
import frame.shape.DrawRectangleState;
import frame.shape.DrawTriangleState;
import frame.state.MoveState;
import frame.state.ResizeState;
import frame.state.RotateState;
import interfaces.DrawingState;
import constants.ShapeType;
import constants.ActionType;

import java.awt.*;
import javax.swing.*;

import static java.awt.Color.DARK_GRAY;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    //components
    private GToolBar toolBar;
    private GActionBar actionBar;
    private GMenubar menuBar;
    private GMainPanel mainPanel;

    // constructor
    public GMainFrame() {
        // attributes
        setTitle("Drawing Application");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.getContentPane().setBackground(DARK_GRAY);

        this.menuBar = new GMenubar();
        this.setJMenuBar(menuBar);

        this.mainPanel = new GMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        this.toolBar = new GToolBar(this);
        add(toolBar, BorderLayout.WEST);

        this.actionBar = new GActionBar(this);
        add(actionBar, BorderLayout.NORTH);

        setVisible(true);
    }

    // initialize
    public void initialize() {
        menuBar.initialize();
        mainPanel.createNewCanvas(800, 600);
        toolBar.initialize();
        actionBar.initialize();
        mainPanel.initialize();
    }

    public GMainPanel getMainPanel() {
        return mainPanel;
    }

    public GToolBar getToolBar() {
        return toolBar;
    }

    public GActionBar getActionBar() {
        return actionBar;
    }

    public void updateDrawingState() {
        ShapeType selectedShape = toolBar.getSelectedShape();
        ActionType selectedAction = actionBar.getSelectedAction();

        DrawingState newState = null;

        if (selectedAction == ActionType.DRAW) {
            switch (selectedShape) {
                case RECTANGLE:
                    newState = new DrawRectangleState(mainPanel);
                    break;
                case OVAL:
                    newState = new DrawOvalState(mainPanel);
                    break;
                case TRIANGLE:
                    newState = new DrawTriangleState(mainPanel);
                    break;
            }
        } else if (selectedAction == ActionType.MOVE) {
            newState = new MoveState(mainPanel);

        } else if (selectedAction == ActionType.RESIZE) {
             newState = new ResizeState(mainPanel);
        } else if (selectedAction == ActionType.ROTATE) {
             newState = new RotateState(mainPanel);
        }

        if (newState != null) {
            mainPanel.setDrawingState(newState);
        }
    }
}