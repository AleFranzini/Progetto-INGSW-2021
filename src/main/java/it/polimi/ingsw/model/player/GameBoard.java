package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.IllegalFaithResourceInputException;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardAbility;

/**
 * Class GameBoard
 *
 * @author Galetti Filippo
 */
public class GameBoard {
    private final Warehouse warehouse;
    private final Strongbox strongbox;
    private final SlotStack slotStack;
    private final FaithTrack faithTrack;
    private final LeaderCardAbility leaderCardAbility;

    /**
     * Construct a gameboard with the default structures.
     */
    public GameBoard() {
        this.warehouse = new Warehouse();
        this.strongbox = new Strongbox();
        this.slotStack = new SlotStack();
        this.faithTrack = new FaithTrack();
        this.leaderCardAbility = new LeaderCardAbility();
    }

    /**
     * Get the player's warehouse.
     *
     * @return a reference to the warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Get the player's strongbox.
     *
     * @return a reference to the strongbox
     */
    public Strongbox getStrongbox() {
        return strongbox;
    }

    /**
     * Get the player's slotstack.
     *
     * @return a reference to the slotstack
     */
    public SlotStack getSlotStack() {
        return slotStack;
    }

    /**
     * Get the player's Faith Track.
     *
     * @return a reference to the Faith Track
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Get the LeaderCardAbility class.
     *
     * @return a reference to the LeaderCardAbility class
     */
    public LeaderCardAbility getLeaderCardAbility() {
        return leaderCardAbility;
    }

    /**
     * Implements the behaviour of the basic production inside of the scroll:
     * check the warehouse and strongbox (also the leader card depot if activated)
     * for the input resources and, if those are present, remove them and
     * add the output resource to the strongbox.
     *
     * @param input1 is the first resource given as input.
     * @param input2 is the second resource given as input.
     * @param output is the resource requested as output.
     * @return true if the input resources are present and removed and output resource
     * is added, false if the input resources aren't present.
     */
    public boolean basicProduction(ResourceType input1, ResourceType input2, ResourceType output) throws IllegalFaithResourceInputException {
        if (output == ResourceType.FAITH)
            throw new IllegalFaithResourceInputException();
        Resource[] resourcesIn = new Resource[2];
        resourcesIn[0] = new Resource(1, input1);
        resourcesIn[1] = new Resource(1, input2);
        if (getStrongbox().giveResources(getLeaderCardAbility().giveResources(getWarehouse().giveResources(resourcesIn, false), false), false) != null)
            return false;
        if (getStrongbox().giveResources(getLeaderCardAbility().giveResources(getWarehouse().giveResources(resourcesIn, true), true), true) != null)
            return false;

        Resource[] resourcesOut = new Resource[1];
        resourcesOut[0] = new Resource(1, output);
        strongbox.addResources(resourcesOut);
        return true;
    }

    /**
     * If the remove parameter is set true it automatically removes the resources requested in the parameter by checking with each store.
     * First it removes resources from the Warehouse, if anything is left it removes from the leader card depot,
     * and at last it removes what's left in the parameter from the strongBox. Returns true if every resources was found in the stores,
     * false otherwise.
     * If the remove parameter is false the method only check if all the resources can be found in the stores and
     * returns true if that's the case, false otherwise.
     *
     * @param resources contains the array with the resources to be found in the stores.
     * @param remove    if true indicates to remove the resources from the store, if false indicates only to check if those are present.
     * @return true if all the resources were found, false otherwise.
     */
    public boolean automaticResourcesRequest(Resource[] resources, boolean remove) {
        if (resources == null)
            return false;
        Resource[] leftResources;
        leftResources = getWarehouse().giveResources(resources, remove);
        if (leftResources != null) {
            leftResources = getLeaderCardAbility().giveResources(leftResources, remove);
        }
        if (leftResources != null) {
            leftResources = getStrongbox().giveResources(leftResources, remove);
        }
        return leftResources == null;
    }

    /**
     * compare the requirements array to the sum of all the resources in every store and, if there is at least
     * as much quantity of resources in the sum array as there is in the requirements array, returns true,
     * false otherwise. If one of the arrays is null, returns false
     *
     * @param requirements is the array to compare
     * @return true if there are enough resources in the stores to cover the requirements, false otherwise.
     */
    public boolean checkResourceRequirement(Resource[] requirements) {
        requirements = Resource.sortResources(requirements);
        Resource[] resources;
        resources = Resource.sumResources(getWarehouse().peekAllResources(), getStrongbox().peekAllResources());
        resources = Resource.sumResources(resources, getLeaderCardAbility().peekAllResources());
        if (resources == null || requirements == null)
            return false;
        for (int i = 0; i < 4; i++) {
            if (requirements[i].getQuantity() > resources[i].getQuantity())
                return false;
        }
        return true;
    }

    /**
     * Sums the resources from all stores and returns an array with the sums. If there are no resources, returns null.
     * @return an array with all the resources from every store
     */
    public Resource[] peekAllStoresResources() {
        Resource[] resources;
        resources = Resource.sumResources(getWarehouse().peekAllResources(), getStrongbox().peekAllResources());
        resources = Resource.sumResources(resources, getLeaderCardAbility().peekAllResources());
        return resources;
    }
}
