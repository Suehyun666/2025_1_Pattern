package frame.state;

import frame.GMainPanel;
import interfaces.DrawingState;
import interfaces.GShape;

import java.awt.event.MouseEvent;

public class RotateState implements DrawingState {
    private GMainPanel panel;
    private GShape selectedShape;
    private int lastX, lastY;

    public RotateState(GMainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedShape = panel.findShapeAt(e.getX(), e.getY());
        if (selectedShape != null) {
            lastX = e.getX();
            lastY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedShape != null) {
            int dx = e.getX() - lastX;
            int dy = e.getY() - lastY;

            selectedShape.move(dx, dy);
            lastX = e.getX();
            lastY = e.getY();
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selectedShape = null;
    }
}