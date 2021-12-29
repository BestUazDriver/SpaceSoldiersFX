package com.sabitov.controller;

import com.sabitov.models.Player;
import com.sabitov.server.Connection;
import com.sabitov.server.Game;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Random;

public class StartController {
    Stage primaryStage;
    Player player;
    int PORT;
    String IP;

    @FXML
    public TextField portInput;

    public void init(Player player, Stage primaryStage) {
        this.player = player;
        this.primaryStage = primaryStage;
    }

    public void createGameBtnTapped() throws Exception {
        Random random = new Random();
        PORT = 6666;
        Game game = new Game(PORT);
        new Thread(new GameStarter(game)).start();

        Connection connection = new Connection(PORT, player);
        if (connection.isConnectedToServer()) {
            SceneLoader.showWaitingController(primaryStage, PORT);
            StartCommandListener startCommandListener = new StartCommandListener();
            startCommandListener.start();
            startCommandListener.setOnSucceeded(e -> {
                try {
                    SceneLoader.showGameController(primaryStage, player);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }

    public void joinToGameBtnTapped() throws Exception {
        PORT = Integer.parseInt(portInput.getText());
        IP = "127.0.0.1";
        Connection connection = new Connection(PORT, player);
        if (connection.isConnectedToServer()){
            SceneLoader.showWaitingController(primaryStage, PORT);
            StartCommandListener startCommandListener = new StartCommandListener();
            startCommandListener.start();
            startCommandListener.setOnSucceeded(e -> {
                try {
                    SceneLoader.showGameController(primaryStage, player);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }


    private class GameStarter implements Runnable {
        Game game;

        public GameStarter(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            game.startGame();
        }
    }

    private class StartCommandListener extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return player.isWaiting();
                }
            };
        }
    }
}