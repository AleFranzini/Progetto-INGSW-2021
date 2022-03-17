package it.polimi.ingsw.model.game.deck.leaderCard;

import it.polimi.ingsw.exceptions.EmptyProductionListException;
import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.exceptions.UndefinedLeaderCardAbilityException;
import it.polimi.ingsw.model.commons.Production;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.player.Store;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class LeaderCardAbility
 *
 * @author Galetti Filippo, Grugni Federico
 */
public class LeaderCardAbility implements Store {

    private final List<ResourceType> discountAbility = new ArrayList<>();
    private final List<Production> productionAbility = new ArrayList<>();
    private final List<ResourceType> blankResourceAbility = new ArrayList<>();
    private final List<Resource> depotAbility = new ArrayList<>();

    /**
     * Add a new type of resource to the list of resources which get a discount of 1 quantity.
     *
     * @param discount is the new type of resource which can be discounted
     * @throws SizeLimitExceededException if add more 2 card
     */
    public void addDiscount(ResourceType discount) throws SizeLimitExceededException {
        if (discountAbility.size() >= 2 || (discountAbility.size() > 0 && discountAbility.get(0) == discount))
            throw new SizeLimitExceededException();
        discountAbility.add(discount);
    }

    /**
     * Apply a discount of the types found in the list to the array of resources passed as a parameter in the form of 1 quantity.
     *
     * @param resources is the array of resources to which the discount will be applied
     * @return the array after the discounts are applied
     */
    public Resource[] applyDiscountAbility(Resource[] resources) {
        if (resources == null)
            return null;
        for (Resource resource : resources) {
            for (ResourceType resourceType : discountAbility)
                if (resource.getResourceType() == resourceType && resource.getQuantity() > 0) {
                    resource.decQuantity(1);
                }
        }
        return Resource.isEmptyArrayResource(Resource.sortResources(resources));
    }

    /**
     * Add a new production ability to the list of productions that can be called.
     *
     * @param resourceIn is the input resource of the new production ability
     * @throws SizeLimitExceededException if add more 2 card
     */
    public void addProductionAbility(ResourceType resourceIn) throws SizeLimitExceededException {
        if (productionAbility.size() >= 2 || (productionAbility.size() > 0 && productionAbility.get(0).getResourceIn().getResourceType() == resourceIn))
            throw new SizeLimitExceededException();
        productionAbility.add(new Production(resourceIn));
    }

    /**
     * Get the resource input of a usable element of the productionAbility list and make that element unusable.
     * An element is usable when the usable attribute of that element is true.
     *
     * @return the resource input of tha production element if there is only one usable element in the productionAbility list
     * @throws UndefinedLeaderCardAbilityException if there are two usable elements in the productionAbility list
     */
    public Resource getProductionAbilityResourceIn() throws UndefinedLeaderCardAbilityException {
        for (Production production : productionAbility) {
            if (production.isUsable())
                break;
        }
        if (productionAbility.size() > 1) {
            throw new UndefinedLeaderCardAbilityException("productionAbility");
        } else {
            productionAbility.get(0).setUsable(false);
            return productionAbility.get(0).getResourceIn();
        }
    }

    /**
     * Get the resource input of the element at the specified index of the productionAbility list and make that element unusable.
     * If the element at the defined index is unusable, the method will behave as if the list is empty, thus throwing
     * the EmptyProductionListException.
     * An element is usable when the usable attribute of that element is true.
     * The method is implemented so that the two possible indexes are 0 and 1.
     *
     * @return the resource input of the production element at the specified index
     * @throws EmptyProductionListException when the productionAbility list is empty
     */
    public Resource getProductionAbilityResourceIn(int index) throws EmptyProductionListException {
        // maybe necessary to check the index range
        if (!productionAbility.get(index).isUsable())
            throw new EmptyProductionListException();
        productionAbility.get(index).setUsable(false);
        return productionAbility.get(index).getResourceIn();
    }

    /**
     * Sets all elements usable attribute to true.
     */
    public void resetElementsUsability() {
        for (Production production : productionAbility) {
            production.setUsable(true);
        }
    }

