package com.sabitov.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {

    private final int PORT;
    public final InetAddress address;

    public Game(int PORT) throws Exception {
        this.PORT = PORT;
        address = InetAddress.getByName("localhost");
    }

    public void startGame() {
        try (ServerSocket socket = new ServerSocket(PORT, 50, address)) {
            Socket client1 = socket.accept();
            Socket client2 = socket.accept();
            new Thread(new MainThread(client1, client2, socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
