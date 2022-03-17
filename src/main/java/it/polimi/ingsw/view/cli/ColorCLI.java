package it.polimi.ingsw.view.cli;

/**
 * Enum ColorCLI is used to get the ANSI color for a String in the CLI.
 *
 * @author Franzini Alessandro
 */
public enum ColorCLI {
    ANSI_WHITE("\u001B[97m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_GREY("\u001B[37m"),
    ANSI_YELLOW("\u001B[93m"),
    ANSI_ORANGE("\u001B[38;2;255;120;0m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_RED_BG("\u001B[41m"),
    ANSI_GREEN_BG("\u001B[42m"),
    ANSI_YELLOW_BG("\u001B[103m"),
    ANSI_ORANGE_BG("\u001B[48;2;255;120;0m");

    public static final String RESET = "\u001B[0m";
    private String escape;

    ColorCLI(String escape) {
        this.escape = escape;
    }

    /**
     * Method setColorMarble is used to get a colored String based on the type of Resource of the marble.
     *
     * @param input is the String of the ResourceType.
     * @return the colored String.
     */
    public static String setColorMarble(String input) {
        ColorCLI color;
        switch (input) {
            case "COIN":
                color = ColorCLI.ANSI_YELLOW;
                break;
            case "SERVANT":
                color = ColorCLI.ANSI_PURPLE;
                break;
            case "SHIELD":
                color = ColorCLI.ANSI_BLUE;
                break;
            case "STONE":
                color = ColorCLI.ANSI_GREY;
                break;
            case "FAITH":
                color = ColorCLI.ANSI_RED;
                break;
            case "BLANK":
                color = ColorCLI.ANSI_WHITE;
                break;
            default:
                return "●";
        }
        return color + "●" + ColorCLI.RESET;
    }

    /**
     * Method setColorResource is used get a colored String based on the type of the Resource.
     *
     * @param input is the String of the ResourceType.
     * @return the colored String.
     */
    public static String setColorResource(String input) {
        String resource = "";
        switch (input) {
            case "COIN":
                resource = ColorCLI.ANSI_YELLOW + "◑";
                break;
            case "SERVANT":
                resource = ColorCLI.ANSI_PURPLE + "☻";
                break;
            case "SHIELD":
                resource = ColorCLI.ANSI_BLUE + "⛊";
                break;
            case "STONE":
                resource = ColorCLI.ANSI_GREY + "◆";
                break;
            case "FAITH":
                resource = ColorCLI.ANSI_RED + "\u2671";
                break;
            default:
                break;
        }
        return ColorCLI.RESET + resource + ColorCLI.RESET;
    }

    /**
     * Create string that represent a flag with defined color and level.
     *
     * @param level  is the level of the flag
     * @param colour is the colour of the flag
     * @return the flag as a string
     */
    public static String setColorFlag(String level, String colour) {
        String lvl;
        String color;
        lvl = switch (level) {
            case "LEVEL_1" -> "Ⅰ";
            case "LEVEL_2" -> "Ⅱ";
            case "LEVEL_3" -> "Ⅲ";
            default -> "";
        };
        color = switch (colour) {
            case "YELLOW" -> ColorCLI.ANSI_YELLOW + "\u2691";
            case "PURPLE" -> ColorCLI.ANSI_PURPLE + "\u2691";
            case "BLUE" -> ColorCLI.ANSI_BLUE + "\u2691";
            case "GREEN" -> ColorCLI.ANSI_GREEN + "\u2691";
            default -> ColorCLI.ANSI_GREY + "\u2691";
        };
        return lvl + color + ColorCLI.RESET;
    }

    @Override
    public String toString() {
        return escape;
    }
}
