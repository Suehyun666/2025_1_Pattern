package menu;

import constants.GMenuConstants.EImageMenuItem;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GImageMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public GImageMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (EImageMenuItem eImageMenuItem : EImageMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eImageMenuItem.getText());
			menuItem.setActionCommand(eImageMenuItem.name());
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
			EImageMenuItem eMenuItem = EImageMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eCanvasSize:
					break;
				case eCrop:
					break;
				case eDuplicate:
					break;
				case eImageSize:
					break;
				case eMode:
					break;
				case eRotate:
					break;
				case eTrim:
					break;
				default:
					break;
			}
		}
	}
}
