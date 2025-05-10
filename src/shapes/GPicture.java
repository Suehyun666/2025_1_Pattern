//package shapes;
//
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.Shape;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.Rectangle2D;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//
//public class GPicture extends GShape {
//    private static final long serialVersionUID = 1L;
//
//    private Image image;
//    private String imagePath;
//    private int x, y;
//    private int width, height;
//
//    // 기본 생성자
//    public GPicture() {
//        super();
//        this.imagePath = null;
//        this.x = 0;
//        this.y = 0;
//        this.width = 0;
//        this.height = 0;
//    }
//
//    // 파일 경로로 생성
//    public GPicture(String imagePath) {
//        this.imagePath = imagePath;
//        loadImage(imagePath);
//        this.x = 0;
//        this.y = 0;
//    }
//
//    // 파일 객체로 생성
//    public GPicture(File imageFile) {
//        super(imageFile.getAbsolutePath());
//        this.imagePath = imageFile.getAbsolutePath();
//        loadImage(imageFile);
//        this.x = 0;
//        this.y = 0;
//    }
//
//    // 이미지 로드 (파일 경로)
//    private void loadImage(String path) {
//        try {
//            File file = new File(path);
//            loadImage(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 이미지 로드 (파일 객체)
//    private void loadImage(File file) {
//        try {
//            image = ImageIO.read(file);
//            width = image.getWidth(null);
//            height = image.getHeight(null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void move(int dx, int dy) {
//        this.x += dx;
//        this.y += dy;
//    }
//
//    @Override
//    public void movePoint(int x, int y) {
//        int dx = x - px;
//        int dy = y - py;
//        move(dx, dy);
//        this.px = x;
//        this.py = y;
//    }
//
//    @Override
//    public void setmovePoint(int x, int y) {
//        this.px = x;
//        this.py = y;
//    }
//
//    @Override
//    public void setPoint(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public void rotate(int x, int y) {
//        // 이미지 회전 구현
//        // 회전 중심점 (이미지의 중심)
//        int centerX = this.x + this.width / 2;
//        int centerY = this.y + this.height / 2;
//
//        // 이전 점과 현재 점으로부터 각도 계산
//        double angle1 = Math.atan2(py - centerY, px - centerX);
//        double angle2 = Math.atan2(y - centerY, x - centerX);
//        double rotationAngle = angle2 - angle1;
//
//        // 실제 회전은 그릴 때 적용해야 하므로 회전 각도 저장
//        // 이미지 회전을 위해서는 추가 변수가 필요함 (rotationAngle 등)
//        // 이 예제에서는 간단한 구현만 제공
//
//        this.px = x;
//        this.py = y;
//    }
//
//    @Override
//    public void resize(int x, int y) {
//        // 이미지 크기 조정
//        int dx = x - px;
//        int dy = y - py;
//
//        // 새 크기 계산 (최소 크기 유지)
//        int newWidth = Math.max(10, this.width + dx);
//        int newHeight = Math.max(10, this.height + dy);
//
//        // 크기 업데이트
//        this.width = newWidth;
//        this.height = newHeight;
//
//        this.px = x;
//        this.py = y;
//    }
//
//    @Override
//    public GShape clone(int x, int y) {
//        // 이미지 복제
//        GPicture cloned = new GPicture();
//        cloned.imagePath = this.imagePath;
//        cloned.image = this.image;
//        cloned.x = x;
//        cloned.y = y;
//        cloned.width = this.width;
//        cloned.height = this.height;
//
//        return cloned;
//    }
//
//    // 이미지 위치 설정
//    public void setLocation(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    // 이미지 크기 설정
//    public void setSize(int width, int height) {
//        this.width = width;
//        this.height = height;
//    }
//
//    @Override
//    public boolean contains(int x, int y) {
//        return (x >= this.x && x <= this.x + width &&
//                y >= this.y && y <= this.y + height);
//    }
//
//    @Override
//    public Rectangle getBounds() {
//        return new Rectangle(x, y, width, height);
//    }
//
//    @Override
//    public GShape clone() {
//        GPicture clone = new GPicture();
//        clone.imagePath = this.imagePath;
//        clone.image = this.image;
//        clone.x = this.x;
//        clone.y = this.y;
//        clone.width = this.width;
//        clone.height = this.height;
//        return clone;
//    }
//
//    @Override
//    public boolean intersects(GShape shape) {
//        if (shape == null) return false;
//        Rectangle shapeBounds = shape.getBounds();
//        Rectangle thisBounds = this.getBounds();
//        return thisBounds.intersects(shapeBounds);
//    }
//}