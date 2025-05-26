package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GCanvas extends JPanel {
    private BufferedImage offscreenBuffer;

    protected void paintComponent(Graphics g) {
        if(offscreenBuffer == null) {
            offscreenBuffer = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2d = offscreenBuffer.createGraphics();
        g.drawImage(offscreenBuffer, 0, 0, null);
    }
}

