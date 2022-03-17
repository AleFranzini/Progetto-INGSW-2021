package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.messages.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class FaithTrackCLI contains the methods to create the faithtrack string.
 */
public class FaithTrackCLI {
    private List<Integer> popeTiles;
    private int faithMarkerPosition;

    /**
     * Construct the faithtrack by setting the default values to its structures.
     */
    public FaithTrackCLI() {
        popeTiles = new ArrayList<>(Arrays.asList(0, 0, 0));
        faithMarkerPosition = 0;
    }

    /**
     * Update the pope tiles of the faithtrack.
     *
     * @param popeTiles is the list of integers to update.
     */
    public void setPopeTiles(List<Integer> popeTiles) {
        this.popeTiles = popeTiles;
    }

    /**
     * Set the position of the faith marker in the faithtrack.
     *
     * @param message contains the value of the faith marker position
     */
    public void setFaithMarkerPosition(Message message) {
        faithMarkerPosition = Integer.parseInt(message.getMessage());
    }

    /**
     * Create the string to represent the updated state of the pope tile.
     *
     * @param n is the value of the pope tile
     * @return the pope tile as a string
     */
    private String evaluatePopeTile(int n) {
        if (popeTiles.get(n) == 0) {
            if (n == 0) {
                return ColorCLI.ANSI_YELLOW_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ❌  " + ColorCLI.RESET;
            } else if (n == 1) {
                return ColorCLI.ANSI_ORANGE_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ❌  " + ColorCLI.RESET;
            } else if (n == 2) {
                return ColorCLI.ANSI_RED_BG.toString() + ColorCLI.ANSI_BLACK + "  4 ❌  " + ColorCLI.RESET;
            }
        } else if (popeTiles.get(n) == 1) {
            if (n == 0) {
                return ColorCLI.ANSI_GREEN_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ✔  " + ColorCLI.RESET;
            } else if (n == 1) {
                return ColorCLI.ANSI_GREEN_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ✔  " + ColorCLI.RESET;
            } else if (n == 2) {
                return ColorCLI.ANSI_GREEN_BG.toString() + ColorCLI.ANSI_BLACK + "  4 ✔  " + ColorCLI.RESET;
            }
        } else if (popeTiles.get(n) == -1) {
            if (n == 0) {
                return ColorCLI.ANSI_RED_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ❌  " + ColorCLI.RESET;
            } else if (n == 1) {
                return ColorCLI.ANSI_RED_BG.toString() + ColorCLI.ANSI_BLACK + "  2 ❌  " + ColorCLI.RESET;
            } else if (n == 2) {
                return ColorCLI.ANSI_RED_BG.toString() + ColorCLI.ANSI_BLACK + "  4 ❌  " + ColorCLI.RESET;
            }
        }
        return "error";
    }

    /**
     * Create the string that represent the faith marker inside the faithtrack.
     * If the lorenzo parameter is true also prints the black cross.
     *
     * @param lorenzo is the boolean that decide if the black cross has to be printed
     * @return the faith marker as a string
     */
    private String printFaithMarker(boolean lorenzo) {
        String marker;
        if (lorenzo)
            marker = ColorCLI.RESET + "\u2671";
        else
            marker = ColorCLI.setColorResource("FAITH");
        StringBuilder track = new StringBuilder();
        track.append("       ");
        for (int i = 0; i < 25; i++) {
            track.append(colorTrack(i)).append("│ ");
            if (faithMarkerPosition == i) {
                track.append(marker).append(" ");
            } else {
                track.append("  ");
            }
            track.append(colorTrack(i)).append(" │");
        }
        return track + ColorCLI.RESET;
    }

    /**
     * Evaluate the position passed as parameter and decide which color to apply.
     *
     * @param i is the position to evaluate
     * @return the string of the color to apply
     */
    private String colorTrack(int i) {
        if (i > 4 && i < 9) {
            return ColorCLI.ANSI_YELLOW.toString();
        } else if (i > 11 && i < 17) {
            return ColorCLI.ANSI_ORANGE.toString();
        } else if (i > 18) {
            return ColorCLI.ANSI_RED.toString();
        }
        if (i == 9 || i == 17) {
            return ColorCLI.RESET;
        }
        return "";
    }

    /**
     * Create the string that contains the faithtrack.
     * If the lorenzo parameter is true prints the lorenzo faithtrack.
     *
     * @param lorenzo is the boolean that decides which faithtrack has to be printed
     * @return the faithtrack as a string
     */
    public String printFaithTrack(boolean lorenzo) {
        String out = "       ┌────┐┌────┐┌────┐┌────┐┌────┐" + ColorCLI.ANSI_YELLOW + "┌────┐┌────┐┌────┐┌─" + ColorCLI.ANSI_RED + "♗ " + ColorCLI.ANSI_YELLOW + "─┐" + ColorCLI.RESET + "┌────┐┌────┐┌────┐" + ColorCLI.ANSI_ORANGE + "┌────┐┌────┐┌────┐┌────┐┌─" + ColorCLI.ANSI_RED + "♗ " + ColorCLI.ANSI_ORANGE + "─┐" + ColorCLI.RESET + "┌────┐┌────┐" + ColorCLI.ANSI_RED + "┌────┐┌────┐┌────┐┌────┐┌────┐┌─♗ ─┐" + ColorCLI.RESET + "\n" + printFaithMarker(lorenzo) + "\n" + "       └────┘└────┘└────┘└─1 ─┘└────┘" + ColorCLI.ANSI_YELLOW + "└────┘└─" + ColorCLI.RESET + "2 " + ColorCLI.ANSI_YELLOW + "─┘└────┘└────┘" + ColorCLI.RESET + "└─4 ─┘└────┘└────┘" + ColorCLI.ANSI_ORANGE + "└─" + ColorCLI.RESET + "6 " + ColorCLI.ANSI_ORANGE + "─┘└────┘└────┘└─" + ColorCLI.RESET + "9 " + ColorCLI.ANSI_ORANGE + "─┘└────┘" + ColorCLI.RESET + "└────┘└─12─┘" + ColorCLI.ANSI_RED + "└────┘└────┘└─" + ColorCLI.RESET + "16" + ColorCLI.ANSI_RED + "─┘└────┘└────┘└─" + ColorCLI.RESET + "20" + ColorCLI.ANSI_RED + "─┘" + ColorCLI.RESET + "\n";
        if (!lorenzo)
            out += "                                            │        │                                │        │                                      │        │" + "\n" + "                                            │" + evaluatePopeTile(0) + "│                                │" + evaluatePopeTile(1) + "│                                      │" + evaluatePopeTile(2) + "│" + "\n" + "                                            └────────┘                                └────────┘                                      └────────┘" + "\n\n\n";
        return out;
    }

}
