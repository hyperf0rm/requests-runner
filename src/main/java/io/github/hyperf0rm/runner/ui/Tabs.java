package io.github.hyperf0rm.runner.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Tabs extends TabPane {

    private Tab headersTab;
    private Tab bodyTab;

    public Tabs() {
        this.headersTab = createHeadersTab();
        this.bodyTab = createBodyTab();
        this.getTabs().addAll(headersTab, bodyTab);
    }

    private Tab createHeadersTab() {
        TableView<Map.Entry<String, String>> headerTable =  new TableView<>();
        headerTable.setEditable(true);
        headerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<Map.Entry<String, String>, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            String currentValue = event.getRowValue().getValue();
            event.getTableView().getItems().set(row, new AbstractMap.SimpleEntry<>(event.getNewValue(), currentValue));
        });
        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            String currentKey = event.getRowValue().getKey();
            event.getTableView().getItems().set(row, new AbstractMap.SimpleEntry<>(currentKey, event.getNewValue()));
        });
        headerTable.getColumns().add(keyColumn);
        headerTable.getColumns().add(valueColumn);
        ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList();
        for (int i = 0; i < 30; i++) {
            data.add(new AbstractMap.SimpleEntry<>("", ""));
        }
        headerTable.setItems(data);
        headersTab = new Tab("Headers", headerTable);
        headersTab.setClosable(false);
        return headersTab;
    }

    private Tab createBodyTab() {
        bodyTab = new Tab("Body", new TextArea());
        bodyTab.setClosable(false);
        return bodyTab;
    }

    public String getBody() {
        if (bodyTab.getContent() instanceof TextArea textArea) {
            return textArea.getText();
        }
        return "";
    }

    public Map<String, String> getHeaders() {
        TableView<Map.Entry<String, String>> table = (TableView<Map.Entry<String, String>>) headersTab.getContent();
        Map<String, String> headersMap = new HashMap<>();
        for (Map.Entry<String, String> entry : table.getItems()) {
            String rawKey = entry.getKey();
            if (rawKey != null && !rawKey.trim().isEmpty()) {
                String cleanKey = rawKey.trim();
                String rawValue = entry.getValue();
                String cleanValue = (rawValue != null) ? rawValue.trim() : "";
                headersMap.put(cleanKey, cleanValue);
            }
        }
        return headersMap;
    }
}
