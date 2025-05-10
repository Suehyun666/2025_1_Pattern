package transformers;

import shapes.GShape;
import shapes.GShape.EAnchors;

import java.awt.*;

public class GResizer extends GTransFormer{
    private GShape shape;
    private EAnchors anchor;
    public GResizer(GShape gshape, GShape.EAnchors anchor) {
        super(gshape);
        this.shape = gshape;
        this.anchor =anchor;
    }
    @Override
    public void start(Graphics2D graphics, int x, int y) {
        this.shape.setmovePoint(x, y);
    }
    @Override
    public void drag(Graphics2D graphics, int x, int y) {
        this.shape.resize(x, y);
    }
    @Override
    public void finish(Graphics2D graphics, int x, int y) {

    }
    @Override
    public void addpoint(Graphics2D graphics, int x, int y) {}
}
