package it.polimi.ingsw.view.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Class DevelopmentCardShopGUI contains methods to handle the card shop in the GUI
 */
public class DevelopmentCardShopGUI extends Stage {
    Pane pane = new Pane();
    GridPane grid;
    private Integer[] cardShop = new Integer[12];
    private UtilsGUI utilsGUI = new UtilsGUI();
    private Button button;
    private Button button2;
    private int cardID = 0;
    private int selection = -1;
    private boolean alreadyVisible = false;

    /**
     * Construct an object by creating the scene and setting the default background.
     */
    public DevelopmentCardShopGUI() {
        Scene scene = new Scene(pane);
        this.setTitle("Development Cards Shop");
        this.setHeight(830);
        this.setWidth(675);
        button = utilsGUI.createButton("Select card", 20, 250, 210, 735);
        button2 = utilsGUI.createButton("Close", 20, 250, 210, 735);
        utilsGUI.getLogo().setX(263);
        utilsGUI.getLogo().setY(8);
        utilsGUI.getLogo().setFitHeight(80);
        utilsGUI.getLogo().setFitWidth(152);
        utilsGUI.getBackgroundBeige().setFitWidth(675);
        utilsGUI.getBackgroundBeige().setFitHeight(795);
        utilsGUI.getBackgroundBlueUp().setFitWidth(675);
        utilsGUI.getBackgroundBlueUp().setFitHeight(60);
        utilsGUI.getBackgroundBlueDown().setFitWidth(675);
        utilsGUI.getBackgroundBlueDown().setFitHeight(70);
        utilsGUI.getBackgroundBlueDown().setY(795 - 70);
        pane.getChildren().add(utilsGUI.getBackgroundBeige());
        pane.getChildren().add(utilsGUI.getBackgroundBlueUp());
        pane.getChildren().add(utilsGUI.getBackgroundBlueDown());
        pane.getChildren().add(utilsGUI.getLogo());
        pane.getChildren().add(button);
        this.setScene(scene);
    }

    /**
     * Show the card shop.
     */
    public void showCardShop() {
        if (!isAlreadyVisible()) {
            setCards(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, false);
            getButton().setText("Close");
            setAlreadyVisible(true);
        }
    }

    /**
     * Sets which cards are visible in the card shop. If filter is true only the buyable cards are shown,
     * otherwise every card in the shop in shown.
     *
     * @param cardIDs are the card ids in the shop
     * @param filter  determines which cards are shown
     */
    public void setCards(Integer[] cardIDs, boolean filter) {
        grid = new GridPane();
        cardID = 0;
        for (int i = 0; i < cardIDs.length; i++) {
            int cardIndex = i;
            Image image = new Image("img/front/DevelopmentCard-0.png", 178, 269, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(image.getHeight() / 1.35);
            imageView.setFitWidth(image.getWidth() / 1.35);
            imageView.setOpacity(0.2);
            if (cardShop[i] != -1) {
                image = new Image("img/front/DevelopmentCard-" + cardShop[i] + ".png", 178, 269, true, true);
                imageView = getDevelopmentCardImageView(cardShop[i]);
                imageView.setFitHeight(image.getHeight() / 1.35);
                imageView.setFitWidth(image.getWidth() / 1.35);
                if (cardIDs[i] == 0) {
                    imageView.setOpacity(0.6);
                } else {
                    imageView.setOpacity(1);
                }
            }
            if (cardIndex < 4) {
                grid.add(imageView, cardIndex, 0, 1, 1);
            } else if (cardIndex < 8) {
                grid.add(imageView, cardIndex - 4, 1, 1, 1);
            } else if (cardIndex < 12) {
                grid.add(imageView, cardIndex - 8, 2, 1, 1);
            }
            if (filter) {
                ImageView finalImageView = imageView;
                Image finalImage = image;
                imageView.setOnMouseEntered(event -> {
                    if (cardIDs[cardIndex] != 0 && cardIDs[cardIndex] != -1) {
                        finalImageView.setFitHeight(finalImage.getHeight() / 1.35 + 5);
                        finalImageView.setFitWidth(finalImage.getWidth() / 1.35 + 5);
                    }
                });

                imageView.setOnMouseExited(event -> {
                    if (cardIDs[cardIndex] != 0 && cardIDs[cardIndex] != -1) {
                        finalImageView.setFitHeight(finalImage.getHeight() / 1.35);
                        finalImageView.setFitWidth(finalImage.getWidth() / 1.35);
                    }
                });
                imageView.setOnMouseClicked(event -> {
                    if (selection == -1 && cardIDs[cardIndex] != 0 && cardIDs[cardIndex] != -1) {
                        finalImageView.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 235, 28, 0.7), 30, 0.5, 0, 0);");
                        selection = cardIndex;
                        cardID = cardIDs[cardIndex];
                    } else if (selection == cardIndex) {
                        finalImageView.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);");
                        selection = -1;
                        cardID = 0;
                    }
                });
            }
        }
        grid.setHgap(3);
        grid.setVgap(1);
        grid.setPadding(new Insets(10, 10, 10, 10));
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        grid.getColumnConstraints().add(column1);
        grid.getColumnConstraints().add(column2);
        grid.getColumnConstraints().add(column3);
        column1.setPrefWidth(140);
        column2.setPrefWidth(140);
        column3.setPrefWidth(140);
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        grid.getRowConstraints().add(row1);
        grid.getRowConstraints().add(row2);
        grid.getRowConstraints().add(row3);
        row1.setPrefHeight(210);
        row2.setPrefHeight(210);
        row3.setPrefHeight(210);
        grid.setLayoutX(45);
        grid.setLayoutY(70);
        pane.getChildren().add(grid);
    }

    /**
     * Remove the card shop from the stage and sets a new label is the parameter is true.
     *
     * @param changeButtonLabel is true if the label is to be changed, false otherwise
     */
    public void resetCardShop(boolean changeButtonLabel) {
        if (changeButtonLabel) {
            getButton().setText("Select card");
            setAlreadyVisible(false);
        }
        pane.getChildren().remove(grid);
        selection = -1;
    }


    public Button getButton() {
        return button;
    }

    public int getSelection() {
        return selection;
    }

    public boolean isAlreadyVisible() {
        return alreadyVisible;
    }

    public void setAlreadyVisible(boolean alreadyVisible) {
        this.alreadyVisible = alreadyVisible;
    }

    /**
     * Create the image of the card from its id.
     *
     * @param ID is the id of the card
     * @return the image of the card
     */
    public ImageView getDevelopmentCardImageView(int ID) {
        String cardUrl = "img/front/DevelopmentCard-" + ID + ".png";
        Image image = new Image(cardUrl, 178, 269, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-background-image: url(" + cardUrl + ");\n" +
                "    -fx-background-size: 100% 100%;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-color: transparent;\n" +
                "    -fx-text-origin: top;\n" +
                "    -fx-text-alignment: center;\n" +
                "    -fx-text-fill: #dfe3e3;\n" +
                "    -fx-effect: dropshadow(one-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);"
        );
        return imageView;
    }

    /**
     * Get the image of the last purchased card.
     *
     * @return the image of the card
     */
    public ImageView getLastPurchasedDevelopmentCard() {
        return getDevelopmentCardImageView(cardID);
    }

    /**
     * Update the card shop with the new card ids.
     *
     * @param cardShop is the new card shop
     */
    public void updateCardShop(Integer[] cardShop) {
        this.cardShop = cardShop;
    }
}
