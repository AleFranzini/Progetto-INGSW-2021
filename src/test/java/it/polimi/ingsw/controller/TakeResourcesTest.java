package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.exceptions.UndefinedLeaderCardAbilityException;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientOffline;
import it.polimi.ingsw.view.cli.CLI;
import org.junit.jupiter.api.*;

import javax.management.InstanceAlreadyExistsException;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;
import static org.junit.jupiter.api.Assertions.*;

class TakeResourcesTest {

    TakeResources takeResources;
    static ClientOffline clientOffline = new ClientOffline();

    @BeforeAll
    static void create() {
        CLI cli = new CLI(clientOffline);
        clientOffline.setView(cli);
        try {
            EventController.getEventController(clientOffline);
        } catch (InstanceAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        cli.setLorenzoFaithTrack();
        getGame().setLorenzoFaithTrack(new FaithTrack());

    }

    @BeforeEach
    void setUp() {
        Player player = new Player("test");
        getGame().addPlayer(player);
        getGameController().setTurn(player);
        getGameController().setGameModel();
        GameController.getGameController().getTurn().setCurrentPlayer(getGame().getFirstPlayerOfGame());
        takeResources = new TakeResources();
    }

    @Test
    void testBuyFromMarket() {
        Market marketBefore = GameController.getGameController().getGame().getMarket();
        //try buying a column
        String line = "c";
        int number = 3;
        Resource[] purchased = marketBefore.getColumn(number);
        for (int i = 0; i < 3; i++)
            marketBefore.getColumn(number);
        takeResources.buyFromMarket(number, line);
        assertEquals(marketBefore, GameController.getGameController().getGame().getMarket());
        assertArrayEquals(purchased, takeResources.getPurchased());
        System.out.println("        Market Before\n" + marketBefore.toString());
        //try buying a row
        line = "r";
        number = 0;
        purchased = marketBefore.getRow(number);
        for (int i = 0; i < 4; i++)
            marketBefore.getRow(number);
        takeResources.buyFromMarket(number, line);
        System.out.println("        Market After\n" + GameController.getGameController().getGame().getMarket().toString());
        assertEquals(marketBefore, GameController.getGameController().getGame().getMarket());
        System.out.println("Purchased marbles:");
        for (Resource resource : takeResources.getPurchased())
            System.out.print(resource.getQuantity() + " " + resource.getResourceType() + ", ");
        System.out.println();
        assertArrayEquals(purchased, takeResources.getPurchased());
    }

    @Test
    void testChecksOnMarbles() {
        testBuyFromMarket();

        assertDoesNotThrow(() -> GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().addBlankResource(ResourceType.SERVANT));
        assertDoesNotThrow(() -> takeResources.checksOnMarbles());

        System.out.println("After checks on marbles:");
        for (Resource resource : takeResources.getPurchased())
            System.out.print(resource.getQuantity() + " " + resource.getResourceType() + ", ");
    }

    @Test
    void testConvertBlankWhenTwoLeaders() {
        testBuyFromMarket();
        assertDoesNotThrow(() -> GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().addBlankResource(ResourceType.SERVANT));
        assertDoesNotThrow(() -> GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().addBlankResource(ResourceType.SHIELD));
        assertThrows(UndefinedLeaderCardAbilityException.class, () -> takeResources.checksOnMarbles());
        int[] input = new int[4];
        int i = 0;
        for (Resource resource : takeResources.getPurchased())
            if (resource.getResourceType() == ResourceType.BLANK) {
                input[i] = 1;
                i++;
            }
        takeResources.convertBlankWhenTwoLeaders(input);
        System.out.println("After checks on marbles:");
        for (Resource resource : takeResources.getPurchased())
            System.out.print(resource.getQuantity() + " " + resource.getResourceType() + ", ");
        System.out.print("\n");
    }

    @Test
    void testAddToStores() {
        testChecksOnMarbles();

        Resource[] inputWarehouse = new Resource[3];
        inputWarehouse[0] = new Resource(1, ResourceType.COIN);
        inputWarehouse[1] = new Resource(1, ResourceType.SERVANT);
        inputWarehouse[2] = new Resource(3, ResourceType.STONE);
        assertThrows(IllegalInputException.class, () -> takeResources.addToStores(inputWarehouse, null));
        for (int i = 0; i < inputWarehouse.length; i++)
            inputWarehouse[i] = null;

        Resource[] purchased = takeResources.getPurchased();
        boolean check = false;
        for (Resource resource : purchased) {
            if (resource.getQuantity() == 3) {
                inputWarehouse[2] = resource;
            }
            else if (resource.getQuantity() == 2) {
                if (inputWarehouse[1] == null)
                    inputWarehouse[1] = resource;
                else
                    inputWarehouse[2] = resource;
            } else if (resource.getQuantity() == 1) {
                if (inputWarehouse[0] == null)
                    inputWarehouse[0] = resource;
                else if (inputWarehouse[1] == null)
                    inputWarehouse[1] = resource;
                else
                    inputWarehouse[2] = resource;
            }
            if (resource.getQuantity() > 3)
                check = true;
        }
        if (!check)
            assertDoesNotThrow(() -> takeResources.addToStores(inputWarehouse, null));
        else
            assertThrows(IllegalInputException.class, () -> takeResources.addToStores(inputWarehouse, null));
        GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getWarehouse().giveResources(inputWarehouse, true);

        //try adding resource when leaderDepot is active
        assertDoesNotThrow(() -> GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().addDepot(inputWarehouse[0].getResourceType()));
        Resource[] inputLeader = new Resource[2];
        inputLeader[0] = inputWarehouse[0];
        inputLeader[1] = null;
        inputWarehouse[0] = null;
        if (!check)
            assertDoesNotThrow(() -> takeResources.addToStores(inputWarehouse, inputLeader));
        else
            assertThrows(IllegalInputException.class, () -> takeResources.addToStores(inputWarehouse, inputLeader));
        GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getWarehouse().giveResources(inputWarehouse, true);
        GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().giveResources(Resource.sortResources(inputLeader), true);


        //try discarding resources
        final String[] discarded = {""};
        assertDoesNotThrow(() -> discarded[0] = takeResources.checkDiscardedResource(Resource.sortResources(inputWarehouse)));
        assertDoesNotThrow(() -> takeResources.addToStores(inputWarehouse, null));
        if (discarded[0] != null && discarded[0].length() < 10)
            assertEquals(discarded[0], inputLeader[0].getResourceType().toString() + " ");
        GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getWarehouse().giveResources(inputWarehouse, true);
    }

    @AfterEach
    public void testReset() {
        GameController.getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().resetAbilities();
    }
}