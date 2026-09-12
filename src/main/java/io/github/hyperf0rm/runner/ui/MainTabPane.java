package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.ui.notes.NotesView;
import io.github.hyperf0rm.runner.ui.runner.RunnerTabPane;
import io.github.hyperf0rm.runner.ui.tools.ToolsView;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    private final RunnerTabPane runnerTabPane = new RunnerTabPane();
    private final ToolsView toolsView = new ToolsView();
    private final NotesView noteListView = new NotesView();

    public MainTabPane() {
        this.setSide(Side.TOP);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        Tab runnerTab = new Tab("Runner", runnerTabPane);
        Tab toolsTab = new Tab("Tools", toolsView);
        Tab notesTab = new Tab("Notes", noteListView);
        this.getTabs().addAll(runnerTab, toolsTab, notesTab);
    }
}
