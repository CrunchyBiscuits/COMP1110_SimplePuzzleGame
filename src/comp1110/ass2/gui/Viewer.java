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
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;
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
            char orientetion = place.charAt(3);
            if (tile > 'j' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            ImageView imageView = new ImageView();
            imageView.imageProperty().set(null);

            if (tile == 'a' || tile == 'd' || tile == 'e' || tile == 'g') {
                imageView.setFitHeight(2 * SQUARE_SIZE);
                imageView.setFitWidth(3 * SQUARE_SIZE);
            } else if (tile == 'b' || tile == 'c' || tile == 'j') {
                imageView.setFitHeight(2 * SQUARE_SIZE);
                imageView.setFitWidth(4 * SQUARE_SIZE);
            } else if (tile == 'f') {
                imageView.setFitHeight(SQUARE_SIZE);
                imageView.setFitWidth(3 * SQUARE_SIZE);
            } else if (tile == 'i') {
                imageView.setFitHeight(2 * SQUARE_SIZE);
                imageView.setFitWidth(2 * SQUARE_SIZE);
            } else {
                imageView.setFitHeight(3 * SQUARE_SIZE);
                imageView.setFitWidth(3 * SQUARE_SIZE);
            }

            switch (orientetion){
                case '0':
                    imageView.setRotate(0);
                    break;
                case '1':
                    imageView.setRotate(90);
                    break;
                case '2':
                    imageView.setRotate(180);
                    break;
                case '3':
                    imageView.setRotate(270);
                    break;
            }
            double x = Character.getNumericValue(place.charAt(1));
            double y = Character.getNumericValue(place.charAt(2));


            imageView.setX(x*SQUARE_SIZE);
            imageView.setY(y*SQUARE_SIZE);
            String imgPath = (URI_BASE + tile + ".png");
            Image img = new Image(Viewer.class.getResourceAsStream(imgPath));
            imageView.setImage(img);
            image.getChildren().add(imageView);
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
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
