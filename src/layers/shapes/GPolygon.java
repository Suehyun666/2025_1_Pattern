package layers.shapes;

import java.awt.*;

public class GPolygon extends GShape {
	private Polygon polygon;
	private int tempX, tempY; 
	
	public GPolygon() {
        super(new Polygon());
		this.polygon = (Polygon) this.getShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.polygon.addPoint(x, y);
		this.tempX = x;
		this.tempY = y;
	}
	
	@Override
	public void dragPoint(int x, int y) {
		this.tempX = x;
		this.tempY = y;
	}
	@Override
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
		this.tempX = x;
		this.tempY = y;
	}

	@Override
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		if (this.polygon != null && this.polygon.npoints > 0) {
			for (int i = 0; i < this.polygon.npoints; i++) {
				this.polygon.xpoints[i] += dx;
				this.polygon.ypoints[i] += dy;
			}
			this.tempX += dx;
			this.tempY += dy;
			this.polygon.invalidate();
		}
		this.px = x;
		this.py = y;
	}

	@Override
	public void resize(int x, int y) {
		if (this.polygon == null || this.polygon.npoints <= 0) return;

		int dx = x - px;
		int dy = y - py;

		int centerX = 0, centerY = 0;
		for (int i = 0; i < this.polygon.npoints; i++) {
			centerX += this.polygon.xpoints[i];
			centerY += this.polygon.ypoints[i];
		}
		centerX /= this.polygon.npoints;
		centerY /= this.polygon.npoints;

		for (int i = 0; i < this.polygon.npoints; i++) {
			int relX = this.polygon.xpoints[i] - centerX;
			int relY = this.polygon.ypoints[i] - centerY;

			double scaleX = 1.0 + (double)dx / 100.0;
			double scaleY = 1.0 + (double)dy / 100.0;

			this.polygon.xpoints[i] = centerX + (int)(relX * scaleX);
			this.polygon.ypoints[i] = centerY + (int)(relY * scaleY);
		}

		this.polygon.invalidate();
		this.px = x;
		this.py = y;
	}

	@Override
	protected Shape createShape() {
		return (Shape) new GPolygon();
	}

	@Override
	public void rotate(int x, int y) {
		if (this.polygon == null || this.polygon.npoints <= 0) return;

		int centerX = 0, centerY = 0;
		for (int i = 0; i < this.polygon.npoints; i++) {
			centerX += this.polygon.xpoints[i];
			centerY += this.polygon.ypoints[i];
		}
		centerX /= this.polygon.npoints;
		centerY /= this.polygon.npoints;

		double angle1 = Math.atan2(py - centerY, px - centerX);
		double angle2 = Math.atan2(y - centerY, x - centerX);
		double rotationAngle = angle2 - angle1;

		for (int i = 0; i < this.polygon.npoints; i++) {
			int relX = this.polygon.xpoints[i] - centerX;
			int relY = this.polygon.ypoints[i] - centerY;

			double cos = Math.cos(rotationAngle);
			double sin = Math.sin(rotationAngle);
			double newX = relX * cos - relY * sin;
			double newY = relX * sin + relY * cos;

			this.polygon.xpoints[i] = centerX + (int)newX;
			this.polygon.ypoints[i] = centerY + (int)newY;
		}

		this.polygon.invalidate();
		this.px = x;
		this.py = y;
	}
	public GShape clone(int x, int y) {
		GPolygon cloned = new GPolygon();

		for (int i = 0; i < this.polygon.npoints; i++) {
			cloned.polygon.addPoint(this.polygon.xpoints[i], this.polygon.ypoints[i]);
		}
		cloned.tempX = this.tempX;
		cloned.tempY = this.tempY;

		return cloned;
	}
}