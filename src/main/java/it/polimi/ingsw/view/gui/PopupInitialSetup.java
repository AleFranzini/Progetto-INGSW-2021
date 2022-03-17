package it.polimi.ingsw.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class PopupInitialSetup extends Popup {

    private UtilsGUI utilsGUI = new UtilsGUI();
    private List<ImageViewComboBox> initialResource = new ArrayList<>();
    private Button button = utilsGUI.createButton("Submit", 50, 250, 275, 420);
    private Label label = utilsGUI.createLabel("");

    /**
     * Constructor of PopupInitialSetup
     */
    PopupInitialSetup() {
        super(500, 800);
        anchorPane.getChildren().add(label);
        anchorPane.getChildren().add(button);
    }

    /**
     * Method setInitialResource sets the list of ImageViewComboBox adding as many
     * ImageViewComboBox as indicated by the param value.
     *
     * @param num indicates how many ImageViewComboBox need to be added to the list.
     */
    public void setInitialResource(int num) {
        for (int i = 0; i < num; i++) {
            initialResource.add(new ImageViewComboBox());
            initialResource.get(i).getResourceSelector().setLayoutX(500 + i * 150);
            initialResource.get(i).getResourceSelector().setLayoutY(200);
            anchorPane.getChildren().add(initialResource.get(i).getResourceSelector());
        }
    }

    /**
     * Method getButton
     *
     * @return the button present in the anchor pane of the popup.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Method getInitialResource
     *
     * @return the list of ImageViewComboBox present in the anchor pane of the popup.
     */
    public List<ImageViewComboBox> getInitialResource() {
        return initialResource;
    }

    /**
     * Method setLabel sets the text value of the label present in the anchor pane of the popup.
     *
     * @param text is the String to set as the text of the label.
     */
    public void setLabel(String text) {
        label.setText(text);
        label.setLayoutY(200);
    }
}
