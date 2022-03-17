package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Abstract Class Client
 *
 * @author Franzini Alessandro, Galetti Filippo
 */
public abstract class Client {
    protected View view;
    private String username = null;
    private Gson gson = new Gson();

    /**
     * Get the username of the client's player.
     *
     * @return the username as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the client's player.
     *
     * @param username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method handleMessage handles the Message received from the EventController or the Server depending on the MessageType.
     *
     * @param message is the message received.
     */
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            default:
                break;
            case DISPLAY_MESSAGE:
                view.displayMessage(message.getMessage());
                break;
            case LEADERCARD_INITIAL_INDEX:
                view.setInitialLeaderCard(gson.fromJson(message.getMessage(), int[].class));
                break;
            case INITIAL_RESOURCES_SELECTION:
                view.chooseInitialResources(Integer.parseInt(message.getMessage()));
                break;
            case LEADERCARD_ACTIVATED:
                view.setActivatedLeaderCard(gson.fromJson(message.getMessage(), int.class));
                break;
            case LEADERCARD_DISCARD:
                view.setDiscardLeaderCard(gson.fromJson(message.getMessage(), int.class));
                break;
            case MARKET_UPDATE:
                view.updateMarket(message);
                break;
            case DEVELOPMENTCARD_DECK_UPDATE:
                view.updateDevelopmentCardDeck(gson.fromJson(message.getMessage(), Integer[].class));
                break;
            case LEADERCARD_UPDATE:
                view.updateLeaderCard(gson.fromJson(message.getMessage(), int[].class));
                break;
            case POPE_TILES_UPDATE:
                view.updatePopeTiles(message);
                break;
            case FAITH_MARKER_UPDATE:
                view.updateFaithMarker(message);
                break;
            case WAREHOUSE_UPDATE:
                view.updateWarehouse(message);
                break;
            case STRONGBOX_UPDATE:
                view.updateStrongbox(message);
                break;
            case LEADERDEPOT_UPDATE:
                view.updateLeaderDepot(message);
                break;
            case SLOTSTACK_CARDS_UPDATE:
                view.updateSlotStack(Arrays.asList(gson.fromJson(message.getMessage(), Integer[].class)));
                break;
            case SLOTSTACK_FLAGS_UPDATE:
                view.updateFlagsCounter(message);
                break;
            case LEADERCARD_OPTION:
                view.displayLeaderCardOption(gson.fromJson(message.getMessage(), boolean[].class));
                break;
            case LEADERCARD_SELECTION:
                view.selectLeaderCardsFromDeck(gson.fromJson(message.getMessage(), int[].class));
                break;
            case ACTION_OPTIONS:
                view.chooseInitialAction(false, false, message);
                break;
            case LEADERCARD_ASK_ACTIVABLE_CARD:
                view.askActivableCard();
                break;
            case LEADERCARD_ACTIVATION_INDEX:
                view.showActivationLeaderCard(gson.fromJson(message.getMessage(), int[].class));
                break;
            case LEADERCARD_ALREADY_ACTIVATED:
                view.leaderCardAlreadyActivated(gson.fromJson(message.getMessage(), boolean.class));
                break;
            case LEADERCARD_ERROR_NOT_ENOUGH_RESOURCES:
                view.leaderCardNotEnoughResources();
                break;
            case LEADERCARD_ERROR_NOT_ENOUGH_COLOR_CARD_QUANTITY:
                view.leaderCardNotEnoughColorCardQuantity();
                break;
            case LEADERCARD_PERMISSION_TRY_AGAIN_ACTIVATE_CARD:
                view.askActivateAnotherLeaderCard();
                break;
            case LEADERCARD_ANOTHER_DISCARD:
                view.askAnotherDiscardLeaderCard();
                break;
            case LEADERCARD_END_ACTION:
                view.leaderCardEndAction(message.getMessage());
                break;
            case LEADERCARD_DISCARD_INDEX:
                view.leaderCardDiscardAction(gson.fromJson(message.getMessage(), int[].class));
                break;
            case PRODUCTION_OPTIONS:
                view.displayProductionAction(gson.fromJson(message.getMessage(), boolean[].class));
                break;
            case PRODUCTION_ACK:
                view.displayMessage(message.getMessage());
                view.callProductionAction();
                break;
            case PRODUCTION_ERROR_EMPTY_SLOT:
                view.displayMessage(message.getMessage());
                view.checkDevelopmentCardProductionSlot();
                break;
            case PRODUCTION_LEADERCARD_RESOURCE_INPUT:
                view.chooseLeaderCardProductionResourceType();
                break;
            case PRODUCTION_LEADERCARD_INDEX:
                view.chooseLeaderCardProductionIndex();
                break;
            case PRODUCTION_BASICPRODUCTION:
                view.chooseBasicProductionResourceType();
                break;
            case PURCHASE_CARD_LIST:
                view.chooseCardToPurchase(gson.fromJson(message.getMessage(), Integer[].class));
                break;
            case PURCHASE_CHOOSE_SLOT:
                view.chooseCardSlot(Arrays.asList(gson.fromJson(message.getMessage(), Integer[].class)));
                break;
            case MARKET_CHOICE:
                view.chooseMarketLine();
                break;
            case PURCHASED_RESOURCES:
                view.displayPurchasedResources(message.getMessage());
                break;
            case BLANKMARBLE_CHOICE:
                view.chooseBlankAbility(Integer.parseInt(message.getMessage()));
                break;
            case ARRANGED_RESOURCES:
                view.manageStores(Integer.parseInt(message.getMessage()));
                break;
            case DISCARDED_RESOURCES:
                view.displayDiscardedResources(message.getMessage());
                break;
            case ACTION_TOKEN:
                view.showActionToken(message.getMessage());
                break;
            case LORENZO_TRACK_INITIALIZATION:
                view.setLorenzoFaithTrack();
                break;
            case LORENZO_TRACK:
                view.updateLorenzoFaithTrack(message);
                break;
            case END_ACTION:
                view.setActionCompleted(true);
                break;
            case END_TURN:
                view.chooseInitialAction(true, false, new Message(new boolean[]{false, false}));
                break;
            case END_GAME:
                view.endGame(message.getMessage());
                break;
        }
    }

    /**
     * Method setView sets the type of View for this client (CLI/GUI)
     */
    public void setView() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the type of view you want to use: [cli/gui] > ");
        String view = scanner.nextLine();
        while (!view.equalsIgnoreCase("cli") && !view.equalsIgnoreCase("gui")) {
            System.out.print("Error! Please enter a valid input. [cli/gui] > ");
            view = scanner.nextLine();
        }
        if (view.equalsIgnoreCase("cli"))
            this.view = new CLI(this);
        else {
            System.out.println("GUI is starting ...");
            Application.launch(GUI.class);
        }
    }

    /**
     * Get the type of view of the client (CLI/GUI).
     *
     * @return the view of the client.
     */
    public View getView() {
        return view;
    }

    /**
     * Set the type of view of the client (CLI/GUI).
     *
     * @param view to be set
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Method sendMessage sends a Message to the Server or the EventController.
     *
     * @param message is the message to be sent.
     */
    public abstract void sendMessage(Message message);

}
