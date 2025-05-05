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
    public void dragPoint(int x, int y) {
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
                (float)textBounds.getWidth(),
                textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()
        );

        // shape 업데이트
        this.shape = this.bounds;
    }

    @Override
    public void draw(Graphics2D graphics) {
        if (text != null && !text.isEmpty()) {
            // 현재 폰트 저장
            Font oldFont = graphics.getFont();

            // 새 폰트 설정
            graphics.setFont(font);

            // 텍스트 그리기
            graphics.drawString(text, x, y);

            // 디버깅용 - 텍스트 경계 표시
            // graphics.draw(bounds);

            // 폰트 복원
            graphics.setFont(oldFont);
        }
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

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        updateBounds();
    }

    // 이 클래스에 필요한 추가 기능
    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }
}