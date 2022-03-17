package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.deck.actionToken.DeckActionToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckActionTokenTest {

    DeckActionToken deck = new DeckActionToken();

    @Test
    void testCounter() {
        assertEquals(deck.getCounter(), 0);
        deck.incCounter();
        assertEquals(deck.getCounter(), 1);
        deck.shuffle();
        assertEquals(deck.getCounter(), 0);
    }

    @Test
    void testShuffle() {
        DeckActionToken test = new DeckActionToken();
        int count = 0, number = 0;
        for (int i = 0; i < 7; i++) {
            if (test.getActionToken(i) != null)
                number++;

            if (test.getActionToken(i).getValue() == deck.getActionToken(i).getValue()) {
                //System.out.println("TEST: " + test.getActionToken(i).getValue());
                //System.out.println("DECK: " + deck.getActionToken(i).getValue());
                count++;
            }
        }
        //System.out.println("Number: "+number+" Count: "+count);
        assertFalse(count == 7 || number < 7);
    }
}