package main.java;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.alibis.Alibi;
import main.java.panes.GamePane;
import main.java.players.Investigator;
import main.java.players.MrJack;
import main.java.players.Player;
import main.java.tiles.AreaTile;
import main.java.tokens.ActionToken;
import main.java.tokens.Detective;

import java.util.*;

public class Controller extends Application {
    private static final GamePane gui = new GamePane();
    private static final MrJack mrJack = new MrJack(gui.boardPane.rightPane.alibiDeck.draw());
    private static final Investigator investigator = new Investigator();
    private static final Deque<Runnable> actionQueue = new ArrayDeque<>();
    private static final Set<AreaTile> visibleTiles = new HashSet<>();
    private static final Set<AreaTile> invisibleTiles = new HashSet<>();
    private static final Set<ActionToken> availableActionTokens = new HashSet<>();
    private static Stage primaryStage;
    private static int turnNumber = 1;
    private static Player currentPlayer;
    private static Node clickedNode;

    public static void startGame() {
        actionQueue.add(() -> gui.frontPane.showText("Game is starting"));
        actionQueue.add(() -> {
            gui.boardPane.districtPane.tilesGrid.initTilesOrientations();
            playNextAction();
        });
        actionQueue.add(mrJack::drawIdentity);
        actionQueue.add(Controller::turn);
        playNextAction();
    }

    public static void turn() {
        gui.frontPane.showText("Turn n°" + turnNumber);
        actionQueue.add(Controller::manhunt);
        actionQueue.add(Controller::appealForWitnesses);
        actionQueue.add(Controller::endTurn);
    }

    public static void manhunt() {
        gui.frontPane.showText("Starting the Manhunt");
        availableActionTokens.addAll(gui.boardPane.rightPane.actionTokens.getTokens());
        Stack<Runnable> actionStack = new Stack<>();
        if (turnNumber % 2 == 1) {
            actionStack.add(() -> investigator.throwActionTokens(availableActionTokens));
            actionStack.add(() -> investigator.chooseActionToken(availableActionTokens));
            actionStack.add(() -> mrJack.chooseActionToken(availableActionTokens));
            actionStack.add(() -> mrJack.chooseActionToken(availableActionTokens));
            actionStack.add(() -> investigator.chooseActionToken(availableActionTokens));
        } else {
            actionStack.add(() -> mrJack.turnActionTokensOver(availableActionTokens));
            actionStack.add(() -> mrJack.chooseActionToken(availableActionTokens));
            actionStack.add(() -> investigator.chooseActionToken(availableActionTokens));
            actionStack.add(() -> investigator.chooseActionToken(availableActionTokens));
            actionStack.add(() -> mrJack.chooseActionToken(availableActionTokens));
        }
        setNextActions(actionStack);
    }

    public static void appealForWitnesses() {
        gui.frontPane.showText("Starting the Appeal for Witnesses");
        updateVisibleTiles();
        Stack<Runnable> actions = new Stack<>();
        boolean mrJackIsVisible = mrJackIsVisible();
        actions.add(() -> {
            String str = mrJack + " is" + ((mrJackIsVisible) ? "" : " not") + " in the lines of sight of the detectives";
            gui.frontPane.showText(str);
        });
        actions.add(() -> hideSuspectsOn((mrJackIsVisible() ? invisibleTiles : visibleTiles)));
        actions.add(() -> {
            String str = ((mrJackIsVisible) ? investigator : mrJack) + " takes the turn token";
            if (mrJackIsVisible && currentPlayer instanceof MrJack) {
                str += ", winning one hourglass";
                mrJack.addHourglasses(1);
            }
            gui.frontPane.showText(str);
            gui.boardPane.leftPane.removeTurnToken();
        });
        setNextActions(actions);
    }

