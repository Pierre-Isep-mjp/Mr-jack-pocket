package main.java.tiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Tile extends ImageView {
    final Image defaultImage;
    //rem

    Tile(String defaultImageFilename) {
        defaultImage = new Image(defaultImageFilename);
        setImage(defaultImage);
    }
}