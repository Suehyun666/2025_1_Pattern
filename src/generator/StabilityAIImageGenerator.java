package generator;

import frames.GMainFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StabilityAIImageGenerator implements ImageGenerator {
    private GMainFrame frame;
    private String apikey = null;

    public StabilityAIImageGenerator(GMainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setApiKey(String key) {
        this.apikey = key;
    }

    @Override
    public String getApiKey() {
        return this.apikey;
    }

    @Override
    public File generateImage(String prompt) throws Exception {
        return generateImage(prompt, this.apikey);
    }

    @Override
    public File generateImage(String prompt, String apiKey) throws Exception {
        // 임시 파일 생성
        File outputFile = File.createTempFile("stability-ai-", ".png");
        File generatedFile = generateImage(prompt, apiKey, outputFile.getAbsolutePath());

        // 이미지 생성 후 다이얼로그 표시
        if (generatedFile != null && generatedFile.exists()) {
            showGeneratedImageInDialog(generatedFile);
        }

        return generatedFile;
    }

    @Override
    public File generateImage(String prompt, String apiKey, String outputPath) throws Exception {
        // API 호출 코드는 그대로 유지...
        String boundary = UUID.randomUUID().toString();
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        URL url = new URL("https://api.stability.ai/v2beta/stable-image/generate/ultra");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Accept", "image/*");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        connection.setDoOutput(true);
        connection.setUseCaches(false);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"prompt\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write((prompt + lineEnd).getBytes(StandardCharsets.UTF_8));

            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"output_format\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("webp" + lineEnd).getBytes(StandardCharsets.UTF_8));

            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"none\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write((lineEnd).getBytes(StandardCharsets.UTF_8));

            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            try (OutputStream fileOutputStream = new FileOutputStream(new File(outputPath))) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = connection.getInputStream().read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Image saved successfully to " + outputPath);
            }
        } else {
            StringBuilder response = new StringBuilder();
            try (java.util.Scanner scanner = new java.util.Scanner(connection.getErrorStream()).useDelimiter("\\A")) {
                response.append(scanner.hasNext() ? scanner.next() : "");
            }
            throw new Exception("Error response code: " + responseCode + " - " + response.toString());
        }

        connection.disconnect();
        return new File(outputPath);
    }

    // 생성된 이미지를 다이얼로그로 표시
    private void showGeneratedImageInDialog(File imageFile) {
        new GPictureViewerDialog(frame, "Generated Image", imageFile, frame.getMainPanel());
    }
}