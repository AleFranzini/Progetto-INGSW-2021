package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyProductionListException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.exceptions.UndefinedLeaderCardAbilityException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.deck.developmentCard.DevelopmentCard;

import static it.polimi.ingsw.controller.EventController.getEventController;
import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * Class ActivateProduction
 */
public class ActivateProduction implements Action {
    private boolean[] slotChecked = new boolean[]{false, false, false};
    private Resource[] requestedResources;
    private Resource[] obtainedResources;
    private int faithPoints;
    private boolean basicProduction = true; //true if is yet to activate, false if already activated
    private boolean productionState = true;

    public ActivateProduction() {
        this.requestedResources = new Resource[4];
        this.obtainedResources = new Resource[4];
        this.faithPoints = 0;
    }

    public boolean isBasicProduction() {
        return basicProduction;
    }

    /**
     * Add a number of points equals to the parameter to the faith points quantity.
     * @param quantity is how much to add to the faith points
     */
    public void addFaithPoints(int quantity) {
        faithPoints += quantity;
    }

    /**
     * Verify if there is at least a card in the slotstack.
     *
     * @return true if there's at least a card, false otherwise
     */
    private boolean checkSlotStack() {
        return getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().peekTopCardsIDs().stream().anyMatch(n -> n != -1);
    }

    /**
     * Tell if the card in the selected slot has already been activated.
     *
     * @param slot is the slot of the card to check
     * @return true if the card is already activated, false if it's still to be activate
     * @throws IllegalArgumentException when the slot it's an illegal index
     */
    private boolean getSlotChecked(int slot) throws IllegalArgumentException {
        if (slot < 0 || slot > 2)
            throw new IllegalArgumentException();
        //the check could be done by the client, if we decided to leave it here it might be better to rename the exception
        return slotChecked[slot];
    }

    /**
     * Set the selected slot to true when the card production is activated.
     *
     * @param slot is the slot to set to true
     */
    private void setTrueSlotChecked(int slot) {
        this.slotChecked[slot] = true;
    }

    /**
     * Check if there are as much resources in all the stores as in the parameter array.
     * @param resources is the array of resources to check
     * @throws NullPointerException when the parameter is null or empty
     * @throws NotEnoughResourcesException when there aren't enough resources
     */
    private void checkEnoughResources(Resource[] resources) throws NullPointerException, NotEnoughResourcesException {
        resources = Resource.sortResources(resources);
        Resource[] currentResources = getGameController().getTurn().getCurrentPlayer().getGameBoard().peekAllStoresResources();
        if (resources == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < 4; i++) {
            if (currentResources == null || currentResources[i].getQuantity() - resources[i].getQuantity() < 0)
                throw new NotEnoughResourcesException();
        }
    }

    /**
     * Check which of the production actions is possible for the player to make.
     */
    public void checkProductionActions() {
        boolean[] legalActions = new boolean[]{false, false, false};
        if (checkSlotStack())
            legalActions[0] = true;
        if (getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().productionAbilityLeft())
            legalActions[1] = true;
        if (basicProduction)
            legalActions[2] = true;
        getEventController().sendMessage(new Message(MessageType.PRODUCTION_OPTIONS, legalActions));
    }

    /**
     * Save the resources necessary to activate the top card of the selected slot only if those resources are present in the stores,
     * and if the same card wasn't already activated.
     *
     * @return true if the production of the selected card was activated, false if the same card has been activated before
     * @throws NotEnoughResourcesException when there aren't enough resources in the store to activate the production
     *                                     of the selected card
     */
    public boolean startCardProduction(int slot) throws NotEnoughResourcesException {
        DevelopmentCard card = getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().peekCard(slot);
        if (card == null)
            getEventController().sendMessage(new Message(MessageType.PRODUCTION_ERROR_EMPTY_SLOT, "There is no card yet in this slot"));
        if (!getSlotChecked(slot)) {
            checkEnoughResources(Resource.sumResources(card.getResourceIn(), requestedResources));
            requestedResources = Resource.sumResources(card.getResourceIn(), requestedResources);
            setTrueSlotChecked(slot);
            addFaithPoints(Resource.totalFaithResources(card.getResourceOut()));
            obtainedResources = Resource.sumResources(card.getResourceOut(), obtainedResources);
            return true;
        }
        return false;
    }

