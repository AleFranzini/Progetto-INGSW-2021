package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    Warehouse warehouse = new Warehouse();
    boolean thrown = false;

    @Test
    void testManageWarehouse() {
        Resource[] res = new Resource[4];
        assertFalse(warehouse.manageWarehouse(res));
        Resource[] resource = new Resource[3];
        assertTrue(warehouse.manageWarehouse(resource));
        resource[0] = new Resource(1, ResourceType.COIN);
        assertTrue(warehouse.manageWarehouse(resource));
        resource[0].setQuantity(2);
        assertFalse(warehouse.manageWarehouse(resource));
        resource[0].setQuantity(3);
        assertFalse(warehouse.manageWarehouse(resource));
        resource[0].setQuantity(4);
        assertFalse(warehouse.manageWarehouse(resource));
        resource[0].setQuantity(1);
        resource[1] = new Resource(3, ResourceType.STONE);
        assertFalse(warehouse.manageWarehouse(resource));
        resource[1].setQuantity(2);
        assertTrue(warehouse.manageWarehouse(resource));
        resource[2] = new Resource(5, ResourceType.SHIELD);
        assertFalse(warehouse.manageWarehouse(resource));
        resource[2].setQuantity(2);
        assertTrue(warehouse.manageWarehouse(resource));
        resource[2].setResourceType(ResourceType.STONE);
        assertFalse(warehouse.manageWarehouse(resource));
    }

    @Test
    void testChangeDepot() {
        Resource[] resources = new Resource[3];
        resources[0] = new Resource(1, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);
        assertTrue(warehouse.changeDepot(0, 0));
        assertTrue(warehouse.changeDepot(0, 1));
        assertTrue(warehouse.changeDepot(0, 2));
        assertFalse(warehouse.changeDepot(0, 3));
        assertTrue(warehouse.changeDepot(1, 0));

        resources[0].setQuantity(2);
        warehouse.changeDepot(0, 1);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        warehouse.changeDepot(1, 2);
        resources[0] = null;
        resources[2] = new Resource(3, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);

        assertTrue(warehouse.changeDepot(0, 1));
        assertTrue(warehouse.changeDepot(1, 1));
        assertFalse(warehouse.changeDepot(1, 2));

        Resource[] w = warehouse.peekAllResources();
        assertTrue(w[0].getQuantity() == resources[2].getQuantity()
                && w[0].getResourceType() == resources[2].getResourceType());

        assertTrue(warehouse.changeDepot(0, 1));
        assertFalse(warehouse.changeDepot(0, 2));
        assertFalse(warehouse.changeDepot(1, 2));
    }

    @Test
    void testAddResources() {
        Resource[] resources = new Resource[3];
        resources[0] = new Resource(1, ResourceType.SHIELD);
        resources[1] = new Resource(4, ResourceType.SERVANT);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        resources[0] = new Resource(1, ResourceType.SHIELD);
        resources[1] = new Resource(2, ResourceType.SERVANT);
        resources[2] = new Resource(4, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        resources[0] = new Resource(4, ResourceType.SHIELD);
        resources[1] = new Resource(1, ResourceType.SERVANT);
        resources[2] = new Resource(1, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        resources[0] = new Resource(1, ResourceType.SHIELD);
        resources[1] = new Resource(1, ResourceType.SERVANT);
        resources[2] = new Resource(1, ResourceType.SHIELD);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        resources[0] = new Resource(1, ResourceType.SHIELD);
        resources[1] = new Resource(1, ResourceType.SHIELD);
        resources[2] = new Resource(1, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        resources[0] = new Resource(1, ResourceType.STONE);
        resources[1] = new Resource(1, ResourceType.SHIELD);
        resources[2] = new Resource(1, ResourceType.COIN);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);

        resources[0] = new Resource(1, ResourceType.STONE);
        resources[1] = new Resource(1, ResourceType.COIN);
        resources[2] = null;
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);

        Resource[] w = warehouse.peekAllResources();
        Resource[] expectedOutput = new Resource[4];
        expectedOutput[0] = resources[1];
        expectedOutput[1] = new Resource(0, ResourceType.SERVANT);
        expectedOutput[2] = new Resource(0,ResourceType.SHIELD);
        expectedOutput[3] = resources[0];
        for (int i = 0; i < w.length; i++)
            assertTrue(expectedOutput[i].getQuantity() == w[i].getQuantity()
                    && expectedOutput[i].getResourceType() == w[i].getResourceType());
    }

    @Test
    void testGiveResources() {
        Resource[] resources = new Resource[3];
        assertNull(warehouse.giveResources(resources, true));
        resources[0] = new Resource(1, ResourceType.COIN);
        Resource[] expected = new Resource[3];
        expected[0] = resources[0];

        Resource[] w = warehouse.giveResources(resources, true);
        assertTrue(expected[0].getResourceType() == w[0].getResourceType()
                && expected[0].getQuantity() == w[0].getQuantity());

        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);
        assertNull(warehouse.giveResources(resources, true));

        Resource[] resources1 = new Resource[3];
        resources1[0] = new Resource(1, ResourceType.COIN);
        resources1[1] = new Resource(1, ResourceType.SHIELD);
        resources1[2] = new Resource(2, ResourceType.STONE);
        try {
            warehouse.addResources(resources1);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);
        Resource[] tmp = new Resource[3];
        tmp[0] = resources1[1];
        assertNull(warehouse.giveResources(tmp, false));

        resources1[1] = new Resource(1, ResourceType.SERVANT);
        resources1[2] = new Resource(2, ResourceType.SHIELD);
        assertNotNull(warehouse.giveResources(resources1, false));

        resources[0] = new Resource(1, ResourceType.STONE);
        assertNull(warehouse.giveResources(resources, true));

        resources[0] = new Resource(1, ResourceType.COIN);
        resources[1] = new Resource(2, ResourceType.STONE);
        resources[2] = new Resource(1, ResourceType.SERVANT);
        try {
            warehouse.addResources(resources);
        } catch (IllegalInputException e) {
            thrown = true;
        }
        assertFalse(thrown);

        resources[1].setQuantity(1);
        resources[2].setQuantity(2);
        Resource[] out = warehouse.giveResources(resources, true);
        System.out.println(warehouse.toString());
        for (Resource resource : out)
            System.out.println("Risorsa rimanente: " + resource.getQuantity() + resource.getResourceType());
    }

}