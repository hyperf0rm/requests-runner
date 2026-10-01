package io.github.hyperf0rm.runner.repository;

import io.github.hyperf0rm.runner.data.AppDataManager;
import io.github.hyperf0rm.runner.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class NoteRepository {

    private final ObservableList<Note> notes = FXCollections.observableArrayList();

    public NoteRepository() {
        List<Note> savedNotes = AppDataManager.get().notes();
        if (savedNotes != null && !savedNotes.isEmpty()) {
            notes.addAll(savedNotes);
        }
    }

    public ObservableList<Note> getNotes() {
        return notes;
    }
    public void addNote(Note note) {
        notes.add(note);
        AppDataManager.saveNotes(notes);
    }

    public void deleteNote(Note note) {
        notes.remove(note);
        AppDataManager.saveNotes(notes);
    }
}
