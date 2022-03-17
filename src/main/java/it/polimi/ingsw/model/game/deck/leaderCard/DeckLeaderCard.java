package it.polimi.ingsw.model.game.deck.leaderCard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.commons.LoadClassAdapter;
import it.polimi.ingsw.model.commons.Resource;
import it.polimi.ingsw.model.commons.ResourceType;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class DeckLeaderCard
 *
 * @author Grugni Federico
 */
public class DeckLeaderCard {
    private LeaderCard[] deckLeaderCard;

    /**
     * Deck leader card constructor
     * Take card by LeaderCard.json
     */
    public DeckLeaderCard() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(LeaderCard.class, new LoadClassAdapter());
        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/json/LeaderCard.json")));
        this.deckLeaderCard = gson.create().fromJson(reader, LeaderCard[].class);
        shuffle();
    }

    /**
     * Get 4 LeaderCard from the deck
     *
     * @return first 4 leader card IDs of the deck and shift the leader card deck
     */
    public int[] getCard() {
        LeaderCard[] leaderCard = {deckLeaderCard[0], deckLeaderCard[1], deckLeaderCard[2], deckLeaderCard[3]};
        int[] IDs = new int[]{deckLeaderCard[0].getID(), deckLeaderCard[1].getID(), deckLeaderCard[2].getID(), deckLeaderCard[3].getID()};
        System.arraycopy(deckLeaderCard, 4, deckLeaderCard, 0, 12);
        System.arraycopy(leaderCard, 0, deckLeaderCard, 12, 4);
        return IDs;
    }

    /**
     * Shuffle the deck of LeaderCards
     */
    public void shuffle() {
        List<LeaderCard> listDevelopmentCard = Arrays.asList(deckLeaderCard);
        Collections.shuffle(listDevelopmentCard);
        deckLeaderCard = listDevelopmentCard.toArray(new LeaderCard[16]);
    }

    /**
     * Search leader card in deck, using ID like a key
     *
     * @param ID of the leader card to find
     * @return leader card of the id searched
     */
    public LeaderCard getCardByID(int ID) {
        int i = 0;
        while (i < 16 && deckLeaderCard[i].getID() != ID) {
            i++;
        }
        return deckLeaderCard[i];
    }

    /**
     * Set deck leader card
     *
     * @param deck to set
     */
    public void setDeckLeaderCard(LeaderCard[] deck) {
        this.deckLeaderCard = deck;
    }

    /**
     * Reset and deactivate leader card
     */
    public void resetDeckLeaderCard() {
        for (LeaderCard leaderCard : deckLeaderCard)
            leaderCard.setActivatedLeaderCard(false);
    }
}
