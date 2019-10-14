package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * author Zheyuan Zhang u6870923
 */
public class StartPage extends Application {

    private static final int GAME_WIDTH = 933;
    private static final int GAME_HEIGHT = 700;

    /* node groups */
    private final Group root = new Group();
    @Override
    public void start(Stage stage){
        stage.setTitle("FOCUSGAME - Fun with the Tiles");
        Scene scene = new Scene(root,GAME_WIDTH, GAME_HEIGHT);

        Button startBtn = new Button("New Game");
        startBtn.setLayoutX(300);
        startBtn.setLayoutY(300);
        startBtn.setOnMousePressed(event -> {
            new Board().start(stage);
        });
        root.getChildren().add(startBtn);
        stage.setScene(scene);
        stage.show();
    }
}
