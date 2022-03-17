package it.polimi.ingsw.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.EventController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientOffline;
import it.polimi.ingsw.network.client.ClientOnline;
import it.polimi.ingsw.view.View;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.PointLight;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class GUI extends Application implements View {

    private final GameBoardGUI gameBoardGUI = new GameBoardGUI();
    private final UtilsGUI utilsGUI = new UtilsGUI();
    private final ImageViewComboBox imageViewComboBox = new ImageViewComboBox();
    private final Gson gson = new Gson();
    private final DevelopmentCardShopGUI developmentCardShopGUI = new DevelopmentCardShopGUI();
    private final SlotStackGUI slotStackGUI = new SlotStackGUI(gameBoardGUI);
    private final MarketGUI marketGUI = new MarketGUI();
    private Client client;
    private Stage primaryStage;
    private boolean[] legalAction = new boolean[2];
    private boolean actionCompleted, restored = false;

    public UtilsGUI getUtilsGUI() {
        return utilsGUI;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setOnCloseRequest((WindowEvent t) -> {
            primaryStage.close();
            System.exit(0);
        });

        primaryStage.setTitle("Masters of Renaissance");
        primaryStage.getIcons().add(new Image("img/punchboard/inkwell.png"));
        primaryStage.setResizable(false);

        if (EventController.getEventController().getClient() != null) {
            client = EventController.getEventController().getClient();
        } else
            client = new ClientOnline();
        client.setView(this);
        displayStart();
        primaryStage.show();
    }

    public void displayStart() {
        PopupStart popupStart = new PopupStart();
        primaryStage.setScene(popupStart.getScene());
        popupStart.getButton().setOnAction(actionEvent -> {
            if (client instanceof ClientOffline)
                insertUsername();
            else
                setupProcess();
        });
    }

    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
            Alert leaderAlert = new Alert(Alert.AlertType.INFORMATION);
            leaderAlert.setTitle("Warning");
            leaderAlert.setHeaderText(message);
            Optional<ButtonType> response = leaderAlert.showAndWait();
        });
    }

    @Override
    public void printGameboard() {
        Platform.runLater(() -> {
            if (primaryStage.getScene() != gameBoardGUI.getScene() && gameBoardGUI.getScene() != null) {
                primaryStage.setScene(gameBoardGUI.getScene());
            }
        });
    }

    @Override
    public void setupProcess() {
        PopupServer popupServer = new PopupServer();
        primaryStage.setScene(popupServer.getScene());
        popupServer.getButton().setOnAction(actionEvent -> {
            ((ClientOnline) client).setServerPort(Integer.parseInt(popupServer.getPort().getText()));
            ((ClientOnline) client).setServerIP(popupServer.getIp().getText());
            ((ClientOnline) client).connectToServer();
        });
        ((ClientOnline) client).connectToServer();
    }

    @Override
    public void insertUsername() {
        Platform.runLater(() -> {
            PopupPlayerSetup popupPlayerSetup = new PopupPlayerSetup();
            primaryStage.setScene(popupPlayerSetup.getScene());
            popupPlayerSetup.getButton().setOnAction(actionEvent -> {
                client.sendMessage(new Message(MessageType.LOGIN, popupPlayerSetup.getUsername().getText()));
                if (client instanceof ClientOffline)
                    ((ClientOffline) client).start();
                else {
                    popupPlayerSetup.anchorPane.getChildren().remove(popupPlayerSetup.getUsername());
                    popupPlayerSetup.anchorPane.getChildren().remove(popupPlayerSetup.getButton());
                    popupPlayerSetup.setLabel("Waiting for your turn ...");
                }
            });
        });
    }

    @Override
    public void insertNumberOfPlayers() {
        Platform.runLater(() -> {
            PopupNumberPlayers popupNumberPlayers = new PopupNumberPlayers();
            primaryStage.setScene(popupNumberPlayers.getScene());
            popupNumberPlayers.getButton().setOnAction(actionEvent -> {
                if (popupNumberPlayers.getSelectNumPlayers().getValue() != null) {
                    client.sendMessage(new Message(MessageType.CHOOSE_NUM_PLAYERS, popupNumberPlayers.getSelectNumPlayers().getValue()));
                    popupNumberPlayers.anchorPane.getChildren().remove(popupNumberPlayers.getSelectNumPlayers());
                    popupNumberPlayers.anchorPane.getChildren().remove(popupNumberPlayers.getButton());
                    popupNumberPlayers.setLabel("Waiting for the players to connect ...");
                    popupNumberPlayers.anchorPane.getChildren().remove(popupNumberPlayers.getSelectNumPlayers());
                }
            });
        });
    }

    @Override
    public void askRestoreGame(String games) {
        Platform.runLater(() -> {
            Popup popup = new Popup(400, 640);
            String[] dates = games.split("\n");
            Button yes = utilsGUI.createButton("Yes", 50, 200, 80, 200);
            Button no = utilsGUI.createButton("No", 50, 200, 350, 200);
            popup.anchorPane.getChildren().addAll(yes, no);
            Label label = utilsGUI.createLabel("Do you want to restore a previous game?");
            label.setLayoutX(130);
            label.setLayoutY(120);
            popup.anchorPane.getChildren().add(label);
            no.setOnAction(event -> {
                client.sendMessage(new Message(MessageType.RESTORE_GAME, null));
                popup.anchorPane.getChildren().remove(yes);
                popup.anchorPane.getChildren().remove(no);
                popup.anchorPane.getChildren().remove(label);
                Label label2 = utilsGUI.createLabel("Waiting for your turn ...");
                popup.anchorPane.getChildren().add(label2);
                label2.setLayoutY(160);
                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), label2);
                scaleTransition.setFromX(0);
                scaleTransition.setFromY(0);
                scaleTransition.setToX(1.5);
                scaleTransition.setToY(1.5);
                scaleTransition.play();
            });
            yes.setOnAction(event -> {
                if (dates.length == 1)
                    client.sendMessage(new Message(MessageType.RESTORE_GAME, dates[0]));
                else {
                    popup.anchorPane.getChildren().removeAll(yes, no);
                    popup.anchorPane.getChildren().remove(label);
                    label.setText("On the server there are " + dates.length + " games previously saved:");
                    popup.anchorPane.getChildren().add(label);
                    Label labelDate = utilsGUI.createLabel("Select the game to restore: ");
                    labelDate.setLayoutX(50);
                    labelDate.setLayoutY(200);
                    popup.anchorPane.getChildren().add(labelDate);
                    ChoiceBox<String> oldGames = new ChoiceBox<>();
                    for (int i = 0; i < dates.length; i++)
                        oldGames.getItems().add(dates[i]);
                    oldGames.setPrefWidth(150);
                    oldGames.setLayoutX(400);
                    oldGames.setLayoutY(220);
                    popup.anchorPane.getChildren().add(oldGames);
                    Button button = utilsGUI.createButton("Continue", 50, 250, 200, 330);
                    popup.anchorPane.getChildren().add(button);
                    button.setOnAction(event1 -> {
                        client.sendMessage(new Message(MessageType.RESTORE_GAME, oldGames.getValue()));
                        printGameboard();
                    });
                }
                popup.anchorPane.getChildren().remove(yes);
                popup.anchorPane.getChildren().remove(no);
                popup.anchorPane.getChildren().remove(label);
                Label label2 = utilsGUI.createLabel("Waiting for your turn ...");
                popup.anchorPane.getChildren().add(label2);
                label2.setLayoutY(160);
                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), label2);
                scaleTransition.setFromX(0);
                scaleTransition.setFromY(0);
                scaleTransition.setToX(1.5);
                scaleTransition.setToY(1.5);
                scaleTransition.play();
            });
            popup.setTitle("Restore Game");
            primaryStage.setScene(popup.getScene());
        });
    }

    @Override
    public void selectLeaderCardsFromDeck(int[] indexes) {
        Platform.runLater(() -> {
            PopupGetLeaderCard popupGetLeaderCard = new PopupGetLeaderCard();
            popupGetLeaderCard.insertLeaderCard(indexes);
            popupGetLeaderCard.getButton().setOnAction(actionEvent -> {
                int[] selectedIndexes = popupGetLeaderCard.getSelectedIndex();
                if (popupGetLeaderCard.getNumberSelectedCard() == 2) {
                    client.sendMessage(new Message(MessageType.LEADERCARD_SELECTION, selectedIndexes));
                }
                popupGetLeaderCard.getButton().setDisable(true);
            });
            primaryStage.setScene(popupGetLeaderCard.getScene());
        });
    }

    @Override
    public void setInitialLeaderCard(int[] indexesCardInitial) {
        Platform.runLater(() -> {
            gameBoardGUI.setLeaderCard(new String[]{"img/front/LeaderCard-" + indexesCardInitial[0] + ".png",
                    "img/front/LeaderCard-" + indexesCardInitial[1] + ".png"}, indexesCardInitial);
            updateActivatedLeaderCard();
        });
    }

    @Override
    public void setActivatedLeaderCard(int indexCardActivated) {
        if (gameBoardGUI.countLeaderCard(true) == 0)
            if (gameBoardGUI.getSelectedCard() >= 0)
                gameBoardGUI.getLeaderOption().set(gameBoardGUI.getSelectedCard(), 1);
            else {
                gameBoardGUI.getLeaderOption().set(0, 1);
                gameBoardGUI.getLeaderOption().set(1, 1);
            }
        updateActivatedLeaderCard();
    }

    @Override
    public void setDiscardLeaderCard(int index) {
        if (gameBoardGUI.countLeaderCard(false) == 0)
            if (gameBoardGUI.getSelectedCard() >= 0)
                gameBoardGUI.getLeaderOption().set(gameBoardGUI.getSelectedCard(), -1);
            else {
                gameBoardGUI.getLeaderOption().set(0, -1);
                gameBoardGUI.getLeaderOption().set(1, -1);
            }
        updateActivatedLeaderCard();
    }

    @Override
    public void chooseInitialResources(int numResources) {
        Platform.runLater(() -> {
            PopupInitialSetup popupInitialSetup = new PopupInitialSetup();
            primaryStage.setScene(popupInitialSetup.getScene());
            popupInitialSetup.setInitialResource(numResources);
            if (numResources == 2)
                popupInitialSetup.setLabel("Choose the resource you want:");
            else
                popupInitialSetup.setLabel("Choose the resources you want:");
            popupInitialSetup.getButton().setOnAction(actionEvent -> {
                List<ImageViewComboBox> resourceSelectors = popupInitialSetup.getInitialResource();
                printGameboard();
                if (numResources == 1)
                    client.sendMessage(new Message(MessageType.INITIAL_RESOURCES_SELECTION, utilsGUI.getResourceFromImage(resourceSelectors.get(0).getResourceSelector().getValue())));
                else if (numResources == 2)
                    client.sendMessage(new Message(MessageType.INITIAL_RESOURCES_SELECTION,
                            utilsGUI.getResourceFromImage(resourceSelectors.get(0).getResourceSelector().getValue()) + " " +
                                    utilsGUI.getResourceFromImage(resourceSelectors.get(1).getResourceSelector().getValue())));
            });
        });
    }

    @Override
    public void checkEndTurn() {
        actionCompleted = false;
        client.sendMessage(new Message(MessageType.END_TURN));
        if (!gameBoardGUI.gameboard.getChildren().contains(gameBoardGUI.getActionToken())) {
            gameBoardGUI.gameboard.getChildren().removeAll(gameBoardGUI.getActions()[5], gameBoardGUI.getActions()[6]);
        } else
            gameBoardGUI.hideLastActionButton();
    }

    @Override
    public void updatePopeTiles(Message message) {
        Platform.runLater(() -> gameBoardGUI.setPopeTiles(Arrays.asList(gson.fromJson(message.getMessage(), Integer[].class))));
    }

    @Override
    public void updateLeaderCard(int[] indexes) {
        Platform.runLater(() -> {
            String card0 = (indexes[0] == -1) ? "img/back/BackLeaderCard.png" : "img/front/LeaderCard-" + indexes[0] + ".png";
            String card1 = (indexes[1] == -1) ? "img/back/BackLeaderCard.png" : "img/front/LeaderCard-" + indexes[1] + ".png";
            gameBoardGUI.setLeaderCard(new String[]{card0, card1}, indexes);
            for (int i = 0; i < 2; i++) {
                if (indexes[i] == -1) {
                    gameBoardGUI.getLeaderOption().set(i, -1);
                } else {
                    if (indexes[i + 2] == 1) {
                        gameBoardGUI.getLeaderOption().set(i, 1);
                    } else {
                        gameBoardGUI.getLeaderOption().set(i, 0);
                    }
                }
            }
            updateActivatedLeaderCard();
        });
    }

    @Override
    public void updateFaithMarker(Message message) {
        Platform.runLater(() -> gameBoardGUI.setFaithMarker(Integer.parseInt(message.getMessage())));
    }

    @Override
    public void updateWarehouse(Message message) {
        Platform.runLater(() -> {
            String[] warehouse = message.getMessage().split("\n");
            for (int i = 0; i < warehouse.length; i++) {
                if (!warehouse[i].contains("-")) {
                    String[] depot = warehouse[i].split(" ");
                    int num = Integer.parseInt(depot[0]);
                    if (i == 0 && num == 1 && gameBoardGUI.getWarehouse(0) == null) {
                        gameBoardGUI.setWarehouseDepot(0, utilsGUI.getResource(depot[1].toLowerCase()), 0, 0);
                        gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getWarehouse(0));
                        restored = true;
                    }
                    if (i == 1) {
                        if (num == 1) {
                            if (gameBoardGUI.getWarehouse(1) != null)
                                gameBoardGUI.setWarehouseDepot(2, null, 0, 0);
                            if (gameBoardGUI.getWarehouse(1) == null && gameBoardGUI.getWarehouse(2) == null) {
                                gameBoardGUI.setWarehouseDepot(1, utilsGUI.getResource(depot[1].toLowerCase()), 0, 0);
                                gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getWarehouse(i));
                                restored = true;
                            }
                        } else {
                            for (int j = 1; j < 3; j++)
                                if (gameBoardGUI.getWarehouse(j) == null) {
                                    gameBoardGUI.setWarehouseDepot(j, utilsGUI.getResource(depot[1].toLowerCase()), 0, 0);
                                    gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getWarehouse(j));
                                    restored = true;
                                }
                        }
                    } else if (i == 2) {
                        gameBoardGUI.orderDepot();
                        for (int j = 5; j > 2 + num; j--)
                            if (gameBoardGUI.getWarehouse(j) != null) {
                                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getWarehouse(j));
                                gameBoardGUI.setWarehouseDepot(j, null, 0, 0);
                            }
                        int k = 3 + num;
                        for (int j = 3; j < k; j++) {
                            if (gameBoardGUI.getWarehouse(j) == null) {
                                gameBoardGUI.setWarehouseDepot(j, utilsGUI.getResource(depot[1].toLowerCase()), 0, 0);
                                gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getWarehouse(j));
                                restored = true;
                            }
                        }
                    }
                } else {
                    int k = 1;
                    if (i == 2) {
                        i++;
                        k = 0;
                    }
                    for (int j = i; j < (i * 2 + k); j++)
                        if (gameBoardGUI.getWarehouse(j) != null) {
                            gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getWarehouse(j));
                            gameBoardGUI.setWarehouseDepot(j, null, 0, 0);
                        }
                }
            }
        });
    }

    @Override
    public void updateStrongbox(Message message) {
        Platform.runLater(() -> {
            String[] strongbox = message.getMessage().split("\n");
            for (int i = 0; i < 4; i++) {
                String[] parts = strongbox[i].split(" ");
                gameBoardGUI.setStrongboxCounter(i, parts[0]);
            }
        });
    }

    @Override
    public void updateLeaderDepot(Message message) {
        Platform.runLater(() -> {
            if (!message.getMessage().equals("null")) {
                JSONArray jsonArray = new JSONArray(message.getMessage());
                for (int i = 0; i < 2; i++) {
                    if (jsonArray.get(i).toString().equals("null") || jsonArray.getJSONObject(i).getInt("quantity") == 0) {
                        if (i == 1)
                            i++;
                        for (int j = 0; j < 2; j++) {
                            if (gameBoardGUI.getLeaderDepot(i + j) != null) {
                                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getLeaderDepot(i + j));
                                gameBoardGUI.setLeaderDepot(i + j, null, 0, 0);
                            }
                        }
                    } else {
                        if (jsonArray.getJSONObject(i).getInt("quantity") == 1) {
                            if (i == 1)
                                i++;
                            if (gameBoardGUI.getLeaderDepot(i + 1) != null && gameBoardGUI.getLeaderDepot(i) != null) {
                                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getLeaderDepot(i + 1));
                                gameBoardGUI.setLeaderDepot(i + 1, null, 0, 0);
                            } else if (gameBoardGUI.getLeaderDepot(i) == null && gameBoardGUI.getLeaderDepot(i + 1) == null) {
                                gameBoardGUI.setLeaderDepot(i, utilsGUI.getResource(jsonArray.getJSONObject(i).get("resourceType").toString().toLowerCase()), 0, 0);
                                gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getLeaderDepot(i));
                                restored = true;
                            }
                        }
                        if (jsonArray.getJSONObject(i).getInt("quantity") == 2) {
                            int j = i;
                            if (i == 1)
                                j = 2;
                            while (j < i + 2) {
                                if (gameBoardGUI.getLeaderDepot(j) == null) {
                                    gameBoardGUI.setLeaderDepot(j, utilsGUI.getResource(jsonArray.getJSONObject(i).get("resourceType").toString().toLowerCase()), 0, 0);
                                    gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getLeaderDepot(j));
                                    restored = true;
                                }
                                j++;
                            }
                        }
                    }
                }
            } else
                for (int i = 0; i < 4; i++)
                    if (gameBoardGUI.getLeaderDepot(i) != null) {
                        gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getLeaderDepot(i));
                        gameBoardGUI.setLeaderDepot(i, null, 0, 0);
                    }
        });
    }

    @Override
    public void updateSlotStack(List<Integer> slotStackIDs) {
        Platform.runLater(() -> {
            List<ImageView> list = new ArrayList<>();
            for (Integer n : slotStackIDs) {
                if (n == -1) {
                    list.add(null);
                    continue;
                }
                list.add(developmentCardShopGUI.getDevelopmentCardImageView(n));
            }
            slotStackGUI.updateSlotStack(list);
        });
    }

    @Override
    public void updateFlagsCounter(Message message) {

    }

    @Override
    public void updateMarket(Message message) {
        Platform.runLater(() -> marketGUI.setMarketGUI(message.getMessage()));
    }

    @Override
    public void updateLorenzoFaithTrack(Message message) {
        gameBoardGUI.setLorenzoFaithMarker(Integer.parseInt(message.getMessage()));
    }

    @Override
    public void chooseInitialAction(boolean fromEndAction, boolean fromLeaderCardAction, Message message) {
        Platform.runLater(() -> {
            printGameboard();
            if (gameBoardGUI.gameboard.getChildren().contains(gameBoardGUI.getActions()[0])) {
                gameBoardGUI.gameboard.getChildren().removeAll(gameBoardGUI.getActions()[0], gameBoardGUI.getActions()[1]);
            }
            updateActivatedLeaderCard();
            if (actionCompleted) {
                lastAction(fromLeaderCardAction);
            } else {
                gameBoardGUI.showActionButton();
                Button[] actionButton = gameBoardGUI.getActions();
                legalAction = gson.fromJson(message.getMessage(), boolean[].class);
                actionButton[0].setOnAction(actionEvent -> {
                    showMarket();
                    marketGUI.getButton().setOnAction(event -> marketGUI.close());
                    marketGUI.setOnCloseRequest(event -> marketGUI.close());
                });
                actionButton[1].setOnAction(actionEvent -> {
                    showCardShop();
                    developmentCardShopGUI.getButton().setOnAction(e -> {
                        developmentCardShopGUI.resetCardShop(true);
                        developmentCardShopGUI.close();
                    });
                    developmentCardShopGUI.setOnCloseRequest((e -> {
                        developmentCardShopGUI.resetCardShop(true);
                        developmentCardShopGUI.close();
                    }));
                });
                if (!fromEndAction)
                    actionButton[2].setDisable(false);
                actionButton[3].setDisable(!legalAction[0]);
                actionButton[4].setDisable(!legalAction[1]);
                if (!fromLeaderCardAction || gameBoardGUI.countLeaderCard(true) == 2 || gameBoardGUI.countLeaderCard(false) == 2)
                    actionButton[5].setDisable(false);

                actionButton[2].setOnAction(actionEvent -> {    //take resources from market
                    if (!fromEndAction) {
                        client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 1));
                        gameBoardGUI.hideActionButton();
                    }
                });
                actionButton[3].setOnAction(actionEvent -> {    //buy development card
                    if (legalAction[0]) {
                        client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 2));
                        gameBoardGUI.hideActionButton();
                    }
                });
                actionButton[4].setOnAction(actionEvent -> {    //activate production
                    if (legalAction[1]) {
                        client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 3));
                        gameBoardGUI.hideActionButton();
                    }
                });
                actionButton[5].setOnAction(actionEvent -> {    //play a leader card
                    if (!fromLeaderCardAction) {
                        client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 4));
                        gameBoardGUI.hideActionButton();
                    }
                });
            }
        });
    }

    @Override
    public void lastAction(boolean fromLeaderCardAction) {
        Button[] actionButton = gameBoardGUI.getActions();
        gameBoardGUI.showLastActionButton();

        if (fromLeaderCardAction || gameBoardGUI.countLeaderCard(true) == 2 || gameBoardGUI.countLeaderCard(false) == 2)
            actionButton[5].setDisable(true);

        actionButton[0].setOnAction(actionEvent -> {
            showMarket();
            marketGUI.getButton().setOnAction(event -> marketGUI.close());
            marketGUI.setOnCloseRequest(event -> marketGUI.close());
        });
        actionButton[1].setOnAction(actionEvent -> {
            showCardShop();
            developmentCardShopGUI.getButton().setOnAction(e -> {
                developmentCardShopGUI.resetCardShop(true);
                developmentCardShopGUI.close();
            });
            developmentCardShopGUI.setOnCloseRequest((e -> {
                developmentCardShopGUI.resetCardShop(true);
                developmentCardShopGUI.close();
            }));
        });
        actionButton[5].setOnAction(actionEvent -> {
            if (!fromLeaderCardAction) {
                client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 4));
                gameBoardGUI.hideLastActionButton();
            }
        });
        actionButton[6].setOnAction(actionEvent -> checkEndTurn());
    }

    @Override
    public void displayLeaderCardOption(boolean[] message) {
        Button activateLeader = gameBoardGUI.getActivationLeader();
        Button discardLeader = gameBoardGUI.getDiscardLeader();
        if (message[0]) {
            activateLeader.setOpacity(1);
        } else {
            activateLeader.setOpacity(0.70);
        }
        if (message[1]) {
            discardLeader.setOpacity(1);
        } else {
            discardLeader.setOpacity(0.70);
        }

        activateLeader.setOnAction(actionEvent -> {
            askActivableCard();
            gameBoardGUI.hideLeaderCardActionButton();
        });
        discardLeader.setOnAction(actionEvent -> {
            client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_DISCARD_OPTION));
            gameBoardGUI.hideLeaderCardActionButton();
        });
        gameBoardGUI.getReturnToMenuFromLeader().setLayoutY(350);
        gameBoardGUI.getReturnToMenuFromLeader().setOnAction(actionEvent -> {
            if (this.actionCompleted) {
                chooseInitialAction(true, false, new Message(new boolean[]{false, false}));
            } else {
                chooseInitialAction(false, false, new Message(legalAction));
            }
            gameBoardGUI.getReturnToMenuFromLeader().setLayoutY(250);
            gameBoardGUI.hideLeaderCardActionButton();
        });
        Platform.runLater(gameBoardGUI::showLeaderCardActionButton);
    }

    @Override
    public void askActivableCard() {
        gameBoardGUI.getActivableLeaderY().setOnAction(actionEvent -> {
            client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_ACTIVATION_OPTION, true));
            gameBoardGUI.hideActivablesLeaderButton();
        });
        gameBoardGUI.getActivableLeaderN().setOnAction(actionEvent -> {
            client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_ACTIVATION_OPTION, false));
            gameBoardGUI.hideActivablesLeaderButton();
        });
        Platform.runLater(gameBoardGUI::showActivablesLeaderButton);
    }

    @Override
    public void showActivationLeaderCard(int[] cardsID) {
        setUpDisplayLeaderCard(cardsID);
        Platform.runLater(gameBoardGUI::showActivateLeaderButton);
        gameBoardGUI.getActivateLeader().setOnAction(actionEvent -> {
            if (gameBoardGUI.getSelectedCard() != -1) {
                resetImageViewLeaderCard();
                gameBoardGUI.hideActivateLeaderButton();
                client.sendMessage(new Message(MessageType.LEADERCARD_SELECTED_CARD, (cardsID.length == 1) ? 0 : gameBoardGUI.getSelectedCard()));
            }
        });
    }

    @Override
    public void leaderCardAlreadyActivated(boolean action) {
        displayMessage("Already activated!!");
        if (action)
            client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
        else
            askAnotherDiscardLeaderCard();
        System.out.println("already activated");
    }

    @Override
    public void leaderCardNotEnoughResources() {
        System.out.println("not enough resources");
        displayMessage("Not enough resources to activate leader");
        client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
    }

    @Override
    public void leaderCardNotEnoughColorCardQuantity() {
        displayMessage("Not enough color to activate leader");
        client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
        System.out.println("not enough color");
    }

    @Override
    public void askActivateAnotherLeaderCard() {
        gameBoardGUI.setSelectedCard(-1);
        Platform.runLater(gameBoardGUI::showActivateAnotherLeaderButton);
        gameBoardGUI.getActivateAnotherLeaderCard().setOnAction(actionEvent -> {
            askActivableCard();
            gameBoardGUI.hideActivateAnotherLeaderButton();
        });
        gameBoardGUI.getReturnToMenuFromLeader().setOnAction(actionEvent -> {
            gameBoardGUI.hideActivateAnotherLeaderButton();
            if (this.actionCompleted) {
                chooseInitialAction(true, true, new Message(new boolean[]{false, false}));
            } else {
                chooseInitialAction(false, true, new Message(legalAction));
            }
        });
    }

    private void updateActivatedLeaderCard() {
        Platform.runLater(() -> {
            for (int i = 0; i < 2; i++) {
                if (gameBoardGUI.getLeaderOption().get(i) == 1)
                    gameBoardGUI.getImageViewLeaderCard().get(i).setOpacity(1);
                else if (gameBoardGUI.getLeaderOption().get(i) == -1) {
                    gameBoardGUI.hideLeaderCard(i);
                }
            }
        });
    }

    private void setUpDisplayLeaderCard(int[] cardsID) {
        List<Integer> app = new ArrayList<>();
        for (int i : cardsID)
            app.add(i);
        if (app.contains(gameBoardGUI.getLeaderCardID().get(0))) {
            gameBoardGUI.getImageViewLeaderCard().get(0).setDisable(false);
            gameBoardGUI.getImageViewLeaderCard().get(0).setOpacity(1);
        }
        if (app.size() > 0 && app.contains(gameBoardGUI.getLeaderCardID().get(1))) {
            gameBoardGUI.getImageViewLeaderCard().get(1).setDisable(false);
            gameBoardGUI.getImageViewLeaderCard().get(1).setOpacity(1);
        }
    }

    @Override
    public void leaderCardEndAction(String message) {
        if (message != null)
            displayMessage(message);
        chooseInitialAction(actionCompleted, true, new Message(legalAction));
    }

    @Override
    public void leaderCardDiscardAction(int[] cardsID) {
        Button discardButton = gameBoardGUI.getDiscardLeader();
        gameBoardGUI.setSelectedCard(-1);
        setUpDisplayLeaderCard(cardsID);
        Platform.runLater(gameBoardGUI::showDiscardLeaderButton);
        discardButton.setOnAction(actionEvent -> {
            if (gameBoardGUI.getSelectedCard() != -1) {
                resetImageViewLeaderCard();
                gameBoardGUI.hideDiscardLeaderButton();
                client.sendMessage(new Message(MessageType.LEADERCARD_DISCARD_ACTION, (cardsID.length == 1) ? 0 : gameBoardGUI.getSelectedCard()));
            }
        });
    }

    private void resetImageViewLeaderCard() {
        for (int i = 0; i < 2; i++) {
            if (gameBoardGUI.getLeaderOption().get(i) != 1)
                gameBoardGUI.getImageViewLeaderCard().get(i).setOpacity(0.70);
            gameBoardGUI.getImageViewLeaderCard().get(i).setDisable(true);
            gameBoardGUI.getImageViewLeaderCard().get(i).setFitHeight(gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getHeight() / 3);
            gameBoardGUI.getImageViewLeaderCard().get(i).setFitWidth(gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getWidth() / 3);
            gameBoardGUI.getImageViewLeaderCard().get(i).setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);");
        }
    }

    @Override
    public void askAnotherDiscardLeaderCard() {
        gameBoardGUI.setSelectedCard(-1);
        Platform.runLater(gameBoardGUI::showDiscardAnotherLeaderButton);
        gameBoardGUI.getDiscardAnotherLeaderCard().setOnAction(actionEvent -> {
            client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_DISCARD_OPTION));
            gameBoardGUI.hideDiscardAnotherLeaderButton();
        });
        gameBoardGUI.getReturnToMenuFromLeader().setOnAction(actionEvent -> {
            gameBoardGUI.hideDiscardAnotherLeaderButton();
            if (this.actionCompleted) {
                chooseInitialAction(true, true, new Message(new boolean[]{false, false}));
            } else {
                chooseInitialAction(false, true, new Message(legalAction));
            }
        });
    }

    @Override
    public void displayProductionAction(boolean[] legalAction) {
        Platform.runLater(() -> {

            Label label = utilsGUI.createLabel("Choose which production\nto activate:");
            label.setLayoutX(1200);
            gameBoardGUI.gameboard.getChildren().add(label);

            List<Button> productionActions = utilsGUI.getProductionButtons();
            gameBoardGUI.gameboard.getChildren().addAll(productionActions);
            for (int i = 0; i < 3; i++) {
                productionActions.get(i).setDisable(!legalAction[i]);
                if (!legalAction[i]) {
                    productionActions.get(i).setOpacity(0.4);
                } else {
                    productionActions.get(i).setOpacity(1);
                }
            }
            if (!slotStackGUI.getActivableCardProduction()) {
                productionActions.get(0).setDisable(true);
                productionActions.get(0).setOpacity(0.4);
            }

            productionActions.get(0).setOnAction(e -> {
                if (legalAction[0] && slotStackGUI.getActivableCardProduction()) {
                    checkDevelopmentCardProductionSlot();
                    gameBoardGUI.gameboard.getChildren().removeAll(productionActions);
                    gameBoardGUI.gameboard.getChildren().remove(label);
                }
            });

            productionActions.get(1).setOnAction(e -> {
                if (legalAction[1]) {
                    client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD));
                    gameBoardGUI.gameboard.getChildren().removeAll(productionActions);
                    gameBoardGUI.gameboard.getChildren().remove(label);
                }
            });

            productionActions.get(2).setOnAction(e -> {
                if (legalAction[2]) {
                    client.sendMessage(new Message(MessageType.PRODUCTION_BASICPRODUCTION));
                    gameBoardGUI.gameboard.getChildren().removeAll(productionActions);
                    gameBoardGUI.gameboard.getChildren().remove(label);
                }
            });

            productionActions.get(3).setOnAction(e -> {
                client.sendMessage(new Message(MessageType.PRODUCTION_ACTIVATION));
                slotStackGUI.resetActivatedProductions();
                gameBoardGUI.gameboard.getChildren().removeAll(productionActions);
                gameBoardGUI.gameboard.getChildren().remove(label);
            });
        });
    }

    @Override
    public void callProductionAction() {
        client.sendMessage(new Message(MessageType.PRODUCTION_OPTIONS));
    }

    @Override
    public void checkDevelopmentCardProductionSlot() {
        Platform.runLater(() -> {
            Label label = utilsGUI.createLabel("Choose which development card\nto activate");
            label.setLayoutX(1195);
            gameBoardGUI.gameboard.getChildren().add(label);

            List<Button> activateSlotProduction = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                activateSlotProduction.add(utilsGUI.createButton("Activate", 30, 200, 450 + i * 220, 740));
                activateSlotProduction.get(i).setMaxWidth(45);
                if (slotStackGUI.getSlotsCounter(i) != 0 && !slotStackGUI.getActivatedProduction(i)) {
                    gameBoardGUI.gameboard.getChildren().add(activateSlotProduction.get(i));
                }
                int finalI = i;
                activateSlotProduction.get(i).setOnAction(e -> {
                    gameBoardGUI.gameboard.getChildren().removeAll(activateSlotProduction);
                    slotStackGUI.setActivatedProduction(finalI);
                    gameBoardGUI.gameboard.getChildren().remove(label);

                    client.sendMessage(new Message(MessageType.PRODUCTION_DEVELOPMENTCARD, finalI));
                });
            }
        });
    }

    @Override
    public void chooseLeaderCardProductionIndex() {
        Platform.runLater(() -> {
            Label label = utilsGUI.createLabel("Choose which leader card\nto activate");
            label.setLayoutX(1210);
            gameBoardGUI.gameboard.getChildren().add(label);

            List<Button> activateLeaderCardProduction = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                activateLeaderCardProduction.add(utilsGUI.createButton("Activate", 30, 150, 1170 + i * 175, 450));
                activateLeaderCardProduction.get(i).setMaxWidth(40);
                gameBoardGUI.gameboard.getChildren().add(activateLeaderCardProduction.get(i));
                int finalI = i;
                activateLeaderCardProduction.get(i).setOnAction(e -> {
                    gameBoardGUI.gameboard.getChildren().removeAll(activateLeaderCardProduction);
                    gameBoardGUI.gameboard.getChildren().remove(label);

                    client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_INDEX, finalI));
                });
            }
        });
    }

    @Override
    public void chooseLeaderCardProductionResourceType() {
        Platform.runLater(() -> {
            Label label = utilsGUI.createLabel("Choose the type of resources\nwhich will be produced\nby the leader card");
            label.setLayoutX(1210);
            gameBoardGUI.gameboard.getChildren().add(label);

            ComboBox<ImageView> resourceSelector = imageViewComboBox.getResourceSelector();
            resourceSelector.setLayoutX(1280);
            resourceSelector.setLayoutY(200);
            gameBoardGUI.gameboard.getChildren().add(resourceSelector);

            Button button = utilsGUI.createButton("Select", 40, 250, 1200, 360);
            gameBoardGUI.setButton(button);
            gameBoardGUI.gameboard.getChildren().add(button);
            button.setOnAction(e -> {
                gameBoardGUI.gameboard.getChildren().remove(label);
                gameBoardGUI.gameboard.getChildren().remove(button);
                gameBoardGUI.gameboard.getChildren().remove(resourceSelector);
                client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_RESOURCE_INPUT, resourceSelector.getValue().getId()));
            });

        });
    }

    @Override
    public void chooseBasicProductionResourceType() {
        Platform.runLater(() -> {
            Label label = utilsGUI.createLabel("Choose the input and output\nresources of the basic production\nfrom the gameboard");
            label.setLayoutX(1210);
            gameBoardGUI.gameboard.getChildren().add(label);

            Button button = utilsGUI.createButton("Continue", 40, 250, 1180, 360);
            gameBoardGUI.setButton(button);
            gameBoardGUI.gameboard.getChildren().add(button);

            List<ComboBox<ImageView>> resourceSelectors = new ArrayList<>();
            resourceSelectors.add(imageViewComboBox.createResourcesImageComboBox());
            resourceSelectors.add(imageViewComboBox.createResourcesImageComboBox());
            resourceSelectors.add(imageViewComboBox.createResourcesImageComboBox());
            resourceSelectors.get(0).setLayoutX(247);
            resourceSelectors.get(1).setLayoutX(247);
            resourceSelectors.get(2).setLayoutX(365);
            resourceSelectors.get(0).setLayoutY(548);
            resourceSelectors.get(1).setLayoutY(608);
            resourceSelectors.get(2).setLayoutY(590);
            gameBoardGUI.gameboard.getChildren().addAll(resourceSelectors);

            button.setOnAction(e -> {
                gameBoardGUI.gameboard.getChildren().remove(label);
                gameBoardGUI.gameboard.getChildren().remove(button);
                gameBoardGUI.gameboard.getChildren().removeAll(resourceSelectors);
                client.sendMessage(new Message(MessageType.PRODUCTION_BASIC_RESOURCES_INPUT, resourceSelectors.get(0).getValue().getId() + " "
                        + resourceSelectors.get(1).getValue().getId() + " " + resourceSelectors.get(2).getValue().getId()));
            });
        });
    }

    @Override
    public void setActionCompleted(boolean actionCompleted) {
        this.actionCompleted = actionCompleted;
    }

    @Override
    public void showMarket() {
        marketGUI.setTitle("Market");
        marketGUI.getIcons().add(new Image("img/punchboard/inkwell.png"));
        marketGUI.setResizable(false);
        marketGUI.setLight();
        marketGUI.disableMarket();
        marketGUI.show();
    }

    @Override
    public void displayPurchasedResources(String purchasedResource) {
        Platform.runLater(() -> {
            Label purchased = utilsGUI.createLabel("Purchased Resources:");
            purchased.setLayoutX(1220);
            gameBoardGUI.setLabel(purchased);
            String[] resources = purchasedResource.split(" ");
            List<ImageView> res = new ArrayList<>();
            int i = 0;
            for (String resource : resources) {
                resource = resource.toLowerCase();
                if (!resource.equalsIgnoreCase("faith") && !resource.equalsIgnoreCase("blank")) {
                    res.add(utilsGUI.getResource(resource));
                    res.get(i).setX(1280);
                    res.get(i).setY(50 + i * 100);
                    i++;
                } else {
                    FadeTransition imageTransition = new FadeTransition(Duration.seconds(0.5), gameBoardGUI.getFaithMarker());
                    imageTransition.setAutoReverse(true);
                    imageTransition.setFromValue(0);
                    imageTransition.setToValue(1);
                    imageTransition.setCycleCount(5);
                    imageTransition.play();
                }
            }
            gameBoardGUI.setPurchased(res);
            gameBoardGUI.gameboard.getChildren().add(purchased);
            gameBoardGUI.gameboard.getChildren().addAll(res);
        });
    }

    @Override
    public void chooseMarketLine() {
        Platform.runLater(() -> {
            showMarket();
            marketGUI.playMarket();
            marketGUI.setOnCloseRequest(Event::consume);
            marketGUI.getButton().setOnAction(event -> {
                marketGUI.close();
                client.sendMessage(new Message(MessageType.MARKET_CHOICE, marketGUI.getLine() + " " + marketGUI.getNumber()));
            });
        });
    }

    @Override
    public void chooseBlankAbility(int numBlank) {
        Platform.runLater(() -> {
            Popup popupBlankAbility = new Popup(400, 640);
            Label blankSelection = utilsGUI.createLabel("Click on the marble to select the Leader ability:");
            blankSelection.setLayoutX(80);
            blankSelection.setLayoutY(120);
            gameBoardGUI.setLabel(blankSelection);
            Button button = utilsGUI.createButton("Continue", 50, 250, 200, 325);
            button.setLayoutX(180);
            button.setLayoutY(330);
            popupBlankAbility.anchorPane.getChildren().add(button);
            Sphere[] blank = new Sphere[numBlank];
            int[] selections = new int[numBlank];
            PhongMaterial[] leaders = new PhongMaterial[3];
            for (int i = 0; i < 2; i++) {
                if (gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getUrl().contains("12"))
                    leaders[i] = utilsGUI.setColor("COIN");
                else if (gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getUrl().contains("13"))
                    leaders[i] = utilsGUI.setColor("SERVANT");
                else if (gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getUrl().contains("14"))
                    leaders[i] = utilsGUI.setColor("STONE");
                else if (gameBoardGUI.getImageViewLeaderCard().get(i).getImage().getUrl().contains("15"))
                    leaders[i] = utilsGUI.setColor("SHIELD");
            }
            for (int i = 0; i < numBlank; i++) {
                blank[i] = new Sphere();
                leaders[2] = new PhongMaterial();
                leaders[2].setSpecularColor(Color.WHITE);
                blank[i].setMaterial(leaders[2]);
                blank[i].translateXProperty().setValue(170 + 100 * i);
                blank[i].translateYProperty().setValue(230);
                blank[i].setRadius(35);

                int finalI = i;
                blank[i].setOnMouseClicked(mouseEvent -> {
                    blank[finalI].setMaterial(leaders[selections[finalI]]);
                    selections[finalI]++;
                    if (selections[finalI] > 1)
                        selections[finalI] = 0;
                });
            }
            int count = 0;
            for (int i = 0; i < numBlank; i++)
                if (selections[i] != 2)
                    count++;
            button.setDisable(count != numBlank);

            PointLight pointlight = new PointLight();
            pointlight.setTranslateZ(-500);
            popupBlankAbility.anchorPane.getChildren().addAll(blank);
            popupBlankAbility.anchorPane.getChildren().add(pointlight);

            popupBlankAbility.anchorPane.getChildren().add(blankSelection);
            popupBlankAbility.show();
            button.setOnAction(event -> {
                for (int i = 0; i < numBlank; i++) {
                    if (selections[i] == 0)
                        selections[i] = 2;
                    else
                        selections[i] = 1;
                }
                client.sendMessage(new Message(MessageType.BLANKMARBLE_CHOICE, selections));
                popupBlankAbility.close();
                gameBoardGUI.gameboard.getChildren().removeAll(gameBoardGUI.getPurchased());
                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getLabel());
                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getButton());
            });
        });
    }

    @Override
    public void manageStores(int numDepotLeader) {
        Platform.runLater(() -> {
            for (int i = 0; i < 6; i++) {
                if (gameBoardGUI.getWarehouse(i) != null)
                    gameBoardGUI.getWarehouse(i).setDisable(false);
                if (numDepotLeader > 0 && i < 4 && gameBoardGUI.getLeaderDepot(i) != null)
                    gameBoardGUI.getLeaderDepot(i).setDisable(false);
            }
            Button button = utilsGUI.createButton("Continue", 50, 250, 200, 325);
            button.setLayoutX(1200);
            button.setLayoutY(450);
            gameBoardGUI.setButton(button);
            gameBoardGUI.gameboard.getChildren().add(button);
            if (!gameBoardGUI.gameboard.getChildren().contains(gameBoardGUI.getLabel()))
                gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getLabel());
            List<ImageView> purchased = gameBoardGUI.getPurchased();
            if (restored) {
                for (int i = 0; i < 6; i++) {
                    if (gameBoardGUI.getWarehouse(i) != null) {
                        purchased.add(gameBoardGUI.getWarehouse(i));
                        purchased.get(purchased.size() - 1).setX(gameBoardGUI.getWarehouse(i).getTranslateX());
                        purchased.get(purchased.size() - 1).setY(gameBoardGUI.getWarehouse(i).getTranslateY());
                    }
                    if (numDepotLeader > 0 && i < 4 && gameBoardGUI.getLeaderDepot(i) != null) {
                        purchased.add(gameBoardGUI.getLeaderDepot(i));
                        purchased.get(purchased.size() - 1).setX(gameBoardGUI.getLeaderDepot(i).getTranslateX());
                        purchased.get(purchased.size() - 1).setY(gameBoardGUI.getLeaderDepot(i).getTranslateY());
                    }
                }
            }
            if (gameBoardGUI.gameboard.getChildren().containsAll(purchased)) {
                for (ImageView imageView : purchased) {
                    imageView.translateXProperty().setValue(0);
                    imageView.translateYProperty().setValue(0);
                }
            } else {
                gameBoardGUI.gameboard.getChildren().removeAll(purchased);
                gameBoardGUI.gameboard.getChildren().addAll(purchased);
            }
            double[] posImg = {0, 0};
            double[] position = {0, 0};
            for (ImageView resource : purchased) {
                resource.setOnMousePressed(e -> {
                    posImg[0] = resource.getX();
                    posImg[1] = resource.getY();
                    position[0] = e.getSceneX();
                    position[1] = e.getSceneY();
                    for (int i = 0; i < 6; i++) {
                        if (gameBoardGUI.getWarehouse(i) == resource) {
                            position[0] = resource.getX() + resource.getFitWidth() / 2;
                            position[1] = resource.getY() + resource.getFitHeight() / 2;
                        }
                        if (i < 4 && gameBoardGUI.getLeaderDepot(i) == resource) {
                            position[0] = resource.getX() + resource.getFitWidth() / 2;
                            position[1] = resource.getY() + resource.getFitHeight() / 2;
                        }
                    }
                });

                resource.setOnMouseDragged(mouseEvent -> {
                    resource.setCursor(Cursor.HAND);
                    resource.translateXProperty().setValue(mouseEvent.getSceneX() - position[0]);
                    resource.translateYProperty().setValue(mouseEvent.getSceneY() - position[1]);
                });

                resource.setOnMouseReleased(mouseEvent -> {
                    position[0] = mouseEvent.getSceneX();
                    position[1] = mouseEvent.getSceneY();

                    resource.translateXProperty().setValue(0);
                    resource.translateYProperty().setValue(0);
                    for (int i = 0; i < 6; i++) {
                        if (gameBoardGUI.getWarehouse(i) != null && gameBoardGUI.getWarehouse(i) == resource)
                            gameBoardGUI.setWarehouseDepot(i, null, 0, 0);
                        else if (i < 4 && gameBoardGUI.getLeaderDepot(i) != null && gameBoardGUI.getLeaderDepot(i) == resource)
                            gameBoardGUI.setLeaderDepot(i, null, 0, 0);
                    }
                    if (0 < position[0] && position[0] < 250) {
                        if (300 < position[1] && position[1] < 410) {
                            gameBoardGUI.setWarehouseDepot(0, resource, posImg[0], posImg[1]);
                        }
                        if (410 < position[1] && position[1] < 480) {
                            if (!gameBoardGUI.setWarehouseDepot(1, resource, posImg[0], posImg[1])) {
                                gameBoardGUI.setWarehouseDepot(2, resource, posImg[0], posImg[1]);
                            }
                        }
                        if (480 < position[1] && position[1] < 560) {
                            int i = 3;
                            while (!gameBoardGUI.setWarehouseDepot(i, resource, posImg[0], posImg[1]) || i > 5) {
                                i++;
                            }
                        }
                    }
                    if (numDepotLeader > 0 && 600 < position[1]) {
                        if (position[0] < 1350 && 800 < position[0]) {
                            if (!gameBoardGUI.setLeaderDepot(0, resource, posImg[0], posImg[1])) {
                                gameBoardGUI.setLeaderDepot(1, resource, posImg[0], posImg[1]);
                            }
                        } else if (position[0] > 1350) {
                            if (!gameBoardGUI.setLeaderDepot(2, resource, posImg[0], posImg[1])) {
                                gameBoardGUI.setLeaderDepot(3, resource, posImg[0], posImg[1]);
                            }
                        }
                    }
                });
            }
            gameBoardGUI.getButton().setOnAction(event -> {
                StringBuilder input = new StringBuilder();

                if (gameBoardGUI.countDepotResources(0).contains("0"))
                    input.append("null ");
                else
                    input.append(gameBoardGUI.countDepotResources(0)).append(" ");
                if (gameBoardGUI.countDepotResources(1).contains("0"))
                    input.append("null ");
                else
                    input.append(gameBoardGUI.countDepotResources(1)).append(" ");
                if (gameBoardGUI.countDepotResources(2).contains("0"))
                    input.append("null ");
                else
                    input.append(gameBoardGUI.countDepotResources(2)).append(" ");
                if (numDepotLeader > 0) {
                    if (gameBoardGUI.countLeaderDepotResources(0).contains("0"))
                        input.append("null ");
                    else
                        input.append(gameBoardGUI.countLeaderDepotResources(0)).append(" ");
                    if (gameBoardGUI.countLeaderDepotResources(1).contains("0"))
                        input.append("null ");
                    else
                        input.append(gameBoardGUI.countLeaderDepotResources(1)).append(" ");
                }

                boolean added;
                for (ImageView imageView : purchased) {
                    added = false;
                    for (int i = 0; i < 6; i++) {
                        if (gameBoardGUI.getWarehouse(i) != null && gameBoardGUI.getWarehouse(i).equals(imageView)) {
                            added = true;
                            break;
                        } else if (numDepotLeader > 0 && i < 4) {
                            if (gameBoardGUI.getLeaderDepot(i) != null && gameBoardGUI.getLeaderDepot(i).equals(imageView)) {
                                added = true;
                                break;
                            }
                        }
                    }
                    if (!added) {
                        gameBoardGUI.gameboard.getChildren().remove(imageView);
                    }
                }
                for (int i = 0; i < 6; i++) {
                    if (gameBoardGUI.getWarehouse(i) != null)
                        gameBoardGUI.getWarehouse(i).setDisable(true);
                    if (numDepotLeader > 0 && i < 4 && gameBoardGUI.getLeaderDepot(i) != null)
                        gameBoardGUI.getLeaderDepot(i).setDisable(true);
                }
                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getButton());
                gameBoardGUI.gameboard.getChildren().remove(gameBoardGUI.getLabel());
                client.sendMessage(new Message(MessageType.ARRANGED_RESOURCES, input.toString()));
            });
        });
    }

    @Override
    public void displayDiscardedResources(String discardedResource) {
        Platform.runLater(() -> {
            Popup popup = new Popup(400, 640);
            String[] resources = discardedResource.split(" ");
            Button button = utilsGUI.createButton("Continue", 50, 250, 200, 325);
            popup.anchorPane.getChildren().add(button);
            List<ImageView> res = new ArrayList<>();
            int i = 0;
            for (String resource : resources) {
                res.add(utilsGUI.getResource(resource));
                if (resources.length > 2)
                    res.get(i).setX(280 + i * 90);
                else
                    res.get(i).setX(360 + i * 90);
                res.get(i).setY(140);
                i++;
            }
            popup.anchorPane.getChildren().addAll(res);
            Label discarded = utilsGUI.createLabel("Discarded Resources:");
            if (resources.length > 2)
                discarded.setLayoutX(20);
            else
                discarded.setLayoutX(80);
            discarded.setLayoutY(160);
            popup.anchorPane.getChildren().add(discarded);
            popup.setTitle("Discarded Resources");
            popup.getIcons().add(new Image("img/punchboard/inkwell.png"));
            popup.setResizable(false);
            popup.show();
            button.setOnAction(event -> popup.close());
        });
    }

    public void showCardShop() {
        Platform.runLater(() -> {
            developmentCardShopGUI.showCardShop();
            developmentCardShopGUI.setResizable(false);
            developmentCardShopGUI.show();
        });
    }

    @Override
    public void chooseCardToPurchase(Integer[] cards) {
        Platform.runLater(() -> {
            if (developmentCardShopGUI.isAlreadyVisible())
                developmentCardShopGUI.resetCardShop(true);
            developmentCardShopGUI.setCards(cards, true);
            developmentCardShopGUI.setResizable(false);
            developmentCardShopGUI.setOnCloseRequest(Event::consume);
            developmentCardShopGUI.show();
            developmentCardShopGUI.getButton().setOnAction(e -> {
                int index = developmentCardShopGUI.getSelection();
                if (index != -1) {
                    client.sendMessage(new Message(MessageType.PURCHASED_CARD, cards[index]));
                    developmentCardShopGUI.resetCardShop(false);
                    developmentCardShopGUI.close();
                }
            });
        });
    }

    @Override
    public void updateDevelopmentCardDeck(Integer[] cardShop) {
        Platform.runLater(() -> developmentCardShopGUI.updateCardShop(cardShop));
    }

    @Override
    public void chooseCardSlot(List<Integer> slotList) {
        Platform.runLater(() -> {
            Button button = utilsGUI.createButton("Continue", 50, 250, 1200, 450);
            gameBoardGUI.setButton(button);
            gameBoardGUI.gameboard.getChildren().add(button);
            Label label = utilsGUI.createLabel("Choose in which slot to put\nthe purchased card:");
            label.setLayoutX(1210);
            gameBoardGUI.gameboard.getChildren().add(label);

            slotStackGUI.setImageContainersToTop();

            ImageView purchasedCard = developmentCardShopGUI.getLastPurchasedDevelopmentCard();
            purchasedCard.setLayoutX(1237);
            purchasedCard.setLayoutY(150);
            gameBoardGUI.gameboard.getChildren().add(purchasedCard);
            ImageView cardToPlace = developmentCardShopGUI.getLastPurchasedDevelopmentCard();

            List<ImageView> imageContainers = slotStackGUI.getSlots();
            purchasedCard.setOnDragDetected(e -> {
                Dragboard db = purchasedCard.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(purchasedCard.getImage());
                db.setContent(content);
                e.consume();
            });

            AtomicInteger chosenSlot = new AtomicInteger();
            chosenSlot.set(-1);
            for (int i = 0; i < 3; i++) {
                int finalI = i;

                imageContainers.get(i).setOnDragOver(e -> {
                    if (e.getDragboard().hasImage()) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    }
                    e.consume();
                });

                imageContainers.get(i).setOnDragDropped(e -> {
                    Dragboard db = e.getDragboard();
                    boolean success = false;
                    if (db.hasImage() && slotList.contains(finalI + 1)) {
                        slotStackGUI.addCard(finalI, cardToPlace);
                        chosenSlot.set(finalI);

                        success = true;
                    }
                    e.setDropCompleted(success);

                    e.consume();
                });

                purchasedCard.setOnDragDone(e -> {
                    if (e.getTransferMode() == TransferMode.MOVE) {
                        purchasedCard.setVisible(false);
                        purchasedCard.setOpacity(0);
                    }
                    e.consume();
                });
            }
            button.setOnAction(e -> {
                if (chosenSlot.get() != -1) {
                    gameBoardGUI.gameboard.getChildren().remove(label);
                    gameBoardGUI.gameboard.getChildren().remove(button);
                    client.sendMessage(new Message(MessageType.PURCHASE_CHOOSE_SLOT, chosenSlot.get()));
                }
            });
        });
    }

    /* Action Token */
    @Override
    public void showActionToken(String token) {
        if (token.equals("1"))
            gameBoardGUI.setActionToken(new Image("img/punchboard/blackCrossToken3.png"));
        if (token.equals("2"))
            gameBoardGUI.setActionToken(new Image("img/punchboard/blackCrossToken1.png"));
        if (token.equals("\"BLUE\""))
            gameBoardGUI.setActionToken(new Image("img/punchboard/discardToken1.png"));
        if (token.equals("\"GREEN\""))
            gameBoardGUI.setActionToken(new Image("img/punchboard/discardToken2.png"));
        if (token.equals("\"PURPLE\""))
            gameBoardGUI.setActionToken(new Image("img/punchboard/discardToken3.png"));
        if (token.equals("\"YELLOW\""))
            gameBoardGUI.setActionToken(new Image("img/punchboard/discardToken4.png"));
        FadeTransition imageTransition = new FadeTransition(Duration.seconds(0.5), gameBoardGUI.getActionToken());
        imageTransition.setAutoReverse(true);
        imageTransition.setFromValue(0);
        imageTransition.setToValue(1);
        imageTransition.setCycleCount(5);
        imageTransition.play();
        imageTransition.setOnFinished(event -> gameBoardGUI.setActionToken(new Image("img/punchboard/backToken.png")));
    }

    @Override
    public void setLorenzoFaithTrack() {
        Platform.runLater(gameBoardGUI::singlePlayer);
    }

    @Override
    public void endGame(String leaderBoard) {
        Platform.runLater(() -> {
            gameBoardGUI.gameboard.getChildren().removeAll(gameBoardGUI.getActions()[0], gameBoardGUI.getActions()[1]);
            Label label = utilsGUI.createLabel(leaderBoard);
            label.setLayoutX(1250);
            label.setLayoutY(80);
            gameBoardGUI.setLabel(label);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), label);
            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1.5);
            scaleTransition.setToY(1.5);
            scaleTransition.play();
            gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getLabel());
            gameBoardGUI.setButton(utilsGUI.createButton("Close", 50, 250, 1200, 450));
            gameBoardGUI.gameboard.getChildren().add(gameBoardGUI.getButton());
            gameBoardGUI.getButton().setOnAction(event -> {
                client.sendMessage(new Message(MessageType.END_GAME));
                Platform.exit();
            });
        });
    }
}