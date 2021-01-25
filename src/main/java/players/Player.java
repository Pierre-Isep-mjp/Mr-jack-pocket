package main.java.players;

import main.java.Controller;
import main.java.FXHelper;
import main.java.alibis.Alibi;
import main.java.tokens.ActionToken;

import java.util.Set;
import java.util.Stack;

public abstract class Player {
    public void chooseActionToken(Set<ActionToken> actionTokens) {
        Controller.getGui().frontPane.showText(this + " chooses an action token");
        Stack<Runnable> actions = new Stack<>();
        actions.add(() -> {
            for (ActionToken actionToken : actionTokens) FXHelper.setClickable(actionToken, true);
            Controller.setCurrentPlayer(this);
        });
        actions.add(Controller::playClickedActionToken);
        Controller.setNextActions(actions);
    }

    public void drawCard(Alibi alibiCard) {
        String str = this + " is drawing an alibi card, ";
        if (this instanceof MrJack) {
            str += "only he should see it";
            ((MrJack) this).addHourglasses(alibiCard.getHourglassesNumber());
        } else {
            str += "everybody can see it";
        }
        Controller.getGui().frontPane.showText(str);
        Controller.setNextAction(() -> Controller.getGui().frontPane.showImage(alibiCard));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
