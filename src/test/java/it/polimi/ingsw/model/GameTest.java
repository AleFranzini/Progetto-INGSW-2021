package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @BeforeEach
    void testSetUp() {
        getGame().removeAllPlayer();
    }

    @Test
    void testAddPlayer() {
        getGame().removeAllPlayer();
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        getGame().addPlayer(player1);
        getGame().removePlayer(getGame().getFirstPlayerOfGame().getUsername());
        assertTrue(getGame().setNumberOfPlayers(4));
        assertFalse(getGame().setNumberOfPlayers(5));
        assertEquals(getGame().getNumberOfPlayers(), 4);
        getGame().addPlayer(player1);
        getGame().addPlayer(player2);
        getGame().addPlayer(player3);
        getGame().addPlayer(player4);

        for (int i = 0; i < 12; i++) {
            Player firstPlayer = getGame().getFirstPlayerOfGame();
            for (int j = 0; j < 3; j++) {
                getGame().getNextPlayer();
            }
            assertEquals(firstPlayer.getUsername(), getGame().getNextPlayer().getUsername());
        }
        getGame().removePlayer(player1.getUsername());
        getGame().removePlayer(player1.getUsername());
        getGame().removePlayer(player3.getUsername());
        getGame().removePlayer(player4.getUsername());
        getGame().addPlayer(player4);
    }

    @Test
    void testGetAllNonCurrentPlayersTest() {
        getGame().removeAllPlayer();
        getGame().setNumberOfPlayers(4);
        List<Player> tempPlayersList = new ArrayList<>();
        Player player1 = new Player("player1");
        tempPlayersList.add(player1);
        Player player2 = new Player("player2");
        tempPlayersList.add(player2);
        Player player3 = new Player("player3");
        tempPlayersList.add(player3);
        Player player4 = new Player("player4");
        tempPlayersList.add(player4);
        getGame().addPlayer(player1);
        getGame().addPlayer(player2);
        getGame().addPlayer(player3);
        getGame().addPlayer(player4);

        Player player = getGame().getFirstPlayerOfGame();
        getGameController().setTurn(player);
        tempPlayersList.remove(player1);

        assertTrue(tempPlayersList.contains(player2));
        assertTrue(tempPlayersList.contains(player3));
        assertTrue(tempPlayersList.contains(player4));

    }

    @Test
    public void testGetNextPlayer() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");

        getGame().addPlayer(player1);
        getGame().addPlayer(player2);
        getGame().addPlayer(player3);
        getGame().addPlayer(player4);

        Player plTest = getGame().getFirstPlayerOfGame();
        List<Player> test = new ArrayList<>(getGame().getAllPlayers());
        assertEquals(test.get(0).getUsername(), plTest.getUsername());
        //assertEquals(4, getGame().getNumberOfPlayers());
        Player next = getGame().getNextPlayer();
        assertEquals(test.get(1).getUsername(), next.getUsername());
        next = getGame().getNextPlayer();
        //assertEquals(4, getGame().getNumberOfPlayers());
        assertEquals(test.get(2).getUsername(), next.getUsername());
        next = getGame().getNextPlayer();
        //assertEquals(4, getGame().getNumberOfPlayers());
        assertEquals(test.get(3).getUsername(), next.getUsername());
        next = getGame().getNextPlayer();
        //assertEquals(4, getGame().getNumberOfPlayers());

        //n run
        int n = 16;
        for (int i = 0; i < n; i++) {
            assertEquals(next.getUsername(), test.get(0).getUsername());
            //assertEquals(4, getGame().getNumberOfPlayers());
            next = getGame().getNextPlayer();
            assertEquals(next.getUsername(), test.get(1).getUsername());
            next = getGame().getNextPlayer();
            //assertEquals(4, getGame().getNumberOfPlayers());
            assertEquals(next.getUsername(), test.get(2).getUsername());
            next = getGame().getNextPlayer();
            //assertEquals(4, getGame().getNumberOfPlayers());
            assertEquals(next.getUsername(), test.get(3).getUsername());
            next = getGame().getNextPlayer();
            //assertEquals(4, getGame().getNumberOfPlayers());
        }

    }

    @Test
    public void testSetMethods() {
        assertFalse(getGame().isLastTurn());
        getGame().setLastTurn();
        assertTrue(getGame().isLastTurn());
        assertNotNull(getGame().getDeckDevelopmentCard());
        assertNotNull(getGame().getDeckLeaderCard());
        assertNotNull(getGame().getMarket());

    }

    @Test
    public void testSetPlayers() {
        List<Player> players = new ArrayList<>();
        Player player1 = new Player("player1");
        players.add(player1);
        Player player2 = new Player("player2");
        players.add(player2);
        Player player3 = new Player("player3");
        players.add(player3);
        Player player4 = new Player("player4");
        players.add(player4);

        getGame().setPlayers(players);
        assertEquals(players, getGame().getAllPlayers());
        assertNotNull(getGame().getAllNonCurrentPlayers());
        assertNotNull(getGame().getCurrentPlayer());


    }

    @AfterEach
    public void testAfterEach() {
        getGame().setCurrentPlayer(0);
        getGame().removeAllPlayer();
    }

}