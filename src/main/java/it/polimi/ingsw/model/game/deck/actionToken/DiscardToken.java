package it.polimi.ingsw.model.game.deck.actionToken;

import it.polimi.ingsw.model.commons.Color;

/**
 * Class DiscardToken
 *
 * @author Franzini Alessandro
 */
public class DiscardToken implements ActionToken<Color> {
    private Color color;

    /**
     * Get Value
     *
     * @return Color of the 2 DevelopmentCards that must be discarded
     */
    public Color getValue() {
        return this.color;
    }

}
