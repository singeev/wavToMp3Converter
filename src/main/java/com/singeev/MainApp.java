package com.singeev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        log.info("Starting Hello JavaFX and Maven demonstration application");
        mainStage = stage;

        String fxmlFile = "/fxml/sample.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        log.debug("Showing JFX scene");

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("WavToMp3");
        stage.setResizable(false);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getMainStage() { return mainStage;}
}
