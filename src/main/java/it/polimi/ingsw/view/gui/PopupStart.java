package it.polimi.ingsw.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PopupStart extends Popup {

    private final UtilsGUI utilsGUI = new UtilsGUI();
    private final ImageView backgroundStart = new ImageView(new Image("img/punchboard/startGame.png"));
    private final Button button = utilsGUI.createButton("Start", 50, 250, 200, 325);

    /**
     * Constructor of popup start.
     */
    PopupStart() {
        super(400, 640);
        button.setMinHeight(80);
        button.setMinWidth(465);
        button.setLayoutX(140);
        button.setLayoutY(375);
        Label developers = utilsGUI.createLabel("Alessandro Franzini - Filippo Galetti - Federico Grugni");
        developers.setLayoutX(70);
        developers.setLayoutY(40);
        developers.setStyle("-fx-text-fill: rgb(247, 215, 164);\n" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.9), 10, 0.75, 0.0, 0.0);");
        anchorPane.getChildren().add(backgroundStart);
        anchorPane.getChildren().add(button);
        anchorPane.getChildren().add(developers);
    }

    /**
     * Get start button.
     *
     * @return start button.
     */
    public Button getButton() {
        return button;
    }
}
