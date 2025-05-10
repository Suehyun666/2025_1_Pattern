package transformers;

import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransFormer {
	private GShape shape;
	private Vector<GShape> selectedShapes;
	private int startX, startY;

	public GMover(GShape gshape, Vector<GShape> selectedShapes) {
		super(gshape);
		this.shape = gshape;
		this.selectedShapes = selectedShapes;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		System.out.println("Mover start");
		this.startX = x;
		this.startY = y;
		for(GShape shape : selectedShapes) {
			shape.setmovePoint(x, y);
		}
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		for(GShape shape : selectedShapes) {
			shape.movePoint(x, y);
		}
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}