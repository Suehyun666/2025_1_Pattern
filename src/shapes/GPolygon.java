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
		// null 체크 추가
		if (shape == null) return;
		
		graphics.draw(shape);
		
		// 폴리곤 점이 비어있는지 체크
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
		// 다각형의 모든 점을 이동
		if (this.polygon != null && this.polygon.npoints > 0) {
			for (int i = 0; i < this.polygon.npoints; i++) {
				this.polygon.xpoints[i] += dx;
				this.polygon.ypoints[i] += dy;
			}
			
			// tempX, tempY도 함께 이동
			this.tempX += dx;
			this.tempY += dy;
			
			// Polygon은 내부 데이터가 변경되어도 bounds를 자동으로 업데이트하지 않으므로
			// 다음 호출로 bounds를 업데이트
			this.polygon.invalidate();
		}
	}
}