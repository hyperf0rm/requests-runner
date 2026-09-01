package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.Result;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;

public class RightPanel extends VBox {

    private final TextArea valuesTextArea;
    private final Accordion resultsAccordion = new Accordion();

    public RightPanel(double spacing) {
        super(spacing);
        this.valuesTextArea = new TextArea();
        this.valuesTextArea.setPrefHeight(150);
        this.valuesTextArea.setMinHeight(Region.USE_PREF_SIZE);
        this.getChildren().addAll(new Label("Enter values:"), this.valuesTextArea);
        this.setPadding(new Insets(10));
        VBox.setVgrow(this.valuesTextArea, Priority.NEVER);

        VBox resultsContainer = new VBox(spacing);
        resultsContainer.getChildren().add(this.resultsAccordion);
        ScrollPane resultsScrollPane = new ScrollPane(resultsContainer);

        resultsScrollPane.setFitToWidth(true);
        resultsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        resultsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        this.getChildren().add(resultsScrollPane);
        VBox.setVgrow(resultsScrollPane, Priority.ALWAYS);
    }

    public RightPanel() {
        this(10);
    }

    public List<String> getValues() {
        String[] parts = valuesTextArea.getText().split("( |\n)+");
        return new ArrayList<>(Arrays.asList(parts));
    }

    public void addSingleResult(Result result) {
        String title = result.getId() + ". " + result.getUrl() + " - " + result.getStatusCode() + " - " + result.getDuration() + " ms";
        TabPane tabPane = createResultTabs(result);
        TitledPane pane = new TitledPane(title, tabPane);
        resultsAccordion.getPanes().add(pane);
    }

    public void clearResults() {
        resultsAccordion.getPanes().clear();
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

    private Tab createHeadersTab(List<Header> headers) {
        HeadersTableView headerTable = new HeadersTableView(false, headers);
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
