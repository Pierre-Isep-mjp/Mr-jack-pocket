package main.java.panes;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import main.java.FXHelper;
import main.java.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class TokensPane<T extends Token> extends VBox {

    TokensPane(T[] tokensList) {
        FXHelper.setDefaultVBox(this);
        double heightPercentage = (100.0 - FXHelper.defaultPadding -
                FXHelper.defaultSpacing * (tokensList.length - 1)) / tokensList.length;
        for (T token : tokensList) {
            token.fitHeightProperty().bind(prefHeightProperty().multiply(heightPercentage).divide(100));
            token.setPreserveRatio(true);
            getChildren().add(token);
        }
    }

    public List<T> getTokens() {
        List<T> tokens = new ArrayList<>();
        for (Node token : getChildren()) //noinspection unchecked
            tokens.add((T) token);
        return tokens;
    }
}
