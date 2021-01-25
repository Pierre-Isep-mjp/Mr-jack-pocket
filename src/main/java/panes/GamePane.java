package main.java.panes;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class GamePane extends AnchorPane {
    public final BoardPane boardPane;
    public final FrontPane frontPane;

    public GamePane() {
        boardPane = new BoardPane();
        setPane(boardPane);
        frontPane = new FrontPane();
        setPane(frontPane);
        getChildren().addAll(boardPane, frontPane);
    }

    private void setPane(Pane pane) {
        pane.prefHeightProperty().bind(heightProperty());
        pane.prefWidthProperty().bind(widthProperty());
        setRightAnchor(pane, 0.0);
        setLeftAnchor(pane, 0.0);
        setTopAnchor(pane, 0.0);
        setBottomAnchor(pane, 0.0);
    }
}
