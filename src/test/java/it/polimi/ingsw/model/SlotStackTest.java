package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.player.SlotStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotStackTest {
    private SlotStack slotStack;
    private DeckDevelopmentCard deckDevelopmentCard;

    @BeforeEach
    void testSetUp() {
        slotStack = new SlotStack();
        deckDevelopmentCard = new DeckDevelopmentCard();
    }

    @Test
    void testAddCardSimple() throws EmptyCardSlotException {
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(0, 0), 0));
        assertFalse(slotStack.addCard(deckDevelopmentCard.peekCard(0, 1), 0));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(1, 1), 0));
        assertFalse(slotStack.addCard(deckDevelopmentCard.peekCard(1, 2), 1));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(0, 3), 1));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(2, 3), 0));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 1));
        assertFalse(slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 2));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(0, 3), 2));

    }

    @Test
    void testCheckCardValidity() throws EmptyCardSlotException {
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), slotStack.checkCardValidity(deckDevelopmentCard.peekCard(0, 0)));
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 0), 0);
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 1), 1);
        assertEquals(new ArrayList<>(Collections.singletonList(3)), slotStack.checkCardValidity(deckDevelopmentCard.peekCard(0, 1)));
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 3), 2);
        assertNull(slotStack.checkCardValidity(deckDevelopmentCard.peekCard(0, 0)));
        assertFalse(slotStack.addCard(deckDevelopmentCard.peekCard(0, 0), 1));
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 1), 0);
        assertEquals(new ArrayList<>(Arrays.asList(2, 3)), slotStack.checkCardValidity(deckDevelopmentCard.peekCard(1, 1)));
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 0), 1);
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 2);
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), slotStack.checkCardValidity(deckDevelopmentCard.peekCard(2, 2)));
        assertTrue(slotStack.addCard(deckDevelopmentCard.peekCard(2, 2), 2));
        assertEquals(7, slotStack.getCardQuantity());

    }

    @Test
    void testColorAndLevelCardQuantityTest() throws EmptyCardSlotException {
        // setup
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 0), 0);
        assertEquals(1, slotStack.colorCardQuantity(Color.GREEN));
        assertEquals(0, slotStack.colorCardQuantity(Color.YELLOW));
        assertEquals(0, slotStack.levelTwoCardQuantity(Color.YELLOW));

        slotStack.addCard(deckDevelopmentCard.peekCard(0, 1), 1);
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 2), 2);
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 2);
        slotStack.addCard(deckDevelopmentCard.peekCard(2, 3), 2);
        assertEquals(1, slotStack.colorCardQuantity(Color.GREEN));
        assertEquals(1, slotStack.colorCardQuantity(Color.BLUE));
        assertEquals(1, slotStack.colorCardQuantity(Color.YELLOW));
        assertEquals(2, slotStack.colorCardQuantity(Color.PURPLE));
        assertEquals(1, slotStack.levelTwoCardQuantity(Color.PURPLE));
        assertEquals(0, slotStack.levelTwoCardQuantity(Color.YELLOW));

        slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 0);
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 0), 1);
        slotStack.addCard(deckDevelopmentCard.peekCard(2, 1), 1);
        assertEquals(2, slotStack.colorCardQuantity(Color.GREEN));
        assertEquals(2, slotStack.colorCardQuantity(Color.BLUE));
        assertEquals(1, slotStack.colorCardQuantity(Color.YELLOW));
        assertEquals(3, slotStack.colorCardQuantity(Color.PURPLE));
        assertEquals(2, slotStack.levelTwoCardQuantity(Color.PURPLE));
        assertEquals(0, slotStack.levelTwoCardQuantity(Color.YELLOW));
        assertEquals(1, slotStack.levelTwoCardQuantity(Color.GREEN));
    }

    @Test
    void testPeekAllCards() throws EmptyCardSlotException {
        List<Integer> expectedList = new ArrayList<>(Arrays.asList(-1, -1, -1));

        assertEquals(new ArrayList<>(Arrays.asList(-1, -1, -1)), slotStack.peekTopCardsIDs());

        slotStack.addCard(deckDevelopmentCard.peekCard(0, 0), 0);
        expectedList.set(0, deckDevelopmentCard.peekCard(0, 0).getId());
        assertEquals(expectedList, slotStack.peekTopCardsIDs());

        slotStack.addCard(deckDevelopmentCard.peekCard(0, 1), 1);
        slotStack.addCard(deckDevelopmentCard.peekCard(0, 2), 2);
        expectedList.set(1, deckDevelopmentCard.peekCard(0, 1).getId());
        expectedList.set(2, deckDevelopmentCard.peekCard(0, 2).getId());
        assertEquals(expectedList, slotStack.peekTopCardsIDs());

        slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 2);
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 3), 0);
        slotStack.addCard(deckDevelopmentCard.peekCard(1, 0), 1);
        slotStack.addCard(deckDevelopmentCard.peekCard(2, 1), 1);

        expectedList.set(0, deckDevelopmentCard.peekCard(1, 3).getId());
        expectedList.set(2, deckDevelopmentCard.peekCard(1, 3).getId());
        expectedList.set(1, deckDevelopmentCard.peekCard(2, 1).getId());
        expectedList.set(2, deckDevelopmentCard.peekCard(1, 3).getId());

        assertEquals(expectedList, slotStack.peekTopCardsIDs());
    }
}