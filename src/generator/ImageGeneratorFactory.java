package generator;

import frames.GMainFrame;

public class ImageGeneratorFactory {

    public enum GeneratorType {
        STABILITY_AI,      // 유료
        HUGGING_FACE,      // 무료 (토큰 옵션)
        POLLINATIONS,      // 완전 무료
        FREE_AI           // 여러 무료 서비스 통합
    }

    public static ImageGenerator createGenerator(GeneratorType type, GMainFrame frame) {
        switch (type) {
            case STABILITY_AI:
                return new StabilityAIImageGenerator(frame);
            case HUGGING_FACE:
                return new HuggingFaceImageGenerator(frame);
            case POLLINATIONS:
                return new PollinationsImageGenerator(frame);
            case FREE_AI:
                return new FreeAIImageGenerator(frame);
            default:
                // 기본값은 무료 서비스
                return new PollinationsImageGenerator(frame);
        }
    }

    // 무료 생성기만 반환하는 메서드
    public static ImageGenerator createFreeGenerator(GMainFrame frame) {
        // Pollinations가 가장 안정적이므로 기본값으로 사용
        return new PollinationsImageGenerator(frame);
    }

    // 여러 무료 서비스를 시도하는 안전한 생성기
    public static ImageGenerator createReliableFreeGenerator(GMainFrame frame) {
        return new FreeAIImageGenerator(frame);
    }
}