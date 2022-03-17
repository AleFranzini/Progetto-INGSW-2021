package it.polimi.ingsw.model.commons;

import java.util.Objects;

/**
 * Class Resource
 *
 * @author Grugni Federico
 */
public class Resource {
    private ResourceType resourceType;
    private int quantity;

    /**
     * Constructor
     *
     * @param type     of resource
     * @param quantity of resource
     */
    public Resource(int quantity, ResourceType type) {
        this.quantity = quantity;
        this.resourceType = type;
    }

    /**
     * Check if array of resources is empty
     *
     * @param resources array
     * @return null if array is empty
     * resources if array is not empty
     */
    public static Resource[] isEmptyArrayResource(Resource[] resources) {
        if (resources != null)
            for (Resource resource : resources)
                if (resource != null && resource.getQuantity() != 0)
                    return resources;
        return null;
    }

    /**
     * Create resources creates an empty ordered array of the 4 resources.
     *
     * @return the array of resources.
     */
    public static Resource[] createResources() {
        Resource[] array = new Resource[4];
        array[0] = new Resource(0, ResourceType.COIN);
        array[1] = new Resource(0, ResourceType.SERVANT);
        array[2] = new Resource(0, ResourceType.SHIELD);
        array[3] = new Resource(0, ResourceType.STONE);
        return array;
    }

    /**
     * Sort resources in a Resource array with its quantity and remove blank resource
     * The order is: COIN, SERVANT, SHIELD, STONE.
     * The resources array can be of a shorter size and still the returned array will be of size 4.
     * If the resources array is null, will be returned null.
     *
     * @param resources input array with the resources to sort
     * @return the sorted array or null if the input is null
     */
    public static Resource[] sortResources(Resource[] resources) {
        Resource[] sortedArray = Resource.createResources();

        if (isEmptyArrayResource(resources) != null) {
            for (Resource resource : resources) {
                if (resource == null || resource.getResourceType() == null)
                    continue;
                switch (resource.getResourceType()) {
                    case COIN:
                        sortedArray[0].incQuantity(resource.getQuantity());
                        break;
                    case SERVANT:
                        sortedArray[1].incQuantity(resource.getQuantity());
                        break;
                    case SHIELD:
                        sortedArray[2].incQuantity(resource.getQuantity());
                        break;
                    case STONE:
                        sortedArray[3].incQuantity(resource.getQuantity());
                        break;
                    default:
                        break;
                }
            }
            return sortedArray;
        }
        return null;
    }

    /**
     * Sums two resources arrays passed as inputs, if one is null returns the other.
     * If both null, returns null.
     * In any case, the returned array will be sorted.
     *
     * @param resources1 first array to be summed
     * @param resources2 second array to be summed
     * @return the sum between resources1 and resources2
     */
    public static Resource[] sumResources(Resource[] resources1, Resource[] resources2) {
        // could maybe be necessary to pass the arrays through sortResources
        Resource[] sum = new Resource[4];
        resources1 = sortResources(resources1);
        resources2 = sortResources(resources2);
        if (resources1 == null)
            return resources2;
        if (resources2 == null)
            return resources1;

        sum[0] = new Resource(resources1[0].getQuantity() + resources2[0].getQuantity(), ResourceType.COIN);
        sum[1] = new Resource(resources1[1].getQuantity() + resources2[1].getQuantity(), ResourceType.SERVANT);
        sum[2] = new Resource(resources1[2].getQuantity() + resources2[2].getQuantity(), ResourceType.SHIELD);
        sum[3] = new Resource(resources1[3].getQuantity() + resources2[3].getQuantity(), ResourceType.STONE);
        return sum;
    }

    /**
     * Count the number of faith resources in the input array.
     *
     * @param resources is the array to be checked
     * @return the number of faith resources
     */
    public static int totalFaithResources(Resource[] resources) {
        for (Resource resource : resources) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                return resource.getQuantity();
            }
        }
        return 0;
    }

    /**
     * Get Resource Type
     *
     * @return type of resource
     */
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    /**
     * Set Type Resource
     *
     * @param type type of resource
     */
    public void setResourceType(ResourceType type) {
        this.resourceType = type;
    }

    /**
     * Get Quantity
     *
     * @return quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Set Quantity
     *
     * @param n number of resource
     */
    public void setQuantity(int n) {
        this.quantity = n;
    }

    /**
     * Increase quantity by param value
     *
     * @param quantity to add
     */
    public void incQuantity(int quantity) {
        this.quantity += quantity;
    }

    /**
     * Decrease quantity by param value
     *
     * @param quantity to subtract
     */
    public void decQuantity(int quantity) {
        this.quantity -= quantity;
    }

    /**
     * return index of sorted array
     *
     * @return index of sorted array
     */
    public int getTypeResource() {
        int type = switch (this.getResourceType()) {
            case COIN -> 0;
            case SERVANT -> 1;
            case SHIELD -> 2;
            case STONE -> 3;
            default -> 0;
        };
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return quantity == resource.quantity && resourceType == resource.resourceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, quantity);
    }

    @Override
    public String toString() {
        return this.getQuantity() + " " + this.getResourceType();
    }


}
