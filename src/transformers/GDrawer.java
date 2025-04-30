package transformers;

import java.awt.Graphics2D;
import shapes.GShape;

public class GDrawer implements GTransFormer {
	private GShape shape;
	private int lastX, lastY; 
	
	public GDrawer(GShape shape) {
		this.shape = shape;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		if (shape == null || graphics == null) return;
		
		this.shape.setPoint(x, y);
		this.shape.dragPoint(x, y);
		this.lastX = x;
		this.lastY = y;
	}
	
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		if (shape == null || graphics == null) return;
		
		// ���� ��� �����
		this.shape.draw(graphics);
		// �� ��ġ�� ������Ʈ
		this.shape.dragPoint(x, y);
		// �� ��� �׸���
		this.shape.draw(graphics);
		
		this.lastX = x;
		this.lastY = y;
	}
	
	@Override
	public GShape finish(Graphics2D graphics, int x, int y) {
		if (shape == null || graphics == null) return null;
		
		if (x != lastX || y != lastY) {
			this.shape.draw(graphics);
			this.shape.dragPoint(x, y);
		}
		return this.shape;
	}
}