package menu;

import javax.swing.*;
import constants.GMenuConstants.ETypeMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GTypeMenu extends JMenu{
	private static final long serialVersionUID = 1L;

	public GTypeMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();
		for (ETypeMenuItem eTypeMenuItem : ETypeMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eTypeMenuItem.getText());
			menuItem.setActionCommand(eTypeMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
		}
	}

	public void initialize() {}
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ETypeMenuItem eMenuItem = ETypeMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case ePanel:
					break;
				default:
					break;
			}
		}
	}
}
