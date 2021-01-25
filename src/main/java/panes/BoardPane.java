package main.java.panes;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BoardPane extends GridPane {
    public final LeftPane leftPane;
    public final RightPane rightPane;
    public final TopPane topPane;
    public final DistrictPane districtPane;


    public BoardPane() {
        NumberBinding districtSize = Bindings.min(widthProperty().multiply(0.8), heightProperty().multiply(0.9));
        NumberBinding sideWidth = widthProperty().subtract(districtSize).divide(2);
        NumberBinding topHeight = heightProperty().subtract(districtSize);

        topPane = new TopPane();
        topPane.prefWidthProperty().bind(districtSize);
        topPane.prefHeightProperty().bind(topHeight);
        add(topPane, 1, 0);

        districtPane = new DistrictPane();
        districtPane.prefWidthProperty().bind(districtSize);
        districtPane.prefHeightProperty().bind(districtSize);
        VBox.setVgrow(districtPane, Priority.ALWAYS);
        HBox.setHgrow(districtPane, Priority.ALWAYS);
        add(districtPane, 1, 1);

        leftPane = new LeftPane();
        leftPane.prefHeightProperty().bind(heightProperty());
        leftPane.prefWidthProperty().bind(sideWidth);
        add(leftPane, 0, 0, 1, 2);

        rightPane = new RightPane();
        rightPane.prefHeightProperty().bind(heightProperty());
        rightPane.prefWidthProperty().bind(sideWidth);
        add(rightPane, 2, 0, 1, 2);
        setStyle("-fx-background-image: url(background.png);" +
                "-fx-background-repeat: stretch;" +
                "-fx-background-size: stretch;" +
                "-fx-background-position: center center;");
    }
}