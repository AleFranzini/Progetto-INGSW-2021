package it.polimi.ingsw.messages;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Class Message
 */
public class Message implements Serializable {
    private final static Gson gson = new Gson();
    private MessageType messageType;
    private String message;

    /**
     * Construct a message of a specified type.
     *
     * @param messageType is the type of the message
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Construct a message of no type with an associated string.
     *
     * @param message is the string of the message
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * Construct a message of no type with an associated object.
     *
     * @param message is the object of the message
     */
    public Message(Object message) {
        this.message = gson.toJson(message);
    }

    /**
     * Construct a message of a specified type with an associated object.
     *
     * @param messageType is the type of the message
     * @param message     is the object of the message
     */
    public Message(MessageType messageType, Object message) {
        this.messageType = messageType;
        this.message = gson.toJson(message);
    }

    /**
     * Construct a message of a specified type with an associated string.
     *
     * @param messageType is the type of message
     * @param message     is the string of the message
     */
    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    /**
     * Get the string of the message.
     *
     * @return the string of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the type of the message.
     *
     * @return the type of the message
     */
    public MessageType getMessageType() {
        return messageType;
    }
}
