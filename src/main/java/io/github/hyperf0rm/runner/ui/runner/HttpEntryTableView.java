package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.model.HttpTableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HttpEntryTableView extends TableView<HttpTableEntry> {

    public enum Type {
        PARAM, HEADER
    }

    private final Type type;
    private Consumer<List<HttpTableEntry>> onParamsChanged;

    public HttpEntryTableView(boolean editable, Type type) {
        this(editable, List.of(), type);
    }

    public HttpEntryTableView(boolean editable, List<HttpTableEntry> entries, Type type) {
        this.type = type;
        this.setEditable(editable);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<HttpTableEntry, String> keyColumn = createKeyColumn(editable);
        TableColumn<HttpTableEntry, String> valueColumn = createValueColumn(editable);
        this.getColumns().add(keyColumn);
        this.getColumns().add(valueColumn);
        setEntries(entries);
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                HttpTableEntry entry = this.getSelectionModel().getSelectedItem();
                if (entry != null) {
                    this.getItems().remove(entry);
                    if (type == Type.PARAM) {
                        notifyAboutChanges();
                    }
                }
            }
        });
    }

    public void setOnParamsChanged(Consumer<List<HttpTableEntry>> onParamsChanged) {
        this.onParamsChanged = onParamsChanged;
    }

    private void notifyAboutChanges() {
        onParamsChanged.accept(getEntries());
    }

    public void setEntries(List<HttpTableEntry> entries) {
        ObservableList<HttpTableEntry> data = FXCollections.observableArrayList();
        if (entries != null) {
            for (HttpTableEntry entry : entries) {
                data.add(new HttpTableEntry(entry.getKey(), entry.getValue()));
            }
        }

        if (this.isEditable()) {
            data.add(new HttpTableEntry("", ""));
        }

        this.setItems(data);
    }

    public List<HttpTableEntry> getEntries() {
        List<HttpTableEntry> entries = new ArrayList<>();
        for (HttpTableEntry entry : this.getItems()) {
            String rawKey = entry.getKey();
            if (rawKey != null && !rawKey.trim().isEmpty()) {
                String cleanKey = rawKey.trim();
                String rawValue = entry.getValue();
                String cleanValue = (rawValue != null) ? rawValue.trim() : "";
                entries.add(new HttpTableEntry(cleanKey, cleanValue));
            }
        }
        return entries;
    }

    private TableColumn<HttpTableEntry, String> createKeyColumn(boolean editable) {
        TableColumn<HttpTableEntry, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setReorderable(false);
        keyColumn.setSortable(false);
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        if (editable) {
            keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            keyColumn.setOnEditCommit(event -> {
                event.getRowValue().setKey(event.getNewValue());
                handleRows(event.getTablePosition().getRow());
                if (type == Type.PARAM) {
                    notifyAboutChanges();
                }
            });
        }
        return keyColumn;
    }

    private TableColumn<HttpTableEntry, String> createValueColumn(boolean editable) {
        TableColumn<HttpTableEntry, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setReorderable(false);
        valueColumn.setSortable(false);
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        if (editable) {
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            valueColumn.setOnEditCommit(event -> {
                event.getRowValue().setValue(event.getNewValue());
                handleRows(event.getTablePosition().getRow());
                if (type == Type.PARAM) {
                    notifyAboutChanges();
                }
            });
        }
        return valueColumn;
    }

    private void handleRows(int editedRow) {
        ObservableList<HttpTableEntry> entries = this.getItems();
        HttpTableEntry entry = entries.get(editedRow);
        boolean entryIsEmpty = (entry.getKey() == null || entry.getKey().isBlank())
                && (entry.getValue() == null || entry.getValue().isBlank());

        if (entryIsEmpty && editedRow != entries.size() - 1 && entries.size() > 1) {
            entries.remove(editedRow);
            return;
        }

        if (!entryIsEmpty && editedRow == entries.size() - 1) {
            entries.add(new HttpTableEntry("", ""));
        }
    }
}
