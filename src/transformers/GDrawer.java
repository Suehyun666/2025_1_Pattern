package transformers;

import java.awt.Graphics2D;
import layers.shapes.GShape;

public class GDrawer extends GTransFormer {
	private GShape shape;
	public GDrawer(GShape gshape) {
		super(gshape);
		this.shape=gshape;
	}
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.shape.setPoint(x, y);
	}
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		this.shape.dragPoint(x, y);
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {
		this.shape.setPoint(x, y);	
	}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}