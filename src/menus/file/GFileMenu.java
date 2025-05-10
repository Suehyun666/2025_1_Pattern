package menus.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import constants.GMenuConstants.EFileMenuItem;
import frames.GMainFrame;


public class GFileMenu extends JMenu {
	//
	private GMainFrame frame;
	private static final long serialVersionUID = 1L;

	//constructor
	public GFileMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
			menuItem.setActionCommand(eFileMenuItem.name());
			menuItem.addActionListener(actionHandler);
			if (eFileMenuItem == EFileMenuItem.eNew) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

			}
			this.add(menuItem);
		}
		
	}
	//associate
	public void associate(GMainFrame mainFrame) {
		this.frame = mainFrame;
	}
	//methods
	
	//initialize
	public void initialize() {}
	//new
	public void create() {
		if (frame != null) {
			GNewFileDialog dialog = new GNewFileDialog(frame);
			if (dialog.showDialog()) {
				String fileName = dialog.getFileName();
				int width = dialog.getImageWidth();
				int height = dialog.getImageHeight();
				String background = dialog.getBackgroundContent();
				frame.setTitle(fileName);
				frame.getMainPanel().createNewCanvas(width, height, background);
			}
		}
	}
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
