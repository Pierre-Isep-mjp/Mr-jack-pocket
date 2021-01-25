package main.java.players;

import main.java.Controller;
import main.java.RandomHelper;
import main.java.tokens.ActionToken;

import java.util.Set;
import java.util.Stack;

public class Investigator extends Player {

    public void throwActionTokens(Set<ActionToken> actionTokens) {
        Controller.getGui().frontPane.showText("The investigator is throwing the action tokens");
        Stack<Runnable> actions = new Stack<>();
        for (ActionToken actionToken : actionTokens)
            if (RandomHelper.randomBoolean()) actions.add(actionToken::flip);
        Controller.setNextActions(actions);
    }
}
