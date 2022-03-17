package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.network.server.Backup;
import it.polimi.ingsw.model.commons.Color;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;
import it.polimi.ingsw.model.game.deck.actionToken.ActionToken;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientOffline;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;

import javax.management.InstanceAlreadyExistsException;
import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.controller.GameController.getGameController;
import static it.polimi.ingsw.model.game.Game.getGame;

/**
 * class EventController
 *
 * @author Grugni Federico, Galetti Filippo, Franzini Alessandro
 */
public class EventController {
    private static EventController instance;
    List<LeaderCard> appList = new ArrayList<>();
    private int endGameCounter = 0;
    private ClientOffline client;
    private ClientHandler clientHandler;
    private Gson gson = new Gson();

    /**
     * Constructor of Event Controller.
     */
    private EventController() {
        getGameController();
    }

    /**
     * Constructor of Event Controller for offline game.
     *
     * @param client is the client offline of game.
     */
    private EventController(ClientOffline client) {
        super();
        this.client = client;
    }

    /**
     * Get EventController by Singleton
     *
     * @return instance of Event Controller.
     */
    public static EventController getEventController() {
        if (instance == null)
            instance = new EventController();
        return instance;
    }

    /**
     * Get EventController by Singleton for offline game.
     *
     * @return instance of Event Controller.
     */
    public static EventController getEventController(ClientOffline client) throws InstanceAlreadyExistsException {
        if (instance == null)
            instance = new EventController(client);
        else
            throw new InstanceAlreadyExistsException();
        return instance;
    }

    /**
     * Send papal report to all players if moveFaithMarker() return true
     */
    public void sendPapalReport() {
        if (Server.getNumberOfPlayers() == 1) {
            getGame().getLorenzoFaithTrack().checkPapalFavor();
        }
        for (Player player : getGameController().getGame().getAllNonCurrentPlayers()) {
            player.getGameBoard().getFaithTrack().checkPapalFavor();
        }
        updatePapalState();
    }

    /**
     * Get client
     *
     * @return client
     */
    public ClientOffline getClient() {
        return client;
    }

    /**
     * Set client handler.
     *
     * @param clientHandler to set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Update papal state.
     */
    public void updatePapalState() {
        if (Server.getNumberOfPlayers() == 1 || EventController.getEventController().client != null)
            sendMessage(new Message(MessageType.POPE_TILES_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().getTilesStateList()));
        else
            for (Player player : getGame().getAllPlayers())
                Server.getClientHandlerFromUsername(player.getUsername()).sendMessage(new Message(MessageType.POPE_TILES_UPDATE, player.getGameBoard().getFaithTrack().getTilesStateList()));
    }

    /**
     * Method sendMessage sends a Message to the client (directly when the match is offline, through the ClientHandler when online).
     *
     * @param message is the message to be sent.
     */
    public void sendMessage(Message message) {
        if (client != null)
            client.handleMessage(message);
        else
            clientHandler.sendMessage(message);
    }

    /**
     * Method sendMessage sends a Message to a specific client through the ClientHandler.
     *
     * @param message is the message to be sent.
     * @param client  is the client to send the message to.
     */
    public void sendMessageTo(Message message, String client) {
        Server.getClientHandlerFromUsername(client).sendMessage(message);
    }

    /**
     * Method sendToAll sends a Message to all the players of the match.
     *
     * @param message is the message to be sent.
     */
    public void sendMessageToAll(Message message) {
        if (client != null)
            client.handleMessage(message);
        else
            Server.sendMessageToAllClients(message);
    }

    /**
     * Method sendToAllExceptThis sends a Message to all the players of the match except the one who's playing.
     *
     * @param message is the message to be sent.
     */
    public void sendMessageToAllExceptThis(Message message) {
        if (Server.getNumberOfPlayers() == 1)
            return;
        for (ClientHandler clients : Server.getConnectedClients())
            if (!clients.equals(clientHandler))
                Server.sendMessageToClient(clients, message);
    }

