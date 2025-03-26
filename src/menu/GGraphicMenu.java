package menu;

import constants.GMenuConstants.EGraphicMenuItem;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GGraphicMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    // constructor
    public GGraphicMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();
        for (EGraphicMenuItem eGraphicMenuItem : EGraphicMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eGraphicMenuItem.getText());
            menuItem.setActionCommand(eGraphicMenuItem.getText());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }
    }
    //initialize
	public void initialize() {
		
	}
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EGraphicMenuItem eMenuItem = EGraphicMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eFontStyle:
                    break;
                case eLineStyle:
                    break;
                case eLineThickness:
                    break;
                case eFontSize:
                    break;
                default:
                    break;
            }
        }
    }
}