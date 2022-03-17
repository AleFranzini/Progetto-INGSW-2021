package it.polimi.ingsw.model.game.deck.actionToken;

import com.google.gson.*;
import it.polimi.ingsw.model.commons.LoadClassAdapter;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class DeckActionToken
 *
 * @author Franzini Alessandro
 */
public class DeckActionToken {
    private final ActionToken<?>[] deck;
    private int counter;

    /**
     * Constructor
     */
    public DeckActionToken() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(ActionToken.class, new LoadClassAdapter());
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/ActionToken.json"));
        this.deck = gson.create().fromJson(reader, ActionToken[].class);

        shuffle();
    }

    /**
     * Get Counter
     *
     * @return index of token
     */
    public int getCounter() {
        return this.counter;
    }

    /**
     * Increment Counter value by 1
     */
    public void incCounter() {
        this.counter++;
    }

    /**
     * Get ActionToken
     *
     * @param index position of the ActionToken in the deck
     * @return the ActionToken
     */
    public ActionToken<?> getActionToken(int index) {
        return deck[index];
    }

    /**
     * Shuffle the deck of ActionTokens
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            ActionToken<?> tmp = deck[index];
            deck[index] = deck[i];
            deck[i] = tmp;
        }
        counter = 0;
    }
}
