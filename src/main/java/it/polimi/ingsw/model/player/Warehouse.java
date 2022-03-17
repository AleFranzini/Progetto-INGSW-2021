package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.model.commons.Resource;

import java.util.ArrayList;

/**
 * Class Warehouse
 *
 * @author Franzini Alessandro
 * @see Store
 */
public class Warehouse implements Store {
    private final Resource[] warehouse = new Resource[3];

    public Warehouse() {
        for (int i = 0; i < 3; i++)
            warehouse[i] = new Resource(0, null);
    }

    /**
     * define if the array in input respect the warehouse rules
     *
     * @param resources is the resource to store
     * @return true if the array respect the rules,
     * false otherwise
     */
    public boolean manageWarehouse(Resource[] resources) {
        if (resources.length != warehouse.length)
            return false;
        int count = 0;
        for (int i = 0; i < 3; i++)
            if (resources[i] == null) {
                resources[i] = new Resource(0, null);
                count++;
            }
        if (count == resources.length)
            return true;

        int check = 0;
        for (int i = 0; i < resources.length - 1; i++)
            if ((resources[i].getQuantity() == 0 || resources[i].getResourceType() != resources[i + 1].getResourceType())
                    && resources[i].getQuantity() <= i + 1)
                check++;

        return check == resources.length - 1 && (resources[resources.length - 1].getQuantity() == 0
                || resources[resources.length - 1].getResourceType() != resources[0].getResourceType())
                && resources[resources.length - 1].getQuantity() <= resources.length;
    }

    /**
     * allows the management of the warehouse by changing
     * the position of the resources in the depots if possible
     *
     * @param depot1 is the first depot to control
     * @param depot2 is the second depot to control
     * @return true after changing the position of the resources,
     * false if you can't change the position
     */
    public boolean changeDepot(int depot1, int depot2) {
        if (depot1 < 0 || depot2 < 0 || depot1 > 2 || depot2 > 2)
            return false;

        if (warehouse[depot1] == null && warehouse[depot2] == null || depot1 == depot2)
            return true;
        if (warehouse[depot1].getQuantity() <= depot2 + 1 && warehouse[depot2].getQuantity() <= depot1 + 1) {
            Resource tmp = new Resource(warehouse[depot1].getQuantity(), warehouse[depot1].getResourceType());
            warehouse[depot1] = warehouse[depot2];
            warehouse[depot2] = tmp;
            return true;
        }
        return false;
    }

    @Override
    public void addResources(Resource[] resources) throws IllegalInputException {
        Warehouse newWarehouse = new Warehouse();
        if (!newWarehouse.manageWarehouse(resources)) throw new IllegalInputException();

        for (int i = 0; i < warehouse.length; i++) {
            warehouse[i].setQuantity(resources[i].getQuantity());
            warehouse[i].setResourceType(resources[i].getResourceType());
        }
    }

    @Override
    public Resource[] giveResources(Resource[] resources, boolean remove) {
        if (resources == null)
            return null;
        int count = 0;
        for (int i = 0; i < resources.length; i++)
            if (resources[i] == null) {
                resources[i] = new Resource(0, null);
                count++;
            }

        if (count == resources.length)
            return null;

        ArrayList<Resource> list = new ArrayList<>();
        for (Resource resource : resources) {
            boolean check = false;
            for (Resource value : warehouse) {
                if (value.getResourceType() == null || resource.getResourceType() == null)
                    continue;
                if (resource.getResourceType() == value.getResourceType()) {
                    int diff = value.getQuantity() - resource.getQuantity();
                    if (diff > 0 && remove)
                        value.setQuantity(diff);
                    else {
                        if (diff < 0)
                            list.add(new Resource(-diff, resource.getResourceType()));
                        if (remove) {
                            value.setQuantity(0);
                            value.setResourceType(null);
                        }
                    }
                    check = true;
                }
            }
            if (!check && resource.getResourceType() != null)
                list.add(new Resource(resource.getQuantity(), resource.getResourceType()));
        }
        if (list.size() == 0)
            return null;
        Resource[] remaining = new Resource[list.size()];
        remaining = list.toArray(remaining);
        return Resource.sortResources(remaining);
    }

    @Override
    public Resource[] peekAllResources() {
        return Resource.sortResources(warehouse);
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Resource resource : warehouse) {
            if (resource == null || resource.getQuantity() == 0)
                out.append(" -  ");
            else
                out.append(resource.getQuantity()).append(" ").append(resource.getResourceType()).append(" ");
            out.append("\n");
        }
        return out.toString();
    }

}
