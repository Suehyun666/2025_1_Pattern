package transformers;

import java.awt.Graphics2D;

import shapes.GRectangle;
import shapes.GShape;

public interface GTransFormer {//인터페이스만 갖고있을 예쩡
	public void start(Graphics2D graphics, int x, int y);
	public void drag(Graphics2D graphics, int x, int y);
	public GShape finish(Graphics2D graphics, int x, int y);
}
