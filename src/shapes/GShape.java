package shapes;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class GShape {
	protected Shape shape;
	public GShape() {}
	
	public void setPoint(int x, int y) {
	}
	
	public void dragPoint(int x, int y) {
	}
	
	public void draw(Graphics2D graphics) {
		// null 체크 추가
		if (shape != null) {
			graphics.draw(shape);
		}
	}
	
	// 도형이 특정 좌표를 포함하는지 확인하는 메서드
	public boolean contains(int x, int y) {
		if (shape == null) return false;
		return shape.contains(x, y);
	}
	
	// 도형이 다른 도형과 교차하는지 확인하는 메서드
	public boolean intersects(GShape other) {
		if (shape == null || other == null || other.shape == null) return false;
		Rectangle thisBounds = shape.getBounds();
		Rectangle otherBounds = other.shape.getBounds();
		return thisBounds.intersects(otherBounds);
	}
	
	// 도형을 이동시키는 메서드
	public void move(int dx, int dy) {
		if (shape == null) return;
		
		// AffineTransform을 사용해 도형 이동
		AffineTransform at = new AffineTransform();
		at.translate(dx, dy);
		shape = at.createTransformedShape(shape);
	}
}