    /**
     * Verify if there is any production which has not yet been activated.
     *
     * @return true is at least a production is left, false if all productions have been activated.
     */
    public boolean productionAbilityLeft() {
        for (Production production : productionAbility) {
            if (production.isUsable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new resource type to the list of resources in which the blank resource can turn into.
     *
     * @param blankResource is the new type of resource to be added to the list
     * @throws SizeLimitExceededException if add more 2 card
     */
    public void addBlankResource(ResourceType blankResource) throws SizeLimitExceededException {
        if (blankResourceAbility.size() >= 2 || (blankResourceAbility.size() > 0 && blankResourceAbility.get(0) == blankResource))
            throw new SizeLimitExceededException();
        blankResourceAbility.add(blankResource);
    }

    /**
     * Convert the blank resources in the passed array into the resource indicated in the blankResourceAbility list,
     * if the list only has one element.
     * If there are no element in the list, all the blank resources in the passed array get removed.
     * If there are two element in the list, the method throws the UndefinedBlankResourceException.
     *
     * @param resources is the array that contains the blank resources to be converted
     * @return the array with the converted blank resources if there is one element in the blankResourceAbility list.
     * Returns the array after having removed the blank resources if the list is empty
     * @throws UndefinedLeaderCardAbilityException if there are two element in the blankResourceAbility list
     */
    public Resource[] convertBlankResource(Resource[] resources) throws UndefinedLeaderCardAbilityException {
        if (blankResourceAbility.size() >= 2) {
            throw new UndefinedLeaderCardAbilityException("blankResource");
        } else {
            ArrayList<Resource> tmp = new ArrayList<>();
            if (blankResourceAbility.size() == 1)
                for (Resource resource : resources)
                    if (resource.getResourceType() == ResourceType.BLANK) {
                        resource.setResourceType(blankResourceAbility.get(0));
                        tmp.add(resource);
                    }
            Resource[] out = Resource.isEmptyArrayResource(Resource.sortResources(resources));
            for (Resource resource : tmp) resource.setResourceType(ResourceType.BLANK);
            return out;
        }
    }

    /**
     * Get the resource type of the one or two resources contained in the blankResourceAbility list.
     *
     * @return an array with the element(s) in the blankResourceAbility list.
     * If there are no elements, returns null
     */
    public ResourceType[] getBlankResourcesType() {
        return blankResourceAbility.toArray(new ResourceType[2]);
    }

    /**
     * Add a new depot to the depotAbility list.
     *
     * @param depotResource is the resource type of the new depot
     * @throws SizeLimitExceededException if add more 2 card
     */
    public void addDepot(ResourceType depotResource) throws SizeLimitExceededException {
        if (depotAbility.size() >= 2 || (depotAbility.size() > 0 && depotAbility.get(0).getResourceType() == depotResource))
            throw new SizeLimitExceededException();
        depotAbility.add(new Resource(0, depotResource));
    }

    @Override
    public void addResources(Resource[] resources) throws IllegalInputException {
        resources = Resource.sortResources(resources);
        if (resources != null) {
            for (Resource depot : depotAbility) {
                for (Resource resource : resources)
                    if (resource.getResourceType() == depot.getResourceType()) {
                        depot.incQuantity(resource.getQuantity());
                        if (depot.getQuantity() > 2) {
                            resource.setQuantity(depot.getQuantity() - 2);
                            depot.setQuantity(2);
                            throw new IllegalInputException(resources);
                        }
                    }
            }
        }
    }

    @Override
    public Resource[] giveResources(Resource[] resources, boolean remove) {
        if (resources == null)
            return null;
        for (Resource resource : resources) {
            for (Resource value : depotAbility)
                if (value.getTypeResource() == resource.getTypeResource()) {
                    if (resource.getQuantity() <= value.getQuantity()) {
                        if (remove) {
                            value.decQuantity(resource.getQuantity());
                        }
                        resource.setQuantity(0);
                    } else {
                        resource.decQuantity(value.getQuantity());
                        if (remove)
                            value.setQuantity(0);
                    }
                }
        }
        return Resource.isEmptyArrayResource(Resource.sortResources(resources));
    }

    @Override
    public Resource[] peekAllResources() {
        Resource[] resources = new Resource[2];
        int i = 0;
        for (Resource resource : depotAbility) {
            resources[i] = resource;
            i++;
        }
        return Resource.isEmptyArrayResource(resources);
    }

    /**
     * peek resource specify in index
     *
     * @param index of depot list
     * @return resource at index in depot list
     */
    public Resource peekAllResources(int index) {
        return depotAbility.get(index);
    }

    /**
     * Determines if the depot at the defined index in the depotAbility list contains the resources in the param,
     * if it does and remove is true, those resources get removed from the depot;
     * if it does and remove is false, those resources are only peeked.
     * If the resources aren't in the store, returns the remaining resources from the input.
     * If all resources are present in the store, returns null.
     *
     * @param resource contains the resources to be removed
     * @param remove   if true remove the resources passed in the array,
     *                 if false doesn't change the store
     * @param index    of the depotAbility list at which the target depot is located
     * @return an array that contains the required resources which aren't in the store
     */
    public Resource giveResources(Resource resource, boolean remove, int index) {
        if (depotAbility.get(index).getResourceType() == resource.getResourceType()) {
            if (resource.getQuantity() <= depotAbility.get(index).getQuantity()) {
                if (remove)
                    depotAbility.get(index).decQuantity(resource.getQuantity());
                resource.setQuantity(0);
                return null;
            } else {
                resource.decQuantity(depotAbility.get(index).getQuantity());
                if (remove)
                    depotAbility.get(index).setQuantity(0);
            }
        }
        return resource;
    }

    /**
     * Get the number of leader with depot ability
     *
     * @return the number of leader with depot ability activated
     */
    public int numLeaderDepot() {
        return depotAbility.size();
    }

    /**
     * reset all ability
     */
    public void resetAbilities() {
        discountAbility.clear();
        depotAbility.clear();
        blankResourceAbility.clear();
        productionAbility.clear();
    }
}
