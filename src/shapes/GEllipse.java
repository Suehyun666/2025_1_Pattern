package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
	private Ellipse2D.Float ellipse;
	
	public GEllipse() {
		this.ellipse = new Ellipse2D.Float(0, 0, 0, 0);
		this.shape = this.ellipse;
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
	public void draw(Graphics2D graphics) {
		if (shape != null) {
			graphics.draw(shape);
		}
	}
	
	@Override
	public void move(int dx, int dy) {
		if (ellipse == null) return;
		
		// 직접 타원의 위치를 조정
		double x = ellipse.getX() + dx;
		double y = ellipse.getY() + dy;
		double width = ellipse.getWidth();
		double height = ellipse.getHeight();
		
		ellipse.setFrame(x, y, width, height);
		// shape 참조도 업데이트
		this.shape = this.ellipse;
	}
}