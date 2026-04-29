package com.example.domacinotinstaledfx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HelloApplication extends Application {

    double velocityY = 0;
    double gravity = 0.5;

    Set<KeyCode> keysPressed = new HashSet<>();

    boolean onGround = false;

    int jumpBuffer = 0;
    boolean inventoryOpen = false;
    int selectedBlock = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Random random = new Random();

        VBox mainDisplay = new VBox();

        int blocksize = 32;
        int sceneW = 1280;
        int sceneH = 832;

        HBox[] rows = new HBox[sceneH / blocksize];

        for (int i = 0; i < (sceneH / blocksize); i++) {
            rows[i] = new HBox();
            rows[i].setPrefHeight(blocksize);
            rows[i].setMaxWidth(sceneW);
            mainDisplay.getChildren().add(rows[i]);
        }

        Pane root = new Pane();



        VBox playerHitBox = new VBox();
        playerHitBox.getChildren().add(new ImageView(
                new Image(getClass().getResource("/steveTexture/steve_headXL.png").toExternalForm())
        ));
        playerHitBox.getChildren().add(new ImageView(
                new Image(getClass().getResource("/steveTexture/steve_bodyXL.png").toExternalForm())
        ));

        playerHitBox.setLayoutX(0);
        playerHitBox.setLayoutY(4 * 32);

        root.getChildren().add(mainDisplay);
        root.getChildren().add(playerHitBox);



        Block[][] world = new Block[sceneW / blocksize + 1][sceneH / blocksize + 1];

        int curentRow = 0;

        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j < sceneW / blocksize; j++) {
                world[j][curentRow] = new Block(0, j, curentRow);
                rows[i].getChildren().add(world[j][curentRow].getView());
            }
            curentRow++;
        }

        for (int j = 0; j < sceneW / blocksize; j++) {
            world[j][curentRow] = new Block(4, j, curentRow);
            rows[6].getChildren().add(world[j][curentRow].getView());
        }
        curentRow++;

        for (int i = 7; i <= 12; i++) {
            for (int j = 0; j < sceneW / blocksize; j++) {
                world[j][curentRow] = new Block(3, j, curentRow);
                rows[i].getChildren().add(world[j][curentRow].getView());
            }
            curentRow++;
        }

        for (int i = 13; i <= 15; i++) {
            for (int j = 0; j < sceneW / blocksize; j++) {
                world[j][curentRow] = new Block(5, j, curentRow);
                rows[i].getChildren().add(world[j][curentRow].getView());
            }
            curentRow++;
        }

        for (int i = 16; i <= 23; i++) {
            for (int j = 0; j < sceneW / blocksize; j++) {
                if (random.nextInt(11) == 10) {
                    world[j][curentRow] = new Block(2, j, curentRow);
                } else {
                    world[j][curentRow] = new Block(5, j, curentRow);
                }
                rows[i].getChildren().add(world[j][curentRow].getView());
            }
            curentRow++;
        }

        for (int i = 24; i < 26; i++) {
            for (int j = 0; j < sceneW / blocksize; j++) {
                world[j][curentRow] = new Block(1, j, curentRow);
                rows[i].getChildren().add(world[j][curentRow].getView());
            }
            curentRow++;
        }



        Pane inventoryPane = new Pane();
        inventoryPane.setVisible(false);
        inventoryPane.setPrefSize(210, 150);
        inventoryPane.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        root.getChildren().add(inventoryPane);

        String[] textures = {
            "",
            "",
            "/blockTextures/diamond_oreB.png",
            "/blockTextures/dirtB.png",
            "/blockTextures/grass_blockB.png",
            "/blockTextures/stoneB.png"
        };

        for (int i = 0; i < textures.length; i++) {

            int index = i;

            ImageView icon = new ImageView(
                    new Image(getClass().getResource(textures[i]).toExternalForm())
            );

            icon.setFitWidth(48);
            icon.setFitHeight(48);

            icon.setLayoutX(20 + (i % 3) * 60);
            icon.setLayoutY(20 + (i / 3) * 60);

            icon.setOnMouseClicked(e -> {
                selectedBlock = index;
            });

            inventoryPane.getChildren().add(icon);
        }




        Scene scene = new Scene(root, sceneW, sceneH);

        stage.setTitle("2D MINECRAFT");
        stage.setScene(scene);




        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (inventoryOpen) return;


                if (jumpBuffer > 0) jumpBuffer--;

                if (jumpBuffer > 0 && onGround) {
                    velocityY = -10;
                    onGround = false;
                    jumpBuffer = 0;
                }


                double newX = playerHitBox.getLayoutX();


                if (keysPressed.contains(KeyCode.A)) newX -= 3;
                if (keysPressed.contains(KeyCode.D)) newX += 3;
                if (newX < 0) newX = 0;
                if (newX > sceneW - 32) newX = sceneW - 32;


                int xLeft = (int)(newX / blocksize);
                int xRight = (int)((newX + 31) / blocksize);

                int yTop = (int)(playerHitBox.getLayoutY() / blocksize);
                int yBottom = (int)((playerHitBox.getLayoutY() + 63) / blocksize);

                boolean blocked =
                        world[xLeft][yTop].getType() != 0 ||
                                world[xLeft][yBottom].getType() != 0 ||
                                world[xRight][yTop].getType() != 0 ||
                                world[xRight][yBottom].getType() != 0;

                if (!blocked) {
                    playerHitBox.setLayoutX(newX);
                } else {

                    if (keysPressed.contains(KeyCode.D)) {
                        playerHitBox.setLayoutX(xRight * blocksize - 32);
                    }

                    if (keysPressed.contains(KeyCode.A)) {
                        playerHitBox.setLayoutX((xLeft + 1) * blocksize);
                    }
                }



                velocityY += gravity;

                double oldY = playerHitBox.getLayoutY();
                double newY = oldY + velocityY;

                int xLeftV = (int)(playerHitBox.getLayoutX() / blocksize);
                int xRightV = (int)((playerHitBox.getLayoutX() + 31) / blocksize);

                if (velocityY > 0) {

                    int start = (int)((oldY + 63) / blocksize);
                    int end = (int)((newY + 63) / blocksize);

                    for (int y = start; y <= end; y++) {

                        if (y < 0 || y >= world[0].length) continue;

                        if (world[xLeftV][y].getType() != 0 ||
                                world[xRightV][y].getType() != 0) {

                            velocityY = 0;
                            onGround = true;
                            playerHitBox.setLayoutY(y * blocksize - 64);
                            return;
                        }
                    }

                    onGround = false;
                    playerHitBox.setLayoutY(newY);
                }

                else if (velocityY < 0) {

                    int start = (int)(oldY / blocksize);
                    int end = (int)(newY / blocksize);

                    for (int y = start; y >= end; y--) {

                        if (y < 0 || y >= world[0].length) continue;

                        if (world[xLeftV][y].getType() != 0 ||
                                world[xRightV][y].getType() != 0) {

                            velocityY = 0;
                            playerHitBox.setLayoutY((y + 1) * blocksize);
                            return;
                        }
                    }
                    playerHitBox.setLayoutY(newY);

                }
                if (playerHitBox.getLayoutY() < 0) {

                    playerHitBox.setLayoutY(0);
                    velocityY = 0;
                }
            }
        };

        timer.start();
        scene.setOnMouseClicked(e -> {

            int x = (int)(e.getSceneX() / blocksize);
            int y = (int)(e.getSceneY() / blocksize);

            if (x < 0 || x >= world.length || y < 0 || y >= world[0].length)  return;


            if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {

                if (world[x][y].getType() != 0 && world[x][y].getType() != 1 ) {
                    Block air = new Block(0, x, y);
                    world[x][y] = air;
                    rows[y].getChildren().set(x, air.getView());
                }
            }


            if (e.getButton() == javafx.scene.input.MouseButton.SECONDARY) {

                if (world[x][y].getType() == 0) {
                    Block newBlock = new Block(selectedBlock, x, y);
                    world[x][y] = newBlock;
                    rows[y].getChildren().set(x, newBlock.getView());
                }
            }
        });

        scene.setOnKeyPressed(e -> {

            keysPressed.add(e.getCode());

            if (e.getCode() == KeyCode.E) {
                inventoryOpen = !inventoryOpen;
                inventoryPane.setVisible(inventoryOpen);
            }

            if (e.getCode() == KeyCode.SPACE) {
                jumpBuffer = 5;
            }
        });


        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
