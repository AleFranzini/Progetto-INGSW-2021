package it.polimi.ingsw.model.commons;

/**
 * Class Level
 */
public enum Level {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3;

    /**
     * Maps the three predefined levels to each own predefined number.
     *
     * @param level to convert to a number
     * @return the predefined number mapped to the level parameter
     */
    public static int levelConverterToInt(Level level) {
        return switch (level) {
            case LEVEL_1 -> 0;
            case LEVEL_2 -> 1;
            case LEVEL_3 -> 2;
        };
    }
}
