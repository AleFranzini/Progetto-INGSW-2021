package it.polimi.ingsw.model.game.deck.actionToken;

/**
 * Class BlackCrossToken
 *
 * @author Franzini Alessandro
 */
public class BlackCrossToken implements ActionToken<Integer> {
    private int quantity;

    /**
     * Get Value
     *
     * @return number of spaces that BlackCross must move forward (1 or 2)
     */
    public Integer getValue() {
        return this.quantity;
    }

}
