package com.sabitov.server;

import com.sabitov.models.Bullet;
import com.sabitov.models.Mob;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainThread implements Runnable {
    private static final int width = 800;
    private static final int height = 800;

    private Thread firstPlayerListener = new Thread(new FirstPlayerListener());
    private Thread secondPlayerListener = new Thread(new SecondPlayerListener());


    private Socket socket1;
    private Socket socket2;
    private ServerSocket mainSocket;
    private Scanner scanner1;
    private Scanner scanner2;
    private PrintWriter printWriter1;
    private PrintWriter printWriter2;
    private Mob mob1;
    private Mob mob2;
    private Timeline tl;

    public MainThread(Socket socket1, Socket socket2, ServerSocket mainSocket) {
        this.socket1 = socket1;
        this.socket2 = socket2;
        this.mainSocket = mainSocket;
    }

    @Override
    public void run() {
        try {
            scanner1 = new Scanner(socket1.getInputStream());
            scanner2 = new Scanner(socket2.getInputStream());
            printWriter1 = new PrintWriter(socket1.getOutputStream(), true);
            printWriter2 = new PrintWriter(socket2.getOutputStream(), true);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        printWriter1.println("Start");
        printWriter2.println("Start");
        firstPlayerListener.start();
        secondPlayerListener.start();
        Bullet bullet1 = new Bullet(width / 2, height);
        Bullet bullet2 = new Bullet(width / 2, 0);

        tl = new Timeline(new KeyFrame(Duration.millis(10), e -> nextStep(bullet1, bullet2)));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }


    private class FirstPlayerListener implements Runnable {

        @Override
        public void run() {
            mob1 = new Mob();
            while (scanner1.hasNext()) {
                mob1.setyPos(Double.parseDouble(scanner1.nextLine()));
                printWriter2.println("oy:" + mob1.getyPos());
            }
        }
    }

    private class SecondPlayerListener implements Runnable {

        @Override
        public void run() {
            mob2 = new Mob();
            try {
                while (scanner2.hasNext()) {
                    mob2.setyPos(Double.parseDouble(scanner2.nextLine()));
                    printWriter1.println("oy:" + mob2.getyPos());
                }
            } catch (IndexOutOfBoundsException e) {
                mob2.setyPos(height);
            }
        }
    }

    private void nextStep(Bullet bullet1, Bullet bullet2) {
        bullet1.setxPos(bullet1.getxPos() + 10);
        bullet2.setxPos(bullet2.getxPos() - 10);

        printWriter1.println("bx1:" + bullet1.getxPos());
        printWriter1.println("bx2:" + bullet2.getxPos());
        printWriter2.println("bx1:" + (width - bullet1.getxPos()));
        printWriter2.println("bx2:" + (width - bullet2.getxPos()));
        printWriter1.println("by1:" + bullet1.getyPos());
        printWriter1.println("by2:" + bullet2.getyPos());
        printWriter2.println("by1:" + bullet1.getyPos());
        printWriter2.println("by2:" + bullet2.getyPos());

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
        }
        if (bullet2.getxPos() < mob1.WIDHT + bullet1.RADIUS / 2 && bullet2.getyPos() < mob1.getyPos() + mob1.HEIGHT / 2 + bullet2.RADIUS / 2 && bullet2.getyPos() > mob1.getyPos() - mob1.HEIGHT / 2 - bullet2.RADIUS / 2) {
            p2 = true;
        }
        if (p2 && p1) {
            printWriter1.write("go:DRAW!");
            printWriter2.write("go:DRAW!");
            endGame();
        } else {
            if (p1) {
                printWriter1.write("go:You win!");
                printWriter2.write("go:You lose!");
            }
            if (p2) {
                printWriter2.write("go:You win!");
                printWriter1.write("go:You lose!");
            }
        }
    }


    private void endGame() {
        try {
            tl.stop();
            mainSocket.close();
            firstPlayerListener.interrupt();
            secondPlayerListener.interrupt();
            socket1.close();
            socket2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

