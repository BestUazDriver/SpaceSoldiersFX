package com.sabitov.models;

public class Mob {
    public final int HEIGHT= 20;
    public final int  WIDHT= 10;
    private double yPos;
    private double xPos;

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public Mob(double yPos) {
        this.yPos = yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public Mob() {
    }
}
