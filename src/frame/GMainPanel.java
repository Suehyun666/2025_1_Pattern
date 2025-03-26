package frame;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage canvasImage;

    public GMainPanel() {
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
    }

    public void createNewCanvas(int width, int height) {
        this.removeAll();
        // new image
        canvasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // set BackGround
        Graphics2D g2d = canvasImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        // mouse listener
        setupMouseListeners();
        // reset
        this.revalidate();
        this.repaint();
    }

    // 마우스 리스너 설정
    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

            }
        };

    }

    private void setupKeyboardListeners() {}

    public Image getCanvasImage() {
        return this.canvasImage;
    }

    public void initialize() {
    }

}