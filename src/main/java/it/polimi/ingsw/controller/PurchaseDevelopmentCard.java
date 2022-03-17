package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Resource;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * class purchase development card
 *
 * @author Grugni Federico
 */
public class PurchaseDevelopmentCard implements Action {
    private final DevelopmentCard[][] developmentCards;
    private DevelopmentCard card;

    /**
     * constructor of class
     */
    public PurchaseDevelopmentCard() {
        this.developmentCards = new DevelopmentCard[3][4];
    }

    /**
     * 1. shows the cards on top of the deck development card
     * and sets an integer matrix of card IDs according to the command.
     * In case a card is not shown, its ID is 0.
     *
     * @return list of buyable cards
     */
    public List<Integer> takeCardToShow() {
        List<Integer> buyableCards = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++) {
                try {
                    developmentCards[i][j] = getGameController().getGame().getDeckDevelopmentCard().peekCard(i, j);
                    if (getGameController().getTurn().getCurrentPlayer().getGameBoard().automaticResourcesRequest(developmentCards[i][j].getDevelopmentCardCost(), false)
                            && getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().checkCardValidity(developmentCards[i][j]) != null) {
                        buyableCards.add(getGameController().getGame().getDeckDevelopmentCard().peekCard(i, j).getId());
                    } else {
                        developmentCards[i][j] = null;
                        buyableCards.add(0);
                    }
                } catch (EmptyCardSlotException e) {
                    developmentCards[i][j] = null;
                    buyableCards.add(-1);
                }
            }
        return buyableCards;
    }

    /**
     * Given an ID returns the associated DevelopmentCard.
     * It works by looking only at the previously saved cards in the takeCardToShow method.
     *
     * @param ID is the ID of the card to find
     * @return the DevelopmentCard relative to the ID
     * @throws EmptyCardSlotException when the card isn't found (which by the way the process function, should never happen)
     */
    public DevelopmentCard getDevelopmentCardByID(int ID) throws EmptyCardSlotException {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (developmentCards[i][j] != null) {
                    if (developmentCards[i][j].getId() == ID) {
                        card = developmentCards[i][j];
                        return card;
                    }
                }
            }
        }
        throw new EmptyCardSlotException();
    }

    /**
     * 3. buy a development card
     *
     * @return true, if the card is bought
     * false, otherwise
     */
    public boolean verifyPurchase(int slot) throws NotEnoughResourcesException {
        Resource[] app = card.getDevelopmentCardCost();
        app = getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().applyDiscountAbility(app);
        if (getGameController().getTurn().getCurrentPlayer().getGameBoard().automaticResourcesRequest(app, false)) {
            getGameController().getTurn().getCurrentPlayer().getGameBoard().automaticResourcesRequest(app, true);
            getGameController().getGame().getDeckDevelopmentCard().hideCard(card);
            getGameController().getTurn().getCurrentPlayer().addVictoryPoints(card.getDevelopmentCardVictoryPoints());
            return getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(card, slot);
        }
        throw new NotEnoughResourcesException();
    }

    /**
     * 4. check if player has won, by number of card >= 7
     *
     * @return true, if current player has won
     * false, otherwise
     */
    public boolean verifyWin() {
        return getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().getCardQuantity() >= 7;
    }
}
