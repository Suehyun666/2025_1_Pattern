package layers.shapes;

import layers.GLayer;
import layers.shapes.GAnchors.EAnchors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.Icon;

public abstract class GShape implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	protected Shape shape;
	protected int px,py;
	private boolean selected;
	private static final int ANCHOR_SIZE = 8;
	protected double rotation = 0.0;
	protected boolean visible = true;
	protected boolean locked = false;
	protected Color lineColor = Color.BLACK;
	protected Color fillColor = null;
	protected Vector<Point> points;
	protected GLayer layer;
	public enum EPoints {
		e2P,
		enP
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
	public int getX() {return (int) this.shape.getBounds().getX();}
	public int getY() {
		return (int) this.shape.getBounds().getY();
	}
	public void setLocation(int x, int y) {
		Rectangle bounds = this.shape.getBounds();
		int dx = x - bounds.x;
		int dy = y - bounds.y;

		for (Point p : this.points) {
			p.translate(dx, dy);
		}
		this.shape = this.createShape();
	}
	public int getWidth() {
		return (int) this.shape.getBounds().getWidth();
	}
	public int getHeight() {
		return (int) this.shape.getBounds().getHeight();
	}
	public void setSize(int width, int height) {
		Rectangle bounds = this.shape.getBounds();
		double scaleX = (double) width / bounds.width;
		double scaleY = (double) height / bounds.height;

		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;

		for (Point p : this.points) {
			int dx = p.x - centerX;
			int dy = p.y - centerY;
			p.x = centerX + (int)(dx * scaleX);
			p.y = centerY + (int)(dy * scaleY);
		}
		this.shape = this.createShape();
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	public Color getLineColor() {
		return lineColor;
	}
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	public void setLayer(GLayer gLayer) {
		this.layer=gLayer;
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public void draw(Graphics2D g2D) {
		if (!visible) return;
		if (selected) {
			drawAnchors(g2D);
		}
		if (rotation != 0) {
			g2D = (Graphics2D) g2D.create();
			Rectangle bounds = shape.getBounds();
			g2D.rotate(Math.toRadians(rotation),
					bounds.getCenterX(),
					bounds.getCenterY());
		}
		if (fillColor != null) {
			g2D.setColor(fillColor);
			g2D.fill(shape);
		}
		if (lineColor != null) {
			g2D.setColor(lineColor);
			g2D.draw(shape);
		}
		if (rotation != 0) {
			g2D.dispose();
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
	}

	private boolean isOnAnchor(int x, int y, int anchorX, int anchorY) {
	    return (Math.abs(x - anchorX) <= ANCHOR_SIZE/2) && 
	           (Math.abs(y - anchorY) <= ANCHOR_SIZE/2);
	}
	protected abstract Shape createShape();
	public abstract void addPoint(int x, int y);
	public abstract void movePoint(int x, int y);
	public abstract void setPoint(int x, int y);
	public abstract void resize(int x, int y);
	@Override
	public GShape clone() {
		try {
			GShape cloned = (GShape) super.clone();
			if (this.fillColor != null) {
				cloned.fillColor = new Color(this.fillColor.getRGB(), true);
			}
			if (this.lineColor != null) {
				cloned.lineColor = new Color(this.lineColor.getRGB(), true);
			}
			if (this.points != null) {
				cloned.points = new Vector<Point>();
				for (Point p : this.points) {
					cloned.points.add(new Point(p));
				}
			}
			if (this.shape != null) {
				cloned.shape = (Shape) cloned.createShape();
			}

			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	public EAnchors onAnchor(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}
	public Icon isVisible() {
		// TODO Auto-generated method stub
		return null;
	}
	public abstract Icon isLocked();
	public void setVisible(boolean selected2) {
		
	}
	public void setLocked(boolean selected2) {
		
	}
}