package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape {
	private Rectangle2D.Float rectangle;
	private int startX, startY; // ������ ����

	public GRectangle() {
		this.rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		this.shape = this.rectangle;
	}

	@Override
	public void setPoint(int x, int y) {
		// ������ ����
		this.startX = x;
		this.startY = y;
		this.rectangle.setFrame(x, y, 0, 0);
	}

	@Override
	public void dragPoint(int x, int y) {
		// �巡�� ���⿡ ���� �ùٸ� �簢�� ��ǥ�� ũ�� ���
		float newX = (x < startX) ? x : startX;
		float newY = (y < startY) ? y : startY;
		float newWidth = Math.abs(x - startX);
		float newHeight = Math.abs(y - startY);

		// �簢�� ������Ʈ
		this.rectangle.setFrame(newX, newY, newWidth, newHeight);
	}

	@Override
	public void draw(Graphics2D graphics) {
		if (shape != null) {
			graphics.draw(shape);
		}
	}

	@Override
	public GShape clone() {
		GRectangle cloned = new GRectangle();
		cloned.rectangle = (Rectangle2D.Float) this.rectangle.clone();
		cloned.shape = cloned.rectangle;
		return cloned;
	}
}