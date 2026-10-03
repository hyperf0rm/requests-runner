package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.ui.notes.NotesView;
import io.github.hyperf0rm.runner.ui.runner.HttpClientView;
import io.github.hyperf0rm.runner.ui.tools.ToolsView;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    private final HttpClientView httpClientView = new HttpClientView();
    private final ToolsView toolsView = new ToolsView();
    private final NotesView noteListView = new NotesView();

    public MainTabPane() {
        this.setSide(Side.TOP);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        Tab runnerTab = new Tab("Runner", httpClientView);
        Tab toolsTab = new Tab("Tools", toolsView);
        Tab notesTab = new Tab("Notes", noteListView);
        this.getTabs().addAll(runnerTab, toolsTab, notesTab);
    }

    public void saveState() {
        httpClientView.saveState();
    }
}
