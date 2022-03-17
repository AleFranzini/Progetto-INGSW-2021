package it.polimi.ingsw.view.gui;

import javafx.animation.PathTransition;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MarketGUI extends Stage {

    private final Pane pane = new Pane();
    private double positionX, positionY;
    private double dragMeasureX, dragMeasureY;
    private final Sphere resourceSlide = new Sphere();
    private final Sphere[][] marbles = new Sphere[3][4];
    private final int[] base = {210, 280, 350, 420};
    private final int[] positions = {177, 245, 315, 385, 460, 524};
    private final int maxY = 447;
    private final UtilsGUI utilsGUI = new UtilsGUI();
    private final ImageView bg = new ImageView(new Image("img/punchboard/market.png", 619, 800, false, true));
    private final PointLight pointlight = new PointLight();
    private final Button button = utilsGUI.createButton("Close", 50, 250, 180, 570);
    private final PathTransition transition = new PathTransition();

    private int number;
    private String line;

    public MarketGUI() {
        positionX = positionY = dragMeasureX = dragMeasureY = 0;
        transition.setDuration(Duration.seconds(1));
        transition.setCycleCount(2);
        transition.setAutoReverse(true);
        pane.getChildren().add(bg);
        Scene scene = new Scene(pane);
        this.setTitle("Market");
        this.setHeight(837);
        this.setWidth(633);
        this.setScene(scene);
    }

    public void playMarket() {
        resourceSlide.setDisable(false);
        button.setDisable(true);
        button.setOpacity(1);
        resourceSlide.setOnMouseDragged(mouseEvent -> {
            transition.stop();
            positionX = mouseEvent.getSceneX() - dragMeasureX;
            positionY = mouseEvent.getSceneY() - dragMeasureY;
            resourceSlide.setTranslateX(positionX);
            resourceSlide.setTranslateY(positionY);
            resourceSlide.setCursor(Cursor.HAND);
            if (positions[1] < positionY && positionY < maxY) {
                Group column = new Group();
                transition.setNode(column);
                for (int i = 0; i < 4; i++)
                    if (positions[i] < positionX && positionX < positions[i + 1]) {
                        for (int j = 0; j < 3; j++)
                            column.getChildren().add(marbles[j][i]);
                        transition.setPath(new Line(base[i], base[1], base[i], base[0]));
                    }
                pane.getChildren().add(column);
            }
            if (positions[4] < positionX && positionX < positions[5]) {
                Group row = new Group();
                transition.setNode(row);
                for (int i = 0; i < 3; i++)
                    if (positions[i] < positionY && positionY < positions[i + 1]) {
                        for (int j = 0; j < 4; j++)
                            row.getChildren().add(marbles[i][j]);
                        transition.setPath(new Line(positions[2], base[i], positions[1], base[i]));
                    }
                pane.getChildren().add(row);
            }
            transition.play();
        });


        resourceSlide.setOnMouseReleased(mouseEvent -> {
            positionX = mouseEvent.getSceneX() - dragMeasureX;
            positionY = mouseEvent.getSceneY() - dragMeasureY;
            resourceSlide.translateXProperty().setValue(480);
            resourceSlide.translateYProperty().setValue(140);
            PathTransition transition = new PathTransition();
            transition.setDuration(Duration.seconds(1));
            transition.setCycleCount(1);

            if (positions[1] < positionY && positionY < maxY) {

                Group column = new Group();
                for (int i = 0; i < 4; i++) {
                    if (positions[i] < positionX && positionX < positions[i + 1]) {
                        line = "c";
                        number = i;
                        for (int j = 0; j < 3; j++)
                            column.getChildren().add(marbles[j][i]);
                        resourceSlide.translateXProperty().setValue(base[i]);
                        resourceSlide.translateYProperty().setValue(420);
                        column.getChildren().add(resourceSlide);
                        //FIRST PART
                        transition.setNode(column);
                        transition.setPath(new Line(base[i], base[1], base[i], positions[1]));
                        transition.play();
                        int finalI = i;
                        transition.setOnFinished(Event -> {
                            //SECOND PART
                            PathTransition transition1 = new PathTransition();
                            transition1.setDuration(Duration.seconds(1));
                            transition1.setCycleCount(1);
                            transition1.setNode(marbles[0][finalI]);
                            transition1.setPath(new Line(base[finalI], base[0], 480, base[0]));
                            transition1.play();
                            transition1.setOnFinished(actionEvent -> button.setDisable(false));
                        });
                    }
                }
                pane.getChildren().add(column);
            }

            if (positions[4] < positionX && positionX < positions[5]) {
                Group row = new Group();
                for (int i = 0; i < 3; i++)
                    if (positions[i] < positionY && positionY < positions[i + 1]) {
                        line = "r";
                        number = i;
                        for (int j = 0; j < 4; j++)
                            row.getChildren().add(marbles[i][j]);
                        resourceSlide.translateXProperty().setValue(490);
                        resourceSlide.translateYProperty().setValue(base[i]);
                        row.getChildren().add(resourceSlide);
                        //FIRST PART
                        transition.setNode(row);
                        transition.setPath(new Line(positions[3], base[i], base[1], base[i]));
                        transition.play();
                        int finalI = i;
                        transition.setOnFinished(Event -> {
                            //SECOND PART
                            PathTransition transition1 = new PathTransition();
                            transition1.setDuration(Duration.seconds(1));
                            transition1.setCycleCount(1);
                            transition1.setNode(marbles[finalI][0]);
                            transition1.setPath(new Line(base[0], base[finalI], base[0], 140));
                            transition1.play();
                            transition1.setOnFinished(Event1 -> {
                                //THIRD PART
                                PathTransition transition2 = new PathTransition();
                                transition2.setDuration(Duration.seconds(1));
                                transition2.setCycleCount(1);
                                transition2.setNode(marbles[finalI][0]);
                                transition2.setPath(new Line(base[0], 140, 540, 140));
                                transition2.play();
                                transition2.setOnFinished(actionEvent -> button.setDisable(false));
                            });
                        });
                    }
                pane.getChildren().add(row);
            }
        });
    }

    /**
     * setMarketCLI creates a structure to memorize the market in local.
     * It is called at the beginning of the match and every time market is updated.
     *
     * @param json is the json String sent as a message by the EventController when it initialize the game or update the market.
     */
    public void setMarketGUI(String json) {
        pane.getChildren().clear();
        pane.getChildren().add(bg);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray marketTray = object.getJSONArray("marketTray");
        JSONObject resource = object.getJSONObject("resourceSlide");
        resourceSlide.translateXProperty().setValue(480);
        resourceSlide.translateYProperty().setValue(140);
        resourceSlide.setRadius(35);
        resourceSlide.setMaterial(utilsGUI.setColor(resource.getString("resourceType")));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++) {
                marbles[i][j] = new Sphere();
                marbles[i][j].setMaterial(utilsGUI.setColor(marketTray.getJSONArray(i).getJSONObject(j).getString("resourceType")));
                marbles[i][j].translateXProperty().setValue(base[0] + 70 * j);
                marbles[i][j].translateYProperty().setValue(base[0] + 70 * i);
                marbles[i][j].setRadius(35);
                pane.getChildren().add(marbles[i][j]);
            }
        pane.getChildren().add(resourceSlide);
        pane.getChildren().add(button);
    }

    public void disableMarket() {
        resourceSlide.setDisable(true);
    }

    public void setLight() {
        pointlight.setTranslateZ(-500);
        Group group = new Group(pointlight, resourceSlide);
        pane.getChildren().add(group);
    }

    public Button getButton() {
        return button;
    }

    public int getNumber() {
        return number;
    }

    public String getLine() {
        return line;
    }

}
