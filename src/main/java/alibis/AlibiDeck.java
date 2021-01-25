package main.java.alibis;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.ImageView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlibiDeck extends ImageView {
    private final ArrayDeque<Alibi> alibis = init();

    public AlibiDeck(DoubleProperty width, DoubleProperty height) {
        super("/alibi_cards/alibi_hidden.png");
        //we want the alibi cards from left and right panel to be the same size,
        // but since they are oriented in a different way we do this little size calculation:
        fitWidthProperty().bind(Bindings.min(width.multiply(0.4), height.multiply(0.2)));
        fitHeightProperty().bind(Bindings.min(height.multiply(0.4), width.multiply(0.4)));
        setPreserveRatio(true);
    }

    private static ArrayDeque<Alibi> init() {
        List<Alibi> alibisList = new ArrayList<>();
        for (int id = 1; id <= 9; id++) alibisList.add(Alibi.getAlibi(id));
        Collections.shuffle(alibisList);
        return new ArrayDeque<>(alibisList);
    }

    public Alibi draw() {
        return alibis.poll();
    }
}
