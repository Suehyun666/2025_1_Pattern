package frame;

import frame.bar.GActionBar;
import frame.bar.GMenubar;
import frame.bar.GToolBar;

import java.awt.*;
import javax.swing.*;

import static java.awt.Color.DARK_GRAY;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    //components
    private GToolBar toolBar;
    private GActionBar shapeBar;
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

        this.toolBar = new GToolBar();
        add(toolBar, BorderLayout.WEST);

        this.shapeBar = new GActionBar();
        add(shapeBar, BorderLayout.NORTH);

        this.mainPanel = new GMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // initialize
    public void initialize() {
        menuBar.initialize();
        toolBar.initialize();
        shapeBar.initialize();
        mainPanel.initialize();
    }

    public GMainPanel getMainPanel() {
        return mainPanel;
    }
}