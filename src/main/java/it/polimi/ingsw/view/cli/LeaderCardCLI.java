package it.polimi.ingsw.view.cli;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * CLass LeaderCardCLI
 */
public class LeaderCardCLI {
    JSONArray leaderCardJson;
    //private String[] leaderCardCLI = new String[16];

    /**
     * Constructor of LeaderCardCLI
     */
    public LeaderCardCLI() {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/LeaderCard.json"));
        this.leaderCardJson = (JSONArray) new JSONTokener(reader).nextValue();
    }

    /**
     * Print leader card by ID
     *
     * @param id of leader card to print.
     * @return String of leader card.
     */
    public String printLeaderCardByID(int id) {
        StringBuilder card = new StringBuilder();
        JSONObject cardJSon;
        int i = 0;

        if (id == -1) {
            card.append("     ╔═════════════╗\n")
                    .append("     ║ ╔═════════╗ ║\n")
                    .append("     ║ ║ /////// ║ ║\n")
                    .append("     ║ ║ \\\\\\\\\\\\\\ ║ ║\n")
                    .append("     ║ ║ /////// ║ ║\n")
                    .append("     ║ ║ \\\\\\\\\\\\\\ ║ ║\n")
                    .append("     ║ ╚═════════╝ ║\n")
                    .append("     ╚═════════════╝\n");
        }
        while (i < 15 && leaderCardJson.getJSONObject(i).getJSONObject("properties").getInt("id") != id) {
            i++;
        }
        cardJSon = leaderCardJson.getJSONObject(i);
        switch (cardJSon.getString("type")) {
            case "LeaderCardDiscountAbility":
                card.append("     ╔═════════════╗\n")
                        .append("     ║    1").append(ColorCLI.setColorFlag("", cardJSon.getJSONObject("properties").getJSONArray("requirements").getString(0))).append("  2").append(ColorCLI.setColorFlag("", cardJSon.getJSONObject("properties").getJSONArray("requirements").getString(1))).append("   ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║  Discount:  ║\n")
                        .append("     ║     ").append("-1").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("resourceId"))).append("     ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║    ").append("VP: ").append(cardJSon.getJSONObject("properties").getInt("victoryPoints")).append("    ║\n")
                        .append("     ╚═════════════╝\n");
                break;
            case "LeaderCardDepotAbility":
                card.append("     ╔═════════════╗\n")
                        .append("     ║      5").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("requirements"))).append("     ").append("║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║   Depot:    ║\n")
                        .append("     ║   [").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("leaderCardDepot"))).append("] [").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("leaderCardDepot"))).append("]   ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║    ").append("VP: ").append(cardJSon.getJSONObject("properties").getInt("victoryPoints")).append("    ║\n")
                        .append("     ╚═════════════╝\n");
                break;
            case "LeaderCardProductionAbility":
                card.append("     ╔═════════════╗\n")
                        .append("     ║      ").append(ColorCLI.setColorFlag("LEVEL_2", cardJSon.getJSONObject("properties").getString("colorRequirements"))).append("     ").append("║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║ Production: ║\n")
                        .append("     ║  ").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("productionResource"))).append(" } (?) ").append(ColorCLI.setColorResource("FAITH")).append("  ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║    ").append("VP: ").append(cardJSon.getJSONObject("properties").getInt("victoryPoints")).append("    ║\n")
                        .append("     ╚═════════════╝\n");
                break;
            case "LeaderCardResourceAbility":
                card.append("     ╔═════════════╗\n")
                        .append("     ║    2").append(ColorCLI.setColorFlag("", cardJSon.getJSONObject("properties").getJSONArray("colorRequirements").getString(0))).append(" 1").append(ColorCLI.setColorFlag("", cardJSon.getJSONObject("properties").getJSONArray("colorRequirements").getString(1))).append("").append("    ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║   Marble:   ║\n")
                        .append("     ║   ").append(ColorCLI.setColorMarble("BLANK")).append(" -> ").append(ColorCLI.setColorResource(cardJSon.getJSONObject("properties").getString("resourceId"))).append("    ║\n")
                        .append("     ╠═════════════╣\n")
                        .append("     ║    ").append("VP: ").append(cardJSon.getJSONObject("properties").getInt("victoryPoints")).append("    ║\n")
                        .append("     ╚═════════════╝\n");
                break;
        }
        return card.toString();
    }

    /**
     * Print leader card side by side.
     *
     * @param id         (s) of leader cards to print.
     * @param withNumber true if print number under the cards.
     *                   false otherwise.
     * @return string of leader card array.
     */
    public String getLeaderCardArray(int[] id, boolean withNumber) {
        String[][] cards = new String[id.length][];

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < id.length; i++)
            cards[i] = printLeaderCardByID(id[i]).split("\n");
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < id.length; i++) {
                temp.append(cards[i][j]).append("    ");
            }
            temp.append("\n");
        }
        if (withNumber)
            for (int i = 1; i <= id.length; i++)
                temp.append("            ").append(i).append("           ");
        return temp.toString();
    }
}