    /**
     * Set the chosen resource for the leader card production output.
     * @param resourceType is the chosen resource type
     */
    public void chooseLeaderCardProductionResourceType(ResourceType resourceType) {
        Resource[] resourceOut = new Resource[1];
        resourceOut[0] = new Resource(1, resourceType);
        obtainedResources = Resource.sumResources(resourceOut, obtainedResources);
        addFaithPoints(1);
    }

    /**
     * Select the chosen leader card by the index in case there are two leader card productions activated.
     * @param index is the index of the chosen card
     * @throws EmptyProductionListException if the production was already activated
     * @throws NotEnoughResourcesException when there aren't enough resources to perform the action
     */
    public void chooseLeaderCardProductionIndex(int index) throws EmptyProductionListException, NotEnoughResourcesException {
        Resource[] resourcesIn = new Resource[1];
        resourcesIn[0] = getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().getProductionAbilityResourceIn(index);
        checkEnoughResources(Resource.sumResources(Resource.sortResources(resourcesIn), requestedResources));
        requestedResources = Resource.sumResources(resourcesIn, requestedResources);
    }

    /**
     * Start the production of the leader card in case there is only one activated.
     * @throws NotEnoughResourcesException if there aren't enough resources to perform the action
     * @throws EmptyProductionListException if the production was already activated
     * @throws UndefinedLeaderCardAbilityException if there are two leader cards production activated
     */
    public void startLeaderCardProduction() throws NotEnoughResourcesException, EmptyProductionListException, UndefinedLeaderCardAbilityException {
        Resource[] resourcesIn = new Resource[1];
        resourcesIn[0] = getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().getProductionAbilityResourceIn();
        checkEnoughResources(Resource.sumResources(Resource.sortResources(resourcesIn), requestedResources));
        requestedResources = Resource.sumResources(resourcesIn, requestedResources);
    }

    /**
     * Start the basic production.
     * @param input1 is the first resource in input
     * @param input2 is the second resource in input
     * @param output is the resource in output
     * @throws NotEnoughResourcesException if there aren't enough resources to perform the action
     */
    public void startBasicProduction(ResourceType input1, ResourceType input2, ResourceType output) throws NotEnoughResourcesException {
        Resource[] resourcesIn = new Resource[2];
        resourcesIn[0] = new Resource(1, input1);
        resourcesIn[1] = new Resource(1, input2);
        checkEnoughResources(Resource.sumResources(Resource.sortResources(resourcesIn), requestedResources));
        requestedResources = Resource.sumResources(resourcesIn, requestedResources);
        Resource[] resourceOut = new Resource[1];
        resourceOut[0] = new Resource(1, output);
        obtainedResources = Resource.sumResources(resourceOut, obtainedResources);
        basicProduction = false;
    }

    /**
     * Activate the production by gaining and loosing the previously handled resources.
     * @throws NotEnoughResourcesException if there aren't enough resources to perform the action
     */
    public void activateProduction() throws NotEnoughResourcesException {
        if (!getGameController().getTurn().getCurrentPlayer().getGameBoard().automaticResourcesRequest(requestedResources, true))
            throw new NotEnoughResourcesException(); //should never happen because check are already done previously, if happen there's a bug♦
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().addResources(obtainedResources);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().moveFaithMarker(faithPoints);
        getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().resetElementsUsability();
        getEventController().sendMessage(new Message(MessageType.FAITH_MARKER_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().getFaithMarkerPosition()));
    }
}
