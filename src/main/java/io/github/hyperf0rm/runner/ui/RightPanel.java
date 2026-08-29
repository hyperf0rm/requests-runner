package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Result;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;

public class RightPanel extends VBox {

    private final TextArea valuesTextArea;
    private final ScrollPane resultsScrollPane;
    private final VBox resultsContainer;
    private final Accordion resultsAccordion = new Accordion();

    public RightPanel(double spacing) {
        super(spacing);
        this.valuesTextArea = new TextArea();
        this.valuesTextArea.setPrefHeight(150);
        this.valuesTextArea.setMinHeight(Region.USE_PREF_SIZE);
        this.getChildren().addAll(new Label("Enter values:"), this.valuesTextArea);
        this.setPadding(new Insets(10));
        VBox.setVgrow(this.valuesTextArea, Priority.NEVER);

        this.resultsContainer = new VBox(spacing);
        this.resultsContainer.getChildren().add(this.resultsAccordion);
        this.resultsScrollPane = new ScrollPane(this.resultsContainer);

        this.resultsScrollPane.setFitToWidth(true);
        this.resultsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.resultsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        this.getChildren().add(this.resultsScrollPane);
        VBox.setVgrow(this.resultsScrollPane, Priority.ALWAYS);
    }

    public RightPanel() {
        this(10);
    }

    public List<String> getValues() {
        String[] parts = valuesTextArea.getText().split("( |\n)+");
        return new ArrayList<>(Arrays.asList(parts));
    }

    public void createResults(List<Result> results) {
        resultsAccordion.getPanes().clear();
        List<TitledPane> panes = new ArrayList<>();
        for (Result result : results) {
            String title = result.getURL() + " - " + result.getStatusCode() + " - " + result.getDuration() + " ms";
            TabPane tabPane = createResultTabs(result);
            TitledPane pane = new TitledPane(title, tabPane);
            panes.add(pane);
        }
        resultsAccordion.getPanes().addAll(panes);
    }

    private TabPane createResultTabs(Result result) {
        TabPane tabPane = new TabPane();
        Tab headersTab = createHeadersTab(result.getHeaders());
        Tab payloadTab = createTextTab("Payload", result.getPayload());
        Tab responseTab = createTextTab("Response", result.getResponse());
        Tab errorTab = createTextTab("Error", result.getError());
        tabPane.getTabs().addAll(headersTab, payloadTab, responseTab, errorTab);
        return tabPane;
    }

    private Tab createHeadersTab(Map<String, String> headers) {
        TableView<Map.Entry<String, String>> headerTable = new TableView<>();
        headerTable.setEditable(false);
        headerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Map.Entry<String, String>, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        headerTable.getColumns().add(keyColumn);
        headerTable.getColumns().add(valueColumn);

        ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            data.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }
        headerTable.setItems(data);
        Tab headersTab = new Tab("Headers", headerTable);
        headersTab.setClosable(false);
        return headersTab;
    }

    private Tab createTextTab(String label, String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        Tab bodyTab = new Tab(label, textArea);
        bodyTab.setClosable(false);
        return bodyTab;
    }
}
