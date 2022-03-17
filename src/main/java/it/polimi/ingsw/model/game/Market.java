package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import it.polimi.ingsw.model.commons.Resource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class Market
 *
 * @author Galetti Filippo
 */
public class Market {
    private final Resource[][] marketTray = new Resource[3][4];
    private Resource resourceSlide;

    /**
     * The constructor initialize the market by importing a json file
     */
    public Market() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/Market.json"));
        Resource[] resources = gson.fromJson(reader, Resource[].class);
        List<Resource> resourceList = new LinkedList<>(Arrays.asList(resources));
        Collections.shuffle(resourceList);
        resourceSlide = resourceList.remove(12);
        for (int i = 0, j = 0; i < 9; i += 4) {
            marketTray[j] = resourceList.subList(i, i + 4).toArray(new Resource[0]);
            j++;
        }
    }

    /**
     * Get the market resources from the chosen row
     *
     * @param row that player has chosen
     * @return an array of resources which contains the resources from the chosen row
     */
    public Resource[] getRow(int row) {
        Resource[] rowResources = new Resource[4];
        System.arraycopy(marketTray[row], 0, rowResources, 0, 4);
        Resource nextResourceSlide = marketTray[row][0];
        System.arraycopy(marketTray[row], 1, marketTray[row], 0, 3);
        marketTray[row][3] = resourceSlide;
        resourceSlide = nextResourceSlide;
        return rowResources;
    }

    /**
     * Get the market resources from the chosen column
     *
     * @param column that player has chosen
     * @return an array of resources which contains the resources from the chosen column
     */
    public Resource[] getColumn(int column) {
        Resource[] columnResources = new Resource[3];
        Resource nextResourceSlide = marketTray[0][column];
        for (int i = 0; i < 3; i++) {
            columnResources[i] = marketTray[i][column];
        }
        for (int i = 0; i < 2; i++) {
            marketTray[i][column] = marketTray[i + 1][column];
        }
        marketTray[2][column] = resourceSlide;
        resourceSlide = nextResourceSlide;
        return columnResources;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++)
                out.append(marketTray[i][j].getResourceType()).append("    ");
            out.append("\n");
        }
        out.append("Remaining Marble: ").append(resourceSlide.getResourceType()).append("\n");
        return out.toString();
    }
}
