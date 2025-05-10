package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GLine extends GShape {
	private Line2D.Float line;
	
	public GLine() {
		super(new Line2D.Float(0, 0, 0, 0));
		this.line = (Line2D.Float) this.getShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.line.setLine(x, y, x, y);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		float x1 = this.line.x1;
		float y1 = this.line.y1;
		this.line.setLine(x1, y1, x, y);
	}

	@Override
	public void addPoint(int x, int y) {

	}
	
	@Override
	public boolean contains(int x, int y) {
		if (line == null) return false;
		return line.ptSegDist(x, y) <= 5.0;
	}
	@Override
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		if (line == null) return;

		float x1 = line.x1 + dx;
		float y1 = line.y1 + dy;
		float x2 = line.x2 + dx;
		float y2 = line.y2 + dy;

		line.setLine(x1, y1, x2, y2);
		this.shape = this.line;
		this.px = x;
		this.py = y;
	}

	@Override
	public void resize(int x, int y) {
		if (line == null) return;
		line.x2 = x;
		line.y2 = y;

		this.shape = this.line;
		this.px = x;
		this.py = y;
	}

	@Override
	public void rotate(int x, int y) {
		if (line == null) return;

		float centerX = line.x1;
		float centerY = line.y1;
		double angle = Math.atan2(y - centerY, x - centerX);
		double distance = Math.sqrt(
				Math.pow(line.x2 - line.x1, 2) +
						Math.pow(line.y2 - line.y1, 2)
		);
		float newX2 = (float)(centerX + distance * Math.cos(angle));
		float newY2 = (float)(centerY + distance * Math.sin(angle));

		line.x2 = newX2;
		line.y2 = newY2;

		this.shape = this.line;
		this.px = x;
		this.py = y;
	}

	@Override
	public GShape clone(int x, int y) {
		GLine cloned = new GLine();
		float dx = x - line.x1;
		float dy = y - line.y1;

		cloned.line.x1 = x;
		cloned.line.y1 = y;
		cloned.line.x2 = line.x2 + dx;
		cloned.line.y2 = line.y2 + dy;
		cloned.shape = cloned.line;

		return cloned;
	}
}