package it.polimi.ingsw.model;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.commons.LoadClassAdapter;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.game.deck.leaderCard.*;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DeckLeaderCardTest {
    DeckLeaderCard deckLeaderCard;

    @BeforeEach
    public void testSetup() {
        deckLeaderCard = new DeckLeaderCard();
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(LeaderCard.class, new LoadClassAdapter());
        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/json/LeaderCard.json")));
        deckLeaderCard.setDeckLeaderCard(gson.create().fromJson(reader, LeaderCard[].class));
    }

    @Test
    public void testGetCard() {
        LeaderCardDepotAbility leaderCardDepotAbility;
        LeaderCardDiscountAbility leaderCardDiscountAbility;
        LeaderCardProductionAbility leaderCardProductionAbility;
        LeaderCardResourceAbility leaderCardResourceAbility;
        /* take 4 leader card */
        int[] leaderCard = deckLeaderCard.getCard();
        assertEquals(1, leaderCard[1]);
        assertEquals(2, leaderCard[2]);
        /* take another 4 leader card */
        leaderCard = deckLeaderCard.getCard();
        assertEquals(4, leaderCard[0]);
        assertEquals(6, leaderCard[2]);
        /* take another 4 leader card */
        leaderCard = deckLeaderCard.getCard();
        assertEquals(8, leaderCard[0]);
        assertEquals(10, leaderCard[2]);
        /* take 4 card that must be the first 4 leader card */
        leaderCard = deckLeaderCard.getCard();
        leaderCard = deckLeaderCard.getCard();
        assertEquals(1, leaderCard[1]);
        assertEquals(2, leaderCard[2]);
    }

//    @Test
//    public void testShuffle() {
//        deckLeaderCard.shuffle();
//        assertNotNull(deckLeaderCard);
//    }

//    @Test
//    public void testSerialize(){
//        Resource resource = new Resource(3, ResourceType.COIN);
//        String string = deckLeaderCard.serialize();
//
//    }

    @Test
    public void testGetCardByID(){

    }
}