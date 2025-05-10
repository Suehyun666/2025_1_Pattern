package frames;

import shapes.GShapeToolBar;
import java.awt.*;

import javax.swing.*;

import static java.awt.Color.DARK_GRAY;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    //components
    private GShapeToolBar shapetoolBar;
    private GMenubar menuBar;
    private GMainPanel mainPanel;
//    private GPictureToolBar pictureToolBar;
    // constructor
    public GMainFrame() {
        // attributes
        setTitle("Drawing Application");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.getContentPane().setBackground(DARK_GRAY);

        //components
        this.menuBar = new GMenubar();
        this.setJMenuBar(menuBar);

        this.mainPanel = new GMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        this.shapetoolBar = new GShapeToolBar(this);
        add(shapetoolBar, BorderLayout.WEST);

//        this.pictureToolBar = new GPictureToolBar(this);
//        add(pictureToolBar, BorderLayout.SOUTH);
//        pictureToolBar.showToolBar(true);

        setVisible(true);
    }

    // initialize
    public void initialize() {
    	this.menuBar.associate(this.mainPanel);
    	this.shapetoolBar.associate(this.mainPanel);
    	
        menuBar.initialize();
        shapetoolBar.initialize();
        mainPanel.initialize();
    }

    public GMainPanel getMainPanel() {
        return mainPanel;
    }

}