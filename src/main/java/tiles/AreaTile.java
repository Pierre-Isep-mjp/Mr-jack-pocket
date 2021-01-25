package main.java.tiles;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import main.java.Controller;
import main.java.FXHelper;
import main.java.alibis.Alibi;

public class AreaTile extends Tile {
    public final Image hiddenImage;
    private final Alibi alibi;
    private boolean suspectIsVisible = true;
    private int orientation = 0;
    //the orientation correspond to the wall side orientation (N=0;W=1,S=2,E=3)

    public AreaTile(int suspectId) {
        super("/tiles/suspect_" + suspectId + "_tile.png");
        alibi = Alibi.getAlibi(suspectId);
        hiddenImage = new Image("/tiles/street_tile.png");
        this.setOnMouseClicked(e -> Controller.setClickedNode(this));
        FXHelper.setClickable(this, false);
    }

    public boolean suspectIsVisible() {
        return suspectIsVisible;
    }

    public void hideSuspect() {
        if (!suspectIsVisible) return;
        suspectIsVisible = false;
        FXHelper.flipAnimation(this, hiddenImage);
    }

    public void rotate(int count, boolean clockWise) {
        rotate(count, clockWise, Controller::playNextAction);
    }

    public void rotate(int count, boolean clockWise, Runnable callback) {
        orientation = (orientation + ((clockWise) ? -count : count) + 4) % 4;
        int angle = ((clockWise) ? 1 : -1) * count * 90;
        rotationAnimation(angle, callback);
    }

    public void rotationAnimation(int rotationAngle, Runnable callback) {
        RotateTransition rt = new RotateTransition(Duration.millis(500), this);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setByAngle(rotationAngle);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setOnFinished(e -> {
            setRotate(0);
            setAngle(rotationAngle);
            callback.run();
        });
        rt.play();
    }

    public void setOrientation(int orientation) {
        int i = (orientation - this.orientation + 4) % 4;
        boolean clockWise = i / 2 == 1;
        int count = (i == 3) ? 1 : i;
        rotate(count, clockWise, () -> {
        });
        this.orientation = orientation;
    }

    public void setAngle(double angle) {
        DoubleBinding xCenter = xProperty().add(fitHeightProperty().divide(2));
        DoubleBinding yCenter = yProperty().add(fitWidthProperty().divide(2));
        Rotate rotate = new Rotate(angle);
        rotate.pivotXProperty().bind(xCenter);
        rotate.pivotYProperty().bind(yCenter);
        getTransforms().add(rotate);
    }

    public Alibi getAlibi() {
        return alibi;
    }

    //(N=0;E=1,S=2,W=3)
    public boolean isBlocked(int orientation) {
        return this.orientation == orientation;
    }

    @Override
    public String toString() {
        return "AreaTile of " + alibi;
    }
}
