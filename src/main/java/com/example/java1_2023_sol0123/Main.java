package com.example.java1_2023_sol0123;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<>();
    private ArrayList<Node> coins = new ArrayList<>();
    private boolean running = true;
    private boolean gameOver = false;

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int levelWidth = 1280;
    private int levelHeight = 720;
    private int score = 0;
    Level level = new Level();
    private void initContent(){
        Rectangle bg = new Rectangle(levelWidth, levelHeight);

        for (int i = 0; i < level.levelList.size(); i++){
            String line = level.levelList.get(i);
            for (int j = 0; j < line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.GREEN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createEntity(j*60,i*60,30,30,Color.WHITE);
                        coins.add(coin);
                        break;
                }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.YELLOW);

        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
    private void update(GraphicsContext gc){
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5){
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >=5){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-5){
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());

        for (Node coin : coins){
            if(player.getBoundsInParent().intersects(coin.getBoundsInParent())){
                coin.getProperties().put("exists",false);
            }
        }
        for (Iterator<Node> it = coins.iterator();it.hasNext();){
            Node coin = it.next();
            if(!(Boolean)coin.getProperties().get("exists")){
                it.remove();
                score += 1;
                gc.clearRect(0,0,levelWidth,levelHeight);
                gameRoot.getChildren().remove(coin);
            }
        }

        if(score == 12){
            gc.setFill(Color.GOLD);
            gc.setFont(new Font("", 50));
            gc.fillText("YOU WON!", levelWidth/2-100, levelHeight/2-50);
            running = false;
            return;
        }

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", levelWidth/2-100, levelHeight/2-50);
            running = false;
            return;
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + score, 10, 30);

    }


    private void movePlayerX(int value){
        boolean movingRight = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            if(player.getTranslateX() < 0 || player.getTranslateX() > levelWidth) {
                return;
            }
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingRight){
                        if (player.getTranslateX() + 40 == platform.getTranslateX()){
                            return;
                        }
                    }else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    private void movePlayerY(int value){
        boolean movingDown = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            if(player.getTranslateY() < 0) {
                return;
            }
            if(player.getTranslateY() + 40 >= levelHeight){
                gameOver = true;
                return;
            }
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if (player.getTranslateY() + 40 == platform.getTranslateY()){
                            canJump = true;
                            return;
                        }
                    }else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    private void jumpPlayer(){
        if(canJump){
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }
    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("exists",true); //V classe dát jako .isAlive / .setAlive

        gameRoot.getChildren().add(entity);
        return entity;

    }
    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Chuckie egg");
        primaryStage.setScene(scene);
        primaryStage.show();
        Canvas c = new Canvas(levelWidth, levelHeight);
        GraphicsContext gc = c.getGraphicsContext2D();
        gameRoot.getChildren().add(c);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(running){
                    update(gc);
                }
            }
        };
        timer.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}