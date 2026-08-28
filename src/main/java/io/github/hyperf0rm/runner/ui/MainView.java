package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.service.RunnerService;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainView extends BorderPane {

    private TopBar topBar = new TopBar();
    private Tabs tabs = new Tabs();

    public MainView() {
        this.topBar.getSendButton().setOnAction(event -> sendDummyRequest());
        setTop(topBar);
        setCenter(tabs);
        setMargin(tabs, new Insets(10, 400, 10, 10));
    }

    public void sendDummyRequest() {
        String url = topBar.getURL();
        HttpMethod method =  topBar.getMethod();
        String body = tabs.getBody();
        Map<String, String> headers = tabs.getHeaders();
        Request request = new Request(method, url, headers, body);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        RunnerService rs = new RunnerService(requests);
        rs.run();
    }
}
