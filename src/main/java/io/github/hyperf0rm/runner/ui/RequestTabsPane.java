package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import javafx.scene.control.*;

import java.util.*;

public class RequestTabsPane extends TabPane {

    private final HeadersTableView headersTableView = new HeadersTableView(true);
    private final TextArea bodyTextArea = new TextArea();

    public RequestTabsPane() {
        Tab headersTab = new Tab("Headers", headersTableView);
        headersTab.setClosable(false);
        Tab bodyTab = new Tab("Body", bodyTextArea);
        bodyTab.setClosable(false);
        this.getTabs().addAll(headersTab, bodyTab);
    }

    public String getBody() {
        return bodyTextArea.getText();
    }

    public List<Header> getHeaders() {
        return headersTableView.getHeaders();
    }

    public void setBody(String body) {
        bodyTextArea.setText(body);
    }

    public void setHeaders(List<Header> headers) {
        headersTableView.setHeaders(headers);
    }
}
