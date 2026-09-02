package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;

public class HeadersTableView extends TableView<Header> {

    public HeadersTableView(boolean editable) {
        this(editable, List.of());
    }

    public HeadersTableView(boolean editable, List<Header> headers) {
        this.setEditable(editable);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<Header, String> keyColumn = createKeyColumn(editable);
        TableColumn<Header, String> valueColumn = createValueColumn(editable);
        this.getColumns().add(keyColumn);
        this.getColumns().add(valueColumn);
        setHeaders(headers);
    }

    public void setHeaders(List<Header> headers) {
        ObservableList<Header> data = FXCollections.observableArrayList();
        if (headers != null) {
            for (Header header : headers) {
                data.add(new Header(header.getKey(), header.getValue()));
            }
        }

        if (this.isEditable()) {
            data.add(new Header("", ""));
        }

        this.setItems(data);
    }

    public List<Header> getHeaders() {
        List<Header> headers = new ArrayList<>();
        for (Header header : this.getItems()) {
            String rawKey = header.getKey();
            if (rawKey != null && !rawKey.trim().isEmpty()) {
                String cleanKey = rawKey.trim();
                String rawValue = header.getValue();
                String cleanValue = (rawValue != null) ? rawValue.trim() : "";
                headers.add(new Header(cleanKey, cleanValue));
            }
        }
        return headers;
    }

    private TableColumn<Header, String> createKeyColumn(boolean editable) {
        TableColumn<Header, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setReorderable(false);
        keyColumn.setSortable(false);
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        if (editable) {
            keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            keyColumn.setOnEditCommit(event -> {
                event.getRowValue().setKey(event.getNewValue());
                handleRows( event.getTablePosition().getRow());
            });
        }
        return keyColumn;
    }

    private TableColumn<Header, String> createValueColumn(boolean editable) {
        TableColumn<Header, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setReorderable(false);
        valueColumn.setSortable(false);
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        if (editable) {
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            valueColumn.setOnEditCommit(event -> {
                event.getRowValue().setValue(event.getNewValue());
                handleRows( event.getTablePosition().getRow());
            });
        }
        return valueColumn;
    }

    private void handleRows(int editedRow) {
        ObservableList<Header> headers = this.getItems();
        Header header = headers.get(editedRow);
        boolean headerIsEmpty = (header.getKey() == null || header.getKey().isBlank())
                && (header.getValue() == null || header.getValue().isBlank());

        if (headerIsEmpty && editedRow != headers.size() - 1 && headers.size() > 1) {
            headers.remove(editedRow);
            return;
        }

        if (!headerIsEmpty && editedRow == headers.size() - 1) {
            headers.add(new Header("", ""));
        }
    }
}
