package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardDepotAbility;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.naming.SizeLimitExceededException;

public class LeaderCardDepotAbilityTest {
    private LeaderCardDepotAbility leaderCardDepotAbility;
    private GameBoard gameBoard;

    @BeforeEach
    public void testSetup() {
        gameBoard = new GameBoard();
        leaderCardDepotAbility = new LeaderCardDepotAbility(ResourceType.SHIELD, ResourceType.COIN, gameBoard);
        leaderCardDepotAbility.setVictoryPoints(2);
    }

    @Test
    public void testGetRequirements() {
    }

    @Test
    public void testSetLeaderCardAbility() {
        assertDoesNotThrow(() -> leaderCardDepotAbility.setLeaderCardAbility(gameBoard));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardDepotAbility.setLeaderCardAbility(gameBoard));
        leaderCardDepotAbility = new LeaderCardDepotAbility(ResourceType.COIN, ResourceType.SHIELD, gameBoard);
        assertDoesNotThrow(() -> leaderCardDepotAbility.setLeaderCardAbility(gameBoard));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardDepotAbility.setLeaderCardAbility(gameBoard));
    }

    @Test
    public void testCardVerified() {
        gameBoard.getStrongbox().addResources(new Resource[]{new Resource(5, ResourceType.SHIELD)});
        assertDoesNotThrow(() -> assertTrue(leaderCardDepotAbility.cardVerified(gameBoard)));
    }

    @Test
    public void testParsingLeaderCard() {
        String test;
        test = leaderCardDepotAbility.parsingLeaderCard();
        assertEquals("Requirements: 5 SHIELD\nVictory points: 2\nDepot resource type: COIN\n", test);
    }
}