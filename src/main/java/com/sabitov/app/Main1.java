package com.sabitov.app;

import com.sabitov.controller.SceneLoader;
import com.sabitov.controller.StartController;
import com.sabitov.models.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main1 extends Application {
    private Player player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        player = new Player();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxml/start.fxml"));
        Parent root = loader.load();
        stage.setTitle("Game");
        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);

        StartController startController = (StartController) loader.getController();
        startController.init(player, stage);

        stage.show();
    }
}
