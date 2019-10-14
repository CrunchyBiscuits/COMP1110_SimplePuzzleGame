package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the IQ-Focus game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */

/**
 * author Zheyuan Zhang u6870923
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group image = new Group();

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        image.getChildren().clear();
        for(int i = 0; i<placement.length() - 3;i += 4){
            String place = placement.substring(i, i + 4);
            char tile = place.charAt(0);
            char ori = place.charAt(3);
            if (tile > 'j' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            ImageView imgView = new ImageView();
            imgView.imageProperty().set(null);

            if (tile == 'a' || tile == 'd' || tile == 'e' || tile == 'g') {
                imgView.setFitHeight(2 * SQUARE_SIZE);
                imgView.setFitWidth(3 * SQUARE_SIZE);
            } else if (tile == 'b' || tile == 'c' || tile == 'j') {
                imgView.setFitHeight(2 * SQUARE_SIZE);
                imgView.setFitWidth(4 * SQUARE_SIZE);
            } else if (tile == 'f') {
                imgView.setFitHeight(SQUARE_SIZE);
                imgView.setFitWidth(3 * SQUARE_SIZE);
            } else if (tile == 'i') {
                imgView.setFitHeight(2 * SQUARE_SIZE);
                imgView.setFitWidth(2 * SQUARE_SIZE);
            } else {
                imgView.setFitHeight(3 * SQUARE_SIZE);
                imgView.setFitWidth(3 * SQUARE_SIZE);
            }

            if (ori=='0'){
                imgView.setRotate(0);
            }else if (ori=='1'){
                imgView.setRotate(90);
            }else if (ori=='2'){
                imgView.setRotate(180);
            }else {
                imgView.setRotate(270);
            }

            double col = Character.getNumericValue(place.charAt(1));
            double row = Character.getNumericValue(place.charAt(2));
            imgView.setX(col*SQUARE_SIZE);
            imgView.setY(row*SQUARE_SIZE);


            String imgPath = ("assets/" + tile + "-0.png");
            Image img = new Image(Viewer.class.getResourceAsStream(imgPath));
            imgView.setImage(img);
            image.getChildren().add(imgView);
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {

        TextField textField = new TextField();
        textField.setPrefWidth(300);
        Button btn = new Button("Show");
        btn.setOnAction(e -> {
            makePlacement(textField.getText());
            textField.clear();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("Placement"), textField, btn);

        hBox.setLayoutX(130);
        hBox.setLayoutY(VIEWER_HEIGHT - 30);

        controls.getChildren().add(hBox);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FocusGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();
        //add the image!!!!!!!!!!!!!!!!!!!!
        root.getChildren().add(image);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
