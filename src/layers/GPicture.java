package layers;

import layers.shapes.GShape;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GPicture extends GShape{
    private static final long serialVersionUID = 1L;

    private Image image;
    private String imagePath;
    private int x, y;
    private int width, height;

    public GPicture() {
        super(new Rectangle(0, 0, 0, 0));
        this.imagePath = null;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public GPicture(String imagePath) {
        super(new Rectangle(0, 0, 0, 0));
        this.imagePath = imagePath;
        loadImage(imagePath);
        this.x = 0;
        this.y = 0;
    }

    public GPicture(File imageFile) {
        super(new Rectangle(0, 0, 0, 0));
        this.imagePath = imageFile.getAbsolutePath();
        loadImage(imageFile);
        this.x = 0;
        this.y = 0;
    }

    private void loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.err.println("File does not Exist : " + imagePath);
                return;
            }

            Thread.sleep(500);

            this.image = ImageIO.read(imageFile);

            if (this.image == null) {
                System.out.println("ImageIO.read() returned null. Toolkit Use...");
                this.image = Toolkit.getDefaultToolkit().getImage(imagePath);
                MediaTracker tracker = new MediaTracker(new Canvas());
                tracker.addImage(this.image, 0);
                tracker.waitForAll();
            }

            if (this.image != null) {
                this.width = image.getWidth(null);
                this.height = image.getHeight(null);
                this.shape = new Rectangle(x, y, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    private void loadImage(File imageFile) {
        try {
            if (!imageFile.exists()) {
                System.err.println("File does not Exist : " + imageFile.getAbsolutePath());
                return;
            }

            Thread.sleep(500);

            this.image = ImageIO.read(imageFile);

            if (this.image == null) {
                System.out.println("ImageIO.read() returned null.  Toolkit Use...");
                // Toolkit 사용
                this.image = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                MediaTracker tracker = new MediaTracker(new Canvas());
                tracker.addImage(this.image, 0);
                tracker.waitForAll();
            }

            if (this.image != null) {
                this.width = image.getWidth(null);
                this.height = image.getHeight(null);
                this.shape = new Rectangle(x, y, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.image = null;
        }
    }
    public void rotate(int x, int y) {}

    @Override
    public void addPoint(int x, int y) {}

    @Override
    public void movePoint(int x, int y) {}

    @Override
    public void setPoint(int x, int y) {}

    public void resize(int x, int y) {
        int newWidth = Math.max(10, x - this.x);
        int newHeight = Math.max(10, y - this.y);

        this.width = newWidth;
        this.height = newHeight;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    protected Shape createShape() {
        return (Shape) new GPicture();
    }

    public boolean contains(int x, int y) {
        return (x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}