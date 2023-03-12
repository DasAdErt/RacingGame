package com.example.racinggame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 100;

    private double x;
    private double y;

    private Image image;

    public Car(double x, double y) {
        this.x = x;
        this.y = y;
        this.image = new Image("https://i.imgur.com/9cL1Klf.png");
    }

    public void moveLeft(double speed) {
        x -= speed;
    }

    public void moveRight(double speed) {
        x += speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y, WIDTH, HEIGHT);
    }

    public boolean intersects(Obstacle obstacle) {
        return (x + WIDTH > obstacle.getX() && x < obstacle.getX() + obstacle.getWidth() &&
                y + HEIGHT > obstacle.getY() && y < obstacle.getY() + obstacle.getHeight());
    }
}
