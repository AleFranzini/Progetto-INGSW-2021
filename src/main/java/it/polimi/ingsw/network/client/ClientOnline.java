package it.polimi.ingsw.network.client;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class ClientOnline is used when the player chose to play an online match.
 *
 * @author Franzini Alessandro, Galetti Filippo
 */
public class ClientOnline extends Client {
    private static Timer heartbeatTimer = null;
    private String serverIP;
    private int serverPort;
    private ServerConnection socket;

    public static void main(String[] args) {
        ClientOnline client = new ClientOnline();
        client.setView();
        if (client.getView() instanceof CLI)
            client.getView().setupProcess();
    }

    /**
     * Method startHeartbeat stops sending the ping message (heartbeat) to the server.
     */
    protected static void stopHeartbeat() {
        if (heartbeatTimer != null) {
            heartbeatTimer.cancel();
        }
    }

    /**
     * Method connectToServer instantiates a connection with the server.
     */
    public void connectToServer() {
        try {
            Socket server = new Socket(getServerIP(), getServerPort());
            server.setSoTimeout(20000);
            socket = new ServerConnection(server, this);
            Thread socketClientThread = new Thread(socket);
            socketClientThread.start();
            startHeartbeat();
        } catch (IOException e) {
        }
    }

    /**
     * This method close the socket connection and the client application.
     */
    public void close() {
        stopHeartbeat();
        socket.stop();
        System.exit(0);
    }

    /**
     * Method startHeartbeat sends a ping message (heartbeat) to the server to notify that it's still active.
     */
    private void startHeartbeat() {
        heartbeatTimer = new Timer();
        heartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new Message(MessageType.HEARTBEAT));
            }
        }, 1000, 10000);
    }

    /**
     * Method sendMessage sends a Message to the Server.
     *
     * @param message is the message to be sent.
     */
    public void sendMessage(Message message) {
        socket.sendMessage(message);
    }

    /**
     * Get the IP of the server.
     *
     * @return the IP of the server
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Set the IP of the server.
     *
     * @param serverIP to be set
     */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * Get the port of the server.
     *
     * @return the port of the server
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Set the port of the server.
     *
     * @param serverPort to be set
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Method handleNetworkMessage handles the received Message depending on the MessageType.
     *
     * @param message is the message received from the server
     */
    public void handleNetworkMessage(Message message) {
        switch (message.getMessageType()) {
            case ERROR_USERNAME -> {
                view.displayMessage(message.getMessage());
                view.insertUsername();
            }
            case LOGIN_FAIL -> {
                view.displayMessage(message.getMessage());
                close();
            }
            case LOGIN_SUCCESS -> {
                Message message1 = new Message(MessageType.LOGIN_SUCCESS, message.getMessage() + " connected!");
                view.displayMessage(message1.getMessage());
                setUsername(message.getMessage());
            }
            case CHOOSE_NUM_PLAYERS -> view.insertNumberOfPlayers();
            case CHECK_OLD_GAMES -> view.askRestoreGame(message.getMessage());
            default -> handleMessage(message);
        }
    }
}
