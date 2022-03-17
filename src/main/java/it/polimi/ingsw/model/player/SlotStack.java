package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Level;
import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;

import java.util.*;

/**
 * Class SlotStack
 *
 * @author Galetti Filippo
 */
public class SlotStack {
    private final List<Deque<DevelopmentCard>> slotStack;
    private int cardQuantity;
    private int[][] flagsCounter;

    /**
     * Construct an empty slotstack composed of three deque and a counter for the development cards flags.
     */
    public SlotStack() {
        this.slotStack = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            slotStack.add(new ArrayDeque<>());
        }
        this.cardQuantity = 0;
        flagsCounter = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    }

    /**
     * Get the number of development card present in the deck.
     *
     * @return the number of cards
     */
    public int getCardQuantity() {
        return cardQuantity;
    }

    /**
     * Add a development card to one of the slots.
     *
     * @param card is the card to place in the defined slot
     * @param slot is the location of the slot where the development card is placed
     * @return true if the development card can be placed in the selected slot, false otherwise
     */
    public boolean addCard(DevelopmentCard card, int slot) {
        if (!checkCardValidityOnSlot(card, slot))
            return false;
        addCardQuantity();
        flagsCounter[Level.levelConverterToInt(card.getDevelopmentCardLevel())][Color.colorConverterToInt(card.getDevelopmentCardColor())] += 1;
        return slotStack.get(slot).offerFirst(card);
    }

    /**
     * Check in which slots a card can be placed.
     *
     * @param developmentCard is the card on which the check is performed
     * @return a list of the slots where the card can be placed, null if there is no slot to place the card
     */
    public List<Integer> checkCardValidity(DevelopmentCard developmentCard) {
        List<Integer> possibleSlots = new ArrayList<>();
        if (developmentCard.getDevelopmentCardVictoryPoints() == 0) {
            return null;
        }
        if (developmentCard.getDevelopmentCardLevel() == Level.LEVEL_1) {
            for (int i = 0; i < 3; i++) {
                if (peekCard(i) == null)
                    possibleSlots.add(i + 1);
            }
        } else if (developmentCard.getDevelopmentCardLevel() == Level.LEVEL_2) {
            for (int i = 0; i < 3; i++) {
                if (peekCard(i) != null && peekCard(i).getDevelopmentCardLevel() == Level.LEVEL_1)
                    possibleSlots.add(i + 1);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (peekCard(i) != null && peekCard(i).getDevelopmentCardLevel() == Level.LEVEL_2)
                    possibleSlots.add(i + 1);
            }
        }
        if (possibleSlots.isEmpty())
            return null;
        return possibleSlots;
    }

    /**
     * View the top card of a stack without removing it
     *
     * @param slot is the location of the card to view
     * @return the development card at the top of the stack if present, null otherwise
     */
    public DevelopmentCard peekCard(int slot) {
        return slotStack.get(slot).peek();
    }

    /**
     * Returns a list that contains the development cards at the top of each slot.
     * THe slots with no cards contains the value -1.
     *
     * @return the list of the top card of every slot
     */
    public List<Integer> peekTopCardsIDs() {
        List<Integer> topCards = new ArrayList<>(Arrays.asList(-1, -1, -1));
        for (int i = 0; i < 3; i++) {
            if (!slotStack.get(i).isEmpty()) {
                topCards.set(i, (slotStack.get(i).peek().getId()));
            }
        }
        return topCards;
    }

    /**
     * Private method called by method addCard when a card is added.
     */
    private void addCardQuantity() {
        this.cardQuantity++;
    }

    /**
     * Count the number of cards of a specified color.
     *
     * @param color of the cards which are counted
     * @return the number of cards
     */
    public int colorCardQuantity(Color color) {
        int colorCounter = 0;
        for (int i = 0; i < 3; i++) {
            colorCounter += flagsCounter[i][Color.colorConverterToInt(color)];
        }
        return colorCounter;
    }

    /**
     * Count the number of second level cards of a specified color.
     *
     * @param color of the cards which are counted
     * @return the number of cards
     */
    public int levelTwoCardQuantity(Color color) {
        return flagsCounter[1][Color.colorConverterToInt(color)];
    }

    /**
     * Get the attribute which stores the number of owned flags.
     *
     * @return the flagCounter attribute
     */
    public int[][] getFlagsCounter() {
        return flagsCounter;
    }

    /**
     * Check if the card can be placed in the designated slot.
     *
     * @param developmentCard is the card on which the check is performed
     * @param slot            is the designated slot for the card
     * @return true is the card can be placed in the slot, false otherwise
     */
    private boolean checkCardValidityOnSlot(DevelopmentCard developmentCard, int slot) {
        if (developmentCard.getDevelopmentCardLevel() == Level.LEVEL_1) {
            return peekCard(slot) == null;
        } else if (developmentCard.getDevelopmentCardLevel() == Level.LEVEL_2) {
            return peekCard(slot) != null && peekCard(slot).getDevelopmentCardLevel() == Level.LEVEL_1;
        } else {
            return peekCard(slot) != null && peekCard(slot).getDevelopmentCardLevel() == Level.LEVEL_2;
        }
    }


}
