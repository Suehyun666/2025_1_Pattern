package menus.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.*;

import constants.GMenuConstants.EFileMenuItem;
import frames.GMainFrame;


public class GFileMenu extends JMenu {
	//component
	private File currentFile;
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
			}if (eFileMenuItem == EFileMenuItem.eSave) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
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

	//save
	public void save() {
		if (currentFile == null) {
			saveAs();
		} else {
			// 현재 파일에 저장
			saveToFile(currentFile);
		}
	}
	public void saveAs() {
		if (frame != null) {
			String defaultName = frame.getTitle();
			if (defaultName.contains(" - ")) {
				defaultName = defaultName.substring(0, defaultName.indexOf(" - "));
			}

			GSaveFileDialog dialog = new GSaveFileDialog(frame, defaultName);
			if (dialog.showDialog()) {
				currentFile = dialog.getSelectedFile();
				GSaveFileDialog.FileFormat format = dialog.getSelectedFormat();

				// 실제 저장 로직 (나중에 구현)
				saveToFile(currentFile);

				// 타이틀 업데이트
				frame.setTitle(currentFile.getName() + " - Drawing Application");
			}
		}
	}

	private void saveToFile(File file) {
		// TODO: 실제 저장 로직 구현
		// format에 따라 다른 저장 방식 적용
		System.out.println("Saving to: " + file.getAbsolutePath());
	}


	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eNew:
					create();
					break;
				case eSave:
					save();
					break;
				case eSaveAs:
					saveAs();
					break;
				default:
					break;
			}
		}
	}

}
