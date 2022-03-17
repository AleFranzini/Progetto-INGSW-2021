package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;

import java.util.List;

/**
 * Interface View implemented by CLI and GUI
 *
 * @author Franzini Alessandro, Galetti Filippo, Grugni Federico
 */
public interface View {

    /**
     * Display message.
     *
     * @param message to show.
     */
    void displayMessage(String message);

    /**
     * Display gameboard.
     */
    void printGameboard();

    /**
     * Ask to player ip and port.
     */
    void setupProcess();

    /**
     * Ask to player username.
     */
    void insertUsername();

    /**
     * Ask to first player the number of player for this game.
     */
    void insertNumberOfPlayers();

    /**
     * Ask to restore a previous game
     *
     * @param games is the string that contains all previous game with the same connected username.
     */
    void askRestoreGame(String games);

    /**
     * Select two leader cards from four card of the deck.
     *
     * @param indexes IDs of four leader cards.
     */
    void selectLeaderCardsFromDeck(int[] indexes);

    /**
     * Set chosen leader in gameboard.
     *
     * @param indexesCardInitial IDs of two leader cards.
     */
    void setInitialLeaderCard(int[] indexesCardInitial);

    /**
     * Choose resource dependent of player turn.
     *
     * @param initialResources number of resources that player can choose.
     */
    void chooseInitialResources(int initialResources);

    /**
     * Activate leader card in gameboard.
     *
     * @param indexCardActivated index of leader card.
     */
    void setActivatedLeaderCard(int indexCardActivated);

    /**
     * Discard leader card in gameboard.
     *
     * @param index index of leader card.
     */
    void setDiscardLeaderCard(int index);

    /**
     * Set action completed to false and pass turn.
     */
    void checkEndTurn();

    /* Updates */
    void updatePopeTiles(Message message);

    void updateFaithMarker(Message message);

    void updateWarehouse(Message message);

    void updateLeaderCard(int[] indexes);

    void updateStrongbox(Message message);

    void updateLeaderDepot(Message message);

    void updateSlotStack(List<Integer> slotStackIDs);

    void updateFlagsCounter(Message message);

    void updateMarket(Message message);

    void updateLorenzoFaithTrack(Message message);

    void updateDevelopmentCardDeck(Integer[] cardShop);

    /* Leader Card Method */

    /**
     * Display which action player can do.
     *
     * @param message flag for legal action.
     */
    void displayLeaderCardOption(boolean[] message);

    /**
     * Ask if player would see all leader card or only activable.
     */
    void askActivableCard();

    /**
     * Show which card can be activated or all.
     *
     * @param cardsID IDs of cards.
     */
    void showActivationLeaderCard(int[] cardsID);

    /**
     * display error already activated leader card
     *
     * @param action if true, action is activation
     *               if false, action is discard
     */
    void leaderCardAlreadyActivated(boolean action);

    void leaderCardNotEnoughResources();

    void leaderCardNotEnoughColorCardQuantity();

    /**
     * Ask if player would activate another leader card.
     */
    void askActivateAnotherLeaderCard();

    /**
     * Turn to choose initial action but with flag of leader card to false.
     *
     * @param message string message.
     */
    void leaderCardEndAction(String message);

    /**
     * Show which card can be discarded.
     *
     * @param cardsID IDs of cards.
     */
    void leaderCardDiscardAction(int[] cardsID);

    /**
     * Ask if player would discard another leader card.
     */
    void askAnotherDiscardLeaderCard();

    /* Production Methods */

    /**
     * Display which action player can do.
     *
     * @param legalAction flag array for action.
     */
    void displayProductionAction(boolean[] legalAction);

    /**
     * Check which action player can do.
     */
    void callProductionAction();

    /**
     * Ask to player in which slot would put development card.
     */
    void checkDevelopmentCardProductionSlot();

    /**
     * Ask to player which leader card would activate.
     */
    void chooseLeaderCardProductionIndex();

    /**
     * Ask to player which type of resource would produce.
     */
    void chooseLeaderCardProductionResourceType();

    /**
     * Choose which resource put in basic production.
     */
    void chooseBasicProductionResourceType();

    /**
     * Choose actions: take resources from market.
     * buy a development card.
     * activate the production.
     * play a leader card.
     *
     * @param fromEndAction        false to show take resources from market action.
     * @param fromLeaderCardAction true if caller is a leader card action methods.
     * @param message              contains the action that player can do.
     */
    void chooseInitialAction(boolean fromEndAction, boolean fromLeaderCardAction, Message message);

    /**
     * Is a menu after player has done the primary action in choose initial actions.
     *
     * @param fromLeaderCardAction true if caller is a leader card action methods.
     */
    void lastAction(boolean fromLeaderCardAction);

    /**
     * Set action completed.
     *
     * @param actionCompleted state.
     */
    void setActionCompleted(boolean actionCompleted);

    /* Take Resources from Market */

    /**
     * Method showMarket prints the current state of the Market.
     */
    void showMarket();

    /**
     * Method displayPurchasedResources shows the resources that this player has bought from the Market.
     *
     * @param purchasedResource is a String that contains which resources the player has bought.
     */
    void displayPurchasedResources(String purchasedResource);

    /**
     * Method chooseMarketLine is used to select the row or the column of the Market that the player wants to buy
     * and to send it as a Message to the EventController.
     */
    void chooseMarketLine();

    /**
     * Method chooseBlankAbility lets the player choose for each white marble which LeaderCard ability to use in case the player
     * has both LeaderCards of type ResourceAbility and both already activated.
     *
     * @param numBlank is the number of white marbles bought from the Market.
     */
    void chooseBlankAbility(int numBlank);

    /**
     * Method manageStores lets the player manage the Warehouse (and in case the LeaderCard depot/s when activated)
     * in order to correctly insert the purchased resources into the stores.
     *
     * @param numDepotLeader is the number of LeaderCard with depot ability activated (could be 0/1/2).
     */
    void manageStores(int numDepotLeader);

    /**
     * Method displayDiscardedResources shows the player the discarded resources among the purchased one.
     *
     * @param discardedResource is a String that contains all the discarded resources.
     */
    void displayDiscardedResources(String discardedResource);
    /* Purchase Development Card Methods */

    /**
     * Ask to player which development card would buy.
     *
     * @param cards index of card.
     */
    void chooseCardToPurchase(Integer[] cards);

    /**
     * Ask to player in which slot would to put card.
     *
     * @param slotList is list of legal slot.
     */
    void chooseCardSlot(List<Integer> slotList);

    /* Single Player */

    /**
     * Method showActionToken is used to show to the client the Action Token peeked from the deck.
     *
     * @param token is a String that represent the peeked token (a number if it's a BlackCrossToken or a color if it's a DiscordToken)
     */
    void showActionToken(String token);

    /**
     * Method setLorenzoFaithTrack initialize a new Faith Track for Lorenzo il Magnifico (only used in single player mode)
     */
    void setLorenzoFaithTrack();

    // End Game

    /**
     * Method endGame show to the client whether he lost or won and the leaderboard of the players.
     *
     * @param leaderBoard is a String that contains the leaderboard of the players for this match.
     */
    void endGame(String leaderBoard);
}
