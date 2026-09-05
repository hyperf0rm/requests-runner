package io.github.hyperf0rm.runner.service.tools;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.UnicodeEscaper;
import org.apache.commons.text.translate.UnicodeUnescaper;

public class UnicodeCodec implements Codec {

    private final UnicodeUnescaper decoder = new UnicodeUnescaper();
    private final UnicodeEscaper encoder = new UnicodeEscaper();

    @Override
    public String encode(String value) {
        return encoder.translate(value);
    }

    @Override
    public String decode(String value) {
        try {
            if (value.contains("\\")) {
                String unescaped = StringEscapeUtils.unescapeJava(value);
                return decoder.translate(unescaped);
            }
            return decoder.translate(value);
        }
        catch(IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
