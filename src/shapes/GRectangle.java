package shapes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape {
	private Rectangle2D rectangle;

	public GRectangle() {
		super(new Rectangle2D.Float(0,0,0,0));
		this.rectangle = (Rectangle2D) this.getShape();
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
	public void addPoint(int x, int y) {}

	@Override
	public void movePoint(int x, int y) {
		int dx=x-px;
		int dy=y-py;
		this.rectangle.setFrame(
				rectangle.getX()+dx, rectangle.getY()+dy,
				rectangle.getWidth(), rectangle.getHeight());
		this.px=x;
		this.py=y;
	}

	@Override
	public void rotate(int x, int y) {
		if (shape == null) return;

		double centerX = rectangle.getX() + rectangle.getWidth() / 2;
		double centerY = rectangle.getY() + rectangle.getHeight() / 2;

		double angle1 = Math.atan2(py - centerY, px - centerX);
		double angle2 = Math.atan2(y - centerY, x - centerX);
		double rotationAngle = angle2 - angle1;

		AffineTransform at = new AffineTransform();
		at.rotate(rotationAngle, centerX, centerY);
		shape = at.createTransformedShape(shape);
		rectangle = (Rectangle2D) shape;

		this.px = x;
		this.py = y;
	}

	@Override
	public void resize(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		double width = rectangle.getWidth() + dx;
		double height = rectangle.getHeight() + dy;
		width = Math.max(5, width);
		height = Math.max(5, height);

		this.rectangle.setFrame(
				rectangle.getX(), rectangle.getY(),
				width, height
		);
		this.px = x;
		this.py = y;
	}

	@Override
	public GShape clone(int x, int y) {
		return null;
	}

	@Override
	public GShape clone() {
		GRectangle cloned = new GRectangle();
		cloned.rectangle = (Rectangle2D.Float) this.rectangle.clone();
		cloned.shape = cloned.rectangle;
		return cloned;
	}
}