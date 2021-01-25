package main.java;

import java.util.Random;

public interface RandomHelper {

    static int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    static boolean randomBoolean() {
        return new Random().nextBoolean();
    }
}
