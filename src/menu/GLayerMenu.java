package menu;

import constants.GMenuConstants.ELayerMenuItem;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GLayerMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	public GLayerMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (ELayerMenuItem eLayerMenuItem : ELayerMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eLayerMenuItem.getText());
			menuItem.setActionCommand(eLayerMenuItem.name());
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
			ELayerMenuItem eMenuItem = ELayerMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eDeleteLayer:
					break;
				case eDuplicate:
					break;
				case eHide:
					break;
				case eLock:
					break;
				case eMerge:
					break;
				case eNewLayer:
					break;
				case eRenameLayer:
					break;
				default:
					break;
			}
		}
	}
}
