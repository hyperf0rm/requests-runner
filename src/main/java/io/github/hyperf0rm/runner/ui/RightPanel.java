package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.Result;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
        TitledPane pane = new TitledPane();
        pane.setMinWidth(0);
        pane.setGraphic(createTitleBox(result));
        pane.setContent(createResultTabs(result));
        resultsAccordion.getPanes().add(pane);
    }

    private HBox createTitleBox(Result result) {
        Label idLabel = new Label(result.getId() + ". ");
        idLabel.setMinWidth(Region.USE_PREF_SIZE);

        Label urlLabel = new Label(result.getUrl() + " - ");
        urlLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        urlLabel.setMinWidth(0);
        urlLabel.setTooltip(new Tooltip(result.getUrl()));

        Label statusCodeLabel = new Label(String.valueOf(result.getStatusCode()));
        statusCodeLabel.setMinWidth(Region.USE_PREF_SIZE);
        statusCodeLabel.setTextFill(getStatusColor(result.getStatusCode()));

        Label durationLabel = new Label(" - " + result.getDuration() + " ms");
        durationLabel.setMinWidth(Region.USE_PREF_SIZE);

        return new HBox(idLabel, urlLabel, statusCodeLabel, durationLabel);
    }

    private Color getStatusColor(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return Color.LIGHTGREEN;
        if (statusCode >= 400) return Color.INDIANRED;
        return Color.LIGHTSKYBLUE;
    }

    public void clearResults() {
        resultsAccordion.getPanes().clear();
    }

    private TabPane createResultTabs(Result result) {
        return new TabPane(
                createHeadersTab("Headers", result.getHeaders()),
                createTextTab("Payload", result.getPayload()),
                createTextTab("Response body", result.getResponse()),
                createHeadersTab("Response headers", result.getResponseHeaders()),
                createTextTab("Error", result.getError())
        );
    }

    private Tab createHeadersTab(String label, List<Header> headers) {
        HeadersTableView headerTable = new HeadersTableView(false, headers);
        Tab headersTab = new Tab(label, headerTable);
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
