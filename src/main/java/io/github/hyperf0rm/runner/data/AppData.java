package io.github.hyperf0rm.runner.data;

import io.github.hyperf0rm.runner.model.Note;
import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;

public record AppData(
        List<Request> requests,
        List<Note> notes
) {
    public static AppData empty() {
        return new AppData(new ArrayList<>(), new ArrayList<>());
    }
}
