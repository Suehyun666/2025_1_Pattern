package menu;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import constants.GMenuConstants.EFilterMenuItem;
public class GFilterMenu extends JMenu{
	private static final long serialVersionUID = 1L;

	public GFilterMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();
		for (EFilterMenuItem eFilterMenuItem : EFilterMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFilterMenuItem.getText());
			menuItem.setActionCommand(eFilterMenuItem.name());
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
			EFilterMenuItem eMenuItem = EFilterMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eBlur:
					break;
				case eNoise:
					break;
				default:
					break;
			}
		}
	}
}
