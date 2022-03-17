package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.FaithTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackTest {
    private FaithTrack faithTrack;

    @BeforeEach
    public void testSetup() {
        this.faithTrack = new FaithTrack();
    }

    @Test
    public void testGetFaithMarkerPosition() {
    }

    @Test
    public void testGetVictoryPointsPopeTiles() {
    }

    @Test
    public void testAddVictoryPointsPopeTiles() {
    }

    @Test
    public void testMoveFaithMarker() {
        try {
            faithTrack.moveFaithMarker(1);
            assertEquals(faithTrack.getFaithMarkerPosition(), 1);
            /* test pope tile 1 */
            faithTrack.moveFaithMarker(7);
            assertEquals(faithTrack.getFaithMarkerPosition(), 8);
            /* test pope tile 2 */
            faithTrack.moveFaithMarker(8);
            assertEquals(faithTrack.getFaithMarkerPosition(), 16);
            /* test pope tile 3 */
            faithTrack.moveFaithMarker(8);
            assertEquals(faithTrack.getFaithMarkerPosition(), 24);
            /* test faith marker over 24 */
            faithTrack.moveFaithMarker(2);
            assertEquals(faithTrack.getFaithMarkerPosition(), 24);
        } catch (NullPointerException e) {

        }
    }

    @Test
    public void testCheckPapalFavor() {
        try {
            /* faith marker in papal report zone */
            /* for when another player activate papal report */
            assertFalse(faithTrack.getPopeTilesList()[0].isPapalFavor());
            faithTrack.moveFaithMarker(5);
            assertEquals(faithTrack.getFaithMarkerPosition(), 5);
            faithTrack.checkPapalFavor();
            assertTrue(faithTrack.getPopeTilesList()[0].isPapalFavor());
            assertFalse(faithTrack.getPopeTilesList()[0].isActivable());
            /* faith marker in papal report cells */
            /* for player that activate papal report, set papal favor and activable*/
            assertFalse(faithTrack.getPopeTilesList()[1].isPapalFavor());
            faithTrack.moveFaithMarker(11);
            assertEquals(faithTrack.getFaithMarkerPosition(), 16);
            assertTrue(faithTrack.getPopeTilesList()[1].isPapalFavor());
            assertFalse(faithTrack.getPopeTilesList()[1].isActivable());
            /* faith marker not in papal report zone */
            assertFalse(faithTrack.getPopeTilesList()[2].isPapalFavor());
            faithTrack.moveFaithMarker(2);
            assertEquals(faithTrack.getFaithMarkerPosition(), 18);
            assertFalse(faithTrack.getPopeTilesList()[2].isPapalFavor());
            assertTrue(faithTrack.getPopeTilesList()[2].isActivable());
        } catch (NullPointerException e) {

        }
    }
}