package it.polimi.ingsw.exceptions;

/**
 * Class NotEnoughResourcesException is thrown when a player tries to use an amount of resources greater than
 * the resources present in his stores.
 *
 * @author Galetti Filippo
 */
public class NotEnoughResourcesException extends Throwable {
    /**
     * get message
     *
     * @return the message of class NotEnoughResourcesException.
     */
    public String getMessage() {
        return "There aren't enough resources for this action";
    }
}
