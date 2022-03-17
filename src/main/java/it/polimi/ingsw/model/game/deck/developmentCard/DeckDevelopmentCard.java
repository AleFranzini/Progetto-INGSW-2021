package it.polimi.ingsw.model.game.deck.developmentCard;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.EmptyCardSlotException;
import it.polimi.ingsw.model.commons.Color;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class DeckDevelopmentCard
 *
 * @author Galetti Filippo
 */
public class DeckDevelopmentCard {
    private final DevelopmentCard[][][] deckDevelopmentCard;

    /**
     * The constructor initialize the deck by importing data from a json file
     */
    public DeckDevelopmentCard() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/DevelopmentCard.json"));
        this.deckDevelopmentCard = gson.fromJson(reader, DevelopmentCard[][][].class);

        shuffle();
    }

    /**
     * shuffle each one of the 12 deck from the deckDevelopmentCard.
     */
    public void shuffle() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                List<DevelopmentCard> asList = Arrays.asList(deckDevelopmentCard[i][j]);
                Collections.shuffle(asList);
                deckDevelopmentCard[i][j] = asList.toArray(new DevelopmentCard[4]);
            }
        }
    }

    /**
     * Give a development card from the specified card pile without "removing" it from the deck
     *
     * @param row    is the row of the card pile and can have value from 0 to 2
     * @param column is the column of the card pile and can have value from 0 to 3
     * @return the top card from the specified pile,
     * @throws EmptyStackException if the stack is empty
     */
    public DevelopmentCard peekCard(int row, int column) throws EmptyCardSlotException {
        for (int i = 0; i < 4; i++) {
            if (deckDevelopmentCard[row][column][i].getVisibilityOnDeck()) {
                return deckDevelopmentCard[row][column][i];
            }
        }
        throw new EmptyCardSlotException();
    }

    /**
     * hide a card from the deck by setting to false its visibility attribute
     *
     * @param cardToHide is the card to hide
     * @return true if the card is successfully hidden, false if the card was already hidden.
     */
    public boolean hideCard(DevelopmentCard cardToHide) {
        if (cardToHide.getVisibilityOnDeck()) {
            cardToHide.setVisibilityOnDeck(false);
            return true;
        }
        return false;
    }

    /**
     * set the visibility on false for two development cards of a specified color
     * (the method can only be called by action tokens in a single player match)
     *
     * @param color is the color of the two cards that will be discarded from the deck
     * @return true if two cards have been successfully discarded,
     * false if there weren't enough card (one or less) to discard.
     */
    public boolean discardColorCard(Color color) {
        int discardCounter = 0;
        int endedPile = 0;
        while (discardCounter < 2 && endedPile == 0) {
            if (color == Color.GREEN) {
                if (firstVisibleCard(0)) {
                    discardCounter++;
                } else {
                    endedPile++;
                }
            } else if (color == Color.BLUE) {
                if (firstVisibleCard(1)) {
                    discardCounter++;
                } else {
                    endedPile++;
                }
            } else if (color == Color.YELLOW) {
                if (firstVisibleCard(2)) {
                    discardCounter++;
                } else {
                    endedPile++;
                }
            } else if (color == Color.PURPLE) {
                if (firstVisibleCard(3)) {
                    discardCounter++;
                } else {
                    endedPile++;
                }
            }
        }
        return endedPile == 0;
    }

    /**
     * private method called by the discardColorCard method to set false the card visibility
     * parameter of a card of a specified color
     *
     * @param column correspondent to the order of the colors of the grid
     * @return true if the card visibility is successfully set to false,
     * false if there aren't any visible card of the specified color left in the deck.
     */
    private boolean firstVisibleCard(int column) {
        for (int i = 0; i < 3; i++) {
            try {
                if (peekCard(i, column).getVisibilityOnDeck()) {
                    peekCard(i, column).setVisibilityOnDeck(false);
                    if (i == 2) {
                        if (peekCard(i, column).getVisibilityOnDeck())
                            return true;
                    } else
                        return true;
                }
            } catch (EmptyCardSlotException e) {
                continue;
            }
        }
        return false;
    }

    /**
     * Get the list of ID of each top card in the card shop.
     *
     * @return the list of development card IDs
     */
    public List<Integer> developmentCardsIDList() {
        List<Integer> buyableCards = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++) {
                try {
                    buyableCards.add(peekCard(i, j).getId());
                } catch (EmptyCardSlotException e) {
                    buyableCards.add(-1);
                }
            }
        return buyableCards;
    }

}
