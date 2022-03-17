package it.polimi.ingsw.exceptions;

/**
 * Class EmptyCardSlotException is thrown when the development card slot is empty.
 *
 * @author Galetti Filippo
 */
public class EmptyCardSlotException extends Exception {
    /**
     * @return the message of the DuplicatedNicknameException object
     */
    public String getMessage() {
        return "There's no card left in this stack";
    }
}
