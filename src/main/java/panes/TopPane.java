package main.java.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.java.FXHelper;

public class TopPane extends VBox {
    TopPane() {
        FXHelper.setDefaultVBox(this);
        Label titleLabel = new Label("Mr Jack Pocket");
        FXHelper.setDefaultLabeled(titleLabel, 40);
        titleLabel.setTextFill(Color.GOLD);
        getChildren().add(titleLabel);
    }
}
