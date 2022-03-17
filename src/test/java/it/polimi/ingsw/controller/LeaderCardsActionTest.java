package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.*;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;
import static org.junit.jupiter.api.Assertions.*;

class LeaderCardsActionTest {
    private LeaderCardsAction leaderCardsAction;

    @BeforeEach
    void setUp() {
        getGame().addPlayer(new Player("nickname"));
        Player player = getGame().getPlayerByUsername("nickname");
        getGameController().setTurn(player);
        getGameController().getTurn().setCurrentPlayer(player);
        getGameController().getTurn().getCurrentPlayer().setLeaderCards(new int[]{4, 2});
        getGame().getDeckLeaderCard().resetDeckLeaderCard();
        leaderCardsAction = new LeaderCardsAction();
    }

    @Test
    void testGiveLeaderCards() throws EmptyCardSlotException {
        //activate true, nothing to show
        leaderCardsAction.setActivated(true);
        assertTrue(leaderCardsAction.giveLeaderCards().isEmpty());
        //activate true, one card to show
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SERVANT)});
        assertNotEquals(leaderCardsAction.giveLeaderCards().get(0).getID(), 2);
        assertEquals(leaderCardsAction.giveLeaderCards().get(0).getID(), 4);
        assertThrows(IndexOutOfBoundsException.class, () -> leaderCardsAction.giveLeaderCards().get(1));
        //activate true, two leader card to show
        DeckDevelopmentCard deck = new DeckDevelopmentCard();
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 1), 0);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 0), 1);
        assertEquals(leaderCardsAction.giveLeaderCards().get(0).getID(), 4);
        assertEquals(leaderCardsAction.giveLeaderCards().get(1).getID(), 2);
        getGame().removePlayer("nickname");
    }

    @Test
    void testGetActivableLeaderCardsQuantity() throws EmptyCardSlotException {
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SERVANT)});
        leaderCardsAction.setActivated(true);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SERVANT)});
        assertEquals(1, leaderCardsAction.getActivableLeaderCardsQuantity());
        DeckDevelopmentCard deck = new DeckDevelopmentCard();
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 1), 0);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 0), 1);
        assertEquals(2, leaderCardsAction.getActivableLeaderCardsQuantity());
        getGame().removePlayer("nickname");
    }

    @Test
    void testLeaderCardActivation() throws EmptyCardSlotException {
        //if one card is activated
        leaderCardsAction.setActivated(true);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SERVANT)});
        assertEquals(1, leaderCardsAction.getActivableLeaderCardsQuantity());
        assertDoesNotThrow(() -> leaderCardsAction.leaderCardActivation(0));

        //if one card is active and one card can be activated
        DeckDevelopmentCard deck = new DeckDevelopmentCard();
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 1), 0);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(deck.peekCard(0, 0), 1);
        assertEquals(1, leaderCardsAction.getActivableLeaderCardsQuantity());
        assertDoesNotThrow(() -> leaderCardsAction.leaderCardActivation(0));

        //reset
        leaderCardsAction.setActivated(false);
        leaderCardsAction.giveLeaderCards().get(0).setActivatedLeaderCard(false);
        leaderCardsAction.giveLeaderCards().get(1).setActivatedLeaderCard(false);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().resetAbilities();

        //if two cards can be activated
        leaderCardsAction.setActivated(true);
        assertEquals(2, leaderCardsAction.getActivableLeaderCardsQuantity());
        assertDoesNotThrow(() -> leaderCardsAction.leaderCardActivation(0));
        assertDoesNotThrow(() -> leaderCardsAction.leaderCardActivation(0));
        leaderCardsAction.setActivated(false);
        assertTrue(leaderCardsAction.giveLeaderCards().get(0).isActivatedLeaderCard());
        assertTrue(leaderCardsAction.giveLeaderCards().get(1).isActivatedLeaderCard());
        getGame().removePlayer("nickname");
    }

    @Test
    void testDiscardCard() {
        //discard one card
        leaderCardsAction = new LeaderCardsAction();
        leaderCardsAction.setActivated(false);
        assertEquals(2, leaderCardsAction.giveLeaderCards().size());
        leaderCardsAction.setActivated(true);
        assertEquals(0, leaderCardsAction.giveLeaderCards().size());
        try {
            leaderCardsAction.discardCard(0);
        }catch (AlreadyActivatedLeaderCardException e){
            assert false;
        }
        leaderCardsAction.setActivated(false);
        assertEquals(1, leaderCardsAction.giveLeaderCards().size());
        leaderCardsAction.setActivated(true);
        assertEquals(0, leaderCardsAction.giveLeaderCards().size());

        //discard another card
        try {
            leaderCardsAction.discardCard(0);
        }catch (AlreadyActivatedLeaderCardException e){
            assert false;
        }
        leaderCardsAction.setActivated(false);
        assertEquals(0, leaderCardsAction.giveLeaderCards().size());
        leaderCardsAction.setActivated(true);
        assertEquals(0, leaderCardsAction.giveLeaderCards().size());

        //reset
        getGame().getDeckLeaderCard().resetDeckLeaderCard();
        getGameController().getTurn().getCurrentPlayer().setLeaderCards(new int[]{4, 2});
        getGame().removePlayer("nickname");
    }

    @Test
    public void testDiscardWithLeaderCardActive(){
        //activate a card
        leaderCardsAction.setActivated(true);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SERVANT)});
        assertEquals(1, leaderCardsAction.getActivableLeaderCardsQuantity());
        assertDoesNotThrow(() -> leaderCardsAction.leaderCardActivation(0));

        //discard a activated card
        try {
            leaderCardsAction.discardCard(0);
        }catch (AlreadyActivatedLeaderCardException e){
            assert true;
        }

        //reset
        getGame().getDeckLeaderCard().resetDeckLeaderCard();
        getGameController().getTurn().getCurrentPlayer().setLeaderCards(new int[]{4, 2});
        getGame().removePlayer("nickname");
    }

    @Test
    void testParsingLeaderCard() {
        getGame().removePlayer("nickname");
    }

    @AfterEach
    public void testAfter() {
        leaderCardsAction = new LeaderCardsAction();
    }
}