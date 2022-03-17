package it.polimi.ingsw.model.game.deck.leaderCard;

import it.polimi.ingsw.exceptions.AlreadyActivatedLeaderCardException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;

import javax.naming.SizeLimitExceededException;

/**
 * Class LeaderCardDepotAbility
 *
 * @author Grugni Federico
 */
public class LeaderCardDepotAbility extends LeaderCard {
    private final ResourceType requirements;
    private final ResourceType leaderCardDepot;

    /**
     * LeaderCardDepotAbility constructor which initialize the requirements
     * and the depot
     *
     * @param requirements    resources required
     *                        to buy the LeaderCard
     * @param leaderCardDepot extra 2-slot depot of a Resource
     */
    public LeaderCardDepotAbility(ResourceType requirements, ResourceType leaderCardDepot, GameBoard gameBoard) {
        this.requirements = requirements;
        this.leaderCardDepot = leaderCardDepot;
    }

    /**
     * get requirements
     *
     * @return cost of the LeaderCard
     */
    public ResourceType getRequirements() {
        return requirements;
    }

    @Override
    public void setLeaderCardAbility(GameBoard gameBoard) throws SizeLimitExceededException {
        gameBoard.getLeaderCardAbility().addDepot(leaderCardDepot);
        super.setActivatedLeaderCard(true);
    }

    @Override
    public boolean cardVerified(GameBoard gameBoard) throws NotEnoughResourcesException, AlreadyActivatedLeaderCardException {
        if (super.isActivatedLeaderCard())
            throw new AlreadyActivatedLeaderCardException();
        if (!gameBoard.automaticResourcesRequest(new Resource[]{new Resource(5, requirements)}, false))
            throw new NotEnoughResourcesException();
        return true;
    }

    @Override
    public String parsingLeaderCard() {
        String parsing = "Requirements: ";
        parsing += "5 " + requirements + "\n";
        parsing += "Victory points: " + super.getLeaderCardVictoryPoints() + "\n";
        parsing += "Depot resource type: " + leaderCardDepot + "\n";
        return parsing;
    }
}
