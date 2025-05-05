package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape {
	private Rectangle2D.Float rectangle;
	private int startX, startY; // 시작점 저장

	public GRectangle() {
		this.rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		this.shape = this.rectangle;
	}

	@Override
	public void setPoint(int x, int y) {
		// 시작점 저장
		this.startX = x;
		this.startY = y;
		this.rectangle.setFrame(x, y, 0, 0);
	}

	@Override
	public void dragPoint(int x, int y) {
		// 드래그 방향에 따라 올바른 사각형 좌표와 크기 계산
		float newX = (x < startX) ? x : startX;
		float newY = (y < startY) ? y : startY;
		float newWidth = Math.abs(x - startX);
		float newHeight = Math.abs(y - startY);

		// 사각형 업데이트
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