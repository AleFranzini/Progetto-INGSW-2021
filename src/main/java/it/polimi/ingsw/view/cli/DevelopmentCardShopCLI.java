package it.polimi.ingsw.view.cli;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DevelopmentCardShopCLI contains the methods to handle development cards graphic.
 */
public class DevelopmentCardShopCLI {
    private JSONArray deck;
    private List<Integer> cardList = new ArrayList<>();

    /**
     * Construct the object by importing the json file that contains the cards infos.
     */
    public DevelopmentCardShopCLI() {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/DevelopmentCard.json"));
        this.deck = (JSONArray) new JSONTokener(reader).nextValue();
    }

    /**
     * Update the list of card ids in the card shop.
     *
     * @param cardList is the list of card ids.
     */
    public void setCardList(List<Integer> cardList) {
        this.cardList = cardList;
    }

    /**
     * Create the string of the development card corresponding to the id parameter.
     *
     * @param id is the id of the card to be converted in a string
     * @return the card as a string
     */
    public String printDevelopmentCardByID(int id) {
        StringBuilder card = new StringBuilder();
        if (id == 0) {
            return "     ╔═════════════╗\n" + "     ║  ---------  ║\n" + "     ║     Non     ║\n" + "     ║ Purchasable ║\n" + "     ║     " + "Card" + "" + "    ║\n" + "     ║  ---------  ║\n" + "     ║             ║\n" + "     ╚═════════════╝\n";
        }
        if (id == -1) {
            return "     ╔═════════════╗\n" + "     ║             ║\n" + "     ║    Empty    ║\n" + "     ║    Stack    ║\n" + "     ║             ║\n" + "     ║             ║\n" + "     ║             ║\n" + "     ╚═════════════╝\n";
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getInt("id") == id) {
                        card.append("     ╔═════════════╗\n").append("     ║ ")
                                .append(ColorCLI.setColorFlag(deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getString("level"), deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getString("color"))).append("\u2590 ")
                                .append(formatArrayResources(deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getJSONArray("cost"))).append(" ║\n")
                                .append("     ╠═════════════╣\n").append("     ║").append(" In : ")
                                .append(formatArrayResources(deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getJSONArray("resourceIn"))).append("║\n")
                                .append("     ║").append(" Out: ")
                                .append(formatArrayResources(deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getJSONArray("resourceOut"))).append("║\n")
                                .append("     ╠═════════════╣\n").append("     ║    ").append("VP: ").append(formatNumber(deck.getJSONArray(i).getJSONArray(j).getJSONObject(k).getInt("victoryPoints"))).append("   ║\n")
                                .append("     ╚═════════════╝\n");
                    }
                }
            }
        }
        return card.toString();
    }

    /**
     * Format the numbers so that the card dimensions remains coherent between every card.
     *
     * @param n is the number to format
     * @return the formatted string
     */
    private String formatNumber(int n) {
        if (n < 10) {
            return n + " ";
        } else {
            return Integer.toString(n);
        }
    }

    /**
     * Format the resources so that the card dimensions remains coherent between every card.
     *
     * @param json is the file which contains the resources to format
     * @return the formatted string
     */
    private String formatArrayResources(JSONArray json) {
        StringBuilder resource = new StringBuilder();
        if (json.length() == 1) {
            resource.append("   ");
            for (int i = 0; i < 1; i++) {
                resource.append(json.getJSONObject(i).getInt("quantity")).append(ColorCLI.setColorResource(json.getJSONObject(i).getString("resourceType")));
            }
            resource.append("  ");
        } else if (json.length() == 2) {
            resource.append(" ");
            for (int i = 0; i < 2; i++) {
                resource.append(json.getJSONObject(i).getInt("quantity")).append(ColorCLI.setColorResource(json.getJSONObject(i).getString("resourceType"))).append(" ");
            }
        } else if (json.length() == 3) {
            resource.append(" ");
            for (int i = 0; i < 3; i++) {
                resource.append(json.getJSONObject(i).getInt("quantity")).append(ColorCLI.setColorResource(json.getJSONObject(i).getString("resourceType")));
            }
        }
        return resource.toString();
    }

    /**
     * Create the string of the card shop for the list of card ids passed as parameter.
     *
     * @param cards is the list of card ids in the card shop
     * @return the card shop as a string
     */
    public String getDevelopmentCardMatrix(List<Integer> cards) {
        String[][][] cardShop = new String[3][4][];
        StringBuilder out = new StringBuilder();
        int i = 0;
        int j = 0;
        for (int n : cards) {
            cardShop[i][j] = printDevelopmentCardByID(n).split("\n");
            if (j < 3) {
                j++;
            } else if (j == 3) {
                j = 0;
                i++;
            }
        }
        for (int k = 0; k < 3; k++) {
            for (int w = 0; w < 8; w++) {
                out.append(cardShop[k][0][w]).append(cardShop[k][1][w]).append(cardShop[k][2][w]).append(cardShop[k][3][w]).append("\n");
            }
            out.append("                                                                                \n");
        }
        return out.toString();
    }

    /**
     * Create the string of the card shop for the list of card ids saved in the class.
     * @return the card shop as a string
     */
    public String getDevelopmentCardMatrix() {
        return getDevelopmentCardMatrix(cardList);
    }
}