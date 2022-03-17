package it.polimi.ingsw.model.commons;

/**
 * Class Production
 *
 * @author Galetti Filippo
 */
public class Production {
    private final Resource resourceIn;
    private boolean usable;

    /**
     * Construct a production object of quantity one and usability set to true.
     *
     * @param resourceIn is the resource type that will be produced
     */
    public Production(ResourceType resourceIn) {
        this.resourceIn = new Resource(1, resourceIn);
        this.usable = true;
    }

    /**
     * Get the resource that will be produced.
     *
     * @return the resource object
     */
    public Resource getResourceIn() {
        return resourceIn;
    }

    /**
     * Tell if the production can be activated or if it's already been activated.
     *
     * @return true if still to activate, false otherwise
     */
    public boolean isUsable() {
        return usable;
    }

    /**
     * Set if the production can be activated.
     *
     * @param usable true if the production is to be set activable, false otherwise
     */
    public void setUsable(boolean usable) {
        this.usable = usable;
    }
}
