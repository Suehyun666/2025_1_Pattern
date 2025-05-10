package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public abstract class GShape implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Shape shape;
	protected int px,py;
	private boolean selected;
	private boolean rotatebutton;
	private static final int ANCHOR_SIZE = 8;
	public enum EPoints {
		e2P,
		enP
	}
	public enum EAnchors {
		NW, N, NE, E, SE, S, SW, W, ROTATE
	}
	public GShape(Shape shape) {
		this.shape=shape;
	}
	protected Shape getShape() {
		return this.shape;
	}
	public Rectangle getBounds() {
		return shape.getBounds();
	}
	public boolean contains(int x, int y) {
		if (shape == null) return false;
		return shape.contains(x, y);
	}
	public void draw(Graphics2D graphics) {
		graphics.draw(shape);
		if (selected) {
			drawAnchors(graphics);
		}
	}
	public void drawAnchors(Graphics2D g) {
		if (!selected) return;
		Rectangle bounds = (Rectangle) getBounds();
		int size = 8;
		g.setColor(Color.BLACK);
		g.fillRect(bounds.x - size/2, bounds.y - size/2, size, size);
		g.fillRect(bounds.x + bounds.width/2 - size/2, bounds.y - size/2, size, size);
		g.fillRect(bounds.x + bounds.width - size/2, bounds.y - size/2, size, size);
		g.fillRect(bounds.x + bounds.width - size/2, bounds.y + bounds.height/2 - size/2, size, size);
		g.fillRect(bounds.x + bounds.width - size/2, bounds.y + bounds.height - size/2, size, size);
		g.fillRect(bounds.x + bounds.width/2 - size/2, bounds.y + bounds.height - size/2, size, size);
		g.fillRect(bounds.x - size/2, bounds.y + bounds.height - size/2, size, size);
		g.fillRect(bounds.x - size/2, bounds.y + bounds.height/2 - size/2, size, size);

		int rotateX = bounds.x + bounds.width/2;
		int rotateY = bounds.y - 20;
		g.setColor(Color.GREEN);
		g.fillOval(rotateX - size/2, rotateY - size/2, size, size);
		g.drawLine(rotateX, bounds.y, rotateX, rotateY);
	}
	public EAnchors onAnchor(int x, int y) {
		if (!selected) return null;

		Rectangle bounds = getBounds();
		int size = ANCHOR_SIZE;

		int rotateX = bounds.x + bounds.width/2;
		int rotateY = bounds.y - 20;
		if (isOnAnchor(x, y, rotateX, rotateY)) {
			return EAnchors.ROTATE;
		}
		if (isOnAnchor(x, y, bounds.x, bounds.y)) return EAnchors.NW;
		if (isOnAnchor(x, y, bounds.x + bounds.width/2, bounds.y)) return EAnchors.N;
		if (isOnAnchor(x, y, bounds.x + bounds.width, bounds.y)) return EAnchors.NE;
		if (isOnAnchor(x, y, bounds.x + bounds.width, bounds.y + bounds.height/2)) return EAnchors.E;
		if (isOnAnchor(x, y, bounds.x + bounds.width, bounds.y + bounds.height)) return EAnchors.SE;
		if (isOnAnchor(x, y, bounds.x + bounds.width/2, bounds.y + bounds.height)) return EAnchors.S;
		if (isOnAnchor(x, y, bounds.x, bounds.y + bounds.height)) return EAnchors.SW;
		if (isOnAnchor(x, y, bounds.x, bounds.y + bounds.height/2)) return EAnchors.W;

		return null;
	}
	private boolean isOnAnchor(int x, int y, int anchorX, int anchorY) {
		int size = ANCHOR_SIZE;
		return Math.abs(x - anchorX) <= size/2 && Math.abs(y - anchorY) <= size/2;
	}
	public void dragPoint(int x, int y) {}

	public void rotate(int x, int y) {
		if (shape == null) return;
		Rectangle bounds = shape.getBounds();
		double centerX = bounds.getX() + bounds.getWidth() / 2;
		double centerY = bounds.getY() + bounds.getHeight() / 2;

		double angle1 = Math.atan2(py - centerY, px - centerX);
		double angle2 = Math.atan2(y - centerY, x - centerX);
		double rotationAngle = angle2 - angle1;

		AffineTransform at = new AffineTransform();
		at.rotate(rotationAngle, centerX, centerY);
		shape = at.createTransformedShape(shape);
		this.px = x;
		this.py = y;
	}

	public void setmovePoint(int x, int y){
		this.px=x;
		this.py=y;
	}
	public void setSelected(boolean input) {this.selected = input;}
	public boolean isSelected() {
		return this.selected;
	}
	public abstract void addPoint(int x, int y);
	public abstract void movePoint(int x, int y);
	public abstract void setPoint(int x, int y);
	public abstract void resize(int x, int y);
	public abstract GShape clone(int x, int y);
}