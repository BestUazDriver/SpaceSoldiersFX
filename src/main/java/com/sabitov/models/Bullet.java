package com.sabitov.models;

public class Bullet {
    private double yPos;
    private double xPos;
    public final int RADIUS=5;

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public Bullet(double yPos, double xPos) {
        this.yPos = yPos;
        this.xPos = xPos;
    }
}
