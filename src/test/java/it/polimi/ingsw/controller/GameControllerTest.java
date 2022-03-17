package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @BeforeEach
    public void setUp() {
        getGameController();
        getGame();

    }


    @Test
    public void countPlayersVictoryPoints() {
        getGame().addPlayer(new Player("player1"));
        getGame().addPlayer(new Player("player2"));
        getGame().addPlayer(new Player("player3"));
        getGame().addPlayer(new Player("player4"));
        getGame().addPlayer(new Player("player5"));
        getGame().addPlayer(new Player("player6"));
        getGame().addPlayer(new Player("player7"));
        Player player2 = getGame().getNextPlayer();
        player2.addVictoryPoints(15);
        player2.countVictoryPoints();
        getGame().getNextPlayer().addVictoryPoints(30);
        getGame().getNextPlayer().addVictoryPoints(30);
        getGame().getNextPlayer().addVictoryPoints(24);
        getGame().getNextPlayer().addVictoryPoints(100);
        getGame().getNextPlayer().addVictoryPoints(50);
        getGame().getNextPlayer().addVictoryPoints(77);
        List<Player> ranking = getGame().getAllPlayers().stream()
                .sorted(Comparator.comparing(Player::getVictoryPoints))
                .collect(Collectors.toList());
        Collections.reverse(ranking);
        for (Player player : ranking) {
            System.out.println(player.getUsername());
        }
    }
}