package io.github.hyperf0rm.runner.tool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UnixTimestampConverter {

    public static String convertToUnix(String timestamp) {
        long unixTimestamp = Long.parseLong(timestamp);
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return localDateTime.toString();
    }
}