    /**
     * Check which actions player can do.
     */
    public void checkActionOptions() {
        boolean[] legalActions = new boolean[]{false, false};
        PurchaseDevelopmentCard temp = new PurchaseDevelopmentCard();
        if (temp.takeCardToShow().stream().anyMatch(n -> n != 0) && temp.takeCardToShow().stream().anyMatch(n -> n != -1)) {
            legalActions[0] = true;
        }
        if (getGameController().getTurn().getCurrentPlayer().getGameBoard().peekAllStoresResources() != null
                && (Arrays.asList(getGameController().getTurn().getCurrentPlayer().getGameBoard().peekAllStoresResources())).size() >= 2) {
            legalActions[1] = true;
        }
        getEventController().sendMessage(new Message(MessageType.ACTION_OPTIONS, legalActions));
    }


    /**
     * Method handleMessage is used to handle the message based on the type of the message.
     *
     * @param message is the Message to be handled.
     */
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case ACTION_OPTIONS:
                int action = gson.fromJson(message.getMessage(), int.class);
                if (action == 1) {
                    getGameController().getTurn().setAction(1);
                    sendMessage(new Message(MessageType.MARKET_CHOICE));
                }
                if (action == 2) {
                    getGameController().getTurn().setAction(2);
                    ((PurchaseDevelopmentCard) getGameController().getTurn().getAction()).takeCardToShow();
                    sendMessage(new Message(MessageType.PURCHASE_CARD_LIST, ((PurchaseDevelopmentCard) getGameController().getTurn().getAction()).takeCardToShow().toArray()));
                }
                if (action == 3) {
                    getGameController().getTurn().setAction(3);
                    ((ActivateProduction) getGameController().getTurn().getAction()).checkProductionActions();
                }
                if (action == 4) {
                    getGameController().getTurn().setAction(4);
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).startLeaderCardAction(false);
                }
                break;
            case LEADERCARD_SELECTION:
                int[] indexes = gson.fromJson(message.getMessage(), int[].class);
                getGameController().getTurn().getCurrentPlayer().setLeaderCards(indexes);
                sendMessage(new Message(MessageType.LEADERCARD_INITIAL_INDEX, message.getMessage()));
                getGameController().getTurn().getCurrentPlayer().setLeaderCardsAlreadyChosen();
                setupInitialResources();
                break;
            case INITIAL_RESOURCES_SELECTION:
                String[] res = message.getMessage().split(" ");
                Resource[] selected = new Resource[res.length];
                for (int i = 0; i < res.length; i++)
                    selected[i] = new Resource(1, ResourceType.resourceTypeConverter(res[i]));
                GameController.getGameController().getTurn().setAction(1);
                ((TakeResources) GameController.getGameController().getTurn().getAction()).setPurchased(Resource.sortResources(selected));
                StringBuilder initialRes = new StringBuilder();
                for (Resource resource : ((TakeResources) getGameController().getTurn().getAction()).getPurchased())
                    for (int i = 0; i < resource.getQuantity(); i++)
                        initialRes.append(resource.getResourceType()).append(" ");
                sendMessage(new Message(MessageType.PURCHASED_RESOURCES, initialRes.toString()));
                sendMessage(new Message(MessageType.ARRANGED_RESOURCES, 0));
                break;
            /* Leader Card */
            case LEADERCARD_SHOW_ACTIVATION_OPTION:
                List<Integer> cardsID = new ArrayList<>();
                ((LeaderCardsAction) getGameController().getTurn().getAction()).setActivated(gson.fromJson(message.getMessage(), boolean.class));
                appList = ((LeaderCardsAction) getGameController().getTurn().getAction()).giveLeaderCards();
                if (appList.size() > 0) {
                    for (LeaderCard leaderCard : appList) {
                        cardsID.add(leaderCard.getID());
                    }
                    sendMessage(new Message(MessageType.LEADERCARD_ACTIVATION_INDEX, cardsID));
                } else {
                    sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "\nNo cards to show."));
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).startLeaderCardAction(true);
                }
                break;
            case LEADERCARD_SELECTED_CARD:
                try {
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).leaderCardActivation(gson.fromJson(message.getMessage(), int.class));
                    sendMessage(new Message(MessageType.LEADERCARD_ACTIVATED, message.getMessage()));
                    if (((LeaderCardsAction) getGameController().getTurn().getAction()).giveLeaderCards().size() >= 1) {
                        sendMessage(new Message(MessageType.LEADERCARD_PERMISSION_TRY_AGAIN_ACTIVATE_CARD));
                    } else {
                        sendMessage(new Message(MessageType.LEADERCARD_END_ACTION));
                    }
                } catch (AlreadyActivatedLeaderCardException e) {
                    sendMessage(new Message(MessageType.LEADERCARD_ALREADY_ACTIVATED, true));
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.LEADERCARD_ERROR_NOT_ENOUGH_RESOURCES));
                } catch (NotEnoughColorCardQuantity e) {
                    sendMessage(new Message(MessageType.LEADERCARD_ERROR_NOT_ENOUGH_COLOR_CARD_QUANTITY));
                } catch (SizeLimitExceededException e) {
                    sendMessage(new Message(MessageType.ERROR_TOO_LEADERCARDS));
                } catch (NullPointerException e) {
                    sendMessage(new Message(MessageType.ERROR_NOT_INITIALIZED_ACTION));
                }
                break;
            case LEADERCARD_ANOTHER_ACTIVATE:
                if (((LeaderCardsAction) getGameController().getTurn().getAction()).giveLeaderCards().size() >= 2) {
                    sendMessage(new Message(MessageType.LEADERCARD_PERMISSION_TRY_AGAIN_ACTIVATE_CARD));
                } else {
                    sendMessage(new Message(MessageType.LEADERCARD_END_ACTION));
                }
                break;
            case LEADERCARD_SHOW_DISCARD_OPTION:
                cardsID = new ArrayList<>();
                boolean app = ((LeaderCardsAction) getGameController().getTurn().getAction()).getActivated();
                ((LeaderCardsAction) getGameController().getTurn().getAction()).setActivated(false);
                appList = ((LeaderCardsAction) getGameController().getTurn().getAction()).giveLeaderCards();
                ((LeaderCardsAction) getGameController().getTurn().getAction()).setActivated(app);
                if (appList.size() > 0) {
                    for (LeaderCard leaderCard : appList) {
                        cardsID.add(leaderCard.getID());
                    }
                    sendMessage(new Message(MessageType.LEADERCARD_DISCARD_INDEX, cardsID.toArray()));
                } else {
                    sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "\nNo cards to show."));
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).startLeaderCardAction(false);
                }
                break;
            case LEADERCARD_DISCARD_ACTION:
                try {
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).discardCard(gson.fromJson(message.getMessage(), int.class));
                    sendMessage(new Message(MessageType.LEADERCARD_DISCARD, message.getMessage()));
                    app = ((LeaderCardsAction) getGameController().getTurn().getAction()).getActivated();
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).setActivated(false);
                    appList = ((LeaderCardsAction) getGameController().getTurn().getAction()).giveLeaderCards();
                    ((LeaderCardsAction) getGameController().getTurn().getAction()).setActivated(app);
                    if (appList.size() > 0)
                        sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_DISCARD));
                    else
                        sendMessage(new Message(MessageType.LEADERCARD_END_ACTION));
                } catch (NullPointerException e) {
                    sendMessage(new Message(MessageType.ERROR_NOT_INITIALIZED_ACTION));
                } catch (AlreadyActivatedLeaderCardException e) {
                    sendMessage(new Message(MessageType.LEADERCARD_ALREADY_ACTIVATED, false));
                }
                break;

            /* Production */
            case PRODUCTION_OPTIONS:
                ((ActivateProduction) getGameController().getTurn().getAction()).checkProductionActions();
                break;
            case PRODUCTION_DEVELOPMENTCARD:
                int slot = gson.fromJson(message.getMessage(), int.class);
                try {
                    if (((ActivateProduction) getGameController().getTurn().getAction()).startCardProduction(slot)) {
                        sendMessage(new Message(MessageType.PRODUCTION_ACK, "The production of the card has been successfully activated"));
                    } else {
                        sendMessage(new Message(MessageType.PRODUCTION_ACK, "The production of this card was already activated before"));
                    }
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, e.getMessage()));
                }
                break;
            case PRODUCTION_LEADERCARD:
                try {
                    ((ActivateProduction) getGameController().getTurn().getAction()).startLeaderCardProduction();
                    sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_RESOURCE_INPUT));
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, e.getMessage()));
                } catch (EmptyProductionListException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, "There's no leader card available for production"));
                } catch (UndefinedLeaderCardAbilityException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_INDEX));
                }
                break;
            case PRODUCTION_LEADERCARD_RESOURCE_INPUT:
                ((ActivateProduction) getGameController().getTurn().getAction()).chooseLeaderCardProductionResourceType(ResourceType.resourceTypeConverter(message.getMessage()));
                sendMessage(new Message(MessageType.PRODUCTION_ACK, "The production of the leader card has been successfully activated"));
                break;
            case PRODUCTION_LEADERCARD_INDEX:
                int index = gson.fromJson(message.getMessage(), int.class);
                try {
                    ((ActivateProduction) getGameController().getTurn().getAction()).chooseLeaderCardProductionIndex(index);
                    sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_RESOURCE_INPUT));
                } catch (EmptyProductionListException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, "There's no leader card available for production"));
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, e.getMessage()));
                }
                break;
            case PRODUCTION_BASICPRODUCTION:
                if (((ActivateProduction) getGameController().getTurn().getAction()).isBasicProduction()) {
                    sendMessage(new Message(MessageType.PRODUCTION_BASICPRODUCTION));
                } else {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, "The basic production was already activated"));
                }
                break;
            case PRODUCTION_BASIC_RESOURCES_INPUT:
                String[] resources = message.getMessage().split(" ");
                try {
                    ((ActivateProduction) getGameController().getTurn().getAction()).startBasicProduction(
                            ResourceType.resourceTypeConverter(resources[0]),
                            ResourceType.resourceTypeConverter(resources[1]),
                            ResourceType.resourceTypeConverter(resources[2]));
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, "The basic production has been successfully activated"));
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.PRODUCTION_ACK, e.getMessage()));
                }
                break;
            case PRODUCTION_ACTIVATION:
                try {
                    ((ActivateProduction) getGameController().getTurn().getAction()).activateProduction();
                    updateAllStores();
                    checkActivableLeaderCard();
                } catch (NotEnoughResourcesException e) {
                    sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "Production error, the program will stop working"));
                }
                break;
            /* Purchase Cards */
            case PURCHASED_CARD:
                List<Integer> slotList = null;
                try {
                    slotList = getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().checkCardValidity(
                            ((PurchaseDevelopmentCard) getGameController().getTurn().getAction()).getDevelopmentCardByID(Integer.parseInt(message.getMessage())));
                } catch (EmptyCardSlotException e) {
                    sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "Purchase error, the program will stop working"));
                }
                if (slotList.size() == 1) {
                    endOfPurchaseAction(slotList.get(0) - 1);
                } else if (slotList.size() > 0) {
                    sendMessage(new Message(MessageType.PURCHASE_CHOOSE_SLOT, slotList.toArray()));
                }
                break;
            case PURCHASE_CHOOSE_SLOT:
                endOfPurchaseAction(Integer.parseInt(message.getMessage()));
                break;

            /* Take Resources */
            case MARKET_CHOICE:
                String[] parts = message.getMessage().split(" ");
                ((TakeResources) getGameController().getTurn().getAction()).buyFromMarket(Integer.parseInt(parts[1]), parts[0]);
                sendMessageToAll(new Message(MessageType.MARKET_UPDATE, getGame().getMarket()));
                StringBuilder purchased = new StringBuilder();
                int numBlank = 0;
                boolean faith = false, emptyLine = false;
                for (Resource resource : ((TakeResources) getGameController().getTurn().getAction()).getPurchased()) {
                    if (resource.getResourceType() == ResourceType.FAITH) {
                        purchased.append(resource.getResourceType()).append(" ");
                        faith = true;
                    }
                    if (resource.getResourceType() == ResourceType.BLANK)
                        numBlank++;
                }
                if ((((TakeResources) getGameController().getTurn().getAction()).getPurchased().length == 3 && (numBlank == 3 || (numBlank == 2 && faith))) ||
                        (((TakeResources) getGameController().getTurn().getAction()).getPurchased().length == 4 && (numBlank == 4 || (numBlank == 3 && faith))))
                    emptyLine = true;
                try {
                    ((TakeResources) getGameController().getTurn().getAction()).checksOnMarbles();
                } catch (UndefinedLeaderCardAbilityException e) {
                    sendMessage(new Message(MessageType.BLANKMARBLE_CHOICE, numBlank));
                }
                if (emptyLine)
                    checkActivableLeaderCard();
                for (Resource resource : ((TakeResources) getGameController().getTurn().getAction()).getPurchased())
                    for (int i = 0; i < resource.getQuantity(); i++)
                        purchased.append(resource.getResourceType()).append(" ");
                sendMessage(new Message(MessageType.PURCHASED_RESOURCES, purchased.toString()));
                sendMessage(new Message(MessageType.ARRANGED_RESOURCES, getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().numLeaderDepot()));
                break;
            case BLANKMARBLE_CHOICE:
                int[] leader = gson.fromJson(message.getMessage(), int[].class);
                ((TakeResources) getGameController().getTurn().getAction()).convertBlankWhenTwoLeaders(leader);
                purchased = new StringBuilder();
                for (Resource resource : ((TakeResources) getGameController().getTurn().getAction()).getPurchased())
                    for (int i = 0; i < resource.getQuantity(); i++)
                        purchased.append(resource.getResourceType()).append(" ");
                sendMessage(new Message(MessageType.PURCHASED_RESOURCES, purchased.toString()));
                sendMessage(new Message(MessageType.ARRANGED_RESOURCES, 0));
                break;
            case ARRANGED_RESOURCES:
                String[] inputString = message.getMessage().split(" ");
                Resource[] input = new Resource[inputString.length];
                for (int i = 0; i < inputString.length; i++) {
                    if (inputString[i].equals("null"))
                        input[i] = null;
                    else
                        input[i] = new Resource(Integer.parseInt(inputString[i].substring(0, 1)), ResourceType.resourceTypeConverter(inputString[i].substring(1)));
                }
                if (input.length == 3) {
                    try {
                        ((TakeResources) getGameController().getTurn().getAction()).addToStores(input, null);
                    } catch (IllegalInputException e) {
                        sendMessage(new Message(MessageType.ARRANGED_RESOURCES, getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().numLeaderDepot()));
                        break;
                    }
                }
                if (input.length > 3) {
                    Resource[] warehouse = new Resource[3];
                    Resource[] leaderDepot = new Resource[2];
                    System.arraycopy(input, 0, warehouse, 0, 3);
                    if (input.length == 4) {
                        leaderDepot[0] = input[3];
                        leaderDepot[1] = null;
                    } else {
                        leaderDepot[0] = input[3];
                        leaderDepot[1] = input[4];
                    }
                    try {
                        ((TakeResources) getGameController().getTurn().getAction()).addToStores(warehouse, leaderDepot);
                    } catch (IllegalInputException e) {
                        sendMessage(new Message(MessageType.ARRANGED_RESOURCES, getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().numLeaderDepot()));
                        break;
                    }
                    sendMessage(new Message(MessageType.LEADERDEPOT_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().peekAllResources()));
                }
                sendMessage(new Message(MessageType.WAREHOUSE_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getWarehouse().toString()));
                if (getGameController().getTurn().getCurrentPlayer().isInitialResourcesAlreadyChosen())
                    checkActivableLeaderCard();
                else {
                    GameController.getGameController().getTurn().getCurrentPlayer().setInitialResourcesAlreadyChosen();
                    checkActionOptions();
                }
                break;
            case END_TURN:
                checkSinglePlayer();
                checkEndFaithTrack();
                if (client == null) {
                    Backup.saveGame();
                }
                getGameController().nextTurn();
                break;
            case END_GAME:
                if (client != null)
                    System.exit(0);
                else {
                    endGameCounter++;
                    if (endGameCounter == Server.getNumberOfPlayers()) {
                        Backup.removeGame();
                        Server.close();
                    }
                }
                break;
            case RESTORE_GAME:
                if (message.getMessage() != null) {
                    System.out.println("Restoring the game ...");
                    Backup.setDate(message.getMessage());
                    Backup.restoreGame();
                    GameController.getGameController().setGameModel();
                    EventController.getEventController().updateAll();
                    GameController.getGameController().nextTurn();
                } else {
                    GameController.getGameController().startGame();
                }
                break;
            case PLAYER_DISCONNECTION:
                int pos = -1;
                String out = "";
                if (message.getMessage().matches("-?\\d+(\\d+)?"))
                    pos = Integer.parseInt(message.getMessage());
                if (pos == -1)
                    out = "Player '" + message.getMessage() + "' disconnected!";
                else {
                    List<String> waitingPlayers = new ArrayList<>(Server.getWaitingClientUsername());
                    out = "Player '" + waitingPlayers.get(pos) + "' disconnected!";
                }
                System.out.println(out);
                int connectedClients = -1;
                for (ClientHandler clientHandler : Server.getConnectedClients())
                    if (clientHandler.isConnected())
                        connectedClients++;
                if (connectedClients > 0 || (connectedClients == 0 && !Server.isGameStarted())) {
                    sendMessageToAll(new Message(MessageType.DISPLAY_MESSAGE, out));
                    Server.removePlayer(message.getMessage());
                } else
                    Server.close();
                break;
        }
    }

    /**
     * Method setupInitialResources lets the valid clients to choose the initial resource/s and
     * in case updates their faith marker's position.
     */
    public void setupInitialResources() {
        int initialResourcesNumber = 0;
        switch (getGame().getCurrentPlayer()) {
            case 0 -> getGameController().getTurn().getCurrentPlayer().setInitialResourcesAlreadyChosen();
            case 1 -> initialResourcesNumber = 1;
            case 2 -> {
                initialResourcesNumber = 1;
                getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().moveFaithMarker(1);
            }
            case 3 -> {
                initialResourcesNumber = 2;
                getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().moveFaithMarker(1);
            }
        }
        if (initialResourcesNumber != 0) {
            sendMessage(new Message(MessageType.INITIAL_RESOURCES_SELECTION, initialResourcesNumber));
        } else
            checkActionOptions();
    }

    private void endOfPurchaseAction(int slot) {
        try {
            if (((PurchaseDevelopmentCard) getGameController().getTurn().getAction()).verifyPurchase(slot)) {
                sendMessage(new Message(MessageType.SLOTSTACK_CARDS_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().peekTopCardsIDs().toArray()));
                sendMessage(new Message(MessageType.SLOTSTACK_FLAGS_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getSlotStack().getFlagsCounter()));
                sendMessageToAll(new Message(MessageType.DEVELOPMENTCARD_DECK_UPDATE, getGame().getDeckDevelopmentCard().developmentCardsIDList().toArray()));
                updateAllStores();
                if (((PurchaseDevelopmentCard) getGameController().getTurn().getAction()).verifyWin()) {
                    getGame().setLastTurn();
                }
                checkActivableLeaderCard();

            }
        } catch (NotEnoughResourcesException e) {
            sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "Purchase error, the program will stop working"));
        }
    }

    /**
     * Sends the necessary messages to update the stores data in client view.
     */
    public void updateAllStores() {
        sendMessage(new Message(MessageType.WAREHOUSE_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getWarehouse().toString()));
        sendMessage(new Message(MessageType.STRONGBOX_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getStrongbox().toString()));
        sendMessage(new Message(MessageType.LEADERDEPOT_UPDATE, getGameController().getTurn().getCurrentPlayer().getGameBoard().getLeaderCardAbility().peekAllResources()));
    }

    /**
     * Used when restoring a Game, it sends all the necessary messages to update the client's view data.
     */
    public void updateAll() {
        List<Player> playerList = getGame().getAllPlayers();
        for (ClientHandler player : Server.getConnectedClients()) {
            int i = 0;
            while (!player.equals(Server.getClientHandlerFromUsername(playerList.get(i).getUsername())))
                i++;
            if (player.equals(Server.getClientHandlerFromUsername(playerList.get(i).getUsername()))) {
                updatePlayer(player, playerList.get(i));
            }
        }
    }

    /**
     * Sends messages to the indicated clientHandler to update the player's data in client view.
     *
     * @param clientHandler is the ClientHandler to send the messages to.
     * @param player        is the player from who take the necessary data.
     */
    public void updatePlayer(ClientHandler clientHandler, Player player) {
        clientHandler.sendMessage(new Message(MessageType.MARKET_UPDATE, getGame().getMarket()));
        clientHandler.sendMessage(new Message(MessageType.DEVELOPMENTCARD_DECK_UPDATE, getGame().getDeckDevelopmentCard().developmentCardsIDList().toArray()));
        clientHandler.sendMessage(new Message(MessageType.WAREHOUSE_UPDATE, player.getGameBoard().getWarehouse().toString()));
        clientHandler.sendMessage(new Message(MessageType.STRONGBOX_UPDATE, player.getGameBoard().getStrongbox().toString()));
        clientHandler.sendMessage(new Message(MessageType.LEADERDEPOT_UPDATE, player.getGameBoard().getLeaderCardAbility().peekAllResources()));
        clientHandler.sendMessage(new Message(MessageType.POPE_TILES_UPDATE, player.getGameBoard().getFaithTrack().getTilesStateList()));
        clientHandler.sendMessage(new Message(MessageType.SLOTSTACK_CARDS_UPDATE, player.getGameBoard().getSlotStack().peekTopCardsIDs().toArray()));
        clientHandler.sendMessage(new Message(MessageType.SLOTSTACK_FLAGS_UPDATE, player.getGameBoard().getSlotStack().getFlagsCounter()));
        clientHandler.sendMessage(new Message(MessageType.FAITH_MARKER_UPDATE, player.getGameBoard().getFaithTrack().getFaithMarkerPosition()));
        int[] indexes = new int[]{-1, -1, 0, 0};
        int j = 0;
        for (LeaderCard leaderCard : player.getLeaderCards()) {
            indexes[j] = leaderCard.getID();
            if(leaderCard.isActivatedLeaderCard())
                indexes[j+2] = 1;
            j++;
        }
        clientHandler.sendMessage(new Message(MessageType.LEADERCARD_UPDATE, indexes));
    }

    /**
     * Method checkSinglePlayer verifies if the game is played in single player mode.
     * If so, at the end of each turn, sends a message of update to the client to notify the changes after playing the action token.
     */
    private void checkSinglePlayer() {
        if (Server.getNumberOfPlayers() == 1) {
            int counter = getGame().getDeckActionToken().getCounter();
            ActionToken<?> token = getGame().getDeckActionToken().getActionToken(counter);
            if (token.getValue().equals(Color.BLUE) || token.getValue().equals(Color.GREEN) || token.getValue().equals(Color.PURPLE) || token.getValue().equals(Color.YELLOW)) {
                if (!getGame().getDeckDevelopmentCard().discardColorCard((Color) token.getValue())) {
                    getGameController().setLoser();
                    getGame().setLastTurn();
                }
                getGame().getDeckActionToken().incCounter();
                sendMessageToAll(new Message(MessageType.DEVELOPMENTCARD_DECK_UPDATE, getGame().getDeckDevelopmentCard().developmentCardsIDList().toArray()));
            }
            if (token.getValue().equals(2)) {
                getGame().getLorenzoFaithTrack().moveFaithMarker(2);
                getGame().getDeckActionToken().incCounter();
                sendMessage(new Message(MessageType.LORENZO_TRACK, getGame().getLorenzoFaithTrack().getFaithMarkerPosition()));
            }
            if (token.getValue().equals(1)) {
                getGame().getLorenzoFaithTrack().moveFaithMarker(1);
                getGame().getDeckActionToken().shuffle();
                sendMessage(new Message(MessageType.LORENZO_TRACK, getGame().getLorenzoFaithTrack().getFaithMarkerPosition()));
            }
            if (getGame().getLorenzoFaithTrack().getFaithMarkerPosition() >= 24) {
                getGameController().setLoser();
                getGame().setLastTurn();
            }
            sendMessage(new Message(MessageType.ACTION_TOKEN, token.getValue()));
        }
    }

    /**
     * Checks if the faith marker has reached the last cell of the faith track.
     * If so, sets the last turn of the game for the remaining players.
     */
    private void checkEndFaithTrack() {
        if (getGameController().getTurn().getCurrentPlayer().getGameBoard().getFaithTrack().getFaithMarkerPosition() >= 24) {
            getGame().setLastTurn();
        }
    }

    /**
     * Checks if there are activable leader card at the end of the turn.
     */
    private void checkActivableLeaderCard() {
        sendMessage(new Message(MessageType.END_ACTION));
        sendMessage(new Message(MessageType.END_TURN));
    }

    /**
     * Used to debug.
     * Add resources and faith points.
     */
    public void debug() {
        for (Player player : getGame().getAllPlayers()) {
            player.getGameBoard().getFaithTrack().moveFaithMarker(20);
            player.getGameBoard().getStrongbox().addResources(new Resource[]{
                    new Resource(99, ResourceType.COIN),
                    new Resource(99, ResourceType.STONE),
                    new Resource(99, ResourceType.SHIELD),
                    new Resource(99, ResourceType.SERVANT)
            });
            sendMessage(new Message(MessageType.FAITH_MARKER_UPDATE, player.getGameBoard().getFaithTrack().getFaithMarkerPosition()));
            sendMessage(new Message(MessageType.STRONGBOX_UPDATE, player.getGameBoard().getStrongbox().toString()));
        }
    }
}
