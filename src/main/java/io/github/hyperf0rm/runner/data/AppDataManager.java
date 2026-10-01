package io.github.hyperf0rm.runner.data;

import io.github.hyperf0rm.runner.model.Note;
import io.github.hyperf0rm.runner.model.Request;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AppDataManager {

    private static final Path DATA_PATH = resolvePath("appdata.json");
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .build();

    private static AppData data;

    static {
        if (Files.exists(DATA_PATH)) {
            try {
                data = MAPPER.readValue(DATA_PATH.toFile(), AppData.class);
            } catch (Exception e) {
                System.err.println("Failed to read appdata.json: " + e.getMessage());
                data = AppData.empty();
            }
        } else {
            data = AppData.empty();
        }
    }

    public static AppData get() {
        return data;
    }

    public static void save(AppData newData) {
        data = newData;
        try {
            MAPPER.writeValue(DATA_PATH.toFile(), data);
        } catch (Exception e) {
            System.err.println("Failed to save appdata.json: " + e.getMessage());
        }
    }

    public static void saveRequests(List<Request> requests) {
        List<Request> copy = new ArrayList<>(requests);
        save(new AppData(copy, data.notes()));
    }

    public static void saveNotes(List<Note> notes) {
        List<Note> copy = new ArrayList<>(notes);
        save(new AppData(data.requests(), copy));
    }

    private static Path resolvePath(String fileName) {
        try {
            Path path = Path.of(AppData.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );

            Path dir = Files.isRegularFile(path) ? path.getParent() : path;
            return dir.resolve(fileName);
        } catch (Exception e) {
            return Path.of(fileName);
        }
    }
}
