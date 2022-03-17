package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;

import java.util.ArrayList;
import java.util.List;


import static it.polimi.ingsw.model.game.Game.getGame;

/**
 * Class Player
 *
 * @author Grugni Federico
 */
public class Player {
    private final String username;
    private boolean inkwell;
    private boolean leaderCardsAlreadyChosen;
    private boolean initialResourcesAlreadyChosen;
    private final GameBoard gameBoard;
    private List<LeaderCard> leaderCards;
    private int victoryPoints = 0;
    private boolean disconnected;

    /**
     * player constructor which initialize the player's nickname
     * and gameBoard
     *
     * @param username of the new player
     */
    public Player(String username) {
        this.username = username;
        this.gameBoard = new GameBoard();
        this.leaderCards = new ArrayList<>();
        this.leaderCardsAlreadyChosen = false;
        this.initialResourcesAlreadyChosen = false;
        this.disconnected = false;
    }

    /**
     * player constructor
     *
     * @param username                     of the player
     * @param inkwell                      of the player
     * @param initialResourceAlreadyChosen of the player
     * @param leaderCardsAlreadyChosen     of the player
     * @param gameBoard                    of the player
     * @param leaderCards                  of the player
     * @param victoryPoints                of the player
     */
    public Player(String username, boolean inkwell, boolean initialResourceAlreadyChosen, boolean leaderCardsAlreadyChosen, GameBoard gameBoard, List<LeaderCard> leaderCards, int victoryPoints) {
        this.username = username;
        this.inkwell = inkwell;
        this.initialResourcesAlreadyChosen = initialResourceAlreadyChosen;
        this.leaderCardsAlreadyChosen = leaderCardsAlreadyChosen;
        this.gameBoard = gameBoard;
        this.leaderCards = leaderCards;
        this.victoryPoints = victoryPoints;
    }

    public boolean isLeaderCardsAlreadyChosen() {
        return leaderCardsAlreadyChosen;
    }

    /**
     * set leader card already chosen, is put to true if player already chosen two leader card
     * false otherwise
     */
    public void setLeaderCardsAlreadyChosen() {
        this.leaderCardsAlreadyChosen = true;
    }

    public boolean isInitialResourcesAlreadyChosen() {
        return initialResourcesAlreadyChosen;
    }

    /**
     * setInitialResourcesAlreadyChosen sets true when the player chose the initial resource/s
     * before starting the first turn.
     */
    public void setInitialResourcesAlreadyChosen() {
        this.initialResourcesAlreadyChosen = true;
    }

    /**
     * set true or false whether the player has the inkwell or not
     *
     * @param inkwell is set at the beginning of the game
     */
    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    /**
     * get nickname
     *
     * @return player's nickname
     */
    public String getUsername() {
        return username;
    }


    /**
     * @return true if the player has the inkwell,
     * false otherwise
     */
    public boolean isInkwell() {
        return inkwell;
    }

    /**
     * get gameBoard
     *
     * @return the player's gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Set the pair of leader card of the player.
     *
     * @param indexes of card to set
     */
    public void setLeaderCards(int[] indexes) {
        List<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(getGame().getDeckLeaderCard().getCardByID(indexes[0]));
        leaderCards.add(getGame().getDeckLeaderCard().getCardByID(indexes[1]));
        this.leaderCards = leaderCards;
    }

    /**
     * Get the pair of leader card of the player.
     *
     * @return an array of the two leader cards.
     */
    public List<LeaderCard> getLeaderCards() {
        return new ArrayList<>(leaderCards);
    }

    /**
     * remove leader card
     *
     * @param index of leader on array
     */
    public void removeLeaderCard(int index) {
        this.leaderCards.remove(index);
    }

    /**
     * get victory points
     *
     * @return victory points of player
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * add victory points
     *
     * @param points to add
     */
    public void addVictoryPoints(int points) {
        this.victoryPoints += points;
    }

    /**
     * count victory point
     * <p>
     * sum points of leader card, faith marker, pope tiles activated, every five resources
     * points of development card are added every time that player buy a card
     */
    public void countVictoryPoints() {
        addVictoryPoints(getGameBoard().getFaithTrack().getFaithMarkerPoints());
        addVictoryPoints(getGameBoard().getFaithTrack().getVictoryPointsPopeTiles());
        if (leaderCards != null) {
            for (LeaderCard leaderCard : leaderCards) {
                if (leaderCard.isActivatedLeaderCard()) {
                    addVictoryPoints(leaderCard.getLeaderCardVictoryPoints());
                }
            }
        }
        int resourcesPoints = 0;
        Resource[] resources = getGameBoard().peekAllStoresResources();
        if (resources != null) {
            for (Resource resource : resources) {
                resourcesPoints += resource.getQuantity();
            }
            addVictoryPoints(resourcesPoints / 5);
        }
    }

    /**
     * is disconnected
     *
     * @return true if player is disconnected
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * set disconnected
     *
     * @param disconnected true if player is disconnected
     *                     false otherwise
     */
    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }
}
