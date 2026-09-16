package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.model.HttpTableEntry;
import javafx.scene.control.*;

import java.util.*;

public class RequestTabsPane extends TabPane {

    private final HttpEntryTableView paramsTableView = new HttpEntryTableView(true, HttpEntryTableView.Type.PARAM);
    private final HttpEntryTableView headersTableView = new HttpEntryTableView(true, HttpEntryTableView.Type.HEADER);
    private final TextArea bodyTextArea = new TextArea();

    public RequestTabsPane() {
        Tab paramsTab = new Tab("Params", paramsTableView);
        paramsTab.setClosable(false);
        Tab headersTab = new Tab("Headers", headersTableView);
        headersTab.setClosable(false);
        Tab bodyTab = new Tab("Body", bodyTextArea);
        bodyTab.setClosable(false);
        this.getTabs().addAll(paramsTab, headersTab, bodyTab);
    }

    public String getBody() {
        return bodyTextArea.getText();
    }

    public List<HttpTableEntry> getHeaders() {
        return headersTableView.getEntries();
    }

    public void setBody(String body) {
        bodyTextArea.setText(body);
    }

    public void setHeaders(List<HttpTableEntry> headers) {
        headersTableView.setEntries(headers);
    }

    public HttpEntryTableView getParamsTable() {
        return paramsTableView;
    }
}
