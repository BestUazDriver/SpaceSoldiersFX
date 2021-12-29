package com.sabitov.models;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    private Scanner scanner;
    private PrintWriter printWriter;
    private Socket socket;

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isWaiting() {
        return scanner.hasNext();
    }
}
