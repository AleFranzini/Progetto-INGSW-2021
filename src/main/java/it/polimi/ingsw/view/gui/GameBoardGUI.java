package it.polimi.ingsw.view.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameBoardGUI extends Stage {

    private final UtilsGUI utilsGUI = new UtilsGUI();
    private final Button[] actions = new Button[7];
    /* - */
    private final ImageView[] warehouse = new ImageView[6];
    private final GridPane strongbox = new GridPane();
    private final Label[] strongboxCounter = new Label[4];
    private final ImageView[] leaderDepot = new ImageView[4];
    private final ImageView faith = utilsGUI.getResource("faith");
    private final double[] faithTrackX = {30, 80, 140, 140, 140, 190, 250, 310, 370, 430, 430, 430, 480, 530, 590, 650, 710, 710, 710, 760, 810, 870, 930, 990, 1050};
    private final double[] faithTrackY = {140, 140, 140, 80, 30, 30, 30, 30, 30, 30, 80, 140, 140, 140, 140, 140, 140, 80, 30, 30, 30, 30, 30, 30, 30};
    private final ImageView[] popeTiles = new ImageView[3];
    private final double[] popeTileX = {283, 565, 903};
    private final double[] popeTileY = {115, 60, 115};
    private final Circle actionToken = new Circle(40);
    private final ImageView blackCross = utilsGUI.getResource("blackCross");
    Pane gameboard = new Pane();
    private Button generalButton;
    private Label generalLabel;
    /* Leader Card */
    private List<ImageView> imageViewLeaderCard;
    private List<Image> imageLeaderCard;
    private List<Integer> leaderCardID;
    private List<Integer> leaderOption;
    private int selectedCard = -1;
    private Button activationLeader;
    private Button discardLeader;
    private Button activableLeaderY;
    private Button activableLeaderN;
    private Button activateLeader;
    private Button activateAnotherLeaderCard;
    private Button discardAnotherLeaderCard;
    private Button returnToMenuFromLeader;
    private List<ImageView> purchased = new ArrayList<>();

    public GameBoardGUI() {
        build();
    }

    /**
     * Build the gameboard.
     */
    public void build() {
        this.imageLeaderCard = new ArrayList<>();
        this.imageViewLeaderCard = new ArrayList<>();
        this.leaderCardID = new ArrayList<>();
        this.leaderOption = new ArrayList<>();
        ImageView bg = new ImageView(new Image("img/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png"));
        bg.setFitHeight(800);
        bg.setFitWidth(1150);
        ImageView wall = new ImageView(new Image("img/punchboard/background.png"));
        wall.setFitHeight(800);
        wall.setFitWidth(370);
        wall.setX(1150);
        gameboard.getChildren().add(bg);
        gameboard.getChildren().add(wall);

        //Strongbox
        strongbox.setLayoutX(50);
        strongbox.setLayoutY(590);
        strongbox.add(utilsGUI.getResource("coin"), 0, 0);
        strongbox.add(utilsGUI.getResource("servant"), 1, 0);
        strongbox.add(utilsGUI.getResource("shield"), 0, 1);
        strongbox.add(utilsGUI.getResource("stone"), 1, 1);
        gameboard.getChildren().add(strongbox);
        for (int i = 0; i < 4; i++) {
            strongboxCounter[i] = utilsGUI.createLabelSerif("0");
            if (i == 0 || i == 2)
                strongboxCounter[i].setLayoutX(80);
            else
                strongboxCounter[i].setLayoutX(160);
            if (i == 0 || i == 1)
                strongboxCounter[i].setLayoutY(605);
            else
                strongboxCounter[i].setLayoutY(690);

            strongboxCounter[i].setStyle("-fx-text-fill: white;\n" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 8, 0.75, 0.0, 0.0);");
        }
        gameboard.getChildren().addAll(strongboxCounter);

        //Faith marker
        setFaithMarker(0);
        gameboard.getChildren().add(faith);

        //Pope tiles
        List<Integer> initTiles = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            initTiles.add(0);
        setPopeTiles(initTiles);
        gameboard.getChildren().addAll(popeTiles);

        //MENU
        createMenu();
        createLeaderCardActionButton();

        Scene scene = new Scene(gameboard, 1520, 800);
        this.setTitle("GameBoard");
        this.setScene(scene);
        this.setFullScreen(true);
        this.setFullScreenExitKeyCombination(null);

        //Black border
        ImageView back = new ImageView(new Image("img/punchboard/background1.png"));
        back.setFitHeight(300);
        back.setFitWidth(370);
        back.setLayoutY(520);
        back.setLayoutX(1150);
        back.setOpacity(1);
        gameboard.getChildren().add(back);

        ImageView border = new ImageView(new Image("img/front/DevelopmentCard-0.png"));
        border.setFitHeight(800);
        border.setFitWidth(3);
        border.setX(1149);
        gameboard.getChildren().add(border);

        ImageView border2 = new ImageView(new Image("img/front/DevelopmentCard-0.png"));
        border2.setFitHeight(3);
        border2.setFitWidth(370);
        border2.setLayoutY(520);
        border2.setLayoutX(1150);
        gameboard.getChildren().add(border2);

        ImageView border3 = new ImageView(new Image("img/front/DevelopmentCard-0.png"));
        border3.setFitHeight(3);
        border3.setFitWidth(370);
        border3.setLayoutY(797);
        border3.setLayoutX(1150);
        gameboard.getChildren().add(border3);

        ImageView border4 = new ImageView(new Image("img/front/DevelopmentCard-0.png"));
        border4.setFitHeight(280);
        border4.setFitWidth(3);
        border4.setLayoutY(520);
        border4.setLayoutX(1517);
        gameboard.getChildren().add(border4);


    }

    /**
     * Create leader card button.
     */
    public void createLeaderCardActionButton() {
        activationLeader = utilsGUI.createButton("Activate Leader", 50, 260, 1200, 150);
        discardLeader = utilsGUI.createButton("Discard Leader", 50, 260, 1200, 250);
        activableLeaderY = utilsGUI.createButton("Only activables Leader", 50, 370, 1150, 150);
        activableLeaderN = utilsGUI.createButton("All Leader", 50, 250, 1210, 250);
        activateLeader = utilsGUI.createButton("Activate", 50, 250, 1210, 250);
        activateAnotherLeaderCard = utilsGUI.createButton("Activate another Leader", 50, 370, 1150, 150);
        discardAnotherLeaderCard = utilsGUI.createButton("Discard another Leader", 50, 300, 1185, 150);
        returnToMenuFromLeader = utilsGUI.createButton("Return to menu", 50, 300, 1183, 250);
    }

    /**
     * Create button menu.
     */
    public void createMenu() {
        actions[0] = utilsGUI.createButton("Show Market", 50, 300, 200, 325);
        actions[1] = utilsGUI.createButton("Show Card Shop", 50, 300, 200, 325);
        actions[2] = utilsGUI.createButton("Buy from Market", 50, 300, 200, 325);
        actions[3] = utilsGUI.createButton("Buy Dev Cards", 50, 300, 200, 325);
        actions[4] = utilsGUI.createButton("Activate Prod", 50, 300, 200, 325);
        actions[5] = utilsGUI.createButton("Play a Leader", 50, 300, 200, 325);
        actions[6] = utilsGUI.createButton("End Turn", 50, 300, 200, 325);
    }

    /**
     * Get actions
     *
     * @return button array of actions menu.
     */
    public Button[] getActions() {
        return actions;
    }

    /**
     * Get faith marker
     *
     * @return image view of faith marker.
     */
    public ImageView getFaithMarker() {
        return faith;
    }

    /**
     * Set faith marker in it position.
     *
     * @param n number of faith marker points.
     */
    public void setFaithMarker(int n) {
        faith.setX(faithTrackX[n]);
        faith.setY(faithTrackY[n]);
    }

    /**
     * Set pope tiles.
     *
     * @param popeTiles
     */
    public void setPopeTiles(List<Integer> popeTiles) {
        for (int i = 0; i < 3; i++) {
            if (popeTiles.get(i) == 0) {
                this.popeTiles[i] = new ImageView(new Image("img/punchboard/popesFavorTile" + (i + 1) + "Back.png"));
                this.popeTiles[i].setLayoutX(popeTileX[i]);
                this.popeTiles[i].setLayoutY(popeTileY[i]);
                this.popeTiles[i].setFitHeight(80);
                this.popeTiles[i].setFitWidth(80);
            } else if (popeTiles.get(i) == 1) {
                this.popeTiles[i].setImage(new Image("img/punchboard/popesFavorTile" + (i + 1) + "Front.png"));
            } else if (popeTiles.get(i) == -1) {
                this.popeTiles[i].setOpacity(0);
            }
        }
    }

    /**
     * Set leader card in gameboard.
     *
     * @param pathImage    path of images of leader card.
     * @param leaderCardID IDs of leader card.
     */
    public void setLeaderCard(String[] pathImage, int[] leaderCardID) {
        for (int i = 0; i < 2; i++) {
            this.imageLeaderCard.add(new Image(pathImage[i]));
            this.imageViewLeaderCard.add(new ImageView(imageLeaderCard.get(i)));
            this.leaderCardID.add(leaderCardID[i]);
            this.leaderOption.add(0);
        }
        for (int i = 0; i < imageViewLeaderCard.size(); i++) {
            int finalI = i;
            imageViewLeaderCard.get(i).setFitHeight(imageLeaderCard.get(i).getHeight() / 3);
            imageViewLeaderCard.get(i).setFitWidth(imageLeaderCard.get(i).getWidth() / 3);
            imageViewLeaderCard.get(i).setY(800 - imageViewLeaderCard.get(i).getFitHeight() - 20);
            imageViewLeaderCard.get(i).setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);");
            imageViewLeaderCard.get(i).setOpacity(0.70);
            imageViewLeaderCard.get(i).setOnMouseEntered(actionEvent -> {
                if (selectedCard == -1) {
                    imageViewLeaderCard.get(finalI).setFitHeight(imageLeaderCard.get(finalI).getHeight() / 3 + 10);
                    imageViewLeaderCard.get(finalI).setFitWidth(imageLeaderCard.get(finalI).getWidth() / 3 + 10);
                }
            });
            imageViewLeaderCard.get(i).setOnMouseExited(actionEvent -> {
                if (selectedCard == -1) {
                    imageViewLeaderCard.get(finalI).setFitHeight(imageLeaderCard.get(finalI).getHeight() / 3);
                    imageViewLeaderCard.get(finalI).setFitWidth(imageLeaderCard.get(finalI).getWidth() / 3);
                }
            });
            imageViewLeaderCard.get(i).setOnMouseClicked(actionEvent -> {
                if (selectedCard == -1) {
                    selectedCard = finalI;
                    imageViewLeaderCard.get(finalI).setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 235, 28, 0.7), 30, 0.7, 0, 0);");
                } else {
                    selectedCard = -1;
                    imageViewLeaderCard.get(finalI).setFitHeight(imageLeaderCard.get(finalI).getHeight() / 3);
                    imageViewLeaderCard.get(finalI).setFitWidth(imageLeaderCard.get(finalI).getWidth() / 3);
                    imageViewLeaderCard.get(finalI).setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);");
                }
            });
            imageViewLeaderCard.get(i).setDisable(true);
        }
        imageViewLeaderCard.get(0).setX(1170);
        imageViewLeaderCard.get(1).setX(1170 + imageViewLeaderCard.get(0).getFitWidth() + 20);
        gameboard.getChildren().addAll(imageViewLeaderCard);
    }

    /**
     * Get image view leader card.
     *
     * @return list of image view of leader card.
     */
    public List<ImageView> getImageViewLeaderCard() {
        return imageViewLeaderCard;
    }

    /**
     * Get leader card options.
     *
     * @return leader card options.
     */
    public List<Integer> getLeaderOption() {
        return leaderOption;
    }

    /**
     * Hide leader card.
     *
     * @param index of leader card to hide.
     */
    public void hideLeaderCard(int index) {
        gameboard.getChildren().removeAll(imageViewLeaderCard);
        imageViewLeaderCard.get(index).setImage(new Image("img/back/BackLeaderCard.png"));
        imageViewLeaderCard.get(index).setDisable(true);
        imageViewLeaderCard.get(index).setFitWidth(462 / 3);
        imageViewLeaderCard.get(index).setFitHeight(698 / 3);
        gameboard.getChildren().addAll(imageViewLeaderCard);
    }

    /**
     * Count leader card
     *
     * @param activated if true, return number of activated leader card
     *                  if false, return number of discard leader card
     */
    public int countLeaderCard(boolean activated) {
        int j = 0;
        if (activated)
            for (int i : leaderOption) {
                if (i == 1)
                    j++;
            }
        else
            for (int i : leaderOption) {
                if (i == -1)
                    j++;
            }
        return j;
    }

    /**
     * Get selected card.
     *
     * @return index of selected leader card in gameboard.
     */
    public int getSelectedCard() {
        return selectedCard;
    }

    /**
     * Set selected leader card.
     *
     * @param selectedCard is index of selected leader card.
     */
    public void setSelectedCard(int selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void hideActionButton() {
        gameboard.getChildren().removeAll(actions);
    }

    public void showActionButton() {
        for (int i = 0; i < 6; i++) {
            actions[i].setLayoutX(1185);
            actions[i].setLayoutY(25 + i * 80);
        }
        gameboard.getChildren().addAll(actions);
        actions[5].setDisable(countLeaderCard(true) >= 2 || countLeaderCard(false) >= 2);
        gameboard.getChildren().remove(actions[6]);
    }

    public void hideLeaderCardActionButton() {
        gameboard.getChildren().remove(activationLeader);
        gameboard.getChildren().remove(discardLeader);
        gameboard.getChildren().remove(returnToMenuFromLeader);
    }

    public void showLeaderCardActionButton() {
        gameboard.getChildren().add(activationLeader);
        gameboard.getChildren().add(discardLeader);
        gameboard.getChildren().add(returnToMenuFromLeader);
    }

    public void hideLastActionButton() {
        gameboard.getChildren().remove(actions[0]);
        gameboard.getChildren().remove(actions[1]);
        gameboard.getChildren().remove(actions[5]);
        gameboard.getChildren().remove(actions[6]);
    }

    public void showLastActionButton() {
        actions[0].setLayoutX(1185);
        actions[0].setLayoutY(25);
        actions[1].setLayoutX(1185);
        actions[1].setLayoutY(105);
        actions[5].setLayoutX(1185);
        actions[5].setLayoutY(185);
        actions[6].setLayoutX(1185);
        actions[6].setLayoutY(265);

        gameboard.getChildren().add(actions[0]);
        gameboard.getChildren().add(actions[1]);
        gameboard.getChildren().add(actions[5]);
        gameboard.getChildren().add(actions[6]);
    }

    public void hideActivablesLeaderButton() {
        gameboard.getChildren().remove(activableLeaderY);
        gameboard.getChildren().remove(activableLeaderN);
    }

    public void showActivablesLeaderButton() {
        gameboard.getChildren().add(activableLeaderY);
        gameboard.getChildren().add(activableLeaderN);
    }

    public void hideActivateAnotherLeaderButton() {
        gameboard.getChildren().remove(activateAnotherLeaderCard);
        gameboard.getChildren().remove(returnToMenuFromLeader);
    }

    public void showActivateAnotherLeaderButton() {
        if (countLeaderCard(true) < 2 || countLeaderCard(false) < 2)
            gameboard.getChildren().add(activateAnotherLeaderCard);
        gameboard.getChildren().add(returnToMenuFromLeader);
    }

    public void hideDiscardAnotherLeaderButton() {
        gameboard.getChildren().remove(discardAnotherLeaderCard);
        gameboard.getChildren().remove(returnToMenuFromLeader);
    }

    public void showDiscardAnotherLeaderButton() {
        if (countLeaderCard(true) < 2 || countLeaderCard(false) < 2)
            gameboard.getChildren().add(discardAnotherLeaderCard);
        gameboard.getChildren().add(returnToMenuFromLeader);
    }

    public void hideActivateLeaderButton() {
        gameboard.getChildren().remove(activateLeader);
    }

    public void showActivateLeaderButton() {
        gameboard.getChildren().add(activateLeader);
    }

    public void hideDiscardLeaderButton() {
        gameboard.getChildren().remove(discardLeader);
    }

    public void showDiscardLeaderButton() {
        gameboard.getChildren().add(discardLeader);
    }

    public Button getReturnToMenuFromLeader() {
        return returnToMenuFromLeader;
    }

    public Button getActivateAnotherLeaderCard() {
        return activateAnotherLeaderCard;
    }

    public Button getDiscardAnotherLeaderCard() {
        return discardAnotherLeaderCard;
    }

    public Button getActivationLeader() {
        return activationLeader;
    }

    public Button getDiscardLeader() {
        return discardLeader;
    }

    public Button getActivableLeaderN() {
        return activableLeaderN;
    }

    public Button getActivableLeaderY() {
        return activableLeaderY;
    }

    public List<Integer> getLeaderCardID() {
        return leaderCardID;
    }

    public Button getActivateLeader() {
        return activateLeader;
    }

    public List<ImageView> getPurchased() {
        return purchased;
    }

    public void setPurchased(List<ImageView> purchased) {
        this.purchased = purchased;
    }

    public Button getButton() {
        return generalButton;
    }

    public void setButton(Button button) {
        this.generalButton = button;
    }

    public Label getLabel() {
        return generalLabel;
    }

    public void setLabel(Label label) {
        this.generalLabel = label;
    }

    public void setStrongboxCounter(int index, String counter) {
        strongboxCounter[index].setText(counter);
    }

    public ImageView getWarehouse(int index) {
        return warehouse[index];
    }

    /**
     * Set warehouse depot.
     *
     * @param index    of resources.
     * @param resource image view of resources.
     * @param x
     * @param y
     * @return true if done.
     * false otherwise.
     */
    public boolean setWarehouseDepot(int index, ImageView resource, double x, double y) {
        if (resource == null) {
            warehouse[index] = null;
            return false;
        }
        if (index < 0 || index > 5)
            return false;
        if (warehouse[index] == null) {
            warehouse[index] = resource;
            if (index == 0) {
                warehouse[index].translateXProperty().set(110 - x);
                warehouse[index].translateYProperty().set(330 - y);
            } else if (index == 1) {
                warehouse[index].translateXProperty().set(80 - x);
                warehouse[index].translateYProperty().set(400 - y);
            } else if (index == 2) {
                warehouse[index].translateXProperty().set(140 - x);
                warehouse[index].translateYProperty().set(400 - y);
            } else {
                warehouse[index].translateXProperty().set(60 + (index - 3) * 50 - x);
                warehouse[index].translateYProperty().set(470 - y);
            }
            return true;
        }
        return false;
    }

    /**
     * Method countDepotResources returns the number of resources stored in the indicated depot of the warehouse and the relative resource type.
     *
     * @param depot is the depot of the warehouse from which count the resources.
     * @return the number of resources stored in the indicated depot and the relative resource type.
     */
    public String countDepotResources(int depot) {
        int counter = 0;
        String type = "";
        switch (depot) {
            case 0:
                if (getWarehouse(0) != null) {
                    counter = 1;
                    type = getResourceFromImage(getWarehouse(0));
                }
                break;
            case 1:
                if (getWarehouse(1) == null) {
                    if (getWarehouse(2) != null) {
                        counter = 1;
                        type = getResourceFromImage(getWarehouse(2));
                    }
                } else if (getWarehouse(2) == null) {
                    if (getWarehouse(1) != null) {
                        counter = 1;
                        type = getResourceFromImage(getWarehouse(1));
                    }
                } else if (getWarehouse(1) != null && getWarehouse(2) != null) {
                    counter = 2;
                    type = getResourceFromImage(getWarehouse(1));
                }
                break;
            case 2:
                if (getWarehouse(3) != null && getWarehouse(4) != null && getWarehouse(5) != null) {
                    counter = 3;
                    type = getResourceFromImage(getWarehouse(3));
                } else if (getWarehouse(3) != null && getWarehouse(4) == null && getWarehouse(5) == null) {
                    counter = 1;
                    type = getResourceFromImage(getWarehouse(3));
                } else if (getWarehouse(3) == null && getWarehouse(4) != null && getWarehouse(5) == null) {
                    counter = 1;
                    type = getResourceFromImage(getWarehouse(4));
                } else if (getWarehouse(3) == null && getWarehouse(4) == null && getWarehouse(5) != null) {
                    counter = 1;
                    type = getResourceFromImage(getWarehouse(5));
                } else if ((getWarehouse(3) != null && getWarehouse(4) != null && getWarehouse(5) == null) ||
                        (getWarehouse(3) != null && getWarehouse(4) == null && getWarehouse(5) != null)) {
                    counter = 2;
                    type = getResourceFromImage(getWarehouse(3));
                } else if (getWarehouse(3) == null && getWarehouse(4) != null && getWarehouse(5) != null) {
                    counter = 2;
                    type = getResourceFromImage(getWarehouse(5));
                }
                break;
        }
        return counter + type;
    }

    /**
     * Method orderDepot is used to properly order the third depot of the warehouse.
     */
    public void orderDepot() {
        if (getWarehouse(3) != null) {
            if (getWarehouse(5) != null && getWarehouse(4) == null) {
                warehouse[4] = warehouse[5];
                setWarehouseDepot(5, null, 0, 0);
            }
        } else if (getWarehouse(4) != null) {
            if (getWarehouse(5) != null) {
                warehouse[3] = warehouse[5];
                setWarehouseDepot(5, null, 0, 0);
            } else {
                warehouse[3] = warehouse[4];
                setWarehouseDepot(4, null, 0, 0);
            }
        } else if (getWarehouse(5) != null) {
            warehouse[3] = warehouse[5];
            setWarehouseDepot(5, null, 0, 0);
        }
    }

    /**
     * Method countDepotResources returns the number of resources stored in the indicated leader depot and the relative resource type.
     *
     * @param leaderDepot is the depot of the leaderCard from which count the resources.
     * @return the number of resources stored in the indicated leader depot and the relative resource type.
     */
    public String countLeaderDepotResources(int leaderDepot) {
        int counter = 0;
        String type = "";
        switch (leaderDepot) {
            case 0:
                if (getLeaderDepot(0) != null) {
                    counter++;
                    type = getResourceFromImage(getLeaderDepot(0));
                }
                if (getLeaderDepot(1) != null) {
                    counter++;
                    type = getResourceFromImage(getLeaderDepot(1));
                }
                break;
            case 1:
                if (getLeaderDepot(2) != null) {
                    counter++;
                    type = getResourceFromImage(getLeaderDepot(2));
                }
                if (getLeaderDepot(3) != null) {
                    counter++;
                    type = getResourceFromImage(getLeaderDepot(3));
                }
                break;
        }
        return counter + type;
    }

    public ImageView getLeaderDepot(int index) {
        return leaderDepot[index];
    }

    public boolean setLeaderDepot(int index, ImageView resource, double x, double y) {
        if (resource == null) {
            leaderDepot[index] = null;
            return false;
        }
        if (index < 0 || index > 3)
            return false;
        if (leaderDepot[index] == null) {
            leaderDepot[index] = resource;
            if (getLeaderCardID().get(0) == 4 && getResourceFromImage(resource).equalsIgnoreCase("shield") ||
                    getLeaderCardID().get(0) == 5 && getResourceFromImage(resource).equalsIgnoreCase("stone") ||
                    getLeaderCardID().get(0) == 6 && getResourceFromImage(resource).equalsIgnoreCase("servant") ||
                    getLeaderCardID().get(0) == 7 && getResourceFromImage(resource).equalsIgnoreCase("coin"))
                leaderDepot[index].translateXProperty().set(1180 + index * 60 - x);
            else
                leaderDepot[index].translateXProperty().set(1230 + index * 60 - x);
            leaderDepot[index].translateYProperty().set(700 - y);
            return true;
        }
        return false;
    }

    /**
     * Get resource from image.
     *
     * @param resource image view of resource
     * @return string of resource.
     */
    public String getResourceFromImage(ImageView resource) {
        if (resource == null)
            return null;
        else if (resource.getImage().getUrl().contains("coin"))
            return "COIN";
        else if (resource.getImage().getUrl().contains("servant"))
            return "SERVANT";
        else if (resource.getImage().getUrl().contains("shield"))
            return "SHIELD";
        else if (resource.getImage().getUrl().contains("stone"))
            return "STONE";
        return "FAITH";
    }

    public Circle getActionToken() {
        return actionToken;
    }

    public void setActionToken(Image image) {
        actionToken.setFill(new ImagePattern(image));
        actionToken.setCenterX(1100);
        actionToken.setCenterY(200);
    }

    public void setLorenzoFaithMarker(int n) {
        blackCross.setX(faithTrackX[n] - 10);
        blackCross.setY(faithTrackY[n] + 10);
    }

    public void singlePlayer() {
        setLorenzoFaithMarker(0);
        setActionToken(new Image("img/punchboard/backToken.png"));
        gameboard.getChildren().add(actionToken);
        blackCross.setFitHeight(60);
        blackCross.setFitWidth(60);
        gameboard.getChildren().add(blackCross);
    }
}