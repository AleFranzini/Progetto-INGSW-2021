package it.polimi.ingsw.exceptions;

/**
 * Class IllegalFaithResourceInputException is thrown when a faith resource is passed as an output
 * of the basic production.
 *
 * @author Galetti Filippo
 */
public class IllegalFaithResourceInputException extends Throwable {
    /**
     * @return the message of the IllegalFaithResourceInputException object
     */
    public String getMessage() {
        return "Faith resource cannot be the output of the basic production!";
    }
}
