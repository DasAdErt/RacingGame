package com.example.racinggame;

public class Obstacle {
    private double x;
    private double y;
    private double width;
    private double height;

    private boolean counted;

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    public Obstacle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(double x, double y, int width, int height) {
        return x < getX() + getWidth() && x + width > getX() && y < getY() + getHeight() && y + height > getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setY(double y) {
        this.y = y;
    }
}
