package main.java.panes;

import javafx.scene.layout.VBox;
import main.java.FXHelper;
import main.java.alibis.AlibiDeck;
import main.java.tokens.ActionToken;

public class RightPane extends VBox {
    public final AlibiDeck alibiDeck;
    public final TokensPane<ActionToken> actionTokens;

    RightPane() {
        FXHelper.setDefaultVBox(this);
        alibiDeck = new AlibiDeck(prefWidthProperty(), prefHeightProperty());
        ActionToken[] actionTokensList = new ActionToken[4];
        for (int i = 0; i < 4; i++) actionTokensList[i] = new ActionToken(i + 1);
        actionTokens = new TokensPane<>(actionTokensList);

        actionTokens.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));

        getChildren().addAll(alibiDeck, actionTokens);
    }
}
