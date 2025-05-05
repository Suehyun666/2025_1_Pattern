package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Path2D;

public class GTriangle extends GShape {
    private Path2D.Float triangle;
    private int startX, startY;
    private int[] xPoints;
    private int[] yPoints;

    public GTriangle() {
        this.triangle = new Path2D.Float();
        this.shape = this.triangle;
        this.xPoints = new int[3];
        this.yPoints = new int[3];
    }

    @Override
    public void setPoint(int x, int y) {
        // 시작점 저장
        this.startX = x;
        this.startY = y;

        // 초기 삼각형은 점 하나로 시작
        this.triangle.reset();
        this.triangle.moveTo(x, y);
        this.triangle.lineTo(x, y);
        this.triangle.lineTo(x, y);
        this.triangle.closePath();

        // 점 배열 초기화
        this.xPoints[0] = x;
        this.yPoints[0] = y;
        this.xPoints[1] = x;
        this.yPoints[1] = y;
        this.xPoints[2] = x;
        this.yPoints[2] = y;
    }

    @Override
    public void dragPoint(int x, int y) {
        // 드래그할 때 삼각형 생성
        // 첫 번째 점은 시작점
        xPoints[0] = startX;
        yPoints[0] = startY;

        // 두 번째 점은 드래그 지점의 x와 시작점의 y
        xPoints[1] = x;
        yPoints[1] = startY;

        // 세 번째 점은 드래그 지점 좌표
        xPoints[2] = startX + (x - startX) / 2;
        yPoints[2] = y;

        // 패스 업데이트
        updateTrianglePath();
    }

    // 삼각형 패스 업데이트
    private void updateTrianglePath() {
        triangle.reset();
        triangle.moveTo(xPoints[0], yPoints[0]);
        triangle.lineTo(xPoints[1], yPoints[1]);
        triangle.lineTo(xPoints[2], yPoints[2]);
        triangle.closePath();
    }

    @Override
    public void draw(Graphics2D graphics) {
        if (shape != null) {
            graphics.draw(shape);
        }
    }

    @Override
    public GShape clone() {
        GTriangle cloned = new GTriangle();
        // 점 배열 복사
        System.arraycopy(this.xPoints, 0, cloned.xPoints, 0, 3);
        System.arraycopy(this.yPoints, 0, cloned.yPoints, 0, 3);

        // 패스 업데이트
        cloned.updateTrianglePath();
        cloned.shape = cloned.triangle;

        return cloned;
    }
}