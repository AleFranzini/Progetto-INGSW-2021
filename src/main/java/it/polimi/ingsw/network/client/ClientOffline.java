package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.EventController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

import javax.management.InstanceAlreadyExistsException;

/**
 * Class ClientOffline is used when the user chose to play offline (only for Single Player modality).
 *
 * @author Franzini Alessandro, Galetti Filippo
 */
public class ClientOffline extends Client {

    private EventController eventController;

    {
        try {
            eventController = EventController.getEventController(this);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct an empty client offline.
     */
    public ClientOffline() {
    }

    public static void main(String[] args) {
        ClientOffline client = new ClientOffline();
        if (System.getProperty("debug") != null && System.getProperty("debug").equals("true"))
            GameController.getGameController().setDebugMode();
        client.setView();
        client.getView().insertUsername();
        client.start();
    }

    /**
     * This method starts a new game offline.
     */
    public void start() {
        GameController.getGameController().startOfflineGame();
    }

    /**
     * Method sendMessage sends a Message to the EventController.
     *
     * @param message is the message to be sent.
     */
    public void sendMessage(Message message) {
        handleOfflineMessage(message);
    }

    /**
     * Method handleOfflineMessage handles the message of type LOGIN and sends all the others to the EventController
     *
     * @param message is the message to be sent.
     */
    public void handleOfflineMessage(Message message) {
        if (message.getMessageType() == MessageType.LOGIN) {
            setUsername(message.getMessage());
            Game.getGame().addPlayer(new Player(message.getMessage()));
        } else {
            eventController.handleMessage(message);
        }
    }
}
