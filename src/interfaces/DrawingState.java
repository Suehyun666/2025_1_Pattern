package interfaces;

import java.awt.event.MouseEvent;

public interface DrawingState {
    public void mousePressed(MouseEvent e);
    public void mouseDragged(MouseEvent e);
    public void mouseReleased(MouseEvent e);
}
