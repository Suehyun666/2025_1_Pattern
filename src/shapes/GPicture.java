package shapes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GPicture extends GShape {
    private static final long serialVersionUID = 1L;

    private Image image;
    private String imagePath;
    private int x, y;
    private int width, height;

    // 기본 생성자
    public GPicture() {
        this.imagePath = null;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    // 파일 경로로 생성
    public GPicture(String imagePath) {
        this.imagePath = imagePath;
        loadImage(imagePath);
        this.x = 0;
        this.y = 0;
    }

    // 파일 객체로 생성
    public GPicture(File imageFile) {
        this.imagePath = imageFile.getAbsolutePath();
        loadImage(imageFile);
        this.x = 0;
        this.y = 0;
    }

    // 이미지 로드 (파일 경로)
    private void loadImage(String path) {
        try {
            File file = new File(path);
            loadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 로드 (파일 객체)
    private void loadImage(File file) {
        try {
            image = ImageIO.read(file);
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (image != null) {
            g2d.drawImage(image, x, y, width, height, null);
        }
    }

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    // 이미지 위치 설정
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 이미지 크기 설정
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean contains(int x, int y) {
        return (x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public GShape clone() {
        GPicture clone = new GPicture();
        clone.imagePath = this.imagePath;
        clone.image = this.image;
        clone.x = this.x;
        clone.y = this.y;
        clone.width = this.width;
        clone.height = this.height;
        return clone;
    }

    @Override
    public boolean intersects(GShape shape) {
        if (shape == null) return false;
        Rectangle shapeBounds = shape.getBounds();
        Rectangle thisBounds = this.getBounds();
        return thisBounds.intersects(shapeBounds);
    }
}