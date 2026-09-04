package io.github.hyperf0rm.runner.service.tools;

public interface Codec {
    String encode(String input);
    String decode(String input);
}
