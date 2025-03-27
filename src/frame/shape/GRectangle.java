package frame.shape;

import interfaces.GShape;
import java.awt.*;
import java.awt.geom.*;

public class GRectangle implements GShape {
    private Rectangle2D.Float rectangle;
    private Color strokeColor;
    private Color fillColor;
    private float strokeWidth;

    public GRectangle(int x, int y, int width, int height) {
        this.rectangle = new Rectangle2D.Float(x, y, width, height);
        this.strokeColor = Color.BLACK;
        this.fillColor = Color.WHITE;
        this.strokeWidth = 1.0f;
    }

    @Override
    public void draw(Graphics2D g) {
        // 저장된 스타일 정보
        Stroke originalStroke = g.getStroke();

        // 채우기
        g.setColor(fillColor);
        g.fill(rectangle);

        // 테두리
        g.setColor(strokeColor);
        g.setStroke(new BasicStroke(strokeWidth));
        g.draw(rectangle);

        // 스타일 복원
        g.setStroke(originalStroke);
    }

    @Override
    public void setColor(Color color) {
        this.strokeColor = color;
    }

    @Override
    public Color getColor() {
        return this.strokeColor;
    }

    @Override
    public int getwidth() {
        return (int)rectangle.width;
    }

    @Override
    public int getheight() {
        return (int)rectangle.height;
    }

    @Override
    public void move(int dx, int dy) {
        rectangle.x += dx;
        rectangle.y += dy;
    }

    @Override
    public void resize(int width, int height) {
        rectangle.width = width;
        rectangle.height = height;
    }

    @Override
    public void rotate(double angle) {
        // 직사각형 회전은 별도 처리 필요
    }

    @Override
    public boolean contains(int x, int y) {
        return rectangle.contains(x, y);
    }
}