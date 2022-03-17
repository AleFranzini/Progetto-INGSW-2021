package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static it.polimi.ingsw.model.commons.Color.*;
import static it.polimi.ingsw.model.commons.Level.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeckDevelopmentCardTest {
    private DeckDevelopmentCard deck;

    private static Stream<Arguments> colors() {
        return Stream.of(
                Arguments.of(GREEN),
                Arguments.of(Color.BLUE),
                Arguments.of(Color.YELLOW),
                Arguments.of(Color.PURPLE)
        );
    }

    @BeforeEach
    public void testSetUp() {
        deck = new DeckDevelopmentCard();
    }

    @Test
    public void testPeekedCardIsNotNull() throws EmptyCardSlotException {
        assertNotNull(deck.peekCard(1, 1));
    }

    @Test
    public void testPeekCardWhenPileEmpty() throws EmptyCardSlotException {
        for (int i = 1; i < 5; i++) {
            deck.hideCard(deck.peekCard(1, 1));
        }
        assertThrows(EmptyCardSlotException.class, () -> deck.peekCard(1, 1));
    }

    @Test
    public void testDiscardColorCardWhenEnoughCards() {
        assertTrue(deck.discardColorCard(GREEN));
    }

    @ParameterizedTest
    @MethodSource("colors")
    public void testDiscardColorCardWHenOneCardRemainingBeforeLastCall(Color colors) throws EmptyCardSlotException {
        for (int i = 0; i < 5; i++) {
            assertTrue(deck.discardColorCard(colors));
        }
        deck.hideCard(deck.peekCard(2, 0));
        deck.hideCard(deck.peekCard(2, 1));
        deck.hideCard(deck.peekCard(2, 2));
        deck.hideCard(deck.peekCard(2, 3));

        assertFalse(deck.discardColorCard(colors));
        assertNotNull(deck.developmentCardsIDList());
    }

    @ParameterizedTest
    @MethodSource("colors")
    public void testDiscardColorCardWHenTwoCardRemainingBeforeLastCall(Color colors) {
        for (int i = 0; i < 5; i++) {
            assertTrue(deck.discardColorCard(colors));
        }
        assertFalse(deck.discardColorCard(colors));
    }

    @Test
    public void testHideCard() {
        deck.discardColorCard(Color.YELLOW);
        deck.discardColorCard(Color.YELLOW);
        assertThrows(EmptyCardSlotException.class, () -> deck.hideCard(deck.peekCard(0, 2)));
    }

    @RepeatedTest(5)
    void testShuffle() throws EmptyCardSlotException {
        deck.shuffle();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (i == 0) {
                        assertEquals(LEVEL_1, deck.peekCard(i, j).getDevelopmentCardLevel());
                    }
                    if (i == 1) {
                        assertEquals(LEVEL_2, deck.peekCard(i, j).getDevelopmentCardLevel());
                    }
                    if (i == 2) {
                        assertEquals(LEVEL_3, deck.peekCard(i, j).getDevelopmentCardLevel());
                    }
                    if (j == 0) {
                        assertEquals(GREEN, deck.peekCard(i, j).getDevelopmentCardColor());
                    }
                    if (j == 1) {
                        assertEquals(BLUE, deck.peekCard(i, j).getDevelopmentCardColor());
                    }
                    if (j == 2) {
                        assertEquals(YELLOW, deck.peekCard(i, j).getDevelopmentCardColor());
                    }
                    if (j == 3) {
                        assertEquals(PURPLE, deck.peekCard(i, j).getDevelopmentCardColor());
                    }
                    assertTrue(deck.hideCard(deck.peekCard(i, j)));
                }
            }
        }
    }
}