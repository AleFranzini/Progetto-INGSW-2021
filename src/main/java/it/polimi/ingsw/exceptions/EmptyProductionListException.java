package it.polimi.ingsw.exceptions;

/**
 * Class EmptyProductionListException is thrown when the user try to activate the leaderCard productionAbility when
 * there is no usable production in the list.
 *
 * @author Galetti Filippo
 */
public class EmptyProductionListException extends Exception {
    /**
     * @return the message of the EmptyProductionListException object.
     */
    public String getMessage() {
        return "There is no leader card production ability to use!";
    }
}
