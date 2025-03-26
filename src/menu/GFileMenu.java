package menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.GMenuConstants.EFileMenuItem;
import frame.GMainFrame;
import frame.GMainPanel;
import frame.GNewDocumentDialog;


public class GFileMenu extends JMenu {
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
	public void create() {
		// 메인 프레임 가져오기
		Frame mainFrame = (Frame) SwingUtilities.getWindowAncestor(this);
		if (mainFrame instanceof GMainFrame) {
			GMainFrame gMainFrame = (GMainFrame) mainFrame;

			// 새 문서 다이얼로그 띄우기
			GNewDocumentDialog dialog = new GNewDocumentDialog(mainFrame);
			dialog.setVisible(true);

			// 다이얼로그에서 확인 버튼을 눌렀을 경우
			if (dialog.isConfirmed()) {
				Dimension size = dialog.getDocumentSize();
				System.out.println(size.width + "x" + size.height);
				// 메인 패널 초기화 (새 캔버스 생성)
				GMainPanel mainPanel = gMainFrame.getMainPanel();
				mainPanel.createNewCanvas(size.width, size.height);
			}
		}
	}

	//open
	public void open() {
		// 메인 프레임 가져오기
		Frame mainFrame = (Frame) SwingUtilities.getWindowAncestor(this);
		if (mainFrame instanceof GMainFrame) {
			GMainFrame gMainFrame = (GMainFrame) mainFrame;

			// 파일 선택 다이얼로그 생성
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("이미지 열기");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			// 이미지 파일 필터 설정
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"이미지 파일 (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
					"jpg", "jpeg", "png", "gif", "bmp");
			fileChooser.setFileFilter(filter);

			// 다이얼로그 표시 및 결과 처리
			int result = fileChooser.showOpenDialog(mainFrame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				try {
					// 이미지 로드
					BufferedImage loadedImage = ImageIO.read(selectedFile);

					// 현재 캔버스가 없으면 새로 생성
					GMainPanel mainPanel = gMainFrame.getMainPanel();
					if (mainPanel.getCanvasImage() == null) {
						// 이미지 크기에 맞춰 새 캔버스 생성
						mainPanel.createNewCanvas(loadedImage.getWidth(), loadedImage.getHeight());
					}

					// 이미지를 레이어로 추가
					addImageAsLayer(gMainFrame, loadedImage, selectedFile.getName());

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(mainFrame,
							"이미지를 로드하는 중 오류가 발생했습니다: " + ex.getMessage(),
							"오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	//import
	public void importfile(){

	}
	//save
	public void save(){

	}
	//save as
	public void saveas(){

	}

	//출력
	public void printFile(){
		
	}

	private void addImageAsLayer(GMainFrame mainFrame, BufferedImage image, String fileName) {
		// 화면 갱신
		mainFrame.getMainPanel().repaint();
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eNew:
					create();
					break;
				case eOpen:
					open();
					break;
				case eSave:
					save();
					break;
				case eSaveAs:
					saveas();
					break;
				case ePrint:
					printFile();
					break;
				case eExit:
					//저장안됐으면 확인하는 로직
					System.exit(0);
					break;
				default:
					break;
			}
		}
	}
}