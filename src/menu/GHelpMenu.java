package menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GMenuConstants;
import constants.GMenuConstants.EHelpMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GHelpMenu extends JMenu{
	// attributes
    private static final long serialVersionUID = 1L;
    
    private JMenuItem systeminfo;
    private JMenuItem about;
    private JMenuItem online;
    
    // constructor
    public GHelpMenu(String label) {
        super(label);
        ActionHandler actionHandler = new ActionHandler();
        for (EHelpMenuItem eHelpMenuItem : EHelpMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eHelpMenuItem.getText());
            menuItem.setActionCommand(eHelpMenuItem.name());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }
    }
    
    // method
    public void initialize() {
        // Add action listeners here
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EHelpMenuItem eMenuItem = EHelpMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eAbout:
                    break;
                case eOnline:
                    break;
                case eSystemInfo:
                    break;
                default:
                    break;
            }
        }
    }
}
