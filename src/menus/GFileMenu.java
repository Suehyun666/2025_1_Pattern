package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GMenuConstants.EFileMenuItem;


public class GFileMenu extends JMenu {
	//
	private static final long serialVersionUID = 1L;

	//constructor
	public GFileMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
			menuItem.setActionCommand(eFileMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
		}
		
	}
	//methods
	
	//initialize
	public void initialize() {}
	//new
	public void create() {}
	//open
	public void open() {}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
			case eNew:
				create();
				break;
			
			default:
				break;
			}
		}
	}

}
