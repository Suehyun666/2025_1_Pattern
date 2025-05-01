package shapes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GPicture extends GShape {
    private Rectangle2D.Float boundary;
    private BufferedImage image;
    private String prompt;
    private boolean isLoading;

    // API 설정
    private static final String API_URL = "https://your-image-generation-api.com/generate";
    private static final String API_KEY = "your_api_key";

    public GPicture() {
        this.boundary = new Rectangle2D.Float(0, 0, 300, 300); // 기본 크기
        this.shape = this.boundary;
        this.isLoading = false;
    }

    // 프롬프트로 이미지 생성 요청
    public void generateFromPrompt(String prompt, Consumer<Boolean> callback) {
        this.prompt = prompt;
        this.isLoading = true;

        // 비동기 이미지 생성 (실제 구현은 사용하는 API에 맞게 수정 필요)
        CompletableFuture.supplyAsync(() -> {
            try {
                // 여기에 실제 API 호출 구현
                // 예시: URL url = new URL(API_URL + "?prompt=" + URLEncoder.encode(prompt, "UTF-8"));
                // 실제 구현시 적절한 HTTP 클라이언트 라이브러리 사용 권장

                // 테스트용 코드 - 실제 구현시 아래 코드 대체 필요
                URL sampleImageUrl = new URL("https://sample-image-url.com/placeholder.jpg");
                BufferedImage generatedImage = ImageIO.read(sampleImageUrl);
                return generatedImage;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).thenAccept(result -> {
            this.image = result;
            this.isLoading = false;
            if (callback != null) {
                callback.accept(result != null);
            }
        });
    }

    // 로컬 이미지 파일 로드
    public void loadImage(String filePath) {
        try {
            this.image = ImageIO.read(new java.io.File(filePath));
            updateBoundary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // URL에서 이미지 로드
    public void loadImageFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            this.image = ImageIO.read(url);
            updateBoundary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBoundary() {
        if (image != null) {
            // 이미지 로드 후 경계 상자 업데이트
            float x = boundary.x;
            float y = boundary.y;
            this.boundary.setFrame(x, y, image.getWidth(), image.getHeight());
            this.shape = this.boundary;
        }
    }

    @Override
    public void setPoint(int x, int y) {
        this.boundary.setFrame(x, y, boundary.width, boundary.height);
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = boundary.getX();
        double oy = boundary.getY();
        double width = x - ox;
        double height = y - oy;
        if (width > 10 && height > 10) { // 최소 크기 제한
            this.boundary.setFrame(ox, oy, width, height);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isLoading) {
            // 로딩 중 표시
            g.draw(boundary);
            g.drawString("Generating image...", (int)boundary.x + 10, (int)boundary.y + 30);
        } else if (image != null) {
            // 이미지 그리기
            g.drawImage(image, (int)boundary.x, (int)boundary.y,
                    (int)boundary.width, (int)boundary.height, null);
            g.draw(boundary); // 선택 표시를 위한 경계선
        } else {
            // 이미지가 없는 경우 빈 경계 표시
            g.draw(boundary);
            g.drawString("No image", (int)boundary.x + 10, (int)boundary.y + 30);
        }
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public BufferedImage getImage() {
        return image;
    }
}