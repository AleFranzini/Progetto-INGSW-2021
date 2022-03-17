package it.polimi.ingsw.view.gui;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class PopupPlayerSetup extends Popup {

    private UtilsGUI utilsGUI = new UtilsGUI();
    private TextField username = utilsGUI.createTextField();
    private Button button = utilsGUI.createButton("Submit", 50, 250, 200, 325);
    private Label labelUsername = utilsGUI.createLabel("Insert username:");

    /**
     * Constructor of popup player setup.
     */
    PopupPlayerSetup() {
        super(400, 640);
        username.setLayoutX(350);
        username.setLayoutY(150);
        labelUsername.setLayoutY(140);

        anchorPane.getChildren().addAll(username, labelUsername);
        anchorPane.getChildren().add(button);
    }

    /**
     * Get select button.
     * @return select button.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Get username from text field.
     *
     * @return username.
     */
    public TextField getUsername() {
        return username;
    }

    /**
     * Set label.
     *
     * @param label string message.
     */
    public void setLabel(String label) {
        labelUsername.setText(label);
        labelUsername.setLayoutY(160);
        labelUsername.setLayoutX(180);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), labelUsername);
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.play();
    }
}
