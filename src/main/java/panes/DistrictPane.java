package main.java.panes;

import javafx.scene.layout.Pane;
import main.java.tokens.Detective;

public class DistrictPane extends Pane {
    public final TilesGrid tilesGrid;
    private final Detective holmes;
    private final Detective watson;
    private final Detective toby;


    DistrictPane() {
        tilesGrid = new TilesGrid();
        tilesGrid.prefHeightProperty().bind(heightProperty());
        tilesGrid.prefWidthProperty().bind(widthProperty());
        getChildren().add(tilesGrid);
        holmes = new Detective("holmes", 1, 0);
        watson = new Detective("watson", 1, 4);
        toby = new Detective("toby", 4, 2);
        getChildren().addAll(holmes, watson, toby);
        holmes.initDimensions(widthProperty(), heightProperty());
        watson.initDimensions(widthProperty(), heightProperty());
        toby.initDimensions(widthProperty(), heightProperty());
    }

    public Detective getHolmes() {
        return holmes;
    }

    public Detective getWatson() {
        return watson;
    }

    public Detective getToby() {
        return toby;
    }

    public Detective[] getDetectives() {
        Detective[] detectivePieces = new Detective[3];
        detectivePieces[0] = holmes;
        detectivePieces[1] = watson;
        detectivePieces[2] = toby;
        return detectivePieces;
    }
}
