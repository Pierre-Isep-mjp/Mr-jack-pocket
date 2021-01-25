package main.java.alibis;

import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

public class Alibi extends ImageView {
    private static final List<Alibi> alibiList = new LinkedList<Alibi>() {{
        for (int id = 1; id <= 9; id++) add(new Alibi(id));
    }};
    private final int id;
    private final int hourglassesNumber;

    private Alibi(int id) {
        super("/alibi_cards/" + "alibi_" + id + ".jpg");
        int hourglassesNumber = 0;
        if (id > 2) hourglassesNumber++;
        if (id == 9) hourglassesNumber++;
        this.hourglassesNumber = hourglassesNumber;
        this.id = id;
    }

    public static Alibi getAlibi(int id) {
        for (Alibi alibi : alibiList) if (alibi.getAlibiId() == id) return alibi;
        return new Alibi(id);
    }

    public int getAlibiId() {
        return id;
    }

    public int getHourglassesNumber() {
        return hourglassesNumber;
    }

    @Override
    public String toString() {
        return "Alibi n°" + id;
    }
}
