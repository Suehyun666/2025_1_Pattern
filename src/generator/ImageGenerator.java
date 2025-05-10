package generator;

import java.io.File;

public interface ImageGenerator {
    // API 키 설정 및 가져오기
    void setApiKey(String key);
    String getApiKey();

    // 이미지 생성 메소드들
    File generateImage(String prompt) throws Exception;
    File generateImage(String prompt, String apiKey) throws Exception;
    File generateImage(String prompt, String apiKey, String outputPath) throws Exception;
}