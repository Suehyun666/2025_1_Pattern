package layers.shapes;

import java.awt.*;
import java.awt.geom.Path2D;

public class GTriangle extends GShape {
    private Path2D.Float triangle;
    private int startX, startY;
    private int[] xPoints;
    private int[] yPoints;

    public GTriangle() {
        super(new Path2D.Float());
        this.triangle = (Path2D.Float) this.getShape();
        this.xPoints = new int[3];
        this.yPoints = new int[3];
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;

        this.triangle.reset();
        this.triangle.moveTo(x, y);
        this.triangle.lineTo(x, y);
        this.triangle.lineTo(x, y);
        this.triangle.closePath();

        this.xPoints[0] = x;
        this.yPoints[0] = y;
        this.xPoints[1] = x;
        this.yPoints[1] = y;
        this.xPoints[2] = x;
        this.yPoints[2] = y;
    }

    @Override
    public void dragPoint(int x, int y) {
        xPoints[0] = startX;
        yPoints[0] = startY;

        xPoints[1] = x;
        yPoints[1] = startY;

        xPoints[2] = startX + (x - startX) / 2;
        yPoints[2] = y;
        updateTrianglePath();
    }

    @Override
    public void addPoint(int x, int y) {}

    // 삼각형 패스 업데이트
    private void updateTrianglePath() {
        triangle.reset();
        triangle.moveTo(xPoints[0], yPoints[0]);
        triangle.lineTo(xPoints[1], yPoints[1]);
        triangle.lineTo(xPoints[2], yPoints[2]);
        triangle.closePath();
    }

    @Override
    public GShape clone() {
        GTriangle cloned = new GTriangle();
        System.arraycopy(this.xPoints, 0, cloned.xPoints, 0, 3);
        System.arraycopy(this.yPoints, 0, cloned.yPoints, 0, 3);

        cloned.updateTrianglePath();
        cloned.shape = cloned.triangle;

        return cloned;
    }
    @Override
    public void movePoint(int x, int y) {
        int dx = x - px;
        int dy = y - py;

        // 모든 점 이동
        for (int i = 0; i < 3; i++) {
            xPoints[i] += dx;
            yPoints[i] += dy;
        }

        // 삼각형 패스 업데이트
        updateTrianglePath();

        this.px = x;
        this.py = y;
    }

    @Override
    public void resize(int x, int y) {
        int dx = x - px;
        int dy = y - py;

        // 삼각형 중심점 계산
        int centerX = (xPoints[0] + xPoints[1] + xPoints[2]) / 3;
        int centerY = (yPoints[0] + yPoints[1] + yPoints[2]) / 3;

        // 각 점을 중심에서부터 확대/축소
        for (int i = 0; i < 3; i++) {
            // 중심에서의 상대적 위치
            int relX = xPoints[i] - centerX;
            int relY = yPoints[i] - centerY;

            // 확대/축소 비율 계산
            double scaleX = 1.0 + (double)dx / 100.0;
            double scaleY = 1.0 + (double)dy / 100.0;

            // 새로운 위치 계산
            xPoints[i] = centerX + (int)(relX * scaleX);
            yPoints[i] = centerY + (int)(relY * scaleY);
        }

        // 삼각형 패스 업데이트
        updateTrianglePath();

        this.px = x;
        this.py = y;
    }

    @Override
    protected Shape createShape() {
    return (Shape) new GTriangle();
    }

    @Override
    public void rotate(int x, int y) {
        // 삼각형 중심점 계산
        int centerX = (xPoints[0] + xPoints[1] + xPoints[2]) / 3;
        int centerY = (yPoints[0] + yPoints[1] + yPoints[2]) / 3;

        double angle1 = Math.atan2(py - centerY, px - centerX);
        double angle2 = Math.atan2(y - centerY, x - centerX);
        double rotationAngle = angle2 - angle1;

        for (int i = 0; i < 3; i++) {
            int relX = xPoints[i] - centerX;
            int relY = yPoints[i] - centerY;
            double cos = Math.cos(rotationAngle);
            double sin = Math.sin(rotationAngle);
            double newX = relX * cos - relY * sin;
            double newY = relX * sin + relY * cos;
            xPoints[i] = centerX + (int)newX;
            yPoints[i] = centerY + (int)newY;
        }
        updateTrianglePath();

        this.px = x;
        this.py = y;
    }
}