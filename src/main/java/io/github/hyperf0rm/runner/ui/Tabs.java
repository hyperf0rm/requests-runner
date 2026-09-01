package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import javafx.scene.control.*;

import java.util.*;

public class Tabs extends TabPane {

    private Tab headersTab;
    private Tab bodyTab;

    public Tabs() {
        this.headersTab = createHeadersTab();
        this.bodyTab = createBodyTab();
        this.getTabs().addAll(headersTab, bodyTab);
    }

    private Tab createHeadersTab() {
        HeadersTableView headerTable =  new HeadersTableView(true);
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

    public List<Header> getHeaders() {
        HeadersTableView table = (HeadersTableView) headersTab.getContent();
        return table.getHeaders();
    }

    public void setBody(String body) {
        if (bodyTab.getContent() instanceof TextArea textArea) {
            textArea.setText(body);
        }
    }

    public void setHeaders(List<Header> headers) {
        HeadersTableView headerTable = (HeadersTableView) headersTab.getContent();
        headerTable.setHeaders(headers);
    }
}
