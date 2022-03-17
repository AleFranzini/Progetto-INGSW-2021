package it.polimi.ingsw.model.player;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.commons.LoadClassAdapter;
import it.polimi.ingsw.model.game.deck.leaderCard.LeaderCard;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import static it.polimi.ingsw.controller.EventController.getEventController;
import static it.polimi.ingsw.controller.GameController.getGameController;

/**
 * Class FaithTrack
 *
 * @author Grugni Federico
 */
public class FaithTrack {
    private final PopeTiles[] popeTilesList;
    private final int[] faithTrackPoints = new int[]{1, 2, 4, 6, 9, 12, 16, 20};
    private int[] tilesStateList;
    private int faithMarkerPosition;
    private int victoryPointsPopeTiles;

    /**
     * faith track constructor
     *
     * create popetiles zone by using a json file and initialize the track
     */
    public FaithTrack() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(LeaderCard.class, new LoadClassAdapter());
        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/json/PopeTiles.json")));
        this.popeTilesList = gson.create().fromJson(reader, PopeTiles[].class);
        //popeTiles initialization
        tilesStateList = new int[]{0, 0, 0};
        this.faithMarkerPosition = 0;
        this.victoryPointsPopeTiles = 0;

    }

    /**
     * get the position of the faith marker on the faith track
     *
     * @return the position of the faith marker
     */
    public int getFaithMarkerPosition() {
        return faithMarkerPosition;
    }

    /**
     * get faith marker points
     *
     * @return the points of player position
     */
    public int getFaithMarkerPoints() {
        if (faithMarkerPosition == 0) {
            return 0;
        }
        return faithTrackPoints[faithMarkerPosition / 3 - 1];
    }

    /**
     * add the victory points assigned by the pope tiles
     */
    public int getVictoryPointsPopeTiles() {
        for (PopeTiles popeTiles : popeTilesList) {
            if (popeTiles.isPapalFavor()) {
                victoryPointsPopeTiles += popeTiles.getVictoryPoints();
            }
        }
        return victoryPointsPopeTiles;
    }

    /**
     * update the list which contains the state of the pope tiles, the list is used to update the view
     */
    private void updateTilesStateList() {
        for (int i = 0; i < 3; i++) {
            if (!popeTilesList[i].isActivable()) {
                if (popeTilesList[i].isPapalFavor()) {
                    tilesStateList[i] = 1;
                } else {
                    tilesStateList[i] = -1;
                }
            }
        }
    }

    /**
     * get tiles state list
     *
     * @return list of the state of pope tiles
     *         0 nothing
     *         1 activated
     *         -1 not activated
     */
    public int[] getTilesStateList() {
        return tilesStateList;
    }

    /**
     * get pope tiles list
     *
     * @return pope tiles list of the faith track
     */
    public PopeTiles[] getPopeTilesList() {
        return popeTilesList;
    }

    /**
     * move faith marker
     *
     * @param quantity number of faith point
     *                 return true if pope tiles has been activated
     *                 false otherwise
     */
    public void moveFaithMarker(int quantity) {
        faithMarkerPosition += quantity;
        if (faithMarkerPosition > 24)
            faithMarkerPosition = 24;
        if (getFaithMarkerPosition() == 8 || getFaithMarkerPosition() == 16 || getFaithMarkerPosition() >= 24) {
            checkPapalFavor();
            getEventController().sendPapalReport();
        }
    }

    /**
     * check if pope tiles can be activated
     * this method could be called by faith track of player that activate the papalReport
     * or by controller, to check for all players if they are in activation zone of papal reports
     */
    public void checkPapalFavor() {
        int i = 0;
        while (i < 2 && !popeTilesList[i].isActivable()) {
            i++;
        }
        if (getFaithMarkerPosition() >= popeTilesList[i].getMinRange() &&
                getFaithMarkerPosition() <= popeTilesList[i].getMaxRange()) {
            popeTilesList[i].setPapalFavor(true);
        }
        popeTilesList[i].setActivable(false);

        updateTilesStateList();
    }
}
