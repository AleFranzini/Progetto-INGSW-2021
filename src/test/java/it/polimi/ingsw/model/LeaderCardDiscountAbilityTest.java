package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardDiscountAbility;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardResourceAbility;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Level;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.SizeLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardDiscountAbilityTest {
    private GameBoard gameBoard;
    private LeaderCardDiscountAbility leaderCardDiscountAbility;

    @BeforeEach
    public void testSetup() {
        gameBoard = new GameBoard();
        leaderCardDiscountAbility = new LeaderCardDiscountAbility(new Color[]{Color.BLUE, Color.GREEN}, ResourceType.COIN);
        leaderCardDiscountAbility.setVictoryPoints(3);
    }

    @Test
    public void testGetRequirements() {
    }

    @Test
    public void testGetDiscountedResource() {
    }

//    @Test
//    public void testSetLeaderCard() {
//        assertDoesNotThrow(() -> leaderCardDiscountAbility.setLeaderCardAbility(gameBoard));
//        assertThrows(SizeLimitExceededException.class, () -> leaderCardDiscountAbility.setLeaderCardAbility(gameBoard));
//        leaderCardDiscountAbility = new LeaderCardDiscountAbility(new Color[]{Color.YELLOW, Color.PURPLE}, ResourceType.SERVANT);
//        assertDoesNotThrow(() -> leaderCardDiscountAbility.setLeaderCardAbility(gameBoard));
//        assertThrows(SizeLimitExceededException.class, () -> leaderCardDiscountAbility.setLeaderCardAbility(gameBoard));
//    }

    @Test
    public void testCardVerified() {
        LeaderCardResourceAbility leaderCardResourceAbility = new LeaderCardResourceAbility(new Color[]{Color.BLUE, Color.GREEN}, ResourceType.COIN);
        DevelopmentCard developmentCard = new DevelopmentCard(2, true);
        DevelopmentCard developmentCard1 = new DevelopmentCard(2, true);
        DevelopmentCard developmentCard2 = new DevelopmentCard(2, true);
        developmentCard.setDevelopmentCardColor(Color.BLUE);
        developmentCard.setDevelopmentCardLevel(Level.LEVEL_1);
        developmentCard1.setDevelopmentCardColor(Color.BLUE);
        developmentCard1.setDevelopmentCardLevel(Level.LEVEL_2);
        try {
            assertFalse(leaderCardResourceAbility.cardVerified(gameBoard));
        } catch (NotEnoughColorCardQuantity | AlreadyActivatedLeaderCardException e) {
            assert true;
        }
        developmentCard2.setDevelopmentCardColor(Color.GREEN);
        developmentCard2.setDevelopmentCardLevel(Level.LEVEL_1);
        gameBoard.getSlotStack().addCard(developmentCard, 0);
        gameBoard.getSlotStack().addCard(developmentCard1, 0);
        gameBoard.getSlotStack().addCard(developmentCard2, 2);
        try {
            assertTrue(leaderCardResourceAbility.cardVerified(gameBoard));
        } catch (NotEnoughColorCardQuantity | AlreadyActivatedLeaderCardException e) {
            assert false;
        }
    }

//    @Test
//    public void parsingLeaderCard() {
//        String test = leaderCardDiscountAbility.parsingLeaderCard();
//        assertEquals("Color requirements: 1 BLUE, 1 GREEN\nVictory points: 3\nDiscounted resource: COIN\n", test);
//    }
}