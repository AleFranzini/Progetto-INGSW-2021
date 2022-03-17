package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;
import it.polimi.ingsw.model.player.GameBoard;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.controller.EventController.getEventController;
import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * class LeaderCardsAction
 *
 * @author Grugni Federico
 */
public class LeaderCardsAction implements Action {
    private final GameController gameController;
    private List<LeaderCard> leaderCardListActivable;
    private GameBoard gameBoard;
    private boolean activated = false;

    /**
     * constructor of class
     */
    public LeaderCardsAction() {
        gameController = GameController.getGameController();
        leaderCardListActivable = new ArrayList<LeaderCard>();
        gameBoard = gameController.getTurn().getCurrentPlayer().getGameBoard();
    }

    /**
     * set activated
     *
     * @param check activated status to set
     */
    public void setActivated(boolean check) {
        this.activated = check;
    }

    /**
     * get activated
     */
    public boolean getActivated() {
        return activated;
    }

    /**
     * 0. ask which action player can do
     */
    public void startLeaderCardAction(boolean fromNoActivableLeaderCard) {
        boolean[] message = new boolean[]{false, false};
        if (!fromNoActivableLeaderCard)
            message[0] = true;
        if (getGameController().getTurn().getCurrentPlayer().getLeaderCards().size() > 0) {
            message[1] = true;
        }
        getEventController().sendMessage(new Message(MessageType.LEADERCARD_OPTION, message));
    }

    /**
     * 1. give leader cards that can be activate
     * when it's called, clear leaderCardListActivable and check all leader card
     *
     * @return list of leader cards
     */
    public List<LeaderCard> giveLeaderCards() {
        leaderCardListActivable.clear();
        if (activated) {
            for (LeaderCard app : gameController.getTurn().getCurrentPlayer().getLeaderCards())
                try {
                    if (app.cardVerified(gameBoard))
                        leaderCardListActivable.add(app);
                } catch (AlreadyActivatedLeaderCardException | NotEnoughResourcesException | NotEnoughColorCardQuantity e) {

                }
            return leaderCardListActivable;
        } else
            return gameController.getTurn().getCurrentPlayer().getLeaderCards();
    }

    /**
     * get activable leader card(s) quantity
     *
     * @return number of activable cards
     */
    public int getActivableLeaderCardsQuantity() {
        boolean app = activated;
        setActivated(true);
        leaderCardListActivable = giveLeaderCards();
        setActivated(app);
        return leaderCardListActivable.size();
    }

    /**
     * 2. activate the required leader card(s)
     *
     * @param command is the leader card chosen by the player
     *                0, is the first card
     *                1, is the second card
     */
    public void leaderCardActivation(int command) throws SizeLimitExceededException, AlreadyActivatedLeaderCardException, NotEnoughColorCardQuantity, NotEnoughResourcesException {
        List<LeaderCard> leaderCardList;
        leaderCardList = giveLeaderCards();
        if (!leaderCardList.isEmpty()) {
            if (leaderCardList.get(command).cardVerified(gameBoard))
                leaderCardList.get(command).setLeaderCardAbility(gameBoard); //controllare se l'eccezione è lanciata prima del set attivabile
        }
    }

    /**
     * 2. discard the required leader card(s)
     *
     * @param command is the leader card chosen by the player
     *                0, is the first card
     *                1, is the second card
     */
    public void discardCard(int command) throws AlreadyActivatedLeaderCardException {
        if (gameController.getTurn().getCurrentPlayer().getLeaderCards().get(command).isActivatedLeaderCard())
            throw new AlreadyActivatedLeaderCardException();
        gameController.getTurn().getCurrentPlayer().removeLeaderCard(command);
        leaderCardListActivable.clear();
    }

    /**
     * parse cards to string
     *
     * @param leaderCardList is the list of card to parse
     * @return parsed cards
     */
    public String parsingLeaderCard(List<LeaderCard> leaderCardList) {
        StringBuilder parsing = new StringBuilder();
        for (LeaderCard leaderCard : leaderCardList)
            parsing.append("\n").append(leaderCard.parsingLeaderCard());
        parsing.append("\n");
        return parsing.toString();
    }
}
