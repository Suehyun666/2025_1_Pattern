package shapes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{
	private Ellipse2D.Float ellipse;
	
	public GOval() {
		super(new Ellipse2D.Float(0,0,0,0));
		this.ellipse = (Ellipse2D.Float) this.getShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.ellipse.setFrame(x, y, 0, 0);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		double ox = ellipse.getX();
		double oy = ellipse.getY();
		double ow = x-ox;
		double oh = y-oy;
		this.ellipse.setFrame(ox, oy, ow, oh);
	}

	@Override
	public void addPoint(int x, int y) {

	}
	@Override
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;

		double ex = ellipse.getX();
		double ey = ellipse.getY();
		double ew = ellipse.getWidth();
		double eh = ellipse.getHeight();

		this.ellipse.setFrame(ex + dx, ey + dy, ew, eh);

		this.px = x;
		this.py = y;
	}
	@Override
	public void resize(int x, int y) {
		if (ellipse == null) return;

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
	public void rotate(int x, int y) {
		if (shape == null) return;

		double centerX = ellipse.getX() + ellipse.getWidth() / 2;
		double centerY = ellipse.getY() + ellipse.getHeight() / 2;

		double angle1 = Math.atan2(py - centerY, px - centerX);
		double angle2 = Math.atan2(y - centerY, x - centerX);
		double rotationAngle = angle2 - angle1;

		AffineTransform at = new AffineTransform();
		at.rotate(rotationAngle, centerX, centerY);
		shape = at.createTransformedShape(shape);

		// 타원 참조 업데이트
		if (shape instanceof Ellipse2D) {
			this.ellipse = (Ellipse2D.Float) shape;
		}

		this.px = x;
		this.py = y;
	}

	@Override
	public GShape clone(int x, int y) {
		GOval cloned = new GOval();

		float dx = (float) (x - ellipse.getX());
		float dy = (float) (y - ellipse.getY());

		cloned.ellipse.setFrame(
				x, y,
				ellipse.getWidth(),
				ellipse.getHeight()
		);

		cloned.shape = cloned.ellipse;

		return cloned;
	}
}