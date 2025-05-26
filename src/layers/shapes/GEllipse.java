package layers.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
	private Ellipse2D ellipse;
	
	public GEllipse() {
		super(new Ellipse2D.Float(0, 0, 0, 0));
		this.ellipse = (Ellipse2D) this.getShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.ellipse.setFrame(x, y, 0, 0);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		double ox = ellipse.getX();
		double oy = ellipse.getY();
		double width = x - ox;
		double height = y - oy;
		this.ellipse.setFrame(ox, oy, width, height);
	}

	@Override
	public void addPoint(int x, int y) {

	}
	@Override
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		if (ellipse == null) return;

		double ox = ellipse.getX() + dx;
		double oy = ellipse.getY() + dy;
		double width = ellipse.getWidth();
		double height = ellipse.getHeight();
		ellipse.setFrame(ox, oy, width, height);

		this.shape = this.ellipse;
		this.px = x;
		this.py = y;
	}
	@Override
	public void resize(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		double width = ellipse.getWidth() + dx;
		double height = ellipse.getHeight() + dy;
		width = Math.max(5, width);
		height = Math.max(5, height);

		ellipse.setFrame(
				ellipse.getX(), ellipse.getY(),
				width, height
		);
		this.px = x;
		this.py = y;
	}

	@Override
	protected Shape createShape() {
		return (Shape) new GEllipse();
	}

	@Override
	public void rotate(int x, int y) {
		double centerX = ellipse.getX() + ellipse.getWidth() / 2;
		double centerY = ellipse.getY() + ellipse.getHeight() / 2;
		double angle1 = Math.atan2(py - centerY, px - centerX);
		double angle2 = Math.atan2(y - centerY, x - centerX);
		double rotationAngle = angle2 - angle1;

		AffineTransform at = new AffineTransform();
		at.rotate(rotationAngle, centerX, centerY);
		shape = at.createTransformedShape(shape);
		this.ellipse = (Ellipse2D) shape;

		this.px = x;
		this.py = y;
	}

	public GShape clone(int x, int y) {
		GEllipse cloned = new GEllipse();
		cloned.ellipse = (Ellipse2D) this.ellipse.clone();
		cloned.shape = cloned.ellipse;
		cloned.movePoint(x, y);
		return cloned;
	}
}