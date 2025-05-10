package frames;

import javax.swing.JMenuBar;

import menus.edit.GEditMenu;
import menus.file.GFileMenu;
import menus.GFilterMenu;
import menus.GGraphicMenu;
import menus.GHelpMenu;
import menus.GImageMenu;
import menus.GLayerMenu;
import menus.GSelectMenu;
import menus.GTypeMenu;
import menus.GWindowMenu;

import static java.awt.Color.GRAY;

public class GMenubar extends JMenuBar {
    // attributes
    private static final long serialVersionUID = 1L;
    //components
    private GFileMenu fileMenu;
    private GEditMenu editMenu;
    private GGraphicMenu graphicMenu;
    private GImageMenu imagemenu;
    private GLayerMenu layermenu;
    private GTypeMenu typemenu;
    private GSelectMenu selectmenu;
    private GFilterMenu filtermenu;
    private GWindowMenu windowmenu;
    private GHelpMenu helpmenu;
    //associate
    private GMainPanel mainPanel;
    
    // constructor
    public GMenubar() {
        this.setBackground(GRAY);
        this.fileMenu = new GFileMenu("File");
        this.add(fileMenu);

        this.editMenu = new GEditMenu("Edit");
        this.add(editMenu);

        this.graphicMenu = new GGraphicMenu("Graphic");
        this.add(graphicMenu);

        this.imagemenu = new GImageMenu("Image");
        this.add(imagemenu);

        this.layermenu = new GLayerMenu("Layer");
        this.add(layermenu);

        this.typemenu = new GTypeMenu("Type");
        this.add(typemenu);

        this.selectmenu = new GSelectMenu("Select");
        this.add(selectmenu);

        this.filtermenu = new GFilterMenu("Filter");
        this.add(filtermenu);

        this.windowmenu = new GWindowMenu("Window");
        this.add(windowmenu);

        this.helpmenu = new GHelpMenu("Help");
        this.add(helpmenu);
    }

    // method
    // initialize
    public void initialize() {
        fileMenu.initialize();
        editMenu.initialize();
        graphicMenu.initialize();
        imagemenu.initialize();
        layermenu.initialize();
        typemenu.initialize();
        selectmenu.initialize();
        filtermenu.initialize();
        windowmenu.initialize();
        helpmenu.initialize();
    }

	public void associate(GMainPanel mainPanel, GMainFrame mainFrame) {
		this.mainPanel=mainPanel;
        this.fileMenu.associate(mainFrame);
	}
}