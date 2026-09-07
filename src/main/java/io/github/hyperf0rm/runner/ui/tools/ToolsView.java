package io.github.hyperf0rm.runner.ui.tools;

import io.github.hyperf0rm.runner.tool.*;
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
    private final TransformTextView jwtDecoderView =
            TransformTextView.forSingleAction("Decode JWT", JwtDecoder::decode);
    private final TransformTextView unixTimestampConverterView =
            TransformTextView.forSingleAction("Convert", UnixTimestampConverter::convertToUnix);

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
        Tab jwtFormatterTab = createTab("JWT Decoder", jwtDecoderView);
        Tab unixTimestampConverterTab = createTab("Unix Timestamp Converter", unixTimestampConverterView);
        this.getTabs().addAll(jsonFormatterTab, unicodeDecoderTab, urlDecoderTab, jwtFormatterTab, unixTimestampConverterTab);
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
