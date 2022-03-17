package it.polimi.ingsw.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UtilsGUI {
    private final Font font = Font.loadFont(getClass().getResourceAsStream("/font/Tangerine-Bold.ttf"), 38);
    private final Font serif = Font.loadFont(getClass().getResourceAsStream("/font/Ringbearer.ttf"), 22);

    private ImageView logo = new ImageView(new Image("img/punchboard/logo.png"));
    private ImageView backgroundBeige = new ImageView(new Image("img/punchboard/background.png"));
    private ImageView backgroundBlueUp = new ImageView(new Image("img/punchboard/background1.png"));
    private ImageView backgroundBlueDown = new ImageView(new Image("img/punchboard/background2.png"));
    private List<Button> actions = new ArrayList<>();
    private List<CheckBox> checkboxes = new ArrayList<>();

    /**
     * Constructor of utilsGUI.
     */
    public UtilsGUI() {
        createProductionButtons();
    }

    /**
     * Create button.
     *
     * @param s      text
     * @param height
     * @param width
     * @param x
     * @param y
     * @return new button.
     */
    public Button createButton(String s, int height, int width, int x, int y) {
        Button button = new Button(s);
        button.setFont(serif);
        button.setMinHeight(height);
        button.setMinWidth(width);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setStyle("-fx-background-image: url('/img/punchboard/button.png');\n" +
                "    -fx-background-size: 100% 100%;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-color: transparent;\n" +
                "    -fx-text-origin: top;\n" +
                "    -fx-text-alignment: center;\n" +
                "    -fx-text-fill: #dfe3e3;\n" +
                "    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 20, 0, 0, 0);");

        button.setOnMouseEntered(e -> button.setOpacity(0.75));
        button.setOnMouseExited(e -> button.setOpacity(1));
        return button;
    }

    /**
     * Create text field.
     *
     * @return new text field.
     */
    public TextField createTextField() {
        TextField textField = new TextField();
        textField.setFont(serif);
        textField.setPrefWidth(200);
        textField.setPrefHeight(25);
        return textField;
    }

    /**
     * Create label.
     *
     * @param s text.
     * @return new label.
     */
    public Label createLabel(String s) {
        Label label = new Label(s);
        label.setFont(font);
        label.setLayoutX(120);
        label.setStyle(
                "    -fx-font-weight: bold;\n" +
                        "    -fx-effect: dropshadow(three-pass-box, #f9b924, 0.3, 0.5, 0, 0);\n" +
                        "    -fx-text-fill: #1a637d;");
        return label;
    }

    public ImageView getLogo() {
        return logo;
    }

    public ImageView getBackgroundBeige() {
        return backgroundBeige;
    }

    public ImageView getBackgroundBlueUp() {
        return backgroundBlueUp;
    }

    public ImageView getBackgroundBlueDown() {
        return backgroundBlueDown;
    }

    /**
     * Create label with font serif.
     *
     * @param s text.
     * @return new label with font serif.
     */
    public Label createLabelSerif(String s) {
        Label label = new Label(s);
        label.setFont(serif);
        label.setLayoutX(120);
        return label;
    }

    /**
     * Get image view of resource.
     *
     * @param resource string of resource.
     * @return image view of resource.
     */
    public ImageView getResource(String resource) {
        ImageView ret = new ImageView(new Image("/img/punchboard/" + resource + ".png"));
        ret.setFitHeight(80);
        ret.setFitWidth(80);
        return ret;
    }

    /**
     * Get the type of resource from the ImageView.
     *
     * @param imageView is the ImageView that represents the resource.
     * @return the String of the resource type depending on the image.
     */
    public String getResourceFromImage(ImageView imageView) {
        if (imageView.getImage().getUrl().contains("coin"))
            return "coin";
        else if (imageView.getImage().getUrl().contains("servant"))
            return "servant";
        else if (imageView.getImage().getUrl().contains("shield"))
            return "shield";
        else if (imageView.getImage().getUrl().contains("stone"))
            return "stone";
        return null;
    }

    /**
     * Get image view list of all resources.
     *
     * @return image view list of all resources.
     */
    public List<ImageView> getAllResources() {
        List<ImageView> resourceList = new ArrayList<>();
        resourceList.add(getResource("coin"));
        resourceList.get(0).setId("coin");
        resourceList.add(getResource("servant"));
        resourceList.get(1).setId("servant");
        resourceList.add(getResource("shield"));
        resourceList.get(2).setId("shield");
        resourceList.add(getResource("stone"));
        resourceList.get(3).setId("stone");
        return resourceList;
    }

    /**
     * Create production button.
     */
    private void createProductionButtons() {
        actions.add(createButton("Development Card", 50, 330, 1170, 150));
        actions.add(createButton("Leader Card", 50, 250, 1207, 250));
        actions.add(createButton("Basic production", 50, 330, 1170, 350));
        actions.add(createButton("Terminate production", 50, 370, 1150, 450));
    }

    /**
     * Method getProductionButtons.
     *
     * @return the list of the action buttons.
     */
    public List<Button> getProductionButtons() {
        return actions;
    }

    /**
     * Set color marble.
     *
     * @param input resource
     * @return Photo Material of resource.
     */
    public PhongMaterial setColor(String input) {
        PhongMaterial out = new PhongMaterial();
        switch (input) {
            case "COIN":
                out.setDiffuseColor(javafx.scene.paint.Color.rgb(253, 212, 64));
                break;
            case "SERVANT":
                out.setDiffuseColor(javafx.scene.paint.Color.rgb(124, 106, 173));
                break;
            case "SHIELD":
                out.setDiffuseColor(javafx.scene.paint.Color.rgb(83, 200, 233));
                break;
            case "STONE":
                out.setDiffuseColor(javafx.scene.paint.Color.rgb(119, 118, 113));
                break;
            case "FAITH":
                out.setDiffuseColor(javafx.scene.paint.Color.rgb(166, 28, 52));
                break;
        }
        out.setSpecularColor(Color.WHITE);
        return out;
    }
}
