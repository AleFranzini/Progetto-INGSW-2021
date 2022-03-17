package it.polimi.ingsw.model.commons;

/**
 * Class ResourceType
 */
public enum ResourceType {
    COIN,
    STONE,
    SHIELD,
    SERVANT,
    FAITH,
    BLANK;

    /**
     * Convert a string to the relative ResourceType constant.
     *
     * @param input is the string to convert
     * @return the relative constant
     */
    public static ResourceType resourceTypeConverter(String input) {
        return switch (input.toUpperCase()) {
            case "COIN" -> COIN;
            case "STONE" -> STONE;
            case "SHIELD" -> SHIELD;
            case "SERVANT" -> SERVANT;
            case "FAITH" -> FAITH;
            case "BLANK" -> BLANK;
            default -> null;
        };
    }
}
