package menus.file;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.GMenuConstants.EFileMenuItem;
import frames.GMainFrame;
import frames.GMainPanel;
import shapes.GShape;


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
		try {
			GSaveFileDialog.FileFormat format = getFormatFromExtension(file.getName());
			GMainPanel panel = frame.getMainPanel();

			switch (format) {
				case PNG:
					savePNG(file, panel);
					break;
				case JPG:
					saveJPEG(file, panel);
					break;
				case BMP:
					saveBMP(file, panel);
					break;
				case GIF:
					saveGIF(file, panel);
					break;
				case PSD:
					savePSD(file, panel);
					break;
				default:
					savePNG(file, panel);
			}

			// 저장 성공 후 수정 플래그 초기화
			frame.setModified(false);
			JOptionPane.showMessageDialog(frame, "File saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage(),
					"Save Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private GSaveFileDialog.FileFormat getFormatFromExtension(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		for (GSaveFileDialog.FileFormat format : GSaveFileDialog.FileFormat.values()) {
			if (format.getExtension().equals(extension)) {
				return format;
			}
		}
		return GSaveFileDialog.FileFormat.PNG; // 기본값
	}

	// PNG 저장 (가장 일반적)
	private void savePNG(File file, GMainPanel panel) throws IOException {
		BufferedImage image = createImageFromPanel(panel);
		ImageIO.write(image, "PNG", file);
	}

	// JPEG 저장 (품질 설정 포함)
	private void saveJPEG(File file, GMainPanel panel) throws IOException {
		BufferedImage image = createImageFromPanel(panel);

		// JPEG는 투명도를 지원하지 않으므로 흰색 배경 추가
		BufferedImage jpegImage = new BufferedImage(
				image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = jpegImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();

		ImageIO.write(jpegImage, "JPEG", file);
	}

	// BMP 저장
	private void saveBMP(File file, GMainPanel panel) throws IOException {
		BufferedImage image = createImageFromPanel(panel);
		ImageIO.write(image, "BMP", file);
	}

	// GIF 저장
	private void saveGIF(File file, GMainPanel panel) throws IOException {
		BufferedImage image = createImageFromPanel(panel);
		ImageIO.write(image, "GIF", file);
	}

	// PSD 저장 (간단한 버전 - 실제로는 복잡한 포맷)
	private void savePSD(File file, GMainPanel panel) throws IOException {
		// PSD 포맷은 복잡하므로 자체 포맷으로 저장
		saveProjectFile(file, panel);
	}

	// 패널을 이미지로 변환
	private BufferedImage createImageFromPanel(GMainPanel panel) {
		Rectangle canvasBounds = panel.getCanvasBounds();
		if (canvasBounds == null) {
			// 캔버스 영역이 없으면 전체 패널
			Dimension size = panel.getPreferredSize();
			canvasBounds = new Rectangle(0, 0, size.width, size.height);
		}

		BufferedImage image = new BufferedImage(
				canvasBounds.width, canvasBounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		// 안티앨리어싱 설정
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 배경 그리기
		Color bgColor = panel.getCanvasBackground();
		if (bgColor != null && bgColor.getAlpha() > 0) {
			g2d.setColor(bgColor);
			g2d.fillRect(0, 0, canvasBounds.width, canvasBounds.height);
		}

		// 도형 그리기
		g2d.translate(-canvasBounds.x, -canvasBounds.y);
		panel.paintShapes(g2d);

		g2d.dispose();
		return image;
	}

	// 프로젝트 파일 저장 (도형 정보 포함)
	private void saveProjectFile(File file, GMainPanel panel) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			// 캔버스 정보 저장
			oos.writeObject(panel.getCanvasBounds());
			oos.writeObject(panel.getCanvasBackground());

			// 도형 정보 저장
			Vector<GShape> shapes = panel.getShapes();
			oos.writeInt(shapes.size());
			for (GShape shape : shapes) {
				oos.writeObject(shape);
			}
		}
	}

	public void open() {
		JFileChooser fileChooser = new JFileChooser();

		// 파일 필터 추가
		FileNameExtensionFilter allFilter = new FileNameExtensionFilter(
				"All Supported Formats", "psd", "png", "jpg", "jpeg", "bmp", "gif");
		fileChooser.addChoosableFileFilter(allFilter);
		fileChooser.setFileFilter(allFilter);

		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			openFile(file);
		}
	}

	private void openFile(File file) {
		try {
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();

			if (extension.equals("psd")) {
				openProjectFile(file);
			} else {
				openImageFile(file);
			}

			currentFile = file;
			frame.setTitle(file.getName() + " - Drawing Application");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage(),
					"Open Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openProjectFile(File file) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Rectangle canvasBounds = (Rectangle) ois.readObject();
			Color canvasBackground = (Color) ois.readObject();

			GMainPanel panel = frame.getMainPanel();
			panel.setCanvasBounds(canvasBounds);
			panel.setCanvasBackground(canvasBackground);

			int shapeCount = ois.readInt();
			Vector<GShape> shapes = new Vector<>();
			for (int i = 0; i < shapeCount; i++) {
				shapes.add((GShape) ois.readObject());
			}
			panel.setShapes(shapes);
			panel.repaint();
		}
	}

	private void openImageFile(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		GMainPanel panel = frame.getMainPanel();

		// 이미지를 새 캔버스로 설정
		panel.createNewCanvas(image.getWidth(), image.getHeight(), "White");
		panel.setBackgroundImage(image);
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
