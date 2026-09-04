package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.ui.tools.ToolsView;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    private final MainView mainView = new MainView();
    private final ToolsView toolsView = new ToolsView();

    public MainTabPane() {
        this.setSide(Side.TOP);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        Tab runnerTab = new Tab("Runner", mainView);
        Tab toolsTab = new Tab("Tools", toolsView);
        this.getTabs().addAll(runnerTab, toolsTab);
    }
}
