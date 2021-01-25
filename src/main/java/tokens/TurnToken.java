package main.java.tokens;

public class TurnToken extends Token {

    public TurnToken(int id) {
        super(id, "/time_tokens/" + "time_" + id + ".png",
                "/time_tokens/hourglass.png");
    }
}