    public static void endTurn() {
        boolean mrJackHasWon = hasWon(mrJack);
        boolean investigatorHasWon = hasWon(investigator);
        Stack<Runnable> actions = new Stack<>();
        if (mrJackHasWon && !investigatorHasWon) {
            actions.add(() -> gui.frontPane.showText(mrJack + " has collected 6 hourglasses"));
            actions.add(() -> endGame(mrJack));
        }
        if (investigatorHasWon && !mrJackHasWon) {
            actions.add(() -> gui.frontPane.showText(investigator + " has discovered " + mrJack + " identity"));
            actions.add(() -> endGame(investigator));
        }
        if (mrJackHasWon && investigatorHasWon) {
            actions.add(() -> gui.frontPane.showText("Both players have reached there goal"));
            if (mrJackIsVisible()) {
                actions.add(() -> gui.frontPane.showText("Mr Jack is visible"));
                actions.add(() -> endGame(investigator));
            } else {
                actions.add(() -> gui.frontPane.showText("Mr Jack is not visible"));
                String str = mrJack + " has to hide from " + investigator + " for " + (8 - turnNumber) + " more turn";
                actions.add(() -> gui.frontPane.showText(str));
                turnNumber++;
                actions.add(Controller::turn);
            }
        }
        if (!mrJackHasWon && !investigatorHasWon) {
            turnNumber++;
            actions.add(Controller::turn);
        }
        setNextActions(actions);
        playNextAction();
    }

    public static void hideSuspectsOn(Set<AreaTile> tiles) {
        Stack<Runnable> actions = new Stack<>();
        for (AreaTile areaTile : tiles) if (areaTile.suspectIsVisible()) actions.add(areaTile::hideSuspect);
        setNextActions(actions);
        playNextAction();
    }

    public static boolean mrJackIsVisible() {
        for (AreaTile areaTile : visibleTiles) {
            if (areaTile.getAlibi() == mrJack.getIdentity()) return true;
        }
        return false;
    }

    public static void playClickedActionToken() {
        ActionToken actionToken = (ActionToken) clickedNode;
        availableActionTokens.remove(actionToken);
        FXHelper.setClickable(gui.boardPane.rightPane.actionTokens, false);
        Detective holmes = gui.boardPane.districtPane.getHolmes();
        Detective watson = gui.boardPane.districtPane.getWatson();
        Detective toby = gui.boardPane.districtPane.getToby();
        boolean onRecto = actionToken.isOnRecto();
        switch (actionToken.getTokenId()) {
            case 1:
                if (onRecto) alibiAction();
                else detectiveAction(holmes);
                break;
            case 2:
                if (onRecto) detectiveAction(toby);
                else detectiveAction(watson);
                break;
            case 3:
                if (onRecto) rotationAction();
                else exchangeAction();
                break;
            case 4:
                if (onRecto) rotationAction();
                else jokerAction();
                break;
        }
    }

    private static void alibiAction() {
        Alibi alibi = gui.boardPane.rightPane.alibiDeck.draw();
        Stack<Runnable> actions = new Stack<>();
        actions.add(() -> currentPlayer.drawCard(alibi));
        if (currentPlayer instanceof Investigator) {
            if (mrJack.getIdentity() == alibi) {
                for (AreaTile areaTile : gui.boardPane.districtPane.tilesGrid.getAreaTiles()) {
                    if (areaTile.getAlibi() != alibi) actions.add(areaTile::hideSuspect);
                }
            } else {
                AreaTile areaTile = gui.boardPane.districtPane.tilesGrid.getAreaTile(alibi);
                if (areaTile.suspectIsVisible()) actions.add(areaTile::hideSuspect);
            }
        }
        setNextActions(actions);
        playNextAction();
    }

    private static void exchangeAction() {
        FXHelper.setClickable(gui.boardPane.districtPane.tilesGrid, true);
        setNextAction(() -> {
            AreaTile areaTile1 = (AreaTile) clickedNode;
            FXHelper.setClickable(areaTile1, false);
            setNextAction(() -> {
                FXHelper.setClickable(gui.boardPane.districtPane.tilesGrid, false);
                gui.boardPane.districtPane.tilesGrid.switchTiles(areaTile1, (AreaTile) clickedNode);
            });
        });
    }

    private static void detectiveAction(Detective detective) {
        //we give him the choice between stepping forward once or twice
        HashMap<String, Runnable> choices = new HashMap<String, Runnable>() {{
            put("Step 1 tile forward", () -> detective.move(1));
            put("Step 2 tiles forward", () -> detective.move(2));
        }};
        gui.frontPane.giveChoice(choices);
    }

    private static void jokerAction() {
        if (currentPlayer instanceof Investigator) {
            FXHelper.setClickable(gui.boardPane.districtPane, true);
            setNextAction(() -> {
                FXHelper.setClickable(gui.boardPane.districtPane, false);
                ((Detective) clickedNode).move(1);
            });
        } else {
            HashMap<String, Runnable> choices = new HashMap<String, Runnable>() {{
                put("Move a Detective one space clockwise", () -> {
                    FXHelper.setClickable(gui.boardPane.districtPane, true);
                    setNextAction(() -> {
                        FXHelper.setClickable(gui.boardPane.districtPane, false);
                        ((Detective) clickedNode).move(1);
                    });
                });
                put("Leave the three detectives where they are", Controller::playNextAction);
            }};
            gui.frontPane.giveChoice(choices);
        }
    }

