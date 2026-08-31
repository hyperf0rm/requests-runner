package io.github.hyperf0rm.runner.util;

import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class JsonFormatter {

    private static final DefaultPrettyPrinter PRETTY_PRINTER = new DefaultPrettyPrinter()
            .withSeparators(Separators.createDefaultInstance().withObjectNameValueSpacing(Separators.Spacing.AFTER))
            .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .defaultPrettyPrinter(PRETTY_PRINTER)
            .build();

    private JsonFormatter() {}

    public static String formatJson(String rawBody) {
        if (rawBody == null || rawBody.isBlank()) {
            return "";
        }
        try {
            Object jsonObject = MAPPER.readValue(rawBody, Object.class);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            return rawBody;
        }
    }
}
