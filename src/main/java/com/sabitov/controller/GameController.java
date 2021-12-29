package com.sabitov.controller;

import com.sabitov.models.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    Player player;
    Stage primaryStage;
    Thread serverListener = new Thread(new ServerListener());

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Rectangle playerRectangle;

    @FXML
    private Rectangle opponentRectangle;

    @FXML
    private Circle bullet1;

    @FXML
    private Circle bullet2;

    @FXML
    private Text result;

    @FXML
    private Text clickForMenuLabel;

    private void setOpponentPositionY(double opponentPositionY) {
        opponentRectangle.setLayoutY(opponentPositionY);
    }

    private void setXBullet1(double positionX) {
        bullet1.setCenterX(positionX);
    }

    private void setXBullet2(double positionX) {
        bullet2.setCenterX(positionX);
    }

    private void setYBullet1(double positionY) {
        bullet1.setCenterY(positionY);
    }

    private void setYBullet2(double positionY) {
        bullet2.setCenterY(positionY);
    }


    private void setResult(String result) {
        this.result.setText(result);
        clickForMenuLabel.setText("Click for menu");
        anchorPane.setOnMouseMoved(null);
        anchorPane.setOnMouseClicked(e -> {
            try {
                SceneLoader.showStartController(primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        serverListener.interrupt();
        player = null;
    }

    public void init(Stage primaryStage, Player player) {
        this.primaryStage = primaryStage;
        this.player = player;
        anchorPane.setOnMouseMoved(e -> {
            playerRectangle.setLayoutY(e.getY());
            sendPositionToSever(e.getY());
        });
        player.getPrintWriter().println(250);
        serverListener.start();
    }

    private void sendPositionToSever(double y) {
        player.getPrintWriter().println(y);
    }

    private class ServerListener implements Runnable {

        @Override
        public void run() {
            try {
                while (player.getScanner().hasNext()) {
                    String answer = player.getScanner().nextLine();
                    if (answer.startsWith("bx1")) {
                        String bulletX = answer.split(":")[1];
                        Platform.runLater(() -> setXBullet1(Double.parseDouble(bulletX)));
                    } else if (answer.startsWith("by1")) {
                        String bulletY = answer.split(":")[1];
                        Platform.runLater(() -> setYBullet1(Double.parseDouble(bulletY)));
                    } else if (answer.startsWith("bx2")) {
                        String bulletX = answer.split(":")[1];
                        Platform.runLater(() -> setXBullet2(Double.parseDouble(bulletX)));
                    } else if (answer.startsWith("by2")) {
                        String bulletY = answer.split(":")[1];
                        Platform.runLater(() -> setYBullet2(Double.parseDouble(bulletY)));
                    } else if (answer.startsWith("oy")) {
                        String opponentY = answer.split(":")[1];
                        Platform.runLater(() -> setOpponentPositionY(Double.parseDouble(opponentY)));
                    } else if (answer.startsWith("go")) {
                        String result = answer.split(":")[1];
                        Platform.runLater(() -> setResult(result));
                    }
                }
            }catch(NullPointerException e){
                System.out.println("null");
            }
        }
    }

}
