package frame.shape;

import interfaces.DrawingState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawOvalState implements DrawingState {
    private Shape shape;
    private JPanel panel;
    public DrawOvalState(JPanel panel) {
        this.panel=panel;
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
