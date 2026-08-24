package io.github.hyperf0rm.runner.ui;

import javafx.scene.layout.BorderPane;


public class MainView extends BorderPane{

    public MainView() {
        setTop(new TopBar());
    }
}
