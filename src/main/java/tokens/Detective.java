package main.java.tokens;

import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.java.Controller;
import main.java.FXHelper;

import java.util.Stack;


public class Detective extends ImageView {
    private final String name;
    private final IntegerProperty column = new SimpleIntegerProperty();
    private final IntegerProperty row = new SimpleIntegerProperty();

    public Detective(String name, int row, int column) {
        this.name = name;
        setImage(new Image("/detective_tokens/" + "detective_" + name + ".png"));
        setPosition(row, column);
        setOnMouseClicked(e -> Controller.setClickedNode(this));
        FXHelper.setClickable(this, false);
    }

    public void move(int count) {
        int i = 0;
        int row = getRow();
        int col = getColumn();
        Stack<Runnable> moves = new Stack<>();
        while (i < count) {
            int rowIncr;
            int colIncr;
            if (row == 0 || row == 4) {
                colIncr = (row == 0) ? 1 : -1;
                rowIncr = (col - row) / 3;
            } else {
                rowIncr = (col == 0) ? -1 : 1;
                colIncr = (row - col) % 3 % 2;
            }
            row += rowIncr;
            col += colIncr;
            int finalCol = col;
            int finalRow = row;
            moves.add(() -> moveTo(finalCol, finalRow));
            i++;
        }
        Controller.setNextActions(moves);
        Controller.playNextAction();
    }

    public void moveTo(int newColumn, int newRow) {
        moveTo(newColumn, newRow, Controller::playNextAction);
    }

    public void moveTo(int newColumn, int newRow, Runnable callback) {
        //movement of the nodes formula, depending on grid size and number of columns/rows
        double deltaX = fitWidthProperty().getValue() * 2.5 * (newColumn - column.getValue());
        double deltaY = fitHeightProperty().getValue() * 2.5 * (newRow - row.getValue());
        TranslateTransition translation = new TranslateTransition(
                new Duration(500), this);
        //I have no idea why it's behaving like that (why -delta instead of +delta)
        translation.setFromX(-deltaX);
        translation.setFromY(-deltaY);
        translation.setToX(0);
        translation.setToY(0);
        translation.setOnFinished(e -> callback.run());
        translation.play();
        row.setValue(newRow);
        column.setValue(newColumn);
    }

    public void setPosition(int row, int column) {
        this.row.setValue(row);
        this.column.setValue(column);
    }

    public void initDimensions(ReadOnlyDoubleProperty parentWidth, ReadOnlyDoubleProperty parentHeight) {
        //automatic resizing and repositioning of the node when the window is resized
        fitWidthProperty().bind(parentWidth.multiply(0.08));
        fitHeightProperty().bind(parentHeight.multiply(0.08));
        xProperty().bind(
                fitWidthProperty().multiply((column.multiply(2).add(1))
                        .multiply(1.25).subtract(0.5)));
        yProperty().bind(
                fitHeightProperty().multiply((row.multiply(2).add(1))
                        .multiply(1.25).subtract(0.5)));
    }

    public int getColumn() {
        return column.get();
    }

    public int getRow() {
        return row.get();
    }

    @Override
    public String toString() {
        return "Detective " + name;
    }
}
