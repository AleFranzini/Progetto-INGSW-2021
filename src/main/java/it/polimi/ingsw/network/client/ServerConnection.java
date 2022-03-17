package it.polimi.ingsw.network.client;

import it.polimi.ingsw.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class ServerConnection is used to handle the connection with a server socket.
 *
 * @author Franzini Alessandro, Galetti Filippo
 */
public class ServerConnection implements Runnable {
    private final Socket server;
    private final ClientOnline client;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private static final int SOCKET_TIMEOUT = 15000;

    /**
     * Construct a server connection with a server socket.
     *
     * @param server is the socket of the server
     * @param client is the client to connect to the server socket
     * @throws IOException if there's issue with the socket
     */
    public ServerConnection(Socket server, ClientOnline client) throws IOException {
        this.server = server;
        this.client = client;
        outputStream = new ObjectOutputStream(server.getOutputStream());
        inputStream = new ObjectInputStream(server.getInputStream());

    }

    @Override
    public void run() {
        client.getView().insertUsername();
        while (true) {
            readMessage();
        }
    }

    /**
     * Method stop closes the socket connection.
     */
    public synchronized void stop() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method sendMessage sends a Message to the Server.
     *
     * @param message is the message to be sent.
     */
    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\n\nThe server disconnected, if you want to restore the match \ncreate another match with the same username and players!");
            disconnect();
        }
    }

    /**
     * Method readMessage reads a Message received from the Server and calls an external method to handle it.
     */
    public void readMessage() {
        try {
            Message received = (Message) inputStream.readObject();
            client.handleNetworkMessage(received);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected!");
            disconnect();
        }
    }

    /**
     * Method disconnect closes the client online application.
     */
    public void disconnect() {
        client.close();
    }

}