    private static void rotationAction() {
        FXHelper.setClickable(gui.boardPane.districtPane.tilesGrid, true);
        //the user click on a tile
        //we give him 3 choices for how to rotate the tile
        HashMap<String, Runnable> choices = new HashMap<String, Runnable>() {{
            put("rotate 90° clockwise", () -> ((AreaTile) clickedNode).rotate(1, true));
            put("rotate 180°", () -> ((AreaTile) clickedNode).rotate(2, true));
            put("rotate 90° anticlockwise", () -> ((AreaTile) clickedNode).rotate(1, false));
        }};
        setNextAction(() -> {
            FXHelper.setClickable(gui.boardPane.districtPane.tilesGrid, false);
            gui.frontPane.giveChoice(choices);
        });
    }

    private static boolean hasWon(Player player) {
        if (player instanceof MrJack) {
            return mrJack.getHourglassesCount() >= 6;
        } else {
            int suspectsNumber = 0;
            for (AreaTile areaTile : gui.boardPane.districtPane.tilesGrid.getAreaTiles())
                if (areaTile.suspectIsVisible()) suspectsNumber++;
            return suspectsNumber == 1;
        }
    }

    private static void endGame(Player winner) {
        gui.frontPane.showText("Game Over: " + winner + " has won");
        setNextAction(() -> gui.setOnMouseClicked(e -> primaryStage.close()));
    }

    private static void updateVisibleTiles() {
        AreaTile[][] areaTiles = gui.boardPane.districtPane.tilesGrid.getAreaTilesGrid();
        visibleTiles.clear();
        invisibleTiles.clear();
        Detective[] detectivePieces = gui.boardPane.districtPane.getDetectives();
        //(N=0;W=1,S=2,E=3)
        //The Detective Pieces can be placed on row and column 0->4 (includes outer Tiles)
        //The Area Tiles can be placed on rows and columns 1->3
        for (Detective detective : detectivePieces) {
            int detectiveRow = detective.getRow();
            int detectiveCol = detective.getColumn();
            int orientation = -1;
            if (detectiveRow == 4) orientation = 0;
            if (detectiveCol == 4) orientation = 1;
            if (detectiveRow == 0) orientation = 2;
            if (detectiveCol == 0) orientation = 3;
            int rowIncr = (orientation - 1) % 2;
            int colIncr = (orientation - 2) % 2;
            int nextRow = detectiveRow + rowIncr;
            int nextCol = detectiveCol + colIncr;
            while (nextRow >= 1 && nextRow <= 3 && nextCol >= 1 && nextCol <= 3) {
                AreaTile areaTile = areaTiles[nextCol - 1][nextRow - 1];
                //blocked when entering the area
                if (areaTile.isBlocked((orientation + 2) % 4)) break;
                //this tile is not accessible from this side, neither should be the following ones
                visibleTiles.add(areaTile);
                //blocked when exiting the area
                if (areaTile.isBlocked(orientation)) break;
                nextRow += rowIncr;
                nextCol += colIncr;
            }
        }
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (!visibleTiles.contains(areaTiles[i][j])) invisibleTiles.add(areaTiles[i][j]);
            }
    }

    public static void setNextAction(Runnable runnable) {
        actionQueue.addFirst(runnable);
    }

    public static void setNextActions(Stack<Runnable> actionStack) {
        while (!actionStack.isEmpty()) setNextAction(actionStack.pop());
    }

    public static void playNextAction() {
        Runnable action = actionQueue.poll();
        assert action != null;
        action.run();
    }

    public static void setClickedNode(Node node) {
        clickedNode = node;
        playNextAction();
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        Controller.currentPlayer = currentPlayer;
    }

    public static GamePane getGui() {
        return gui;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(new Scene(gui));
        primaryStage.setTitle("Mr Jack Pocket");
        primaryStage.setWidth(720);
        primaryStage.setHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        primaryStage.setFullScreen(true);
        primaryStage.setOnShown(e -> startGame());
        primaryStage.show();
    }
}

