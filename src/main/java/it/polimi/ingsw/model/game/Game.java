package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.game.deck.actionToken.DeckActionToken;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.DeckLeaderCard;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * Class Game
 *
 * @author Galetti Filippo
 */
public class Game {
    private static Game instance;
    private final DeckDevelopmentCard deckDevelopmentCard;
    private final Market market;
    private final DeckLeaderCard deckLeaderCard;
    private List<Player> players;
    private int currentPlayer;
    private DeckActionToken deckActionToken;
    private FaithTrack lorenzoFaithTrack;
    private int numberOfPlayers;
    private boolean lastTurn = false;

    /**
     * Construct a Game with the default structures for a new Game.
     */
    private Game() {
        players = new ArrayList<>();
        deckDevelopmentCard = new DeckDevelopmentCard();
        market = new Market();
        deckLeaderCard = new DeckLeaderCard();
    }

    /**
     * Construct a Game that is getting restored by setting the inputs as game structures.
     *
     * @param players             is the list of players
     * @param currentPlayer       is the player which turn is currently on
     * @param deckDevelopmentCard is the card shop
     * @param market              is the marbles market
     * @param deckLeaderCard      is the deck of leader cards
     * @param numberOfPlayers     is the number of players currently playing
     * @param lastTurn            is a boolean which indicates if the match is in the last turn phase or not
     */
    private Game(List<Player> players, int currentPlayer, DeckDevelopmentCard deckDevelopmentCard, Market market, DeckLeaderCard deckLeaderCard, int numberOfPlayers, boolean lastTurn) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.deckDevelopmentCard = deckDevelopmentCard;
        this.market = market;
        this.deckLeaderCard = deckLeaderCard;
        this.numberOfPlayers = numberOfPlayers;
        this.lastTurn = lastTurn;
    }

    /**
     * Behaves as a singleton constructor for a new Game.
     *
     * @return either the new Game or the already created instance
     */
    public static Game getGame() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Behaves as a singleton constructor for a Game that is getting restored.
     *
     * @param players             is the list of players
     * @param currentPlayer       is the player which turn is currently on
     * @param deckDevelopmentCard is the card shop
     * @param market              is the marbles market
     * @param deckLeaderCard      is the deck of leader cards
     * @param numberOfPlayers     is the number of players currently playing
     * @param lastTurn            is a boolean which indicates if the match is in the last turn phase or not
     * @return either the restored Game or the already created instance
     */
    public static Game getGame(List<Player> players, int currentPlayer, DeckDevelopmentCard deckDevelopmentCard, Market market, DeckLeaderCard deckLeaderCard, int numberOfPlayers, boolean lastTurn) {
        if (instance == null)
            instance = new Game(players, currentPlayer, deckDevelopmentCard, market, deckLeaderCard, numberOfPlayers, lastTurn);
        return instance;
    }

    /**
     * Set the players list for the Game.
     *
     * @param players is the list of players to be set
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Add a player to the list of players.
     *
     * @param player to add to the list
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Set the player which turn is currently on.
     *
     * @param currentPlayer is the player to be set as current
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Tell if the current turn is the last of the game or not.
     *
     * @return true if is the last turn, false otherwise
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Set the next turn as the last of the game.
     */
    public void setLastTurn() {
        this.lastTurn = true;
    }

    /**
     * Remove the requested player from the players list.
     */
    public void removePlayer(String username) {
        players.removeIf(player1 -> ((player1.getUsername()).equalsIgnoreCase((username))));
    }

    /**
     * Get a player for the next turn by removing him from the front and also adding him to the back of the list.
     *
     * @return the player for the next turn
     */
    public Player getNextPlayer() {
        Player player;
        do {
            currentPlayer++;
            if (currentPlayer >= players.size()) {
                currentPlayer = 0;
            }
            player = players.get(currentPlayer);
        } while (player.isDisconnected());
        return player;
    }

    /**
     * Shuffle the list of player and choose one to be the first, assigning him the inkwell.
     *
     * @return the player that will start the game
     */
    public Player getFirstPlayerOfGame() {
        shufflePlayers();
        currentPlayer = 0;
        Player player = players.get(currentPlayer);
        player.setInkwell(true);
        return player;
    }

    /**
     * Shuffle the list of players.
     */
    private void shufflePlayers() {
        Collections.shuffle(players);
    }

    /**
     * Get all players which aren't playing in the current turn.
     *
     * @return a list with the players
     */
    public ArrayList<Player> getAllNonCurrentPlayers() {
        List<Player> nonCurrentPlayers = new ArrayList<>(players);
        nonCurrentPlayers.removeIf(player1 -> (player1.getUsername().equals(getGameController().getTurn().getCurrentPlayer().getUsername())));
        return new ArrayList<>(nonCurrentPlayers);
    }

    /**
     * Get the list of players of the game.
     *
     * @return the list of players
     */
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Remove all the players from the list.
     */
    public void removeAllPlayer() {
        players.clear();
    }

    /**
     * Get the index of the current player in the list.
     * @return the index
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the number of players in the match.
     *
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Set the number of players that will play the match.
     *
     * @param numberOfPlayers is the number of players
     * @return true if the number is correctly set, false if the number of player is greater then 4 or smaller then 1
     */
    public boolean setNumberOfPlayers(int numberOfPlayers) {
        if (numberOfPlayers < 1 || numberOfPlayers > 4)
            return false;
        this.numberOfPlayers = numberOfPlayers;
        return true;
    }

    /**
     * Get the player object corresponding to the input username.
     *
     * @param username is the player's name
     * @return the player object if the username correspond to a player in the list, null otherwise
     */
    public Player getPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Get deck development card.
     *
     * @return deck development card
     */
    public DeckDevelopmentCard getDeckDevelopmentCard() {
        return this.deckDevelopmentCard;
    }

    /**
     * Get deck leader card.
     *
     * @return deck leader card
     */
    public DeckLeaderCard getDeckLeaderCard() {
        return deckLeaderCard;
    }

    /**
     * Get deck action token only used in single player.
     *
     * @return deck action token
     */
    public DeckActionToken getDeckActionToken() {
        return deckActionToken;
    }

    /**
     * Set deck action token only used in single player.
     */
    public void setDeckActionToken(DeckActionToken deck) {
        deckActionToken = deck;
    }

    /**
     * Get the faith track of Lorenzo only used in single player mode.
     *
     * @return lorenzo faith track
     */
    public FaithTrack getLorenzoFaithTrack() {
        return lorenzoFaithTrack;
    }

    /**
     * Set the faith track of Lorenzo only used in single player mode.
     */
    public void setLorenzoFaithTrack(FaithTrack faithTrack) {
        lorenzoFaithTrack = faithTrack;
    }

    /**
     * Get deck market.
     *
     * @return market
     */
    public Market getMarket() {
        return market;
    }
}
