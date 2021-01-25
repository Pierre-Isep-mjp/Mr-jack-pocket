package main.java.panes;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import main.java.Controller;
import main.java.FXHelper;

import java.util.HashMap;

public class FrontPane extends VBox {
    public final double defaultDelay = 3000;
    private final Label infoLabel;
    private final HBox choiceHBox;

    public FrontPane() {
        setAlignment(Pos.CENTER);
        setVisible(false);
        setOpacity(0.0);
        infoLabel = new Label();
        FXHelper.setDefaultLabeled(infoLabel, 30);
        infoLabel.prefWidthProperty().bind(prefWidthProperty().multiply(0.5));
        infoLabel.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));
        choiceHBox = new HBox();
        choiceHBox.prefWidthProperty().bind(prefWidthProperty().multiply(0.5));
        choiceHBox.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));
        FXHelper.setDefaultHBox(choiceHBox);
        RadialGradient shadePaint = new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.TRANSPARENT)
        );
        setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(-10))));
    }

    public void showText(String text) {
        showText(text, defaultDelay);
    }

    public void showText(String text, double delay) {
        infoLabel.setText(text);
        getChildren().add(infoLabel);
        temporarilyShow(delay);
    }

    public void showImage(ImageView imageView) {
        showImage(imageView, defaultDelay);
    }

    public void showImage(ImageView imageView, double delay) {
        double imageSize = 0.5;
        imageView.fitWidthProperty().bind(prefWidthProperty().multiply(imageSize));
        imageView.fitHeightProperty().bind(prefHeightProperty().multiply(imageSize));
        imageView.setPreserveRatio(true);
        getChildren().add(imageView);
        temporarilyShow(delay);
    }

    private void temporarilyShow(double delay) {
        show(delay / 10);
        new Timeline(new KeyFrame(Duration.millis(delay * 0.8), ev -> hide(delay / 10))).play();
    }

    private void show(double animationDelay) {
        setVisible(true);
        FadeTransition appear = new FadeTransition(Duration.millis(animationDelay), this);
        appear.setFromValue(0.0);
        appear.setToValue(1.0);
        appear.play();
    }

    private void hide(double animationDelay) {
        FadeTransition fade = new FadeTransition(Duration.millis(animationDelay), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            setVisible(false);
            getChildren().clear();
            Controller.playNextAction();
        });
        fade.play();
    }

    public void giveChoice(HashMap<String, Runnable> choices) {
        choiceHBox.getChildren().clear();
        for (String key : choices.keySet()) {
            Button button = new Button(key);
            FXHelper.setDefaultLabeled(button, 20);
            button.setOnAction(e -> {
                Controller.setNextAction(choices.get(key));
                hide(defaultDelay / 10);
            });
            button.defaultButtonProperty().bind(button.focusedProperty());
            choiceHBox.getChildren().add(button);
        }
        getChildren().add(choiceHBox);
        show(defaultDelay / 10);
    }
}
