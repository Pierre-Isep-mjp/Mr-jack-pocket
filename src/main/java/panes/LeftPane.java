package main.java.panes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import main.java.FXHelper;
import main.java.alibis.AlibiDeck;
import main.java.tokens.TurnToken;

public class LeftPane extends VBox {
    public final AlibiDeck MrJackAlibi;
    public final TokensPane<TurnToken> turnTokens;

    LeftPane() {
        FXHelper.setDefaultVBox(this);
        MrJackAlibi = new AlibiDeck(prefWidthProperty(), prefHeightProperty());
        MrJackAlibi.setRotate(90);

        TurnToken[] turnTokensList = new TurnToken[8];
        for (int i = 0; i < 8; i++) turnTokensList[i] = new TurnToken(i + 1);
        turnTokens = new TokensPane<>(turnTokensList);
        turnTokens.prefHeightProperty().bind(prefHeightProperty().multiply(0.7));

        getChildren().addAll(MrJackAlibi, turnTokens);
    }

    public void removeTurnToken() {
        ObservableList<Node> children = turnTokens.getChildren();
        children.remove(children.size() - 1);
    }
}
