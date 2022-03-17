package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;

import static it.polimi.ingsw.controller.GameController.getGameController;

public class Turn {
    private Player currentPlayer;
    private Action action;

    /**
     * constructor of turn
     *
     * @param player , determines whose turn it is
     */
    public Turn(Player player) {
        this.currentPlayer = player;
    }

    /**
     * set action
     *
     * @param action 1 take resources from market
     *               2 purchase a development card
     *               3 activate production
     *               4 play a leader card
     */
    public void setAction(int action) {
        if (action == 1) {
            this.action = new TakeResources();
        }
        if (action == 2) {
            this.action = new PurchaseDevelopmentCard();
        }
        if (action == 3) {
            this.action = new ActivateProduction();
        }
        if (action == 4) {
            this.action = new LeaderCardsAction();
        }
    }

    /**
     * get action
     *
     * @return action of turn
     */
    public Action getAction() {
        return action;
    }


    /**
     * get current player
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * set current player
     *
     * @param currentPlayer is player to put in current turn
     */
    public void setCurrentPlayer(Player currentPlayer) {
//      could be useful to check if the player submitted is actually in the players list
        this.currentPlayer = currentPlayer;
    }

}
