package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * author Zheyuan Zhang u6870923
 */
public class StartPage extends Application {

    private static final int GAME_WIDTH = 933;
    private static final int GAME_HEIGHT = 700;

    private static final String URI_BASE_Stage = "assets/";
    private static final String IQFOCUS_URI = StartPage.class.getResource(URI_BASE_Stage + "iqfocus.png").toString();

    /* node groups */
    private final Group root = new Group();

    @Override
    public void start(Stage stage){
        stage.setTitle("FOCUSGAME - Fun with the Tiles");
        Scene scene = new Scene(root,GAME_WIDTH,GAME_HEIGHT);

        Button startBtn = new Button("New Game");
        startBtn.setLayoutX(700);
        startBtn.setLayoutY(50);

        startBtn.setPrefSize(150,50);

        startBtn.setOnMousePressed(event -> {
                new Board().start(stage);
        });

        Label developerLabel = new Label("developers:\nZheyuan Zhang u6870923\nJianwu Yao u6987162\nSiyu Zhou u6692356");
        developerLabel.setLayoutX(700);
        developerLabel.setLayoutY(120);

        developerLabel.setPrefSize(150,100);

        root.getChildren().add(new ImageView(IQFOCUS_URI));
        root.getChildren().add(startBtn);
        root.getChildren().add(developerLabel);
        stage.setScene(scene);
        stage.show();
    }
}
