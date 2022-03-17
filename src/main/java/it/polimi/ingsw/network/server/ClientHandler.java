package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.EventController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static it.polimi.ingsw.network.server.Server.*;

/**
 * Class ClientHandler is used to handle the connection with a client socket.
 *
 * @author Franzini Alessandro
 */
public class ClientHandler implements Runnable {
    private final Socket client;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private boolean connected;
    private boolean waitingClient;
    private boolean restored;

    /**
     * Construct a client connection with a client socket.
     * @param client is the socket of the client
     * @throws IOException when there's issue with the socket
     */
    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.connected = false;
        this.waitingClient = false;
        this.restored = false;
        outputStream = new ObjectOutputStream(client.getOutputStream());
        inputStream = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        try {
            if (Server.getNumberOfPlayers() == 0 || Server.getNumberOfPlayers() > Server.getConnectedClients().size() || Server.getDisconnectedClientMap().size() > 0)
                while (!connected && !getClient().isClosed())
                    clientConnection();
            else {
                sendMessage(new Message(MessageType.LOGIN_FAIL, "ERROR! All clients have been already selected!"));
                client.close();
            }
            if (getClient().isClosed())
                return;
            if (!isRestored()) {
                if (Server.getNumberOfPlayers() == Server.getConnectedClients().size()) {
                    // only the last client enter here and notifies all the others
                    // before starting the game we check if there are saved games
                    if (Backup.checkOldGames(Server.getClientMap().keySet()) == null) {
                        GameController.getGameController().startGame();
                    } else {
                        Server.sendMessageToClient(Server.getClientHandlerFromUsername(Server.getFirstPlayerUsername()), new Message(MessageType.CHECK_OLD_GAMES, Backup.checkOldGames(Server.getClientMap().keySet())));
                    }
                    Server.setGameStarted();
                } else
                    Server.sendMessageToAllClients(new Message(MessageType.DISPLAY_MESSAGE, "Please wait until all the players are connected..."));
            }
            while (isConnected()) {
                EventController.getEventController().handleMessage(getMessageFromInputStream());
            }
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof SocketTimeoutException) {
                System.out.println("TIMEOUT EXPIRED! This client is unreachable.");
            }
            connected = false;
            System.out.println("Client " + client.getInetAddress() + " connection dropped");
        }
    }

    /**
     * Method sendMessage sends a Message to the current client.
     *
     * @param message is the message to be sent.
     */
    public void sendMessage(Message message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            try {
                outputStream.close();
            } catch (IOException ioException) {
            }
        }
    }

    /**
     * Method clientConnection sets up a new connection between the server and a new client.
     *
     * @throws IOException            is thrown if an error occurred when trying to read the received message.
     * @throws ClassNotFoundException is thrown if an error occurred when trying to read the received message.
     */
    private void clientConnection() throws IOException, ClassNotFoundException {
        Message received;
        do {
            if (waitingClient) {
                connected = true;
                return;
            }
            received = getMessageFromInputStream();
        } while (received == null || received.getMessageType() == MessageType.HEARTBEAT);

        if (received.getMessageType() == MessageType.PLAYER_DISCONNECTION) {
            if (!getWaitingClients().isEmpty() && received.getMessage().matches("-?\\d+(\\d+)?")) {
                removeWaitingClient(this);
                List<String> usernameList = new ArrayList<>(getWaitingClientUsername());
                removeWaitingClientUsername(usernameList.get(Integer.parseInt(received.getMessage())));
            } else {
                removeFromClientMap(this);
            }
            closeConnection();
            return;
        }
        if (received.getMessageType() == MessageType.LOGIN) {
            String username = received.getMessage();
            if (Server.getDisconnectedClientMap().size() > 0 && Server.isGameStarted()) {
                if (username.isBlank() || !Server.getClientMap().containsKey(username)) {
                    sendMessage(new Message(MessageType.ERROR_USERNAME, "This username is not valid. Please try a new one:"));
                    connected = false;
                } else {
                    if (Server.getClientHandlerFromUsername(username).getClient().getInetAddress().equals(client.getInetAddress())) {
                        Server.replaceClientHandler(username, this);
                        System.out.println("Reconnected to '" + username + "', " + client.getInetAddress());
                        sendMessage(new Message(MessageType.LOGIN_SUCCESS, username));
                        connected = true;
                        restored = true;
                        Server.restorePlayer(username);
                    }
                }
                return;
            }
            if (username.isBlank()) {
                sendMessage(new Message(MessageType.ERROR_USERNAME, "This username is not valid. Please try a new one:"));
                connected = false;
                return;
            }
            if (Server.getClientMap().containsKey(username) || Server.getWaitingClientUsername().contains(username)) {
                sendMessage(new Message(MessageType.ERROR_USERNAME, "This username is already taken. Please try a new one:"));
                connected = false;
                return;
            }
            if (Server.getNumberOfPlayers() == 0 && Server.getConnectedClients().size() == 1) {
                sendMessage(new Message(MessageType.DISPLAY_MESSAGE, "Waiting for the first client to chose the number of players of this match..."));
                Server.addWaitingClient(this);
                Server.addWaitingClientUsername(username);
                waitingClient = true;
                return;
            }
            Server.addClientMapEntry(username, this);
            System.out.println("Connected to " + username + ", " + client.getInetAddress());
            sendMessage(new Message(MessageType.LOGIN_SUCCESS, username));
            if (Server.getConnectedClients().size() == 1) {
                setFirstPlayerUsername(username);
                sendMessage(new Message(MessageType.CHOOSE_NUM_PLAYERS));
                return;
            }
            connected = true;
        }

        if (received.getMessageType() == MessageType.CHOOSE_NUM_PLAYERS) {
            Server.setNumberOfPlayers(Integer.parseInt(received.getMessage()));
            connected = true;
            Deque<ClientHandler> waitingClients = Server.getWaitingClients();
            Deque<String> usernames = Server.getWaitingClientUsername();
            while (waitingClients.peekFirst() != null) {
                if (Server.getNumberOfPlayers() == Server.getConnectedClients().size())
                    break;
                System.out.println("Connected to " + usernames.peekFirst() + ", " + waitingClients.peekFirst().client.getInetAddress());
                Server.addClientMapEntry(usernames.peekFirst(), waitingClients.peekFirst());
                Server.sendMessageToClient(waitingClients.peekFirst(), new Message(MessageType.LOGIN_SUCCESS, usernames.peekFirst()));
                Server.removeWaitingClient(waitingClients.peekFirst());
                Server.removeWaitingClientUsername(usernames.peekFirst());
            }

            if (!waitingClients.isEmpty())
                for (ClientHandler clientHandler : waitingClients)
                    Server.sendMessageToClient(clientHandler, new Message(MessageType.LOGIN_FAIL, "Sorry, all clients have already been selected! Retry next time!"));

        }
    }

    /**
     * Method getMessageFromInputStream returns the Message read from InputStream.
     *
     * @return the message read from the InputStream.
     */
    public synchronized Message getMessageFromInputStream() {
        try {
            return (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnection ...");
            if (this.waitingClient) {
                int position = 0;
                for (ClientHandler clientHandler : getWaitingClients()) {
                    if (clientHandler.equals(this))
                        break;
                    position++;
                }
                connected = false;
                return new Message(MessageType.PLAYER_DISCONNECTION, position);
            } else
                return new Message(MessageType.PLAYER_DISCONNECTION, Server.getUsernameFromClientHandler(this));
        }
    }

    /**
     * Get the socket of the client.
     * @return the socket of the client.
     */
    public synchronized Socket getClient() {
        return client;
    }

    /**
     * Method closeConnection closes the socket connection and set the value of connected false.
     */
    protected synchronized void closeConnection() {
        try {
            client.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        connected = false;
    }

    /**
     * Tell if the client is connected to the server via the socket.
     * @return true if the client is connected, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Tell if the client restored the connection to the server after being disconnected.
     * @return true if the connection is restored, false otherwise.
     */
    public boolean isRestored() {
        return restored;
    }
}