package io.github.hyperf0rm.runner;

import atlantafx.base.theme.NordDark;
import io.github.hyperf0rm.runner.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        Scene scene = new Scene(new MainView());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    static void main() {
        launch();
    }
}
