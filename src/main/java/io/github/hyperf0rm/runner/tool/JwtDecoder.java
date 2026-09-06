package io.github.hyperf0rm.runner.tool;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtDecoder {

    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public static String decode(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return "Invalid token";
        }
        String header = new String(DECODER.decode(parts[0]), StandardCharsets.UTF_8);
        String formattedHeader = JsonFormatter.formatJson(header);
        String payload = new String(DECODER.decode(parts[1]), StandardCharsets.UTF_8);
        String formattedPayload = JsonFormatter.formatJson(payload);
        return "Header:\n" + formattedHeader + "\n\nPayload:\n" + formattedPayload;
    }
}
