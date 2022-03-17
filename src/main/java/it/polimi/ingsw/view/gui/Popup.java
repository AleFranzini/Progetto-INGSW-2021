package it.polimi.ingsw.view.gui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;

public class Popup extends Stage {
    private UtilsGUI utilsGUI = new UtilsGUI();
    protected VBox vBox = new VBox();
    protected AnchorPane anchorPane = new AnchorPane();

    /**
     * Constructor of popup.
     *
     * @param height
     * @param width
     */
    Popup(int height, int width) {
        super();
        buildStage(height, width);
        buildPopup();
    }

    /**
     * Build stage.
     *
     * @param height of popup
     * @param width of popup
     */
    private void buildStage(int height, int width) {
        utilsGUI.getLogo().setX((width - 190) * 0.5);
        utilsGUI.getLogo().setFitHeight(height * 0.24);
        utilsGUI.getLogo().setFitWidth(190);
        utilsGUI.getBackgroundBeige().setFitWidth(width);
        utilsGUI.getBackgroundBeige().setFitHeight(height);
        utilsGUI.getBackgroundBlueUp().setFitWidth(width);
        utilsGUI.getBackgroundBlueUp().setFitHeight(height * 0.24);
        utilsGUI.getBackgroundBlueDown().setFitWidth(width);
        utilsGUI.getBackgroundBlueDown().setFitHeight(height * 0.24);
        utilsGUI.getBackgroundBlueDown().setY(height - utilsGUI.getBackgroundBlueDown().getFitHeight());
        anchorPane.getChildren().add(utilsGUI.getBackgroundBeige());
        anchorPane.getChildren().add(utilsGUI.getBackgroundBlueUp());
        anchorPane.getChildren().add(utilsGUI.getBackgroundBlueDown());
        anchorPane.getChildren().add(utilsGUI.getLogo());
    }

    /**
     * Build popup.
     */
    private void buildPopup() {
        vBox.getChildren().add(anchorPane);
        Scene input = new Scene(vBox);
        this.setScene(input);
    }

}
