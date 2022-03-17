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
 * Class LeaderCardProductionAbility
 *
 * @author Grugni Federico
 */
public class LeaderCardProductionAbility extends LeaderCard {
    private final Color colorRequirements;
    private final ResourceType productionResource;

    /**
     * LeaderCardProductionAbility constructor which initialize the requirements
     *
     * @param colorRequirements  color of DevelopmentCard required
     *                           to buy the LeaderCard
     * @param productionResource color of DevelopmentCard required
     *                           to buy the LeaderCard
     */
    public LeaderCardProductionAbility(Color colorRequirements, ResourceType productionResource) {
        this.colorRequirements = colorRequirements;
        this.productionResource = productionResource;
    }

    /**
     * get colorRequirements
     *
     * @return the color of DevelopmentCard required
     */
    public Color getColorRequirements() {
        return colorRequirements;
    }

    /**
     * get productionResource
     *
     * @return the ResourceType needed to activate production
     */
    public ResourceType getProductionResource() {
        return productionResource;
    }

    @Override
    public void setLeaderCardAbility(GameBoard gameBoard) throws SizeLimitExceededException {
        gameBoard.getLeaderCardAbility().addProductionAbility(productionResource);
        super.setActivatedLeaderCard(true);
    }

    @Override
    public boolean cardVerified(GameBoard gameBoard) throws AlreadyActivatedLeaderCardException, NotEnoughColorCardQuantity {
        if (super.isActivatedLeaderCard())
            throw new AlreadyActivatedLeaderCardException();
        if (gameBoard.getSlotStack().levelTwoCardQuantity(colorRequirements) <= 0)
            throw new NotEnoughColorCardQuantity();
        return true;
    }

    @Override
    public String parsingLeaderCard() {
        String parsing = "Requirements: ";
        parsing += "1 level two " + colorRequirements + " card\n";
        parsing += "Victory points: " + super.getLeaderCardVictoryPoints() + "\n";
        parsing += "Power of production: 1 " + productionResource + " } 1 prefered resource, 1 faith point";
        return parsing;
    }
}
