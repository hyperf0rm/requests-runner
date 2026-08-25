package io.github.hyperf0rm.runner.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class Tabs extends TabPane {

    private Tab headersTab;
    private Tab bodyTab;

    public Tabs() {
        this.headersTab = createHeadersTab();
        this.bodyTab = createBodyTab();
        this.getTabs().addAll(headersTab, bodyTab);
    }

    private Tab createHeadersTab() {
        headersTab = new Tab("Headers", new TextArea());
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

    public String getHeaders() {
        return headersTab.getText();
    }
}
