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
		// ���� ��� ��Ȯ�� ��ġ ��� ���� �Ÿ� ���� �ִ��� Ȯ��
		// ptSegDist: ���� �� ������ �Ÿ��� ���
		return line.ptSegDist(x, y) <= 5.0; // 5�ȼ� �̳��� �������� ����
	}
	
	@Override
	public void move(int dx, int dy) {
		if (line == null) return;
		
		// ���� ���� �� ������ ����
		float x1 = line.x1 + dx;
		float y1 = line.y1 + dy;
		float x2 = line.x2 + dx;
		float y2 = line.y2 + dy;
		
		line.setLine(x1, y1, x2, y2);
		// shape ������ ������Ʈ
		this.shape = this.line;
	}
}