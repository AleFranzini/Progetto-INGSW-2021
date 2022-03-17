package it.polimi.ingsw.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PopupGetLeaderCard extends Popup {
    private final UtilsGUI utilsGUI = new UtilsGUI();
    private final Button button;
    private final List<ImageView> card = new ArrayList<>();
    private int[] indexes = new int[4];
    private boolean[] selectedCard = new boolean[]{false, false, false, false};

    /**
     * Constructor of popup leader card
     */
    public PopupGetLeaderCard() {
        super(500, 800);
        button = utilsGUI.createButton("Select card", 50, 250, 800 - 250 - 50, 425);
        anchorPane.getChildren().add(button);
    }

    /**
     * Insert two leader card from initial four.
     *
     * @param indexes IDs of four leader card.
     */
    public void insertLeaderCard(int[] indexes) {
        String stringUrl;
        this.indexes = indexes;
        Label label = utilsGUI.createLabel("Select two leader from these four:");
        label.setLayoutX(100);
        label.setLayoutY(425);
        label.setStyle(
                "    -fx-font-weight: bold;\n" +
                        "    -fx-effect: dropshadow(three-pass-box, #d7ba10, 0.3, 0.5, 0, 0);\n" +
                        "    -fx-text-fill: #ffffff;");
        anchorPane.getChildren().add(label);
        for (int i = 0; i < indexes.length; i++) {
            int finalI = i;
            stringUrl = "img/front/LeaderCard-" + indexes[i] + ".png";
            Image image = new Image(stringUrl);
            card.add(new ImageView(image));
            card.get(i).setStyle("-fx-background-image: url(" + stringUrl + ");\n" +
                    "    -fx-background-size: 100% 100%;\n" +
                    "    -fx-background-repeat: no-repeat;\n" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);"
            );
            card.get(i).setFitHeight(image.getHeight() / 3);
            card.get(i).setFitWidth(image.getWidth() / 3);
            card.get(i).setLayoutX(50 + (30 + image.getWidth() / 3) * i);
            card.get(i).setLayoutY(500 * 0.5 - image.getHeight() / 6);
            card.get(finalI).setOpacity(0.70);
            card.get(i).setOnMouseEntered(event -> {
//                card.get(finalI).setOpacity(0.75);

                card.get(finalI).setFitHeight(image.getHeight() / 3 + 10);
                card.get(finalI).setFitWidth(image.getWidth() / 3 + 10);
            });
            card.get(i).setOnMouseClicked(actionEvent -> {
                if (getNumberSelectedCard() < 2 || selectedCard[finalI]) {
                    if (selectedCard[finalI]) {
                        selectedCard[finalI] = false;
                        card.get(finalI).setOpacity(0.70);
                    } else {
                        selectedCard[finalI] = true;
                        card.get(finalI).setOpacity(1);
                    }
                }
            });
            card.get(i).setOnMouseExited(event -> {
                card.get(finalI).setFitHeight(image.getHeight() / 3);
                card.get(finalI).setFitWidth(image.getWidth() / 3);

            });
        }
        anchorPane.getChildren().addAll(card);
    }

    /**
     * Get select button.
     *
     * @return select button.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Take the two selected leader card.
     *
     * @return IDs of two selected card.
     */
    public int[] getSelectedIndex() {
        int[] index = new int[2];
        int count = 0;

        for (int i = 0; i < 4; i++) {
            if (selectedCard[i]) {
                index[count] = indexes[i];
                count++;
            }
        }
        return index;
    }

    /**
     * Get the number of selected card.
     *
     * @return number of selected card.
     */
    public int getNumberSelectedCard() {
        int count = 0;
        for (boolean bool : selectedCard)
            if (bool)
                count++;
        return count;
    }
}
