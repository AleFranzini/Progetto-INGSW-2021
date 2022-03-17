package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.EventController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Class Server
 *
 * @author Franzini Alessandro
 */
public final class Server {
    public static final int MIN_PORT = 1024;
    public static final int MAX_PORT = 65535;
    private static int numberOfPlayers = 0;
    private static Deque<ClientHandler> waitingClients = new LinkedList<>();
    private static HashMap<String, ClientHandler> clientMap = new HashMap<>();
    private static Deque<String> waitingClientUsername = new LinkedList<>();
    private static String firstPlayerUsername;
    private boolean active;
    private static boolean gameStarted;

    /**
     * Construct a server and set it active.
     */
    private Server() {
        active = true;
        gameStarted = false;
        startHeartbeat();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert Port number:");
        int port = scanner.nextInt();
        while (port < MIN_PORT || port > MAX_PORT) {
            System.out.println("Value must be between " + MIN_PORT + " and " + MAX_PORT + ". Please, try again:");
            port = scanner.nextInt();
        }
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            System.out.println("Waiting for connections...");
        } catch (IOException e) {
            System.out.println("Cannot open the server socket!");
            System.exit(1);
            return;
        }

        Server server = new Server();
        if (System.getProperty("debug") != null && System.getProperty("debug").equals("true"))
            GameController.getGameController().setDebugMode();
        while (server.active) {
            try {
                Socket client = socket.accept();
                client.setSoTimeout(20000);
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Connection dropped!");
            }
        }
    }

    /**
     * Method sendMessageToAllClients sends a Message to all the connected clients.
     *
     * @param message is the message to be sent.
     */
    public static void sendMessageToAllClients(Message message) {
        for (ClientHandler client : Server.getConnectedClients())
            client.sendMessage(message);
    }

    /**
     * Method sendMessageToClient sends a Message to a specific client.
     *
     * @param client  is the client to send the message to.
     * @param message is the message to be sent.
     */
    public static void sendMessageToClient(ClientHandler client, Message message) {
        client.sendMessage(message);
    }

    /**
     * Get the list of clients waiting to be accepted into the match.
     *
     * @return the list of clients as a linked list
     */
    public static Deque<ClientHandler> getWaitingClients() {
        return waitingClients;
    }

    /**
     * Method addWaitingClient adds a client to the list of waiting clients.
     *
     * @param clientHandler the client that has to be added.
     */
    public static void addWaitingClient(ClientHandler clientHandler) {
        waitingClients.addLast(clientHandler);
    }

    /**
     * Method removeWaitingClient removes a client from the list of waiting clients.
     *
     * @param clientHandler the client that has to be removed.
     */
    public static void removeWaitingClient(ClientHandler clientHandler) {
        waitingClients.removeIf(clientHandler1 -> ((clientHandler1).equals((clientHandler))));
    }

    /**
     * Method getConnectedClients returns a copy of the list of ClientHandler that are present in the clientMap.
     *
     * @return the list of connected ClientHandlers.
     */
    public static List<ClientHandler> getConnectedClients() {
        return new ArrayList<>(clientMap.values());
    }

    /**
     * Method getDisconnectedClientMap search in the clientMap if there are any disconnected ClientHandler.
     *
     * @return the HashMap containing the disconnected ClientHandlers and the relative username.
     */
    public static HashMap<String, ClientHandler> getDisconnectedClientMap() {
        HashMap<String, ClientHandler> disconnectedClientMap = new HashMap();
        for (ClientHandler clientHandler : clientMap.values())
            if (!clientHandler.isConnected())
                disconnectedClientMap.put(getUsernameFromClientHandler(clientHandler), clientHandler);
        return disconnectedClientMap;
    }

    /**
     * Get the map of the clients playing the match.
     *
     * @return a map of username and clients
     */
    public static HashMap<String, ClientHandler> getClientMap() {
        return new HashMap<>(Server.clientMap);
    }

    /**
     * Add an element to the map of usernames and clients.
     *
     * @param username      is the username of the new element
     * @param clientHandler is the client of the new element
     */
    public static void addClientMapEntry(String username, ClientHandler clientHandler) {
        clientMap.put(username, clientHandler);
    }

    public static void removeFromClientMap(ClientHandler clientHandler) {
        clientMap.remove(getUsernameFromClientHandler(clientHandler), clientHandler);
    }

    /**
     * Replace the client associated to a username with a new one.
     *
     * @param username         is the key of the client to be replaced
     * @param newClientHandler is the new client of the entry
     */
    public static void replaceClientHandler(String username, ClientHandler newClientHandler) {
        clientMap.replace(username, newClientHandler);
    }

    /**
     * Method getPlayers creates a new list of Players from the usernames of the connected clients.
     *
     * @return the list of players ready to play the Game.
     */
    public static List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (String username : clientMap.keySet()) {
            players.add(new Player(username));
        }
        return players;
    }

    /**
     * Method getClientHandlerFromUsername
     *
     * @param username is a String that represents the key from which the Server gets the ClientHandler in the map.
     * @return the ClientHandler linked to the username.
     */
    public static ClientHandler getClientHandlerFromUsername(String username) {
        return clientMap.get(username);
    }

    /**
     * Method getUsernameFromClientHandler
     *
     * @param clientHandler is the ClientHandler that represents the value from which the Server gets the username in the map.
     * @return the username linked to the ClientHandler.
     */
    public static String getUsernameFromClientHandler(ClientHandler clientHandler) {
        for (String key : clientMap.keySet())
            if (clientHandler.equals(clientMap.get(key)))
                return key;
        return null;
    }

    /**
     * Get the list of usernames waiting to be accepted into the match.
     *
     * @return the list of usernames as a linked list
     */
    public static Deque<String> getWaitingClientUsername() {
        return waitingClientUsername;
    }

    /**
     * Method addWaitingClientUsername adds the username of the client to the list of waiting clients usernames.
     *
     * @param username the username of the client that has to be added.
     */
    public static void addWaitingClientUsername(String username) {
        waitingClientUsername.addLast(username);
    }

    /**
     * Method removeWaitingClientUsername removes the username of the client from the list of waiting clients usernames.
     *
     * @param username the username of the client that has to be removed.
     */
    public static void removeWaitingClientUsername(String username) {
        waitingClientUsername.removeIf(username1 -> ((username1).equals((username))));
    }

    /**
     * Get the number of players in the game.
     *
     * @return the number of players
     */
    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Set the number of players in the game.
     *
     * @param number is the number of players
     */
    public static void setNumberOfPlayers(int number) {
        numberOfPlayers = number;
    }

    /**
     * Get the username of the first player to connect.
     *
     * @return the username as a string
     */
    public static String getFirstPlayerUsername() {
        return firstPlayerUsername;
    }

    /**
     * Set the username of the first player to connect.
     *
     * @param username to be set as first player
     */
    public static void setFirstPlayerUsername(String username) {
        firstPlayerUsername = username;
    }

    /**
     * Method close is used to close all the client connections and the server.
     */
    public static void close() {
        for (ClientHandler client : Server.getConnectedClients())
            client.closeConnection();
        System.out.println("Closing server ...");
        System.exit(0);
    }

    /**
     * Method removePlayer closes the client connection and removes the player from the game.
     *
     * @param username is a String that represents the username of the Player to remove.
     */
    public static void removePlayer(String username) {
        if (isGameStarted()) {
            Game.getGame().getPlayerByUsername(username).setDisconnected(true);
            if (GameController.getGameController().getTurn().getCurrentPlayer().getUsername().equals(username))
                GameController.getGameController().nextTurn();
            getClientHandlerFromUsername(username).closeConnection();
        } else {
            if (!waitingClients.isEmpty()) {
                List<ClientHandler> clientHandlerList = new ArrayList<>(waitingClients);
                removeWaitingClient(clientHandlerList.get(Integer.parseInt(username)));
                List<String> usernameList = new ArrayList<>(waitingClientUsername);
                removeWaitingClientUsername(usernameList.get(Integer.parseInt(username)));
            } else if (getConnectedClients().size() > 0) {
                getClientHandlerFromUsername(username).closeConnection();
                removeFromClientMap(getClientHandlerFromUsername(username));
            }
        }
    }

    /**
     * Method restorePlayer restores a player from the disconnectionsBackup and adds the player to the current game.
     *
     * @param username is a String that represents the username of the Player to restore.
     */
    public static void restorePlayer(String username) {
        Player player = Game.getGame().getPlayerByUsername(username);
        player.setDisconnected(false);
        EventController.getEventController().updatePlayer(getClientHandlerFromUsername(username), player);
    }

    /**
     * Method startHeartbeat sends a ping message (heartbeat) to all clients to notify that it's still active.
     */
    private void startHeartbeat() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<ClientHandler> copyConnectedClients = new ArrayList<>(clientMap.values());
                for (ClientHandler client : copyConnectedClients)
                    if (client != null) {
                        client.sendMessage(new Message(MessageType.HEARTBEAT));
                    }
            }
        }, 1000, 10000);
    }

    /**
     * Is game started.
     *
     * @return true if game is started,
     * false otherwise.
     */
    public static boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Set game started to true.
     */
    public static void setGameStarted() {
        gameStarted = true;
    }
}