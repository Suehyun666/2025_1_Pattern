package frame.shape;

import frame.GMainPanel;
import interfaces.DrawingState;

import java.awt.event.MouseEvent;

public class DrawRectangleState implements DrawingState {
    private GMainPanel panel;
    private int startX, startY;

    public DrawRectangleState(GMainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        panel.setCurrentShape(new GRectangle(startX, startY, 0, 0));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int currentX = e.getX();
        int currentY = e.getY();

        int x = Math.min(startX, currentX);
        int y = Math.min(startY, currentY);
        int width = Math.abs(currentX - startX);
        int height = Math.abs(currentY - startY);

        GRectangle rect = (GRectangle) panel.getCurrentShape();
        rect.resize(width, height);
        rect.move(x - startX, y - startY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (panel.getCurrentShape() != null) {
            GRectangle rect = (GRectangle) panel.getCurrentShape();
            if (rect.getwidth() < 5 && rect.getheight() < 5) {
                rect.resize(10, 10);
            }
        }
    }

}