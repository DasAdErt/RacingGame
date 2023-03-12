package com.example.racinggame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RacingGame extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int CAR_WIDTH = 50;
    private static final int CAR_HEIGHT = 100;
    private static final int CAR_SPEED = 5;
    private static final int OBSTACLE_WIDTH = 50;
    private static final int OBSTACLE_HEIGHT = 50;
    private static final int MAX_OBSTACLES = 5;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean gameOver = false;

    private double carX = WIDTH / 2 - CAR_WIDTH / 2;
    private double carY = HEIGHT - CAR_HEIGHT - 20;

    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();

    private int score = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.setCenter(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
                movingLeft = true;
            } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                movingRight = true;
            } else if (e.getCode() == KeyCode.R && gameOver) {
                restartGame();
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
                movingLeft = false;
            } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                movingRight = false;
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (!gameOver) {
                    update();
                    render(gc);
                } else {
                    renderGameOverScreen(gc);
                }
            }
        }.start();

        primaryStage.setTitle("RacingGame");
        primaryStage.getIcons().add(new Image("https://i.imgur.com/Xwf3Sm4.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void update() {
        // Обновляем позицию машины
        if (movingLeft) {
            carX -= CAR_SPEED;
        } else if (movingRight) {
            carX += CAR_SPEED;
        }
        if (carX < 0) {
            carX = 0;
        } else if (carX > WIDTH - CAR_WIDTH) {
            carX = WIDTH - CAR_WIDTH;
        }
        // Создаем препятствия
        if (obstacles.size() < MAX_OBSTACLES) {
            int randomNum = random.nextInt(100);
            if (randomNum < 10) {
                double obstacleX = random.nextInt(WIDTH - OBSTACLE_WIDTH);
                double obstacleY = -OBSTACLE_HEIGHT;
                Obstacle newObstacle = new Obstacle(obstacleX, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

                // Проверяем, пересекается ли новое препятствие с уже существующими препятствиями
                boolean intersectsExisting = false;
                for (Obstacle obstacle : obstacles) {
                    if (newObstacle.intersects(obstacle.getX(), obstacle.getY(), (int) obstacle.getWidth(), (int) obstacle.getHeight())) {
                        intersectsExisting = true;
                        break;
                    }
                }

                // Добавляем новое препятствие, если оно не пересекается ни с одним из уже существующих препятствий
                if (!intersectsExisting) {
                    obstacles.add(newObstacle);
                }
            }
        }

        // Обновляем позицию препятствий и начисляем очки за прохождение препятствий
        for (Obstacle obstacle : obstacles) {
            obstacle.setY(obstacle.getY() + 5);

            // Проверяем столкновение с машиной
            if (obstacle.intersects(carX, carY, CAR_WIDTH, CAR_HEIGHT)) {
                gameOver = true;
            } else if (obstacle.getY() > carY + CAR_HEIGHT && !obstacle.isCounted()) {
                // Добавляем очки за прохождение препятствия
                score += 1;
                obstacle.setCounted(true);
            }

            // Удаляем препятствие, если оно выходит за границы экрана
            if (obstacle.getY() > HEIGHT) {
                obstacles.remove(obstacle);
                break;
            }
        }
    }

        private void render(GraphicsContext gc) {
        // Очистить холст
        gc.clearRect(0, 0, WIDTH, HEIGHT);

            // Нарисовать машину
            gc.setFill(Color.BLUE);
            gc.fillRect(carX, carY, CAR_WIDTH, CAR_HEIGHT);

            // Нарисовать препятствия
            gc.setFill(Color.RED);
            for (Obstacle obstacle : obstacles) {
                gc.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            }

            // Нарисовать счет
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(20));
            gc.fillText("Score: " + score, 10, 30);
        }
    private void renderGameOverScreen(GraphicsContext gc) {
        // Очистить холст
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Вывести текст "Game over"
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(50));
        gc.fillText("GAME OVER", WIDTH / 2 - 150, HEIGHT / 2);

        // Вывести итоговый счет
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(20));
        gc.fillText("Final Score: " + score, WIDTH / 2 - 70, HEIGHT / 2 + 50);

        // Вывести инструкции для перезапуска
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(20));
        gc.fillText("Press R to restart", WIDTH / 2 - 90, HEIGHT / 2 + 100);
    }

    private void restartGame() {
        carX = WIDTH / 2 - CAR_WIDTH / 2;
        carY = HEIGHT - CAR_HEIGHT - 20;
        obstacles.clear();
        score = 0;
        gameOver = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}