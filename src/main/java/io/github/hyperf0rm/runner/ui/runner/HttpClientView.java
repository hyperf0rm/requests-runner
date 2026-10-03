package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.repository.RequestHistoryRepository;
import io.github.hyperf0rm.runner.repository.RequestTabRepository;
import javafx.scene.control.SplitPane;

public class HttpClientView extends SplitPane {

    private final RequestHistoryRepository historyRepository = new RequestHistoryRepository();
    private final RequestTabRepository tabRepository = new RequestTabRepository();
    private final RequestListPanel requestListPanel;
    private final RunnerTabPane runnerTabPane;

    public HttpClientView() {
        this.runnerTabPane = new RunnerTabPane(historyRepository, tabRepository);
        this.requestListPanel = new RequestListPanel(historyRepository, runnerTabPane::openRequestInNewTab);
        this.setDividerPositions(0.2);
        this.getItems().addAll(requestListPanel, runnerTabPane);
    }

    public void saveState() {
        runnerTabPane.saveRequestTabs();
    }
}
