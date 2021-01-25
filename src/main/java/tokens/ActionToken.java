package main.java.tokens;

public class ActionToken extends Token {
    public ActionToken(int id) {
        super(id, "/action_tokens/" + "action_" + id + "_recto.png",
                "/action_tokens/" + "action_" + id + "_verso.png");
    }
}
