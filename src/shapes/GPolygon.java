package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;

public class GPolygon extends GShape {
	private Polygon polygon;
	private int tempX, tempY; 
	
	public GPolygon() {
		this.polygon = new Polygon();
		this.shape = this.polygon;
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
	public void draw(Graphics2D graphics) {
		// null üũ �߰�
		if (shape == null) return;
		
		graphics.draw(shape);
		
		// ������ ���� ����ִ��� üũ
		if (this.polygon != null && this.polygon.npoints > 0) {
			int lastX = this.polygon.xpoints[this.polygon.npoints - 1];
			int lastY = this.polygon.ypoints[this.polygon.npoints - 1];
			graphics.drawLine(lastX, lastY, tempX, tempY);
		}
	}
	
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
		this.tempX = x;
		this.tempY = y;
	}
	
	@Override
	public void move(int dx, int dy) {
		// �ٰ����� ��� ���� �̵�
		if (this.polygon != null && this.polygon.npoints > 0) {
			for (int i = 0; i < this.polygon.npoints; i++) {
				this.polygon.xpoints[i] += dx;
				this.polygon.ypoints[i] += dy;
			}
			
			// tempX, tempY�� �Բ� �̵�
			this.tempX += dx;
			this.tempY += dy;
			
			// Polygon�� ���� �����Ͱ� ����Ǿ bounds�� �ڵ����� ������Ʈ���� �����Ƿ�
			// ���� ȣ��� bounds�� ������Ʈ
			this.polygon.invalidate();
		}
	}
}