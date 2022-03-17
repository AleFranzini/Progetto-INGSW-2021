package it.polimi.ingsw.model.game.deck.leaderCard;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.player.GameBoard;

import javax.naming.SizeLimitExceededException;

import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * Abstract class LeaderCard
 *
 * @author Grugni Federico
 */

public abstract class LeaderCard {
    int id;
    private int victoryPoints;
    private boolean activatedLeaderCard;

    /**
     * get leader card id
     *
     * @return leader card id
     */
    public int getID() {
        return id;
    }

    /**
     * set leader card id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * initialize leader card ability depot
     */
    public abstract void setLeaderCardAbility(GameBoard gameBoard) throws SizeLimitExceededException;

    /**
     * Is Activated Leader Card
     *
     * @return if Leader Card is active
     */
    public boolean isActivatedLeaderCard() {
        return this.activatedLeaderCard;
    }

    /**
     * set activated leader card
     *
     * @param check state of leader card
     */
    public void setActivatedLeaderCard(boolean check) {
        this.activatedLeaderCard = check;
    }

    /**
     * get victoryPoints
     *
     * @return value of victoryPoints  of the LeaderCard
     */
    public int getLeaderCardVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * set victory point
     *
     * @param victoryPoints value of victory point of the leader card
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * check if the card could be bought
     *
     * @return boolean
     */
    public abstract boolean cardVerified(GameBoard gameBoard) throws NotEnoughResourcesException, AlreadyActivatedLeaderCardException, NotEnoughColorCardQuantity;

    /**
     * parsing leader card
     *
     * @return String
     */
    public abstract String parsingLeaderCard();
}
