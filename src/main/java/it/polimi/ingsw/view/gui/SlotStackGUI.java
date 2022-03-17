package it.polimi.ingsw.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotStackGUI {
    private GameBoardGUI gameBoardGUI;
    private List<ImageView> slots = new ArrayList<>(3);
    private List<String> topCardsUrl;
    private List<Integer> slotsCounter;
    private List<Boolean> activatedProduction;

    public SlotStackGUI(GameBoardGUI gameBoardGUI) {
        this.gameBoardGUI = gameBoardGUI;
        slots.add(new ImageView(new Image("img/punchboard/background.png")));
        slots.add(new ImageView(new Image("img/punchboard/background.png")));
        slots.add(new ImageView(new Image("img/punchboard/background.png")));
        addImageContainers();
        slotsCounter = new ArrayList<>(Arrays.asList(0, 0, 0));
        topCardsUrl = new ArrayList<>(Arrays.asList(" ", " ", " "));
        activatedProduction = new ArrayList<>(Arrays.asList(false, false, false));
    }

    public void addCard(int slot, ImageView card) {
        topCardsUrl.set(slot, card.getImage().getUrl());
        card.setLayoutX(455 + slot * 227);
        card.setLayoutY(460 - slotsCounter.get(slot) * 65);
        gameBoardGUI.gameboard.getChildren().add(card);
        slotsCounter.set(slot, slotsCounter.get(slot) + 1);
    }

    public void updateSlotStack(List<ImageView> list) {
        for (int i = 0; i < 3; i++) {
            if (list.get(i) != null) {
                if (!list.get(i).getImage().getUrl().equals(topCardsUrl.get(i))) {
                    addCard(i, list.get(i));
                }
            }
        }
    }

    public List<ImageView> getSlots() {
        return slots;
    }

    private void addImageContainers() {
        int i = 0;
        for (ImageView slot : slots) {
            slot.setOpacity(0);
            slot.setFitHeight(400);
            slot.setFitWidth(200);
            slot.setLayoutX(444 + i * 222);
            slot.setLayoutY(310);
            gameBoardGUI.gameboard.getChildren().add(slot);
            i++;
        }
    }

    public void setImageContainersToTop() {
        for (ImageView slot : slots) {
            slot.toFront();
        }
    }

    /**
     * Check if all the development cards are in production.
     * @return true if there is at least a card yet to be activated, false otherwise
     */
    public boolean getActivableCardProduction() {
        for (int i = 0; i < 3; i++) {
            if(!getActivatedProduction(i) && getSlotsCounter(i) != 0)
                return true;
        }
        return false;
    }

//    [1,1,0]
//    [true, true, false]

    public int getSlotsCounter(int slot) {
        return slotsCounter.get(slot);
    }

    public void setActivatedProduction(int slot) {
        activatedProduction.set(slot, true);
    }

    public boolean getActivatedProduction(int slot) {
        return activatedProduction.get(slot);
    }

    public List<Boolean> getActivatedProduction() {
        return activatedProduction;
    }

    public void resetActivatedProductions() {
        activatedProduction = new ArrayList<>(Arrays.asList(false, false, false));
    }

    public List<Integer> getSlotsCounter() {
        return slotsCounter;
    }
}
