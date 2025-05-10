package generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StabilityAIImageGenerator implements ImageGenerator {

    private String apikey = null;

    // API 키 설정 메소드
    @Override
    public void setApiKey(String key) {
        this.apikey = key;
    }

    @Override
    public String getApiKey() {
        return this.apikey;
    }

    // 기본 API 키로 이미지 생성
    @Override
    public File generateImage(String prompt) throws Exception {
        return generateImage(prompt, this.apikey);
    }

    // 커스텀 API 키로 이미지 생성
    @Override
    public File generateImage(String prompt, String apiKey) throws Exception {
        // 임시 파일 생성
        File outputFile = File.createTempFile("stability-ai-", ".webp");
        return generateImage(prompt, apiKey, outputFile.getAbsolutePath());
    }

    // 기존 메소드 유지 (파일명을 직접 지정)
    @Override
    public File generateImage(String prompt, String apiKey, String outputPath) throws Exception {
        // Create boundary string for multipart form data
        String boundary = UUID.randomUUID().toString();
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        // Setup connection
        URL url = new URL("https://api.stability.ai/v2beta/stable-image/generate/ultra");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request headers
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Accept", "image/*");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Enable output and disable caching
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // Create output stream for request body
        try (OutputStream outputStream = connection.getOutputStream()) {
            // Add the prompt field
            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"prompt\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write((prompt + lineEnd).getBytes(StandardCharsets.UTF_8));

            // Add output format field
            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"output_format\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("webp" + lineEnd).getBytes(StandardCharsets.UTF_8));

            // Add empty "none" field which was in the Python code
            outputStream.write((twoHyphens + boundary + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Disposition: form-data; name=\"none\"" + lineEnd + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.write((lineEnd).getBytes(StandardCharsets.UTF_8));

            // End of multipart form data
            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        // Get response
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            // Save the image to a file
            try (OutputStream fileOutputStream = new FileOutputStream(new File(outputPath))) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = connection.getInputStream().read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Image saved successfully to " + outputPath);
            }
        } else {
            // Handle error
            StringBuilder response = new StringBuilder();
            try (java.util.Scanner scanner = new java.util.Scanner(connection.getErrorStream()).useDelimiter("\\A")) {
                response.append(scanner.hasNext() ? scanner.next() : "");
            }
            throw new Exception("Error response code: " + responseCode + " - " + response.toString());
        }

        connection.disconnect();

        // 생성된 파일 반환
        return new File(outputPath);
    }
}