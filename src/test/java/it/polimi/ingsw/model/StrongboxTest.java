package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class StrongboxTest {
    private Strongbox strongbox;

    @BeforeEach
    public void testSetup() {
        this.strongbox = new Strongbox(3, 2, 1, 1);
    }

    @Test
    public void testGetTypeResourceTest() {
    }

    @Test
    public void testAddResourcesAndPeekAllResources() {
        boolean check = false;
        Resource[] resources = new Resource[5];
        resources[0] = new Resource(1, ResourceType.COIN);
        resources[1] = new Resource(1, ResourceType.STONE);
        resources[2] = new Resource(2, ResourceType.COIN);
        resources[3] = new Resource(1, ResourceType.SHIELD);
        resources[4] = new Resource(2, ResourceType.SERVANT);

        Resource[] test = new Resource[4];
        test[0] = new Resource(6, ResourceType.COIN);
        test[1] = new Resource(4, ResourceType.SERVANT);
        test[2] = new Resource(2, ResourceType.SHIELD);
        test[3] = new Resource(2, ResourceType.STONE);

        strongbox.addResources(resources);

        Resource[] test1 = new Resource[4];
        System.arraycopy(strongbox.peekAllResources(), 0, test1, 0, 4);
        for (int i = 0; i < 4; i++)
            if (test[i].getResourceType() != test1[i].getResourceType() || test[i].getQuantity() != test1[i].getQuantity()) {
                check = true;
                break;
            }
        assertFalse(check);
    }

    @Test
    public void testGiveResourcesTrue() {
        boolean check = false;
        strongbox.giveResources(new Resource[]{new Resource(1, ResourceType.COIN),
                new Resource(1, ResourceType.STONE),
                new Resource(1, ResourceType.SERVANT),
                new Resource(1, ResourceType.SHIELD)}, true);
        Resource[] test = new Resource[4];
        test[0] = new Resource(2, ResourceType.COIN);
        test[1] = new Resource(1, ResourceType.SERVANT);
        test[2] = new Resource(0, ResourceType.SHIELD);
        test[3] = new Resource(0, ResourceType.STONE);

        Resource[] test1 = new Resource[4];
        System.arraycopy(strongbox.peekAllResources(), 0, test1, 0, 4);
        for (int i = 0; i < 4; i++)
            if (test[i].getResourceType() != test1[i].getResourceType() || test[i].getQuantity() != test1[i].getQuantity()) {
                check = true;
                break;
            }
        assertFalse(check);
    }

    @Test
    public void testGiveResourcesFalse() {
        boolean check = false;
        Resource[] test1 = strongbox.giveResources(new Resource[]{new Resource(3, ResourceType.COIN),
                new Resource(2, ResourceType.STONE),
                new Resource(2, ResourceType.SERVANT),
                new Resource(1, ResourceType.SHIELD)}, true);
        Resource[] test = new Resource[4];
        test[0] = new Resource(0, ResourceType.COIN);
        test[1] = new Resource(0, ResourceType.SERVANT);
        test[2] = new Resource(0, ResourceType.SHIELD);
        test[3] = new Resource(1, ResourceType.STONE);

        for (int i = 0; i < 4; i++)
            if (test[i].getResourceType() != test1[i].getResourceType() || test[i].getQuantity() != test1[i].getQuantity()) {
                check = true;
                break;
            }
        assertFalse(check);
    }

    @Test
    public void testUseGiveResourcesLikePeekResourcesTrue() {
        Resource[] test = new Resource[4];
        test[0] = new Resource(3, ResourceType.COIN);
        test[1] = new Resource(2, ResourceType.SERVANT);
        test[2] = new Resource(1, ResourceType.SHIELD);
        test[3] = new Resource(1, ResourceType.STONE);
        assertNull(strongbox.giveResources(test, false));
    }

    @Test
    public void testUseGiveResourcesLikePeekResourcesFalse() {
        Resource[] test = new Resource[4];
        test[0] = new Resource(4, ResourceType.COIN);
        test[1] = new Resource(3, ResourceType.SERVANT);
        test[2] = new Resource(4, ResourceType.SHIELD);
        test[3] = new Resource(3, ResourceType.STONE);
        Resource[] app = strongbox.giveResources(test, false);
        assertNotNull(app);

        app = Resource.sortResources(app);
        assertEquals(1, app[0].getQuantity());
        assertEquals(1, app[1].getQuantity());
        assertEquals(3, app[2].getQuantity());
        assertEquals(2, app[3].getQuantity());
    }

    @Test
    public void testPeekAllResources() {
        boolean check = false;
        Resource[] test = new Resource[4];
        test[0] = new Resource(3, ResourceType.COIN);
        test[1] = new Resource(2, ResourceType.SERVANT);
        test[2] = new Resource(1, ResourceType.SHIELD);
        test[3] = new Resource(1, ResourceType.STONE);

        Resource[] test1;
        test1 = strongbox.peekAllResources();
        for (int i = 0; i < 4; i++)
            if (test[i].getResourceType() != test1[i].getResourceType() || test[i].getQuantity() != test1[i].getQuantity()) {
                check = true;
                break;
            }
        assertFalse(check);
    }
}