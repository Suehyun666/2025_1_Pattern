package interfaces;

import java.awt.*;

public interface GShape {
    // layer
    public void draw(Graphics2D g);
    public void setColor(Color color);
    public Color getColor();
    public int getwidth();
    public int getheight();
    public void move(int dx, int dy);
    public void resize(int width, int height);
    public void rotate(double angle);
    public boolean contains(int x, int y);
}
