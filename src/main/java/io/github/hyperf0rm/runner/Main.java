package io.github.hyperf0rm.runner;

import atlantafx.base.theme.NordDark;
import io.github.hyperf0rm.runner.ui.MainTabPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    private MainTabPane ui;

    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        ui = new MainTabPane();
        Scene scene = new Scene(ui);
        String cssPath = getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Runner");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void stop() {
        ui.saveState();
    }

    public static void main(String[] args) {
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "expect,host,connection,content-length,upgrade");
        launch(args);
    }
}
