package transformers;

import java.awt.Graphics2D;

import shapes.GRectangle;
import shapes.GShape;

public interface GTransFormer {//�������̽��� �������� ����
	public void start(Graphics2D graphics, int x, int y);
	public void drag(Graphics2D graphics, int x, int y);
	public GShape finish(Graphics2D graphics, int x, int y);
}
