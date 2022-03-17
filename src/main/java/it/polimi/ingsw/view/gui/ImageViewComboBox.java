package it.polimi.ingsw.view.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

/**
 * Class ImageViewComboBox
 */
public class ImageViewComboBox {
    private ComboBox<ImageView> resourceSelector;

    /**
     * Construct an object with a combobox selector.
     */
    public ImageViewComboBox() {
        resourceSelector = createResourcesImageComboBox();
    }

    public ComboBox<ImageView> getResourceSelector() {
        return resourceSelector;
    }

    /**
     * Create a comboBox of images of the resources.
     *
     * @return the comboBox
     */
    public ComboBox<ImageView> createResourcesImageComboBox() {
        ComboBox<ImageView> resourceSelector = new ComboBox<>();
        UtilsGUI utilsGUI = new UtilsGUI();
        resourceSelector.getItems().addAll(utilsGUI.getAllResources());
        resourceSelector.setButtonCell(new ImageViewListCell());
        resourceSelector.setCellFactory(imageViewListView -> new ImageViewListCell());
        resourceSelector.getSelectionModel().select(0);
        return resourceSelector;
    }

    /**
     * Class ImageViewListCell
     */
    class ImageViewListCell extends ListCell<ImageView> {
        private final ImageView view;

        ImageViewListCell() {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            view = new ImageView();
        }

        @Override
        protected void updateItem(ImageView item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
            } else {
                view.setImage(item.getImage());
                view.setFitWidth(50);
                view.setFitHeight(50);
                setGraphic(view);
            }
        }

    }
}
