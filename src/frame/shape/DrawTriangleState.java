package frame.shape;

import interfaces.DrawingState;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DrawTriangleState implements DrawingState {
    private JPanel panel;
    public DrawTriangleState(JPanel panel) {
        this.panel = panel;
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
}
