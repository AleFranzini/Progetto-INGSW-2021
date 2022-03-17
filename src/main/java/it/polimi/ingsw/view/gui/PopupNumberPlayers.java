package it.polimi.ingsw.view.gui;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PopupNumberPlayers extends Popup {

    private UtilsGUI utilsGUI = new UtilsGUI();
    private ChoiceBox<Integer> selectNumPlayers;
    private Button button = utilsGUI.createButton("Submit", 50, 250, 200, 325);
    private Label labelPlayers = utilsGUI.createLabel("Select number of players:");

    /**
     * Constructor of popup number players
     */
    PopupNumberPlayers() {
        super(400, 640);
        selectNumPlayers = new ChoiceBox<>();
        for (int i = 1; i < 5; i++)
            selectNumPlayers.getItems().add(i);
        selectNumPlayers.setPrefWidth(150);
        selectNumPlayers.setLayoutX(450);
        selectNumPlayers.setLayoutY(150);
        labelPlayers.setLayoutY(140);
        anchorPane.getChildren().addAll(selectNumPlayers, labelPlayers);
        anchorPane.getChildren().add(button);
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
     * Get selected number of players.
     *
     * @return number of players.
     */
    public ChoiceBox<Integer> getSelectNumPlayers() {
        return selectNumPlayers;
    }

    /**
     * Set label.
     *
     * @param label string message.
     */
    public void setLabel(String label) {
        labelPlayers.setText(label);
        labelPlayers.setLayoutY(160);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), labelPlayers);
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.play();
    }
}
