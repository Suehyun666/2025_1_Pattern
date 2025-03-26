package menu;

import constants.GMenuConstants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import constants.GMenuConstants.EEditMenuItem;

public class GEditMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    // constructor
    public GEditMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        for (EEditMenuItem eEDitMenuItem : EEditMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eEDitMenuItem.getText());
            menuItem.setActionCommand(eEDitMenuItem.name());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }
    }

    // method
    public void initialize() {

    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GMenuConstants.EEditMenuItem eMenuItem = GMenuConstants.EEditMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eProperty:
                    break;
                case eUndo:
                    break;
                case eForward:
                    break;
                case eBackward:
                    break;
                case eFade:
                    break;
                case eCopy:
                    break;
                case ePaste:
                    break;
                case eCut:
                    break;
                case eFill:
                    break;
                case eClear:
                    break;
                case eColorSetting:
                    break;
                default:
                    break;
            }
        }
    }
}