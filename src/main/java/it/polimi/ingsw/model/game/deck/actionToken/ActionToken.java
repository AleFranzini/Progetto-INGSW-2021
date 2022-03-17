package it.polimi.ingsw.model.game.deck.actionToken;

/**
 * Interface ActionToken
 *
 * @author Franzini Alessandro
 */
public interface ActionToken<T> {
    /**
     * returns the value of the token
     *
     * @return the Color to discard if it's a DiscardToken, or a Integer (1 or 2) if it's a BlackCrossToken
     */
    T getValue();
}