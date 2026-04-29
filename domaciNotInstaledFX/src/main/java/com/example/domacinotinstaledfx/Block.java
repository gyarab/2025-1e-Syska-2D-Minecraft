package com.example.domacinotinstaledfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
    int type;
    ImageView view;
    int xCordinate;
    int yCordinate;

    public Block(int type,int x, int y) {
        this.type = type;
        Image texture = null;
        xCordinate = x;
        yCordinate = y;

        texture = switch (this.type) {
            case 0 -> new Image(getClass().getResource("/blockTextures/air2B.png").toExternalForm());
            case 1 -> new Image(getClass().getResource("/blockTextures/bedrockB.png").toExternalForm());
            case 2 -> new Image(getClass().getResource("/blockTextures/diamond_oreB.png").toExternalForm());
            case 3 -> new Image(getClass().getResource("/blockTextures/dirtB.png").toExternalForm());
            case 4 -> new Image(getClass().getResource("/blockTextures/grass_blockB.png").toExternalForm());
            case 5 -> new Image(getClass().getResource("/blockTextures/stoneB.png").toExternalForm());
            default -> texture;
        };


        view = new ImageView(texture);
        view.setFitWidth(32);
        view.setFitHeight(32);

    }

    public ImageView getView() {
        return view;
    }

    public int getType() {
        return type;
    }
}
