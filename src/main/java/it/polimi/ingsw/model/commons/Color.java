package it.polimi.ingsw.model.commons;

/**
 * Class Color
 */
public enum Color {
    GREEN,
    BLUE,
    YELLOW,
    PURPLE;

    /**
     * Maps the four predefined colors to each own predefined number.
     *
     * @param color to convert to a number
     * @return the predefined number mapped to the color parameter
     */
    public static int colorConverterToInt(Color color) {
        return switch (color) {
            case GREEN -> 0;
            case BLUE -> 1;
            case YELLOW -> 2;
            case PURPLE -> 3;
        };
    }
}
