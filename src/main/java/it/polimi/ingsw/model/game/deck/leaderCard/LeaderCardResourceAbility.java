package it.polimi.ingsw.model.game.deck.leaderCard;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.ResourceType;

import javax.naming.SizeLimitExceededException;

/**
 * Class LeaderCardResourceAbility
 *
 * @author Grugni Federico
 */
public class LeaderCardResourceAbility extends LeaderCard {
    private final Color[] colorRequirements; //primo 2 secondo 1
    private final ResourceType resourceId;

    /**
     * LeaderCardResourceAbility constructor which initialize the requirements
     * and resourceId
     *
     * @param color    color of DevelopmentCard required
     *                 to buy the LeaderCard
     * @param resource the Resource to take for each of the white marbles
     */
    public LeaderCardResourceAbility(Color[] color, ResourceType resource) {
        colorRequirements = color;
        resourceId = resource;
    }

    /**
     * get colorRequirements
     *
     * @return Color array which represents the color of DevelopmentCard required
     */
    public Color[] getColorRequirements() {
        return colorRequirements;
    }

    /**
     * get the ResourceId
     *
     * @return the Resource to take for each of the white marbles
     */
    public ResourceType getResourceId() {
        return resourceId;
    }

    @Override
    public void setLeaderCardAbility(GameBoard gameBoard) throws SizeLimitExceededException {
        gameBoard.getLeaderCardAbility().addBlankResource(resourceId);
        super.setActivatedLeaderCard(true);
    }

    @Override
    public boolean cardVerified(GameBoard gameBoard) throws AlreadyActivatedLeaderCardException, NotEnoughColorCardQuantity {
        if (super.isActivatedLeaderCard())
            throw new AlreadyActivatedLeaderCardException();
        if (gameBoard.getSlotStack().colorCardQuantity(colorRequirements[0]) < 2 || gameBoard.getSlotStack().colorCardQuantity(colorRequirements[1]) < 1)
            throw new NotEnoughColorCardQuantity();
        return true;
    }

    @Override
    public String parsingLeaderCard() {
        String parsing = "Color requirements: ";
        parsing += "2 " + colorRequirements[0] + ", 1 " + colorRequirements[1] + "\n";
        parsing += "Victory points: " + super.getLeaderCardVictoryPoints() + "\n";
        parsing += "Color of white marble: " + resourceId + "\n";
        return parsing;
    }
}
