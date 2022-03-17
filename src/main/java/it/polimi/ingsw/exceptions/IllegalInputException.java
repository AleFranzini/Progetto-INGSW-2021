package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.commons.Resource;

/**
 * Class IllegalInputException is thrown when the user's input isn't valid.
 *
 * @author Franzini Alessandro
 */
public class IllegalInputException extends Exception {
    private String message;
    private Resource[] remainingResources;

    /**
     * display exception message
     */
    public IllegalInputException() {
        message = "Wrong Input!";
    }

    /**
     * to put remaining resources
     *
     * @param remainingResources resources array that cannot stay in depot
     */
    public IllegalInputException(Resource[] remainingResources) {
        message = "Try to add too many resources";
        this.remainingResources = remainingResources;
    }

    /**
     * get message
     *
     * @return the message of the IllegalInputException object.
     */
    public String getMessage() {
        return message;
    }

    /**
     * take remainining resources
     *
     * @return remaining resources from depot
     */
    public Resource[] takeRemainingResource() {
        return remainingResources;
    }
}