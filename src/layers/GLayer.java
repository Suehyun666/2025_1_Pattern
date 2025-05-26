package layers;

import layers.shapes.GShape;

import java.awt.*;
import java.util.*;

public class GLayer {
    private String name;
    private boolean visible;
    private float opacity;
    private Vector<GShape> shapes = new Vector<>();

    public void addShape(GShape shape) {
        this.shapes.add(shape);
        shape.setLayer(this);
    }

    public void draw(Graphics2D g2d) {
        if (visible) {
                // 투명도 설정 (필요한 경우)
            for (GShape shape : shapes) {
                shape.draw(g2d);
            }
        }
    }
    public void setopacity(float opacity) {
        this.opacity = opacity;
    }
    public float getopacity() {
        return opacity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}