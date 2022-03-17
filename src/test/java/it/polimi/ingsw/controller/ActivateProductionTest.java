package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientOffline;
import it.polimi.ingsw.view.cli.CLI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;
import static org.junit.jupiter.api.Assertions.*;

class ActivateProductionTest {
    ActivateProduction activateProduction;
    static ClientOffline clientOffline = new ClientOffline();
    static DevelopmentCard card;
    static LeaderCard leaderCard;
    static int[] array = new int[]{1, 3};

    @BeforeAll
    static void create() throws EmptyCardSlotException {
        CLI cli = new CLI(clientOffline);
        clientOffline.setView(cli);
        try {
            EventController.getEventController(clientOffline);
        } catch (InstanceAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        cli.setLorenzoFaithTrack();
        card = getGame().getDeckDevelopmentCard().peekCard(0, 0);
        leaderCard = getGame().getDeckLeaderCard().getCardByID(1);
        getGame().setLorenzoFaithTrack(new FaithTrack());
    }

    @BeforeEach
    void setUp() {
        Player player = new Player("test");
        getGame().addPlayer(player);
        getGameController().setTurn(player);
        getGameController().setGameModel();
        GameController.getGameController().getTurn().setCurrentPlayer(getGame().getFirstPlayerOfGame());
        activateProduction = new ActivateProduction();
    }

    @Test
    public void testStartCardProduction() throws NotEnoughResourcesException {
        Player player = getGameController().getTurn().getCurrentPlayer();
        assertTrue(getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().addCard(card, 0));
        getGameController().getTurn().getCurrentPlayer().setLeaderCards(array);
        assertNotNull(card);
        activateProduction.chooseLeaderCardProductionResourceType(ResourceType.COIN);
        assertThrows(NotEnoughResourcesException.class, () -> activateProduction.startCardProduction(0));
        assertThrows(NotEnoughResourcesException.class, () -> activateProduction.startBasicProduction(ResourceType.COIN, ResourceType.SERVANT, ResourceType.STONE));
        assertThrows(IndexOutOfBoundsException.class, () -> activateProduction.chooseLeaderCardProductionIndex(1));
        assertDoesNotThrow(() -> activateProduction.chooseLeaderCardProductionResourceType(ResourceType.COIN));
        activateProduction.activateProduction();
    }


}