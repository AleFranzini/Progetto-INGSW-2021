package it.polimi.ingsw.view.cli;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Class MarketCLI is used to memorize the Market structure in local.
 *
 * @author Franzini Alessandro
 */
public class MarketCLI {
    private static String resourceSlide;
    private static String[][] market = new String[3][4];

    /**
     * setMarketCLI creates a structure to memorize the market in local.
     * It is called at the beginning of the match and every time market is updated.
     *
     * @param json is the json String sent as a message by the EventController when it initialize the game or update the market.
     */
    public void setMarketCLI(String json) {
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray marketTray = object.getJSONArray("marketTray");
        JSONObject resource = object.getJSONObject("resourceSlide");

        resourceSlide = ColorCLI.setColorMarble(resource.getString("resourceType"));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                market[i][j] = ColorCLI.setColorMarble(marketTray.getJSONArray(i).getJSONObject(j).getString("resourceType"));
    }

    /**
     * Print market
     *
     * @return string of market.
     */
    public String printMarket() {
        StringBuilder out = new StringBuilder("       ┏━━━━━━━━━━ MARKET ━━━━━━━━━━┓\n");
        out.append("       ┃  Resource in the slide: ").append(resourceSlide).append("  ┃\n       ┃ ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++)
                out.append("    ").append(market[i][j]);
            out.append("   ←   ┃\n       ┃ ");
        }
        out.append("    ↑".repeat(4)).append("       ┃\n");
        out.append("       ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n");
        out.append("       ┃     ").append(ColorCLI.setColorMarble("STONE")).append(" = ").append(ColorCLI.setColorResource("STONE")).append("  ");
        out.append(ColorCLI.setColorMarble("SERVANT")).append(" = ").append(ColorCLI.setColorResource("SERVANT")).append("  ");
        out.append(ColorCLI.setColorMarble("FAITH")).append(" = ").append(ColorCLI.setColorResource("FAITH")).append("    ┃\n");
        out.append("       ┃     ").append(ColorCLI.setColorMarble("SHIELD")).append(" = ").append(ColorCLI.setColorResource("SHIELD")).append("  ");
        out.append(ColorCLI.setColorMarble("COIN")).append(" = ").append(ColorCLI.setColorResource("COIN")).append("  ");
        out.append(ColorCLI.setColorMarble("")).append(" = ").append("0").append("    ┃\n");
        out.append("       ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
        return out.toString();
    }
}
