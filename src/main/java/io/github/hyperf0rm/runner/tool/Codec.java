package io.github.hyperf0rm.runner.tool;

public interface Codec {
    String encode(String input);
    String decode(String input);
}
