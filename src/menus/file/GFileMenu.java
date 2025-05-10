package menus.file;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;

import constants.GMenuConstants.EFileMenuItem;
import frames.GMainFrame;
import frames.GMainPanel;
import shapes.GShape;

import static java.awt.print.Printable.PAGE_EXISTS;

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
						KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.eSave) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.eSaveAs) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.eOpen) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.eExit) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.ePrint) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));}
			if (eFileMenuItem == EFileMenuItem.eClose) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));}
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
				this.frame.setModified(true);
			}
		}
	}

	//save
	public void save() {
		if (currentFile == null) {
			saveAs();
		} else {
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
			Dimension size = panel.getPreferredSize();
			canvasBounds = new Rectangle(0, 0, size.width, size.height);
		}

		BufferedImage image = new BufferedImage(
				canvasBounds.width, canvasBounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		// 안티앨리어싱 설정
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color bgColor = panel.getCanvasBackground();
		if (bgColor != null && bgColor.getAlpha() > 0) {
			g2d.setColor(bgColor);
			g2d.fillRect(0, 0, canvasBounds.width, canvasBounds.height);
		}

		g2d.translate(-canvasBounds.x, -canvasBounds.y);
		panel.paintShapes(g2d);

		g2d.dispose();
		return image;
	}

	private void saveProjectFile(File file, GMainPanel panel) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(panel.getCanvasBounds());
			oos.writeObject(panel.getCanvasBackground());
			this.frame.setModified(true);
			Vector<GShape> shapes = panel.getShapes();
			oos.writeInt(shapes.size());
			for (GShape shape : shapes) {
				oos.writeObject(shape);
			}
		}
	}

	public void open() {
		GOpenFileDialog dialog = new GOpenFileDialog(frame);
		if (dialog.showDialog()) {
			File file = dialog.getSelectedFile();
			openFile(file);
		}
	}

	private void openFile(File file) {
		try {
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();
			this.frame.setModified(true);
			if (extension.equals("psd")) {
				openProjectFile(file);
			} else {
				openImageFile(file);
			}

			currentFile = file;
			frame.setTitle(file.getName() + " - Drawing Application");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage(),
					"Open Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openProjectFile(File file) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Rectangle canvasBounds = (Rectangle) ois.readObject();
			Color canvasBackground = (Color) ois.readObject();

			GMainPanel panel = frame.getMainPanel();
			panel.setCanvasBounds(canvasBounds);  // 이미 수정됨
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
		if (image == null) {
			throw new IOException("Cannot read image file: " + file.getName());
		}
		GMainPanel panel = frame.getMainPanel();
		panel.createNewCanvas(image.getWidth(), image.getHeight(), "White");
		panel.setBackgroundImage(image);

		System.out.println("Image loaded: " + image.getWidth() + "x" + image.getHeight());
	}

	private void print() {
		if (frame != null) {
			GPrintFileDialog dialog = new GPrintFileDialog(frame);
			if (dialog.showDialog()) {
				try {
					PrinterJob printerJob = dialog.getPrinterJob();
					PageFormat pageFormat = dialog.getPageFormat();

					// Create a printable version of the main panel
					Printable printable = new Printable() {
						@Override
						public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
								throws PrinterException {
							if (pageIndex > 0) {
								return NO_SUCH_PAGE;
							}

							Graphics2D g2d = (Graphics2D) graphics;
							g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

							// Scale the panel to fit the page
							GMainPanel panel = frame.getMainPanel();
							double scaleX = pageFormat.getImageableWidth() / panel.getWidth();
							double scaleY = pageFormat.getImageableHeight() / panel.getHeight();
							double scale = Math.min(scaleX, scaleY);

							g2d.scale(scale, scale);

							// Paint the panel
							panel.paint(g2d);

							return PAGE_EXISTS;
						}
					};

					printerJob.setPrintable(printable, pageFormat);

					if (printerJob.printDialog()) {
						printerJob.print();
					}

				} catch (PrinterException ex) {
					JOptionPane.showMessageDialog(frame,
							"Error printing: " + ex.getMessage(),
							"Print Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void exit(){
		if (!checkSaveorNot()) {
			System.exit(0);
		}
	}

	private boolean checkSaveorNot() {
		boolean bCancel = true;
		if (this.frame.isModified()) {
			int reply = JOptionPane.showConfirmDialog(this.frame, "Do you want to save your changes?");
			if (reply == JOptionPane.OK_OPTION) {
				this.save();
				bCancel = false;
			} else if (reply == JOptionPane.NO_OPTION) {
				this.frame.setModified(false);
				bCancel = false;
			} else if (reply == JOptionPane.CANCEL_OPTION) {
				// 암모것도 안함
				bCancel = true;
			} else {
				bCancel = true;
			}
		} else {
			bCancel = false;
		}
		return bCancel;
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
				case eOpen:
					open();
					break;
				case ePrint:
					print();
					break;
				case eExit:
					exit();
				default:
					break;
			}
		}
	}

}
