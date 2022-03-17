package it.polimi.ingsw.model.player;

/**
 * Class PopeTiles
 *
 * @author Grugni Federico
 */
public class PopeTiles {
    private final int victoryPoints;
    private boolean papalFavor;
    private boolean activable;
    private final int minRange;
    private final int maxRange;

    /**
     * constructor of pope tiles
     *
     * @param victoryPoints of the pope tile
     * @param papalFavor    if pope tiles is turn
     * @param activable     if pope tiles can be activated
     * @param minRange      where pope tile start
     * @param maxRange      where pope tile end
     */
    public PopeTiles(int victoryPoints, boolean papalFavor, boolean activable, int minRange, int maxRange) {
        this.victoryPoints = victoryPoints;
        this.papalFavor = papalFavor;
        this.activable = activable;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    /**
     * get the victory points granted by the pope tile
     *
     * @return the victory points if papal favor is activated
     */
    public int getVictoryPoints() {
        if (papalFavor)
            return victoryPoints;
        else
            return 0;
    }

    /**
     * the method is meaningful only if activable equals false,
     * it gives the state of the pope tile
     *
     * @return true if the pope tile victory points are earned,
     * false otherwise
     */
    public boolean isPapalFavor() {
        return papalFavor;
    }

    /**
     * set the value true to earn victory points, false not to
     * have the right to the victory points
     *
     * @param papalFavor is the boolean that determines
     *                   the impact of the pope tile
     */
    public void setPapalFavor(boolean papalFavor) {
        this.papalFavor = papalFavor;
    }

    /**
     * determines the relevance of the papalFavor attribute,
     * if returns true papalFavor is meaningful, otherwise it isn't
     *
     * @return true if nobody have activated the papal report,
     * false if the papal report has already been activated
     */
    public boolean isActivable() {
        return activable;
    }

    /**
     * is set to false when someone activate the papal report
     *
     * @param activable is passed as false to make the papalFavor meaningful
     */
    public void setActivable(boolean activable) {
        this.activable = activable;
    }

    /**
     * min range
     *
     * @return low limit of pope tile
     */
    public int getMinRange() {
        return minRange;
    }

    /**
     * max range
     *
     * @return high limit of pope tiles
     */
    public int getMaxRange() {
        return maxRange;
    }
}
