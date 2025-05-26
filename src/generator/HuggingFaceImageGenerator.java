package generator;

import frames.GMainFrame;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.JSONObject;
import org.json.JSONArray;

public class HuggingFaceImageGenerator implements ImageGenerator {
    private GMainFrame frame;
    private String apikey = null;

    // Hugging Face 무료 모델들
    private static final String[] FREE_MODELS = {
            "stabilityai/stable-diffusion-2-1",
            "runwayml/stable-diffusion-v1-5",
            "CompVis/stable-diffusion-v1-4",
            "prompthero/openjourney",
            "SG161222/Realistic_Vision_V2.0",
            "dreamlike-art/dreamlike-diffusion-1.0"
    };

    private int currentModelIndex = 0;

    public HuggingFaceImageGenerator(GMainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setApiKey(String key) {
        // Hugging Face의 경우 API 키 없이도 무료로 사용 가능하지만,
        // 토큰이 있으면 rate limit이 더 높음
        this.apikey = key;
    }

    @Override
    public String getApiKey() {
        return this.apikey;
    }

    @Override
    public File generateImage(String prompt) throws Exception {
        // API 키가 없어도 작동
        return generateImage(prompt, this.apikey);
    }

    @Override
    public File generateImage(String prompt, String apiKey) throws Exception {
        File outputFile = File.createTempFile("huggingface-", ".png");
        File generatedFile = generateImage(prompt, apiKey, outputFile.getAbsolutePath());

        if (generatedFile != null && generatedFile.exists()) {
            showGeneratedImageInDialog(generatedFile);
        }

        return generatedFile;
    }

    @Override
    public File generateImage(String prompt, String apiKey, String outputPath) throws Exception {
        // 현재 모델로 시도
        Exception lastException = null;

        for (int attempts = 0; attempts < FREE_MODELS.length; attempts++) {
            try {
                String modelUrl = "https://api-inference.huggingface.co/models/" + FREE_MODELS[currentModelIndex];
                System.out.println("Attempting with model: " + FREE_MODELS[currentModelIndex]);

                URL url = new URL(modelUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                // API 키가 있으면 사용 (rate limit 증가)
                if (apiKey != null && !apiKey.isEmpty()) {
                    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                }

                connection.setDoOutput(true);

                // JSON 페이로드 생성
                JSONObject payload = new JSONObject();
                payload.put("inputs", prompt);

                // 품질 향상을 위한 파라미터들
                JSONObject parameters = new JSONObject();
                parameters.put("guidance_scale", 7.5);
                parameters.put("negative_prompt", "blurry, bad quality, low resolution, distorted");
                parameters.put("num_inference_steps", 50);
                payload.put("parameters", parameters);

                // 전송
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    // 성공적으로 이미지 받음
                    try (InputStream is = connection.getInputStream();
                         FileOutputStream fos = new FileOutputStream(new File(outputPath))) {

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }

                        System.out.println("Image saved successfully to " + outputPath);
                        connection.disconnect();
                        return new File(outputPath);
                    }
                } else if (responseCode == 503) {
                    // 모델이 로딩 중이거나 사용 불가
                    System.out.println("Model is loading, trying next model...");
                    currentModelIndex = (currentModelIndex + 1) % FREE_MODELS.length;
                    continue;
                } else {
                    // 다른 에러
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }

                    lastException = new Exception("Error response code: " + responseCode +
                            " - " + response.toString());
                    System.out.println("Error with model " + FREE_MODELS[currentModelIndex] +
                            ": " + response.toString());

                    // 다음 모델로 시도
                    currentModelIndex = (currentModelIndex + 1) % FREE_MODELS.length;
                }

                connection.disconnect();

            } catch (Exception e) {
                lastException = e;
                System.out.println("Exception with model " + FREE_MODELS[currentModelIndex] +
                        ": " + e.getMessage());
                currentModelIndex = (currentModelIndex + 1) % FREE_MODELS.length;
            }
        }

        // 모든 모델이 실패한 경우
        if (lastException != null) {
            throw new Exception("All models failed. Last error: " + lastException.getMessage());
        }

        throw new Exception("Failed to generate image with all available models");
    }

    private void showGeneratedImageInDialog(File imageFile) {
        new GPictureViewerDialog(frame, "Generated Image", imageFile, frame.getMainPanel());
    }

    // 현재 사용 중인 모델 이름 반환
    public String getCurrentModel() {
        return FREE_MODELS[currentModelIndex];
    }

    // 사용 가능한 모든 모델 리스트 반환
    public String[] getAvailableModels() {
        return FREE_MODELS.clone();
    }

    // 특정 모델로 변경
    public void setModel(String modelName) {
        for (int i = 0; i < FREE_MODELS.length; i++) {
            if (FREE_MODELS[i].equals(modelName)) {
                currentModelIndex = i;
                break;
            }
        }
    }
}
