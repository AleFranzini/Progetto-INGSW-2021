package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.game.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    private Market market;

    @BeforeEach
    public void testSetUp() {
        market = new Market();
    }

    @Test
    public void testGetRow() {
        for (int row = 0; row < 3; row++) {
            Resource[] firstCall = market.getRow(row);
            Resource[] secondCall = new Resource[0];
            for (int i = 0; i < 5; i++) {
                secondCall = market.getRow(row);
            }
            assertArrayEquals(firstCall, secondCall);
        }
    }

    @Test
    public void testGetColumn() {
        for (int column = 0; column < 4; column++) {
            Resource[] firstCall = market.getColumn(column);
            Resource[] secondCall = new Resource[0];
            for (int i = 0; i < 4; i++) {
                secondCall = market.getColumn(column);
            }
            assertArrayEquals(firstCall, secondCall);
        }
    }

    @Test
    void testGetResource() {
        System.out.println(market.toString());
        Resource[] firstCall = market.getRow(1);
        System.out.println(market.toString());
        market.getColumn(1);
        market.getColumn(0);
        market.getRow(0);
        market.getRow(0);
        market.getRow(2);
        market.getColumn(2);
        market.getColumn(2);
        market.getRow(1);
        market.getColumn(0);
        market.getRow(2);
        market.getColumn(2);
        market.getRow(0);
        market.getRow(0);
        market.getColumn(1);
        market.getColumn(1);
        market.getColumn(1);
        assertArrayEquals(firstCall, market.getRow(1));
    }

    @Test
    public void testSerializeMarket(){
        Gson gson = new Gson();
        String string = gson.toJson(market);

        Market market = gson.fromJson(string, Market.class);
        market.toString();
    }
}