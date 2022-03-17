package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardProductionAbility;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardResourceAbility;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Level;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.SizeLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardProductionAbilityTest {
    private LeaderCardProductionAbility leaderCardProductionAbility;
    private GameBoard gameBoard;

    @BeforeEach
    public void testSetup() {
        gameBoard = new GameBoard();
        leaderCardProductionAbility = new LeaderCardProductionAbility(Color.BLUE, ResourceType.COIN);
        leaderCardProductionAbility.setVictoryPoints(3);
    }

    @Test
    public void testGetColorRequirements() {
    }

    @Test
    public void testGetLevelRequirements() {
    }

    @Test
    public void testGetProductionResource() {
    }

//    @Test
//    public void testSetLeaderCardAbility() throws SizeLimitExceededException {
//        assertDoesNotThrow(() -> leaderCardProductionAbility.setLeaderCardAbility(gameBoard));
//        /* try to add the same resourceId */
//        assertThrows(SizeLimitExceededException.class, () -> leaderCardProductionAbility.setLeaderCardAbility(gameBoard));
//        /* try to add another resourceId */
//        leaderCardProductionAbility = new LeaderCardProductionAbility(Color.BLUE, ResourceType.SERVANT);
//        assertDoesNotThrow(() -> leaderCardProductionAbility.setLeaderCardAbility(gameBoard));
//        /* try to add third resourceId */
//        assertThrows(SizeLimitExceededException.class, () -> leaderCardProductionAbility.setLeaderCardAbility(gameBoard));
//    }

    @Test
    public void testCardVerified() {
        DevelopmentCard developmentCard = new DevelopmentCard(2, true);
        DevelopmentCard developmentCard1 = new DevelopmentCard(2, true);
        DevelopmentCard developmentCard2 = new DevelopmentCard(2, true);
        developmentCard.setDevelopmentCardColor(Color.BLUE);
        developmentCard.setDevelopmentCardLevel(Level.LEVEL_1);
        developmentCard1.setDevelopmentCardColor(Color.BLUE);
        developmentCard1.setDevelopmentCardLevel(Level.LEVEL_2);
        developmentCard2.setDevelopmentCardColor(Color.GREEN);
        developmentCard2.setDevelopmentCardLevel(Level.LEVEL_1);
        gameBoard.getSlotStack().addCard(developmentCard, 0);
        gameBoard.getSlotStack().addCard(developmentCard1, 0);
        gameBoard.getSlotStack().addCard(developmentCard2, 2);
        LeaderCardResourceAbility leaderCardResourceAbility = new LeaderCardResourceAbility(new Color[]{Color.BLUE, Color.GREEN}, ResourceType.COIN);
        assertDoesNotThrow(() -> assertTrue(leaderCardResourceAbility.cardVerified(gameBoard)));
    }

//    @Test
//    public void testParsingLeaderCard() {
//        String test = leaderCardProductionAbility.parsingLeaderCard();
//        assertEquals("Requirements: 1 level two BLUE card\nVictory points: 3\nPower of production: 1 COIN } 1 prefered resource, 1 faith point", test);
//    }
}