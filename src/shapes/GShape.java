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
		// null üũ �߰�
		if (shape != null) {
			graphics.draw(shape);
		}
	}
	
	// ������ Ư�� ��ǥ�� �����ϴ��� Ȯ���ϴ� �޼���
	public boolean contains(int x, int y) {
		if (shape == null) return false;
		return shape.contains(x, y);
	}
	
	// ������ �ٸ� ������ �����ϴ��� Ȯ���ϴ� �޼���
	public boolean intersects(GShape other) {
		if (shape == null || other == null || other.shape == null) return false;
		Rectangle thisBounds = shape.getBounds();
		Rectangle otherBounds = other.shape.getBounds();
		return thisBounds.intersects(otherBounds);
	}
	
	// ������ �̵���Ű�� �޼���
	public void move(int dx, int dy) {
		if (shape == null) return;
		
		// AffineTransform�� ����� ���� �̵�
		AffineTransform at = new AffineTransform();
		at.translate(dx, dy);
		shape = at.createTransformedShape(shape);
	}

    protected Rectangle getBounds() {
		return shape.getBounds();
    }
	public GShape clone() {
		return new GShape();
	}
}