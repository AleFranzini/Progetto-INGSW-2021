package it.polimi.ingsw.exceptions;

/**
 * undefined leader card ability exception
 *
 * @author Grugni Filippo, Galetti Federico
 */
public class UndefinedLeaderCardAbilityException extends Throwable {
    private String message;

    /**
     * set message that depends on the input:
     *
     * @param type String "blankResource" returns a message for blankResource ability
     *             String "productionAbility" returns a message for productionAbility
     */
    public UndefinedLeaderCardAbilityException(String type) {
        switch (type) {
            case "blankResource" -> this.message = "There are two resources the white marble can be converted into, you have to pick one!";
            case "productionAbility" -> {
                this.message = "You have two Leader Cards that can convert your blank marble/s.\nFor each marble you have to choose which Leader Card to use.";
                this.message = "There are two active leader card production, you have to choose one!";
            }
            default -> this.message = "There are two leader card active but sadly, type is unknown";
        }
    }

    /**
     * @return the message of the UndefinedLeaderCardAbilityException object
     */
    public String getMessage() {
        return message;
    }
}
