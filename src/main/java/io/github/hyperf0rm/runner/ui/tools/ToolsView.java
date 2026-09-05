package io.github.hyperf0rm.runner.ui.tools;

import io.github.hyperf0rm.runner.service.tools.UnicodeCodec;
import io.github.hyperf0rm.runner.service.tools.UrlCodec;
import io.github.hyperf0rm.runner.util.JsonFormatter;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ToolsView extends TabPane {

    private static final double TAB_WIDTH = 150.0;
    private final TransformTextView urlDecoderView = TransformTextView.forCodec(new UrlCodec());
    private final TransformTextView unicodeDecoderView = TransformTextView.forCodec(new UnicodeCodec());
    private final TransformTextView jsonFormatterView =
            TransformTextView.forSingleAction("Format JSON", JsonFormatter::formatJson);

    public ToolsView() {
        this.setSide(Side.LEFT);
        this.setRotateGraphic(true);
        this.setTabMinHeight(TAB_WIDTH);
        this.setTabMaxHeight(TAB_WIDTH);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.getStyleClass().add("horizontal-tab-pane");
        Tab urlDecoderTab = createTab("URL Decoder", urlDecoderView);
        Tab unicodeDecoderTab = createTab("Unicode Decoder", unicodeDecoderView);
        Tab jsonFormatterTab = createTab("JSON Formatter", jsonFormatterView);
        this.getTabs().addAll(jsonFormatterTab, unicodeDecoderTab, urlDecoderTab);
    }

    public Tab createTab(String labelName, Node content) {
        Label label = new Label(labelName);
        Tab tab = new Tab();
        tab.setContent(content);
        tab.setGraphic(label);
        Platform.runLater(() -> {
            Parent tabContainer = tab.getGraphic().getParent().getParent();
            tabContainer.setRotate(90);
            tabContainer.setTranslateY(-(TAB_WIDTH / 2));
        });
        return tab;
    }
}
