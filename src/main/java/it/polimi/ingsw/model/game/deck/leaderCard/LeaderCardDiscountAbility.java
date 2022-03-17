package it.polimi.ingsw.model.game.deck.leaderCard;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughColorCardQuantity;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.ResourceType;

import javax.naming.SizeLimitExceededException;

/**
 * Class LeaderCardDiscountAbility
 *
 * @author Grugni Federico
 */
public class LeaderCardDiscountAbility extends LeaderCard {
    private final Color[] requirements;
    private final ResourceType resourceId;

    /**
     * LeaderCardDiscountAbility constructor which initialize the requirements
     * and resourceId
     *
     * @param requirements color of DevelopmentCard required
     *                     to buy the LeaderCard
     * @param resourceId   the discounted Resource when you buy
     *                     a DevelopmentCard
     */
    public LeaderCardDiscountAbility(Color[] requirements, ResourceType resourceId) {
        this.requirements = requirements;
        this.resourceId = resourceId;
    }

    /**
     * get requirements
     *
     * @return Color array which represents the color of DevelopmentCard required
     */
    public Color[] getRequirements() {
        return requirements;
    }

    /**
     * get the discounted Resource
     *
     * @return the discounted Resource from the LeaderCard
     */
    public ResourceType getDiscountedResource() {
        return resourceId;
    }

    @Override
    public void setLeaderCardAbility(GameBoard gameBoard) throws SizeLimitExceededException {
        gameBoard.getLeaderCardAbility().addDiscount(resourceId);
        super.setActivatedLeaderCard(true);
    }

    @Override
    public boolean cardVerified(GameBoard gameBoard) throws NotEnoughColorCardQuantity, AlreadyActivatedLeaderCardException {
        if (super.isActivatedLeaderCard())
            throw new AlreadyActivatedLeaderCardException();
        for (Color tmp : requirements)
            if (gameBoard.getSlotStack().colorCardQuantity(tmp) < 1) {
                throw new NotEnoughColorCardQuantity();
            }
        return true;
    }

    @Override
    public String parsingLeaderCard() {
        String parsing = "Color requirements: ";
        parsing += "1 " + requirements[0] + ", 1 " + requirements[1] + "\n";
        parsing += "Victory points: " + super.getLeaderCardVictoryPoints() + "\n";
        parsing += "Discounted resource: " + resourceId + "\n";
        return parsing;
    }
}
