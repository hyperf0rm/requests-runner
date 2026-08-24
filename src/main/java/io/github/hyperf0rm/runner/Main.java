package io.github.hyperf0rm.runner;

import io.github.hyperf0rm.runner.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public void start(Stage stage) {
        Scene scene = new Scene(new MainView());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    static void main() {
        launch();
    }
}
