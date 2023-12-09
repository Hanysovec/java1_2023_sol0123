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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode,Boolean>();
    private ArrayList<Node> platforms = new ArrayList<Node>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0,0);
    private int levelWidth;
    private boolean canJump = true;

    private void initContent(){
        Rectangle backGround = new Rectangle(1280,720);
        levelWidth = Level.LEVEL1[0].length()*60;

        for (int i = 0; i < Level.LEVEL1.length;i++){
            String line = Level.LEVEL1[i];
            for (int j = 0; j < line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60,i*60,60,60, Color.GREEN);
                        platforms.add(platform);
                        break;
                }
            }
        }
        player = createEntity(0,600,40,40,Color.YELLOW);

        appRoot.getChildren().addAll(backGround,gameRoot,uiRoot);
    }

    private void update(){
        if(isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }
        if(isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }
        if(isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth-5){
            movePlayerX(5);
        }
        if(playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0,1);
        }
        movePlayerY((int)playerVelocity.getY());
    }

    private void movePlayerX(int value){
        boolean movingRight = value > 0;
        for (int i = 0;i < Math.abs(value);i++){
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingRight){
                        if(player.getTranslateX() + 40 == platform.getTranslateX()){
                            return;
                        }
                    }
                    else{
                        if(player.getTranslateX() == platform.getTranslateX()+60){
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
        for (int i = 0;i < Math.abs(value);i++){
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if(player.getTranslateY() + 40 == platform.getTranslateY()){
                            player.setTranslateY(player.getTranslateY()-1);
                            canJump = true;
                            return;
                        }
                    }
                    else{
                        if(player.getTranslateY() == platform.getTranslateY()+60){
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingDown ? 1 : -1));
        }
    }
    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w,h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);

        gameRoot.getChildren().add(entity);
        return entity;
    }


    private void jumpPlayer(){
        if(canJump){
            playerVelocity = playerVelocity.add(0,-30);
            canJump = false;
        }
    }

    private Boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }
    @Override
    public void start(Stage stage) throws IOException {
        initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(),true));
        scene.setOnKeyPressed(event -> keys.put(event.getCode(),false));
        stage.setTitle("Chuckie egg");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        timer.start();

    }

    public static void main(String[] args) {
        launch();
    }
}