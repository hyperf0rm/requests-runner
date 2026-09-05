package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.ui.runner.MainRunnerView;
import io.github.hyperf0rm.runner.ui.tools.ToolsView;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    private final MainRunnerView mainRunnerView = new MainRunnerView();
    private final ToolsView toolsView = new ToolsView();

    public MainTabPane() {
        this.setSide(Side.TOP);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        Tab runnerTab = new Tab("Runner", mainRunnerView);
        Tab toolsTab = new Tab("Tools", toolsView);
        this.getTabs().addAll(runnerTab, toolsTab);
    }
}
