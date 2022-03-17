package it.polimi.ingsw.network.server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.deck.developmentCard.DeckDevelopmentCard;
import it.polimi.ingsw.model.game.deck.leaderCard.DeckLeaderCard;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;
import it.polimi.ingsw.model.player.GameBoard;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.model.game.Game.getGame;

/**
 * Class Backup is used to save backups to manage client disconnections and the interruption of the server execution.
 *
 * @author Franzini Alessandro
 */
public class Backup {
    private static final Gson gson = new Gson();
    private static JsonArray jsonArray;
    private static JsonObject savedGameJsonObject;
    private static String date;
    private final static File dir = new File(System.getProperty("user.home") + "/Documents/MastersOfRenaissance");
    private final static File gameBackup = new File(dir.getPath() + "/gameBackup.json");

    /**
     * Method setJsonArray reads from the gameBackup file and sets the JsonArray
     */
    public static void setJsonArray() {
        JsonArray jsonArray = null;
        dir.mkdir();
        if (!gameBackup.exists()) {
            try {
                gameBackup.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Backup.jsonArray = new JsonArray();
        } else {
            try (JsonReader jsonReader = new JsonReader(new FileReader(gameBackup.getPath()))) {
                jsonArray = new Gson().fromJson(jsonReader, JsonArray.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonArray == null) jsonArray = new JsonArray();
            Backup.jsonArray = jsonArray;
        }
    }

    /**
     * Method setSavedGameJsonObject search for the current game in the JsonArray and sets the JsonObject
     */
    public static void setSavedGameJsonObject() {
        if (jsonArray != null) {
            for (JsonElement currentJsonElement : jsonArray) {
                String savedGameDate = gson.fromJson(currentJsonElement.getAsJsonObject().get("date"), new TypeToken<String>() {
                }.getType());
                if (savedGameDate.equals(date))
                    savedGameJsonObject = currentJsonElement.getAsJsonObject();
            }
            if (savedGameJsonObject != null)
                jsonArray.remove(savedGameJsonObject);
        }
    }

    /**
     * Method removeGame overwrites the gameBackup with the content of the JsonArray.
     */
    public static void removeGame() {
        setSavedGameJsonObject();
        try (Writer writer = new FileWriter(gameBackup, false)) {
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method saveGame writes in the gameBackup file the current game data.
     */
    public static void saveGame() {
        String date = new Date().toString();
        setSavedGameJsonObject();

        try (Writer writer = new FileWriter(gameBackup, false)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("date", date);
            jsonObject.add("game", JsonParser.parseString(gson.toJson(getGame())));
            jsonArray.add(jsonObject);
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDate(date);
    }

    /**
     * Method checkOldGames searches in the gameBackup file for a previous unfinished game with the same usernames as the one passed by param.
     *
     * @param usernames is a set of String which are the usernames of the connected clients.
     * @return a String containing the date/s of the previous unfinished game/s or null if there aren't.
     */
    public static String checkOldGames(Set<String> usernames) {
        setJsonArray();
        StringBuilder dateOldGames = new StringBuilder();
        if (jsonArray != null) {
            for (JsonElement jsonElement : jsonArray) {
                int checkUsername = 0;
                JsonArray oldPlayers = jsonElement.getAsJsonObject().get("game").getAsJsonObject().get("players").getAsJsonArray();
                if (usernames.size() != oldPlayers.size())
                    continue;
                for (int i = 0; i < oldPlayers.size(); i++) {
                    if (usernames.contains(oldPlayers.get(i).getAsJsonObject().get("username").getAsString()))
                        checkUsername++;
                }
                if (checkUsername == usernames.size())
                    dateOldGames.append(gson.fromJson(jsonElement.getAsJsonObject().get("date"), new TypeToken<String>() {
                    }.getType()).toString()).append("\n");
            }
            if (dateOldGames.length() == 0)
                return null;
            return dateOldGames.toString();
        }
        return null;
    }

    /**
     * Method restoreGame restores a Game from the data read in the JsonObject with the selected date.
     */
    public static void restoreGame() {
        setSavedGameJsonObject();
        JsonElement jsonElement = savedGameJsonObject.get("game");
        List<Player> players = restorePlayers(jsonElement);
        JsonObject game = jsonElement.getAsJsonObject();
        Game.getGame(players,
                game.get("currentPlayer").getAsInt(),
                gson.fromJson(game.get("deckDevelopmentCard"), DeckDevelopmentCard.class),
                gson.fromJson(game.get("market"), Market.class),
                new DeckLeaderCard(),
                game.get("numberOfPlayers").getAsInt(),
                game.get("lastTurn").getAsBoolean());
    }

    /**
     * Method restorePlayers restores from the JsonElement the list of players of the game that has to be restored.
     *
     * @param jsonElement is the JsonElement that contains the necessary data to restore the players.
     * @return the list of Players of the restored Game.
     */
    public static List<Player> restorePlayers(JsonElement jsonElement) {
        List<Player> playerList = new ArrayList<>();
        JsonArray players = jsonElement.getAsJsonObject().get("players").getAsJsonArray();
        for (int i = 0; i < players.size(); i++) {
            JsonObject player = players.get(i).getAsJsonObject();
            String username = player.get("username").getAsString();
            playerList.add(restorePlayer(username, player));
        }
        return playerList;
    }

    /**
     * Method restorePlayer restores a single Player from the JsonObject
     *
     * @param username is the username of the Player to restore.
     * @param player   is the JsonObject from which to get the data.
     * @return a new Player with all the data correctly set.
     */
    public static Player restorePlayer(String username, JsonObject player) {
        DeckLeaderCard deckLeaderCard = new DeckLeaderCard();

        boolean inkwell = player.get("inkwell").getAsBoolean();
        boolean initialResourcesAlreadyChosen = player.get("leaderCardsAlreadyChosen").getAsBoolean();
        boolean leaderCardsAlreadyChosen = player.get("leaderCardsAlreadyChosen").getAsBoolean();
        GameBoard gameBoard = gson.fromJson(player.get("gameBoard").getAsJsonObject(), GameBoard.class);
        JsonArray leaders = player.get("leaderCards").getAsJsonArray();
        List<LeaderCard> leaderCardList = new ArrayList<>();
        for (int j = 0; j < leaders.size(); j++) {
            leaderCardList.add(deckLeaderCard.getCardByID(leaders.get(j).getAsJsonObject().get("id").getAsInt()));
            leaderCardList.get(j).setActivatedLeaderCard(leaders.get(j).getAsJsonObject().get("activatedLeaderCard").getAsBoolean());
        }
        int victoryPoints = player.get("victoryPoints").getAsInt();
        return new Player(username, inkwell, initialResourcesAlreadyChosen, leaderCardsAlreadyChosen, gameBoard, leaderCardList, victoryPoints);
    }

    /**
     * Set date
     * @param date of chosen game
     */
    public static void setDate(String date) {
        Backup.date = date;
    }

}
