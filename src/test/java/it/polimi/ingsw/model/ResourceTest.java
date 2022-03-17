package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class ResourceTest {

    @Test
    void testSortResources() {
        Resource[] arrayActual = new Resource[4];
        arrayActual[0] = new Resource(1, ResourceType.COIN);
        arrayActual[1] = new Resource(2, ResourceType.SERVANT);
        arrayActual[2] = null;
        arrayActual[3] = new Resource(2, ResourceType.COIN);
        Resource[] arrayExpected = new Resource[4];
        arrayExpected[0] = new Resource(3, ResourceType.COIN);
        arrayExpected[1] = new Resource(2, ResourceType.SERVANT);
        arrayExpected[2] = new Resource(0, ResourceType.SHIELD);
        arrayExpected[3] = new Resource(0, ResourceType.STONE);
        for (int i = 0; i < 4; i++) {
            assertEquals(arrayExpected[i].getResourceType(), Objects.requireNonNull(Resource.sortResources(arrayActual))[i].getResourceType());
            assertEquals(arrayExpected[i].getQuantity(), Objects.requireNonNull(Resource.sortResources(arrayActual))[i].getQuantity());
        }
    }

    @Test
    void testSortResourcesWhenEmptyArray() {
        Resource[] arrayActual = new Resource[4];
        arrayActual[0] = new Resource(0, ResourceType.COIN);
        arrayActual[1] = new Resource(0, ResourceType.SERVANT);
        arrayActual[2] = new Resource(0, ResourceType.SHIELD);
        arrayActual[3] = new Resource(0, ResourceType.COIN);
        assertNull(Resource.sortResources(arrayActual));
    }

    @Test
    void testSortResourcesWhenArrayShorter() {
        Resource[] arrayExpected = new Resource[4];
        arrayExpected[0] = new Resource(5, ResourceType.COIN);
        arrayExpected[1] = new Resource(0, ResourceType.SERVANT);
        arrayExpected[2] = new Resource(1, ResourceType.SHIELD);
        arrayExpected[3] = new Resource(0, ResourceType.STONE);

        Resource[] arrayActual = new Resource[3];
        arrayActual[0] = new Resource(5, ResourceType.COIN);
        arrayActual[1] = new Resource(0, ResourceType.SERVANT);
        arrayActual[2] = new Resource(1, ResourceType.SHIELD);

        assertArrayEquals(arrayExpected, Resource.sortResources(arrayActual));
    }

    @Test
    void testSumResources() {
        Resource[] resources1 = new Resource[4];
        resources1[0] = new Resource(0, ResourceType.COIN);
        resources1[1] = new Resource(2, ResourceType.SERVANT);
        resources1[2] = null;
        resources1[3] = new Resource(5, ResourceType.COIN);

        Resource[] resources2 = new Resource[4];
        resources2[0] = new Resource(1, ResourceType.COIN);
        resources2[1] = new Resource(2, ResourceType.SERVANT);
        resources2[2] = new Resource(4, ResourceType.SHIELD);
        resources2[3] = new Resource(2, ResourceType.STONE);

        Resource[] resources3 = new Resource[3];
        resources3[0] = new Resource(3, ResourceType.COIN);
        resources3[1] = new Resource(0, ResourceType.SERVANT);
        resources3[2] = new Resource(1, ResourceType.SHIELD);

        Resource[] resources4 = null;

        Resource[] expected1 = new Resource[4];
        expected1[0] = new Resource(6, ResourceType.COIN);
        expected1[1] = new Resource(4, ResourceType.SERVANT);
        expected1[2] = new Resource(4, ResourceType.SHIELD);
        expected1[3] = new Resource(2, ResourceType.STONE);

        Resource[] expected2 = new Resource[4];
        expected2[0] = new Resource(3, ResourceType.COIN);
        expected2[1] = new Resource(0, ResourceType.SERVANT);
        expected2[2] = new Resource(1, ResourceType.SHIELD);
        expected2[3] = new Resource(0, ResourceType.STONE);

        assertArrayEquals(expected1, Resource.sumResources(resources1, resources2));
        assertArrayEquals(expected2, Resource.sumResources(resources3, resources4));

    }

    @Test
    void testSoStringTest() {
        Resource[] printedArray = new Resource[4];
        printedArray[0] = new Resource(0, ResourceType.COIN);
        printedArray[1] = new Resource(0, ResourceType.SERVANT);
        printedArray[2] = new Resource(0, ResourceType.SHIELD);
        printedArray[3] = new Resource(0, ResourceType.STONE);

        String out = "";
        for (int i = 0; i < 4; i++) {
            if (printedArray[i].getQuantity() != 0) {
                out += printedArray[i].toString();
                out += ", ";
            }
        }
        if (out.equals("")) {
            System.out.println("No resources");
        } else {
            System.out.println(out.substring(0, out.length() - 2));
        }

        Resource[] printedArray1 = new Resource[4];
        printedArray1[0] = new Resource(0, ResourceType.COIN);
        printedArray1[1] = new Resource(5, ResourceType.SERVANT);
        printedArray1[2] = new Resource(0, ResourceType.SHIELD);
        printedArray1[3] = new Resource(1, ResourceType.STONE);

        String out1 = "";
        for (int i = 0; i < 4; i++) {
            if (printedArray1[i].getQuantity() != 0) {
                out1 += printedArray1[i].toString();
                out1 += ", ";
            }
        }
        if (out1.equals("")) {
            System.out.println("No resources");
        } else {
            System.out.println(out1.substring(0, out1.length() - 2));
        }

        Resource[] printedArray2 = new Resource[4];
        printedArray2[0] = new Resource(3, ResourceType.COIN);
        printedArray2[1] = new Resource(5, ResourceType.SERVANT);
        printedArray2[2] = new Resource(4, ResourceType.SHIELD);
        printedArray2[3] = new Resource(1, ResourceType.STONE);

        String out2 = "";
        for (int i = 0; i < 4; i++) {
            if (printedArray2[i].getQuantity() != 0) {
                out2 += printedArray2[i].toString();
                out2 += ", ";
            }
        }
        if (out2.equals("")) {
            System.out.println("No resources");
        } else {
            System.out.println(out2.substring(0, out2.length() - 2));
        }


    }
}