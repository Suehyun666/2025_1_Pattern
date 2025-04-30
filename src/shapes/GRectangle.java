package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{
	private Rectangle2D.Float rectangle;
	
	public GRectangle() {
		this.rectangle = new Rectangle2D.Float(0,0,0,0);
		this.shape = this.rectangle;
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.rectangle.setFrame(x, y, 0, 0);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		double ox = rectangle.getX();
		double oy = rectangle.getY();
		double ow = x-ox;
		double oh = y-oy;
		this.rectangle.setFrame(ox, oy, ow, oh);
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		if (shape != null) {
			graphics.draw(shape);
		}
	}
}