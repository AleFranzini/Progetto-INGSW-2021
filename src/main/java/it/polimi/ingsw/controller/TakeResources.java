package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.exceptions.UndefinedLeaderCardAbilityException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;

import static it.polimi.ingsw.controller.EventController.getEventController;
import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;

/**
 * Class TakeResources
 *
 * @author Franzini Alessandro
 */
public class TakeResources implements Action {
    private final GameBoard gameboard;
    private Resource[] purchased;
    private final Market market = Game.getGame().getMarket();

    /**
     * Constructor of TakeResources sets the GameBoard of the current Player.
     */
    public TakeResources() {
        Turn turn = GameController.getGameController().getTurn();
        gameboard = turn.getCurrentPlayer().getGameBoard();
    }

    /**
     * Method getPurchased returns the array of purchased resources.
     *
     * @return the array of purchased resources.
     */
    public Resource[] getPurchased() {
        return purchased;
    }

    /**
     * Method setPurchased sets the array of purchased resources.
     *
     * @param purchased is the array of resources to set as purchased.
     */
    public void setPurchased(Resource[] purchased) {
        this.purchased = purchased;
    }

    /**
     * Method selectLine takes the row or the column of the Market chosen by the Player and sets attribute purchased.
     *
     * @param number is the number of the line chosen by the Player
     * @param line   is the row or the column chosen by the Player
     */
    public void buyFromMarket(int number, String line) {
        if (line.equals("r") || line.equals("R"))
            setPurchased(market.getRow(number));
        if (line.equals("c") || line.equals("C"))
            setPurchased(market.getColumn(number));
    }

    /**
     * Method checksOnMarbles does some checks on the purchased marbles.
     * If a marble is red, moves the Player's Faith Marker by one space on the Faith Track.
     * If a marble is white, and the Player has only one Leader Card ability that converts a blank marble into a resource,
     * each marble is converted.
     *
     * @throws UndefinedLeaderCardAbilityException when the Player has two Leader Card ability that converts a blank marble into a resource
     */
    public void checksOnMarbles() throws UndefinedLeaderCardAbilityException {
        for (Resource resource : purchased) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                gameboard.getFaithTrack().moveFaithMarker(1);
                getEventController().sendMessage(new Message(MessageType.FAITH_MARKER_UPDATE, gameboard.getFaithTrack().getFaithMarkerPosition()));
            }
        }
        purchased = gameboard.getLeaderCardAbility().convertBlankResource(purchased);
    }

    /**
     * Method convertBlankWhenTwoLeaders converts each white marble as indicated by the Player.
     *
     * @param choice is a array of integers which indicates the choice of the Player of which Leader Card to use for each white marble.
     */
    public void convertBlankWhenTwoLeaders(int[] choice) {
        int i = 0;
        ArrayList<Resource> tmp = new ArrayList<>();
        ResourceType[] abilities = gameboard.getLeaderCardAbility().getBlankResourcesType();
        for (Resource resource : purchased) {
            if (resource.getResourceType() == ResourceType.BLANK) {
                resource.setResourceType(abilities[choice[i] - 1]);
                i++;
                tmp.add(resource);
            }
        }
        purchased = Resource.sortResources(purchased);
        for (Resource resource : tmp) resource.setResourceType(ResourceType.BLANK);
    }

    /**
     * Method checkDiscardedResource checks if the Player has discarded some Resources bought from the Market,
     * if so, the other Players receive a Faith Point for each Resource discarded.
     *
     * @param choice sorted array of Resources inserted by the client in input.
     * @return the string with the discarded resources.
     * @throws IllegalInputException if the input doesn't respect the rules of the stores.
     */
    public String checkDiscardedResource(Resource[] choice) throws IllegalInputException {
        Resource[] actualStores = Resource.sumResources(gameboard.getWarehouse().peekAllResources(), gameboard.getLeaderCardAbility().peekAllResources());
        if (purchased == null)
            return null;
        if (choice == null)
            throw new IllegalInputException();
        if (actualStores == null)
            actualStores = Resource.createResources();

        Resource[] added = new Resource[4];
        for (int i = 0; i < 4; i++) {
            if (choice[i].getQuantity() < actualStores[i].getQuantity() || choice[i].getQuantity() > (actualStores[i].getQuantity() + purchased[i].getQuantity()))
                throw new IllegalInputException();
            else
                added[i] = new Resource(choice[i].getQuantity() - actualStores[i].getQuantity(), actualStores[i].getResourceType());
        }
        int count = 0, diff;
        StringBuilder discarded = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            diff = purchased[i].getQuantity() - added[i].getQuantity();
            count += diff;
            if (diff > 0)
                while (diff > 0) {
                    discarded.append(purchased[i].getResourceType()).append(" ");
                    diff--;
                }
        }
        if (count > 0) {
            if (Server.getNumberOfPlayers() > 1) {
                for (Player player : getGameController().getGame().getAllNonCurrentPlayers()) {
                    player.getGameBoard().getFaithTrack().moveFaithMarker(count);
                    Server.getClientHandlerFromUsername(player.getUsername()).sendMessage(new Message(MessageType.FAITH_MARKER_UPDATE, player.getGameBoard().getFaithTrack().getFaithMarkerPosition()));
                }
            } else {
                Game.getGame().getLorenzoFaithTrack().moveFaithMarker(count);
                getEventController().sendMessage(new Message(MessageType.LORENZO_TRACK, getGameController().getGame().getLorenzoFaithTrack().getFaithMarkerPosition()));
            }
            return discarded.toString();
        }
        return null;
    }

    /**
     * Method addToStores adds the purchased Resources to the stores as indicated by the Player.
     *
     * @param newWarehouse   is a Resource array that represent the arranged warehouse (after the purchase).
     * @param newLeaderDepot is a Resource array that represent the arranged Leader Card depot (after the purchase).
     *                       If there are no depots added with a Leader Card ability, this param is null.
     */
    public void addToStores(Resource[] newWarehouse, Resource[] newLeaderDepot) throws IllegalInputException {
        String discarded = checkDiscardedResource(Resource.sumResources(newWarehouse, newLeaderDepot));
        gameboard.getWarehouse().addResources(newWarehouse);
        if (gameboard.getLeaderCardAbility().numLeaderDepot() != 0 && newLeaderDepot != null)
            gameboard.getLeaderCardAbility().addResources(newLeaderDepot);
        if (discarded != null)
            EventController.getEventController().sendMessage(new Message(MessageType.DISCARDED_RESOURCES, discarded));
    }
}
