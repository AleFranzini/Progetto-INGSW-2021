package it.polimi.ingsw.model.game.deck.developmentCard;

import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Level;
import it.polimi.ingsw.model.commons.Resource;

/**
 * Class DevelopmentCard
 *
 * @author Galetti Filippo
 */
public class DevelopmentCard {
    private int id;
    private Resource[] cost;
    private Color color;
    private Level level;
    private Resource[] resourceIn;
    private Resource[] resourceOut;
    private int victoryPoints;
    private boolean visibilityOnDeck;

    /**
     * Construct a development card with predefined victory points and deck visibility.
     *
     * @param victoryPoints    is the number of points the card will give at the end of the game
     * @param visibilityOnDeck is true if the card is already been bought, false otherwise.
     */
    public DevelopmentCard(int victoryPoints, boolean visibilityOnDeck) {
        this.victoryPoints = victoryPoints;
        this.visibilityOnDeck = visibilityOnDeck;
    }

    /**
     * Get the Development Card ID.
     *
     * @return ID of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Get Development Card Cost.
     *
     * @return cost of card
     */
    public Resource[] getDevelopmentCardCost() {
        return this.cost;
    }

    /**
     * Get Development Card Type.
     *
     * @return Type of development card
     */
    public Color getDevelopmentCardColor() {
        return this.color;
    }

    /**
     * Set Development Card Type.
     *
     * @param color type of development card
     */
    public void setDevelopmentCardColor(Color color) {
        this.color = color;
    }

    /**
     * Get Development Card Level.
     *
     * @return level of development card
     */
    public Level getDevelopmentCardLevel() {
        return this.level;
    }

    /**
     * Set Development Card Level.
     *
     * @param level level of development card
     */
    public void setDevelopmentCardLevel(Level level) {
        this.level = level;
    }

    /**
     * Get Development Card Victory point.
     *
     * @return victory point of development card
     */
    public int getDevelopmentCardVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Get Resource In.
     *
     * @return resources that development card required
     */
    public Resource[] getResourceIn() {
        return this.resourceIn;
    }

    /**
     * Get Resource Out.
     *
     * @return resources that development card give
     */
    public Resource[] getResourceOut() {
        return this.resourceOut;
    }

    /**
     * Get Visibility On Deck.
     *
     * @return visibility of development card
     */
    public boolean getVisibilityOnDeck() {
        return this.visibilityOnDeck;
    }

    /**
     * Set Visibility On Deck
     *
     * @param visibilityOnDeck visibility of development card
     */
    public void setVisibilityOnDeck(boolean visibilityOnDeck) {
        this.visibilityOnDeck = visibilityOnDeck;
    }

}
