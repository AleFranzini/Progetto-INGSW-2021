package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.network.server.Backup;
import it.polimi.ingsw.model.game.deck.actionToken.DeckActionToken;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Server;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.EventController.getEventController;

public class GameController {
    private static GameController instance;
    private Game gameModel;
    private Turn turn;
    private boolean debugMode = false;
    private boolean loser = false;

    private GameController() {
        //still to define what to initialize
    }

    public static GameController getGameController() {
        if (instance == null)
            instance = new GameController();
        return instance;
    }

    /**
     * Method setUpSinglePlayer initialize a new Deck of Action Token
     * and a new Faith Track used by Lorenzo for Single Player modality
     */
    public void setUpSinglePlayer() {
        gameModel.setDeckActionToken(new DeckActionToken());
        gameModel.setLorenzoFaithTrack(new FaithTrack());
        Server.setNumberOfPlayers(1);
        getEventController().sendMessage(new Message(MessageType.LORENZO_TRACK_INITIALIZATION, getGame().getDeckActionToken()));
    }

    public void startGame() {
        Game.getGame().setPlayers(Server.getPlayers());
        setGameModel();
        getEventController().sendMessageToAll(new Message(MessageType.MARKET_UPDATE, getGame().getMarket()));
        getEventController().sendMessageToAll(new Message(MessageType.DEVELOPMENTCARD_DECK_UPDATE, getGame().getDeckDevelopmentCard().developmentCardsIDList().toArray()));
        Player player = Game.getGame().getFirstPlayerOfGame();
        this.turn = new Turn(player);
        getEventController().setClientHandler(Server.getClientHandlerFromUsername(player.getUsername()));
        if (Server.getNumberOfPlayers() == 1) {
            setUpSinglePlayer();
        }
        getEventController().sendMessageToAll(new Message(MessageType.DISPLAY_MESSAGE, "The first player is: " + player.getUsername()));
        getEventController().sendMessage(new Message(MessageType.LEADERCARD_SELECTION, getGame().getDeckLeaderCard().getCard()));
        if (debugMode)
            getEventController().debug();
        Backup.saveGame();
    }

    public void startOfflineGame() {
        setGameModel();
        getEventController().sendMessage(new Message(MessageType.MARKET_UPDATE, getGame().getMarket()));
        getEventController().sendMessage(new Message(MessageType.DEVELOPMENTCARD_DECK_UPDATE, getGame().getDeckDevelopmentCard().developmentCardsIDList().toArray()));
        setUpSinglePlayer();
        Player player = Game.getGame().getFirstPlayerOfGame();
        this.turn = new Turn(player);
        if (debugMode)
            getEventController().debug();
        getEventController().sendMessage(new Message(MessageType.LEADERCARD_SELECTION, getGame().getDeckLeaderCard().getCard()));
    }

    public void nextTurn() {
        Player player = Game.getGame().getNextPlayer();
        if (player.isInkwell() && Game.getGame().isLastTurn())
            countPlayersVictoryPoints(loser);
        else {
            this.turn = new Turn(player);
            if (getEventController().getClient() == null)
                getEventController().setClientHandler(Server.getClientHandlerFromUsername(player.getUsername()));
            if (Server.getNumberOfPlayers() != 1)
                getEventController().sendMessageToAllExceptThis(new Message(MessageType.DISPLAY_MESSAGE, "The next player is: " + player.getUsername()));
            if (player.isLeaderCardsAlreadyChosen()) {
                if (!player.isInitialResourcesAlreadyChosen())
                    getEventController().setupInitialResources();
                else
                    getEventController().checkActionOptions();
            } else {
                getEventController().sendMessage(new Message(MessageType.LEADERCARD_SELECTION, getGame().getDeckLeaderCard().getCard()));
            }
        }
    }

    public Game getGame() {
        return this.gameModel;
    }

    /**
     * Get the class linked to the Turn
     *
     * @return the Turn class
     */
    public Turn getTurn() {
        return this.turn;
    }

    public void setTurn(Player player) {
        this.turn = new Turn(player);
    }

    public void countPlayersVictoryPoints(boolean lost) {
        String youLost = "You Lost!", youWon = "You Won!";
        for (Player player : getGame().getAllPlayers()) {
            player.countVictoryPoints();
        }
        List<Player> ranking = getGame().getAllPlayers().stream()
                .sorted(Comparator.comparing(Player::getVictoryPoints))
                .collect(Collectors.toList());
        Collections.reverse(ranking);
        StringBuilder leaderBoard = new StringBuilder();
        for (int i = 0; i < ranking.size(); i++) {
            leaderBoard.append(i + 1).append("°) ").append(ranking.get(i).getUsername()).append(": ").append(ranking.get(i).getVictoryPoints()).append(" points\n");
        }
        if (lost)
            getEventController().sendMessage(new Message(MessageType.END_GAME, youLost));
        else {
            if (getEventController().getClient() == null) {
                getEventController().sendMessageTo(new Message(MessageType.END_GAME, youWon + "\n\n" + leaderBoard), ranking.get(0).getUsername());
                for (int i = 1; i < ranking.size(); i++)
                    getEventController().sendMessageTo(new Message(MessageType.END_GAME, youLost + "\n\n" + leaderBoard), ranking.get(i).getUsername());
            } else
                getEventController().sendMessage(new Message(MessageType.END_GAME, youWon + "\n\n" + leaderBoard));
        }
    }

    public void setLoser() {
        loser = true;
    }

    public void setGameModel() {
        gameModel = Game.getGame();
    }

    public void setDebugMode() {
        this.debugMode = true;
    }
}