package it.polimi.ingsw.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PopupServer extends Popup {

    private UtilsGUI utilsGUI = new UtilsGUI();
    private TextField ip = utilsGUI.createTextField();
    private TextField port = utilsGUI.createTextField();
    private Button button = utilsGUI.createButton("Submit", 50, 250, 200, 325);

    /**
     * Constructor of popup server.
     */
    PopupServer() {
        super(400, 640);
        ip.setLayoutX(350);
        ip.setLayoutY(150);
        Label labelIP = utilsGUI.createLabel("Insert server ip:");
        labelIP.setLayoutY(140);

        port.setLayoutX(350);
        port.setLayoutY(225);
        Label labelPort = utilsGUI.createLabel("Insert server port:");
        labelPort.setLayoutY(220);

        anchorPane.getChildren().addAll(ip, port);
        anchorPane.getChildren().addAll(labelIP, labelPort);

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

    public TextField getIp() {
        return ip;
    }

    public TextField getPort() {
        return port;
    }
}
