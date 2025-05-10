package shapes;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GText extends GShape {
    private String text;
    private float x, y;
    private Font font;
    private TextLayout textLayout;
    private Rectangle2D.Float bounds;

    public GText() {
        super(new Rectangle2D.Float(0, 0, 100, 100));
        this.text = "Text";
        this.font = new Font("SansSerif", Font.PLAIN, 12);
        this.bounds = new Rectangle2D.Float(0, 0, 0, 0);
        this.shape = this.bounds;
    }

    public GText(String text) {
        this();
        this.text = text;
    }

    @Override
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    @Override
    public void movePoint(int x, int y) {
        int dx = x - px;
        int dy = y - py;
        this.x += dx;
        this.y += dy;
        updateBounds();
        this.px = x;
        this.py = y;
    }

    @Override
    public void resize(int x, int y) {
        // 텍스트 크기 조정 (글꼴 크기 변경)
        int dx = x - px;

        // 폰트 크기 변경 (dx 값에 따라 조정)
        float newSize = font.getSize() + dx / 10.0f;
        newSize = Math.max(8, Math.min(72, newSize)); // 최소/최대 크기 제한

        // 새 폰트 생성
        this.font = font.deriveFont(newSize);

        // 경계 업데이트
        updateBounds();

        this.px = x;
        this.py = y;
    }

    @Override
    public void rotate(int x, int y) {
        // 텍스트 회전 (AffineTransform 적용)
        double centerX = bounds.getX() + bounds.getWidth() / 2;
        double centerY = bounds.getY() + bounds.getHeight() / 2;

        double angle1 = Math.atan2(py - centerY, px - centerX);
        double angle2 = Math.atan2(y - centerY, x - centerX);
        double rotationAngle = angle2 - angle1;

        // 회전을 위한 변환 생성 (텍스트 렌더링 시 적용 필요)
        AffineTransform at = new AffineTransform();
        at.rotate(rotationAngle, centerX, centerY);

        // 실제 회전은 그릴 때 적용해야 함
        // 간단한 구현을 위해 여기서는 shape만 변환
        shape = at.createTransformedShape(bounds);

        this.px = x;
        this.py = y;
    }

    @Override
    public GShape clone(int x, int y) {
        GText cloned = new GText(this.text);
        cloned.x = x;
        cloned.y = y;
        cloned.font = this.font;
        cloned.updateBounds();

        return cloned;
    }
    // 텍스트 내용 설정
    public void setText(String text) {
        if (text != null && !text.isEmpty()) {
            this.text = text;
            updateBounds();
        }
    }

    // 폰트 설정
    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
            updateBounds();
        }
    }

    // 텍스트 경계 업데이트
    private void updateBounds() {
        // 텍스트 레이아웃 생성 (드로잉 없이 측정용)
        FontRenderContext frc = new FontRenderContext(null, true, true);
        textLayout = new TextLayout(text, font, frc);

        // 텍스트 바운딩 박스 계산
        Rectangle2D textBounds = textLayout.getBounds();

        // bounds 업데이트
        this.bounds.setFrame(
                x,
                y - textLayout.getAscent(), // y는 베이스라인이므로 ascent를 빼서 상단 위치로 조정
                (float) textBounds.getWidth(),
                textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()
        );

        // shape 업데이트
        this.shape = this.bounds;
    }

    @Override
    public void addPoint(int x, int y) {
    }

    @Override
    public GShape clone() {
        GText cloned = new GText(this.text);
        cloned.x = this.x;
        cloned.y = this.y;
        cloned.font = this.font;
        cloned.updateBounds();
        return cloned;
    }
    // 이 클래스에 필요한 추가 기능
    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }
}