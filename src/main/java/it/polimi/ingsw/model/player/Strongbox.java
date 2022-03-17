package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;

/**
 * Class Strongbox
 *
 * @author Grugni Federico
 */
public class Strongbox implements Store {
    private final Resource[] strongbox;

    /**
     * constructor of strongbox
     */
    public Strongbox() {
        this.strongbox = new Resource[4];
        this.strongbox[0] = new Resource(0, ResourceType.COIN);
        this.strongbox[1] = new Resource(0, ResourceType.SERVANT);
        this.strongbox[2] = new Resource(0, ResourceType.SHIELD);
        this.strongbox[3] = new Resource(0, ResourceType.STONE);
    }

    /**
     * for test and if the game is restarted
     *
     * @param n_coin    number of coin
     * @param n_servant number of servant
     * @param n_shield  number of shield
     * @param n_stone   number of stone
     */
    public Strongbox(int n_coin, int n_servant, int n_shield, int n_stone) {
        this.strongbox = new Resource[4];
        this.strongbox[0] = new Resource(n_coin, ResourceType.COIN);
        this.strongbox[1] = new Resource(n_servant, ResourceType.SERVANT);
        this.strongbox[2] = new Resource(n_shield, ResourceType.SHIELD);
        this.strongbox[3] = new Resource(n_stone, ResourceType.STONE);
    }

    @Override
    public void addResources(Resource[] resources) {
        if (resources != null && resources[0] != null) {
            for (Resource resource : resources)
                strongbox[(resource.getTypeResource())].incQuantity(resource.getQuantity());
        }
    }

    @Override
    public Resource[] giveResources(Resource[] resources, boolean remove) {
        if (resources == null)
            return null;
        Resource[] resource = new Resource[4];
        for (Resource value : resources) {
            resource[value.getTypeResource()] = new Resource(0, strongbox[value.getTypeResource()].getResourceType());
            if (value.getQuantity() <= strongbox[value.getTypeResource()].getQuantity()) {
                if (remove)
                    strongbox[value.getTypeResource()].decQuantity(value.getQuantity());
            } else
                resource[value.getTypeResource()].incQuantity(value.getQuantity() - strongbox[value.getTypeResource()].getQuantity());
        }
        return Resource.isEmptyArrayResource(Resource.sortResources(resource));
    }

    @Override
    public Resource[] peekAllResources() {
        return Resource.sortResources(strongbox);
    }

    /**
     * strong box to string
     *
     * @return string that contains number of resources
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (Resource.isEmptyArrayResource(strongbox) == null)
            return "0 COIN\n0 SERVANT\n0 SHIELD\n0 STONE";
        for (Resource resource : Resource.sortResources(strongbox))
            stringBuilder.append(resource.toString()).append("\n");
        return stringBuilder.toString();
    }
}
