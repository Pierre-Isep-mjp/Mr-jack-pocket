package main.java.players;

import main.java.Controller;
import main.java.alibis.Alibi;
import main.java.tokens.ActionToken;

import java.util.Set;
import java.util.Stack;

public class MrJack extends Player {
    private final Alibi identity;
    private int hourglassesCount = 0;

    public MrJack(Alibi identity) {
        super();
        this.identity = identity;
    }

    public void drawIdentity() {
        String str = this + " is drawing an alibi card, this is going to be his identity, only he should see it";
        Controller.getGui().frontPane.showText(str);
        Controller.setNextAction(() -> Controller.getGui().frontPane.showImage(identity));
    }

    public void turnActionTokensOver(Set<ActionToken> actionTokens) {
        Controller.getGui().frontPane.showText(this + " is turning the action tokens over");
        Stack<Runnable> actions = new Stack<>();
        for (ActionToken actionToken : actionTokens) actions.add(actionToken::flip);
        Controller.setNextActions(actions);
    }

    public Alibi getIdentity() {
        return identity;
    }

    public void addHourglasses(int count) {
        this.hourglassesCount += count;
    }

    public int getHourglassesCount() {
        return hourglassesCount;
    }
}
