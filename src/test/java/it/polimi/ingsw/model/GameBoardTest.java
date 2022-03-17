package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalFaithResourceInputException;
import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private static GameBoard gameBoard;

    @BeforeEach
    void testSetup() {
        gameBoard = new GameBoard();
    }

    @Test
    public void testBasicProduction() throws IllegalInputException, IllegalFaithResourceInputException {
        assertThrows(IllegalFaithResourceInputException.class,
                () -> gameBoard.basicProduction(ResourceType.COIN, ResourceType.SERVANT, ResourceType.FAITH));
        //setup stores
        Resource[] resources = new Resource[3];
        resources[0] = new Resource(1, ResourceType.COIN);
        resources[1] = new Resource(2, ResourceType.SERVANT);
        resources[2] = new Resource(3, ResourceType.STONE);
        gameBoard.getWarehouse().addResources(resources);

        assertFalse(gameBoard.basicProduction(ResourceType.COIN, ResourceType.SHIELD, ResourceType.SERVANT));

        Resource[] depotResource = new Resource[2];
        depotResource[0] = new Resource(1, ResourceType.STONE);
        assertDoesNotThrow(() -> gameBoard.getLeaderCardAbility().addDepot(ResourceType.STONE));
        gameBoard.getLeaderCardAbility().addResources(depotResource);

        assertFalse(gameBoard.basicProduction(ResourceType.COIN, ResourceType.SHIELD, ResourceType.SERVANT));


        Resource[] resources2 = new Resource[4];
        resources2[0] = new Resource(2, ResourceType.COIN);
        resources2[1] = new Resource(2, ResourceType.SERVANT);
        resources2[2] = new Resource(2, ResourceType.SHIELD);
        resources2[3] = new Resource(2, ResourceType.STONE);
        gameBoard.getStrongbox().addResources(resources2);
        //

        assertTrue(gameBoard.basicProduction(ResourceType.COIN, ResourceType.SHIELD, ResourceType.SERVANT));

    }

    @Test
    void testAutomaticResourcesRequest() throws IllegalInputException {
        //SetUp and check
        Resource[] resources = new Resource[3];
        resources[0] = new Resource(1, ResourceType.COIN);
        resources[1] = new Resource(2, ResourceType.SERVANT);
        resources[2] = new Resource(3, ResourceType.STONE);
        gameBoard.getWarehouse().addResources(resources);
        Resource[] resources2 = new Resource[4];
        resources2[0] = resources[0];
        resources2[1] = resources[1];
        resources2[3] = resources[2];
        resources2[2] = new Resource(0, ResourceType.SHIELD);
        assertArrayEquals(resources2, Resource.sortResources(gameBoard.getWarehouse().peekAllResources()));

        resources2[0] = new Resource(2, ResourceType.COIN);
        resources2[1] = new Resource(2, ResourceType.SERVANT);
        resources2[2] = new Resource(2, ResourceType.SHIELD);
        resources2[3] = new Resource(1, ResourceType.STONE);
        assertDoesNotThrow(() -> gameBoard.getLeaderCardAbility().addDepot(ResourceType.STONE));
        gameBoard.getLeaderCardAbility().addResources(resources2);
        assertEquals(gameBoard.getLeaderCardAbility().peekAllResources()[0].getQuantity(), 1);

        resources2[0] = new Resource(4, ResourceType.COIN);
        resources2[1] = new Resource(3, ResourceType.SERVANT);
        resources2[2] = new Resource(2, ResourceType.SHIELD);
        resources2[3] = new Resource(0, ResourceType.STONE);
        gameBoard.getStrongbox().addResources(resources2);
        assertArrayEquals(resources2, gameBoard.getStrongbox().peekAllResources());

        Resource[] resources1 = new Resource[4];
        resources1[0] = new Resource(5, ResourceType.COIN);
        resources1[1] = new Resource(5, ResourceType.SERVANT);
        resources1[2] = new Resource(3, ResourceType.SHIELD);
        resources1[3] = new Resource(4, ResourceType.STONE);
        assertFalse(gameBoard.automaticResourcesRequest(resources1, false));

        resources1[0] = new Resource(5, ResourceType.COIN);
        resources1[1] = new Resource(5, ResourceType.SERVANT);
        resources1[2] = new Resource(2, ResourceType.SHIELD);
        resources1[3] = new Resource(4, ResourceType.STONE);
        assertTrue(gameBoard.automaticResourcesRequest(resources1, false));

        assertTrue(gameBoard.automaticResourcesRequest(resources1, true));

        assertNull(gameBoard.getWarehouse().peekAllResources());
        assertNull(gameBoard.getStrongbox().peekAllResources());
        assertNull(gameBoard.getLeaderCardAbility().peekAllResources());

    }

    @Test
    void testCheckResourceRequirement() throws IllegalInputException {
        Resource[] resources = new Resource[3];
        resources[0] = new Resource(1, ResourceType.COIN);
        resources[1] = new Resource(2, ResourceType.SERVANT);
        resources[2] = new Resource(3, ResourceType.STONE);
        gameBoard.getWarehouse().addResources(resources);
        Resource[] resources2 = new Resource[4];
        resources2[0] = resources[0];
        resources2[1] = resources[1];
        resources2[3] = resources[2];
        resources2[2] = new Resource(0, ResourceType.SHIELD);
        assertArrayEquals(resources2, Resource.sortResources(gameBoard.getWarehouse().peekAllResources()));

        resources2[0] = new Resource(2, ResourceType.COIN);
        resources2[1] = new Resource(2, ResourceType.SERVANT);
        resources2[2] = new Resource(2, ResourceType.SHIELD);
        resources2[3] = new Resource(1, ResourceType.STONE);
        assertDoesNotThrow(() -> gameBoard.getLeaderCardAbility().addDepot(ResourceType.STONE));
        gameBoard.getLeaderCardAbility().addResources(resources2);
        assertEquals(gameBoard.getLeaderCardAbility().peekAllResources()[0].getQuantity(), 1);

        resources2[0] = new Resource(2, ResourceType.COIN);
        resources2[1] = new Resource(2, ResourceType.SERVANT);
        resources2[2] = new Resource(2, ResourceType.SHIELD);
        resources2[3] = new Resource(2, ResourceType.STONE);
        gameBoard.getStrongbox().addResources(resources2);
        assertArrayEquals(resources2, gameBoard.getStrongbox().peekAllResources());

        Resource[] requirements = new Resource[4];
        requirements[0] = new Resource(2, ResourceType.COIN);
        requirements[1] = new Resource(2, ResourceType.SERVANT);
        requirements[2] = new Resource(2, ResourceType.SHIELD);
        requirements[3] = new Resource(1, ResourceType.STONE);
        assertTrue(gameBoard.checkResourceRequirement(requirements));

        Resource[] requirements2 = new Resource[4];
        requirements2[0] = new Resource(3, ResourceType.COIN);
        requirements2[1] = new Resource(4, ResourceType.SERVANT);
        requirements2[2] = new Resource(2, ResourceType.SHIELD);
        requirements2[3] = new Resource(6, ResourceType.STONE);
        assertNotNull(gameBoard.peekAllStoresResources());
        assertTrue(gameBoard.checkResourceRequirement(requirements2));

        Resource[] requirements3 = new Resource[4];
        requirements3[0] = new Resource(4, ResourceType.COIN);
        requirements3[1] = new Resource(4, ResourceType.SERVANT);
        requirements3[2] = new Resource(2, ResourceType.SHIELD);
        requirements3[3] = new Resource(6, ResourceType.STONE);
        assertFalse(gameBoard.checkResourceRequirement(requirements3));

        assertFalse(gameBoard.checkResourceRequirement(null));
    }
}