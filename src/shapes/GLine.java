package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GLine extends GShape {
	private Line2D.Float line;
	
	public GLine() {
		this.line = new Line2D.Float(0, 0, 0, 0);
		this.shape = this.line;
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
	public void draw(Graphics2D graphics) {
		if (shape != null) {
			graphics.draw(shape);
		}
	}
	
	@Override
	public boolean contains(int x, int y) {
		if (line == null) return false;
		// 선의 경우 정확한 위치 대신 일정 거리 내에 있는지 확인
		// ptSegDist: 점과 선 사이의 거리를 계산
		return line.ptSegDist(x, y) <= 5.0; // 5픽셀 이내면 선택으로 간주
	}
	
	@Override
	public void move(int dx, int dy) {
		if (line == null) return;
		
		// 직접 선의 양 끝점을 조정
		float x1 = line.x1 + dx;
		float y1 = line.y1 + dy;
		float x2 = line.x2 + dx;
		float y2 = line.y2 + dy;
		
		line.setLine(x1, y1, x2, y2);
		// shape 참조도 업데이트
		this.shape = this.line;
	}
}