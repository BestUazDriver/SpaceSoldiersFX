package com.sabitov;

import com.sabitov.models.Bullet;
import com.sabitov.models.Mob;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Offline extends Application {
    private boolean isStarted = false;
    private int height = 800;
    private int width = 800;
    private int bulletSpeed = 10;

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(width, height);
        Mob mob1 = new Mob();
        Mob mob2 = new Mob();
        mob2.setyPos(width / 2);
        Bullet bullet1 = new Bullet(mob1.getyPos(), 0);
        Bullet bullet2 = new Bullet(mob1.getyPos(), height);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(graphicsContext, mob1, mob2, bullet1, bullet2)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        canvas.setOnMouseClicked(e -> isStarted = true);
        canvas.setOnMouseMoved(e -> mob1.setyPos(e.getY()));
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        timeline.play();
    }


    private void run(GraphicsContext context, Mob mob1, Mob mob2, Bullet bullet1, Bullet bullet2) {
        mob2.setxPos(height - mob2.WIDHT);
        if (mob2.getyPos() < mob1.getyPos()) {
            mob2.setyPos(mob2.getyPos() + 2);
        } else {
            mob2.setyPos(mob2.getyPos() - 2);
        }
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, width, height);
        context.setFill(Color.WHITE);
        context.setFont(Font.font(25));
        if (isStarted) {
            bullet1.setxPos(bullet1.getxPos() + bulletSpeed);
            bullet1.setyPos(bullet1.getyPos());
            context.fillOval(bullet1.getxPos(), bullet1.getyPos(), bullet1.RADIUS, bullet1.RADIUS);
            bullet2.setxPos(bullet2.getxPos() - bulletSpeed);
            bullet1.setyPos(bullet2.getyPos());
            context.fillOval(bullet2.getxPos(), bullet2.getyPos(), bullet2.RADIUS, bullet2.RADIUS);
        } else {
            context.setStroke(Color.WHITE);
            bullet1.setyPos(mob1.getyPos());
            context.setTextAlign(TextAlignment.CENTER);
            context.strokeText("Click to Start", width / 2, height / 2);
        }
        context.fillRect(mob1.getxPos(), mob1.getyPos(), mob1.WIDHT, mob1.HEIGHT);
        context.fillRect(mob2.getxPos(), mob2.getyPos(), mob2.WIDHT, mob2.HEIGHT);

        if (bullet1.getxPos() >= 800) {
            bullet1.setyPos(mob1.getyPos());
            bullet1.setxPos(0);
        }
        if (bullet2.getxPos() <= 0) {
            bullet2.setyPos(mob2.getyPos());
            bullet2.setxPos(height);
        }
        boolean p1 = false;
        boolean p2 = false;
        if (bullet1.getxPos() > height - mob2.WIDHT - bullet1.RADIUS / 2 && bullet1.getyPos() < mob2.getyPos() + mob2.HEIGHT / 2 + bullet1.RADIUS / 2 && bullet1.getyPos() > mob2.getyPos() - mob2.HEIGHT / 2 - bullet1.RADIUS / 2) {
            p1 = true;
            isStarted = false;
        }
        if (bullet2.getxPos() < mob1.WIDHT + bullet1.RADIUS / 2 && bullet2.getyPos() < mob1.getyPos() + mob1.HEIGHT / 2 + bullet2.RADIUS / 2 && bullet2.getyPos() > mob1.getyPos() - mob1.HEIGHT / 2 - bullet2.RADIUS / 2) {
            p2 = true;
            isStarted = false;
        }
        if (p2 && p1) {
            context.fillText("DRAW!", height / 2, width / 2 - 40);
        }else {
            if (p1) {
                context.fillText("Player 1 win", height / 2, width / 2 - 40);
            }
            if (p2) {
                context.fillText("Player 2 win", height / 2, width / 2 - 40);
            }
        }
    }
}
