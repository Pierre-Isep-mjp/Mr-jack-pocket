package main.java.tokens;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Controller;
import main.java.FXHelper;

public abstract class Token extends ImageView {
    protected final Image recto;
    protected final Image verso;
    protected final int id;
    protected boolean onRecto;

    protected Token(int id, String rectoFilename, String versoFilename) {
        this.id = id;
        recto = new Image(rectoFilename);
        verso = new Image(versoFilename);
        onRecto = true;
        setImage(recto);
        setOnMouseClicked(e -> Controller.setClickedNode(this));
        FXHelper.setClickable(this, false);
    }

    public void flip() {
        onRecto = !onRecto;
        FXHelper.flipAnimation(this, (onRecto) ? recto : verso, Controller::playNextAction);
    }

    public int getTokenId() {
        return id;
    }

    public boolean isOnRecto() {
        return onRecto;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + id;
    }
}
