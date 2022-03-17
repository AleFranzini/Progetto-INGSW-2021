package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.model.commons.Resource;

/**
 * Interface Store
 *
 * @author Galetti Filippo
 */
public interface Store {
    /**
     * add the resources contained into the param to the target depot
     *
     * @param resources contains the resources to be added
     * @throws IllegalInputException if client input isn't correct
     */
    void addResources(Resource[] resources) throws IllegalInputException;

    /**
     * determines if the depot contains the resources in the param,
     * if it does and remove is true, those resources get removed from the depot;
     * if it does and remove is false, those resources are only peeked.
     * If the resources aren't in the store, returns the remaining resources from the input.
     * If all resources are present in the store, returns null.
     *
     * @param resources contains the resources to be removed
     * @param remove    if true remove the resources passed in the array,
     *                  if false doesn't change the store
     * @return a Resource or a Resource array that contains the required resources which aren't in the store
     */
    Resource[] giveResources(Resource[] resources, boolean remove);

    /**
     * returns all the resources contained in the depot
     *
     * @return a Resource or a Resource array with type and quantity of each resource
     */
    Resource[] peekAllResources();
}
