package transformers;

import layers.shapes.GShape;

import java.awt.*;
import java.util.Vector;

public class GRotator extends GTransFormer {
    private GShape shape;
    private Vector<GShape> selectedShapes;
    public GRotator(GShape gshape, Vector<GShape> selectedShapes) {
        super(gshape);
        this.shape=gshape;
        this.selectedShapes = selectedShapes;
    }
    @Override
    public void start(Graphics2D graphics, int x, int y) {
        for (GShape shape : selectedShapes) {
            shape.setmovePoint(x, y);
        }
    }
    @Override
    public void drag(Graphics2D graphics, int x, int y) {
        for (GShape shape : selectedShapes) {
            shape.rotate(x, y);
        }
    }
    @Override
    public void finish(Graphics2D graphics, int x, int y) {

    }
    @Override
    public void addpoint(Graphics2D graphics, int x, int y) {}
}
