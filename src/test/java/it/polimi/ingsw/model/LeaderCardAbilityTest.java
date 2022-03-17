package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyProductionListException;
import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.exceptions.UndefinedLeaderCardAbilityException;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCardAbility;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.SizeLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LeaderCardAbilityTest {
    private LeaderCardAbility leaderCardAbility;

    @BeforeEach
    public void testSetup() {
        leaderCardAbility = new LeaderCardAbility();
    }

    @Test
    public void testAddDiscount() {
        assertDoesNotThrow(() -> leaderCardAbility.addDiscount(ResourceType.COIN));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDiscount(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addDiscount(ResourceType.SERVANT));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDiscount(ResourceType.SHIELD));
    }

    @Test
    public void testApplyDiscountAbility() {
        Resource[] testResource = null;
        Resource[] resources = new Resource[]{
                new Resource(2, ResourceType.COIN),
                new Resource(1, ResourceType.STONE)
        };
        assertDoesNotThrow(() -> leaderCardAbility.addDiscount(ResourceType.COIN));
        assertNull(leaderCardAbility.applyDiscountAbility(testResource));
        assertNotNull(resources = leaderCardAbility.applyDiscountAbility(resources));
        assertEquals(1, resources[0].getQuantity());
        assertSame(resources[0].getResourceType(), ResourceType.COIN);
        assertDoesNotThrow(() -> leaderCardAbility.addDiscount(ResourceType.STONE));
        resources = leaderCardAbility.applyDiscountAbility(resources);
        assertNull(resources);
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDiscount(ResourceType.SHIELD));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDiscount(ResourceType.STONE));
    }

    @Test
    public void testAddProductionAbility() {
        assertDoesNotThrow(() -> leaderCardAbility.addProductionAbility(ResourceType.COIN));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addProductionAbility(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addProductionAbility(ResourceType.SERVANT));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addProductionAbility(ResourceType.SHIELD));
    }

    @Test
    public void testGetProductionAbility() {
        //test with index or not
    }

    @Test
    public void testResetElementUsability() {
        assertDoesNotThrow(() -> leaderCardAbility.addProductionAbility(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addProductionAbility(ResourceType.SHIELD));
        assertDoesNotThrow(() -> leaderCardAbility.getProductionAbilityResourceIn(0));
        assertDoesNotThrow(() -> leaderCardAbility.getProductionAbilityResourceIn(1));
        assertThrows(EmptyProductionListException.class, () -> leaderCardAbility.getProductionAbilityResourceIn(0));
        assertThrows(EmptyProductionListException.class, () -> leaderCardAbility.getProductionAbilityResourceIn(1));
        leaderCardAbility.resetElementsUsability();
        assertDoesNotThrow(() -> leaderCardAbility.getProductionAbilityResourceIn(0));
        assertDoesNotThrow(() -> leaderCardAbility.getProductionAbilityResourceIn(1));
    }

    @Test
    public void testAddBlankResource() {
        assertDoesNotThrow(() -> leaderCardAbility.addBlankResource(ResourceType.COIN));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addBlankResource(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addBlankResource(ResourceType.SERVANT));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addBlankResource(ResourceType.SHIELD));
    }

    @Test
    public void testConvertBlankResource() {
        Resource[] resources = new Resource[]{new Resource(1, ResourceType.COIN),
                new Resource(1, ResourceType.BLANK),
                new Resource(1, ResourceType.BLANK)
        };
        Resource[] resource;
        try {
            resource = leaderCardAbility.convertBlankResource(resources);
            for (Resource resource1 : resource)
                assertNotSame(resource1.getResourceType(), ResourceType.BLANK);
            leaderCardAbility.addBlankResource(ResourceType.COIN);
            resource = leaderCardAbility.convertBlankResource(resources);
            assertEquals(3, resource[0].getQuantity());
            leaderCardAbility.addBlankResource(ResourceType.STONE);
            assertThrows(UndefinedLeaderCardAbilityException.class, () -> leaderCardAbility.convertBlankResource(resources));
            assert true;
        } catch (UndefinedLeaderCardAbilityException | SizeLimitExceededException e) {
            assert false;
        }
    }

    @Test
    public void testGetBlankResourcesType() {
        assertDoesNotThrow(() -> leaderCardAbility.addBlankResource(ResourceType.COIN));
        ResourceType[] resourceTypes = leaderCardAbility.getBlankResourcesType();
        assertSame(resourceTypes[0], ResourceType.COIN);
        assertDoesNotThrow(() -> leaderCardAbility.addBlankResource(ResourceType.STONE));
        resourceTypes = leaderCardAbility.getBlankResourcesType();
        assertTrue(resourceTypes[0] == ResourceType.COIN && resourceTypes[1] == ResourceType.STONE);
    }

    @Test
    public void testAddDepot() {
        assertDoesNotThrow(() -> leaderCardAbility.addDepot(ResourceType.COIN));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDepot(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addDepot(ResourceType.SERVANT));
        assertThrows(SizeLimitExceededException.class, () -> leaderCardAbility.addDepot(ResourceType.SHIELD));
    }

    @Test
    public void testAddResources() {
        Resource[] resources;
        try {
            leaderCardAbility.addDepot(ResourceType.COIN);
            leaderCardAbility.addResources(new Resource[]{
                    new Resource(1, ResourceType.COIN)
            });
            resources = leaderCardAbility.peekAllResources();
            assertTrue(resources[0].getQuantity() == 1 &&
                    resources[0].getResourceType() == ResourceType.COIN);

            leaderCardAbility.addDepot(ResourceType.STONE);
            leaderCardAbility.addResources(new Resource[]{
                    new Resource(2, ResourceType.STONE)
            });
            resources = leaderCardAbility.peekAllResources();
            assertTrue(resources[1].getQuantity() == 2 &&
                    resources[1].getResourceType() == ResourceType.STONE);
            assert true;
        } catch (SizeLimitExceededException | IllegalInputException e) {
            assert false;
        }
        resources = leaderCardAbility.peekAllResources();
        assertNotNull(resources[0]);
        assertNotNull(resources[1]);
        assertTrue(resources[0].getQuantity() == 1 &&
                resources[0].getResourceType() == ResourceType.COIN &&
                resources[1].getQuantity() == 2 &&
                resources[1].getResourceType() == ResourceType.STONE
        );
    }

    @Test
    public void testAddResourcesButIsFull() {
        Resource[] resources;
        assertDoesNotThrow(() -> leaderCardAbility.addDepot(ResourceType.COIN));
        assertDoesNotThrow(() -> leaderCardAbility.addResources(new Resource[]{
                new Resource(1, ResourceType.COIN)
        }));
        resources = leaderCardAbility.peekAllResources();
        assertTrue(resources[0].getQuantity() == 1 &&
                resources[0].getResourceType() == ResourceType.COIN);
        // try add too resource
        try {
            leaderCardAbility.addResources(new Resource[]{
                    new Resource(3, ResourceType.COIN)
            });
            assert false;
        } catch (IllegalInputException e) {
            resources = e.takeRemainingResource();
            assertEquals(2, resources[0].getQuantity());
            assert true;
        }
    }

    @Test
    public void testGiveResources() {
        Resource[] resources;
        try {
            leaderCardAbility.addDepot(ResourceType.COIN);
            leaderCardAbility.addDepot(ResourceType.STONE);
            Resource[] test = new Resource[]{
                    new Resource(1, ResourceType.COIN),
                    new Resource(2, ResourceType.STONE)
            };
            leaderCardAbility.addResources(test);
            //search when all resource there is in depot
            resources = leaderCardAbility.giveResources(test, false);
            assertNull(resources);
            //take resource when resource aren't all in depot
            resources = leaderCardAbility.giveResources(new Resource[]{
                    new Resource(2, ResourceType.COIN)
            }, true);
            assertTrue(resources[0].getQuantity() == 1 && resources[0].getResourceType() == ResourceType.COIN);
            resources = leaderCardAbility.peekAllResources();
            assertEquals(0, resources[0].getQuantity());
            //take resources when all there are and remove is true
            resources = leaderCardAbility.giveResources(new Resource[]{new Resource(2, ResourceType.STONE)}, true);
            assertNull(resources);
            resources = leaderCardAbility.peekAllResources();
            assertNull(resources);

            test = new Resource[]{
                    new Resource(1, ResourceType.COIN),
                    new Resource(2, ResourceType.STONE)
            };
            leaderCardAbility.addResources(test);
            assert true;
        } catch (SizeLimitExceededException | IllegalInputException e) {
            assert false;
        }
        //take resource from index when all resource isn't in depot and remove is false
        Resource resource = leaderCardAbility.giveResources(new Resource(2, ResourceType.COIN), false, 0);
        assertNotNull(resource);
        assertTrue(resource.getQuantity() == 1 && resource.getResourceType() == ResourceType.COIN);
        //take resource when all there is in depot and remove is true
        resource = leaderCardAbility.giveResources(new Resource(1, ResourceType.COIN), true, 0);
        assertNull(resource);
        resources = leaderCardAbility.peekAllResources();
        assertEquals(0, resources[0].getQuantity());
    }

    @Test
    public void testPeekAllResources() {
        Resource[] resources;
        try {
            leaderCardAbility.addDepot(ResourceType.COIN);
            Resource[] test = new Resource[]{
                    new Resource(1, ResourceType.COIN)
            };
            leaderCardAbility.addResources(test);
            resources = leaderCardAbility.peekAllResources();
            assertTrue(resources[0].getQuantity() == 1 &&
                    resources[0].getResourceType() == ResourceType.COIN
            );

            leaderCardAbility.addDepot(ResourceType.STONE);
            test = new Resource[]{
                    new Resource(1, ResourceType.COIN),
                    new Resource(2, ResourceType.STONE)
            };
            leaderCardAbility.addResources(test);
            assert true;
        } catch (SizeLimitExceededException | IllegalInputException e) {
            assert false;
        }
        resources = leaderCardAbility.peekAllResources();
        assertTrue(resources[0].getQuantity() == 2 &&
                resources[0].getResourceType() == ResourceType.COIN &&
                resources[1].getQuantity() == 2 &&
                resources[1].getResourceType() == ResourceType.STONE
        );

        Resource resource = leaderCardAbility.peekAllResources(0);
        assertTrue(resource.getQuantity() == 2 &&
                resource.getResourceType() == ResourceType.COIN
        );
        resource = leaderCardAbility.peekAllResources(1);
        assertTrue(resource.getQuantity() == 2 &&
                resource.getResourceType() == ResourceType.STONE
        );
    }
}